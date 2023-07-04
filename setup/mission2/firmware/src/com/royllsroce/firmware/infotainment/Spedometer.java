package com.royllsroce.firmware.infotainment;

import com.royllsroce.firmware.vehicle.Vehicle;
import com.royllsroce.firmware.infotainment.Component;

public class Spedometer implements Component {

    private String speedUnit;
    private Vehicle vehicle;
    private double speed;

    public Spedometer(String speedUnit, Vehicle vehicle) {
        if (speedUnit.toUpperCase() != "MPH" && speedUnit.toUpperCase() != "KPH") {
            throw new IllegalArgumentException("Invalid speed unit");
        }
        this.speedUnit = speedUnit;
        this.vehicle = vehicle;

        this.vehicle.subscribeToVehicleData("speed", (Object speed) -> this.updateSpeed((Double) (speed)));
    }

    private void updateSpeed(double speed) {
        this.speed = speed;
    }

    public double getSpeed() {
        return this.speed;
    }

    public String getSpeedUnit() {
        return this.speedUnit;
    }

    public String getSpeedString() {
        return this.speed + " " + this.speedUnit;
    }
    
    public Object get(String key) {
        if (key == "speed") {
            return this.getSpeed();
        } else if (key == "speedUnit") {
            return this.getSpeedUnit();
        } else {
            throw new IllegalArgumentException("Invalid key");
        }
    }
}
