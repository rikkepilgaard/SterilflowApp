package com.example.sterilflowapp;

class TrackEvent {
    private String localitykey, objectkey, comments,distanceFloor,distanceMtr,durationSec,eventTime,floor,latitude, longitude,locationSgln;
    private boolean expired;

    public TrackEvent() {
    }

    public TrackEvent(String localitykey, String objectkey, String comments, String distanceFloor, String distanceMtr, String durationSec, String eventTime, String floor, String latitude, String longitude, String locationSgln, boolean expired) {
        this.localitykey = localitykey;
        this.objectkey = objectkey;
        this.comments = comments;
        this.distanceFloor = distanceFloor;
        this.distanceMtr = distanceMtr;
        this.durationSec = durationSec;
        this.eventTime = eventTime;
        this.floor = floor;
        this.latitude = latitude;
        this.longitude = longitude;
        this.locationSgln = locationSgln;
        this.expired = expired;
    }


    public String getLocalitykey() {
        return localitykey;
    }

    public void setLocalitykey(String localityKey) {
        this.localitykey = localityKey;
    }

    public String getObjectkey() {
        return objectkey;
    }

    public void setObjectkey(String objectKey) {
        this.objectkey = objectKey;
    }

    public String getComments() {
        return comments;
    }

    public void setComments(String comments) {
        this.comments = comments;
    }

    public String getDistanceFloor() {
        return distanceFloor;
    }

    public void setDistanceFloor(String distanceFloor) {
        this.distanceFloor = distanceFloor;
    }

    public String getDistanceMtr() {
        return distanceMtr;
    }

    public void setDistanceMtr(String distanceMtr) {
        this.distanceMtr = distanceMtr;
    }

    public String getDurationSec() {
        return durationSec;
    }

    public void setDurationSec(String durationSec) {
        this.durationSec = durationSec;
    }

    public String getEventTime() {
        return eventTime;
    }

    public void setEventTime(String eventTime) {
        this.eventTime = eventTime;
    }

    public String getFloor() {
        return floor;
    }

    public void setFloor(String floor) {
        this.floor = floor;
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

    public String getLocationSgln() {
        return locationSgln;
    }

    public void setLocationSgln(String locationSgln) {
        this.locationSgln = locationSgln;
    }

    public boolean isExpired() {
        return expired;
    }

    public void setExpired(boolean expired) {
        this.expired = expired;
    }
}

