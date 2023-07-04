package com.royllsroce.sim.can;

public abstract class Converter {

    abstract public byte[] convert(String value);

    protected byte[] packDouble(double value) {
        byte[] packedValue = new byte[8];
        long longValue = Double.doubleToLongBits(value);
        for (int i = 0; i < 8; i++) {
            packedValue[i] = (byte) (longValue >> (i * 8));
        }
        return packedValue;
    }

    protected byte[] packInt(int value) {
        byte[] packedValue = new byte[4];
        for (int i = 0; i < 4; i++) {
            packedValue[i] = (byte) (value >> (i * 8));
        }
        return packedValue;
    }

    protected byte[] packString(String value, int length) {
        byte[] packedValue = new byte[length];
        for (int i = 0; i < length; i++) {
            if (i < value.length()) {
                // Truncate upper byte of char
                packedValue[i] = (byte) value.charAt(i);
            } else {
                // Pad with zeroes
                packedValue[i] = 0;
            }
        }
        return packedValue;
    }

    protected byte[] packBoolean(boolean value) {
        byte[] packedValue = new byte[1];
        packedValue[0] = (byte) (value ? 1 : 0);
        return packedValue;
    }

    protected byte[] packByte(byte value) {
        byte[] packedValue = new byte[1];
        packedValue[0] = value;
        return packedValue;
    }
    
}
