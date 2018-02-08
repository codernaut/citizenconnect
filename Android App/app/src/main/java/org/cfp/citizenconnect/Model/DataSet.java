package org.cfp.citizenconnect.Model;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.IgnoreExtraProperties;
import com.google.firebase.database.PropertyName;

import org.cfp.citizenconnect.CustomCallBack;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import io.realm.Case;
import io.realm.Realm;
import io.realm.RealmObject;
import io.realm.RealmResults;
import io.realm.annotations.PrimaryKey;

import static org.cfp.citizenconnect.CitizenConnectApplication.database;
import static org.cfp.citizenconnect.CitizenConnectApplication.realm;
import static org.cfp.citizenconnect.Constants.DATA_MEDICAL_STORE;
import static org.cfp.citizenconnect.MyUtils.getAFireBaseData;
import static org.cfp.citizenconnect.MyUtils.getFirebaseDataOnce;

/**
 * Created by shahzaibshahid on 23/01/2018.
 */

@IgnoreExtraProperties
public class DataSet extends RealmObject {

    @PrimaryKey
    private String id;

    private String dataSetType;

    private String address;

    private String name;

    @PropertyName("Address")
    public String getAddress() {
        return address;
    }

    @PropertyName("Address")
    public void setAddress(String address) {
        this.address = address;
    }

    @PropertyName("Name")
    public String getName() {
        return name;
    }

    @PropertyName("Name")
    public void setName(String name) {
        this.name = name;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getDataSetType() {
        return dataSetType;
    }

    private void setDataSetType(String dataSetType) {
        this.dataSetType = dataSetType;
    }

    public static void getDataSet(CustomCallBack.Listener<Boolean> _response,
                                  CustomCallBack.ErrorListener<DatabaseError> mErr) {
        getFirebaseDataOnce(database.getReference(DATA_MEDICAL_STORE), response ->
                realm.executeTransactionAsync(realm -> {
                            for (DataSnapshot _child : response.getChildren()) {
                                DataSnapshot snapshot = _child.child("data");
                                for (DataSnapshot _snapshot : snapshot.getChildren()) {
                                    final DataSet dataSet = _snapshot.getValue(DataSet.class);
                                    final DataSet object = realm.createObject(DataSet.class,
                                            UUID.randomUUID().toString());
                                    object.setName(dataSet.getName());
                                    object.setAddress(dataSet.getAddress());
                                    object.setDataSetType(_child.child("type").getValue().toString());
                                }
                            }
                        }, () -> _response.onResponse(true)), mErr);
    }

    public static List<DataSet> fetchFromRealm(String type) {
        final List<DataSet> list = new ArrayList<>();
        RealmResults<DataSet> dataSet = realm.where(DataSet.class).equalTo("dataSetType",
                type, Case.INSENSITIVE).findAll();
        list.addAll(dataSet);
        return list;
    }

}
