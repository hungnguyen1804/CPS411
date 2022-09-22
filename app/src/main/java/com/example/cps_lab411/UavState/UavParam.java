package com.example.cps_lab411.UavState;

import com.example.cps_lab411.DataHolder;

import java.util.List;

public class UavParam {
    private int AimMode;
    private UavMode UavMode;
    private int Vx, Vy, Vz, YawRate;
    private List<WayPointParam> WayPointList;

    public void setAimMode(int aimMode) {
        AimMode = aimMode;
    }

    public int getAimMode() {
        return AimMode;
    }

    public void setUavMode(UavMode uavMode) {
        UavMode = uavMode;
    }

    public UavMode getUavMode() {
        return UavMode;
    }

    public void setVx(int vx) {
        Vx = vx;
    }

    public int getVx() {
        return Vx;
    }

    public void setVy(int vy) {
        Vy = vy;
    }

    public int getVy() {
        return Vy;
    }

    public void setVz(int vz) {
        Vz = vz;
    }

    public int getVz() {
        return Vz;
    }

    public void setYawRate(int yawRate) {
        YawRate = yawRate;
    }

    public int getYawRate() {
        return YawRate;
    }

    public void setLeftJoystick(int Vz, int YawRate) {
        this.Vz = Vz;
        this.YawRate = YawRate;
    }

    public void setRightJoystick(int Vx, int Vy) {
        this.Vx = Vx;
        this.Vy = Vy;
    }

    public void setWayPointList(List<WayPointParam> wayPointList) {
        WayPointList = wayPointList;
    }

    public List<WayPointParam> getWayPointList() {
        return WayPointList;
    }

    public static final UavParam param = new UavParam();
    public static UavParam getInstance()
    {
        return param;
    }

}
