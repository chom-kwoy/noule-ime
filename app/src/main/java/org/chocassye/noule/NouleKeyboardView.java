package org.chocassye.noule;

import static org.chocassye.noule.HangulData.decomposeHangul;
import static org.chocassye.noule.HangulData.getDisplayComposingText;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.res.AssetManager;
import android.os.Handler;
import android.os.Looper;
import android.os.SystemClock;
import android.util.AttributeSet;
import android.util.Log;
import android.view.Gravity;
import android.view.HapticFeedbackConstants;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.ViewGroup;
import android.view.inputmethod.InputConnection;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.Space;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.button.MaterialButton;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.HashMap;
import java.util.Map;
import java.util.Vector;

public class NouleKeyboardView extends ConstraintLayout {
    private NouleIME imeService;

    private final int INITIAL_REPEAT_INTERVAL = 400;
    private final int REPEAT_INTERVAL = 50;

    public static class LayoutSet {
        public String[][] lowerLayout;
        public String[][] upperLayout;
    };

    private static final LayoutSet EN_LAYOUT = new LayoutSet();
    private static final LayoutSet KO_LAYOUT = new LayoutSet();
    static {
        EN_LAYOUT.lowerLayout = new String[][]{
            {"1", "2", "3", "4", "5", "6", "7", "8", "9", "0"},
            {"q", "w", "e", "r", "t", "y", "u", "i", "o", "p"},
            {"space-.5", "a", "s", "d", "f", "g", "h", "j", "k", "l", "space-.5"},
            {"Shift", "z", "x", "c", "v", "b", "n", "m", "Back"},
            {",", "KO", "Space", ".", "Enter"},
        };
        EN_LAYOUT.upperLayout = new String[][]{
            {"1", "2", "3", "4", "5", "6", "7", "8", "9", "0"},
            {"Q", "W", "E", "R", "T", "Y", "U", "I", "O", "P"},
            {"space-.5", "A", "S", "D", "F", "G", "H", "J", "K", "L", "space-.5"},
            {"Shift", "Z", "X", "C", "V", "B", "N", "M", "Back"},
            {",", "KO", "Space", ".", "Enter"},
        };

        KO_LAYOUT.lowerLayout = new String[][]{
            {"1", "2", "3", "4", "5", "6", "7", "8", "9", "0"},
            {"ㅂ", "ㅈ", "ㄷ", "ㄱ", "ㅅ", "ㅛ", "ㅕ", "ㅑ", "ㅐ", "ㅔ"},
            {"space-.5", "ㅁ", "ㄴ", "ㅇ", "ㄹ", "ㅎ", "ㅗ", "ㅓ", "ㅏ", "ㅣ", "space-.5"},
            {"Shift", "ㅋ", "ㅌ", "ㅊ", "ㅍ", "ㅠ", "ㅜ", "ㅡ", "Back"},
            {",", "EN", "Space", ".", "Enter"},
        };
        KO_LAYOUT.upperLayout = new String[][]{
            {"1", "2", "3", "4", "5", "6", "7", "8", "9", "0"},
            {"ㅃ", "ㅉ", "ㄸ", "ㄲ", "ㅆ", "ㅛ", "〮", "〯", "ㅒ", "ㅖ"}, // TODO: add tone marks
            {"space-.5", "ㅿ", "ㄴ", "ㆁ", "ㄹ", "ㆆ", "ㅗ", "ㅓ", "ㆍ", "ㅣ", "space-.5"},
            {"Shift", "ᄼ", "ᄾ", "ᅎ", "ᅐ", "ᅔ", "ᅕ", "ㅸ", "Back"},
            {",", "EN", "Space", ".", "Enter"},
        };
    }

    private LayoutSet curLayoutSet;
    private String[][] curLayout;

    public static class HanjaDictEntry {
        String hangul;
        String hanja;
        String[] meanings;
        int freq;
    }
    private HashMap<String, Vector<HanjaDictEntry>> hanjaDict = new HashMap<>();
    private HashMap<String, Integer> frequencyMap = new HashMap<>();
    boolean isHanjaDictInitialized = false;

    private Handler keyRepeatHandler;
    private String curComposingText = "";
    private int expectedSelEndPos = 0;
    private SuggestionAdapter suggestionAdapter;
    private boolean ignoreOnce = false;

