package com.royllsroce.firmware.miscellaneous;

import com.royllsroce.firmware.maintenance.MaintenanceOutput;
import com.royllsroce.firmware.maintenance.MaintenanceInput;
import com.royllsroce.firmware.can.Bus;
import com.royllsroce.firmware.config.ConfigItem;

import java.io.IOException;
import java.util.HashMap;

public class Utils {
    public static void enableUtils(ConfigItem[] config) throws IOException {
        // Get configs
        HashMap<String, String> configMap = new HashMap<>();
        for (ConfigItem configItem : config) {
            configMap.put(configItem.getName(), configItem.getValue());
        }
        int inputPort = Integer.parseInt(configMap.get("inputPort"));
        int outputPort = Integer.parseInt(configMap.get("outputPort"));

        // CAN Bus Instance
        Bus bus = new Bus("can0");

        // Start maintenance server
        new Thread(() -> {
            try {
                MaintenanceInput server = new MaintenanceInput(inputPort, bus);
                server.startUDPServer();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }).start();
        new Thread(() -> {
            try {
                MaintenanceOutput client = new MaintenanceOutput(outputPort, bus);
                client.start();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }).start();
    }
}
