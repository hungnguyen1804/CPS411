package com.example.cps_lab411.Communication.Protocols.Commands;

import java.nio.ByteBuffer;

public class Uavlink_cmd_takeoff_t {
    private static int LENGHT = 6;
    private float Altitude;
    private byte[] data;

    public Uavlink_cmd_takeoff_t() {
        Altitude = 0.0f;
    }

    public byte[] getData() {
        return data;
    }

    public void setAltitude(float altitude) {
        Altitude = altitude;
    }

    public byte[] Encode() {
        ByteBuffer bufferData = ByteBuffer.allocate(LENGHT);
        bufferData.putFloat(Altitude);
        bufferData.putShort(CommandId.getCommandId(CommandId.UAVLINK_CMD_TAKEOFF));

        data = convertToByteArray(LENGHT, bufferData);
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
