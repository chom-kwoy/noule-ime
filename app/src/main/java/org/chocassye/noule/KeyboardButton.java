package org.chocassye.noule;

import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.GradientDrawable;
import android.os.Handler;
import android.os.Looper;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.HapticFeedbackConstants;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.google.android.material.button.MaterialButton;

import java.util.HashMap;

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
    private LinearLayout popupLinearLayout;
    private TextView popupTextView;
    private int lastSelectedIdx = -1;
    private Handler longPressHandler;
    String[] alternatives;

    private static HashMap<String, String[]> ipaMap = new HashMap<>();
    static {
//        ipaMap.put("q", new String[]{ "ʠ", "ɋ" });
//        ipaMap.put("w", new String[]{ "ʍ", "ɰ", "ʷ", "ᶭ" });
//        ipaMap.put("e", new String[]{ "ɘ", "ə", "ɚ", "ɛ", "ɜ", "ɞ", "ɝ", "ᵉ", "ᵊ", "ᵋ", "ᶟ" });
//        ipaMap.put("r", new String[]{ "ɾ", "ɽ", "ɹ", "ɻ", "ɺ", "ʀ", "ʁ", "ʳ", "ʴ", "ʵ", "ʶ" });
//        ipaMap.put("t", new String[]{ "ʈ", "θ", "ᵗ", "ᶿ" });
//        ipaMap.put("y", new String[]{ "ʏ", "ɤ", "ɥ", "ʸ", "ᶣ" });
//        ipaMap.put("u", new String[]{ "ʉ", "ɯ", "ʊ", "ᵘ", "ᶶ", "ᵚ", "ᶷ" });
//        ipaMap.put("i", new String[]{ "ɨ", "ɪ", "ⁱ", "ᶤ", "ᶦ" });
//        ipaMap.put("o", new String[]{ "ɔ", "ø", "ɵ", "œ", "ɶ", "ᵒ", "ᵓ", "ᶱ", "ꟹ" });
//        ipaMap.put("p", new String[]{ "ᵖ" });
//        ipaMap.put("a", new String[]{ "ɐ", "æ", "ɑ", "ɒ", "ᵃ", "ᵄ", "ᵅ", "ᶛ" });
//        ipaMap.put("s", new String[]{ "ʂ", "ʃ", "ˢ", "ᶳ", "ᶴ" });
//        ipaMap.put("d", new String[]{ "ɖ", "ᶑ", "ð", "ɗ", "ᵈ", "ᶞ" });
//        ipaMap.put("f", new String[]{ "ɸ", "ᶠ", "ᶲ" });
//        ipaMap.put("g", new String[]{ "ɣ", "ɢ", "g", "ɠ", "ʛ" });
//        ipaMap.put("h", new String[]{ "ɦ", "ħ", "ʜ", "ɧ", "ʰ", "ʱ" });
//        ipaMap.put("j", new String[]{ "ʝ", "ɟ", "ʄ", "ʲ", "ᶨ", "ᶡ" });
//        ipaMap.put("k", new String[]{ "ᵏ" });
//        ipaMap.put("l", new String[]{ "ɬ", "ɭ", "ꞎ", "ʟ", "ɫ", "ɮ", "ˡ", "ᶩ", "ᶫ", "ʎ" });
//        ipaMap.put("z", new String[]{ "ʐ", "ʑ", "ʒ", "ᶻ", "ᶼ", "ᶽ", "ᶾ" });
//        ipaMap.put("x", new String[]{ "ꭓ", "ˣ", "ᵡ" });
//        ipaMap.put("c", new String[]{ "ç", "ɕ", "ᶜ", "ᶝ" });
//        ipaMap.put("v", new String[]{ "ʋ", "ⱱ", "ʌ", "ᵛ", "ᶹ", "ᶺ" });
//        ipaMap.put("b", new String[]{ "ɓ", "β", "ʙ", "ᵇ", "ᵝ" });
//        ipaMap.put("n", new String[]{ "ɳ", "ɲ", "ŋ", "ɴ", "ⁿ", "ᶮ", "ᶯ", "ᵑ", "ᶰ" });
//        ipaMap.put("m", new String[]{ "ɱ", "ᵐ", "ᶬ" });
//        ipaMap.put("ʔ", new String[]{ "ʡ", "ʕ", "ʢ", "ˀ", "ˤ" });
    }

    void initialize() {
        LayoutInflater layoutInflater = LayoutInflater.from(getContext());
        View popupView = layoutInflater.inflate(R.layout.button_popup, null);
        popupLinearLayout = popupView.findViewById(R.id.linearLayout);
        popupTextView = (TextView) layoutInflater.inflate(R.layout.button_popup_item, null);
        popupLinearLayout.addView(popupTextView);
        popupWindow = new PopupWindow(popupView, 200, 100, false);
        popupWindow.setAnimationStyle(R.style.PopupWindowAnimation);
        longPressHandler = new Handler(Looper.getMainLooper());
    }

    public void dismiss() {
        popupWindow.dismiss();
    }

    @Override
    public void setPressed(boolean pressed) {
        super.setPressed(pressed);
    }

    public void setPopupBackgroundColor(Integer color) {
        if (color != null) {
            GradientDrawable drawable = new GradientDrawable();
            drawable.setShape(GradientDrawable.RECTANGLE);

            // Corner radius
            float radiusInDp = 10f;
            float radiusInPx = TypedValue.applyDimension(
                    TypedValue.COMPLEX_UNIT_DIP,
                    radiusInDp,
                    getContext().getResources().getDisplayMetrics()
            );
            drawable.setCornerRadius(radiusInPx);

            // Set background
            drawable.setColor(color);

            // Stroke
            int strokeWidthInPx = (int) TypedValue.applyDimension(
                    TypedValue.COMPLEX_UNIT_DIP,
                    2,
                    getContext().getResources().getDisplayMetrics()
            );
            drawable.setStroke(strokeWidthInPx, getCurrentTextColor());
            popupLinearLayout.setBackground(drawable);
        }
    }

    public void setTextAndPopup(CharSequence text) {
        LayoutInflater layoutInflater = LayoutInflater.from(getContext());
        setText(text);
        alternatives = ipaMap.get(text.toString());
        if (alternatives != null) {
            popupLinearLayout.removeAllViews();
            for (String alternative : alternatives) {
                popupTextView = (TextView) layoutInflater.inflate(R.layout.button_popup_item, null);
                popupTextView.setText(alternative);
                popupTextView.setTextColor(getCurrentTextColor());
                popupLinearLayout.addView(popupTextView);
            }
        } else {
            if (popupLinearLayout.getChildCount() != 1) {
                popupLinearLayout.removeAllViews();
                popupTextView = (TextView) layoutInflater.inflate(R.layout.button_popup_item, null);
                popupLinearLayout.addView(popupTextView);
            }
            popupTextView.setText(getText());
            popupTextView.setTextColor(getCurrentTextColor());
        }
    }

    public interface OnAlternativeSelectedListener {
        void onAlternativeSelectedEvent(String alternative);
    }

    private OnAlternativeSelectedListener onAlternativeSelectedListener;
    public void setOnAlternativeSelectedListener(OnAlternativeSelectedListener listener) {
        onAlternativeSelectedListener = listener;
    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent event) {
        if (event.getAction() == MotionEvent.ACTION_DOWN) {
            for (int i = 0; i < popupLinearLayout.getChildCount(); ++i) {
                TextView textView = (TextView) popupLinearLayout.getChildAt(i);
                textView.setWidth(getWidth());
            }
            popupLinearLayout.measure(View.MeasureSpec.UNSPECIFIED, View.MeasureSpec.UNSPECIFIED);
            popupWindow.setWidth(popupLinearLayout.getMeasuredWidth());
            popupWindow.setHeight(getHeight());

            int[] location = new int[2];
            this.getLocationInWindow(location);
            int popupX = location[0] - (popupWindow.getWidth() - getWidth()) / 2;
            int popupY = location[1] - popupWindow.getHeight();
            popupWindow.showAtLocation(this, Gravity.NO_GRAVITY, popupX, popupY);

            lastSelectedIdx = -1;
        } else if (event.getAction() == MotionEvent.ACTION_UP) {
            popupWindow.dismiss();
            if (lastSelectedIdx != -1 && onAlternativeSelectedListener != null) {
                if (alternatives != null && lastSelectedIdx < alternatives.length) {
                    onAlternativeSelectedListener.onAlternativeSelectedEvent(
                            alternatives[lastSelectedIdx]
                    );
                }
            }
        } else if (event.getAction() == MotionEvent.ACTION_MOVE) {
            int[] location = new int[2];
            this.getLocationOnScreen(location);

            float touchX = location[0] + event.getX();
            float touchY = location[1] + event.getY();

            int selectedIdx = -1;
            for (int i = 0; i < popupLinearLayout.getChildCount(); ++i) {
                TextView textView = (TextView) popupLinearLayout.getChildAt(i);
                int[] textViewLocation = new int[2];
                textView.getLocationOnScreen(textViewLocation);
                float startX = textViewLocation[0];
                float endX = textViewLocation[0] + textView.getWidth();

                if (selectedIdx == -1 && startX < touchX && touchX <= endX) {
                    selectedIdx = i;
                    if (lastSelectedIdx != -1 && lastSelectedIdx != selectedIdx) {
                        textView.setBackgroundResource(R.drawable.alternative_selected_bg);
                    }
                }
                else {
                    textView.setBackground(null);
                }
            }

            if (lastSelectedIdx != -1 && selectedIdx != -1
                    && selectedIdx != lastSelectedIdx) {
                this.performHapticFeedback(HapticFeedbackConstants.VIRTUAL_KEY);
            }
            lastSelectedIdx = selectedIdx;
        }
        return super.dispatchTouchEvent(event);
    }
}
