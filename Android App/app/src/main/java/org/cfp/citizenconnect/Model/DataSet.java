package org.cfp.citizenconnect.Model;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;

import org.cfp.citizenconnect.CustomCallBack;

import java.util.ArrayList;
import java.util.UUID;

import io.realm.Case;
import io.realm.RealmObject;
import io.realm.RealmResults;
import io.realm.annotations.PrimaryKey;

import static org.cfp.citizenconnect.CitizenConnectApplication.database;
import static org.cfp.citizenconnect.CitizenConnectApplication.realm;
import static org.cfp.citizenconnect.Constants.DATA_MEDICAL_STORE;
import static org.cfp.citizenconnect.MyUtils.getAFireBaseData;

/**
 * Created by shahzaibshahid on 23/01/2018.
 */

public class DataSet extends RealmObject {
    @PrimaryKey
    String id;
    String dataSetType;
    String Address;
    String Name;

    public String get_Address() {
        return Address;
    }

    public void set_Address(String address) {
        Address = address;
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

    public void setDataSetType(String dataSetType) {
        this.dataSetType = dataSetType;
    }


    public String get_Name() {
        return Name;
    }

    public void set_Name(String name) {
        Name = name;
    }


    public static void getDataSet(String type, CustomCallBack.Listener<ArrayList<DataSet>> mList, CustomCallBack.ErrorListener<DatabaseError> mErr) {
        final ArrayList<DataSet>[] list = new ArrayList[]{new ArrayList<>()};
        getAFireBaseData(database.getReference(DATA_MEDICAL_STORE), response -> {
            for (DataSnapshot _child : response.getChildren()) {
                if (_child.child("type").getValue().equals(type)) {
                    DataSnapshot snapshot = _child.child("data");
                    for (DataSnapshot _snapshot : snapshot.getChildren()) {
                        DataSet dataSet = _snapshot.getValue(DataSet.class);
                        list[0].add(dataSet);

                        realm.executeTransaction(realm -> {
                            DataSet object = realm.createObject(DataSet.class, UUID.randomUUID().toString());
                            object.set_Name(dataSet.get_Name());
                            object.set_Address(dataSet.get_Address());
                            object.setDataSetType(type);
                        });
                    }
                    break;
                }

            }
            mList.onResponse(list[0]);
        }, error -> mErr.onErrorResponse(error));
    }

    public static ArrayList<DataSet> isObjectExist(String type) {
        ArrayList<DataSet> list = new ArrayList<>();
        RealmResults<DataSet> dataSet = realm.where(DataSet.class).equalTo("dataSetType", type, Case.INSENSITIVE).findAll();
        for (DataSet _data : dataSet) {
            list.add(_data);
        }
        return list;
    }

}
