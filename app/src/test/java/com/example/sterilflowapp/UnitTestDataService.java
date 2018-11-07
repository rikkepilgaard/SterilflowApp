package com.example.sterilflowapp;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertThat;


public class UnitTestDataService {
    ArrayList<TrackEvent> trackEventArrayList;
    ArrayList<BufferZone> bufferZones;
    ArrayList<TrackEvent> arrayList;
    ArrayList<TrackEvent> wagonList;



    @Before
    public void setUp() throws Exception {
        trackEventArrayList=new ArrayList<TrackEvent>();
        bufferZones=new ArrayList<BufferZone>();
        wagonList=new ArrayList<TrackEvent>();
        arrayList = new ArrayList<TrackEvent>();
    }

    @After
    public void tearDown() throws Exception {
        bufferZones.clear();
        trackEventArrayList.clear();
        wagonList.clear();
    }

    @Test
    public void Bufferzone202AddOneWagon(){
        //Arrange
        DataService dataService= new DataService();

        ArrayList<String> formerGln = new ArrayList<String>();
        formerGln.add("urn:epc:id:sgln:57980102.2859.0");
        ArrayList<String> containedIn = new ArrayList<String>();


        bufferZones= new ArrayList<BufferZone>();
        BufferZone bufferzone202= new BufferZone("Bufferområde 202", "urn:epc:id:sgln:57980101.8705.0", "latitude", "longitude", formerGln, "locationName", containedIn, wagonList);
        bufferZones.add(bufferzone202);


        trackEventArrayList= new ArrayList<TrackEvent>();
        TrackEvent trackEventRecent1= new TrackEvent("localityKey", "S30", "comments", "distanceFloor", "distanceMtr", "durationSec", "06:03",  "floor",  "latitude",  "longitude",  "urn:epc:id:sgln:57980102.2859.0",false);
        TrackEvent trackEventRecent2= new TrackEvent("localityKey", "S30", "comments", "distanceFloor", "distanceMtr", "durationSec", "08:03",  "floor",  "latitude",  "longitude",  "urn:epc:id:sgln:57980101.8705.0",false);
        trackEventArrayList.add(trackEventRecent1);
        trackEventArrayList.add(trackEventRecent2);

        ArrayList<TrackEvent> expectedList = new ArrayList<TrackEvent>();
        expectedList.add(trackEventRecent2);


        //Act
        dataService.setBufferZones(bufferZones);
        dataService.setTrackEventArrayList(trackEventArrayList);

        dataService.wagonInBufferzones();



        //Assert
        ArrayList<BufferZone> actualList = dataService.getBufferZoneList();
        BufferZone actualBuffer = actualList.get(0);

        assertThat(actualBuffer.getWagonList(), is(expectedList));
    }
    @Test
    public void Bufferzone202NotAddWagon(){
        //Arrange
        DataService dataService= new DataService();

        ArrayList<String> formerGln = new ArrayList<String>();
        formerGln.add("urn:epc:id:sgln:57980102.2859.0");
        ArrayList<String> containedIn = new ArrayList<String>();


        bufferZones= new ArrayList<BufferZone>();
        BufferZone bufferzone202= new BufferZone("Bufferområde 202", "urn:epc:id:sgln:57980101.8705.0", "latitude", "longitude", formerGln, "locationName", containedIn, wagonList);
        bufferZones.add(bufferzone202);


        trackEventArrayList= new ArrayList<TrackEvent>();
        TrackEvent trackEventRecent1= new TrackEvent("localityKey", "S30", "comments", "distanceFloor", "distanceMtr", "durationSec", "06:03",  "floor",  "latitude",  "longitude",  "urn:epc:id:sgln:57980102.2839.0",false);
        TrackEvent trackEventRecent2= new TrackEvent("localityKey", "S30", "comments", "distanceFloor", "distanceMtr", "durationSec", "08:03",  "floor",  "latitude",  "longitude",  "urn:epc:id:sgln:57980101.8705.0",false);
        trackEventArrayList.add(trackEventRecent1);
        trackEventArrayList.add(trackEventRecent2);


        //Act
        dataService.setBufferZones(bufferZones);
        dataService.setTrackEventArrayList(trackEventArrayList);

        dataService.wagonInBufferzones();



        //Assert
        ArrayList<BufferZone> actualList = dataService.getBufferZoneList();
        BufferZone actualBuffer = actualList.get(0);

        assertNull(actualBuffer.getWagonList());
    }
    @Test
    public void Bufferzone202AddTwoWagons(){
        //Arrange
        DataService dataService= new DataService();

        ArrayList<String> formerGln = new ArrayList<String>();
        formerGln.add("urn:epc:id:sgln:57980102.2859.0");
        ArrayList<String> containedIn = new ArrayList<String>();


        bufferZones= new ArrayList<BufferZone>();
        BufferZone bufferzone202= new BufferZone("Bufferområde 202", "urn:epc:id:sgln:57980101.8705.0", "latitude", "longitude", formerGln, "locationName", containedIn, wagonList);
        bufferZones.add(bufferzone202);


        trackEventArrayList= new ArrayList<TrackEvent>();
        TrackEvent trackEventS30track1= new TrackEvent("localityKey", "S30", "comments", "distanceFloor", "distanceMtr", "durationSec", "06:03",  "floor",  "latitude",  "longitude",  "urn:epc:id:sgln:57980102.2859.0",false);
        TrackEvent trackEventS30track2= new TrackEvent("localityKey", "S30", "comments", "distanceFloor", "distanceMtr", "durationSec", "08:03",  "floor",  "latitude",  "longitude",  "urn:epc:id:sgln:57980101.8705.0",false);
        TrackEvent trackEventS20track1= new TrackEvent("localityKey", "S20", "comments", "distanceFloor", "distanceMtr", "durationSec", "06:03",  "floor",  "latitude",  "longitude",  "urn:epc:id:sgln:57980102.2859.0",false);
        TrackEvent trackEventS20track2= new TrackEvent("localityKey", "S20", "comments", "distanceFloor", "distanceMtr", "durationSec", "08:03",  "floor",  "latitude",  "longitude",  "urn:epc:id:sgln:57980101.8705.0",false);
        trackEventArrayList.add(trackEventS30track1);
        trackEventArrayList.add(trackEventS30track2);
        trackEventArrayList.add(trackEventS20track1);
        trackEventArrayList.add(trackEventS20track2);


        ArrayList<TrackEvent> expectedList = new ArrayList<TrackEvent>();
        expectedList.add(trackEventS30track2);
        expectedList.add(trackEventS20track2);

        //Act
        dataService.setBufferZones(bufferZones);
        dataService.setTrackEventArrayList(trackEventArrayList);

        dataService.wagonInBufferzones();



        //Assert
        ArrayList<BufferZone> actualList = dataService.getBufferZoneList();
        BufferZone actualBuffer = actualList.get(0);

        assertThat(actualBuffer.getWagonList().size(),is(2));
    }

