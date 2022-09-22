package com.example.cps_lab411.Communication.Protocols.Messages;

import java.nio.ByteBuffer;

public class Uavlink_msg_global_position_t {

    private double _lat;
    private double _lon;
    private float _alt;
    private float _vx;
    private float _vy;
    private float _vz;
    private float _rotation;

    public double getGlobalLat() { return _lat; }
    public double getGlobalLon() { return _lon; }
    public float getGlobalAlt() { return _alt; }
    public float getGlobalVx() { return _vx; }
    public float getGlobalVy() { return _vy; }
    public float getGlobalVz() { return _vz; }
    public float getRotation() { return _rotation; }

    public void Decode(byte[] data) {
        byte[] lat = new byte[4], lon = new byte[4], alt = new byte[4], vx = new byte[4], vy = new byte[4], vz = new byte[4], rotation = new byte[4];

        System.arraycopy(data, 0, lat, 0, 4);
        System.arraycopy(data, 4, lon, 0, 4);
        System.arraycopy(data, 8, alt, 0, 2);
        System.arraycopy(data, 10, vx, 0, 2);
        System.arraycopy(data, 12, vy, 0, 2);
        System.arraycopy(data, 14, vz, 0, 2);
        System.arraycopy(data, 16, rotation, 0, 2);

        _lat = ((double) getIntData(reverseByte(lat))) / 10000000.0f;
        _lon = ((double) getIntData(reverseByte(lon))) / 10000000.0f;
        _alt = ((float) getIntData(reverseByte(alt))) / 100.0f;
        _vx = ((float) getIntData(reverseByte(vx))) / 100.0f;
        _vy = ((float) getIntData(reverseByte(vy))) / 100.0f;
        _vz = ((float) getIntData(reverseByte(vz))) / 100.0f;
        _rotation = ((float) getIntData(reverseByte(rotation)));

        _vx = (_vx >= 50)? 0 : _vx;
        _vy = (_vy >= 50)? 0 : _vy;
        _vz = (_vz >= 50)? 0 : _vz;
        _alt = (_alt >= 100)? 0 : _alt;
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

    public byte[] reverseByte(byte[] myArray) {
        byte[] b = new byte[4];
        int j = 4;
        for (int i = 0; i < 4; i++) {
            b[j - 1] = myArray[i];
            j = j - 1;
        }
        return b;
    }

    public byte[] reverseByteInt16(byte[] myArray) {
        byte[] b = new byte[4];
        int j = 2;
        for (int i = 0; i < 2; i++) {
            b[j - 1] = myArray[i];
            j = j - 1;
        }
        return b;
    }

}
