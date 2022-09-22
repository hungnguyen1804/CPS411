package com.example.cps_lab411.Communication.Protocols.Commands;

import java.nio.ByteBuffer;

public class Uavlink_cmd_arm_disarm_t {
    private byte armDisarm;
    private byte[] data;

    public byte[] getData() {
        return data;
    }

    public void setArmDisarm(byte armDisarm) {
        this.armDisarm = armDisarm;
    }

    public void Encode() {
        int n = 3;
        //byte[] byteSetMode = ByteBuffer.allocate(4).order(ByteOrder.nativeOrder()).putInt(CommandId.getCommandId(CommandId.UAVLINK_CMD_SETMODE)).array();;
        // Data packing with byte buffer
        ByteBuffer bufferData = ByteBuffer.allocate(n);
        bufferData.put(armDisarm);
        bufferData.putShort((short) CommandId.getCommandId(CommandId.UAVLINK_CMD_ARM));

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
