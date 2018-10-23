package com.example.sterilflowapp;

import java.util.ArrayList;

class BufferZone {
    private String name, gln, latitude, longitude, formerGln, locationName;
    private ArrayList<String> containedIn;
    private ArrayList<TrackEvent> vogneList;


    public BufferZone(String name, String gln, String latitude, String longitude, String formerGln, String locationName, ArrayList<String> containedIn, ArrayList<TrackEvent> vogneList) {
        this.name = name;
        this.gln = gln;
        this.latitude = latitude;
        this.longitude = longitude;
        this.formerGln = formerGln;
        this.containedIn = containedIn;
        this.vogneList = vogneList;
        this.locationName = locationName;
    }

    public BufferZone() {
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getGln() {
        return gln;
    }

    public void setGln(String gln) {
        this.gln = gln;
    }

    public String getLatitude() {
        return latitude;
    }

    public void setLatitude(String latitude) {
        this.latitude = latitude;
    }

    public String getLongitude() {
        return longitude;
    }

    public void setLongitude(String longitude) {
        this.longitude = longitude;
    }

    public String getFormerGln() {
        return formerGln;
    }

    public void setFormerGln(String formerGln) {
        this.formerGln = formerGln;
    }

    public String getLocationName() {
        return locationName;
    }

    public void setLocationName(String locationName) {
        this.locationName = locationName;
    }

    public ArrayList<String> getContainedIn() {
        return containedIn;
    }

    public void setContainedIn(ArrayList<String> containedIn) {
        this.containedIn = containedIn;
    }

    public ArrayList<TrackEvent> getVogneList() {
        return vogneList;
    }

    public void setVogneList(ArrayList<TrackEvent> vogneList) {
        this.vogneList = vogneList;
    }
}
