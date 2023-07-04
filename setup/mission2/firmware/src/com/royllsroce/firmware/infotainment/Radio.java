package com.royllsroce.firmware.infotainment;

import com.royllsroce.firmware.infotainment.Component;
import com.royllsroce.firmware.vehicle.Vehicle;

public class Radio implements Component {

    private String modulation;
    private double station;
    private Vehicle vehicle;

    public Radio(String stationName, Vehicle vehicle) {
        this.vehicle = vehicle;
        parseStationString(stationName);
        this.vehicle.subscribeToVehicleData("radioStation", (Object stationUpdate) -> this.tune((String) (stationUpdate)));
    }

    private void parseStationString(String stationString) {
        String[] stationParts = stationString.split(" ");
        String mod = stationParts[1];
        station = Double.parseDouble(stationParts[0]);
        if (!mod.toUpperCase().equals("FM") && !mod.toUpperCase().equals("AM")) {
            this.modulation = "FM";
            this.station = 0.0;
        }
        try {
            this.modulation = stationParts[1];
            this.station = Double.parseDouble(stationParts[0]);
        } catch (NumberFormatException e) {
            throw new IllegalArgumentException("Invalid station");
        } catch (ArrayIndexOutOfBoundsException e) {
            throw new IllegalArgumentException("Invalid station");
        }
    }

    public void tune(String stationString) {
        this.parseStationString(stationString);
        System.out.println("Tuning to " + stationString);
    }

    public void tune(double station) {
        this.station = station;
        System.out.println("Tuning to " + station);
    }

    public void switchModulation(String modulation) {
        this.modulation = modulation;
        System.out.println("Switching modulation to " + modulation);
    }

    public void seekUp() {
        this.station += 0.1;
        System.out.println("Seeking up to " + this.station);
    }

    public void seekDown() {
        this.station -= 0.1;
        System.out.println("Seeking down to " + this.station);
    }   

    public void play() {
        System.out.println("Playing " + this.station);
    }

    public String getStationName() {
        return this.station + " " + this.modulation;
    }

    public double getStation() {
        return this.station;
    }

    public String getModulation() {
        return this.modulation;
    }

    public Object get(String key) {
        if (key == "station") {
            return this.getStation();
        } else if (key == "modulation") {
            return this.getModulation();
        } else {
            throw new IllegalArgumentException("Invalid key");
        }
    }
    
}
