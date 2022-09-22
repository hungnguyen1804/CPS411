package com.example.cps_lab411.EvenBus;

public class HandleManualControl {
    private int Vx, Vy, Vz, YawRate;

    public void setManualControl(int Vx, int Vy, int Vz, int YawRate) {
        this.Vx = Vx;
        this.Vy = Vy;
        this.Vz = Vz;
        this.YawRate = YawRate;
    }

    public void setLeftManualControl(int Vz, int YawRate) {
        this.Vz = Vz;
        this.YawRate = YawRate;
    }

    public void setRightManualControl(int Vx, int Vy) {
        this.Vx = Vx;
        this.Vy = Vy;
    }

    public void setVx(int Vx) {
        this.Vx = Vx;
    }

    public void setVy(int Vy) {
        this.Vy = Vy;
    }

    public void setVz(int Vz) {
        this.Vz = Vz;
    }

    public void setYawRate(int YawRate) {
        this.YawRate = YawRate;
    }

    public int getVx() {
        return Vx;
    }
    public int getVy() {
        return Vy;
    }
    public int getVz() {
        return Vz;
    }
    public int getYawRate() {
        return YawRate;
    }
}
