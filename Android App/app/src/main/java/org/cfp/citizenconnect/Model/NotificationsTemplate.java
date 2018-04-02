package org.cfp.citizenconnect.Model;

import io.realm.RealmObject;

/**
 * Created by shahzaibshahid on 03/01/2018.
 */

public class NotificationsTemplate extends RealmObject {

    String id;
    String filePath;
    String date;
    String description;
    String tag;

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
