package com.example.slothgoldencare.sudoko;

import android.content.Context;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.preference.PreferenceFragmentCompat;
import androidx.preference.PreferenceManager;

import com.example.slothgoldencare.R;

/**
 * The Prefs activity allows the user to customize game settings and preferences.
 * It provides a fragment-based user interface to display and modify preferences.
 */
public class Prefs extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getSupportFragmentManager()
                .beginTransaction()
                .replace(android.R.id.content, new PrefsFragment())
                .commit();
    }

    /**
     * The PrefsFragment class represents the fragment that displays the preferences.
     * It extends PreferenceFragmentCompat to provide a user interface for modifying preferences.
     */
    public static class PrefsFragment extends PreferenceFragmentCompat {
        @Override
        public void onCreatePreferences(Bundle savedInstanceState, String rootKey) {
            setPreferencesFromResource(R.xml.setting, rootKey);
        }
    }


    /**
     * Retrieves the current value of the "hintsOn" preference.
     *
     * @param context The context of the application or activity.
     * @return True if hints are enabled, false otherwise.
     */
    public static boolean getHints(Context context) {
        return PreferenceManager.getDefaultSharedPreferences(context)
                .getBoolean("hintsOn", true);
    }
}
