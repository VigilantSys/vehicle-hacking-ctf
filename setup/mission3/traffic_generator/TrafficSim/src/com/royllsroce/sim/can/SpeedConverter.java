package com.royllsroce.sim.can;

public class SpeedConverter extends Converter {

    public byte[] convert(String value) {
        double speed = Double.parseDouble(value);
        // Preserve 2 decimal points of precision and pack as int
        int multiplier = 100;
        int converted = (int) (speed * multiplier);
        byte[] packedSpeed = this.packInt(converted);
        return packedSpeed;
    }
    
}
