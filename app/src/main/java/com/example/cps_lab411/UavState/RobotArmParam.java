package com.example.cps_lab411.UavState;

import com.example.cps_lab411.DataHolder;

public class RobotArmParam {
    private int servo1, servo2, servo3, servo4, servo5;

    public int getServo1() { return servo1; }
    public int getServo2() { return servo2; }
    public int getServo3() { return servo3; }
    public int getServo4() {return servo4; }

    public int getServo5() {
        return servo5;
    }

    public void setRobotAimServo12(int servo1, int servo2) {
        this.servo1 = servo1;
        this.servo2 = servo2;
    }

    public void setRobotAimServo34(int servo3, int servo4) {
        this.servo3 = servo3;
        this.servo4 = servo4;
    }
    public void setRobotAimServo(int servo1, int servo2, int servo3, int servo4) {
        this.servo1 = servo1;
        this.servo2 = servo2;
        this.servo3 = servo3;
        this.servo4 = servo4;
    }

    public void setServo5(int servo5) {
        this.servo5 = servo5;
    }

    public static final RobotArmParam holder = new RobotArmParam();
    public static RobotArmParam getInstance()
    {
        return holder;
    }

}
