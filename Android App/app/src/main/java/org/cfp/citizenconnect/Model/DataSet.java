package org.cfp.citizenconnect.Model;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.PropertyName;

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
    @PropertyName("Address")
    String Address;
    @PropertyName("Name")
    String Name;
    @PropertyName("Registration Status(Reg No,& Date & Relevant Law")
    String Registration;

    @PropertyName("Registration Status(Reg No,& Date & Relevant Law")
    public String get_Registration() {
        return Registration;
    }

    @PropertyName("Registration Status(Reg No,& Date & Relevant Law")
    public void set_Registration(String registration) {
        Registration = registration;
    }

    @PropertyName("Address")
    public String get_Address() {
        return Address;
    }

    @PropertyName("Address")
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

    @PropertyName("Name")
    public String get_Name() {
        return Name;
    }

    @PropertyName("Name")
    public void set_Name(String name) {
        Name = name;
    }


    public static void getDataSet( CustomCallBack.Listener<Boolean> _response, CustomCallBack.ErrorListener<DatabaseError> mErr) {
        getAFireBaseData(database.getReference(DATA_MEDICAL_STORE), response -> {
            for (DataSnapshot _child : response.getChildren()) {
                DataSnapshot snapshot = _child.child("data");
                for (DataSnapshot _snapshot : snapshot.getChildren()) {
                    DataSet dataSet = _snapshot.getValue(DataSet.class);
                    realm.executeTransaction(realm -> {
                        DataSet object = realm.createObject(DataSet.class, UUID.randomUUID().toString());
                        object.set_Name(dataSet.get_Name());
                        object.set_Address(dataSet.get_Address());
                        object.setDataSetType(_child.child("type").getValue().toString());
                    });
                }
            }
            _response.onResponse(true);
        }, error -> {
            mErr.onErrorResponse(error);
        });
    }

    public static ArrayList<DataSet> fetchFromRealm(String type) {
        ArrayList<DataSet> list = new ArrayList<>();
        RealmResults<DataSet> dataSet = realm.where(DataSet.class).equalTo("dataSetType", type, Case.INSENSITIVE).findAll();
        for (DataSet _data : dataSet) {
            list.add(_data);
        }
        return list;
    }

}
