package org.cfp.citizenconnect;

import android.app.Application;
import android.content.Intent;

import com.crashlytics.android.Crashlytics;
import com.facebook.drawee.backends.pipeline.Fresco;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.messaging.FirebaseMessaging;
import com.google.firebase.remoteconfig.FirebaseRemoteConfig;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import io.fabric.sdk.android.Fabric;
import io.realm.Realm;
import io.realm.RealmConfiguration;

/**
 * Created by root on 05/12/2017.
 */

public class CitizenConnectApplication extends Application {
    public static FirebaseRemoteConfig mFirebaseRemoteConfig;
    public  static FirebaseStorage firebaseStorage ;
    public  static StorageReference firebaseStorageRef;
    public  static  FirebaseDatabase database;
    public  static DatabaseReference FilesRef;
    public static FirebaseAuth mAuth;
    public  static  Realm realm;
    public  static  RealmConfiguration config;

    public static final String FILE_PROVIDER_AUTHORITY = "org.cfp.citizenconnect.fileprovider";

    @Override
    public void onCreate() {
        super.onCreate();
        Fabric.with(this, new Crashlytics());
        Fresco.initialize(this);
        Realm.init(this);

        config = new RealmConfiguration.Builder().deleteRealmIfMigrationNeeded().build();
        realm = Realm.getInstance(config);
        database = FirebaseDatabase.getInstance();
        FilesRef = database.getReference("Notifications");
        mAuth = FirebaseAuth.getInstance();

        mFirebaseRemoteConfig = FirebaseRemoteConfig.getInstance();
        FirebaseMessaging.getInstance().subscribeToTopic("notification");

        firebaseStorage = FirebaseStorage.getInstance();
        firebaseStorageRef = firebaseStorage.getReference();



    }
}
