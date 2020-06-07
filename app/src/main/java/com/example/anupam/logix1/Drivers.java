package com.example.anupam.logix1;

public class Drivers {

    String lastUpdated;
    String name;
    String onduty;
    String sick;

    Drivers()
    {

    }

    public Drivers(String lastUpdated, String name, String onduty, String sick) {
        this.lastUpdated = lastUpdated;
        this.name = name;
        this.onduty = onduty;
        this.sick = sick;
    }

    public String getLastUpdated() {
        return lastUpdated;
    }

    public void setLastUpdated(String lastUpdated) {
        this.lastUpdated = lastUpdated;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getOnduty() {
        return onduty;
    }

    public void setOnduty(String onduty) {
        this.onduty = onduty;
    }

    public String getSick() {
        return sick;
    }

    public void setSick(String sick) {
        this.sick = sick;
    }
}
