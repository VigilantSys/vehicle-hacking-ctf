package com.royllsroce.sim.can;

public class CoordinateConverter extends Converter {

    public byte[] convert(String value) {
        byte[] packedCoordinate = this.packDouble(Double.parseDouble(value));
        return packedCoordinate;
    }
    
}
