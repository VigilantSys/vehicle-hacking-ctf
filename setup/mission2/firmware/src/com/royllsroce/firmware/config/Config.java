package com.royllsroce.firmware.config;

import com.royllsroce.firmware.config.ConfigItem;

import java.util.HashMap;

public class Config {
    public static ConfigItem[] performConfiguration() {
        ConfigItem[] configItems = {
            new ConfigItem("SoftwareVersion", "1.0.32"),
            new ConfigItem("InitialIncrement", "65534"),
            new ConfigItem("protocol", "UDP"),
            new ConfigItem("RadioStation", "103.7 FM"),
            new ConfigItem("InitialTemperature", "72"),
            new ConfigItem("TemperatureUnit", "F"),
            new ConfigItem("SpeedUnit", "MPH"),
            new ConfigItem("inputPort", "5000"),
            new ConfigItem("outputPort", "5555"),
            new ConfigItem("debug", "false")
        };

        for (ConfigItem configItem : configItems) {
            configItem.establish();
        }

        return configItems;
    }

    public static HashMap<String, String> toMap(ConfigItem[] configItems) {
        HashMap<String, String> configMap = new HashMap<String, String>();

        for (ConfigItem configItem : configItems) {
            configMap.put(configItem.getName(), configItem.getValue());
        }

        return configMap;
    }
}
