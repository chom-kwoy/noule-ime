package org.chocassye.noule;

import android.content.SharedPreferences;
import android.inputmethodservice.InputMethodService;
import android.util.Log;
import android.view.ContextThemeWrapper;
import android.view.View;
import android.view.inputmethod.EditorInfo;

import androidx.annotation.Nullable;
import androidx.preference.PreferenceManager;

import org.chocassye.noule.lang.HanjaDict;
import org.chocassye.noule.lang.SymbolData;

public class NouleIME extends InputMethodService implements SharedPreferences.OnSharedPreferenceChangeListener  {
    private NouleKeyboardView inputView;

    private View createInputView() {
        inputView = (NouleKeyboardView) getLayoutInflater()
                .cloneInContext(new ContextThemeWrapper(this, R.style.Theme_Noule))
                .inflate(R.layout.keyboard, null);

        inputView.setParentService(this);

        return inputView;
    }

    @Override
    public void onCreate() {
        super.onCreate();

        // Detect preference updates
        SharedPreferences preferences =
                PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
        preferences.registerOnSharedPreferenceChangeListener(this);

        // Load data from assets
        HanjaDict.initializeAsync(getAssets());
        SymbolData.initialize(getAssets());
    }

    @Override
    public View onCreateInputView() {
        return createInputView();
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

    @Override
    public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, @Nullable String key) {
        // Recreate input view
        setInputView(createInputView());
    }
}
