package org.cfp.citizenconnect;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.util.Log;

/**
 * Created by shahzaibshahid on 03/01/2018.
 */

public class TaskHandlerService extends Service {


    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        Log.i("ServiceTask", "Service Started");
        return START_NOT_STICKY;
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        Log.i("ServiceTask", "onDestroy()");
        Intent i = new Intent("android.intent.action.MAIN").putExtra("saveCurrentState", true);
        this.sendBroadcast(i);
    }

    @Override
    public void onTaskRemoved(Intent rootIntent) {
        super.onTaskRemoved(rootIntent);
        Log.i("ServiceTask", "Task Removed ");
    }
}
