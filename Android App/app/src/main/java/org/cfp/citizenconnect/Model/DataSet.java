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
import io.realm.RealmObject;
import io.realm.RealmResults;
import io.realm.annotations.PrimaryKey;
import static org.cfp.citizenconnect.CitizenConnectApplication.database;
import static org.cfp.citizenconnect.CitizenConnectApplication.realm;
import static org.cfp.citizenconnect.MyUtils.getAFireBaseData;

/**
 * Created by shahzaibshahid on 23/01/2018.
 */

@IgnoreExtraProperties
public class DataSet extends RealmObject {

    @PrimaryKey
    private String id;

    @PropertyName("dataSetType")
    private String dataSetType;

    @PropertyName("Address")
    private String address;

    @PropertyName("Name")
    private String name;

    public double getLatitude() {
        return latitude;
    }

    public void setLatitude(double latitude) {
        this.latitude = latitude;
    }

    public double getLongitude() {
        return longitude;
    }

    public void setLongitude(double longitude) {
        this.longitude = longitude;
    }

    @PropertyName("Latitude")
    private double latitude;

    @PropertyName("Longitude")
    private double longitude;

    public String getAddress() {
        return address;
    }


    public void setAddress(String address) {
        this.address = address;
    }


    public String getName() {
        return name;
    }


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
        getAFireBaseData(database.getReference("dataset"), response ->
                realm.executeTransactionAsync(realm -> {
                    for (DataSnapshot _child : response.getChildren()) {
                        DataSnapshot snapshot = _child.child("data");
                        for (DataSnapshot _snapshot : snapshot.getChildren()) {
                            final DataSet dataSet = _snapshot.getValue(DataSet.class);
                            final DataSet object = realm.createObject(DataSet.class,
                                    UUID.randomUUID().toString());
                            object.setName(dataSet.getName());
                            object.setAddress(dataSet.getAddress());
                            object.setLatitude(dataSet.getLatitude());
                            object.setLongitude(dataSet.getLongitude());
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