    private void readFreqFile(String filename) throws IOException {
        AssetManager assetManager = getContext().getAssets();
        InputStream freqHanjaFile = assetManager.open(filename);
        BufferedReader reader = new BufferedReader(new InputStreamReader(freqHanjaFile));
        String line;
        while ((line = reader.readLine()) != null) {
            line = line.strip();
            if (line.startsWith("#") || line.isEmpty()) {
                continue;
            }

            String[] words = line.split(":");
            String hanja = words[0];
            int frequency = Integer.parseInt(words[1]) % 1000000;

            frequencyMap.put(hanja, frequency);
        }
        freqHanjaFile.close();
    }

    public void initialize() {
        keyRepeatHandler = new Handler(Looper.getMainLooper());

        String[] punctuations = {
            "?", "!", ":", "~", "-", "@", "#", "$", "%", "^", "&", "*",
            "(", ")", "'", "\"", "/", "\\", "|", "`", "{", "}", "[", "]",
            "<", ">", "_", "+", "=",
        };
        Vector<HanjaDictEntry> dotEntries = new Vector<>();
        for (String punct : punctuations) {
            HanjaDictEntry entry = new HanjaDictEntry();
            entry.hangul = ".";
            entry.hanja = punct;
            dotEntries.add(entry);
        }
        hanjaDict.put(".", dotEntries);

        Thread thread = new Thread(() -> {
            try {
                readFreqFile("freq-hanja.txt");
                readFreqFile("freq-hanjaeo.txt");

                AssetManager assetManager = getContext().getAssets();
                InputStream hanjaTxtFile = assetManager.open("hanja.txt");
                BufferedReader reader = new BufferedReader(new InputStreamReader(hanjaTxtFile));
                String line;
                while ((line = reader.readLine()) != null) {
                    line = line.strip();
                    if (line.startsWith("#") || line.isEmpty()) {
                        continue;
                    }

                    String[] words = line.split(":");
                    String hangul = words[0];

                    HanjaDictEntry entry = new HanjaDictEntry();
                    entry.hangul = hangul;
                    entry.hanja = words[1];
                    entry.meanings = words.length >= 3? words[2].split(", ") : null;
                    entry.freq = frequencyMap.getOrDefault(entry.hanja, 0);

                    if (!hanjaDict.containsKey(hangul)) {
                        hanjaDict.put(hangul, new Vector<>());
                    }
                    hanjaDict.get(hangul).add(entry);
                }
                hanjaTxtFile.close();

                for (Map.Entry<String, Vector<HanjaDictEntry>> item : hanjaDict.entrySet()) {
                    item.getValue().sort((a, b) -> b.freq - a.freq);
                }

                isHanjaDictInitialized = true;
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        });
        thread.start();

    }

    public NouleKeyboardView(@NonNull Context context) {
        super(context, null, R.style.Theme_Noule);
        initialize();
    }

    public NouleKeyboardView(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs, R.style.Theme_Noule);
        initialize();
    }

