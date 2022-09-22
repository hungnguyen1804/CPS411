package com.example.cps_lab411.Communication.Protocols.Messages;

import java.nio.ByteBuffer;

public class Uavlink_msg_manual_control_t {
    private static final int LENGTH = 16;
    private byte[] data = new byte[LENGTH];

    public byte[] getData() {
        return data;
    }

    public void Encode(int vx, int vy, int vz, int vr) {
        // Data packing with byte buffer
        ByteBuffer bufferData = ByteBuffer.allocate(LENGTH);
        bufferData.putInt(vr);
        bufferData.putInt(vz);
        bufferData.putInt(vy);
        bufferData.putInt(vx);

        data = convertToByteArray(LENGTH, bufferData);
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

    private byte[] convertToByteArray2(int n , ByteBuffer bufferData) {
        byte[] b = new byte[n];
        for(int i = 0; i < n; i++) {
            b[i] = bufferData.array()[i];
        }
        return b;
    }
}
