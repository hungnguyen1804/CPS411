package com.example.cps_lab411.Communication.Protocols.Messages;

public enum MessageId {
    NO_COMMAND,
    UAVLINK_MSG_ID_STATE,
    UAVLINK_MSG_ID_GLOBAL_POSITION,
    UAVLINK_MSG_ID_LOCAL_POSITION,
    UAVLINK_MSG_ID_SETWP,
    UAVLINK_MSG_ID_COMMAND,
    UAVLINK_MSG_ID_MANUAL_CONTROL,
    UAVLINK_MSG_ID_ROBOT_CONTROL,
    UAVLINK_MSG_ID_SENSOR;

    public static MessageId getMessageId(byte id) {
        switch (id) {
            case 0x01:
                return UAVLINK_MSG_ID_STATE;
            case 0x02:
                return UAVLINK_MSG_ID_GLOBAL_POSITION;
            case 0x03:
                return UAVLINK_MSG_ID_LOCAL_POSITION;
            case 0x04:
                return UAVLINK_MSG_ID_SETWP;
            case 0x05:
                return UAVLINK_MSG_ID_COMMAND;
            case 0x06:
                return UAVLINK_MSG_ID_MANUAL_CONTROL;
            case 0x07:
                return UAVLINK_MSG_ID_ROBOT_CONTROL;
            case 0x08:
                return UAVLINK_MSG_ID_SENSOR;
            default:
                return NO_COMMAND;
        }
    }

    public static byte getByteMessageId(MessageId message) {
        switch (message) {
            case UAVLINK_MSG_ID_STATE:
                return 0x01;
            case UAVLINK_MSG_ID_GLOBAL_POSITION:
                return 0x02;
            case UAVLINK_MSG_ID_LOCAL_POSITION:
                return 0x03;
            case UAVLINK_MSG_ID_SETWP:
                return 0x04;
            case UAVLINK_MSG_ID_COMMAND:
                return 0x05;
            case UAVLINK_MSG_ID_MANUAL_CONTROL:
                return 0x06;
            case UAVLINK_MSG_ID_ROBOT_CONTROL:
                return 0x07;
            case UAVLINK_MSG_ID_SENSOR:
                return 0x08;
            default:
                return 0x00;
        }
    }
}
