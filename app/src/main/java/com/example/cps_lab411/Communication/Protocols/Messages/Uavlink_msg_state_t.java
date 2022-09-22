package com.example.cps_lab411.Communication.Protocols.Messages;

public class Uavlink_msg_state_t {
    private int UAVLINK_MSG_ID_STATE_LEN = 4;
    private byte _connected;
    private byte _armed;
    private byte _mode;
    private byte _battery;

    public Uavlink_msg_state_t() {

    }

    public byte getConnected() { return _connected; }

    public byte getArmMode() { return _armed; }

    public byte getMode() { return _mode; }

    public byte getBattery() { return _battery; }


    public void Decode(byte[] data)
    {
        _connected = (byte) data[0];
        _armed = (byte) data[1];
        _mode = (byte) data[2];
        _battery = (byte) data[3];
    }

}
