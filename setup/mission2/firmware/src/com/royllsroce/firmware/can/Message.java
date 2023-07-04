package com.royllsroce.firmware.can;

public class Message {

    private int id;
    private String data;
    private int dataLength;
    
    public Message(String message) {
        this.parseString(message.trim());
    }

    public Message(int id, String data) {
        this.id = id;
        this.data = data;
        this.dataLength = data.length() / 2;
    }

    private void parseString(String s) {
        String[] parts = s.split("#");
        this.id = Integer.parseInt(parts[0], 16);
        this.data = parts[1];
        this.dataLength = this.data.length() / 2;
    }

    @Override
    public String toString() {
        return String.format("%03X#%s", this.id, this.data);
    }

    public int getId() {
        return this.id;
    }

    public String getData() {
        return this.data;
    }

    public int getDataLength() {
        return this.dataLength;
    }

    public String getDataWithSpaces() {
        String val = "2";   // use 4 here to insert spaces every 4 characters
        String result = this.data.replaceAll("(.{" + val + "})", "$1 ").trim();
        return result;
    }

}
