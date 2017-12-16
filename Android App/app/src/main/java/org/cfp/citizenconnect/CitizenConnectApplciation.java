package org.cfp.citizenconnect;

import android.app.Application;

import com.facebook.drawee.backends.pipeline.Fresco;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.messaging.FirebaseMessaging;
import com.google.firebase.remoteconfig.FirebaseRemoteConfig;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

/**
 * Created by root on 05/12/2017.
 */

public class CitizenConnectApplciation extends Application {
    public static FirebaseRemoteConfig mFirebaseRemoteConfig;
    public  static FirebaseStorage firebaseStorage ;
    public  static StorageReference firebaseStorageRef;
    public  static  FirebaseDatabase database;
    public  static DatabaseReference FilesRef;

    @Override
    public void onCreate() {
        super.onCreate();
        Fresco.initialize(this);

        database = FirebaseDatabase.getInstance();
        FilesRef = database.getReference("Files");

        mFirebaseRemoteConfig = FirebaseRemoteConfig.getInstance();
        FirebaseMessaging.getInstance().subscribeToTopic("notification");

        firebaseStorage = FirebaseStorage.getInstance();
        firebaseStorageRef = firebaseStorage.getReference();



    }
}