    @Test
    public void BufferzoneSterilcentralAddOneWagon(){
        //Arrange
        DataService dataService= new DataService();

        ArrayList<String> formerGln = new ArrayList<String>();
        ArrayList<String> containedIn = new ArrayList<String>();


        bufferZones= new ArrayList<BufferZone>();
        BufferZone bufferzoneSteril= new BufferZone("Bufferområde SterilCentral", "urn:epc:id:sgln:57980101.7856.0", "latitude", "longitude", formerGln, "locationName", containedIn, wagonList);
        bufferZones.add(bufferzoneSteril);


        trackEventArrayList= new ArrayList<TrackEvent>();
        TrackEvent trackEventRecent= new TrackEvent("localityKey", "S30", "comments", "distanceFloor", "distanceMtr", "durationSec", "eventTime",  "floor",  "latitude",  "longitude",  "urn:epc:id:sgln:57980101.7856.0",false);
        trackEventArrayList.add(trackEventRecent);


        //Act
        dataService.setBufferZones(bufferZones);
        dataService.setTrackEventArrayList(trackEventArrayList);

        dataService.wagonInBufferzones();


        //Assert
        ArrayList<BufferZone> actualList = dataService.getBufferZoneList();
        BufferZone actualBuffer = actualList.get(0);

        assertThat(actualBuffer.getWagonList(), is(trackEventArrayList));
    }
    @Test
    public void BufferzoneSterilcentralAddTwoWagons(){
        //Arrange
        DataService dataService= new DataService();

        ArrayList<String> formerGln = new ArrayList<String>();
        ArrayList<String> containedIn = new ArrayList<String>();


        bufferZones= new ArrayList<BufferZone>();
        BufferZone bufferzoneSteril= new BufferZone("Bufferområde SterilCentral", "urn:epc:id:sgln:57980101.7856.0", "latitude", "longitude", formerGln, "locationName", containedIn, wagonList);
        bufferZones.add(bufferzoneSteril);


        trackEventArrayList= new ArrayList<TrackEvent>();
        TrackEvent trackEventS30track1= new TrackEvent("localityKey", "S30", "comments", "distanceFloor", "distanceMtr", "durationSec", "06:03",  "floor",  "latitude",  "longitude",  "urn:epc:id:sgln:57980102.2859.0",false);
        TrackEvent trackEventS30track2= new TrackEvent("localityKey", "S30", "comments", "distanceFloor", "distanceMtr", "durationSec", "08:03",  "floor",  "latitude",  "longitude",  "urn:epc:id:sgln:57980101.7856.0",false);
        TrackEvent trackEventS20track1= new TrackEvent("localityKey", "S20", "comments", "distanceFloor", "distanceMtr", "durationSec", "06:03",  "floor",  "latitude",  "longitude",  "urn:epc:id:sgln:57980102.2859.0",false);
        TrackEvent trackEventS20track2= new TrackEvent("localityKey", "S20", "comments", "distanceFloor", "distanceMtr", "durationSec", "08:03",  "floor",  "latitude",  "longitude",  "urn:epc:id:sgln:57980101.7856.0",false);
        trackEventArrayList.add(trackEventS30track1);
        trackEventArrayList.add(trackEventS30track2);
        trackEventArrayList.add(trackEventS20track1);
        trackEventArrayList.add(trackEventS20track2);


        //Act
        dataService.setBufferZones(bufferZones);
        dataService.setTrackEventArrayList(trackEventArrayList);

        dataService.wagonInBufferzones();


        //Assert
        ArrayList<BufferZone> actualList = dataService.getBufferZoneList();
        BufferZone actualBuffer = actualList.get(0);

        assertThat(actualBuffer.getWagonList().size(), is(2));
    }
    @Test
    public void BufferzoneSteilcentralNotAddWagon(){
        //Arrange
        DataService dataService= new DataService();

        ArrayList<String> formerGln = new ArrayList<String>();
        ArrayList<String> containedIn = new ArrayList<String>();


        bufferZones= new ArrayList<BufferZone>();
        BufferZone bufferzoneSteril= new BufferZone("Bufferområde SterilCentral", "urn:epc:id:sgln:57980101.7856.0", "latitude", "longitude", formerGln, "locationName", containedIn, wagonList);
        bufferZones.add(bufferzoneSteril);


        trackEventArrayList= new ArrayList<TrackEvent>();
        TrackEvent trackEventRecent= new TrackEvent("localityKey", "S30", "comments", "distanceFloor", "distanceMtr", "durationSec", "eventTime",  "floor",  "latitude",  "longitude",  "urn:epc:id:sgln:57900101.7856.0",false);
        trackEventArrayList.add(trackEventRecent);


        //Act
        dataService.setBufferZones(bufferZones);
        dataService.setTrackEventArrayList(trackEventArrayList);

        dataService.wagonInBufferzones();


        //Assert
        ArrayList<BufferZone> actualList = dataService.getBufferZoneList();
        BufferZone actualBuffer = actualList.get(0);

        assertNull(actualBuffer.getWagonList());
    }

