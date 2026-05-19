package org.chocassye.noule;

import android.content.SharedPreferences;
import android.inputmethodservice.InputMethodService;
import android.view.ContextThemeWrapper;
import android.view.View;
import android.view.inputmethod.EditorInfo;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.preference.PreferenceManager;

import org.chocassye.noule.lang.HanjaDict;
import org.chocassye.noule.lang.SymbolData;

public class NouleIME extends InputMethodService implements SharedPreferences.OnSharedPreferenceChangeListener {
    private NouleKeyboardView inputView;
    private int cachedBottomInset = 0;

    private View createInputView() {
        inputView = (NouleKeyboardView) getLayoutInflater()
                .cloneInContext(new ContextThemeWrapper(this, R.style.Theme_Noule))
                .inflate(R.layout.keyboard, null);

        // Apply last-known inset immediately so recreated views (e.g. pref changes)
        // never flash without padding even before the listener fires.
        inputView.setPadding(0, 0, 0, cachedBottomInset);

        // Fallback: if setDecorFitsSystemWindows didn't fully prevent the window from
        // extending behind the nav bar, keep the padding in sync via the insets listener.
        ViewCompat.setOnApplyWindowInsetsListener(inputView, (v, insets) -> {
            int bottom = insets.getInsets(WindowInsetsCompat.Type.systemBars()).bottom;
            if (bottom != cachedBottomInset) {
                cachedBottomInset = bottom;
                v.setPadding(v.getPaddingLeft(), v.getPaddingTop(), v.getPaddingRight(), bottom);
            }
            return insets;
        });

        // Request insets dispatch as soon as the view attaches so the listener fires
        // before the first draw rather than after it.
        inputView.addOnAttachStateChangeListener(new View.OnAttachStateChangeListener() {
            @Override
            public void onViewAttachedToWindow(@NonNull View v) {
                ViewCompat.requestApplyInsets(v);
                v.removeOnAttachStateChangeListener(this);
            }
            @Override
            public void onViewDetachedFromWindow(@NonNull View v) {}
        });

        inputView.setParentService(this);
        return inputView;
    }

    @Override
    public void onCreate() {
        super.onCreate();

        // Primary fix for Android 16: tell the IME window not to extend behind
        // system bars so the keyboard is always positioned above the nav bar.
        if (getWindow() != null && getWindow().getWindow() != null) {
            WindowCompat.setDecorFitsSystemWindows(getWindow().getWindow(), true);
        }

        SharedPreferences preferences =
                PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
        preferences.registerOnSharedPreferenceChangeListener(this);

        HanjaDict.initializeAsync(getAssets());
        SymbolData.initialize(getAssets());
    }

    @Override
    public boolean onShowInputRequested(int flags, boolean configChange) {
        return true;
    }

    @Override
    public boolean onEvaluateInputViewShown() {
        return super.onEvaluateInputViewShown() || true;
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
        setInputView(createInputView());
    }
}
