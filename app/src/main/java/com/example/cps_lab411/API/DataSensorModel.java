package com.example.cps_lab411.API;

public class DataSensorModel {
    private String sensorName;
    private String sensorValue;

    public DataSensorModel() {
    }

    public String getSensorName() {
        return sensorName;
    }

    public void setSensorName(String sensorName) {
        this.sensorName = sensorName;
    }

    public String getSensorValue() {
        return sensorValue;
    }

    public void setSensorValue(String sensorValue) {
        this.sensorValue = sensorValue;
    }

    @Override
    public String toString() {
        return
                "Sensor Name= " + sensorName + "\n" + "Sensor Value= " + sensorValue;
    }
}