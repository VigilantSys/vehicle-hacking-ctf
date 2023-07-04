package com.royllsroce.sim.can;

public class FuelConverter extends Converter {

    public byte[] convert(String value) {
        double fuel = Double.parseDouble(value);
        // Preserve 2 decimal points of precision and pack as int
        int multiplier = 100;
        int converted = (int) (fuel * multiplier);
        byte[] packedFuel = this.packInt(converted);
        return packedFuel;
    }
    
}
