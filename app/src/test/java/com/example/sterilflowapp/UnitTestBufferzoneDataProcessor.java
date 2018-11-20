package com.example.sterilflowapp;

import com.example.sterilflowapp.dal.BufferzoneDataProcessor;
import com.example.sterilflowapp.model.BufferZone;
import com.example.sterilflowapp.model.TrackEvent;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.*;

public class UnitTestBufferzoneDataProcessor {

    private ArrayList<TrackEvent> trackEventArrayList;
    private ArrayList<BufferZone> bufferZones;

    private ArrayList<TrackEvent> wagonList;
    BufferzoneDataProcessor bufferProcessor;

    @Before
    public void setUp() throws Exception {
        bufferProcessor= new BufferzoneDataProcessor();
        trackEventArrayList=new ArrayList<>();
        bufferZones=new ArrayList<>();
        wagonList=new ArrayList<>();


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
        ArrayList<String> formerGln = new ArrayList<>();
        formerGln.add("urn:epc:id:sgln:57980102.2859.0");
        ArrayList<String> containedIn = new ArrayList<>();


        bufferZones= new ArrayList<>();
        BufferZone bufferzone202= new BufferZone("Bufferområde 202", "urn:epc:id:sgln:57980101.8705.0", "latitude", "longitude", formerGln, "locationName", containedIn, wagonList,false);
        bufferZones.add(bufferzone202);


        trackEventArrayList= new ArrayList<>();
        TrackEvent trackEventRecent1= new TrackEvent("localityKey", "S30", "comments", "distanceFloor", "distanceMtr", "durationSec", "06:03",  "floor",  "latitude",  "longitude",  "urn:epc:id:sgln:57980102.2859.0",false);
        TrackEvent trackEventRecent2= new TrackEvent("localityKey", "S30", "comments", "distanceFloor", "distanceMtr", "durationSec", "08:03",  "floor",  "latitude",  "longitude",  "urn:epc:id:sgln:57980101.8705.0",false);
        trackEventArrayList.add(trackEventRecent1);
        trackEventArrayList.add(trackEventRecent2);

        ArrayList<TrackEvent> expectedList = new ArrayList<>();
        expectedList.add(trackEventRecent2);

        //Act
        bufferProcessor.trolleyInBufferzones(trackEventArrayList,bufferZones);

        //Assert
        ArrayList<BufferZone> actualList = bufferProcessor.getBufferZones();
        BufferZone actualBuffer = actualList.get(0);

        assertThat(actualBuffer.getTrolleyList(), is(expectedList));
    }
    @Test
    public void Bufferzone202NotAddWagon(){
        //Arrange
        BufferzoneDataProcessor bufferProcessor= new BufferzoneDataProcessor();

        ArrayList<String> formerGln = new ArrayList<>();
        formerGln.add("urn:epc:id:sgln:57980102.2859.0");
        ArrayList<String> containedIn = new ArrayList<>();


        bufferZones= new ArrayList<>();
        BufferZone bufferzone202= new BufferZone("Bufferområde 202", "urn:epc:id:sgln:57980101.8705.0", "latitude", "longitude", formerGln, "locationName", containedIn, wagonList, false);
        bufferZones.add(bufferzone202);

        trackEventArrayList= new ArrayList<>();
        TrackEvent trackEventRecent1= new TrackEvent("localityKey", "S30", "comments", "distanceFloor", "distanceMtr", "durationSec", "06:03",  "floor",  "latitude",  "longitude",  "urn:epc:id:sgln:57980102.2839.0",false);
        TrackEvent trackEventRecent2= new TrackEvent("localityKey", "S30", "comments", "distanceFloor", "distanceMtr", "durationSec", "08:03",  "floor",  "latitude",  "longitude",  "urn:epc:id:sgln:57980101.8705.0",false);
        trackEventArrayList.add(trackEventRecent1);
        trackEventArrayList.add(trackEventRecent2);


        //Act
        bufferProcessor.trolleyInBufferzones(trackEventArrayList,bufferZones);

        //Assert
        ArrayList<BufferZone> actualList = bufferProcessor.getBufferZones();
        BufferZone actualBuffer = actualList.get(0);

        assertNull(actualBuffer.getTrolleyList());
    }
    @Test
    public void Bufferzone202AddTwoWagons(){
        //Arrange
        ArrayList<String> formerGln = new ArrayList<>();
        formerGln.add("urn:epc:id:sgln:57980102.2859.0");
        ArrayList<String> containedIn = new ArrayList<>();

        bufferZones= new ArrayList<>();
        BufferZone bufferzone202= new BufferZone("Bufferområde 202", "urn:epc:id:sgln:57980101.8705.0", "latitude", "longitude", formerGln, "locationName", containedIn, wagonList,false);
        bufferZones.add(bufferzone202);


        trackEventArrayList= new ArrayList<>();
        TrackEvent trackEventS30track1= new TrackEvent("localityKey", "S30", "comments", "distanceFloor", "distanceMtr", "durationSec", "06:03",  "floor",  "latitude",  "longitude",  "urn:epc:id:sgln:57980102.2859.0",false);
        TrackEvent trackEventS30track2= new TrackEvent("localityKey", "S30", "comments", "distanceFloor", "distanceMtr", "durationSec", "08:03",  "floor",  "latitude",  "longitude",  "urn:epc:id:sgln:57980101.8705.0",false);
        TrackEvent trackEventS20track1= new TrackEvent("localityKey", "S20", "comments", "distanceFloor", "distanceMtr", "durationSec", "06:03",  "floor",  "latitude",  "longitude",  "urn:epc:id:sgln:57980102.2859.0",false);
        TrackEvent trackEventS20track2= new TrackEvent("localityKey", "S20", "comments", "distanceFloor", "distanceMtr", "durationSec", "08:03",  "floor",  "latitude",  "longitude",  "urn:epc:id:sgln:57980101.8705.0",false);
        trackEventArrayList.add(trackEventS30track1);
        trackEventArrayList.add(trackEventS30track2);
        trackEventArrayList.add(trackEventS20track1);
        trackEventArrayList.add(trackEventS20track2);

        //Act
        bufferProcessor.trolleyInBufferzones(trackEventArrayList,bufferZones);

        //Assert
        ArrayList<BufferZone> actualList = bufferProcessor.getBufferZones();
        BufferZone actualBuffer = actualList.get(0);

        assertThat(actualBuffer.getTrolleyList().size(),is(2));
    }

