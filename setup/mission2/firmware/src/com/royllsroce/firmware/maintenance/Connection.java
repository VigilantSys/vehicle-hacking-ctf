package com.royllsroce.firmware.maintenance;

import java.net.Socket;
import java.util.concurrent.ArrayBlockingQueue;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.IOException;

public class Connection implements Runnable {

    Socket socket;
    InputStream in;
    OutputStream out;
    ArrayBlockingQueue<String> queue;

    public Connection(Socket socket, ArrayBlockingQueue<String> queue) throws IOException {
        this.socket = socket;
        this.socket.setKeepAlive(true);
        this.in = socket.getInputStream();
        this.out = socket.getOutputStream();
        this.queue = queue;
    }

    public void send(String message) throws IOException {
        if (this.socket.isClosed()) {
            this.close();
            throw new IOException("Client socket is closed", null);
        }
        synchronized (this.queue) {
            this.queue.add(message);
        }
    }

    private void sendTCP(String message) throws IOException {
        this.out.write(message.getBytes());
    }

    @Override
    protected void finalize() {
        try {
            this.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void close() throws IOException {
        this.socket.close();
        this.in.close();
        this.out.close();
    }

    @Override
    public void run() {
        while (true) {
            try {
                String message = this.queue.take();
                this.sendTCP(message);
            } catch (InterruptedException e) {
                e.printStackTrace();
                continue;
            } catch (IOException e) {
                e.printStackTrace();
                continue;
            }
        }
    }
    
}
