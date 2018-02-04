package org.cfp.citizenconnect.Model;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
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
import static org.cfp.citizenconnect.Constants.DATA_MEDICAL_STORE;
import static org.cfp.citizenconnect.MyUtils.getAFireBaseData;

/**
 * Created by shahzaibshahid on 23/01/2018.
 */

public class DataSet extends RealmObject {

    @PrimaryKey
    private String id;

    private String dataSetType;

    @PropertyName("Address") //TODO There is another Address field with a space at the end.
    private String address;

    @PropertyName("Name")
    private String name;

    @PropertyName("Registration Status(Reg No,& Date & Relevant Law ") //TODO why such a long and problematic key name and there is a space at the end
    private String registration;

    @PropertyName("Male")
    private Double male; //TODO why double?

    @PropertyName("Female")
    private Double female; //TODO why double?

    @PropertyName("Total Employees")
    private Double totalEmployees;

    @PropertyName("Registration Status(Reg No,& Date & Relevant Law ")
    public String getRegistration() {
        return registration;
    }

    @PropertyName("Registration Status(Reg No,& Date & Relevant Law ")
    public void setRegistration(String registration) {
        this.registration = registration;
    }

    @PropertyName("Address")
    public String getAddress() {
        return address;
    }

    @PropertyName("Address")
    private void setAddress(String address) {
        this.address = address;
    }

    @PropertyName("Name")
    public String getName() {
        return name;
    }

    @PropertyName("Name")
    private void setName(String name) {
        this.name = name;
    }

    @PropertyName("Male")
    public Double getMale() {
        return male;
    }

    @PropertyName("Male")
    public void setMale(Double male) {
        this.male = male;
    }

    @PropertyName("Female")
    public Double getFemale() {
        return female;
    }

    @PropertyName("Female")
    public void setFemale(Double female) {
        this.female = female;
    }

/*    @PropertyName("Total Employees")
    public Double getTotalEmployees() {
        return totalEmployees;
    }

    @PropertyName("Total Employees")
    public void setTotalEmployees(Double totalEmployees) {
        this.totalEmployees = totalEmployees;
    }*/

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
        getAFireBaseData(database.getReference(DATA_MEDICAL_STORE), response -> {
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
                            _response.onResponse(true);
                        }
                    }
            );
        }, mErr);
    }

    public static List<DataSet> fetchFromRealm(String type) {
        final List<DataSet> list = new ArrayList<>();
        RealmResults<DataSet> dataSet = realm.where(DataSet.class).equalTo("dataSetType",
                type, Case.INSENSITIVE).findAll();
        list.addAll(dataSet);
        return list;
    }

}
