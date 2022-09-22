package com.example.cps_lab411.Communication;

import com.example.cps_lab411.Communication.Protocols.Commands.CommandId;
import com.example.cps_lab411.Communication.Protocols.Commands.Uavlink_cmd_arm_disarm_t;
import com.example.cps_lab411.Communication.Protocols.Commands.Uavlink_cmd_flyto_t;
import com.example.cps_lab411.Communication.Protocols.Commands.Uavlink_cmd_land_t;
import com.example.cps_lab411.Communication.Protocols.Commands.Uavlink_cmd_setmode_t;
import com.example.cps_lab411.Communication.Protocols.Commands.Uavlink_cmd_takeoff_t;
import com.example.cps_lab411.Communication.Protocols.Messages.MessageId;
import com.example.cps_lab411.Communication.Protocols.Messages.Uavlink_msg_manual_control_t;
import com.example.cps_lab411.Communication.Protocols.Messages.Uavlink_msg_robot_control_t;
import com.example.cps_lab411.Communication.Protocols.Messages.Uavlink_msg_setwp;
import com.example.cps_lab411.Communication.Protocols.Uavlink_message_t;
import com.example.cps_lab411.DataHolder;
import com.example.cps_lab411.UavState.WayPointParam;

public class EncodeData {

    public void SendManualControlCommand(int vx, int vy, int vz, int yawRate) {
        byte[] mannual_data;
        Uavlink_msg_manual_control_t manual_msg = new Uavlink_msg_manual_control_t();
        manual_msg.Encode(vx, vy, vz,yawRate);
        mannual_data = manual_msg.getData();

        Uavlink_message_t message = new Uavlink_message_t();
        message.setMessageId(MessageId.UAVLINK_MSG_ID_MANUAL_CONTROL);
        message.setLenPayLoad((byte) mannual_data.length);
        message.setPayLoad(mannual_data);
        SendCommandToQueue(message.Encode());
    }

    public void SendFlyToCommand(int allwp, int wpid) {
        byte[] flyto_data;
        Uavlink_cmd_flyto_t flyto_msg = new Uavlink_cmd_flyto_t();
        flyto_msg.setAllwp(allwp);
        flyto_msg.setWpid(wpid);
        flyto_data = flyto_msg.Encode();

        Uavlink_message_t message = new Uavlink_message_t();
        message.setMessageId(MessageId.UAVLINK_MSG_ID_COMMAND);
        message.setLenPayLoad((byte) flyto_data.length);
        message.setPayLoad(flyto_data);
        SendCommandToQueue(message.Encode());
    }

    public void SendCommandTakeoff(float altitude) {
        Uavlink_cmd_takeoff_t takeOffCmd = new Uavlink_cmd_takeoff_t();
        takeOffCmd.setAltitude(altitude);
        byte[] takeOffData = takeOffCmd.Encode();

        Uavlink_message_t message = new Uavlink_message_t();
        message.setMessageId(MessageId.UAVLINK_MSG_ID_COMMAND);
        message.setLenPayLoad((byte) takeOffData.length);
        message.setPayLoad(takeOffData);
        SendCommandToQueue(message.Encode());
    }

    public byte[] SendManualControlCommandByte(int vx, int vy, int vz, int yawRate) {
        byte[] mannual_data;
        Uavlink_msg_manual_control_t manual_msg = new Uavlink_msg_manual_control_t();
        manual_msg.Encode(vx, vy, vz,yawRate);
        mannual_data = manual_msg.getData();

        Uavlink_message_t message = new Uavlink_message_t();
        message.setMessageId(MessageId.UAVLINK_MSG_ID_MANUAL_CONTROL);
        message.setLenPayLoad((byte) mannual_data.length);
        message.setPayLoad(mannual_data);
        return message.Encode();
    }


    public void SendCommandSetMode(int mode) {
        byte[] setMode_data;

        Uavlink_cmd_setmode_t setmodeCmd = new Uavlink_cmd_setmode_t();
        setmodeCmd.setMode((byte) mode);
        setmodeCmd.Encode();
        setMode_data = setmodeCmd.getData();

        Uavlink_message_t message = new Uavlink_message_t();
        message.setMessageId(MessageId.UAVLINK_MSG_ID_COMMAND);
        message.setLenPayLoad((byte) setMode_data.length);
        message.setPayLoad(setMode_data);
        SendCommandToQueue(message.Encode());
    }

    public void SendCommandArmDisarm(byte armDisarm) {
        byte[] setArm_data;

        Uavlink_cmd_arm_disarm_t setArmDisarm = new Uavlink_cmd_arm_disarm_t();
        setArmDisarm.setArmDisarm((byte) armDisarm);
        setArmDisarm.Encode();
        setArm_data = setArmDisarm.getData();

        Uavlink_message_t message = new Uavlink_message_t();
        message.setMessageId(MessageId.UAVLINK_MSG_ID_COMMAND);
        message.setLenPayLoad((byte) setArm_data.length);
        message.setPayLoad(setArm_data);
        SendCommandToQueue(message.Encode());
    }

    public void SendCommandLand() {
        byte[] setLand;

        Uavlink_cmd_land_t Land = new Uavlink_cmd_land_t();
        Land.Encode();
        setLand = Land.getData();

        Uavlink_message_t message = new Uavlink_message_t();
        message.setMessageId(MessageId.UAVLINK_MSG_ID_COMMAND);
        message.setLenPayLoad((byte) setLand.length);
        message.setPayLoad(setLand);
        SendCommandToQueue(message.Encode());
    }

    public void SendMissionMessage(WayPointParam wp) {
        if(wp != null) {
            byte[] wpData;
            Uavlink_msg_setwp wpMessage = new Uavlink_msg_setwp();
            wpMessage.setWPID(wp.get_wpID());
            wpMessage.setTarget(wp.getLatitude(), wp.getLongitude(), wp.getAltitude(), wp.getSpeed());
            wpData = wpMessage.Encode();

            Uavlink_message_t message_t = new Uavlink_message_t();
            message_t.setMessageId(MessageId.UAVLINK_MSG_ID_SETWP);
            message_t.setLenPayLoad((byte) wpData.length);
            message_t.setPayLoad(wpData);
            SendCommandToQueue(message_t.Encode());
        }
    }

    public void SendRobotControlCommand(int servo1, int servo2, int servo3, int servo4, int servo5) {
        byte[] robot_control;
        Uavlink_msg_robot_control_t RbControlMessage = new Uavlink_msg_robot_control_t();
        RbControlMessage.Encode(servo1, servo2, servo3, servo4, servo5);
        robot_control = RbControlMessage.getData();

        Uavlink_message_t message_t = new Uavlink_message_t();
        message_t.setMessageId(MessageId.UAVLINK_MSG_ID_ROBOT_CONTROL);
        message_t.setLenPayLoad((byte) robot_control.length);
        message_t.setPayLoad(robot_control);
        SendCommandToQueue(message_t.Encode());

    }

    public void SendCommandToQueue(byte[] message) {
        DataHolder.getInstance().putSendQueue(message);
    }
}