package org.cfp.citizenconnect.Notification;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.media.RingtoneManager;
import android.os.Build;
import android.support.v4.app.NotificationCompat;
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
    private NotificationChannel mChannel;
    private NotificationManager notifManager;

    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {
        mRealm = Realm.getInstance(config);
        UpdateNewNotification();
        Log.d("FirebaseData", "Message data payload: " + remoteMessage.getData());
        if (remoteMessage.getData().size() > 0) {

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                if (notifManager == null) {
                    notifManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
                }

                Intent i = new Intent("android.intent.action.MAIN").putExtra("newUpdate", true);
                this.sendBroadcast(i);

                NotificationCompat.Builder builder;

                Intent openApp = new Intent(this, MainActivity.class);
                openApp.putExtra("clearNotificationCount", true);

                int importance = NotificationManager.IMPORTANCE_HIGH;
                if (mChannel == null) {
                    mChannel = new NotificationChannel(getResources().getString(R.string.default_notification_channel_id), "oreo", importance);
                    mChannel.enableVibration(true);
                    notifManager.createNotificationChannel(mChannel);
                }

                builder = new NotificationCompat.Builder(this, mChannel.getId());
                PendingIntent pendingIntent = PendingIntent.getActivity(this, 1,
                        openApp, PendingIntent.FLAG_UPDATE_CURRENT);
                        builder.setContentIntent(pendingIntent)
                        .setSmallIcon(R.drawable.ic_notification)
                        .setLargeIcon(MyUtils.getBitmap(R.drawable.logo, getApplicationContext()))
                        .setContentTitle("ICT Citizen Connect")
                        .setContentText("New update added click to view")
                        .setPriority(Notification.PRIORITY_MAX)
                        .setDefaults(Notification.DEFAULT_LIGHTS | Notification.DEFAULT_SOUND)
                        .setAutoCancel(true)
                        .setSound(RingtoneManager.getDefaultUri
                        (RingtoneManager.TYPE_NOTIFICATION));
                Notification notification = builder.build();
                notifManager.notify(Integer.parseInt(getString(R.string.ICT_NOTIFICATION_ID)), notification);
            } else {
                Intent i = new Intent("android.intent.action.MAIN").putExtra("newUpdate", true);
                this.sendBroadcast(i);

                Intent openApp = new Intent(this, MainActivity.class);
                openApp.putExtra("clearNotificationCount", true);

                PendingIntent pendingIntent = PendingIntent.getActivity(this, 1,openApp, PendingIntent.FLAG_UPDATE_CURRENT);
                Notification notification = new Notification.Builder(this)
                        .setContentIntent(pendingIntent)
                        .setSmallIcon(R.drawable.ic_notification)
                        .setLargeIcon(MyUtils.getBitmap(R.drawable.ic_notification, getApplicationContext()))
                        .setContentTitle("ICT Citizen Connect")
                        .setContentText("New update added click to view")
                        .setPriority(Notification.PRIORITY_MAX)
                        .setDefaults(Notification.DEFAULT_LIGHTS | Notification.DEFAULT_SOUND)
                        .setAutoCancel(true)
                        .build();
                NotificationManager notificationManager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
                notificationManager.notify(Integer.parseInt(getString(R.string.ICT_NOTIFICATION_ID)), notification);

            }

        }
    }

    public void UpdateNewNotification() {
        NotificationUpdate notificationUpdate = NotificationUpdate.getInstance(mRealm);
        int newNotification = notificationUpdate.getNewNotification();

        newNotification = newNotification + 1;
        int finalNewNotification = newNotification;
        mRealm.executeTransaction((Realm realm) -> {
            notificationUpdate.setNewNotification(finalNewNotification);
            notificationUpdate.setLastStateRead(false);
        });

    }


}