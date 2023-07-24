package com.example.slothgoldencare;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.preference.Preference;
import android.preference.PreferenceFragment;
import androidx.annotation.Nullable;


public class SettingsFragment extends PreferenceFragment {
    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        addPreferencesFromResource(R.xml.preference);
        //for the About Us
        findPreference("aboutUs_preference").setOnPreferenceClickListener(
                new Preference.OnPreferenceClickListener() {
                    @Override
                    public boolean onPreferenceClick(Preference preference) {
                        startActivity(new Intent(getContext(), AboutUsActivity.class));
                        return true;
                    }
                });
        //for the app info
        findPreference("info_preference").setOnPreferenceClickListener(
                new Preference.OnPreferenceClickListener() {
                    @Override
                    public boolean onPreferenceClick(Preference preference) {
                        startActivity(new Intent(getContext(), AppInfoActivity.class));
                        return true;
                    }
                });
        //for the call
        findPreference("contact_preference").setOnPreferenceClickListener(
                new Preference.OnPreferenceClickListener() {
                    @Override
                    public boolean onPreferenceClick(Preference preference) {
                        String phoneNumber = "*3555";
                        Uri uri = Uri.parse("tel:" + phoneNumber);

                        Intent dialIntent = new Intent(Intent.ACTION_DIAL, uri);
                        startActivity(dialIntent);
                        return true;
                    }
                });
        //for the Web
        findPreference("contact_us_link_preference").setOnPreferenceClickListener(
                new Preference.OnPreferenceClickListener() {
                    @Override
                    public boolean onPreferenceClick(Preference preference) {
                        String websiteUrl = "https://www.maccabi4u.co.il/";
                        Uri uri = Uri.parse(websiteUrl);

                        Intent viewIntent = new Intent(Intent.ACTION_VIEW, uri);
                        startActivity(viewIntent);
                        return true;
                    }
                });
    }

}
