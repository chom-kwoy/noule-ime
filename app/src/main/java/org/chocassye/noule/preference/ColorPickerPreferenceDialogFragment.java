package org.chocassye.noule.preference;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.preference.PreferenceDialogFragmentCompat;
import androidx.preference.PreferenceManager;

import com.skydoves.colorpickerview.ColorPickerView;
import com.skydoves.colorpickerview.preference.ColorPickerPreferenceManager;
import com.skydoves.colorpickerview.sliders.BrightnessSlideBar;

import org.chocassye.noule.R;

public class ColorPickerPreferenceDialogFragment extends PreferenceDialogFragmentCompat {
    public static ColorPickerPreferenceDialogFragment newInstance(String key) {
        ColorPickerPreferenceDialogFragment fragment = new ColorPickerPreferenceDialogFragment();
        Bundle bundle = new Bundle();
        bundle.putString(ARG_KEY, key);
        fragment.setArguments(bundle);
        return fragment;
    }

    @Nullable
    @Override
    protected View onCreateDialogView(@NonNull Context context) {
        LayoutInflater inflater = requireActivity().getLayoutInflater();
        View view = inflater.inflate(R.layout.color_picker_dialog, null);

        colorPickerView = view.findViewById(R.id.colorPickerView);
        colorPickerView.setPreferenceName("MyColorPicker");

        BrightnessSlideBar brightnessSlideBar = view.findViewById(R.id.BrightnessSlideBar);
        colorPickerView.attachBrightnessSlider(brightnessSlideBar);

        return view;
    }

    private ColorPickerView colorPickerView;

    private static final String TAG = "MyColorPicker";

    @Override
    protected void onBindDialogView(@NonNull View view) {
        super.onBindDialogView(view);

        ColorPickerPreference pref = (ColorPickerPreference) getPreference();
        String color = pref.getColorValue();
        int brightnessPos = pref.getBrightnessPos();

        Log.i("MYLOG", String.format("onBindDialogView: read color %s, brightness %d", color, brightnessPos));

        ColorPickerPreferenceManager manager =
                ColorPickerPreferenceManager.getInstance(getContext());
        manager.clearSavedAllData();
        if (color != null) {
            manager.setColor(TAG, Integer.parseUnsignedInt(color, 16));
        }
        if (brightnessPos != -1) {
            manager.setBrightnessSliderPosition(TAG, brightnessPos);
        }
        manager.restoreColorPickerData(colorPickerView);
    }

    @Override
    public void onDialogClosed(boolean positiveResult) {
        if (positiveResult) {
            ColorPickerPreferenceManager manager =
                    ColorPickerPreferenceManager.getInstance(getContext());
            manager.saveColorPickerData(colorPickerView);

            String color = colorPickerView.getColorEnvelope().getHexCode();
            int brightnessPos = manager.getBrightnessSliderPosition(TAG, -1);

            Log.i("MYLOG", String.format("onDialogClosed: Setting color to %s, brightness to %d", color, brightnessPos));

            ColorPickerPreference pref = (ColorPickerPreference) getPreference();
            pref.setColorValue(color);
            if (brightnessPos != -1) {
                pref.setBrightnessPos(brightnessPos);
            }
        }
    }
}
