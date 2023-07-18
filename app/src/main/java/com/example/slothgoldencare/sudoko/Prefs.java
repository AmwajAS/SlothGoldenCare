package com.example.slothgoldencare.sudoko;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.preference.PreferenceActivity;
import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.preference.PreferenceFragmentCompat;

import com.example.slothgoldencare.R;

public class Prefs extends AppCompatActivity {




        @SuppressLint("ResourceType")
        @Override
        protected void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            setContentView(R.xml.setting);
        }

        public static class PrefsFragment extends PreferenceFragmentCompat {
            @Override
            public void onCreatePreferences(Bundle savedInstanceState, String rootKey) {
                setPreferencesFromResource(R.xml.setting, rootKey);
            }
        }
    }

