package com.example.cps_lab411.Communication.UDP;

import com.example.cps_lab411.DataHolder;
import com.example.cps_lab411.EvenBus.ConntectHandleEvenbus;

import org.greenrobot.eventbus.EventBus;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;
import java.nio.charset.StandardCharsets;
import java.util.concurrent.Executor;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class UDPConnection {
    private String SERVER_IP;
    private int SERVER_PORT;

    DatagramSocket client;

    byte[] receiveData = new byte[256];
//    public ReceivingThread receivingThread = new ReceivingThread(SERVER_IP, SERVER_PORT);
//    public ParsingThread parsingThread = new ParsingThread(SERVER_IP, SERVER_PORT);
//    public SendingThread sendingThread = new SendingThread(SERVER_IP, SERVER_PORT);

    public UDPConnection(String SERVER_IP, int SERVER_PORT) {
        this.SERVER_IP = SERVER_IP;
        this.SERVER_PORT = SERVER_PORT;
        try {
           client = new DatagramSocket();
        } catch (SocketException e) {
            e.printStackTrace();
        }
    }

    public void initConnection() {
        Thread initThread = new Thread() {
            public void run() {
                try {
                    client.setSoTimeout(3000);
                    String initString = "Hello World";
                    byte[] initSignal = initString.getBytes();

                    DatagramPacket dataPack = new DatagramPacket(initSignal, initSignal.length,
                            InetAddress.getByName(SERVER_IP),
                            SERVER_PORT);
                    client.send(dataPack);

                    System.out.println("Init Successful");

                    DataHolder.getInstance().setConnectionStatus(true);
                    ConntectHandleEvenbus conntectHandleEvenbus = new ConntectHandleEvenbus();
                    conntectHandleEvenbus.setConnectionState(true);
                    EventBus.getDefault().post(conntectHandleEvenbus);
                    Connect();

                } catch (IOException e) {
                    e.printStackTrace();
                    System.out.println("Error Received Package!!");
                    DataHolder.getInstance().setConnectionStatus(false);
                    ConntectHandleEvenbus conntectHandleEvenbus = new ConntectHandleEvenbus();
                    conntectHandleEvenbus.setConnectionState(false);
                    EventBus.getDefault().post(conntectHandleEvenbus);

                }
            }
        };
        initThread.start();
    }

    public void Connect() throws SocketException {
        if (DataHolder.getInstance().getConnectionStatus()) {
//            Thread receivingThread = new Thread(new ReceivingThread(SERVER_IP, SERVER_PORT));
            Thread parsingThread = new Thread(new ParsingThread(SERVER_IP, SERVER_PORT));
//            Thread sendingThread = new Thread(new SendingThread(SERVER_IP, SERVER_PORT));
            ExecutorService exConnection = Executors.newFixedThreadPool(1);
//            exConnection.submit(receivingThread);
            exConnection.submit(parsingThread);
//            exConnection.submit(sendingThread);
            ManualControlThread manualControlThread = new ManualControlThread(SERVER_IP, SERVER_PORT);
            Thread thread1 = new Thread(receivingThread);
            Thread thread2 = new Thread(sendingThread);
            thread1.start();
            thread2.start();
            manualControlThread.start();
        }
    }

    Runnable receivingThread =
            new Runnable(){
                public void run(){
                    while (DataHolder.getInstance().getConnectionStatus()) {
                        receivePackage();
                    }
                }
            };

    Runnable sendingThread =
            new Runnable(){
                public void run(){
                    while (DataHolder.getInstance().getConnectionStatus()) {
                        sendingPackage();
                    }
                }
            };

    private void receivePackage() {
        try {
            client.setSoTimeout(3000);
            DatagramPacket receivePacket = new DatagramPacket(receiveData, receiveData.length);
            client.receive(receivePacket);
            DataHolder.getInstance().putRecvQueue(receiveData);
            Thread.sleep(1);
        } catch (IOException | InterruptedException e) {
            e.printStackTrace();
            System.out.println("Error Received Package!!");
            DataHolder.getInstance().setConnectionStatus(false);
            ConntectHandleEvenbus conntectHandleEvenbus = new ConntectHandleEvenbus();
            conntectHandleEvenbus.setConnectionState(false);
            EventBus.getDefault().post(conntectHandleEvenbus);
        }
    }

    public void sendingPackage() {
        try {
            byte[] controlSignal = DataHolder.getInstance().takeSendQueue();

            if (controlSignal != null) {
                DatagramPacket dataPack = new DatagramPacket(controlSignal, controlSignal.length,
                        InetAddress.getByName(SERVER_IP),
                        SERVER_PORT);
                client.send(dataPack);
            }

            Thread.sleep(1);
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("Error sending data!!!");
        }
    }
}


