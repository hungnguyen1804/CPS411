package com.example.cps_lab411;

import com.google.android.gms.maps.model.LatLng;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;
import java.util.concurrent.LinkedBlockingQueue;

public class DataHolder {
    private double lat, lon;
    private int angle, strength;
    private String commandJoystickLeft;
    private int vx, vy, vz, yawRate;
    private boolean  joyStickEnable, SendingNavigationPath, SendArmCommand, RobotAimEnable;
    private List<LatLng> latLngList = new ArrayList<>();
    private int ControlMode;
    private int servo1, servo2, servo3, servo4;
    private int connectionType;
    private Queue<byte[]> _sendQueue = new LinkedBlockingQueue<>(50);
    private Queue<byte[]> _recvQueue = new LinkedBlockingQueue<>(50);
    private byte[] recvByte;
    private boolean ConnectionStatus;


    public double getLat()
    {
        return lat;
    }
    public double getLon() { return lon; }
    public int getAngle() { return angle; }
    public int getStrength() { return strength; }
    public String getCommandJoystickLeft() { return commandJoystickLeft; }
    public int getVx() { return vx; }
    public int getVy() { return vy; }
    public int getVz() { return vz; }
    public int getYawRate() { return yawRate; }
    public List<LatLng> getLatLngList() { return latLngList; }
    public boolean getJoyStickEnable() { return joyStickEnable; }
    public boolean getSendingNavigationPath() {return SendingNavigationPath; }
    public boolean getSendArmCommand() { return SendArmCommand; }
    public boolean getRobotAimEnable() { return RobotAimEnable; }
    public int getControlMode() { return ControlMode; }
    public int getServo1() { return servo1; }
    public int getServo2() { return servo2; }
    public int getServo3() { return servo3; }
    public int getServo4() {return servo4; }
    public int getConnectionType() { return connectionType; }
    public boolean getConnectionStatus() { return ConnectionStatus; }

    public byte[] takeRecvQueue()  {
            return  _recvQueue.poll();
    }

    public byte[] takeSendQueue()  {
        return _sendQueue.poll();
    }

    public byte[] getRecvByte() {
            return recvByte;
    }


    public void setLat(double data)
    {
        this.lat = data;
    }
    public void setLon(double data) { this.lon = data; }
    public void setAngle(int angle) { this.angle = angle; }
    public void setStrength(int strength) { this.strength = strength; }
    public void setCommandJoystickLeft(String commandJoystickLeft) { this.commandJoystickLeft = commandJoystickLeft; }
    public void setVx(int vx) { this.vx = vx; }
    public void setVy(int vy) { this.vy = vy; }
    public void setVz(int vz) { this.vz = vz; }
    public void setYawRate(int yawRate) { this.yawRate = yawRate; }
    public void setLatLngList(List<LatLng> latLngList) { this.latLngList = latLngList; }
    public void setJoyStickEnable(boolean joyStickEnable) {this.joyStickEnable = joyStickEnable; }
    public void setSendingNavigationPath(boolean SendingNavigationPath) { this.SendingNavigationPath = SendingNavigationPath; }
    public void setSendArmCommand(boolean SendArmCommand) {
        this.SendArmCommand = SendArmCommand;
    }
    public void setRobotAimEnable(boolean RobotAimEnable) { this.RobotAimEnable = RobotAimEnable; }
    public void setControlMode( int ControlMode) { this.ControlMode = ControlMode; }
    public void setRobotAimServo12(int servo1, int servo2) {
        this.servo1 = servo1;
        this.servo2 = servo2;
    }

    public void setRobotAimServo34(int servo3, int servo4) {
        this.servo3 = servo3;
        this.servo4 = servo4;
    }
    public void setRobotAimServo(int servo1, int servo2, int servo3) {
        this.servo1 = servo1;
        this.servo2 = servo2;
        this.servo3 = servo3;
    }

    public void setConnectionType(int connectionType) {
        this.connectionType = connectionType;
    }

    public void putRecvQueue(byte[] recvData) {
        _recvQueue.offer(recvData);
    }
    public void putSendQueue(byte[] sendData ) {
        _sendQueue.offer(sendData);
    }
    public void setRecvByte(byte[] recvByte) {
        this.recvByte = recvByte;
    }

    public void setConnectionStatus(boolean connection) { ConnectionStatus = connection; }

    public static final DataHolder holder = new DataHolder();
    public static DataHolder getInstance()
    {
        return holder;
    }

}
