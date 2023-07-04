package com.royllsroce.sim.can;

public class TemperatureConverter extends Converter {

    public byte[] convert(String value) {
        byte[] packedTemperature = this.packDouble(Double.parseDouble(value));
        return packedTemperature;
    }
    
}
