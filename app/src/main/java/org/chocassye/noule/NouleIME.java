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

}
