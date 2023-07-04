package com.royllsroce.firmware.vehicle;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.function.Consumer;

public class Vehicle {

    private HashMap<String, Object> data;
    private HashMap<String, ArrayList<Consumer<Object>>> subscribers;

    public Vehicle() {
        this.data = new HashMap<String, Object>();
        this.subscribers = new HashMap<String, ArrayList<Consumer<Object>>>();

        this.data.put("speed", 0.0);
        this.data.put("rpm", 0.0);
        this.data.put("fuel", 0.0);
        this.data.put("radioStation", "FM 100.0");
        this.data.put("temperature", 0.0);
        this.data.put("longitude", 0.0);
        this.data.put("latitude", 0.0);
        this.data.put("gear", "drive");
    }

    public void subscribeToVehicleData(String channel, Consumer<Object> callback) {
        if (channel == "all") {
            for (String key : this.data.keySet()) {
                this.subscribeToVehicleData(key, callback);
            }
        }
        else if (!this.data.containsKey(channel)) {
            throw new IllegalArgumentException("Invalid data");
        }
        else if (!this.subscribers.containsKey(channel)) {
            this.subscribers.put(channel, new ArrayList<Consumer<Object>>());
            this.subscribers.get(channel).add(callback);
        }
        else {
            this.subscribers.get(channel).add(callback);
        }
    }

    public void updateVehicleData(String newData, Object value) {
        this.data.put(newData, value);
        if (this.subscribers.containsKey(newData)) {
            for (Consumer<Object> callback : this.subscribers.get(newData)) {
                callback.accept(value);
            }
        }
    }
    
}
