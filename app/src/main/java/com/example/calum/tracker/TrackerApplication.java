package com.example.calum.tracker;

import android.app.Application;
import android.app.FragmentManager;
import android.content.Context;

import com.example.calum.utils.HTTP;

/**
 * Created by calum on 19/12/17.
 */

public class TrackerApplication extends Application {

    private static Context context;
    private static FragmentManager fragmentManager;

    public void onCreate() {
        super.onCreate();
        TrackerApplication.context = getApplicationContext();
    }

    public static Context getAppContext() {
        return TrackerApplication.context;
    }
}
