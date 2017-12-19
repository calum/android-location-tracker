package com.example.calum.tracker;

import android.app.Activity;
import android.content.Intent;
import android.support.v4.app.Fragment;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v4.app.FragmentActivity;
import android.util.Log;

import com.example.calum.location.Location;
import com.example.calum.location.PostLocationService;
import com.example.calum.utils.HTTP;

public class SettingsActivity extends FragmentActivity implements SharedPreferences.OnSharedPreferenceChangeListener {

    private static SettingsActivity instance;
    private static Fragment fragment;
    private HTTP http;

    private static final String TAG = "Settings Activity";

    private Location locationService;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        instance = this;
        fragment = new SettingsFragment();

        // Display the fragment as the main content.
        android.support.v4.app.FragmentManager fragmentManager = getSupportFragmentManager();
        android.support.v4.app.FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(android.R.id.content, fragment);
        fragmentTransaction.commit();

        // store the server url for later when we need to make POST requests
        http = new HTTP(PreferenceManager.getDefaultSharedPreferences(this).getString("api_address", ""));

        Log.i(TAG, "Started the preference screen!");

        startService(new Intent(this,PostLocationService.class));
    }

    public static Activity getActivity() {
        return instance;
    }
    public static Fragment getFragment() {
        return fragment;
    }

    public void onSharedPreferenceChanged(SharedPreferences sharedPreferences,
                                          String key) {
        Log.d(TAG, "shared preference changed: "+key);
        locationService.getLocation();
        if (key.equals("tracking_switch")) {
            Log.i(TAG, "tracking switch clicked: "+sharedPreferences.getBoolean(key, false));
        } else if (key.equals("api_address")) {
            Log.i(TAG, "api address changed: "+sharedPreferences.getString(key, ""));
            http.setUrl(sharedPreferences.getString(key, ""));
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        PreferenceManager.getDefaultSharedPreferences(this)
                .registerOnSharedPreferenceChangeListener(this);
    }

    @Override
    protected void onPause() {
        super.onPause();
        PreferenceManager.getDefaultSharedPreferences(this)
                .unregisterOnSharedPreferenceChangeListener(this);
    }

}