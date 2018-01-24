package org.cfp.citizenconnect.Model;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.PropertyName;

import org.cfp.citizenconnect.CustomCallBack;

import java.util.ArrayList;

import static org.cfp.citizenconnect.CitizenConnectApplication.database;
import static org.cfp.citizenconnect.Constants.DATASET_REFFERENCE;
import static org.cfp.citizenconnect.MyUtils.getAFireBaseData;

/**
 * Created by shahzaibshahid on 22/01/2018.
 */

public class Layout {
    String color;
    String icon;
    String name;

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

    public static void getDataSetLayout(DatabaseReference databaseReference,CustomCallBack.Listener<ArrayList<Layout>> mList, CustomCallBack.ErrorListener<DatabaseError> mErr) {
        ArrayList<Layout> list = new ArrayList<>();
        getAFireBaseData(databaseReference, response -> {
            for (DataSnapshot _child : response.getChildren()) {
                Layout mLayout = _child.getValue(Layout.class);
                list.add(mLayout);
            }
            mList.onResponse(list);
        }, error -> mErr.onErrorResponse(error));
    }
}
