package com.example.cps_lab411.EvenBus;

public class UpdateGPS {
    private double Latitude, Longitude, Altitute, Vx, Vy, Vz, Battery;;
    private float Rotation;

    public void setUpdateGPS(double Latitude, double Longitude, float Rotation, double Altitude, double Vx, double Battery) {
        this.Latitude = Latitude;
        this.Longitude = Longitude;
        this.Rotation = Rotation;
        this.Altitute = Altitude;
        this.Vx = Vx;
        this.Battery = Battery;
    }

    public double getLat() {
        return Latitude;
    }

    public double getLon() {
        return Longitude;
    }

    public float getRotation() {
        return Rotation;
    }

    public double getAltitute() { return  Altitute; }

    public double getVx() { return Vx; }

    public double getBattery() { return Battery; }
}
