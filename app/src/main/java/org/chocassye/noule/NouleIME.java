package org.chocassye.noule;

import android.inputmethodservice.InputMethodService;
import android.view.View;

public class NouleIME extends InputMethodService {
    public NouleIME() {
    }

    @Override
    public View onCreateInputView() {
        NouleKeyboardView inputView =
                (NouleKeyboardView) getLayoutInflater().inflate(R.layout.keyboard, null);

        inputView.setParentService(this);

        return inputView;
    }

    public interface OnUpdateSelectionListener {
        void onUpdateSelection(int oldSelStart, int oldSelEnd, int newSelStart, int newSelEnd, int candidatesStart, int candidatesEnd);
    }

    private OnUpdateSelectionListener onUpdateSelectionListener;

    @Override
    public void onUpdateSelection(int oldSelStart, int oldSelEnd, int newSelStart, int newSelEnd, int candidatesStart, int candidatesEnd) {
        super.onUpdateSelection(oldSelStart, oldSelEnd, newSelStart, newSelEnd, candidatesStart, candidatesEnd);
        if (onUpdateSelectionListener != null) {
            onUpdateSelectionListener.onUpdateSelection(oldSelStart, oldSelEnd, newSelStart, newSelEnd, candidatesStart, candidatesEnd);
        }
    }

    public void setOnUpdateSelectionListener(OnUpdateSelectionListener onUpdateSelectionListener) {
        this.onUpdateSelectionListener = onUpdateSelectionListener;
    }

}