    @Test
    public void Bufferzone109AddOneWagon(){
        //Arrange
        DataService dataService= new DataService();

        ArrayList<String> formerGln = new ArrayList<String>();
        ArrayList<String> containedIn = new ArrayList<String>();


        bufferZones= new ArrayList<BufferZone>();
        BufferZone bufferzone109= new BufferZone("Bufferområde 109", "urn:epc:id:sgln:57980102.6407.0", "latitude", "longitude", formerGln, "locationName", containedIn, wagonList);
        bufferZones.add(bufferzone109);


        trackEventArrayList= new ArrayList<TrackEvent>();
        TrackEvent trackEventRecent= new TrackEvent("localityKey", "S30", "comments", "distanceFloor", "distanceMtr", "durationSec", "eventTime",  "floor",  "latitude",  "longitude",  "urn:epc:id:sgln:57980102.6407.0",false);
        trackEventArrayList.add(trackEventRecent);


        //Act
        dataService.setBufferZones(bufferZones);
        dataService.setTrackEventArrayList(trackEventArrayList);

        dataService.wagonInBufferzones();


        //Assert
        ArrayList<BufferZone> actualList = dataService.getBufferZoneList();
        BufferZone actualBuffer = actualList.get(0);

        assertThat(actualBuffer.getWagonList(), is(trackEventArrayList));
    }
    @Test
    public void Bufferzone109AddTwoWagons(){
        //Arrange
        DataService dataService= new DataService();

        ArrayList<String> formerGln = new ArrayList<String>();
        ArrayList<String> containedIn = new ArrayList<String>();


        bufferZones= new ArrayList<BufferZone>();
        BufferZone bufferzone109= new BufferZone("Bufferområde SterilCentral", "urn:epc:id:sgln:57980102.6407.0", "latitude", "longitude", formerGln, "locationName", containedIn, wagonList);
        bufferZones.add(bufferzone109);


        trackEventArrayList= new ArrayList<TrackEvent>();
        TrackEvent trackEventS30track1= new TrackEvent("localityKey", "S30", "comments", "distanceFloor", "distanceMtr", "durationSec", "06:03",  "floor",  "latitude",  "longitude",  "urn:epc:id:sgln:57980102.2859.0",false);
        TrackEvent trackEventS30track2= new TrackEvent("localityKey", "S30", "comments", "distanceFloor", "distanceMtr", "durationSec", "08:03",  "floor",  "latitude",  "longitude",  "urn:epc:id:sgln:57980102.6407.0",false);
        TrackEvent trackEventS20track1= new TrackEvent("localityKey", "S20", "comments", "distanceFloor", "distanceMtr", "durationSec", "06:03",  "floor",  "latitude",  "longitude",  "urn:epc:id:sgln:57980102.2859.0",false);
        TrackEvent trackEventS20track2= new TrackEvent("localityKey", "S20", "comments", "distanceFloor", "distanceMtr", "durationSec", "08:03",  "floor",  "latitude",  "longitude",  "urn:epc:id:sgln:57980102.6407.0",false);
        trackEventArrayList.add(trackEventS30track1);
        trackEventArrayList.add(trackEventS30track2);
        trackEventArrayList.add(trackEventS20track1);
        trackEventArrayList.add(trackEventS20track2);


        //Act
        dataService.setBufferZones(bufferZones);
        dataService.setTrackEventArrayList(trackEventArrayList);

        dataService.wagonInBufferzones();


        //Assert
        ArrayList<BufferZone> actualList = dataService.getBufferZoneList();
        BufferZone actualBuffer = actualList.get(0);

        assertThat(actualBuffer.getWagonList().size(), is(2));
    }
    @Test
    public void Bufferzone109NotAddWagon(){
        //Arrange
        DataService dataService= new DataService();

        ArrayList<String> formerGln = new ArrayList<String>();
        ArrayList<String> containedIn = new ArrayList<String>();


        bufferZones= new ArrayList<BufferZone>();
        BufferZone bufferzone109= new BufferZone("Bufferområde 109", "urn:epc:id:sgln:57980102.6407.0", "latitude", "longitude", formerGln, "locationName", containedIn, wagonList);
        bufferZones.add(bufferzone109);


        trackEventArrayList= new ArrayList<TrackEvent>();
        TrackEvent trackEventRecent= new TrackEvent("localityKey", "S30", "comments", "distanceFloor", "distanceMtr", "durationSec", "eventTime",  "floor",  "latitude",  "longitude",  "urn:epc:id:sgln:57900101.7856.0",false);
        trackEventArrayList.add(trackEventRecent);


        //Act
        dataService.setBufferZones(bufferZones);
        dataService.setTrackEventArrayList(trackEventArrayList);

        dataService.wagonInBufferzones();


        //Assert
        ArrayList<BufferZone> actualList = dataService.getBufferZoneList();
        BufferZone actualBuffer = actualList.get(0);

        assertNull(actualBuffer.getWagonList());
    }

