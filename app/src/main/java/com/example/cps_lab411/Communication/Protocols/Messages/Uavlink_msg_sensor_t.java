package com.example.cps_lab411.Communication.Protocols.Messages;

import java.nio.ByteBuffer;
import java.text.DecimalFormat;

public class Uavlink_msg_sensor_t {
    private static final int LENGTH = 16;
    private byte[] data = new byte[LENGTH];
    private double _lat;
    private double _lon;
    private int _id;
    private float _temp;
    private float _hum;
    private float _gas;


    public int getSensorID() {
        return _id;
    }

    public float getTemp() {
        return _temp;
    }

    public float getHum() {
        return _hum;
    }

    public float getGas() {
        return _gas;
    }

    public double getLat() {
        return _lat;
    }

    public double getLon() {
        return _lon;
    }

    public byte[] getData() {
        return data;
    }

    public void Decode(byte[] data) {
        byte[] idSensor = new byte[4], lat = new byte[4], lon = new byte[4], temp = new byte[4], hum = new byte[4], gas = new byte[4];
        final DecimalFormat df = new DecimalFormat("0.00");

        System.arraycopy(data, 0, idSensor, 0, 2);
        System.arraycopy(data, 2, lat, 0, 4);
        System.arraycopy(data, 6, lon, 0, 4);
        System.arraycopy(data, 10, temp, 0, 2);
        System.arraycopy(data, 12, hum, 0, 2);
        System.arraycopy(data, 14, gas, 0, 2);

        _lat = ((double) getIntData(reverseByte(lat))) / 10000000.0f;
        _lon = ((double) getIntData(reverseByte(lon))) / 10000000.0f;
        _id =  getIntData(reverseByte(idSensor));
        _temp = ((float) getIntData(reverseByte(temp))) / 100.0f;
        _hum = ((float) getIntData(reverseByte(hum))) / 100.0f;
        _gas = ((float) getIntData(reverseByte(gas))) / 100.0f;


    }

    public byte[] reverseByte(byte[] myArray) {
        byte[] b = new byte[4];
        int j = 4;
        for (int i = 0; i < 4; i++) {
            b[j - 1] = myArray[i];
            j = j - 1;
        }
        return b;
    }

    public float getFloatData(byte[] byteArray) {
        ByteBuffer buffer = ByteBuffer.wrap(byteArray);
        return buffer.getFloat();
    }

    //Get Int Data Received
    public double ToDoubleData(byte[] byteArray) {
        ByteBuffer buffer = ByteBuffer.wrap(byteArray);
        return buffer.getDouble();
    }

    public int getIntData(byte[] byteArray) {
        ByteBuffer buffer = ByteBuffer.wrap(byteArray);
        return buffer.getInt();
    }

    public int getShortData(byte[] byteArray) {
        ByteBuffer buffer = ByteBuffer.wrap(byteArray);
        return buffer.getShort();
    }

}
