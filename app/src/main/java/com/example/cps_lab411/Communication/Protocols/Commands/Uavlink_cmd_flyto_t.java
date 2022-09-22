package com.example.cps_lab411.Communication.Protocols.Commands;

import java.nio.ByteBuffer;

public class Uavlink_cmd_flyto_t {
    private int Allwp;
    private int WPid;
    private byte[] data;
    private int LENGHT = 18;
    private int type = 1;

    public Uavlink_cmd_flyto_t() {
        Allwp = 1;
        WPid = 1;
    }

    public void setAllwp(int allwp) {
        Allwp = allwp;
    }

    public void setWpid(int wpid) {
        WPid = wpid;
    }

    public byte[] getData() {
        return data;
    }

    public byte[] Encode() {
        //byte[] byteSetMode = ByteBuffer.allocate(4).order(ByteOrder.nativeOrder()).putInt(CommandId.getCommandId(CommandId.UAVLINK_CMD_SETMODE)).a                                                                    rray();;
        // Data packing with byte buffer
        ByteBuffer bufferData = ByteBuffer.allocate(LENGHT);
        bufferData.putFloat(1);
        bufferData.putFloat(1);
        bufferData.putFloat(1);
        bufferData.putFloat(1);
        bufferData.putShort(CommandId.getCommandId(CommandId.UAVLINK_CMD_FLYTO));

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