package com.example.cps_lab411.Communication.Protocols.Commands;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;

public class Uavlink_cmd_setmode_t {
    private byte Mode;
    private byte[] data = new byte[3];
    private static int LENGHT = 18;

    public Uavlink_cmd_setmode_t()
    {
    }

    public byte[] getData() {
        return data;
    }

    public void setMode(byte mode) {
        Mode = mode;
    }

    public void Encode() {
        ByteBuffer bufferData = ByteBuffer.allocate(LENGHT);
        bufferData.putInt(0);
        bufferData.putInt(0);
        bufferData.putInt(0);
        bufferData.putFloat(Mode);
        bufferData.putShort(CommandId.getCommandId(CommandId.UAVLINK_CMD_SETMODE));

        data = convertToByteArray(LENGHT, bufferData);
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
