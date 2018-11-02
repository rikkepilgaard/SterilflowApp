package com.example.sterilflowapp;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.nio.Buffer;
import java.util.ArrayList;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.*;

public class DataServiceTest {
    ArrayList<TrackEvent> trackEventArrayList;
    ArrayList<BufferZone> bufferZones;
    ArrayList<TrackEvent> wagonList;
    ArrayList<TrackEvent> arrayList;
    ArrayList<TrackEvent> expectedList;

    @Before
    public void setUp() throws Exception {
        trackEventArrayList=new ArrayList<TrackEvent>();
        bufferZones=new ArrayList<BufferZone>();
        wagonList=new ArrayList<TrackEvent>();
        arrayList = new ArrayList<TrackEvent>();
        expectedList=new ArrayList<TrackEvent>();

    }

    @After
    public void tearDown() throws Exception {
    }

    @Test
    public void wagonInBufferzones120() {
        ArrayList<String> formerGln = new ArrayList<String>();
        formerGln.add("urn:epc:id:sgln:57980102.2859.0");
        ArrayList<String> containedIn = new ArrayList<String>();

        //Initialize bufferzone120
        BufferZone bufferzone120= new BufferZone("Bufferområde 120", "urn:epc:id:sgln:57980102.6410.0", "latitude", "longitude", formerGln, "locationName", containedIn, wagonList);
        bufferZones.add(bufferzone120);
        //Initialize trackevent
        TrackEvent trackEventRecent= new TrackEvent("localityKey", "S30", "comments", "distanceFloor", "distanceMtr", "durationSec", "eventTime",  "floor",  "latitude",  "longitude",  "urn:epc:id:sgln:57980102.6410.0");
        trackEventArrayList.add(trackEventRecent);
        expectedList.add(trackEventRecent);
        arrayList=trackEventArrayList;
        //Act
        DataService dataservice = new DataService();
        dataservice.wagonInBufferzones();

        //Assert
        assertThat(wagonList, is(expectedList));


    }
}