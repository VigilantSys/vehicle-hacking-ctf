package com.royllsroce.firmware.infotainment;

import com.royllsroce.firmware.vehicle.Vehicle;
import com.royllsroce.firmware.infotainment.Component;

public class GPS implements Component {

    private double latitude;
    private double longitude;

    public GPS(double latitude, double longitude, Vehicle vehicle) {
        if (latitude < -90.0 || latitude > 90.0) {
            throw new IllegalArgumentException("Invalid latitude");
        }
        this.latitude = latitude;
        if (longitude < -180.0 || longitude > 180.0) {
            throw new IllegalArgumentException("Invalid longitude");
        }
        this.longitude = longitude;
        vehicle.subscribeToVehicleData("longitude", (Object lon) -> this.updateLongitude((Double) (lon)));
        vehicle.subscribeToVehicleData("latitude", (Object lat) -> this.updateLatitude((Double) (lat)));
    }

    public GPS(Vehicle vehicle) {
        this(0.0, 0.0, vehicle);
    }

    public void updateLongitude(double longitude) {
        if (longitude < -180.0 || longitude > 180.0) {
            throw new IllegalArgumentException("Invalid longitude");
        }
        this.longitude = longitude;
    }

    public void updateLatitude(double latitude) {
        if (latitude < -90.0 || latitude > 90.0) {
            throw new IllegalArgumentException("Invalid latitude");
        }
        this.latitude = latitude;
    }

    public double getLongitude() {
        return this.longitude;
    }

    public double getLatitude() {
        return this.latitude;
    }

    public Object get(String key) {
        if (key == "longitude") {
            return this.getLongitude();
        } else if (key == "latitude") {
            return this.getLatitude();
        } else {
            throw new IllegalArgumentException("Invalid key");
        }
    }
    
}
