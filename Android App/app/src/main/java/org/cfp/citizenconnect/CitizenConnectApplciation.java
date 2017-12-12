package org.cfp.citizenconnect;

import android.app.Application;

import com.facebook.drawee.backends.pipeline.Fresco;
import com.google.firebase.messaging.FirebaseMessaging;
import com.google.firebase.remoteconfig.FirebaseRemoteConfig;

/**
 * Created by root on 05/12/2017.
 */

public class CitizenConnectApplciation extends Application {
    public static FirebaseRemoteConfig mFirebaseRemoteConfig;
    @Override
    public void onCreate() {
        super.onCreate();
        Fresco.initialize(this);
        mFirebaseRemoteConfig = FirebaseRemoteConfig.getInstance();
        FirebaseMessaging.getInstance().subscribeToTopic("news");


    }
}
