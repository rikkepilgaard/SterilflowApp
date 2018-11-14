package com.example.sterilflowapp;

import android.Manifest;

public class ConstantValues {

    //Bufferzones:
    static final String BUFFER_STERILCENTRAL = "urn:epc:id:sgln:57980101.7856.0";
    static final String BUFFER_NORDLAGER = "urn:epc:id:sgln:57980101.3329.0";
    static final String BUFFER_202 = "urn:epc:id:sgln:57980101.8705.0";
    static final String BUFFER_109 = "urn:epc:id:sgln:57980102.6407.0";
    static final String BUFFER_120 = "urn:epc:id:sgln:57980102.6410.0";
    static final String BUFFER_232 = "urn:epc:id:sgln:57980101.3660.0";
    static final String BUFFER_236 = "urn:epc:id:sgln:57980102.8548.0";
    static final String BUFFER_105 = "urn:epc:id:sgln:57980102.6093.0";


    //Permissions
    static final int PERMISSION_ALL = 1;
    static final String[] PERMISSIONS = {
            Manifest.permission.ACCESS_FINE_LOCATION,
            Manifest.permission.ACCESS_COARSE_LOCATION,
            Manifest.permission.READ_EXTERNAL_STORAGE,
            Manifest.permission.WRITE_EXTERNAL_STORAGE,
            Manifest.permission.INTERNET,
            Manifest.permission.ACCESS_WIFI_STATE,
            Manifest.permission.ACCESS_NETWORK_STATE
    };

}