    @Test
    public void BufferzoneSterilcentralAddOneWagon(){
        //Arrange
        ArrayList<String> formerGln = new ArrayList<>();
        ArrayList<String> containedIn = new ArrayList<>();


        bufferZones= new ArrayList<>();
        BufferZone bufferzoneSteril= new BufferZone("Bufferområde SterilCentral", "urn:epc:id:sgln:57980101.7856.0", "latitude", "longitude", formerGln, "locationName", containedIn, wagonList,false);
        bufferZones.add(bufferzoneSteril);


        trackEventArrayList= new ArrayList<>();
        TrackEvent trackEventRecent= new TrackEvent("localityKey", "S30", "comments", "distanceFloor", "distanceMtr", "durationSec", "eventTime",  "floor",  "latitude",  "longitude",  "urn:epc:id:sgln:57980101.7856.0",false);
        trackEventArrayList.add(trackEventRecent);

        //Act
        bufferProcessor.trolleyInBufferzones(trackEventArrayList,bufferZones);


        //Assert
        ArrayList<BufferZone> actualList = bufferProcessor.getBufferZones();
        BufferZone actualBuffer = actualList.get(0);

        assertThat(actualBuffer.getTrolleyList(), is(trackEventArrayList));
    }
    @Test
    public void BufferzoneSterilcentralAddTwoWagons(){
        //Arrange
        ArrayList<String> formerGln = new ArrayList<>();
        ArrayList<String> containedIn = new ArrayList<>();


        bufferZones= new ArrayList<>();
        BufferZone bufferzoneSteril= new BufferZone("Bufferområde SterilCentral", "urn:epc:id:sgln:57980101.7856.0", "latitude", "longitude", formerGln, "locationName", containedIn, wagonList,false);
        bufferZones.add(bufferzoneSteril);


        trackEventArrayList= new ArrayList<>();
        TrackEvent trackEventS30track1= new TrackEvent("localityKey", "S30", "comments", "distanceFloor", "distanceMtr", "durationSec", "06:03",  "floor",  "latitude",  "longitude",  "urn:epc:id:sgln:57980102.2859.0",false);
        TrackEvent trackEventS30track2= new TrackEvent("localityKey", "S30", "comments", "distanceFloor", "distanceMtr", "durationSec", "08:03",  "floor",  "latitude",  "longitude",  "urn:epc:id:sgln:57980101.7856.0",false);
        TrackEvent trackEventS20track1= new TrackEvent("localityKey", "S20", "comments", "distanceFloor", "distanceMtr", "durationSec", "06:03",  "floor",  "latitude",  "longitude",  "urn:epc:id:sgln:57980102.2859.0",false);
        TrackEvent trackEventS20track2= new TrackEvent("localityKey", "S20", "comments", "distanceFloor", "distanceMtr", "durationSec", "08:03",  "floor",  "latitude",  "longitude",  "urn:epc:id:sgln:57980101.7856.0",false);
        trackEventArrayList.add(trackEventS30track1);
        trackEventArrayList.add(trackEventS30track2);
        trackEventArrayList.add(trackEventS20track1);
        trackEventArrayList.add(trackEventS20track2);

        //Act
        bufferProcessor.trolleyInBufferzones(trackEventArrayList,bufferZones);


        //Assert
        ArrayList<BufferZone> actualList = bufferProcessor.getBufferZones();
        BufferZone actualBuffer = actualList.get(0);

        assertThat(actualBuffer.getTrolleyList().size(), is(2));
    }
    @Test
    public void BufferzoneSteilcentralNotAddWagon(){
        //Arrange
        ArrayList<String> formerGln = new ArrayList<>();
        ArrayList<String> containedIn = new ArrayList<>();


        bufferZones= new ArrayList<>();
        BufferZone bufferzoneSteril= new BufferZone("Bufferområde SterilCentral", "urn:epc:id:sgln:57980101.7856.0", "latitude", "longitude", formerGln, "locationName", containedIn, wagonList,false);
        bufferZones.add(bufferzoneSteril);


        trackEventArrayList= new ArrayList<>();
        TrackEvent trackEventRecent= new TrackEvent("localityKey", "S30", "comments", "distanceFloor", "distanceMtr", "durationSec", "eventTime",  "floor",  "latitude",  "longitude",  "urn:epc:id:sgln:57900101.7856.0",false);
        trackEventArrayList.add(trackEventRecent);

        //Act
        bufferProcessor.trolleyInBufferzones(trackEventArrayList,bufferZones);

        //Assert
        ArrayList<BufferZone> actualList = bufferProcessor.getBufferZones();
        BufferZone actualBuffer = actualList.get(0);

        assertNull(actualBuffer.getTrolleyList());
    }

