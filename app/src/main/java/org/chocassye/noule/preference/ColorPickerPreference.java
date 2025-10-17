package org.chocassye.noule.preference;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.AttributeSet;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.preference.DialogPreference;

public class ColorPickerPreference extends DialogPreference {
    private String colorValue = null;

    private int brightnessPos = -1;

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

    public int getBrightnessPos() {
        return brightnessPos;
    }

    public void setBrightnessPos(int brightnessPos) {
        if (shouldPersist()) {
            SharedPreferences sharedPreferences = getSharedPreferences();
            if (sharedPreferences != null) {
                SharedPreferences.Editor editor = getSharedPreferences().edit();
                editor.putInt(String.format("%s-brightnessPos", getKey()), brightnessPos);
                editor.apply();
            }

            this.brightnessPos = brightnessPos;
        }
    }

    @Override
    protected void onSetInitialValue(@Nullable Object defaultValue) {
        this.colorValue = getPersistedString(null);
        SharedPreferences sharedPreferences = getSharedPreferences();
        if (sharedPreferences != null) {
            this.brightnessPos = sharedPreferences.getInt(
                    String.format("%s-brightnessPos", getKey()), -1);
        }
    }
}
