package com.example.sterilflowapp;

import android.Manifest;

public class ConstantValues {

    //Fragment tags
    public static final String FRAGMENT_ONE_TAG = "fragment_one";
    public static final String FRAGMENT_TWO_TAG = "fragment_two";

    //Bufferzones:
    public static final String BUFFER_STERILCENTRAL = "urn:epc:id:sgln:57980101.7856.0";
    public static final String BUFFER_NORDLAGER = "urn:epc:id:sgln:57980101.3329.0";
    public static final String BUFFER_202 = "urn:epc:id:sgln:57980101.8705.0";
    public static final String BUFFER_109 = "urn:epc:id:sgln:57980102.6407.0";
    public static final String BUFFER_120 = "urn:epc:id:sgln:57980102.6410.0";
    public static final String BUFFER_232 = "urn:epc:id:sgln:57980101.3660.0";
    public static final String BUFFER_236 = "urn:epc:id:sgln:57980102.8548.0";
    public static final String BUFFER_105 = "urn:epc:id:sgln:57980102.6093.0";


    //Permissions
    public static final int PERMISSION_ALL = 1;
    public static final String[] PERMISSIONS = {
            Manifest.permission.ACCESS_FINE_LOCATION,
            Manifest.permission.ACCESS_COARSE_LOCATION,
            Manifest.permission.READ_EXTERNAL_STORAGE,
            Manifest.permission.WRITE_EXTERNAL_STORAGE,
            Manifest.permission.INTERNET,
            Manifest.permission.ACCESS_WIFI_STATE,
            Manifest.permission.ACCESS_NETWORK_STATE
    };

    //Broadcast actions
    public static final String ACTION_DATA = "data";
    public static final String ACTION_TIME = "time";
    public static final String ACTION_TIME_EXCEEDED = "time_wagon";
    public static final String ACTION_CHANGE_TAB = "changetab";
    public static final String ACTION_NULL = "dataNull";

    //Broadcast extras
    public static final String EXTRA_BUFFERZONE = "bufferzone";

    //Database reference
    public static final String CHILD_NAME = "TrackEvents";

    //XML file names
    public static final String BUFFER_XML = "buffer.xml";
    public static final String BUILDINGS_XML = "AUHbuildings.xml";

    public static final String BUFFERZONE = "bufferzone";
    public static final String BUFFER_NAME = "name";
    public static final String BUFFER_GLN = "gln";
    public static final String BUFFER_FORMER_GLN = "formerGln";
    public static final String BUFFER_LATITUDE = "latitude";
    public static final String BUFFER_LONGITUDE = "longitude";
    public static final String BUFFER_LOCATION = "location";


    public static final String BUILDING = "Building";
    public static final String BUILDING_NAME = "name";
    public static final String BUILDING_LATITUDE = "latitude";
    public static final String BUILDING_LONGITUDE = "longitude";

    //Notification channel
    public static final String NOT_CHANNEL_ID = "channel_id_1";
    public static final String NOT_CHANNEL_NAME = "chanel_name_1";



}