    @Test
    public void Bufferzone109AddOneWagon(){
        //Arrange
        ArrayList<String> formerGln = new ArrayList<>();
        ArrayList<String> containedIn = new ArrayList<>();


        bufferZones= new ArrayList<>();
        BufferZone bufferzone109= new BufferZone("Bufferområde 109", "urn:epc:id:sgln:57980102.6407.0", "latitude", "longitude", formerGln, "locationName", containedIn, wagonList,false);
        bufferZones.add(bufferzone109);


        trackEventArrayList= new ArrayList<>();
        TrackEvent trackEventRecent= new TrackEvent("localityKey", "S30", "comments", "distanceFloor", "distanceMtr", "durationSec", "eventTime",  "floor",  "latitude",  "longitude",  "urn:epc:id:sgln:57980102.6407.0",false);
        trackEventArrayList.add(trackEventRecent);

        //Act
        bufferProcessor.trolleyInBufferzones(trackEventArrayList,bufferZones);

        //Assert
        ArrayList<BufferZone> actualList = bufferProcessor.getBufferZones();
        BufferZone actualBuffer = actualList.get(0);

        assertThat(actualBuffer.getTrolleyList(), is(trackEventArrayList));
    }
    @Test
    public void Bufferzone109AddTwoWagons(){
        //Arrange
        ArrayList<String> formerGln = new ArrayList<>();
        ArrayList<String> containedIn = new ArrayList<>();


        bufferZones= new ArrayList<>();
        BufferZone bufferzone109= new BufferZone("Bufferområde SterilCentral", "urn:epc:id:sgln:57980102.6407.0", "latitude", "longitude", formerGln, "locationName", containedIn, wagonList,false);
        bufferZones.add(bufferzone109);


        trackEventArrayList= new ArrayList<>();
        TrackEvent trackEventS30track1= new TrackEvent("localityKey", "S30", "comments", "distanceFloor", "distanceMtr", "durationSec", "06:03",  "floor",  "latitude",  "longitude",  "urn:epc:id:sgln:57980102.2859.0",false);
        TrackEvent trackEventS30track2= new TrackEvent("localityKey", "S30", "comments", "distanceFloor", "distanceMtr", "durationSec", "08:03",  "floor",  "latitude",  "longitude",  "urn:epc:id:sgln:57980102.6407.0",false);
        TrackEvent trackEventS20track1= new TrackEvent("localityKey", "S20", "comments", "distanceFloor", "distanceMtr", "durationSec", "06:03",  "floor",  "latitude",  "longitude",  "urn:epc:id:sgln:57980102.2859.0",false);
        TrackEvent trackEventS20track2= new TrackEvent("localityKey", "S20", "comments", "distanceFloor", "distanceMtr", "durationSec", "08:03",  "floor",  "latitude",  "longitude",  "urn:epc:id:sgln:57980102.6407.0",false);
        trackEventArrayList.add(trackEventS30track1);
        trackEventArrayList.add(trackEventS30track2);
        trackEventArrayList.add(trackEventS20track1);
        trackEventArrayList.add(trackEventS20track2);

        //Act
        bufferProcessor.trolleyInBufferzones(trackEventArrayList,bufferZones);

        //Assert
        ArrayList<BufferZone> actualList = bufferProcessor.getBufferZones();
        BufferZone actualBuffer = actualList.get(0);

        assertThat(actualBuffer.getTrolleyList().size(), is(2));
    }
    @Test
    public void Bufferzone109NotAddWagon(){
        //Arrange
        ArrayList<String> formerGln = new ArrayList<>();
        ArrayList<String> containedIn = new ArrayList<>();


        bufferZones= new ArrayList<>();
        BufferZone bufferzone109= new BufferZone("Bufferområde 109", "urn:epc:id:sgln:57980102.6407.0", "latitude", "longitude", formerGln, "locationName", containedIn, wagonList,false);
        bufferZones.add(bufferzone109);


        trackEventArrayList= new ArrayList<>();
        TrackEvent trackEventRecent= new TrackEvent("localityKey", "S30", "comments", "distanceFloor", "distanceMtr", "durationSec", "eventTime",  "floor",  "latitude",  "longitude",  "urn:epc:id:sgln:57900101.7856.0",false);
        trackEventArrayList.add(trackEventRecent);


        //Act
        bufferProcessor.trolleyInBufferzones(trackEventArrayList,bufferZones);


        //Assert
        ArrayList<BufferZone> actualList = bufferProcessor.getBufferZones();
        BufferZone actualBuffer = actualList.get(0);

        assertNull(actualBuffer.getTrolleyList());
    }

