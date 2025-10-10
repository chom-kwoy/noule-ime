package org.chocassye.noule;

import static org.chocassye.noule.HangulData.getDisplayComposingText;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.Handler;
import android.os.Looper;
import android.os.SystemClock;
import android.util.AttributeSet;
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

public class NouleKeyboardView extends ConstraintLayout {
    private NouleIME imeService;
    private final int INITIAL_REPEAT_INTERVAL = 400;
    private final int REPEAT_INTERVAL = 50;
    private final String[][] EN_LOWER_LAYOUT = {
        {"1", "2", "3", "4", "5", "6", "7", "8", "9", "0"},
        {"q", "w", "e", "r", "t", "y", "u", "i", "o", "p"},
        {"space-.5", "a", "s", "d", "f", "g", "h", "j", "k", "l", "space-.5"},
        {"Shift", "z", "x", "c", "v", "b", "n", "m", "Back"},
        {",", "KO", "Space", ".", "Enter"},
    };
    private final String[][] EN_UPPER_LAYOUT = {
        {"1", "2", "3", "4", "5", "6", "7", "8", "9", "0"},
        {"Q", "W", "E", "R", "T", "Y", "U", "I", "O", "P"},
        {"space-.5", "A", "S", "D", "F", "G", "H", "J", "K", "L", "space-.5"},
        {"Shift", "Z", "X", "C", "V", "B", "N", "M", "Back"},
        {",", "KO", "Space", ".", "Enter"},
    };
    private final String[][] KO_LOWER_LAYOUT = {
        {"1", "2", "3", "4", "5", "6", "7", "8", "9", "0"},
        {"ㅂ", "ㅈ", "ㄷ", "ㄱ", "ㅅ", "ㅛ", "ㅕ", "ㅑ", "ㅐ", "ㅔ"},
        {"space-.5", "ㅁ", "ㄴ", "ㅇ", "ㄹ", "ㅎ", "ㅗ", "ㅓ", "ㅏ", "ㅣ", "space-.5"},
        {"Shift", "ㅋ", "ㅌ", "ㅊ", "ㅍ", "ㅠ", "ㅜ", "ㅡ", "Back"},
        {",", "EN", "Space", ".", "Enter"},
    };
    private final String[][] KO_UPPER_LAYOUT = {
        {"1", "2", "3", "4", "5", "6", "7", "8", "9", "0"},
        {"ㅃ", "ㅉ", "ㄸ", "ㄲ", "ㅆ", "ㅛ", "ㅕ", "ㅑ", "ㅒ", "ㅖ"}, // TODO: add tone marks
        {"space-.5", "ㅿ", "ㄴ", "ㆁ", "ㄹ", "ㆆ", "ㅗ", "ㅓ", "ㆍ", "ㅣ", "space-.5"},
        {"Shift", "ㅋ", "ㅌ", "ㅊ", "ㅍ", "ㅠ", "ㅜ", "ㅡ", "Back"},
        {",", "EN", "Space", ".", "Enter"},
    };

    private String[][] curLayout = null;
    private String[][] returnToLayout = null;
    private Handler keyRepeatHandler;
    private String curComposingText = "";
    private int expectedSelEndPos = 0;

    public void initialize() {
        keyRepeatHandler = new Handler(Looper.getMainLooper());
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
        setCurKeyLayout(EN_LOWER_LAYOUT);
        this.imeService.setOnUpdateSelectionListener((oldSelStart, oldSelEnd, newSelStart, newSelEnd, candidatesStart, candidatesEnd) -> {

            // If the current selection in the text view changes, we should
            // clear whatever candidate text we have.
            if (!curComposingText.isEmpty() && (newSelStart != candidatesEnd || newSelEnd != candidatesEnd)) {
                InputConnection ic = imeService.getCurrentInputConnection();
                if (ic != null) {
                    ic.finishComposingText();
                    this.curComposingText = "";
                }
            }

            // If the content of the edittext is changed (e.g. flushed),
            // clear the composing buffer.
            if (this.expectedSelEndPos != oldSelEnd) {
                InputConnection ic = imeService.getCurrentInputConnection();
                if (ic != null && !curComposingText.isEmpty()) {
                    curComposingText = curComposingText.substring(curComposingText.length() - 1);
                    ic.setComposingText(getDisplayComposingText(curComposingText), 1);
                }
            }

            this.expectedSelEndPos = newSelEnd;
        });
    }

