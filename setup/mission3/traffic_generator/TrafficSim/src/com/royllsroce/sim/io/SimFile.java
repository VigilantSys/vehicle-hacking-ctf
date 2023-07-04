package com.royllsroce.sim.io;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.HashMap;

public class SimFile {

    BufferedReader reader;
    String header;
    String[] columns;
    ArrayList<HashMap<String, String>> table;

    public SimFile(String filepath) throws IOException {
        InputStream is = getClass().getResourceAsStream(filepath);
        InputStreamReader isr = new InputStreamReader(is);
        this.reader = new BufferedReader(isr);
        this.table = this.readLines();
    }

    private ArrayList<HashMap<String, String>> readLines() throws IOException {
        String line = this.reader.readLine();
        String[] columns = line.split(",");
        ArrayList<HashMap<String, String>> data = new ArrayList<HashMap<String, String>>();
        line = this.reader.readLine();
        while (line != null) {
            String[] values = line.split(",");
            HashMap<String, String> row = new HashMap<String, String>();
            for (int i = 0; i < columns.length; i++) {
                row.put(columns[i].trim(), values[i]);
            }
            row.put("line", line);
            data.add(row);
            line = this.reader.readLine();
        }
        return data;
    }

    public ArrayList<HashMap<String, String>> getSimData() {
        return this.table;
    }

    @Override
    protected void finalize() throws Throwable {
        this.reader.close();
    }
    
}
