package com.tongjiapp.remirobert.devicecollect.deviceMapper;

import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;

/**
 * Created by remirobert on 01/07/16.
 */
public class Record extends RealmObject {

    @PrimaryKey
    private String id;

    private double latitude;
    private double longitude;

    public Record(String id) {
        this.id = id;
    }

    public Record() {}

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
}
