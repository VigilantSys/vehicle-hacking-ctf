package com.royllsroce.sim.can;

public class ConverterFactory {

    public Converter getConverter(String name) {
        Converter converter;
        switch (name.toLowerCase()) {
            case "speed":
                converter = new SpeedConverter();
                break;
            case "rpm":
                converter = new RpmConverter();
                break;
            case "fuel":
                converter = new FuelConverter();
                break;
            case "temperature":
                converter = new TemperatureConverter();
                break;
            case "longitude":
                converter = new CoordinateConverter();
                break;
            case "latitude":
                converter = new CoordinateConverter();
                break;
            case "radiostation":
                converter = new RadioStationConverter();
                break;
            case "gear":
                converter = new GearConverter();
                break;
            default:
                throw new IllegalArgumentException("Invalid converter name: " + name);
        }
        return converter;
    }
    
}
