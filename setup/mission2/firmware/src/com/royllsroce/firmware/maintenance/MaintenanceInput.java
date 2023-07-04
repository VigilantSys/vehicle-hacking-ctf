package com.royllsroce.firmware.maintenance;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;

import com.royllsroce.firmware.can.Bus;
import com.royllsroce.firmware.can.Message;

public class MaintenanceInput {

    private int port;
    private DatagramSocket socket;
    private Bus bus;

    public MaintenanceInput(int port, Bus canbus) {
        this.bus = canbus;
        this.port = port;
    }

    public void startUDPServer() throws IOException {
        System.out.println("Starting maintenance server...");
        this.socket = new DatagramSocket(this.port);

        System.out.println("Server started on port " + port);

        byte[] buffer = new byte[1024];
        DatagramPacket packet = null;

        // Listen for CAN frames
        while (true) {
            buffer = new byte[1024];
            packet = new DatagramPacket(buffer, buffer.length);
            this.socket.receive(packet);

            byte[] data = packet.getData();
            String message = new String(data, 0, packet.getLength());
            if (!message.contains("#")) {
                continue;
            }

            System.out.println("Received message: " + message);
            Message canMessage = new Message(message.trim());

            sendCANFrame(canMessage);
            this.sendResponse("Sent: " + canMessage.toString(), packet);
        }
    }

    private void sendCANFrame(Message message) throws IOException {
        synchronized (this.bus) {
            this.bus.send(message.toString());
        }
    }

    private void sendResponse(String message, DatagramPacket received) {
        byte[] buf = message.getBytes();
        DatagramPacket DpSend =
                  new DatagramPacket(buf, buf.length, received.getAddress(), received.getPort());
        try {
            this.socket.send(DpSend);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}