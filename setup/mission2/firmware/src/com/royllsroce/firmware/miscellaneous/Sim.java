package com.royllsroce.firmware.miscellaneous;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;

public class Sim {

    int port;
    DatagramSocket socket;

    public Sim() throws IOException {
        this.port = 9999;
        this.socket = new DatagramSocket(this.port);
    }

    public String[] tick() throws IOException {

        byte[] buffer = new byte[1024];
        DatagramPacket packet = null;

        while (true) {
            packet = new DatagramPacket(buffer, buffer.length);
            socket.receive(packet);

            byte[] data = packet.getData();
            String message = new String(data, 0, packet.getLength());

            String[] items = message.trim().split(",");
            if (items.length > 0) {
                return items;
            }
        }

    }

    @Override
    protected void finalize() {
        this.socket.close();
    }
    
}
