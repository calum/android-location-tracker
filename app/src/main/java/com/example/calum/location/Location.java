package com.example.calum.location;


import android.app.Activity;
import android.support.v4.app.Fragment;

import com.example.calum.tracker.TrackerApplication;
import com.yayandroid.locationmanager.LocationManager;
import com.yayandroid.locationmanager.configuration.Configurations;
import com.yayandroid.locationmanager.configuration.LocationConfiguration;
import com.yayandroid.locationmanager.listener.LocationListener;

public class Location {

    private LocationConfiguration config;
    private LocationManager locationManager;

    public Location(Activity activity, Fragment fragment, LocationListener locationListener) {
        // use the default silent config
        config = Configurations.defaultConfiguration("permission", "permission");

        // LocationManager MUST be initialized with Application context in order to prevent MemoryLeaks
        locationManager = new LocationManager.Builder(TrackerApplication.getAppContext())
                .activity(activity) // Only required to ask permission and/or GoogleApi - SettingsApi
                .fragment(fragment)
                .configuration(config)
                .notify(locationListener)
                .build();

        // turn on the logs
        LocationManager.enableLog(true);

    }

    public void getLocation() {
        locationManager.get();
    }
}