package org.chocassye.noule.preference;

import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.preference.PreferenceDialogFragmentCompat;

import com.skydoves.colorpickerview.ColorPickerView;
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
        return inflater.inflate(R.layout.color_picker_dialog, null);
    }

    private ColorPickerView colorPickerView;

    @Override
    protected void onBindDialogView(@NonNull View view) {
        super.onBindDialogView(view);
        colorPickerView = view.findViewById(R.id.colorPickerView);
        BrightnessSlideBar brightnessSlideBar = view.findViewById(R.id.BrightnessSlideBar);
        colorPickerView.attachBrightnessSlider(brightnessSlideBar);

        ColorPickerPreference pref = (ColorPickerPreference) getPreference();
        String color = pref.getColorValue();

        if (color != null) {
            Log.i("MYLOG", String.format("onBindDialogView: read color %s", color));

            colorPickerView.setInitialColor(Integer.parseUnsignedInt(color, 16));
        }
    }

    @Override
    public void onDialogClosed(boolean positiveResult) {
        if (positiveResult) {
            String color = colorPickerView.getColorEnvelope().getHexCode();

            Log.i("MYLOG", String.format("onDialogClosed: Setting color to %s", color));

            ColorPickerPreference pref = (ColorPickerPreference) getPreference();
            pref.setColorValue(color);
        }
    }
}
