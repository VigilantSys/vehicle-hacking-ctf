package com.royllsroce.sim.can;

public class RpmConverter extends Converter {
    
    public byte[] convert(String value) {
        byte[] packedRpm = this.packInt(Integer.parseInt(value));
        return packedRpm;
    }

}