    @Test
    public void Bufferzone120AddOneWagon() {
        //Arrange
        ArrayList<String> formerGln = new ArrayList<>();
        formerGln.add("urn:epc:id:sgln:57980102.2859.0");
        ArrayList<String> containedIn = new ArrayList<>();


        bufferZones= new ArrayList<>();
        BufferZone bufferzone120= new BufferZone("Bufferområde 120", "urn:epc:id:sgln:57980102.6410.0", "latitude", "longitude", formerGln, "locationName", containedIn, wagonList,false);
        bufferZones.add(bufferzone120);


        trackEventArrayList= new ArrayList<>();
        TrackEvent trackEventRecent= new TrackEvent("localityKey", "S30", "comments", "distanceFloor", "distanceMtr", "durationSec", "eventTime",  "floor",  "latitude",  "longitude",  "urn:epc:id:sgln:57980102.6410.0",false);
        trackEventArrayList.add(trackEventRecent);

        //Act
        bufferProcessor.trolleyInBufferzones(trackEventArrayList,bufferZones);

        //Assert
        ArrayList<BufferZone> actualList = bufferProcessor.getBufferZones();
        BufferZone actualBuffer = actualList.get(0);

        assertThat(actualBuffer.getTrolleyList(), is(trackEventArrayList));

    }
    @Test
    public void Bufferzone120NotAddWagon(){
        //Arrange
        ArrayList<String> formerGln = new ArrayList<>();
        formerGln.add("urn:epc:id:sgln:57980102.2859.0");
        ArrayList<String> containedIn = new ArrayList<>();


        bufferZones= new ArrayList<>();
        BufferZone bufferzone120= new BufferZone("Bufferområde 120", "urn:epc:id:sgln:57980102.6410.0", "latitude", "longitude", formerGln, "locationName", containedIn, wagonList,false);
        bufferZones.add(bufferzone120);


        trackEventArrayList= new ArrayList<>();
        TrackEvent trackEventRecent= new TrackEvent("localityKey", "S30", "comments", "distanceFloor", "distanceMtr", "durationSec", "eventTime",  "floor",  "latitude",  "longitude",  "urn:epc:id:sgln:57980902.6410.0",false);
        trackEventArrayList.add(trackEventRecent);


        //Act
        bufferProcessor.trolleyInBufferzones(trackEventArrayList,bufferZones);



        //Assert
        ArrayList<BufferZone> actualList = bufferProcessor.getBufferZones();
        BufferZone actualBuffer = actualList.get(0);

        assertNull(actualBuffer.getTrolleyList());


    }

