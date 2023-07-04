package com.royllsroce.sim.net;

import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.io.IOException;

public class SimClient {

    private int port;
    DatagramSocket socket;
    InetAddress address;

    public SimClient(int simPort) throws IOException {
        this.port = simPort;
        this.address = InetAddress.getByName("localhost");
        this.socket = new DatagramSocket();
    }

    public void send(String line) throws IOException {
        byte[] buffer = line.getBytes();
        DatagramPacket packet = new DatagramPacket(buffer, buffer.length, this.address, this.port);
        this.socket.send(packet);
    }
    
}