    public NouleKeyboardView(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, R.style.Theme_Noule);
        initialize();
    }

    public void switchLayoutSet(LayoutSet newLayoutSet) {
        this.curLayoutSet = newLayoutSet;
        setCurKeyLayout(newLayoutSet.lowerLayout);
    }

    private void finishComposing(InputConnection ic) {
        finishComposing(ic, "");
    }

    private void finishComposing(InputConnection ic, String newComposingText) {
        if (ic != null) {
            ic.finishComposingText();
            this.curComposingText = newComposingText;
            if (!newComposingText.isEmpty()) {
                ic.setComposingText(".", 1);
            }
            updateSuggestionBar();
        }
    }

    private void updateComposingText(InputConnection ic, String newComposingText) {
        if (ic != null) {
            curComposingText = newComposingText;
            ic.setComposingText(getDisplayComposingText(curComposingText), 1);
            updateSuggestionBar();
        }
    }

    private void updateSuggestionBar() {
        if (isHanjaDictInitialized && !curComposingText.isEmpty()) {
            String text = getDisplayComposingText(curComposingText);
            Vector<HanjaDictEntry> lookUp = hanjaDict.get(text);
            if (lookUp != null) {
                suggestionAdapter.setData(lookUp);
                suggestionAdapter.notifyDataSetChanged();
                return;
            }
        }
        suggestionAdapter.setData(new Vector<>());
        suggestionAdapter.notifyDataSetChanged();
    }

    public void setParentService(NouleIME ime) {
        this.imeService = ime;

        switchLayoutSet(EN_LAYOUT);

        this.imeService.setOnUpdateSelectionListener((oldSelStart, oldSelEnd, newSelStart, newSelEnd, candidatesStart, candidatesEnd) -> {

            // If the current selection in the text view changes, we should
            // clear whatever candidate text we have.
            if (!curComposingText.isEmpty() &&
                    (newSelStart != candidatesEnd || newSelEnd != candidatesEnd)) {
                if (ignoreOnce) {
                    ignoreOnce = false;
                }
                else {
                    Log.i("MYLOG", "current selection in the text view changed, clear whatever candidate text we have");
                    InputConnection ic = imeService.getCurrentInputConnection();
                    finishComposing(ic);
                }
            }

            // If the content of the edittext is changed (e.g. flushed),
            // clear the composing buffer.
            if (this.expectedSelEndPos != oldSelEnd) {
                Log.i("MYLOG", "content of the edittext is changed, clear the composing buffer");
                InputConnection ic = imeService.getCurrentInputConnection();
                if (ic != null && !curComposingText.isEmpty()) {
                    updateComposingText(ic, curComposingText.substring(curComposingText.length() - 1));
                }
            }

            this.expectedSelEndPos = newSelEnd;
        });

        RecyclerView recyclerView = findViewById(R.id.suggestionRecycler);
        LinearLayoutManager manager = new LinearLayoutManager(getContext());
        manager.setOrientation(LinearLayoutManager.HORIZONTAL);
        recyclerView.setLayoutManager(manager);
        suggestionAdapter = new SuggestionAdapter();
        recyclerView.setAdapter(suggestionAdapter);
        suggestionAdapter.setOnTouchListener((v, event, entry) -> {
            if (event.getAction() == MotionEvent.ACTION_DOWN) {
                v.setPressed(true);
                v.performHapticFeedback(HapticFeedbackConstants.VIRTUAL_KEY);
            }
            else if (event.getAction() == MotionEvent.ACTION_UP) {
                v.setPressed(false);
                InputConnection ic = imeService.getCurrentInputConnection();
                String decomposedHangul = decomposeHangul(entry.hangul);

                if (ic != null && curComposingText.startsWith(decomposedHangul)) {
                    ic.setComposingText("", 1);
                    ic.commitText(entry.hanja, 1);

                    updateComposingText(ic, curComposingText.substring(decomposedHangul.length()));
                }
            }
            else if (event.getAction() == MotionEvent.ACTION_CANCEL) {
                v.setPressed(false);
            }
            return true;
        });
    }

    @SuppressLint("ClickableViewAccessibility")
    private void setCurKeyLayout(String[][] keys) {
        curLayout = keys;

        LinearLayout[] rows = {
            this.findViewById(R.id.row0),
            this.findViewById(R.id.row1),
            this.findViewById(R.id.row2),
            this.findViewById(R.id.row3),
            this.findViewById(R.id.row4),
        };

        for (int rowIdx = 0; rowIdx < rows.length; ++rowIdx) {
            LinearLayout curRow = rows[rowIdx];
            curRow.removeAllViews();
            for (String key : keys[rowIdx]) {
                if (key.startsWith("space-")) {
                    float weight = Float.parseFloat(key.substring("space-".length()));
                    curRow.addView(new Space(getContext()), new LinearLayout.LayoutParams(
                        0,
                        ViewGroup.LayoutParams.MATCH_PARENT,
                        weight
                    ));
                }
                else {
                    Button button = new MaterialButton(getContext());
                    button.setText(key);
                    button.setAllCaps(false);
                    button.setSingleLine(true);
                    button.setEllipsize(null);
                    button.setPadding(0, 0, 0, 0);
                    button.setTextAlignment(TEXT_ALIGNMENT_CENTER);
                    button.setGravity(Gravity.CENTER);
                    button.setClickable(true);
                    button.setOnTouchListener((v, event) -> {
                        if (event.getAction() == MotionEvent.ACTION_DOWN) {
                            v.setPressed(true);
                            this.onKeyPress(key, () -> {
                                v.performHapticFeedback(HapticFeedbackConstants.VIRTUAL_KEY);
                            });
                        } else if (event.getAction() == MotionEvent.ACTION_UP) {
                            v.setPressed(false);
                            // v.performHapticFeedback(HapticFeedbackConstants.VIRTUAL_KEY_UP);
                            this.onKeyRelease(key);
                        }
                        return true;
                    });

                    float weight = 1.0f;
                    if (key.equals("Space")) {
                        weight = 5.0f;
                    } else if (key.equals("Shift") || key.equals("Back") || key.equals("Enter")) {
                        weight = 1.3f;
                    } else {
                        button.setTextSize(20);
                    }
                    LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
                        0,
                        ViewGroup.LayoutParams.MATCH_PARENT,
                        weight
                    );
                    curRow.addView(button, params);
                }
            }
        }
    }

    private void typeSymbol(InputConnection ic, String key) {
        if (HangulData.consInfoMap.containsKey(key) || HangulData.vowelInfoMap.containsKey(key)) {
            if (curComposingText.equals(".")) {
                ic.finishComposingText();
                curComposingText = "";
                ignoreOnce = true;
            }

            updateComposingText(ic, curComposingText + key);
        }
        else {
            if (key.equals(".")) {
                finishComposing(ic, ".");
                ignoreOnce = true;
            }
            else {
                finishComposing(ic);
                if (key.equals("Space")) {
                    ic.commitText(" ", 1);
                } else {
                    ic.commitText(key, 1);
                }
            }
        }
    }

    private boolean doBackspace(InputConnection ic) {
        if (!curComposingText.isEmpty()) {
            updateComposingText(ic, curComposingText.substring(0, curComposingText.length() - 1));
            return true;
        }
        else {
            CharSequence selectedText = ic.getSelectedText(0);
            if (selectedText == null || selectedText.length() == 0) {
                return ic.deleteSurroundingText(1, 0);
            } else {
                return ic.commitText("", 1);
            }
        }
    }

    private void onKeyPress(String key, Runnable makeHapticFeedback) {
        if (imeService != null) {
            InputConnection ic = imeService.getCurrentInputConnection();

            makeHapticFeedback.run();

            if (key.equals("Back")) {
                keyRepeatHandler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        boolean success = doBackspace(ic);
                        if (success) {
                            makeHapticFeedback.run();
                        }
                        keyRepeatHandler.postDelayed(this, REPEAT_INTERVAL);
                    }
                }, INITIAL_REPEAT_INTERVAL);
                doBackspace(ic);
            }
            else if (key.equals("Shift")) {
                if (curLayout == curLayoutSet.lowerLayout) {
                    setCurKeyLayout(curLayoutSet.upperLayout);
                }
                else {
                    setCurKeyLayout(curLayoutSet.lowerLayout);
                }
            }
            else if (key.equals("KO")) {
                switchLayoutSet(KO_LAYOUT);
            }
            else if (key.equals("EN")) {
                switchLayoutSet(EN_LAYOUT);
            }
            else if (key.equals("Enter")) {
                finishComposing(ic);

                long eventTime = SystemClock.uptimeMillis();
                // Send ACTION_DOWN for the Enter key
                ic.sendKeyEvent(new KeyEvent(eventTime, eventTime, KeyEvent.ACTION_DOWN, KeyEvent.KEYCODE_ENTER, 0));
                // Send ACTION_UP for the Enter key
                ic.sendKeyEvent(new KeyEvent(SystemClock.uptimeMillis(), eventTime, KeyEvent.ACTION_UP, KeyEvent.KEYCODE_ENTER, 0));
            }
            else {
                typeSymbol(ic, key);

                // Return back to un-shifted layout
                if (curLayout == curLayoutSet.upperLayout) {
                    setCurKeyLayout(curLayoutSet.lowerLayout);
                }
            }
        }
    }

    private void onKeyRelease(String key) {
        if (key.equals("Back")) {
            keyRepeatHandler.removeCallbacksAndMessages(null);
        }
    }
}
