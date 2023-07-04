package com.royllsroce.firmware.infotainment;

import com.royllsroce.firmware.config.ConfigItem;
import com.royllsroce.firmware.infotainment.Radio;
import com.royllsroce.firmware.infotainment.Spedometer;
import com.royllsroce.firmware.infotainment.TemperatureControl;
import com.royllsroce.firmware.infotainment.GPS;
import com.royllsroce.firmware.vehicle.Vehicle;
import com.royllsroce.firmware.miscellaneous.Sim;

import java.io.IOException;
import java.util.HashMap;

public class Interface {
    
    private static Interface instance = null;

    public static Interface getInstance() {
        if (instance == null) {
            instance = new Interface();
        }
        return instance;
    }

    private Vehicle vehicle;
    private boolean running;
    private Component[] components;

    public Interface() {
        this.vehicle = new Vehicle();
        this.running = false;
    }

    public void boot(ConfigItem[] config) throws IOException {
        if (this.running) {
            throw new IllegalStateException("Interface is already running");
        }
        System.out.println("Booting infotainment system...");

        HashMap<String, String> configMap = new HashMap<>();
        for (ConfigItem configItem : config) {
            configMap.put(configItem.getName(), configItem.getValue());
        }

        Radio radio = new Radio(configMap.get("RadioStation"), vehicle);
        radio.play();

        TemperatureControl temperatureControl = new TemperatureControl(configMap.get("InitialTemperature"), configMap.get("TemperatureUnit"));
        temperatureControl.turnOn();

        Spedometer spedometer = new Spedometer(configMap.get("SpeedUnit"), vehicle);
        GPS gps = new GPS(vehicle);

        this.components = new Component[] { radio, temperatureControl, spedometer, gps };

        Sim sim = new Sim();
        while (true) {
            try {
                String[] data = sim.tick();
                vehicle.updateVehicleData("speed", Double.parseDouble(data[0]));
                vehicle.updateVehicleData("rpm", Double.parseDouble(data[1]));
                vehicle.updateVehicleData("fuel", Double.parseDouble(data[2]));
                vehicle.updateVehicleData("temperature", Double.parseDouble(data[3]));
                vehicle.updateVehicleData("longitude", Double.parseDouble(data[4]));
                vehicle.updateVehicleData("latitude", Double.parseDouble(data[5]));
                vehicle.updateVehicleData("radioStation", data[6]);
                vehicle.updateVehicleData("gear", data[7]);
            } catch (Exception e) {
                e.printStackTrace();
            }
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

}
