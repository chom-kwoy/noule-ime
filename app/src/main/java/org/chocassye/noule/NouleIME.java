package org.chocassye.noule;

import android.content.SharedPreferences;
import android.inputmethodservice.InputMethodService;
import android.util.Log;
import android.view.ContextThemeWrapper;
import android.view.View;

import androidx.preference.PreferenceManager;

public class NouleIME extends InputMethodService {
    public NouleIME() {
    }

    @Override
    public View onCreateInputView() {
        NouleKeyboardView inputView =
                (NouleKeyboardView) getLayoutInflater()
                        .cloneInContext(new ContextThemeWrapper(this, R.style.Theme_Noule))
                        .inflate(R.layout.keyboard, null);

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
