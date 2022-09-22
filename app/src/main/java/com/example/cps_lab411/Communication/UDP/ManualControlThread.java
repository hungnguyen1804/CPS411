package com.example.cps_lab411.Communication.UDP;

import com.example.cps_lab411.Communication.EncodeData;
import com.example.cps_lab411.ConnFragment;
import com.example.cps_lab411.DataHolder;
import com.example.cps_lab411.UavState.RobotArmParam;
import com.example.cps_lab411.UavState.UavParam;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.Socket;
import java.util.concurrent.atomic.AtomicBoolean;

public class ManualControlThread implements Runnable {

    private String SERVER_IP;
    private int SERVER_PORT;
    private Thread worker;
    private AtomicBoolean running = new AtomicBoolean(false);
    private EncodeData encodeManualControlData = new EncodeData();
    private int x, y ,z , yaw;
    private int servo1, servo2, servo3, servo4, servo5;

    public void start() {
        worker = new Thread(this);
        worker.start();
    }

    public ManualControlThread(String SERVER_IP, int SERVER_PORT) {
        this.SERVER_IP = SERVER_IP;
        this.SERVER_PORT = SERVER_PORT;
    }

    public void stop() {
        running.set(false);
    }

    public boolean isAlive() {
        return running.get();
    }

    public void run() {
        while (DataHolder.getInstance().getConnectionStatus()) {
                if(DataHolder.getInstance().getControlMode() == 0) {
                    try {
                        x = UavParam.getInstance().getVx();
                        y = UavParam.getInstance().getVy();
                        z = UavParam.getInstance().getVz();
                        yaw = UavParam.getInstance().getYawRate();

                        encodeManualControlData.SendManualControlCommand(x, y, z, yaw);

                        Thread.sleep(100);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                } else if(DataHolder.getInstance().getControlMode() == 1) {
                    try {
                        servo1 = RobotArmParam.getInstance().getServo1();
                        servo2 = RobotArmParam.getInstance().getServo2();
                        servo3 = RobotArmParam.getInstance().getServo3();
                        servo4 = RobotArmParam.getInstance().getServo4();
                        servo5 = RobotArmParam.getInstance().getServo5();

                        encodeManualControlData.SendRobotControlCommand(servo1, servo2, servo3, servo4, servo5);

                        Thread.sleep(100);

                        encodeManualControlData.SendManualControlCommand(0, 0, 500, 0);

                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
        }

    }
}
