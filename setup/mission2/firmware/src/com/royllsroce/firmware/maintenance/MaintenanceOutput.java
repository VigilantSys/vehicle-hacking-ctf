package com.royllsroce.firmware.maintenance;

import com.royllsroce.firmware.can.Bus;
import com.royllsroce.firmware.can.Message;

import java.net.Socket;
import java.net.ServerSocket;
import java.io.IOException;
import java.util.ArrayList;
import java.util.concurrent.ArrayBlockingQueue;

public class MaintenanceOutput {

    Bus bus;
    int port;
    ArrayList<Connection> connections;
    ServerSocket acceptSocket;
    
    public MaintenanceOutput(int port, Bus canbus) {
        this.port = port;
        this.bus = canbus;
        this.connections = new ArrayList<Connection>();
    }

    public void start() throws IOException {
        System.out.println("Starting maintenance output on port " + this.port);
        Thread acceptThread = new Thread(() -> {
            try {
                this.accept();
            } catch (IOException e) {
                e.printStackTrace();
            }
        });
        acceptThread.start();

        while (true) {
            String message = this.bus.recv();
            if (message == null) {
                continue;
            }
            synchronized (this.connections) {
                Message canMessage = new Message(message);
                String sendMessage = String.format("%s\t%04X\t[%d]\t%s\n", this.bus.getInterface(), canMessage.getId(), canMessage.getDataLength(), canMessage.getDataWithSpaces());
                ArrayList<Connection> connectionsToRemove = new ArrayList<Connection>();
                for (Connection connection : this.connections) {
                    try {
                        connection.send(sendMessage);
                    } catch (IOException e) {
                        connectionsToRemove.add(connection);
                        continue;
                    }
                }
                for (Connection connection : connectionsToRemove) {
                    connection.close();
                    this.connections.remove(connection);
                }
            }
        }
    }

    private void accept() throws IOException {
        this.acceptSocket = new ServerSocket(this.port);
        while (true) {
            ArrayBlockingQueue<String> queue = new ArrayBlockingQueue<String>(1024);
            Connection connection = new Connection(this.acceptSocket.accept(), queue);
            Thread connectionThread = new Thread(connection);
            connectionThread.start();
            synchronized (this.connections) {
                 this.connections.add(connection);
            }
        }
    }

    @Override
    protected void finalize() {
        try {
            this.acceptSocket.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
