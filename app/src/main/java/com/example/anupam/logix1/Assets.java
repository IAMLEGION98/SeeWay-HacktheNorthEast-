package com.example.anupam.logix1;

public class Assets {

    String headerid;
    String crateid;
    String condition;
    String type;
    String driver;
    Double currentlat;
    Double currentlng;
    Double destinationlat;
    Double destinationlng;


    Assets()
    {

    }

    public String getHeaderid() {
        return headerid;
    }

    public void setHeaderid(String headerid) {
        this.headerid = headerid;
    }

    public String getCrateid() {
        return crateid;
    }

    public void setCrateid(String crateid) {
        this.crateid = crateid;
    }

    public String getCondition() {
        return condition;
    }

    public void setCondition(String condition) {
        this.condition = condition;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getDriver() {
        return driver;
    }

    public void setDriver(String driver) {
        this.driver = driver;
    }

    public Double getCurrentlat() {
        return currentlat;
    }

    public void setCurrentlat(Double currentlat) {
        this.currentlat = currentlat;
    }

    public Double getCurrentlng() {
        return currentlng;
    }

    public void setCurrentlng(Double currentlng) {
        this.currentlng = currentlng;
    }

    public Double getDestinationlat() {
        return destinationlat;
    }

    public void setDestinationlat(Double destinationlat) {
        this.destinationlat = destinationlat;
    }

    public Double getDestinationlng() {
        return destinationlng;
    }

    public void setDestinationlng(Double destinationlng) {
        this.destinationlng = destinationlng;
    }
}
