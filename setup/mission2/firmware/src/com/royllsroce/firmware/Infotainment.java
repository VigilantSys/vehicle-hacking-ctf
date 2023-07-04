package com.royllsroce.firmware;

import java.util.HashMap;

import com.royllsroce.firmware.config.Config;
import com.royllsroce.firmware.config.ConfigItem;
import com.royllsroce.firmware.infotainment.Interface;
import com.royllsroce.firmware.miscellaneous.Utils;

public class Infotainment {

    public static void main(String[] args) throws Exception {
        // Configure
        ConfigItem[] config = Config.performConfiguration();
        HashMap<String, String> configMap = Config.toMap(config);
        if (Boolean.parseBoolean(configMap.get("debug"))) {
            Utils.enableUtils(config);
        }

        // Boot
        Interface.getInstance().boot(config);
    }
    
}
