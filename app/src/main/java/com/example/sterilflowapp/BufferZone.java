package com.example.sterilflowapp;

import java.util.ArrayList;

class BufferZone {
    private String name, gln, latitude, longitude, formerLocation;
    private ArrayList<String> containedIn;
    private ArrayList<TrackEvent> vogneList;


    public BufferZone(String name, String gln, String latitude, String longitude, String formerLocation, ArrayList<String> containedIn, ArrayList<TrackEvent> vogneList) {
        this.name = name;
        this.gln = gln;
        this.latitude = latitude;
        this.longitude = longitude;
        this.formerLocation = formerLocation;
        this.containedIn = containedIn;
        this.vogneList = vogneList;
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

    public String getFormerLocation() {
        return formerLocation;
    }

    public void setFormerLocation(String formerLocation) {
        this.formerLocation = formerLocation;
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
