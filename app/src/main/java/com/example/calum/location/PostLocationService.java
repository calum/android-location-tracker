package com.example.calum.location;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.preference.PreferenceManager;

import com.example.calum.tracker.SettingsActivity;
import com.example.calum.tracker.TrackerApplication;
import com.example.calum.utils.HTTP;

import java.util.Timer;
import java.util.TimerTask;


/**
 * The background service which will periodically post
 * a location to an API.
 * Created by calum on 19/12/17.
 */

public class PostLocationService extends Service {

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        super.onStartCommand(intent, flags, startId);
        new Thread(new Runnable() {
            @Override
            public void run() {
                Timer timer = new Timer();
                timer.schedule(new TimerTask() {
                    @Override
                    public void run() {
                        HTTP http = new HTTP(PreferenceManager.getDefaultSharedPreferences(TrackerApplication.getAppContext()).getString("api_address", ""));

                        http.new Post().execute(http.getData());

                    }
                }, 0, 60*1000);
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
