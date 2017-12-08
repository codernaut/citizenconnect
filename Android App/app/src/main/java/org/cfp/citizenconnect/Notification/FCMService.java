package org.cfp.citizenconnect.Notification;

import android.util.Log;

import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;

/**
 * Created by root on 05/12/2017.
 */

public class FCMService extends FirebaseMessagingService {
    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {
        Log.d("FirebaseData", "Message data payload: " + remoteMessage.getData());
        if (remoteMessage.getData().size() > 0) {
            Log.d("FirebaseData", "Message data payload: " + remoteMessage.getData());

        }
    }
}