    @SuppressLint("ClickableViewAccessibility")
    public void setCurKeyLayout(String[][] keys) {
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
                    Button button = new Button(getContext());
                    button.setText(key);
                    button.setAllCaps(false);
                    button.setSingleLine(true);
                    button.setEllipsize(null);
                    button.setPadding(0, 0, 0, 0);
                    button.setOnTouchListener((v, event) -> {
                        if (event.getAction() == MotionEvent.ACTION_DOWN) {
                            this.onKeyPress(key, () -> {
                                v.performHapticFeedback(HapticFeedbackConstants.VIRTUAL_KEY);
                            });
                        } else if (event.getAction() == MotionEvent.ACTION_UP) {
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
                    curRow.addView(button, new LinearLayout.LayoutParams(
                        0,
                        ViewGroup.LayoutParams.MATCH_PARENT,
                        weight
                    ));
                }
            }
        }
    }

    private void typeSymbol(InputConnection ic, String key) {
        if (HangulData.consInfoMap.containsKey(key) || HangulData.vowelInfoMap.containsKey(key)) {
            curComposingText += key;
            ic.setComposingText(getDisplayComposingText(curComposingText), 1);
        }
        else {
            ic.finishComposingText();
            curComposingText = "";
            if (key.equals("Space")) {
                ic.commitText(" ", 1);
            }
            else {
                ic.commitText(key, 1);
            }
        }
    }

    private boolean doBackspace(InputConnection ic) {
        if (!curComposingText.isEmpty()) {
            curComposingText = curComposingText.substring(0, curComposingText.length() - 1);
            ic.setComposingText(getDisplayComposingText(curComposingText), 1);
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

    public void onKeyPress(String key, Runnable makeHapticFeedback) {
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
                if (returnToLayout == null) {
                    if (curLayout == EN_LOWER_LAYOUT) {
                        setCurKeyLayout(EN_UPPER_LAYOUT);
                        returnToLayout = EN_LOWER_LAYOUT;
                    }
                    else if (curLayout == KO_LOWER_LAYOUT) {
                        setCurKeyLayout(KO_UPPER_LAYOUT);
                        returnToLayout = KO_LOWER_LAYOUT;
                    }
                } else {
                    setCurKeyLayout(returnToLayout);
                    returnToLayout = null;
                }
            }
            else if (key.equals("KO")) {
                setCurKeyLayout(KO_LOWER_LAYOUT);
                returnToLayout = null;
            }
            else if (key.equals("EN")) {
                setCurKeyLayout(EN_LOWER_LAYOUT);
                returnToLayout = null;
            }
            else if (key.equals("Enter")) {
                long eventTime = SystemClock.uptimeMillis();
                // Send ACTION_DOWN for the Enter key
                ic.sendKeyEvent(new KeyEvent(eventTime, eventTime, KeyEvent.ACTION_DOWN, KeyEvent.KEYCODE_ENTER, 0));
                // Send ACTION_UP for the Enter key
                ic.sendKeyEvent(new KeyEvent(SystemClock.uptimeMillis(), eventTime, KeyEvent.ACTION_UP, KeyEvent.KEYCODE_ENTER, 0));
            }
            else {
                typeSymbol(ic, key);

                // Return back to un-shifted layout
                if (returnToLayout != null) {
                    setCurKeyLayout(returnToLayout);
                    returnToLayout = null;
                }
            }
        }
    }

    public void onKeyRelease(String key) {
        if (key.equals("Back")) {
            keyRepeatHandler.removeCallbacksAndMessages(null);
        }
    }
}