    @Test
    public void BufferzoneNordAddOneWagon(){
        //Arrange
        ArrayList<String> formerGln = new ArrayList<>();
        formerGln.add("urn:epc:id:sgln:57980102.1414.0");
        ArrayList<String> containedIn = new ArrayList<>();


        bufferZones= new ArrayList<>();
        BufferZone bufferzoneNord= new BufferZone("Bufferområde Nord", "urn:epc:id:sgln:57980101.3329.0", "latitude", "longitude", formerGln, "locationName", containedIn, wagonList,false);
        bufferZones.add(bufferzoneNord);


        trackEventArrayList= new ArrayList<>();
        TrackEvent trackEventRecent1= new TrackEvent("localityKey", "S30", "comments", "distanceFloor", "distanceMtr", "durationSec", "06:03",  "floor",  "latitude",  "longitude",  "urn:epc:id:sgln:57980102.1414.0",false);
        TrackEvent trackEventRecent2= new TrackEvent("localityKey", "S30", "comments", "distanceFloor", "distanceMtr", "durationSec", "08:03",  "floor",  "latitude",  "longitude",  "urn:epc:id:sgln:57980101.3329.0",false);
        trackEventArrayList.add(trackEventRecent1);
        trackEventArrayList.add(trackEventRecent2);

        ArrayList<TrackEvent> expectedList = new ArrayList<>();
        expectedList.add(trackEventRecent2);

        //Act
        bufferProcessor.trolleyInBufferzones(trackEventArrayList,bufferZones);



        //Assert
        ArrayList<BufferZone> actualList = bufferProcessor.getBufferZones();
        BufferZone actualBuffer = actualList.get(0);

        assertThat(actualBuffer.getTrolleyList(), is(expectedList));
    }
    @Test
    public void BufferzoneNordAddTwoWagons(){
        //Arrange
        ArrayList<String> formerGln = new ArrayList<>();
        formerGln.add("urn:epc:id:sgln:57980102.1414.0");
        ArrayList<String> containedIn = new ArrayList<>();


        bufferZones= new ArrayList<>();
        BufferZone bufferzoneNord= new BufferZone("Bufferområde Nord", "urn:epc:id:sgln:57980101.3329.0", "latitude", "longitude", formerGln, "locationName", containedIn, wagonList,false);
        bufferZones.add(bufferzoneNord);


        trackEventArrayList= new ArrayList<>();
        TrackEvent trackEventS30track1= new TrackEvent("localityKey", "S30", "comments", "distanceFloor", "distanceMtr", "durationSec", "06:03",  "floor",  "latitude",  "longitude",  "urn:epc:id:sgln:57980102.1414.0",false);
        TrackEvent trackEventS30track2= new TrackEvent("localityKey", "S30", "comments", "distanceFloor", "distanceMtr", "durationSec", "08:03",  "floor",  "latitude",  "longitude",  "urn:epc:id:sgln:57980101.3329.0",false);
        TrackEvent trackEventS20track1= new TrackEvent("localityKey", "S20", "comments", "distanceFloor", "distanceMtr", "durationSec", "06:03",  "floor",  "latitude",  "longitude",  "urn:epc:id:sgln:57980102.1414.0",false);
        TrackEvent trackEventS20track2= new TrackEvent("localityKey", "S20", "comments", "distanceFloor", "distanceMtr", "durationSec", "08:03",  "floor",  "latitude",  "longitude",  "urn:epc:id:sgln:57980101.3329.0",false);
        trackEventArrayList.add(trackEventS30track1);
        trackEventArrayList.add(trackEventS30track2);
        trackEventArrayList.add(trackEventS20track1);
        trackEventArrayList.add(trackEventS20track2);


        //Act
        bufferProcessor.trolleyInBufferzones(trackEventArrayList,bufferZones);



        //Assert
        ArrayList<BufferZone> actualList = bufferProcessor.getBufferZones();
        BufferZone actualBuffer = actualList.get(0);

        assertThat(actualBuffer.getTrolleyList().size(),is(2));
    }
    @Test
    public void BufferzoneNordNotAddWagon(){
        //Arrange
        ArrayList<String> formerGln = new ArrayList<>();
        formerGln.add("urn:epc:id:sgln:57980102.1414.0");
        ArrayList<String> containedIn = new ArrayList<>();


        bufferZones= new ArrayList<>();
        BufferZone bufferzoneNord= new BufferZone("Bufferområde Nord", "urn:epc:id:sgln:57980101.5946.0", "latitude", "longitude", formerGln, "locationName", containedIn, wagonList,false);
        bufferZones.add(bufferzoneNord);


        trackEventArrayList= new ArrayList<>();
        TrackEvent trackEventRecent1= new TrackEvent("localityKey", "S30", "comments", "distanceFloor", "distanceMtr", "durationSec", "06:03",  "floor",  "latitude",  "longitude",  "urn:epc:id:sgln:57980102.2839.0",false);
        TrackEvent trackEventRecent2= new TrackEvent("localityKey", "S30", "comments", "distanceFloor", "distanceMtr", "durationSec", "08:03",  "floor",  "latitude",  "longitude",  "urn:epc:id:sgln:57980101.5946.0",false);
        trackEventArrayList.add(trackEventRecent1);
        trackEventArrayList.add(trackEventRecent2);

        //Act
        bufferProcessor.trolleyInBufferzones(trackEventArrayList,bufferZones);

        //Assert
        ArrayList<BufferZone> actualList = bufferProcessor.getBufferZones();
        BufferZone actualBuffer = actualList.get(0);

        assertNull(actualBuffer.getTrolleyList());
    }

