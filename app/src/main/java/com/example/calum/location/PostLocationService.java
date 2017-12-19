package com.example.calum.location;

import android.app.Service;
import android.content.Intent;
import android.os.Bundle;
import android.os.IBinder;
import android.preference.PreferenceManager;
import android.util.Log;

import com.example.calum.tracker.SettingsActivity;
import com.example.calum.tracker.TrackerApplication;
import com.example.calum.utils.HTTP;
import com.yayandroid.locationmanager.listener.LocationListener;

import org.json.JSONObject;

import java.util.Timer;
import java.util.TimerTask;


/**
 * The background service which will periodically post
 * a location to an API.
 * Created by calum on 19/12/17.
 */

public class PostLocationService extends Service {

    private String TAG = "PostLocationService";
    private String api_address;
    private Location locationService;
    private HTTP http;

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        super.onStartCommand(intent, flags, startId);

        api_address = PreferenceManager.getDefaultSharedPreferences(TrackerApplication.getAppContext()).getString("api_address", "");

        http = new HTTP(api_address);

        // setup the location service
        locationService = new Location(SettingsActivity.getActivity(), SettingsActivity.getFragment(), new LocationListener() {
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

        new Thread(new Runnable() {
            @Override
            public void run() {

                Timer timer = new Timer();

                // send a post request containing the location every 5 seconds
                timer.scheduleAtFixedRate(new TimerTask() {
                    @Override
                    public void run() {

                        locationService.getLocation();

                        Log.d(TAG, "Posting to api: "+api_address);

                        if (http.getData() == null) {
                            http.setData("{}");
                        }

                        http.new Post().execute(http.getData());
                    }
                },0, 60*1000);

            }
        }).start();
        return START_STICKY;
    }

    @Override
    public IBinder onBind(Intent intent) {
        // We don't provide binding, so return null
        return null;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();

        // restart this service
        Intent restartService = new Intent("RestartService");
        sendBroadcast(restartService);
    }
}
