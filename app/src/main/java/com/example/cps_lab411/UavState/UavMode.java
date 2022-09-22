package com.example.cps_lab411.UavState;

import com.example.cps_lab411.Communication.Protocols.Messages.MessageId;
import com.example.cps_lab411.R;
import com.example.cps_lab411.R2;

public enum UavMode {
    Connected,
    Manual,
    Position,
    OffBoard,
    Hold,
    TakeOff,
    Land;

    public static UavMode getUavMode(int id) {
        switch (id) {
            case 0:
                return Manual;
            case 1:
                return Position;
            case 2:
                return OffBoard;
            case 3:
                return Land;
            case 4:
                return TakeOff;
            case 5:
                return Hold;
            default:
                return Connected;
        }
    }
}