    @Test
    public void Bufferzone120AddOneWagon() {
        //Arrange
        DataService dataService= new DataService();

        ArrayList<String> formerGln = new ArrayList<String>();
        formerGln.add("urn:epc:id:sgln:57980102.2859.0");
        ArrayList<String> containedIn = new ArrayList<String>();


        bufferZones= new ArrayList<BufferZone>();
        BufferZone bufferzone120= new BufferZone("Bufferområde 120", "urn:epc:id:sgln:57980102.6410.0", "latitude", "longitude", formerGln, "locationName", containedIn, wagonList);
        bufferZones.add(bufferzone120);


        trackEventArrayList= new ArrayList<TrackEvent>();
        TrackEvent trackEventRecent= new TrackEvent("localityKey", "S30", "comments", "distanceFloor", "distanceMtr", "durationSec", "eventTime",  "floor",  "latitude",  "longitude",  "urn:epc:id:sgln:57980102.6410.0",false);
        trackEventArrayList.add(trackEventRecent);


        //Act
        dataService.setBufferZones(bufferZones);
        dataService.setTrackEventArrayList(trackEventArrayList);

        dataService.wagonInBufferzones();



        //Assert
        ArrayList<BufferZone> actualList = dataService.getBufferZoneList();
        BufferZone actualBuffer = actualList.get(0);

        assertThat(actualBuffer.getWagonList(), is(trackEventArrayList));

    }
    @Test
    public void Bufferzone120NotAddWagon(){
        //Arrange
        DataService dataService= new DataService();

        ArrayList<String> formerGln = new ArrayList<String>();
        formerGln.add("urn:epc:id:sgln:57980102.2859.0");
        ArrayList<String> containedIn = new ArrayList<String>();


        bufferZones= new ArrayList<BufferZone>();
        BufferZone bufferzone120= new BufferZone("Bufferområde 120", "urn:epc:id:sgln:57980102.6410.0", "latitude", "longitude", formerGln, "locationName", containedIn, wagonList);
        bufferZones.add(bufferzone120);


        trackEventArrayList= new ArrayList<TrackEvent>();
        TrackEvent trackEventRecent= new TrackEvent("localityKey", "S30", "comments", "distanceFloor", "distanceMtr", "durationSec", "eventTime",  "floor",  "latitude",  "longitude",  "urn:epc:id:sgln:57980902.6410.0",false);
        trackEventArrayList.add(trackEventRecent);


        //Act
        dataService.setBufferZones(bufferZones);
        dataService.setTrackEventArrayList(trackEventArrayList);

        dataService.wagonInBufferzones();



        //Assert
        ArrayList<BufferZone> actualList = dataService.getBufferZoneList();
        BufferZone actualBuffer = actualList.get(0);

        assertNull(actualBuffer.getWagonList());


    }

