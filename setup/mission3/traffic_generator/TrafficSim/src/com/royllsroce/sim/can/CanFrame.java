package com.royllsroce.sim.can;

import java.util.HashMap;

public class CanFrame {

    HashMap<String, String> canData;
    HashMap<String, byte[]> packed;
    String[] frame;

    public CanFrame(HashMap<String, String> canData) {
        this.canData = canData;
        this.packed = this.packData();
        this.frame = this.buildFrame();
    }

    private HashMap<String, byte[]> packData() {
        HashMap<String, byte[]> packedData = new HashMap<String, byte[]>();
        ConverterFactory converterFactory = new ConverterFactory();
        for (HashMap.Entry<String, String> entry : this.canData.entrySet()) {
            String key = entry.getKey();
            String value = entry.getValue();

            // Ignore the raw line data
            if (key.equals("line")) {
                continue;
            }

            byte[] packedValue = converterFactory.getConverter(key).convert(value);
            packedData.put(key, packedValue);
        }
        return packedData;
    }

    private String[] buildFrame() {

        String[] frame = new String[6];

        // Radio
        String radioData = this.packData(this.packed.get("radioStation"));
        frame[0] = this.buildMessage(0x1A0, radioData);
        // RPM/Fuel
        String rpmFuelData = this.packData(this.packed.get("rpm"), this.packed.get("fuel"));
        frame[1] = this.buildMessage(0x1A1, rpmFuelData);
        // Speed/Gear
        String speedGearData = this.packData(this.packed.get("speed"), this.packed.get("gear"));
        int checksum = calcChecksum(speedGearData);
        frame[2] = this.buildMessage(0x1A2, speedGearData + String.format("%02X", checksum));
        // Latitude
        String latData = this.packData(this.packed.get("latitude"));
        frame[3] = this.buildMessage(0x1A3, latData);
        // Longitude
        String lonData = this.packData(this.packed.get("longitude"));
        frame[4] = this.buildMessage(0x1A4, lonData);
        // Temperature
        String temperatureData = this.packData(this.packed.get("temperature"));
        frame[5] = this.buildMessage(0x1A5, temperatureData);

        return frame;
    }

    private int calcChecksum(String data) {
        int checksum = 0;
        for (int i = 0; i < data.length(); i += 2) {
            checksum += Integer.parseInt(data.substring(i, i + 2), 16);
        }
        checksum %= 256;
        return checksum;
    }

    private String packData(byte[]... data) {
        String dataString = "";

        for (byte[] datum : data) {
            for (byte b : datum) {
                dataString += String.format("%02X", b);
            }
        }

        return dataString;
    }

    private String buildMessage(int arbitrationID, String data) {
        String message = String.format("%03X", arbitrationID) + "#" + data;
        return message;
    }

    public String[] getFrame() {
        return this.frame;
    }
    
}
