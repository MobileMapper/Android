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
    private double batteryCapacity;
    private double batteryPercent;

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
    public double getBatteryCapacity() {
        return batteryCapacity;
    }
    public void setBatteryCapacity(double batteryCapacity) {
        this.batteryCapacity = batteryCapacity;
    }
    public double getBatteryPercent() {
        return batteryPercent;
    }
    public void setBatteryPercent(double batteryPercent) {
        this.batteryPercent = batteryPercent;
    }
}