    @Test
    public void BufferzoneNordAddOneWagon(){
        //Arrange
        DataService dataService= new DataService();

        ArrayList<String> formerGln = new ArrayList<String>();
        formerGln.add("urn:epc:id:sgln:57980102.1414.0");
        ArrayList<String> containedIn = new ArrayList<String>();


        bufferZones= new ArrayList<BufferZone>();
        BufferZone bufferzoneNord= new BufferZone("Bufferområde Nord", "urn:epc:id:sgln:57980101.5946.0", "latitude", "longitude", formerGln, "locationName", containedIn, wagonList);
        bufferZones.add(bufferzoneNord);


        trackEventArrayList= new ArrayList<TrackEvent>();
        TrackEvent trackEventRecent1= new TrackEvent("localityKey", "S30", "comments", "distanceFloor", "distanceMtr", "durationSec", "06:03",  "floor",  "latitude",  "longitude",  "urn:epc:id:sgln:57980102.1414.0",false);
        TrackEvent trackEventRecent2= new TrackEvent("localityKey", "S30", "comments", "distanceFloor", "distanceMtr", "durationSec", "08:03",  "floor",  "latitude",  "longitude",  "urn:epc:id:sgln:57980101.5946.0",false);
        trackEventArrayList.add(trackEventRecent1);
        trackEventArrayList.add(trackEventRecent2);

        ArrayList<TrackEvent> expectedList = new ArrayList<TrackEvent>();
        expectedList.add(trackEventRecent2);

        //Act
        dataService.setBufferZones(bufferZones);
        dataService.setTrackEventArrayList(trackEventArrayList);

        dataService.wagonInBufferzones();



        //Assert
        ArrayList<BufferZone> actualList = dataService.getBufferZoneList();
        BufferZone actualBuffer = actualList.get(0);

        assertThat(actualBuffer.getWagonList(), is(expectedList));
    }
    @Test
    public void BufferzoneNordAddTwoWagons(){
        //Arrange
        DataService dataService= new DataService();

        ArrayList<String> formerGln = new ArrayList<String>();
        formerGln.add("urn:epc:id:sgln:57980102.1414.0");
        ArrayList<String> containedIn = new ArrayList<String>();


        bufferZones= new ArrayList<BufferZone>();
        BufferZone bufferzoneNord= new BufferZone("Bufferområde Nord", "urn:epc:id:sgln:57980101.5946.0", "latitude", "longitude", formerGln, "locationName", containedIn, wagonList);
        bufferZones.add(bufferzoneNord);


        trackEventArrayList= new ArrayList<TrackEvent>();
        TrackEvent trackEventS30track1= new TrackEvent("localityKey", "S30", "comments", "distanceFloor", "distanceMtr", "durationSec", "06:03",  "floor",  "latitude",  "longitude",  "urn:epc:id:sgln:57980102.1414.0",false);
        TrackEvent trackEventS30track2= new TrackEvent("localityKey", "S30", "comments", "distanceFloor", "distanceMtr", "durationSec", "08:03",  "floor",  "latitude",  "longitude",  "urn:epc:id:sgln:57980101.5946.0",false);
        TrackEvent trackEventS20track1= new TrackEvent("localityKey", "S20", "comments", "distanceFloor", "distanceMtr", "durationSec", "06:03",  "floor",  "latitude",  "longitude",  "urn:epc:id:sgln:57980102.1414.0",false);
        TrackEvent trackEventS20track2= new TrackEvent("localityKey", "S20", "comments", "distanceFloor", "distanceMtr", "durationSec", "08:03",  "floor",  "latitude",  "longitude",  "urn:epc:id:sgln:57980101.5946.0",false);
        trackEventArrayList.add(trackEventS30track1);
        trackEventArrayList.add(trackEventS30track2);
        trackEventArrayList.add(trackEventS20track1);
        trackEventArrayList.add(trackEventS20track2);


        //Act
        dataService.setBufferZones(bufferZones);
        dataService.setTrackEventArrayList(trackEventArrayList);

        dataService.wagonInBufferzones();



        //Assert
        ArrayList<BufferZone> actualList = dataService.getBufferZoneList();
        BufferZone actualBuffer = actualList.get(0);

        assertThat(actualBuffer.getWagonList().size(),is(2));
    }
    @Test
    public void BufferzoneNordNotAddWagon(){
        //Arrange
        DataService dataService= new DataService();

        ArrayList<String> formerGln = new ArrayList<String>();
        formerGln.add("urn:epc:id:sgln:57980102.1414.0");
        ArrayList<String> containedIn = new ArrayList<String>();


        bufferZones= new ArrayList<BufferZone>();
        BufferZone bufferzoneNord= new BufferZone("Bufferområde Nord", "urn:epc:id:sgln:57980101.5946.0", "latitude", "longitude", formerGln, "locationName", containedIn, wagonList);
        bufferZones.add(bufferzoneNord);


        trackEventArrayList= new ArrayList<TrackEvent>();
        TrackEvent trackEventRecent1= new TrackEvent("localityKey", "S30", "comments", "distanceFloor", "distanceMtr", "durationSec", "06:03",  "floor",  "latitude",  "longitude",  "urn:epc:id:sgln:57980102.2839.0",false);
        TrackEvent trackEventRecent2= new TrackEvent("localityKey", "S30", "comments", "distanceFloor", "distanceMtr", "durationSec", "08:03",  "floor",  "latitude",  "longitude",  "urn:epc:id:sgln:57980101.5946.0",false);
        trackEventArrayList.add(trackEventRecent1);
        trackEventArrayList.add(trackEventRecent2);


        //Act
        dataService.setBufferZones(bufferZones);
        dataService.setTrackEventArrayList(trackEventArrayList);

        dataService.wagonInBufferzones();



        //Assert
        ArrayList<BufferZone> actualList = dataService.getBufferZoneList();
        BufferZone actualBuffer = actualList.get(0);

        assertNull(actualBuffer.getWagonList());
    }

