package org.chocassye.noule;

import android.os.Bundle;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.preference.Preference;
import androidx.preference.PreferenceFragmentCompat;

import org.chocassye.noule.preference.ColorPickerPreference;
import org.chocassye.noule.preference.ColorPickerPreferenceDialogFragment;

public class NouleIMESettings extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.settings_activity);
        if (savedInstanceState == null) {
            getSupportFragmentManager()
                    .beginTransaction()
                    .replace(R.id.settings, new SettingsFragment())
                    .commit();
        }
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
        }
    }

    public static class SettingsFragment extends PreferenceFragmentCompat {
        @Override
        public void onCreatePreferences(Bundle savedInstanceState, String rootKey) {
            setPreferencesFromResource(R.xml.root_preferences, rootKey);
        }

        @Override
        public void onDisplayPreferenceDialog(@NonNull Preference preference) {
            Log.i("MYLOG", String.format("onDisplayPreferenceDialog %s %s", preference.getClass().toString(), preference.toString()));
            if (preference instanceof ColorPickerPreference) {
                ColorPickerPreferenceDialogFragment dialogFragment =
                        ColorPickerPreferenceDialogFragment.newInstance(preference.getKey());
                dialogFragment.setTargetFragment(this, 0);
                dialogFragment.show(getParentFragmentManager(), "ColorPickerPreference");
            }
            else {
                super.onDisplayPreferenceDialog(preference);
            }
        }
    }

}