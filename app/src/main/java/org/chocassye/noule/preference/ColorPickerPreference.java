package org.chocassye.noule.preference;

import android.content.Context;
import android.util.AttributeSet;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.preference.DialogPreference;

public class ColorPickerPreference extends DialogPreference {
    private String colorValue = null;

    public ColorPickerPreference(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
    }

    public ColorPickerPreference(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    public ColorPickerPreference(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    public ColorPickerPreference(@NonNull Context context) {
        super(context);
    }

    public String getColorValue() {
        return colorValue;
    }

    public void setColorValue(String colorValue) {
        boolean wasPersisted = persistString(colorValue);
        if (wasPersisted) {
            this.colorValue = colorValue;
        }
    }

    @Override
    protected void onSetInitialValue(@Nullable Object defaultValue) {
        Log.i("MYLOG", "onSetInitialValue");
        if (defaultValue != null) {
            Log.i("MYLOG", defaultValue.toString());
        } else {
            Log.i("MYLOG", "defaultValue is null");
        }
        setColorValue(getPersistedString((String) defaultValue));
    }
}
