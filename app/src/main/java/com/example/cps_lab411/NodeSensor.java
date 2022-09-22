package com.example.cps_lab411;

import android.widget.TextView;

import com.example.cps_lab411.Sensor.SensorArlet;

public class NodeSensor {
    String SensorID;
    int ID;
    double Latitude, Longitude, Temperature, Humidity, Gas, Light, Dust;
    private boolean isAlert = false;
    private double[] SensorData;

    NodeSensor(String SensorID) {
        this.SensorID = SensorID;
        ID = Integer.parseInt(SensorID);
    }

    public void setSensorCoordinate(double Latitude, double Longitude) {
        this.Latitude = Latitude;
        this.Longitude = Longitude;
    }

    public double getSensorLatitude() {
        return Latitude;
    }

    public double getSensorLongitude() {
        return Longitude;
    }

    public void setTemperature(double Temperature) {
        this.Temperature = Temperature;
    }

    public void setHumidity(double Humidity) {
        this.Humidity = Humidity;
    }

    public void setGas(double Gas) {
        this.Gas = Gas;
    }

    public double getTemperature() {
        return Temperature;
    }

    public double getHumidity() {
        return Humidity;
    }

    public double getGas() {
        return Gas;
    }

    public boolean getAlert() { return isAlert;}

    public void setAlert(boolean isAlert) { this.isAlert = isAlert; }

    public int getID() { return ID; }

    public boolean checkSensorParameter() {
        return this.Temperature >= 40 || this.Humidity >= 95 || this.Gas >= 10;
    }

}
