package org.cfp.citizenconnect.Model;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.PropertyName;

import org.cfp.citizenconnect.CustomCallBack;

import java.util.ArrayList;
import java.util.UUID;

import io.realm.Realm;
import io.realm.RealmObject;
import io.realm.RealmResults;
import io.realm.annotations.PrimaryKey;

import static org.cfp.citizenconnect.CitizenConnectApplication.realm;
import static org.cfp.citizenconnect.MyUtils.getAFireBaseData;

/**
 * Created by shahzaibshahid on 22/01/2018.
 */

public class Layout extends RealmObject {

    @PrimaryKey
    String id;
    String color;
    String icon;
    String name;
    String type;
    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }
    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getColor() {
        return color;
    }

    public void setColor(String color) {
        this.color = color;
    }

    public String getIcon() {
        return icon;
    }

    public void setIcon(String icon) {
        this.icon = icon;
    }

    @PropertyName("data_set_name")
    public String getName() {
        return name;
    }

    @PropertyName("data_set_name")
    public void setName(String name) {
        this.name = name;
    }

    public static void getLayout(String type,DatabaseReference databaseReference, CustomCallBack.Listener<ArrayList<Layout>> mList, CustomCallBack.ErrorListener<DatabaseError> mErr) {
        ArrayList<Layout> list = new ArrayList<>();
        RealmResults<Layout> results = realm.where(Layout.class).equalTo("type",type).findAll();
        if(results.size()>0){
            for(Layout layout :results){
                list.add(layout);
            }
            mList.onResponse(list);
        }
        else {
            getAFireBaseData(databaseReference, response -> {
                for (DataSnapshot _child : response.getChildren()) {
                    Layout mLayout = _child.getValue(Layout.class);
                    Layout realmDataSetlayout = Layout.getInstance(realm);
                    realm.executeTransaction(realm -> {
                        realmDataSetlayout.setColor(mLayout.getColor());
                        realmDataSetlayout.setIcon(mLayout.getIcon());
                        realmDataSetlayout.setName(mLayout.getName());
                        realmDataSetlayout.setType(type);
                    });
                    list.add(mLayout);
                }
                mList.onResponse(list);
            }, error -> mErr.onErrorResponse(error));
        }
    }

    public static Layout getInstance(Realm realm){
        realm.beginTransaction();
        Layout layout = realm.createObject(Layout.class, UUID.randomUUID().toString());
        realm.commitTransaction();
        return layout;
    }
}
