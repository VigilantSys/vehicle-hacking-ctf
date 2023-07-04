package com.royllsroce.sim;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;

import com.royllsroce.sim.io.SimFile;
import com.royllsroce.sim.net.CanClient;
import com.royllsroce.sim.net.SimClient;

public class Simulator {
    
    public static void main(String[] args) {
        int tickTime = 300;
        int simPort = 9999;
        int canPort = 5000;
        String simFilePath = "/com/royllsroce/sim/simfile.csv";

        try {
            SimFile simFile = new SimFile(simFilePath);
            ArrayList<HashMap<String, String>> simData = simFile.getSimData();

            SimClient simClient = new SimClient(simPort);
            CanClient canClient = new CanClient(canPort);

            while (true) {

                for (HashMap<String, String> row : simData) {

                    // Send data to UDP server
                    String line = row.get("line");
                    simClient.send(line);

                    // Send data to CAN server
                    canClient.sendFrame(row);
                Thread.sleep(tickTime);
                }


            }
        } catch (IOException e) {
            System.out.println("Error reading sim file: " + e.getMessage());
            e.printStackTrace();
        } catch (InterruptedException e) {
            System.out.println("Error sleeping thread: " + e.getMessage());
        }

    }

}
