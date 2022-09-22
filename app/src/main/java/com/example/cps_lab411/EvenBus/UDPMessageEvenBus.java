package com.example.cps_lab411.EvenBus;

public class UDPMessageEvenBus {
    private int connected, arm, mode, batteryRemaining;

    public void setMessage1(int connected, int arm, int mode, int batteryRemaining) {
        this.connected = connected;
        this.arm = arm;
        this.mode = mode;
        this.batteryRemaining = batteryRemaining;
    }

    public int getConnected() { return connected; }
    public int getArm() { return arm; }
    public int getMode() { return mode; }
    public int getBatteryRemaining() { return batteryRemaining; }

}
