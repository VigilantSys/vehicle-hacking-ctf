package com.royllsroce.sim.net;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.util.HashMap;

import com.royllsroce.sim.can.CanFrame;

public class CanClient {

    private int port;
    private DatagramSocket socket;
    private InetAddress address;

    public CanClient(int port) throws IOException {
        this.port = port;
        this.socket = new DatagramSocket();
        this.address = InetAddress.getByName("localhost");
    }

    public void sendFrame(HashMap<String, String> row) throws IOException {
        CanFrame canFrame = new CanFrame(row);
        String[] frame = canFrame.getFrame();

        for (String line : frame) {
            byte[] buffer = line.getBytes();
            DatagramPacket packet = new DatagramPacket(buffer, buffer.length, this.address, this.port);
            this.socket.send(packet);
        }
    }
    
}