    @Test
    public void Bufferzone232P1AddOneWagon(){
        //Arrange
        BufferzoneDataProcessor bufferProcessor= new BufferzoneDataProcessor();

        ArrayList<String> formerGln = new ArrayList<>();
        ArrayList<String> containedIn = new ArrayList<>();


        bufferZones= new ArrayList<>();
        BufferZone bufferzone232= new BufferZone("Bufferområde 232", "urn:epc:id:sgln:57980101.3660.0", "latitude", "longitude", formerGln, "locationName", containedIn, wagonList,false);
        bufferZones.add(bufferzone232);


        trackEventArrayList= new ArrayList<>();
        TrackEvent trackEventRecent= new TrackEvent("localityKey", "S30", "comments", "distanceFloor", "distanceMtr", "durationSec", "eventTime",  "floor",  "latitude",  "longitude",  "urn:epc:id:sgln:57980101.3660.0",false);
        trackEventArrayList.add(trackEventRecent);


        //Act
        bufferProcessor.trolleyInBufferzones(trackEventArrayList,bufferZones);


        //Assert
        ArrayList<BufferZone> actualList = bufferProcessor.getBufferZones();
        BufferZone actualBuffer = actualList.get(0);

        assertThat(actualBuffer.getTrolleyList(), is(trackEventArrayList));
    }
    @Test
    public void Bufferzone232P1AddTwoWagons(){
        //Arrange
        BufferzoneDataProcessor bufferProcessor= new BufferzoneDataProcessor();

        ArrayList<String> formerGln = new ArrayList<>();
        ArrayList<String> containedIn = new ArrayList<>();


        bufferZones= new ArrayList<>();
        BufferZone bufferzone232= new BufferZone("Bufferområde 232-P1", "urn:epc:id:sgln:57980101.3660.0", "latitude", "longitude", formerGln, "locationName", containedIn, wagonList,false);
        bufferZones.add(bufferzone232);


        trackEventArrayList= new ArrayList<>();
        TrackEvent trackEventS30track1= new TrackEvent("localityKey", "S30", "comments", "distanceFloor", "distanceMtr", "durationSec", "06:03",  "floor",  "latitude",  "longitude",  "urn:epc:id:sgln:57980102.2859.0",false);
        TrackEvent trackEventS30track2= new TrackEvent("localityKey", "S30", "comments", "distanceFloor", "distanceMtr", "durationSec", "08:03",  "floor",  "latitude",  "longitude",  "urn:epc:id:sgln:57980101.3660.0",false);
        TrackEvent trackEventS20track1= new TrackEvent("localityKey", "S20", "comments", "distanceFloor", "distanceMtr", "durationSec", "06:03",  "floor",  "latitude",  "longitude",  "urn:epc:id:sgln:57980102.2859.0",false);
        TrackEvent trackEventS20track2= new TrackEvent("localityKey", "S20", "comments", "distanceFloor", "distanceMtr", "durationSec", "08:03",  "floor",  "latitude",  "longitude",  "urn:epc:id:sgln:57980101.3660.0",false);
        trackEventArrayList.add(trackEventS30track1);
        trackEventArrayList.add(trackEventS30track2);
        trackEventArrayList.add(trackEventS20track1);
        trackEventArrayList.add(trackEventS20track2);


        //Act
        bufferProcessor.trolleyInBufferzones(trackEventArrayList,bufferZones);


        //Assert
        ArrayList<BufferZone> actualList = bufferProcessor.getBufferZones();
        BufferZone actualBuffer = actualList.get(0);

        assertThat(actualBuffer.getTrolleyList().size(), is(2));
    }
    @Test
    public void Bufferzone232P1NotAddWagon(){
        //Arrange
        BufferzoneDataProcessor bufferProcessor= new BufferzoneDataProcessor();

        ArrayList<String> formerGln = new ArrayList<>();
        ArrayList<String> containedIn = new ArrayList<>();


        bufferZones= new ArrayList<>();
        BufferZone bufferzone232= new BufferZone("Bufferområde 232-P1", "urn:epc:id:sgln:57980101.3660.0", "latitude", "longitude", formerGln, "locationName", containedIn, wagonList,false);
        bufferZones.add(bufferzone232);


        trackEventArrayList= new ArrayList<>();
        TrackEvent trackEventRecent= new TrackEvent("localityKey", "S30", "comments", "distanceFloor", "distanceMtr", "durationSec", "eventTime",  "floor",  "latitude",  "longitude",  "urn:epc:id:sgln:57900101.0006.0",false);
        trackEventArrayList.add(trackEventRecent);


        //Act
        bufferProcessor.trolleyInBufferzones(trackEventArrayList,bufferZones);


        //Assert
        ArrayList<BufferZone> actualList = bufferProcessor.getBufferZones();
        BufferZone actualBuffer = actualList.get(0);

        assertNull(actualBuffer.getTrolleyList());
    }

