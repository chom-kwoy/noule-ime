package org.chocassye.noule;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.Handler;
import android.os.Looper;
import android.util.AttributeSet;
import android.view.HapticFeedbackConstants;
import android.view.MotionEvent;
import android.view.ViewGroup;
import android.view.inputmethod.InputConnection;
import android.widget.Button;
import android.widget.LinearLayout;

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
        {"a", "s", "d", "f", "g", "h", "j", "k", "l"},
        {"Shift", "z", "x", "c", "v", "b", "n", "m", "Back"},
        {",", "Space", "."},
    };
    private final String[][] EN_UPPER_LAYOUT = {
        {"1", "2", "3", "4", "5", "6", "7", "8", "9", "0"},
        {"Q", "W", "E", "R", "T", "Y", "U", "I", "O", "P"},
        {"A", "S", "D", "F", "G", "H", "J", "K", "L"},
        {"Shift", "Z", "X", "C", "V", "B", "N", "M", "Back"},
        {",", "Space", "."},
    };
    private String[][] returnToLayout = null;
    private Handler keyRepeatHandler;

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
    }

    @SuppressLint("ClickableViewAccessibility")
    public void setCurKeyLayout(String[][] keys) {
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
                Button button = new Button(this.getContext());
                button.setText(key);
                button.setAllCaps(false);
                button.setOnTouchListener((v, event) -> {
                    if (event.getAction() == MotionEvent.ACTION_DOWN) {
                        this.onKeyPress(key, () -> {
                            v.performHapticFeedback(HapticFeedbackConstants.VIRTUAL_KEY);
                        });
                    }
                    else if (event.getAction() == MotionEvent.ACTION_UP) {
                        // v.performHapticFeedback(HapticFeedbackConstants.VIRTUAL_KEY_UP);
                        this.onKeyRelease(key);
                    }
                    return true;
                });
                curRow.addView(button, new LinearLayout.LayoutParams(
                    ViewGroup.LayoutParams.WRAP_CONTENT,
                    ViewGroup.LayoutParams.MATCH_PARENT,
                    1.0f
                ));
            }
        }
    }

    private static boolean doBackspace(InputConnection ic) {
        CharSequence selectedText = ic.getSelectedText(0);
        if (selectedText == null || selectedText.length() == 0) {
            return ic.deleteSurroundingText(1, 0);
        }
        else {
            return ic.commitText("", 1);
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
            else if (key.equals("Space")) {
                ic.commitText(" ", 1);
            }
            else if (key.equals("Shift")) {
                if (returnToLayout == null) {
                    setCurKeyLayout(EN_UPPER_LAYOUT);
                    returnToLayout = EN_LOWER_LAYOUT;
                } else {
                    setCurKeyLayout(returnToLayout);
                    returnToLayout = null;
                }
            }
            else {
                ic.commitText(key, 1);

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