    @Test
    public void Bufferzone232P1AddOneWagon(){
        //Arrange
        DataService dataService= new DataService();

        ArrayList<String> formerGln = new ArrayList<String>();
        ArrayList<String> containedIn = new ArrayList<String>();


        bufferZones= new ArrayList<BufferZone>();
        BufferZone bufferzone232= new BufferZone("Bufferområde 232", "urn:epc:id:sgln:57980101.3660.0", "latitude", "longitude", formerGln, "locationName", containedIn, wagonList);
        bufferZones.add(bufferzone232);


        trackEventArrayList= new ArrayList<TrackEvent>();
        TrackEvent trackEventRecent= new TrackEvent("localityKey", "S30", "comments", "distanceFloor", "distanceMtr", "durationSec", "eventTime",  "floor",  "latitude",  "longitude",  "urn:epc:id:sgln:57980101.3660.0",false);
        trackEventArrayList.add(trackEventRecent);


        //Act
        dataService.setBufferZones(bufferZones);
        dataService.setTrackEventArrayList(trackEventArrayList);

        dataService.wagonInBufferzones();


        //Assert
        ArrayList<BufferZone> actualList = dataService.getBufferZoneList();
        BufferZone actualBuffer = actualList.get(0);

        assertThat(actualBuffer.getWagonList(), is(trackEventArrayList));
    }
    @Test
    public void Bufferzone232P1AddTwoWagons(){
        //Arrange
        DataService dataService= new DataService();

        ArrayList<String> formerGln = new ArrayList<String>();
        ArrayList<String> containedIn = new ArrayList<String>();


        bufferZones= new ArrayList<BufferZone>();
        BufferZone bufferzone232= new BufferZone("Bufferområde 232-P1", "urn:epc:id:sgln:57980101.3660.0", "latitude", "longitude", formerGln, "locationName", containedIn, wagonList);
        bufferZones.add(bufferzone232);


        trackEventArrayList= new ArrayList<TrackEvent>();
        TrackEvent trackEventS30track1= new TrackEvent("localityKey", "S30", "comments", "distanceFloor", "distanceMtr", "durationSec", "06:03",  "floor",  "latitude",  "longitude",  "urn:epc:id:sgln:57980102.2859.0",false);
        TrackEvent trackEventS30track2= new TrackEvent("localityKey", "S30", "comments", "distanceFloor", "distanceMtr", "durationSec", "08:03",  "floor",  "latitude",  "longitude",  "urn:epc:id:sgln:57980101.3660.0",false);
        TrackEvent trackEventS20track1= new TrackEvent("localityKey", "S20", "comments", "distanceFloor", "distanceMtr", "durationSec", "06:03",  "floor",  "latitude",  "longitude",  "urn:epc:id:sgln:57980102.2859.0",false);
        TrackEvent trackEventS20track2= new TrackEvent("localityKey", "S20", "comments", "distanceFloor", "distanceMtr", "durationSec", "08:03",  "floor",  "latitude",  "longitude",  "urn:epc:id:sgln:57980101.3660.0",false);
        trackEventArrayList.add(trackEventS30track1);
        trackEventArrayList.add(trackEventS30track2);
        trackEventArrayList.add(trackEventS20track1);
        trackEventArrayList.add(trackEventS20track2);


        //Act
        dataService.setBufferZones(bufferZones);
        dataService.setTrackEventArrayList(trackEventArrayList);

        dataService.wagonInBufferzones();


        //Assert
        ArrayList<BufferZone> actualList = dataService.getBufferZoneList();
        BufferZone actualBuffer = actualList.get(0);

        assertThat(actualBuffer.getWagonList().size(), is(2));
    }
    @Test
    public void Bufferzone232P1NotAddWagon(){
        //Arrange
        DataService dataService= new DataService();

        ArrayList<String> formerGln = new ArrayList<String>();
        ArrayList<String> containedIn = new ArrayList<String>();


        bufferZones= new ArrayList<BufferZone>();
        BufferZone bufferzone232= new BufferZone("Bufferområde 232-P1", "urn:epc:id:sgln:57980101.3660.0", "latitude", "longitude", formerGln, "locationName", containedIn, wagonList);
        bufferZones.add(bufferzone232);


        trackEventArrayList= new ArrayList<TrackEvent>();
        TrackEvent trackEventRecent= new TrackEvent("localityKey", "S30", "comments", "distanceFloor", "distanceMtr", "durationSec", "eventTime",  "floor",  "latitude",  "longitude",  "urn:epc:id:sgln:57900101.0006.0",false);
        trackEventArrayList.add(trackEventRecent);


        //Act
        dataService.setBufferZones(bufferZones);
        dataService.setTrackEventArrayList(trackEventArrayList);

        dataService.wagonInBufferzones();


        //Assert
        ArrayList<BufferZone> actualList = dataService.getBufferZoneList();
        BufferZone actualBuffer = actualList.get(0);

        assertNull(actualBuffer.getWagonList());
    }

