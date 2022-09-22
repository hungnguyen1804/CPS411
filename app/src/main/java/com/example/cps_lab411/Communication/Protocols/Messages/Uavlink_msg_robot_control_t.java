package com.example.cps_lab411.Communication.Protocols.Messages;

import java.nio.ByteBuffer;

public class Uavlink_msg_robot_control_t {
    private static final int LENGTH = 20;
    private byte[] data = new byte[LENGTH];

    public byte[] getData() {
        return data;
    }

    public void Encode(int servo1, int servo2, int servo3, int servo4, int servo5) {
        // Data packing with byte buffer
        ByteBuffer bufferData = ByteBuffer.allocate(LENGTH);
        bufferData.putInt(servo5);
        bufferData.putInt(servo4);
        bufferData.putInt(servo3);
        bufferData.putInt(servo2);
        bufferData.putInt(servo1);

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
}
