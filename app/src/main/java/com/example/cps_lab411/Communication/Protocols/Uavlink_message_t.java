package com.example.cps_lab411.Communication.Protocols;

import com.example.cps_lab411.Communication.Protocols.Messages.MessageId;

public class Uavlink_message_t {
    public Uavlink_message_t() {
    }

    private MessageId _msgid;
    private byte _lenPayload;
    private byte[] _payload;

    public MessageId getMessageId() {
        return _msgid;
    }

    public void setMessageId(MessageId _msgid) {
        this._msgid = _msgid;
    }

    public byte getLenPayLoad() {
        return _lenPayload;
    }

    public void setLenPayLoad(byte _lenPayload) {
        this._lenPayload = _lenPayload;
    }

    public byte[] getPayLoad() {
        return _payload;
    }

    public void setPayLoad(byte[] _payload) {
        this._payload = _payload;
    }

    public byte[] Encode()
    {
        byte[] data = new byte[1+1+_lenPayload];
        data[0] = MessageId.getByteMessageId(_msgid);
        data[1] = (byte)_lenPayload;
        if (_lenPayload >= 0) {
            System.arraycopy(_payload, 0, data, 2, _lenPayload);
        }
        return data;
    }


    public void Decode(byte[] data) {
        _msgid = MessageId.getMessageId(data[0]);
        _lenPayload = (byte)data[1];
        _payload = new byte[_lenPayload];
        System.arraycopy(data, 2, _payload, 0, _lenPayload);
    }
}
