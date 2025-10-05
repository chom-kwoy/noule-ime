package org.chocassye.noule;

import android.annotation.SuppressLint;
import android.content.Context;
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

    public NouleKeyboardView(@NonNull Context context) {
        super(context);
    }

    public NouleKeyboardView(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    public NouleKeyboardView(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @SuppressLint("ClickableViewAccessibility")
    public void setParentService(NouleIME ime) {
        this.imeService = ime;

        LinearLayout[] rows = {
            this.findViewById(R.id.row0),
            this.findViewById(R.id.row1),
            this.findViewById(R.id.row2),
            this.findViewById(R.id.row3),
            this.findViewById(R.id.row4),
        };

        String[][] keys = {
            {"1", "2", "3", "4", "5", "6", "7", "8", "9", "0"},
            {"Q", "W", "E", "R", "T", "Y", "U", "I", "O", "P"},
            {"A", "S", "D", "F", "G", "H", "J", "K", "L"},
            {"Shift", "Z", "X", "C", "V", "B", "N", "M", "Back"},
            {",", "Space", "."},
        };

        for (int rowIdx = 0; rowIdx < rows.length; ++rowIdx) {
            LinearLayout curRow = rows[rowIdx];
            for (String key : keys[rowIdx]) {
                Button button = new Button(this.getContext());
                button.setText(key);
                button.setAllCaps(false);
                button.setOnTouchListener((v, event) -> {
                    if (event.getAction() == MotionEvent.ACTION_DOWN) {
                        v.performHapticFeedback(HapticFeedbackConstants.VIRTUAL_KEY);
                        this.onKeyPress(key);
                    }
                    else if (event.getAction() == MotionEvent.ACTION_UP) {
//                        v.performHapticFeedback(HapticFeedbackConstants.VIRTUAL_KEY_UP);
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

    public void onKeyPress(String key) {
        if (imeService != null) {
            InputConnection ic = imeService.getCurrentInputConnection();

            if (key.equals("Back")) {
                CharSequence selectedText = ic.getSelectedText(0);
                if (selectedText == null || selectedText.length() == 0) {
                    ic.deleteSurroundingText(1, 0);
                }
                else {
                    ic.commitText("", 1);
                }
            }
            else if (key.equals("Space")) {
                ic.commitText(" ", 1);
            }
            else {
                ic.commitText(key, 1);
            }
        }
    }
}
