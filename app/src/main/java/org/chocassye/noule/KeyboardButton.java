package org.chocassye.noule;

import android.content.Context;
import android.graphics.Typeface;
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
import android.view.ViewConfiguration;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import android.media.AudioManager;

import com.google.android.material.button.MaterialButton;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

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

    private PopupWindow popupWindowSingle, popupWindowMultiple;
    private int lastSelectedIdx = -1;
    private boolean skipNextSelectionHaptic = false;
    private static Typeface charisFace;
    String[] alternatives;
    boolean isLongClicked = false;
    float lastX, lastY;

    Handler handler;
    private Runnable longPressRunnable;

    private static final int MAX_PER_ROW = 6;

    private static HashMap<String, String[]> ipaMap = new HashMap<>();
    static {
        ipaMap.put(".", new String[]{ ",", "?", "!", ":" });
        // IPA
        ipaMap.put("a", new String[]{ "ɐ", "ɑ", "ɒ", "æ" });
        ipaMap.put("b", new String[]{ "ʙ", "ɓ", "β" });
        ipaMap.put("c", new String[]{ "ƈ", "ç", "ʗ", "ɕ" });
        ipaMap.put("d", new String[]{ "ᶑ", "ɗ", "ð", "ɖ", "ʣ", "ʤ", "ʥ" });
        ipaMap.put("e", new String[]{ "ɝ", "ə", "ɚ", "ɛ", "ɜ", "ɞ", "ʚ", "ɘ" });
        ipaMap.put("f", new String[]{ "ǀ", "ǁ", "ǂ", "ǃ", "ʘ", "ɸ" });
        ipaMap.put("g", new String[]{ "ɢ", "ɠ", "ɣ", "ɡ", "ʛ", "ɤ" });
        ipaMap.put("h", new String[]{ "ʜ", "ɥ", "ɦ", "ɧ", "ħ", "ʮ", "ʯ" });
        ipaMap.put("i", new String[]{ "ɪ", "ɩ", "ɨ", "ˑ", "ː" });
        ipaMap.put("j", new String[]{ "ʝ", "ɟ", "ʄ", "‿" });
        ipaMap.put("k", new String[]{ "ʞ", "ƙ" });
        ipaMap.put("l", new String[]{ "ʟ", "ƛ", "ɬ", "λ", "ɭ", "ɫ", "ȴ", "ɮ" });
        ipaMap.put("m", new String[]{ "ɯ", "ɱ", "ɰ" });
        ipaMap.put("n", new String[]{ "ɴ", "ɳ", "ŋ", "ƞ", "ȵ", "ɲ" });
        ipaMap.put("o", new String[]{ "ɶ", "ɷ", "ɔ", "ɵ", "ø", "œ" });
        ipaMap.put("p", new String[]{ "ƥ" });
        ipaMap.put("q", new String[]{ "ʠ", "ʔ", "ʕ", "ʡ", "ʢ", "ʖ" });
        ipaMap.put("r", new String[]{ "ɿ", "ʀ", "ɹ", "ɾ", "ʁ", "ɽ", "ɻ", "ɺ", "ɼ" });
        ipaMap.put("s", new String[]{ "ʅ", "ʃ", "ʂ", "ʆ" });
        ipaMap.put("t", new String[]{ "ʇ", "ƭ", "θ", "ʈ", "ƫ", "ȶ" });
        ipaMap.put("u", new String[]{ "ʊ", "ʉ", "ˌ", "ˈ" });
        ipaMap.put("v", new String[]{ "ʌ", "ⱱ", "ʋ", "|", "‖" });
        ipaMap.put("w", new String[]{ "ʍ" });
        ipaMap.put("x", new String[]{ "χ", "ʦ", "ʧ", "ʨ" });
        ipaMap.put("y", new String[]{ "ʏ", "ʎ" });
        ipaMap.put("z", new String[]{ "ʒ", "ʐ", "ƻ", "ʑ", "ʓ" });
        // Show superscript IPA on caps
        ipaMap.put("Q", new String[]{ "ˀ", "ˤ" });
        ipaMap.put("W", new String[]{ "ʷ", "ᶭ" });
        ipaMap.put("E", new String[]{ "ᵉ", "ᵊ", "ᵋ", "ᶟ" });
        ipaMap.put("R", new String[]{ "ʳ", "ʴ", "ʵ", "ʶ" });
        ipaMap.put("T", new String[]{ "ᵗ", "ᶿ" });
        ipaMap.put("Y", new String[]{ "ʸ", "ᶣ" });
        ipaMap.put("U", new String[]{ "ᵘ", "ᶶ", "ᵚ", "ᶷ" });
        ipaMap.put("I", new String[]{ "ⁱ", "ᶤ", "ᶦ" });
        ipaMap.put("O", new String[]{ "ᵒ", "ᵓ", "ᶱ", "ꟹ" });
        ipaMap.put("P", new String[]{ "ᵖ" });
        ipaMap.put("A", new String[]{ "ᵃ", "ᵄ", "ᵅ", "ᶛ" });
        ipaMap.put("S", new String[]{ "ˢ", "ᶳ", "ᶴ" });
        ipaMap.put("D", new String[]{ "ᵈ", "ᶞ" });
        ipaMap.put("F", new String[]{ "ᶠ", "ᶲ" });
        ipaMap.put("G", new String[]{ "ˠ" });
        ipaMap.put("H", new String[]{ "ʰ", "ʱ", "ꟸ" });
        ipaMap.put("J", new String[]{ "ʲ", "ᶨ", "ᶡ" });
        ipaMap.put("K", new String[]{ "ᵏ" });
        ipaMap.put("L", new String[]{ "ˡ", "ᶩ", "ᶫ" });
        ipaMap.put("Z", new String[]{ "ᶻ", "ᶼ", "ᶽ", "ᶾ" });
        ipaMap.put("X", new String[]{ "ˣ", "ᵡ" });
        ipaMap.put("C", new String[]{ "ᶜ", "ᶝ" });
        ipaMap.put("V", new String[]{ "ᵛ", "ᶹ", "ᶺ" });
        ipaMap.put("B", new String[]{ "ᵇ", "ᵝ" });
        ipaMap.put("N", new String[]{ "ⁿ", "ᶮ", "ᶯ", "ᵑ", "ᶰ" });
        ipaMap.put("M", new String[]{ "ᵐ", "ᶬ" });
        // IPA diacritics
        ipaMap.put("1", new String[]{ "◌̀", "◌̄", "◌́", "◌̏", "◌̋" });
        ipaMap.put("2", new String[]{ "◌̌", "◌̂", "◌᷄", "◌᷅", "◌᷈", "◌᷉" });
        ipaMap.put("3", new String[]{ "◌̊", "◌̥", "◌̬", "◌̤", "◌̰" });
        ipaMap.put("4", new String[]{ "◌̙", "◌̘", "◌̝", "◌̞", "◌̜", "◌̹" });
        ipaMap.put("5", new String[]{ "◌̈", "◌̽", "◌̺", "◌̪" });
        ipaMap.put("6", new String[]{ "◌̃", "◌˞", "◌̢", "◌̴" });
        ipaMap.put("7", new String[]{ "◌̩", "◌̆", "◌̯" });
        ipaMap.put("8", new String[]{ "◌͡" });
        ipaMap.put("9", new String[]{ "◌̚" });
        ipaMap.put("0", new String[]{ "◌̇", "◌̣", "◌̗", "◌̑", "◌̫" });
    }

    void initialize() {
        LayoutInflater layoutInflater = LayoutInflater.from(getContext());

        // Layout for single letter
        View popupSingle = layoutInflater.inflate(R.layout.button_popup, null);
        LinearLayout popupSingleLinearLayout = popupSingle.findViewById(R.id.linearLayout);
        TextView popupTextView = (TextView) layoutInflater.inflate(R.layout.button_popup_item, null);
        popupSingleLinearLayout.addView(popupTextView);

        popupWindowSingle = new PopupWindow(popupSingle, 200, 100, false);
        popupWindowSingle.setAnimationStyle(R.style.PopupWindowAnimation);
        popupWindowSingle.setTouchable(false);

        // Layout for multiple candidates, shown when long pressed
        View popupMultiple = layoutInflater.inflate(R.layout.button_popup, null);
        popupWindowMultiple = new PopupWindow(popupMultiple, 200, 100, false);
        popupWindowMultiple.setAnimationStyle(R.style.PopupWindowAnimation);
        popupWindowMultiple.setTouchable(false);

        handler = new Handler(Looper.getMainLooper());
        longPressRunnable = () -> {
            if (alternatives != null) {
                isLongClicked = true;
                lastSelectedIdx = -1;
                skipNextSelectionHaptic = true;
                popupWindowSingle.dismiss();
                showPopup(popupWindowMultiple);
                doHapticFeedback(HapticFeedbackConstants.LONG_PRESS);
                handler.postDelayed(() -> handleTouchMoveEvent(lastX, lastY), 50);
            }
        };
    }

    public void dismissPopups() {
        popupWindowSingle.dismiss();
        popupWindowMultiple.dismiss();
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

            LinearLayout popupSingleLinearLayout =
                    popupWindowSingle.getContentView().findViewById(R.id.linearLayout);
            popupSingleLinearLayout.setBackground(drawable);

            LinearLayout popupMultipleLinearLayout =
                    popupWindowMultiple.getContentView().findViewById(R.id.linearLayout);
            popupMultipleLinearLayout.setBackground(drawable);
        }
    }

    public void setTextAndPopup(CharSequence text) {
        // Set button text itself
        setText(text);

        // Set single popup view
        LinearLayout popupSingleLinearLayout =
                popupWindowSingle.getContentView().findViewById(R.id.linearLayout);
        TextView popupTextView = (TextView) popupSingleLinearLayout.getChildAt(0);
        popupTextView.setText(getText());
        popupTextView.setTextColor(getCurrentTextColor());

        // Set candidates popup view
        LinearLayout popupMultipleLinearLayout =
                popupWindowMultiple.getContentView().findViewById(R.id.linearLayout);
        popupMultipleLinearLayout.removeAllViews();

        alternatives = ipaMap.get(text.toString());
        if (alternatives != null) {
            LayoutInflater layoutInflater = LayoutInflater.from(getContext());
            boolean isDiacritic = text.length() == 1 && Character.isDigit(text.charAt(0));
            if (isDiacritic && charisFace == null) {
                charisFace = Typeface.createFromAsset(getContext().getAssets(), "Charis-Regular.ttf");
            }
            if (alternatives.length <= MAX_PER_ROW) {
                popupMultipleLinearLayout.setOrientation(LinearLayout.HORIZONTAL);
                for (String alternative : alternatives) {
                    popupTextView = (TextView) layoutInflater.inflate(R.layout.button_popup_item, null);
                    popupTextView.setText(alternative);
                    popupTextView.setTextColor(getCurrentTextColor());
                    if (isDiacritic) popupTextView.setTypeface(charisFace);
                    popupMultipleLinearLayout.addView(popupTextView);
                }
            } else {
                popupMultipleLinearLayout.setOrientation(LinearLayout.VERTICAL);
                int row1Count = (alternatives.length + 1) / 2;
                int[] splits = {0, row1Count, alternatives.length};
                for (int r = 0; r < 2; r++) {
                    LinearLayout row = new LinearLayout(getContext());
                    row.setOrientation(LinearLayout.HORIZONTAL);
                    for (int i = splits[r]; i < splits[r + 1]; i++) {
                        popupTextView = (TextView) layoutInflater.inflate(R.layout.button_popup_item, null);
                        popupTextView.setText(alternatives[i]);
                        popupTextView.setTextColor(getCurrentTextColor());
                        if (isDiacritic) popupTextView.setTypeface(charisFace);
                        row.addView(popupTextView);
                    }
                    popupMultipleLinearLayout.addView(row);
                }
            }
        }
    }

    private void setPopupSizes(PopupWindow popupWindow, LinearLayout container) {
        boolean isMultiRow = container.getChildCount() > 0
                && container.getChildAt(0) instanceof LinearLayout;
        if (isMultiRow) {
            for (int i = 0; i < container.getChildCount(); i++) {
                LinearLayout row = (LinearLayout) container.getChildAt(i);
                for (int j = 0; j < row.getChildCount(); j++) {
                    ((TextView) row.getChildAt(j)).setWidth(getWidth());
                }
            }
        } else {
            for (int i = 0; i < container.getChildCount(); i++) {
                ((TextView) container.getChildAt(i)).setWidth(getWidth());
            }
        }
        container.measure(View.MeasureSpec.UNSPECIFIED, View.MeasureSpec.UNSPECIFIED);
        popupWindow.setWidth(container.getMeasuredWidth());
        popupWindow.setHeight(getHeight() * (isMultiRow ? container.getChildCount() : 1));
    }

    private void doHapticFeedback(int constant) {
        AudioManager am = (AudioManager) getContext().getSystemService(android.content.Context.AUDIO_SERVICE);
        if (am == null || am.getRingerMode() != AudioManager.RINGER_MODE_SILENT) {
            performHapticFeedback(constant);
        }
    }

    private List<TextView> getMultiplePopupTextViews() {
        List<TextView> result = new ArrayList<>();
        LinearLayout container = popupWindowMultiple.getContentView().findViewById(R.id.linearLayout);
        for (int i = 0; i < container.getChildCount(); i++) {
            View child = container.getChildAt(i);
            if (child instanceof LinearLayout) {
                LinearLayout row = (LinearLayout) child;
                for (int j = 0; j < row.getChildCount(); j++) {
                    result.add((TextView) row.getChildAt(j));
                }
            } else if (child instanceof TextView) {
                result.add((TextView) child);
            }
        }
        return result;
    }

    public interface OnAlternativeSelectedListener {
        void onAlternativeSelectedEvent(String alternative);
    }

    private OnAlternativeSelectedListener onAlternativeSelectedListener;
    public void setOnAlternativeSelectedListener(OnAlternativeSelectedListener listener) {
        onAlternativeSelectedListener = listener;
    }

    private void showPopup(PopupWindow popupWindow) {
        LinearLayout container = popupWindow.getContentView().findViewById(R.id.linearLayout);
        setPopupSizes(popupWindow, container);

        int[] location = new int[2];
        this.getLocationInWindow(location);
        int popupX = location[0] - (popupWindow.getWidth() - getWidth()) / 2;
        // The LinearLayout is centered inside the ConstraintLayout that fills the popup window.
        // Content bottom = popupY + (windowHeight + contentHeight) / 2.
        // Solve for popupY so content bottom sits at the key top edge (location[1]).
        int popupY = location[1] - (popupWindow.getHeight() + container.getMeasuredHeight()) / 2;
        popupWindow.showAtLocation(this, Gravity.NO_GRAVITY, popupX, popupY);
    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent event) {
        lastX = event.getX();
        lastY = event.getY();

        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                // Cancel any stale long-press timer from a previous tap before starting
                // a new one — this is the main guard against false triggers during rapid tapping.
                handler.removeCallbacksAndMessages(null);
                showPopup(popupWindowSingle);
                isLongClicked = false;
                handler.postDelayed(longPressRunnable, ViewConfiguration.getLongPressTimeout());
                break;

            case MotionEvent.ACTION_UP:
                handler.removeCallbacksAndMessages(null);
                dismissPopups();
                if (isLongClicked) {
                    if (lastSelectedIdx != -1 && onAlternativeSelectedListener != null) {
                        if (alternatives != null && lastSelectedIdx < alternatives.length) {
                            onAlternativeSelectedListener.onAlternativeSelectedEvent(
                                    alternatives[lastSelectedIdx]);
                        }
                    }
                    isLongClicked = false;
                    // Send CANCEL instead of UP so the touch listener's performClick()
                    // doesn't fire alongside the alternative selection.
                    MotionEvent cancel = MotionEvent.obtain(event);
                    cancel.setAction(MotionEvent.ACTION_CANCEL);
                    boolean result = super.dispatchTouchEvent(cancel);
                    cancel.recycle();
                    return result;
                }
                isLongClicked = false;
                break;

            case MotionEvent.ACTION_CANCEL:
                handler.removeCallbacksAndMessages(null);
                dismissPopups();
                isLongClicked = false;
                break;

            case MotionEvent.ACTION_MOVE:
                handleTouchMoveEvent(event.getX(), event.getY());
                break;
        }

        return super.dispatchTouchEvent(event);
    }

    private void handleTouchMoveEvent(float x, float y) {
        if (!isLongClicked) {
            return;
        }

        int[] location = new int[2];
        this.getLocationOnScreen(location);

        float touchX = location[0] + x;
        float touchY = location[1] + y - getHeight();

        // Pick the item whose bounding box is closest to the touch point so that
        // sliding outside the popup still keeps the nearest item selected.
        int selectedIdx = -1;
        float minDist = Float.MAX_VALUE;
        List<TextView> textViews = getMultiplePopupTextViews();
        for (int i = 0; i < textViews.size(); i++) {
            TextView textView = textViews.get(i);
            int[] tvLocation = new int[2];
            textView.getLocationOnScreen(tvLocation);

            float startX = tvLocation[0];
            float endX = tvLocation[0] + textView.getWidth();
            float startY = tvLocation[1];
            float endY = tvLocation[1] + textView.getHeight();

            float dist = squaredDistToRect(touchX, touchY, startX, startY, endX, endY);
            if (dist < minDist) {
                minDist = dist;
                selectedIdx = i;
            }
        }

        for (int i = 0; i < textViews.size(); i++) {
            if (i == selectedIdx) {
                if (lastSelectedIdx != selectedIdx) {
                    textViews.get(i).setBackgroundResource(R.drawable.alternative_selected_bg);
                }
            } else {
                textViews.get(i).setBackground(null);
            }
        }

        if (selectedIdx != -1 && selectedIdx != lastSelectedIdx && !skipNextSelectionHaptic) {
            this.doHapticFeedback(HapticFeedbackConstants.LONG_PRESS);
        }
        if (selectedIdx != -1) {
            skipNextSelectionHaptic = false;
        }
        lastSelectedIdx = selectedIdx;
    }

    private static float squaredDistToRect(float px, float py,
                                           float x1, float y1, float x2, float y2) {
        float dx = Math.max(x1 - px, Math.max(0, px - x2));
        float dy = Math.max(y1 - py, Math.max(0, py - y2));
        return dx * dx + dy * dy;
    }
}
