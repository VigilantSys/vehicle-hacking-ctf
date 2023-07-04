package com.royllsroce.firmware.can;

import java.util.concurrent.ArrayBlockingQueue;

public class Bus {
    
    String iface;
    ArrayBlockingQueue<String> queue;

    public Bus(String interfaceName) {
        this.iface = interfaceName;
        this.queue = new ArrayBlockingQueue<String>(1024);
    }

    public String getInterface() {
        return this.iface;
    }

    public void send(String message) {
        this.queue.add(message);
    }

    public String recv() {
        return this.queue.poll();
    }

}
