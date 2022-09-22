package com.example.cps_lab411;

public class UpdateSensor {
    private String SSID;     // Sensor Id
    private int ID;
    private float Temp, Hum, Gas;
    private double  SSLatitude, SSLongitude;

    public void setSensorParameter(int ID, double SSLatitude, double SSLongitude, float Temp, float Hum, float Gas) {
        this.ID = ID;
        this.SSLatitude = SSLatitude;
        this.SSLongitude = SSLongitude;
        this.Temp = Temp;
        this.Hum = Hum;
        this.Gas = Gas;
        SSID = String.valueOf(ID);
    }

    public String getSSID() {
        return SSID;
    }

    public double getSSLatitude() {
        return SSLatitude;
    }

    public double getSSLongitude() {
        return SSLongitude;
    }

    public float getTemp() {
        return Temp;
    }

    public float getHum() {
        return Hum;
    }

    public float getGas() { return Gas;}

    public int getID() { return ID; }

}
