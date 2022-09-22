package com.example.cps_lab411.EvenBus;

public class UDPMessage2EvenBus {
    private double latitude, longitude;
    private float vx, vy, vz, alt, rotation;

    public void setMessage2(double latitude, double longitude, float alt, float vx, float vy, float vz, float rotation) {
        this.latitude = latitude;
        this.longitude = longitude;
        this.alt = alt;
        this.vx = vx;
        this.vy = vy;
        this.vz = vz;
        this.rotation = rotation;
    }

    public double getLatitude() { return latitude; }
    public double getLongitude() { return longitude; }
    public float getAltitude() { return alt; }
    public float getVx() { return vx; }
    public float getVy() { return vy; }
    public float getVz() { return vz; }
    public float getRotation() { return rotation; }
}
