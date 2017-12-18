package com.example.calum.location;


import android.app.Activity;

import com.example.calum.track_calum.SettingsActivity;
import com.yayandroid.locationmanager.LocationManager;
import com.yayandroid.locationmanager.configuration.Configurations;
import com.yayandroid.locationmanager.configuration.LocationConfiguration;

public class Location {

    private LocationConfiguration config;
    private LocationManager locationManager;

    public Location(Activity activity) {
        // use the default silent config
        config = Configurations.silentConfiguration();

        // LocationManager MUST be initialized with Application context in order to prevent MemoryLeaks
        locationManager = new LocationManager.Builder(SettingsActivity.getContext())
                .activity(activity) // Only required to ask permission and/or GoogleApi - SettingsApi
                .configuration(config)
                .build();

        // turn on the logs
        if (System.getenv("LOG_LEVEL") != null || System.getenv("LOG_LEVEL").isEmpty()) {
            LocationManager.enableLog(false);
        }

    }

    public void getLocation() {
        locationManager.get();
    }
}