    @Test
    public void Bufferzone236P1AddOneWagon(){

        //Arrange
        ArrayList<String> formerGln = new ArrayList<>();
        ArrayList<String> containedIn = new ArrayList<>();


        bufferZones= new ArrayList<>();
        BufferZone bufferzone236= new BufferZone("Bufferområde 236", "urn:epc:id:sgln:57980102.8548.0", "latitude", "longitude", formerGln, "locationName", containedIn, wagonList,false);
        bufferZones.add(bufferzone236);


        trackEventArrayList= new ArrayList<>();
        TrackEvent trackEventRecent= new TrackEvent("localityKey", "S30", "comments", "distanceFloor", "distanceMtr", "durationSec", "eventTime",  "floor",  "latitude",  "longitude",  "urn:epc:id:sgln:57980102.8548.0",false);
        trackEventArrayList.add(trackEventRecent);


        //Act
        bufferProcessor.trolleyInBufferzones(trackEventArrayList,bufferZones);


        //Assert
        ArrayList<BufferZone> actualList = bufferProcessor.getBufferZones();
        BufferZone actualBuffer = actualList.get(0);

        assertThat(actualBuffer.getTrolleyList(), is(trackEventArrayList));
    }
    @Test
    public void Bufferzone236P1AddTwoWagons(){
        //Arrange
        ArrayList<String> formerGln = new ArrayList<>();
        ArrayList<String> containedIn = new ArrayList<>();


        bufferZones= new ArrayList<>();
        BufferZone bufferzone236= new BufferZone("Bufferområde 236", "urn:epc:id:sgln:57980102.8548.0", "latitude", "longitude", formerGln, "locationName", containedIn, wagonList,false);
        bufferZones.add(bufferzone236);


        trackEventArrayList= new ArrayList<>();
        TrackEvent trackEventS30track1= new TrackEvent("localityKey", "S30", "comments", "distanceFloor", "distanceMtr", "durationSec", "06:03",  "floor",  "latitude",  "longitude",  "urn:epc:id:sgln:57980102.2859.0",false);
        TrackEvent trackEventS30track2= new TrackEvent("localityKey", "S30", "comments", "distanceFloor", "distanceMtr", "durationSec", "08:03",  "floor",  "latitude",  "longitude",  "urn:epc:id:sgln:57980102.8548.0",false);
        TrackEvent trackEventS20track1= new TrackEvent("localityKey", "S20", "comments", "distanceFloor", "distanceMtr", "durationSec", "06:03",  "floor",  "latitude",  "longitude",  "urn:epc:id:sgln:57980102.2859.0",false);
        TrackEvent trackEventS20track2= new TrackEvent("localityKey", "S20", "comments", "distanceFloor", "distanceMtr", "durationSec", "08:03",  "floor",  "latitude",  "longitude",  "urn:epc:id:sgln:57980102.8548.0",false);
        trackEventArrayList.add(trackEventS30track1);
        trackEventArrayList.add(trackEventS30track2);
        trackEventArrayList.add(trackEventS20track1);
        trackEventArrayList.add(trackEventS20track2);


        //Act
        bufferProcessor.trolleyInBufferzones(trackEventArrayList,bufferZones);


        //Assert
        ArrayList<BufferZone> actualList = bufferProcessor.getBufferZones();
        BufferZone actualBuffer = actualList.get(0);

        assertThat(actualBuffer.getTrolleyList().size(), is(2));
    }
    @Test
    public void Bufferzone236P1NotAddWagon(){
        //Arrange
        ArrayList<String> formerGln = new ArrayList<>();
        ArrayList<String> containedIn = new ArrayList<>();


        bufferZones= new ArrayList<>();
        BufferZone bufferzone236= new BufferZone("Bufferområde 236", "urn:epc:id:sgln:57980102.8548.0", "latitude", "longitude", formerGln, "locationName", containedIn, wagonList,false);
        bufferZones.add(bufferzone236);


        trackEventArrayList= new ArrayList<>();
        TrackEvent trackEventRecent= new TrackEvent("localityKey", "S30", "comments", "distanceFloor", "distanceMtr", "durationSec", "eventTime",  "floor",  "latitude",  "longitude",  "urn:epc:id:sgln:57900101.0006.0",false);
        trackEventArrayList.add(trackEventRecent);


        //Act
        bufferProcessor.trolleyInBufferzones(trackEventArrayList,bufferZones);


        //Assert
        ArrayList<BufferZone> actualList = bufferProcessor.getBufferZones();
        BufferZone actualBuffer = actualList.get(0);

        assertNull(actualBuffer.getTrolleyList());
    }
}