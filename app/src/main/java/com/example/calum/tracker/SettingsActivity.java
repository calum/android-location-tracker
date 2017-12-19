package com.example.calum.tracker;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.Preference;
import android.preference.PreferenceActivity;
import android.util.Log;

public class SettingsActivity extends PreferenceActivity implements SharedPreferences.OnSharedPreferenceChangeListener {

    private static SettingsActivity instance;

    private static final String TAG = "Settings Activity";

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        instance = this;
        addPreferencesFromResource(R.xml.preferences);
        Log.i(TAG, "Started the preference screen!");
    }

    public static Context getContext() {
        return instance;
    }

    public void onSharedPreferenceChanged(SharedPreferences sharedPreferences,
                                          String key) {
        Log.d(TAG, "shared preference changed: "+key);
        if (key.equals("tracking_switch")) {
            Preference trackingSwitch = findPreference(key);
            Log.i(TAG, "tracking switch clicked: "+sharedPreferences.getBoolean(key, false));
        } else if (key.equals("api_address")) {
            Preference apiAddress = findPreference(key);
            // Set summary to be the user-description for the selected value
            apiAddress.setSummary(sharedPreferences.getString(key, ""));

            Log.i(TAG, "api address changed: "+sharedPreferences.getString(key, ""));
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        getPreferenceScreen().getSharedPreferences()
                .registerOnSharedPreferenceChangeListener(this);
    }

    @Override
    protected void onPause() {
        super.onPause();
        getPreferenceScreen().getSharedPreferences()
                .unregisterOnSharedPreferenceChangeListener(this);
    }

}