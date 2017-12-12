package org.cfp.citizenconnect.Notification;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.util.Log;

import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;

import org.cfp.citizenconnect.MainActivity;

/**
 * Created by root on 05/12/2017.
 */

public class FCMService extends FirebaseMessagingService {
    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {
        Log.d("FirebaseData", "Message data payload: " + remoteMessage.getData());
        if (remoteMessage.getData().size() > 0) {
            Log.d("FirebaseData", "Message data payload: " + remoteMessage.getData());

            SharedPreferences myPreferences =  getSharedPreferences("MyPrefference", Context.MODE_PRIVATE);
            SharedPreferences.Editor editor = myPreferences.edit();
            editor.putString("imageLink",remoteMessage.getData().values().toArray()[0].toString());
            editor.commit();
            startActivity(new Intent(this, MainActivity.class));
        }
    }
}