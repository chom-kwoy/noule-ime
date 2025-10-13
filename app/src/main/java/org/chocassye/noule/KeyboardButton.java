package org.chocassye.noule;

import android.content.Context;
import android.graphics.Typeface;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.widget.PopupWindow;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.google.android.material.button.MaterialButton;

public class KeyboardButton extends MaterialButton {
    public KeyboardButton(@NonNull Context context) {
        super(context);
        initialize();
    }

    public KeyboardButton(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        initialize();
    }

    public KeyboardButton(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initialize();
    }

    private PopupWindow popupWindow;
    private TextView textView;

    void initialize() {
        LayoutInflater layoutInflater = LayoutInflater.from(getContext());
        View view = layoutInflater.inflate(R.layout.button_popup, null);
        textView = view.findViewById(R.id.textView);
        popupWindow = new PopupWindow(
            view, 200, 100, false
        );
        popupWindow.setAnimationStyle(R.style.PopupWindowAnimation);
    }

    @Override
    public void setPressed(boolean pressed) {
        super.setPressed(pressed);
    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent event) {
        if (event.getAction() == MotionEvent.ACTION_DOWN) {
            popupWindow.setWidth(getWidth());
            popupWindow.setHeight(getHeight());
            textView.setText(getText());
            int[] location = new int[2];
            this.getLocationInWindow(location);
            int popupX = location[0] - (popupWindow.getWidth() - getWidth()) / 2;
            int popupY = location[1] - popupWindow.getHeight();
            popupWindow.showAtLocation(this, Gravity.NO_GRAVITY, popupX, popupY);
        } else if (event.getAction() == MotionEvent.ACTION_UP) {
            popupWindow.dismiss();
        }
        return super.dispatchTouchEvent(event);
    }
}
