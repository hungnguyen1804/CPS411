package com.example.cps_lab411.EvenBus;

public class UpdateJoystickCommand {
    private double vx, vy, vz, yawrate;

    public void setJoystickRight (double vx, double vy) {
        this.vx = vx;
        this.vy = vy;
    }

    public void setJoystickLeft (double vz, double yawrate) {
        this.vz = vz;
        this.yawrate = yawrate;
    }

    public double getVx() {

        return vx;
    }

    public double getVy() {
        return vy;
    }

    public double getVz(){
        return vz;
    }

    public double getYawrate() {
        return yawrate;
    }
}
