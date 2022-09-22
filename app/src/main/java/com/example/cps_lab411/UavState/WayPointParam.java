package com.example.cps_lab411.UavState;

import com.google.android.gms.maps.model.Marker;

public class WayPointParam {
    private int _wpID;
    private float Latitude, Longitude;
    private float Altitude = 1;
    private float Speed = 0.8f;
    Marker marker;

    public static int idCount = 1;

    public WayPointParam() {
        _wpID = idCount;
        idCount++;
    }

    public WayPointParam(float latitude, float longitude, float altitude, float speed) {
        Latitude = latitude;
        Longitude = longitude;
        Altitude = altitude;
        Speed = speed;
    }

    public void setAltitude(float altitude) {
        Altitude = altitude;
    }

    public void setSpeed(float speed) {
        Speed = speed;
    }

    public void setLatitude(float latitude) {
        Latitude = latitude;
    }

    public void setLongitude(float longitude) {
        Longitude = longitude;
    }

    public float getAltitude() {
        return Altitude;
    }

    public float getSpeed() {
        return Speed;
    }

    public float getLatitude() {
        return Latitude;
    }

    public float getLongitude() {
        return Longitude;
    }

    public int get_wpID() {
        return _wpID;
    }

    public void set_wpID(int _wpID) {
        this._wpID = _wpID;
    }
}
