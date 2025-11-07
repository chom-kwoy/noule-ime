package org.chocassye.noule;

import android.content.SharedPreferences;
import android.inputmethodservice.InputMethodService;
import android.os.Build;
import android.view.ContextThemeWrapper;
import android.view.View;
import android.view.Window;
import android.view.inputmethod.EditorInfo;

import androidx.annotation.Nullable;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.preference.PreferenceManager;

import org.chocassye.noule.lang.HanjaDict;
import org.chocassye.noule.lang.SymbolData;

public class NouleIME extends InputMethodService implements SharedPreferences.OnSharedPreferenceChangeListener  {
    private NouleKeyboardView inputView;

    private View createInputView() {
        inputView = (NouleKeyboardView) getLayoutInflater()
                .cloneInContext(new ContextThemeWrapper(this, R.style.Theme_Noule))
                .inflate(R.layout.keyboard, null);

        // Pad bottom for android 16
        ViewCompat.setOnApplyWindowInsetsListener(
                inputView,
                (v, insets) -> {
                    // 1. Get the Insets object for system bars (status bar + navigation bar)
                    androidx.core.graphics.Insets systemBarsInsets =
                            insets.getInsets(WindowInsetsCompat.Type.systemBars());

                    // 2. Extract the bottom inset size
                    int bottomInsetSize = systemBarsInsets.bottom; // This is the size in pixels

                    // --- Use the bottomInsetSize here ---
                    // For example, to apply it as bottom padding to a specific view:
                    inputView.setPadding(
                            inputView.getPaddingLeft(),
                            inputView.getPaddingTop(),
                            inputView.getPaddingRight(),
                            bottomInsetSize // Apply the calculated bottom inset
                    );
                    return insets;
                }
        );

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

        setInputView(createInputView());
    }

    @Override
    public void onInitializeInterface() {
        super.onInitializeInterface();

        // Get the window of the IME
        Window window = getWindow().getWindow();

        if (window != null) {
            // This flag is often needed to correctly position the IME window
            // and have it respect system bars/insets.
            window.getDecorView().setSystemUiVisibility(
                    View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN |
                    View.SYSTEM_UI_FLAG_LAYOUT_STABLE
            );

            // You can also try setting decor fits system windows to true,
            // which often helps with respecting insets like the navigation bar.
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
                window.setDecorFitsSystemWindows(true);
            }

            // If you are setting a background color, use FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS
            // to ensure the system handles the drawing area correctly.
            // window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
        }
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
        // Recreate input view
        setInputView(createInputView());
    }
}
