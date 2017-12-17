package org.cfp.citizenconnect.Notification;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v4.app.NotificationCompat;
import android.util.Log;

import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;

import org.cfp.citizenconnect.MainActivity;
import org.cfp.citizenconnect.R;

/**
 * Created by root on 05/12/2017.
 */

public class FCMService extends FirebaseMessagingService {
    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {
        Log.d("FirebaseData", "Message data payload: " + remoteMessage.getData());
        if (remoteMessage.getData().size() > 0) {
            Intent i = new Intent("android.intent.action.MAIN").putExtra("newUpdate", true);
            this.sendBroadcast(i);
            PendingIntent pendingIntent = PendingIntent.getActivity(this, 1,
                    new Intent(this,MainActivity.class), PendingIntent.FLAG_UPDATE_CURRENT);
            Notification noti = new Notification.Builder(this)
                    .setContentIntent(pendingIntent)
                    .setContentTitle("ICT Citizen Connect")
                    .setContentText("New update added click to view")
                    .setSmallIcon(R.drawable.ic_launcher_background)
                    .setPriority(Notification.PRIORITY_MAX)
                    .setDefaults(Notification.DEFAULT_LIGHTS | Notification.DEFAULT_SOUND)
                    .build();

            NotificationManager notificationManager =
                    (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
            notificationManager.notify(0, noti);

        }
    }
}