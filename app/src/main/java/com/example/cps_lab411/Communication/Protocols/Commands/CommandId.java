package com.example.cps_lab411.Communication.Protocols.Commands;

import com.example.cps_lab411.Communication.Protocols.Messages.MessageId;

public enum CommandId {
    UAVLINK_CMD_TAKEOFF,
    UAVLINK_CMD_ARM,
    UAVLINK_CMD_LAND,
    UAVLINK_CMD_FLYTO,
    UAVLINK_CMD_SETMODE,
    NO_COMMAND;

    public static CommandId getCommandId(short id) {
        switch (id) {
            case 22:
                return UAVLINK_CMD_TAKEOFF;
            case 23:
                return UAVLINK_CMD_ARM;
            case 24:
                return UAVLINK_CMD_LAND;
            case 25:
                return UAVLINK_CMD_FLYTO;
            case 26:
                return UAVLINK_CMD_SETMODE;
            default:
                return NO_COMMAND;
        }
    }

    public static short getCommandId(CommandId command) {
        switch (command) {
            case UAVLINK_CMD_TAKEOFF:
                return 22;
            case UAVLINK_CMD_ARM:
                return 23;
            case UAVLINK_CMD_LAND:
                return 24;
            case UAVLINK_CMD_FLYTO:
                return 25;
            case UAVLINK_CMD_SETMODE:
                return 26;
            default:
                return 0;
        }
    }
}
