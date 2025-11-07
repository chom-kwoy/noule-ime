package org.chocassye.noule;

import android.content.Intent;
import android.os.Bundle;
import android.provider.Settings;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.WindowCompat;
import androidx.preference.Preference;
import androidx.preference.PreferenceFragmentCompat;

import org.chocassye.noule.preference.ColorPickerPreference;
import org.chocassye.noule.preference.ColorPickerPreferenceDialogFragment;

public class NouleIMESettings extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        WindowCompat.enableEdgeToEdge(getWindow());

        setContentView(R.layout.settings_activity);

        // Get the Toolbar from the layout
        androidx.appcompat.widget.Toolbar toolbar = findViewById(R.id.toolbar);
        // Set the Toolbar as the Activity's ActionBar
        setSupportActionBar(toolbar);

        if (savedInstanceState == null) {
            getSupportFragmentManager()
                    .beginTransaction()
                    .replace(R.id.settings, new SettingsFragment())
                    .commit();
        }

        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(false);
        }

        setTitle(R.string.settings);
    }

    public static class SettingsFragment extends PreferenceFragmentCompat {
        @Override
        public void onCreatePreferences(Bundle savedInstanceState, String rootKey) {
            setPreferencesFromResource(R.xml.root_preferences, rootKey);

            Preference openSettings = findPreference("open_settings_pref");
            openSettings.setOnPreferenceClickListener((preference) -> {
                // Create an Intent with the action to open input method settings
                Intent intent = new Intent(Settings.ACTION_INPUT_METHOD_SETTINGS);

                // Start the activity
                startActivity(intent);

                return true;
            });
        }

        @Override
        public void onDisplayPreferenceDialog(@NonNull Preference preference) {
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