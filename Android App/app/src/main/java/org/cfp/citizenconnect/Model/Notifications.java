package org.cfp.citizenconnect.Model;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;

import org.cfp.citizenconnect.CustomCallBack;

import java.util.ArrayList;
import java.util.List;

import io.realm.Realm;
import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;

import static org.cfp.citizenconnect.CitizenConnectApplication.realm;
import static org.cfp.citizenconnect.MyUtils.getAFireBaseData;

/**
 * Created by shahzaibshahid on 13/12/2017.
 */


public class Notifications extends RealmObject{

    @PrimaryKey
    String id;
    String filePath;
    String date;
    String description;
    String tag;

    public static void setNotifications(Notifications notificationsObj, String key, Realm mRealm) {
        mRealm.executeTransaction(realm -> {
            Notifications notifications = realm.createObject(Notifications.class, key);
            notifications.setDate(notificationsObj.getDate());
            notifications.setDescription(notificationsObj.getDescription());
            notifications.setFilePath(notificationsObj.getFilePath());
            notifications.setTag(notificationsObj.getTag());
        });
    }

    public static void fetchFirebaseNotifications(DatabaseReference databaseReference, CustomCallBack.Listener<List<Notifications>> mResultListener, CustomCallBack.ErrorListener<DatabaseError> mError) {
        List<Notifications> notificationsModel = new ArrayList<>();
        realm.executeTransaction(realm -> realm.where(Notifications.class).findAll().deleteAllFromRealm());
        getAFireBaseData(databaseReference, response -> {
            for (DataSnapshot data : response.getChildren()) {
                Notifications notifications = data.getValue(Notifications.class);
                Notifications.setNotifications(notifications, data.getKey(), realm);
                notificationsModel.add(notifications);
            }
            mResultListener.onResponse(notificationsModel);
        }, (DatabaseError error) -> {
            mError.onErrorResponse(error);
        });
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getFilePath() {
        return filePath;
    }

    public void setFilePath(String filePath) {
        this.filePath = filePath;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getTag() {
        return tag;
    }

    public void setTag(String tag) {
        this.tag = tag;
    }

}
