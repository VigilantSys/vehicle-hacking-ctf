package com.royllsroce.firmware.infotainment;

import com.royllsroce.firmware.infotainment.Component;

public class TemperatureControl implements Component {

    private double temperature;
    private int fanspeed;
    private String temperatureUnit;

    public TemperatureControl(String initialTemperature, String temperatureUnit) {
        if (Double.parseDouble(initialTemperature) < 0.0 || Double.parseDouble(initialTemperature) > 100.0) {
            this.temperature = 0.0;
            throw new IllegalArgumentException("Invalid temperature");
        }
        this.temperature = Double.parseDouble(initialTemperature);
        if (temperatureUnit.toUpperCase() != "C" && temperatureUnit.toUpperCase() != "F") {
            this.temperatureUnit = "C";
            throw new IllegalArgumentException("Invalid temperature unit");
        }
        this.temperatureUnit = temperatureUnit;
        this.fanspeed = 0;
    }

    public void setTemperature(double temperature) {
        if (temperature < 0.0 || temperature > 100.0) {
            throw new IllegalArgumentException("Invalid temperature");
        }
        this.temperature = temperature;
    }

    public void setTemperatureUnit(String temperatureUnit) {
        if (temperatureUnit.toUpperCase() != "C" && temperatureUnit.toUpperCase() != "F") {
            throw new IllegalArgumentException("Invalid temperature unit");
        }
        this.temperatureUnit = temperatureUnit;
    }

    public void increaseTemperature(double increment) {
        if (this.temperatureUnit.toUpperCase() == "C") {
            if (this.temperature + increment > 100.0) {
                throw new IllegalArgumentException("Invalid temperature");
            }
        } else {
            if (this.temperature + increment > 212.0) {
                throw new IllegalArgumentException("Invalid temperature");
            }
        }
        this.temperature += increment;
    }

    public void decreaseTemperature(double decrement) {
        if (this.temperatureUnit.toUpperCase() == "C") {
            if (this.temperature - decrement < 0.0) {
                throw new IllegalArgumentException("Invalid temperature");
            }
        } else {
            if (this.temperature - decrement < 32.0) {
                throw new IllegalArgumentException("Invalid temperature");
            }
        }
        this.temperature -= decrement;
    }
    
    public void increaseFanSpeed() {
        if (this.fanspeed >= 5) {
            throw new IllegalArgumentException("Invalid fan speed");
        }
        this.fanspeed++;
    }

    public void decreaseFanSpeed() {
        if (this.fanspeed <= 0) {
            throw new IllegalArgumentException("Invalid fan speed");
        }
        this.fanspeed--;
    }

    public void turnOff() {
        this.fanspeed = 0;
    }

    public void turnOn() {
        this.fanspeed = 1;
    }

    public void setFanSpeed(int fanspeed) {
        if (fanspeed < 0 || fanspeed > 5) {
            throw new IllegalArgumentException("Invalid fan speed");
        }
        this.fanspeed = fanspeed;
    }

    public double getTemperature() {
        return this.temperature;
    }

    public String getTemperatureUnit() {
        return this.temperatureUnit;
    }

    public int getFanSpeed() {
        return this.fanspeed;
    }

    public String getTemperatureString() {
        return this.temperature + " " + this.temperatureUnit;
    }

    public Object get(String key) {
        if (key == "temperature") {
            return this.getTemperature();
        } else if (key == "temperatureUnit") {
            return this.getTemperatureUnit();
        } else if (key == "fanSpeed") {
            return this.getFanSpeed();
        } else {
            throw new IllegalArgumentException("Invalid key");
        }
    }
}
