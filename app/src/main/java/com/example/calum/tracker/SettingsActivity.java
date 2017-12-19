package com.example.calum.tracker;

import android.content.Intent;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v4.app.FragmentActivity;
import android.util.Log;

import com.example.calum.location.Location;
import com.example.calum.location.PostLocationService;
import com.example.calum.utils.HTTP;
import com.yayandroid.locationmanager.listener.LocationListener;

import org.json.JSONObject;

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

        // setup the location service
        locationService = new Location(instance, fragment, new LocationListener() {
            @Override
            public void onProcessTypeChanged(int processType) {
                Log.d(TAG, "Process Type Changed: "+processType);
            }

            @Override
            public void onLocationChanged(android.location.Location location) {
                Log.d(TAG, "Location Changed: "+location.toString());

                // post the location to the server:
                try {
                    // construct the JSON to send
                    JSONObject jsonLocation = new JSONObject()
                            .put("latitude", location.getLatitude())
                            .put("longitude", location.getLongitude());
                    String data = new JSONObject()
                            .put("location", jsonLocation)
                            .put("timestamp", System.currentTimeMillis()).toString();

                    // set the http data to be posted in the main thread
                    http.setData(data);

                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onLocationFailed(int type) {
                Log.d(TAG, "Location Failed: "+type);
            }

            @Override
            public void onPermissionGranted(boolean alreadyHadPermission) {
                Log.d(TAG, "Permission Granted: "+alreadyHadPermission);
            }

            @Override
            public void onStatusChanged(String provider, int status, Bundle extras) {
                Log.d(TAG, "Status Changed: "+ provider + ", " + status + ", " + extras.toString());
            }

            @Override
            public void onProviderEnabled(String provider) {
                Log.d(TAG, "Provider Enabled: "+provider);
            }

            @Override
            public void onProviderDisabled(String provider) {
                Log.d(TAG, "Provider Disabled: "+provider);
            }
        });

        startService(new Intent(this,PostLocationService.class));
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