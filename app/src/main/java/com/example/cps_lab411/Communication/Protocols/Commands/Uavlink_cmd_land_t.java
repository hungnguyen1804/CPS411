package com.example.cps_lab411.Communication.Protocols.Commands;

import java.nio.ByteBuffer;

public class Uavlink_cmd_land_t {

    private byte[] data;

    public byte[] getData() {
        return data;
    }

    public void Encode() {
        int n = 2;
        // Data packing with byte buffer
        ByteBuffer bufferData = ByteBuffer.allocate(n);
        bufferData.putShort(CommandId.getCommandId(CommandId.UAVLINK_CMD_LAND));
        data = convertToByteArray(n, bufferData);
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
