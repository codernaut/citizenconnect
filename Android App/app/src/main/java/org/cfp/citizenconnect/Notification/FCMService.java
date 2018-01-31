package org.cfp.citizenconnect.Notification;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Intent;
import android.util.Log;

import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;

import org.cfp.citizenconnect.MainActivity;
import org.cfp.citizenconnect.Model.NotificationUpdate;
import org.cfp.citizenconnect.MyUtils;
import org.cfp.citizenconnect.R;

import io.realm.Realm;

import static org.cfp.citizenconnect.CitizenConnectApplication.config;

/**
 * Created by root on 05/12/2017.
 */

public class FCMService extends FirebaseMessagingService {
    Realm mRealm;

    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {
        mRealm = Realm.getInstance(config);
        UpdateNewNotification();
        Log.d("FirebaseData", "Message data payload: " + remoteMessage.getData());
        if (remoteMessage.getData().size() > 0) {

            Intent i = new Intent("android.intent.action.MAIN").putExtra("newUpdate", true);
            this.sendBroadcast(i);

            Intent openApp = new Intent(this, MainActivity.class);
            openApp.putExtra("clearNotificationCount", true);

            PendingIntent pendingIntent = PendingIntent.getActivity(this, 1,
                    openApp, PendingIntent.FLAG_UPDATE_CURRENT);
            Notification notification = new Notification.Builder(this)
                    .setContentIntent(pendingIntent)
                    .setSmallIcon(R.drawable.connection_notification)
                    .setLargeIcon(MyUtils.getBitmap(R.mipmap.connection, getApplicationContext()))
                    .setContentTitle("ICT Citizen Connect")
                    .setContentText("New update added click to view")
                    .setPriority(Notification.PRIORITY_MAX)
                    .setDefaults(Notification.DEFAULT_LIGHTS | Notification.DEFAULT_SOUND)
                    .setAutoCancel(true)
                    .build();

            NotificationManager notificationManager =
                    (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
            notificationManager.notify(0, notification);


        }
    }

    public void UpdateNewNotification() {
        NotificationUpdate notificationUpdate = NotificationUpdate.getInstance(mRealm);
        int newNotification = notificationUpdate.getNewNotification();

        newNotification = newNotification + 1;
        int finalNewNotification = newNotification;
        mRealm.executeTransaction((Realm realm) -> {
            notificationUpdate.setNewNotification(finalNewNotification);
        });

    }




}