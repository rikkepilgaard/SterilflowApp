package com.example.sterilflowapp.model;

public class TrackEvent {
    private String objectkey, eventTime,floor,locationSgln;
    private boolean expired;

    public TrackEvent() {
    }

    public TrackEvent(String objectkey, String eventTime, String floor,
                      String locationSgln, boolean expired) {
        this.objectkey = objectkey;
        this.eventTime = eventTime;
        this.floor = floor;
        this.locationSgln = locationSgln;
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

    public boolean isExpired() {
        return expired;
    }

    public void setExpired(boolean expired) {
        this.expired = expired;
    }
}

