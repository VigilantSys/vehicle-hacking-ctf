package com.royllsroce.sim.can;

import java.io.ByteArrayOutputStream;
import java.io.IOException;

public class RadioStationConverter extends Converter {

    public byte[] convert(String value) {
        String[] split = value.split(" ");
        String station = split[0];
        boolean modulation = split[1].equalsIgnoreCase("AM");

        byte[] packedStation = this.packString(station, 5);
        byte[] packedModulation = this.packBoolean(modulation);

        // Concat values into a single byte array
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream( );
        try {
            outputStream.write( packedStation );
            outputStream.write( packedModulation );
        } catch (IOException e) {
            e.printStackTrace();
        }

        byte packedRadioStation[] = outputStream.toByteArray( );

        return packedRadioStation;
    }
    
}
