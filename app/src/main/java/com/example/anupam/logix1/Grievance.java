package com.example.anupam.logix1;

public class Grievance {

    String latlng;
    String truckid;
    String rating;
    Grievance()
    {}

    public Grievance(String latlng, String truckid, String rating) {
        this.latlng = latlng;
        this.truckid = truckid;
        this.rating = rating;
    }

    public String getLatlng() {
        return latlng;
    }

    public void setLatlng(String latlng) {
        this.latlng = latlng;
    }

    public String getTruckid() {
        return truckid;
    }

    public void setTruckid(String truckid) {
        this.truckid = truckid;
    }

    public String getRating() {
        return rating;
    }

    public void setRating(String rating) {
        this.rating = rating;
    }
}