    @Test
    public void Bufferzone236P1AddOneWagon(){

        //Arrange
        DataService dataService= new DataService();

        ArrayList<String> formerGln = new ArrayList<String>();
        ArrayList<String> containedIn = new ArrayList<String>();


        bufferZones= new ArrayList<BufferZone>();
        BufferZone bufferzone236= new BufferZone("Bufferområde 236", "urn:epc:id:sgln:57980102.8548.0", "latitude", "longitude", formerGln, "locationName", containedIn, wagonList);
        bufferZones.add(bufferzone236);


        trackEventArrayList= new ArrayList<TrackEvent>();
        TrackEvent trackEventRecent= new TrackEvent("localityKey", "S30", "comments", "distanceFloor", "distanceMtr", "durationSec", "eventTime",  "floor",  "latitude",  "longitude",  "urn:epc:id:sgln:57980102.8548.0",false);
        trackEventArrayList.add(trackEventRecent);


        //Act
        dataService.setBufferZones(bufferZones);
        dataService.setTrackEventArrayList(trackEventArrayList);

        dataService.wagonInBufferzones();


        //Assert
        ArrayList<BufferZone> actualList = dataService.getBufferZoneList();
        BufferZone actualBuffer = actualList.get(0);

        assertThat(actualBuffer.getWagonList(), is(trackEventArrayList));
    }
    @Test
    public void Bufferzone236P1AddTwoWagons(){
        //Arrange
        DataService dataService= new DataService();

        ArrayList<String> formerGln = new ArrayList<String>();
        ArrayList<String> containedIn = new ArrayList<String>();


        bufferZones= new ArrayList<BufferZone>();
        BufferZone bufferzone236= new BufferZone("Bufferområde 236", "urn:epc:id:sgln:57980102.8548.0", "latitude", "longitude", formerGln, "locationName", containedIn, wagonList);
        bufferZones.add(bufferzone236);


        trackEventArrayList= new ArrayList<TrackEvent>();
        TrackEvent trackEventS30track1= new TrackEvent("localityKey", "S30", "comments", "distanceFloor", "distanceMtr", "durationSec", "06:03",  "floor",  "latitude",  "longitude",  "urn:epc:id:sgln:57980102.2859.0",false);
        TrackEvent trackEventS30track2= new TrackEvent("localityKey", "S30", "comments", "distanceFloor", "distanceMtr", "durationSec", "08:03",  "floor",  "latitude",  "longitude",  "urn:epc:id:sgln:57980102.8548.0",false);
        TrackEvent trackEventS20track1= new TrackEvent("localityKey", "S20", "comments", "distanceFloor", "distanceMtr", "durationSec", "06:03",  "floor",  "latitude",  "longitude",  "urn:epc:id:sgln:57980102.2859.0",false);
        TrackEvent trackEventS20track2= new TrackEvent("localityKey", "S20", "comments", "distanceFloor", "distanceMtr", "durationSec", "08:03",  "floor",  "latitude",  "longitude",  "urn:epc:id:sgln:57980102.8548.0",false);
        trackEventArrayList.add(trackEventS30track1);
        trackEventArrayList.add(trackEventS30track2);
        trackEventArrayList.add(trackEventS20track1);
        trackEventArrayList.add(trackEventS20track2);


        //Act
        dataService.setBufferZones(bufferZones);
        dataService.setTrackEventArrayList(trackEventArrayList);

        dataService.wagonInBufferzones();


        //Assert
        ArrayList<BufferZone> actualList = dataService.getBufferZoneList();
        BufferZone actualBuffer = actualList.get(0);

        assertThat(actualBuffer.getWagonList().size(), is(2));
    }
    @Test
    public void Bufferzone236P1NotAddWagon(){
        //Arrange
        DataService dataService= new DataService();

        ArrayList<String> formerGln = new ArrayList<String>();
        ArrayList<String> containedIn = new ArrayList<String>();


        bufferZones= new ArrayList<BufferZone>();
        BufferZone bufferzone236= new BufferZone("Bufferområde 236", "urn:epc:id:sgln:57980102.8548.0", "latitude", "longitude", formerGln, "locationName", containedIn, wagonList);
        bufferZones.add(bufferzone236);


        trackEventArrayList= new ArrayList<TrackEvent>();
        TrackEvent trackEventRecent= new TrackEvent("localityKey", "S30", "comments", "distanceFloor", "distanceMtr", "durationSec", "eventTime",  "floor",  "latitude",  "longitude",  "urn:epc:id:sgln:57900101.0006.0",false);
        trackEventArrayList.add(trackEventRecent);


        //Act
        dataService.setBufferZones(bufferZones);
        dataService.setTrackEventArrayList(trackEventArrayList);

        dataService.wagonInBufferzones();


        //Assert
        ArrayList<BufferZone> actualList = dataService.getBufferZoneList();
        BufferZone actualBuffer = actualList.get(0);

        assertNull(actualBuffer.getWagonList());
    }





}