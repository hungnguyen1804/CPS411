package com.example.cps_lab411.Communication.Protocols.Messages;

import com.example.cps_lab411.Communication.Protocols.Commands.Uavlink_cmd_takeoff_t;
import com.google.android.gms.maps.model.LatLng;

import java.nio.ByteBuffer;

public class Uavlink_msg_setwp {
    private static int LENGTH = 18;
    private int WaypointID;
    private float TargetLongitude;
    private float TargetLatitude;
    private float TargetAltitude;
    private float TargetSpeed;
    private byte[] data;

    public Uavlink_msg_setwp() {
        WaypointID = 0;
        TargetLongitude = 0;
        TargetLatitude = 0;
        TargetSpeed = 0;
    }

    public byte[] getData() {
        return data;
    }

    public void setTarget(float latitude, float longitude, float altitude, float speed) {
        TargetLongitude = longitude;
        TargetLatitude = latitude;
        TargetSpeed = speed;
        TargetAltitude = altitude;
    }

    public void setWPID(int ID) {
        WaypointID = ID;
    }

    public byte[] Encode() {
        int type = 1;
        // Data packing with byte buffer
        ByteBuffer bufferData = ByteBuffer.allocate(LENGTH);
        bufferData.putFloat(TargetAltitude);
        bufferData.putFloat(TargetLongitude);
        bufferData.putFloat(TargetLatitude);
        bufferData.putFloat(WaypointID);
        bufferData.putShort((short) type);

        data = convertToByteArray(LENGTH, bufferData);
        return data;
    }

    private byte[] convertToByteArray(int n, ByteBuffer bufferData) {
        byte[] b = new byte[n];
        int j = n;
        for (int i = 0; i < n; i++) {
            b[j - 1] = bufferData.array()[i];
            j = j - 1;
        }
        return b;
    }
}