package org.chocassye.noule;

import android.content.SharedPreferences;
import android.inputmethodservice.InputMethodService;
import android.util.Log;
import android.view.ContextThemeWrapper;
import android.view.View;
import android.view.inputmethod.EditorInfo;

import androidx.preference.PreferenceManager;

public class NouleIME extends InputMethodService {
    public NouleIME() {
    }

    private NouleKeyboardView inputView;

    @Override
    public View onCreateInputView() {
        inputView = (NouleKeyboardView) getLayoutInflater()
                .cloneInContext(new ContextThemeWrapper(this, R.style.Theme_Noule))
                .inflate(R.layout.keyboard, null);

        inputView.setParentService(this);

        return inputView;
    }

    @Override
    public void onUpdateSelection(int oldSelStart, int oldSelEnd, int newSelStart, int newSelEnd, int candidatesStart, int candidatesEnd) {
        super.onUpdateSelection(oldSelStart, oldSelEnd, newSelStart, newSelEnd, candidatesStart, candidatesEnd);
        if (inputView != null) {
            inputView.onUpdateSelection(oldSelStart, oldSelEnd, newSelStart, newSelEnd, candidatesStart, candidatesEnd);
        }
    }

    @Override
    public void onStartInput(EditorInfo attribute, boolean restarting) {
        super.onStartInput(attribute, restarting);
        if (inputView != null) {
            inputView.onStartInput(attribute, restarting);
        }
    }

    @Override
    public void onStartInputView(EditorInfo editorInfo, boolean restarting) {
        super.onStartInputView(editorInfo, restarting);
        if (inputView != null) {
            inputView.onStartInputView(editorInfo, restarting);
        }
    }

    @Override
    public void onFinishInput() {
        super.onFinishInput();
        if (inputView != null) {
            inputView.onFinishInput();
        }
    }

    @Override
    public void onFinishInputView(boolean finishingInput) {
        super.onFinishInputView(finishingInput);
        if (inputView != null) {
            inputView.onFinishInputView(finishingInput);
        }
    }
}
