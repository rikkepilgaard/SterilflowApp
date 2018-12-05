package com.example.sterilflowapp.model;

public class TrackEvent {
    private String objectkey, eventTime,floor,locationSgln, timeSincePlacement;
    private boolean expired;

    public TrackEvent() {
    }

    public TrackEvent(String objectkey, String eventTime, String floor,
                      String locationSgln, String timeSincePlacement, boolean expired) {
        this.objectkey = objectkey;
        this.eventTime = eventTime;
        this.floor = floor;
        this.locationSgln = locationSgln;
        this.timeSincePlacement = timeSincePlacement;
        this.expired = expired;
    }

    public String getObjectkey() {
        return objectkey;
    }

    public String getEventTime() {
        return eventTime;
    }

    public String getFloor() {
        return floor;
    }

    public String getLocationSgln() {
        return locationSgln;
    }

    public String getTimeSincePlacement() {
        return timeSincePlacement;
    }

    public void setTimeSincePlacement(String timeSincePlacement) {
        this.timeSincePlacement = timeSincePlacement;
    }

    public boolean isExpired() {
        return expired;
    }

    public void setExpired(boolean expired) {
        this.expired = expired;
    }
}

