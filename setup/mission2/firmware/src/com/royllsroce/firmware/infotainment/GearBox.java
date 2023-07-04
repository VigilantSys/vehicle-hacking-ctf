package com.royllsroce.firmware.infotainment;

import java.util.List;
import java.util.Arrays;

public class GearBox implements Component {
    
    private static final String[] GEARLIST = {"park", "reverse", "neutral", "drive", "low"};
    private static final List<String> GEARS = Arrays.asList(GEARLIST);
    private String gear;
    
    public GearBox(String gear) {
        this.setGear(gear);
    }
    
    public String getGear() {
        return this.gear;
    }
    
    public void setGear(String gear) {
        if (GEARS.contains(gear)) {
            this.gear = gear;
        } else {
            throw new IllegalArgumentException("Invalid gear");
        }    
    }
    
    public Object get(String key) {
        if (key == "gear") {
            return this.getGear();
        } else {
            throw new IllegalArgumentException("Invalid key");
        }
    }

}
