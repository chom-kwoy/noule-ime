package org.chocassye.noule;

import static org.chocassye.noule.lang.HangulData.getDisplayComposingText;
import static org.chocassye.noule.lang.HangulData.isHangulString;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.res.AssetManager;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.os.Build;
import android.os.Handler;
import android.os.Looper;
import android.os.SystemClock;
import android.util.AttributeSet;
import android.util.Log;
import android.view.Gravity;
import android.view.HapticFeedbackConstants;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputConnection;
import android.widget.LinearLayout;
import android.widget.Space;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.content.res.ResourcesCompat;
import androidx.preference.PreferenceManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import org.apache.commons.collections4.Trie;
import org.apache.commons.collections4.trie.PatriciaTrie;
import org.chocassye.noule.lang.HangulData;
import org.chocassye.noule.lang.HanjaDict;
import org.chocassye.noule.lang.ManchuData;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.HashMap;
import java.util.Map;
import java.util.SortedMap;
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
    private static final LayoutSet SPECIAL_LAYOUT = new LayoutSet();
    static {
        EN_LAYOUT.lowerLayout = new String[][]{
            {"1", "2", "3", "4", "5", "6", "7", "8", "9", "0"},
            {"q", "w", "e", "r", "t", "y", "u", "i", "o", "p"},
            {"space-.5", "a", "s", "d", "f", "g", "h", "j", "k", "l", "space-.5"},
            {"Shift", "z", "x", "c", "v", "b", "n", "m", "Back"},
            {"!#?", "KO", "Space", ".", "Enter"},
        };
        EN_LAYOUT.upperLayout = new String[][]{
            {"1", "2", "3", "4", "5", "6", "7", "8", "9", "0"},
            {"Q", "W", "E", "R", "T", "Y", "U", "I", "O", "P"},
            {"space-.5", "A", "S", "D", "F", "G", "H", "J", "K", "L", "space-.5"},
            {"Shift", "Z", "X", "C", "V", "B", "N", "M", "Back"},
            {"!#?", "KO", "Space", ".", "Enter"},
        };

        KO_LAYOUT.lowerLayout = new String[][]{
            {"1", "2", "3", "4", "5", "6", "7", "8", "9", "0"},
            {"ㅂ", "ㅈ", "ㄷ", "ㄱ", "ㅅ", "ㅛ", "ㅕ", "ㅑ", "ㅐ", "ㅔ"},
            {"space-.5", "ㅁ", "ㄴ", "ㅇ", "ㄹ", "ㅎ", "ㅗ", "ㅓ", "ㅏ", "ㅣ", "space-.5"},
            {"Shift", "ㅋ", "ㅌ", "ㅊ", "ㅍ", "ㅠ", "ㅜ", "ㅡ", "Back"},
            {"!#?", "EN", "Space", ".", "Enter"},
        };
        KO_LAYOUT.upperLayout = new String[][]{
            {"1", "2", "3", "4", "5", "6", "7", "8", "9", "0"},
            {"ㅃ", "ㅉ", "ㄸ", "ㄲ", "ㅆ", "ㅛ", "〮", "〯", "ㅒ", "ㅖ"},
            {"space-.5", "ㅿ", "ㄴ", "ㆁ", "ㄹ", "ㆆ", "ㅗ", "ㅓ", "ㆍ", "ㅣ", "space-.5"},
            {"Shift", "ᄼ", "ᄾ", "ᅎ", "ᅐ", "ᅔ", "ᅕ", "ㅸ", "Back"},
            {"!#?", "EN", "Space", ".", "Enter"},
        };

        SPECIAL_LAYOUT.lowerLayout = new String[][]{
            {"?", "!", ":", "~", "-", "@", "#", "$", "%", "^"},
            {"&", "*", "(", ")", "'", "\"", "/", "\\", "|", "`"},
            {"{", "}", "[", "]", "<", ">", "_", "+", "=", ";"},
            {"∅", "→", "「", "」", "『", "』", "·", "᠈", "᠉", "Back"},
            {"Prev", ",", "Space", ".", "Enter"},
        };
    }

    private LayoutSet curLayoutSet;
    private String[][] curLayout;
    private LayoutSet prevLayoutSet;
    private String[][] prevLayout;

    private Handler keyRepeatHandler;
    private String curComposingText = "";
    private int expectedSelEndPos = 0;
    private SuggestionAdapter suggestionAdapter;
    private boolean ignoreOnce = false;

    private Integer themeColor, backgroundColor, buttonColor;
    private String buttonStyle;

    public void initialize() {
        keyRepeatHandler = new Handler(Looper.getMainLooper());

        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(getContext());
        String themeColorStr = preferences.getString("theme_color_pref", null);
        if (themeColorStr != null) {
            themeColor = Color.parseColor(String.format("#%s", themeColorStr));
        }
        String backgroundColorStr = preferences.getString("background_color_pref", null);
        if (backgroundColorStr != null) {
            backgroundColor = Color.parseColor(String.format("#%s", backgroundColorStr));
        }
        String buttonColorStr = preferences.getString("button_color_pref", null);
        if (buttonColorStr != null) {
            buttonColor = Color.parseColor(String.format("#%s", buttonColorStr));
        }
        buttonStyle = preferences.getString("button_style_pref", "outlined");
    }

    public NouleKeyboardView(@NonNull Context context) {
        super(context);
        initialize();
    }

    public NouleKeyboardView(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        initialize();
    }

    public NouleKeyboardView(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initialize();
    }

    public void setParentService(NouleIME ime) {
        this.imeService = ime;
        if (backgroundColor != null) {
            this.setBackgroundColor(backgroundColor);
        }

        switchLayoutSet(EN_LAYOUT);

        RecyclerView recyclerView = findViewById(R.id.suggestionRecycler);

        LinearLayoutManager manager = new LinearLayoutManager(getContext());
        manager.setOrientation(LinearLayoutManager.HORIZONTAL);
        recyclerView.setLayoutManager(manager);

        suggestionAdapter = new SuggestionAdapter(
            ResourcesCompat.getFont(getContext(), R.font.manchu)
        );
        suggestionAdapter.setOnTouchListener((v, event, entry) -> {
            if (event.getAction() == MotionEvent.ACTION_DOWN) {
                v.setPressed(true);
                v.performHapticFeedback(HapticFeedbackConstants.VIRTUAL_KEY);
            }
            else if (event.getAction() == MotionEvent.ACTION_UP) {
                v.setPressed(false);
                InputConnection ic = imeService.getCurrentInputConnection();

                if (ic != null && curComposingText.startsWith(entry.input)) {
                    ic.setComposingText("", 1);
                    ic.commitText(entry.output, 1);

                    updateComposingText(ic, curComposingText.substring(entry.input.length()));
                }
            }
            else if (event.getAction() == MotionEvent.ACTION_CANCEL) {
                v.setPressed(false);
            }
            return true;
        });

        recyclerView.setAdapter(suggestionAdapter);
    }

    public void onUpdateSelection(int oldSelStart, int oldSelEnd, int newSelStart, int newSelEnd, int candidatesStart, int candidatesEnd) {
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
    }

    public void onStartInput(EditorInfo attribute, boolean restarting) {
        InputConnection ic = imeService.getCurrentInputConnection();
        finishComposing(ic);
    }

    public void onStartInputView(EditorInfo editorInfo, boolean restarting) {
        InputConnection ic = imeService.getCurrentInputConnection();
        finishComposing(ic);
    }

    public void onFinishInput() {
        InputConnection ic = imeService.getCurrentInputConnection();
        finishComposing(ic);
    }

    public void onFinishInputView(boolean finishingInput) {
        InputConnection ic = imeService.getCurrentInputConnection();
        finishComposing(ic);
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
        }
        this.curComposingText = newComposingText;
        if (ic != null) {
            if (!newComposingText.isEmpty()) {
                ic.setComposingText(newComposingText, 1);
            }
        }
        updateSuggestionBar();
    }

    private void updateComposingText(InputConnection ic, String newComposingText) {
        if (ic != null) {
            curComposingText = newComposingText;
            ic.setComposingText(getDisplayComposingText(curComposingText), 1);
            updateSuggestionBar();
        }
    }

    private void updateSuggestionBar() {
        Vector<SuggestionAdapter.SuggestionEntry> entries = null;

        if (!curComposingText.isEmpty()) {

            // Hangul -> Hanja conversion
            if (isHangulString(curComposingText) && HanjaDict.isHanjaDictInitialized()) {
                String decomposedText = HangulData.decomposeHangul(curComposingText);
                if (decomposedText.length() >= 2) {  // No point when its just one letter
                    SortedMap<String, Vector<HanjaDict.HanjaDictEntry>> prefixMap =
                            HanjaDict.hanjaDict.prefixMap(decomposedText);

                    if (!prefixMap.isEmpty()) {
                        // Collect exact entries if present
                        Vector<SuggestionAdapter.SuggestionEntry> exactEntries = new Vector<>();
                        Vector<HanjaDict.HanjaDictEntry> lookUp = HanjaDict.hanjaDict.get(decomposedText);
                        if (lookUp != null) {
                            for (HanjaDict.HanjaDictEntry entry : lookUp) {
                                SuggestionAdapter.SuggestionEntry suggestionEntry =
                                        new SuggestionAdapter.SuggestionEntry();
                                suggestionEntry.input = curComposingText;
                                suggestionEntry.output = entry.hanja;
                                suggestionEntry.freq = entry.freq;
                                exactEntries.add(suggestionEntry);
                            }
                        }

                        // Collect prefix matches and sort by frequency
                        Vector<SuggestionAdapter.SuggestionEntry> approxEntries = new Vector<>();
                        for (Map.Entry<String, Vector<HanjaDict.HanjaDictEntry>> prefixEntries : prefixMap.entrySet()) {
                            if (prefixEntries.getKey().equals(decomposedText)) {
                                continue;
                            }
                            for (HanjaDict.HanjaDictEntry entry : prefixEntries.getValue()) {
                                SuggestionAdapter.SuggestionEntry suggestionEntry =
                                        new SuggestionAdapter.SuggestionEntry();
                                suggestionEntry.input = curComposingText;
                                suggestionEntry.output = entry.hanja;
                                suggestionEntry.freq = entry.freq;
                                approxEntries.add(suggestionEntry);
                            }
                        }
                        approxEntries.sort((a, b) -> b.freq - a.freq);

                        entries = new Vector<>();
                        entries.addAll(exactEntries);
                        entries.addAll(approxEntries);
                    }
                }
            }

            // Symbols
            else if (curComposingText.equals(".")) {
                String[] punctuations = {
                    "?", ",", "!", ":", "~", "-", "@", "%", "^", "&", "*",
                    "(", ")", "'", "/", "<", ">", "_", "+", "=",
                };
                entries = new Vector<>();
                for (String punct : punctuations) {
                    SuggestionAdapter.SuggestionEntry suggestionEntry =
                            new SuggestionAdapter.SuggestionEntry();
                    suggestionEntry.input = curComposingText;
                    suggestionEntry.output = punct;
                    entries.add(suggestionEntry);
                }
            }

            // Latin -> Manchu conversion
            else if (isAlphabetic(curComposingText)) {
                entries = new Vector<>();
                SuggestionAdapter.SuggestionEntry suggestionEntry =
                        new SuggestionAdapter.SuggestionEntry();
                suggestionEntry.input = curComposingText;
                suggestionEntry.output = ManchuData.convertToManchu(curComposingText);
                entries.add(suggestionEntry);
            }
        }

        suggestionAdapter.setData(entries);
        suggestionAdapter.notifyDataSetChanged();
    }

    @SuppressLint("ClickableViewAccessibility")
    private void setCurKeyLayout(String[][] keys) {
        LayoutInflater inflater = (LayoutInflater) getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);

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
            if (curRow.getChildCount() != keys[rowIdx].length) {
                curRow.removeAllViews();
                for (String key : keys[rowIdx]) {
                    if (key.startsWith("space-")) {
                        curRow.addView(new Space(getContext()), new LinearLayout.LayoutParams(
                            0,
                            ViewGroup.LayoutParams.MATCH_PARENT,
                            1.0f
                        ));
                    }
                    else {
                        int layout = R.layout.keyboard_key_outlined;
                        switch (buttonStyle) {
                            case "flat":
                                layout = R.layout.keyboard_key_flat;
                                break;
                            case "filled":
                                layout = R.layout.keyboard_key_filled;
                                break;
                        }
                        KeyboardButton button = (KeyboardButton) inflater.inflate(
                                layout, null, false);
                        if (themeColor != null) {
                            button.setTextColor(themeColor);
                        }
                        if (backgroundColor != null) {
                            button.setPopupBackgroundColor(backgroundColor);
                        }
                        if (buttonColor != null) {
                            switch (buttonStyle) {
                                case "flat":
                                case "outlined":
                                    button.setRippleColor(ColorStateList.valueOf(
                                        0x1f000000 | (buttonColor & 0xffffff)
                                    ));
                                    break;
                                case "filled":
                                    button.setBackgroundTintList(ColorStateList.valueOf(buttonColor));
                                    break;
                            }
                        }
                        curRow.addView(button, new LinearLayout.LayoutParams(
                            0,
                            ViewGroup.LayoutParams.MATCH_PARENT,
                            1.0f
                        ));
                    }
                }
            }
            int index = 0;
            for (String key : keys[rowIdx]) {
                if (key.startsWith("space-")) {
                    float weight = Float.parseFloat(key.substring("space-".length()));
                    Space space = (Space) curRow.getChildAt(index);
                    space.setLayoutParams(new LinearLayout.LayoutParams(
                        0,
                        ViewGroup.LayoutParams.MATCH_PARENT,
                        weight
                    ));
                }
                else {
                    KeyboardButton button = (KeyboardButton) curRow.getChildAt(index);
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
                            v.performClick();
                            v.setPressed(false);
                            if (Build.VERSION.SDK_INT >= 27) {
                                v.performHapticFeedback(HapticFeedbackConstants.VIRTUAL_KEY_RELEASE);
                            }
                            this.onKeyRelease(key);
                        }
                        return true;
                    });

                    button.setTextSize(15);

                    float weight = 1.0f;
                    if (key.equals("Space")) {
                        weight = 5.0f;
                    } else if (key.length() > 3) {
                        weight = 1.3f;
                    } else {
                        button.setTextSize(20);
                    }
                    button.setLayoutParams(new LinearLayout.LayoutParams(
                        0,
                        ViewGroup.LayoutParams.MATCH_PARENT,
                        weight
                    ));
                }
                index += 1;
            }
        }
    }

    public static boolean isAlphabetic(String s) {
        if (s == null || s.isEmpty()) {
            return false;
        }
        return s.matches("[a-zA-Z]+");
    }

    private void typeSymbol(InputConnection ic, String key) {
        if (HangulData.consInfoMap.containsKey(key) || HangulData.vowelInfoMap.containsKey(key)) {
            if (!HangulData.isHangulString(curComposingText)) {
                finishComposing(ic, key);
                ignoreOnce = true;
            }
            else {
                updateComposingText(ic, curComposingText + key);
            }
        }
        else if (key.length() == 1 && isAlphabetic(key)) {
            if (!curComposingText.isEmpty() && !isAlphabetic(curComposingText)) {
                finishComposing(ic, key);
                ignoreOnce = true;
            }
            else {
                updateComposingText(ic, curComposingText + key);
            }
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
            else if (key.equals("!#?")) {
                prevLayoutSet = curLayoutSet;
                prevLayout = curLayout;
                switchLayoutSet(SPECIAL_LAYOUT);
            }
            else if (key.equals("Prev")) {
                switchLayoutSet(prevLayoutSet);
                setCurKeyLayout(prevLayout);
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
