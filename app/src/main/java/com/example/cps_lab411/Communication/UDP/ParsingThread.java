package com.example.cps_lab411.Communication.UDP;

import com.example.cps_lab411.Communication.Protocols.Messages.Uavlink_msg_sensor_t;
import com.example.cps_lab411.Communication.Protocols.Uavlink_message_t;
import com.example.cps_lab411.Communication.Protocols.Messages.Uavlink_msg_global_position_t;
import com.example.cps_lab411.Communication.Protocols.Messages.Uavlink_msg_state_t;
import com.example.cps_lab411.DataHolder;
import com.example.cps_lab411.EvenBus.UDPMessage2EvenBus;
import com.example.cps_lab411.EvenBus.UDPMessageEvenBus;
import com.example.cps_lab411.UavState.UavMode;
import com.example.cps_lab411.UavState.UavParam;
import com.example.cps_lab411.UpdateSensor;

import org.greenrobot.eventbus.EventBus;

public class ParsingThread implements  Runnable{
    private boolean manualMode = false;
    private String SERVER_IP;
    private int SERVER_PORT;

    public ParsingThread(String SERVER_IP, int SERVER_PORT) {
        this.SERVER_IP = SERVER_IP;
        this.SERVER_PORT = SERVER_PORT;
    }

    @Override
    public void run() {
        while (DataHolder.getInstance().getConnectionStatus()) {
            Uavlink_message_t message = new Uavlink_message_t();
            byte[] buffer;
            buffer = DataHolder.getInstance().takeRecvQueue();
            if(buffer != null) {
                message.Decode(buffer);
                OnReceiveMessage(message);
            }

            try {
                Thread.sleep(1);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

        }
    }

    private void OnReceiveMessage(Uavlink_message_t message) {
        switch (message.getMessageId()) {
            case UAVLINK_MSG_ID_STATE:
                OnReceiveState(message.getPayLoad());
                break;
            case UAVLINK_MSG_ID_GLOBAL_POSITION:
                OnReceiveGlobalPosition(message.getPayLoad());
                break;
            case UAVLINK_MSG_ID_LOCAL_POSITION:
                OnReceiveLocalPosition(message.getPayLoad());
                break;
            case UAVLINK_MSG_ID_SENSOR:
                OnReceiveSensor(message.getPayLoad());
                break;
        }
    }

    private void OnReceiveLocalPosition(byte[] data) {

    }

    private void OnReceiveGlobalPosition(byte[] data) {
        Uavlink_msg_global_position_t _globalPosition = new Uavlink_msg_global_position_t();
        _globalPosition.Decode(data);

        UDPMessage2EvenBus UDPMessageGlobalPosition = new UDPMessage2EvenBus();
        UDPMessageGlobalPosition.setMessage2(_globalPosition.getGlobalLat(),
                _globalPosition.getGlobalLon(),
                _globalPosition.getGlobalAlt(),
                _globalPosition.getGlobalVx(),
                _globalPosition.getGlobalVy(),
                _globalPosition.getGlobalVz(),
                _globalPosition.getRotation());

        EventBus.getDefault().post(UDPMessageGlobalPosition);
    }


    private void OnReceiveState(byte[] data) {
        Uavlink_msg_state_t _state = new Uavlink_msg_state_t();
        _state.Decode(data);

        UavParam.getInstance().setAimMode(_state.getArmMode());
        UavParam.getInstance().setUavMode(UavMode.getUavMode(_state.getMode()));

        UDPMessageEvenBus UDPMessageState = new UDPMessageEvenBus();
        UDPMessageState.setMessage1(_state.getConnected(), _state.getArmMode(), _state.getMode() , _state.getBattery());
        EventBus.getDefault().post(UDPMessageState);
    }

    private void OnReceiveSensor(byte[] data) {
        Uavlink_msg_sensor_t _sensor = new Uavlink_msg_sensor_t();
        _sensor.Decode(data);

        UpdateSensor SensorEventbus = new UpdateSensor();
        SensorEventbus.setSensorParameter(
                _sensor.getSensorID(),
                _sensor.getLat(),
                _sensor.getLon(),
                _sensor.getTemp(),
                _sensor.getHum(),
                _sensor.getGas());

        EventBus.getDefault().post(SensorEventbus);
    }
}
