package com.royllsroce.sim.can;

public class GearConverter extends Converter {

    public byte[] convert(String value) {
        byte gear = 0;
        switch (value) {
            case "park":
                gear = 0;
                break;
            case "reverse":
                gear = 1;
                break;
            case "neutral":
                gear = 2;
                break;
            case "drive":
                gear = 3;
                break;
            case "low":
                gear = 4;
                break;
        }
        byte[] packedGear = this.packByte(gear);
        return packedGear;
    }
    
}
