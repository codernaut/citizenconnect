package org.cfp.citizenconnect.Model;

import io.realm.Realm;
import io.realm.RealmObject;


/**
 * Created by shahzaibshahid on 03/01/2018.
 */

public class NotificationUpdate extends RealmObject {
    int newNotiifcation;

    public int getLastState() {
        return lastState;
    }

    public void setLastState(int lastState) {
        this.lastState = lastState;
    }

    int lastState;

    public int getNewNotiifcation() {
        return newNotiifcation;
    }

    public void setNewNotiifcation(int newNotiifcation) {
        this.newNotiifcation = newNotiifcation;
    }

    public static NotificationUpdate getInstance(Realm realm) {
        final NotificationUpdate[] notificationUpdate = {realm.where(NotificationUpdate.class).findFirst()};
        if (notificationUpdate[0] == null) {
            realm.executeTransaction(new Realm.Transaction() {
                @Override
                public void execute(Realm realm) {
                    notificationUpdate[0] = realm.createObject(NotificationUpdate.class);
                }
            });
        }
        return notificationUpdate[0];
    }
}
