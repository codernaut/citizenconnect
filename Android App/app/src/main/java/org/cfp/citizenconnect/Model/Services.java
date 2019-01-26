package org.cfp.citizenconnect.Model;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.PropertyName;

import org.cfp.citizenconnect.CustomCallBack;

import java.util.UUID;

import io.realm.Realm;
import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;

import static org.cfp.citizenconnect.CitizenConnectApplication.database;
import static org.cfp.citizenconnect.CitizenConnectApplication.realm;
import static org.cfp.citizenconnect.Constants.SERVICES_REFFERENCE;
import static org.cfp.citizenconnect.MyUtils.getAFireBaseData;

/**
 * Created by shahzaibshahid on 24/01/2018.
 */

public class Services extends RealmObject {
    @PrimaryKey
    String id;
    String type;
    String fileUrl;

    public static void getServices(CustomCallBack.Listener<Boolean> status, CustomCallBack.ErrorListener<DatabaseError> mErr) {
        getAFireBaseData(database.getReference(SERVICES_REFFERENCE), response -> {
            for (DataSnapshot _child : response.getChildren()) {
                Services services = _child.getValue(Services.class);
                realm.executeTransaction(realm -> {
                    Services _services = realm.createObject(Services.class, UUID.randomUUID().toString());
                    _services.setType(services.getType());
                    _services.setFileUrl(services.getFileUrl());
                });
            }
            status.onResponse(true);
        }, error -> mErr.onErrorResponse(error));
    }

    public static Services isObjectExist(String type, Realm realm) {

        return realm.where(Services.class).equalTo("type", type).findFirst();

    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    @PropertyName("data_set_name")
    public String getType() {
        return type;
    }

    @PropertyName("data_set_name")
    public void setType(String type) {
        this.type = type;
    }

    public String getFileUrl() {
        return fileUrl;
    }

    public void setFileUrl(String fileUrl) {
        this.fileUrl = fileUrl;
    }

}
