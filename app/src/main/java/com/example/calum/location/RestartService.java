package com.example.calum.location;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

/**
 * Restarts the PostLocationService to ensure that the service
 * never dies.
 * Created by calum on 19/12/17.
 */

public class RestartService extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        context.startService(new Intent(context,PostLocationService.class));
    }
}
