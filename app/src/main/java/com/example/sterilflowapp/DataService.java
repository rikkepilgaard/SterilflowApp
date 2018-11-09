package com.example.sterilflowapp;

import android.app.Service;
import android.content.Intent;
import android.os.Binder;
import android.os.IBinder;
import android.provider.ContactsContract;
import android.util.Log;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;
import org.xmlpull.v1.XmlPullParserFactory;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;

import androidx.localbroadcastmanager.content.LocalBroadcastManager;

public class DataService extends Service {

    private static final String TAG = "DataService";

    private FirebaseDatabase database;
    private DatabaseReference reference;

    private ArrayList<TrackEvent> trackEventArrayList;
    private ArrayList<BufferZone> bufferZones;


    public DataService() {
    }

    @Override
    public IBinder onBind(Intent intent) {
        Log.d(TAG,"onBind");
        return binder;
    }

    public class DataServiceBinder extends Binder {
        DataService getService() {
            return DataService.this;
        }
    }

    private IBinder binder = new DataServiceBinder();

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        database = FirebaseDatabase.getInstance();
        reference = database.getReference().child("TrackEvents");

        Log.d(TAG,"DataService started");

        reference.addValueEventListener(new ValueEventListener() {

            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                // This method is called once with the initial value and again
                // whenever data at this location is updated.

                trackEventArrayList = new ArrayList<>();

                for(DataSnapshot ds : dataSnapshot.getChildren()){
                    for (DataSnapshot ds1 : ds.getChildren()) {
                        TrackEvent trackEvent = ds1.getValue(TrackEvent.class);
                        trackEvent.setExpired(false);
                        trackEventArrayList.add(trackEvent);

                    }
                }

                wagonInBufferzones();

            }

            @Override
            public void onCancelled(DatabaseError error) {
                // Failed to read value
                Log.w(TAG, "Failed to read value.", error.toException());
            }
        });

        parseXML();

        return START_STICKY;
    }


    private ArrayList<TrackEvent> newestEvents(ArrayList<TrackEvent> trackList){
        String lastValue = "";
        String currentValue = "";
        ArrayList<TrackEvent> newList = new ArrayList<>();

        if(trackList != null) {
            for (int i = trackList.size() - 1; i >= 0; i--) {
                TrackEvent event = trackList.get(i);

                if (event.getObjectkey() != null) {
                    currentValue = event.getObjectkey();
                }
                if (!currentValue.equals(lastValue)) {
                    newList.add(event);
                    lastValue = currentValue;
                }
            }
        }
        return newList;
    }

    public void wagonInBufferzones(){

        ArrayList<TrackEvent> arrayList = newestEvents(trackEventArrayList);


        for (int j = 0; j < bufferZones.size(); j++){
            ArrayList<TrackEvent> oldWagonList=bufferZones.get(j).getWagonList();
            bufferZones.get(j).setWagonList(null);
            ArrayList<TrackEvent> list = new ArrayList<>();

            switch (bufferZones.get(j).getGln()) {
                case "urn:epc:id:sgln:57980101.8705.0": //buffer 202
                case "urn:epc:id:sgln:57980101.5946.0": //buffer nordlager

                    for (int i = 0; i < arrayList.size() ; i++){
                        TrackEvent trackEvent = arrayList.get(i);
                        if (bufferZones.get(j).getGln().equals(trackEvent.getLocationSgln())) {
                            for (int h = 0; h < trackEventArrayList.size(); h++) {
                                TrackEvent event = trackEventArrayList.get(h);
                                if (event.getObjectkey().equals(trackEvent.getObjectkey())
                                        && event.getEventTime().equals(trackEvent.getEventTime())) {
                                    TrackEvent event1 = trackEventArrayList.get(h-1);
                                    for (String formerGln: bufferZones.get(j).getFormerGln() ) {
                                        if(event1.getLocationSgln().equals(formerGln)){
                                            list.add(trackEvent);
                                            bufferZones.get(j).setWagonList(list);
                                        }
                                    }
                                }
                            }
                        }
                    }

                    break;

                case "urn:epc:id:sgln:57980101.7856.0": //buffer ren steril
                case "urn:epc:id:sgln:57980102.6407.0": //buffer 109
                case "urn:epc:id:sgln:57980102.6410.0": //buffer 120
                case "urn:epc:id:sgln:57980101.3660.0": //buffer 232
                case "urn:epc:id:sgln:57980102.8548.0": //buffer 236

                    for (int i = 0; i < arrayList.size() ; i++) {
                        TrackEvent trackEvent = arrayList.get(i);
                        if (bufferZones.get(j).getGln().equals(trackEvent.getLocationSgln())) {
                            list.add(trackEvent);
                            bufferZones.get(j).setWagonList(list);
                        }
                    }

                    break;
            }
            int newSize=0;
            if(bufferZones.get(j).getWagonList()!= null){newSize=bufferZones.get(j).getWagonList().size();}

            int oldSize = 0;
            if(oldWagonList != null){oldSize=oldWagonList.size();}

                if(newSize!=oldSize){
                    sendBroadcast();}
        }
    }


    public ArrayList<BufferZone> getBufferZoneList(){
        return bufferZones;
    }


    private void parseXML() {
        //https://www.youtube.com/watch?v=-deKKeEdpbw
        XmlPullParserFactory parserFactory;
        try {
            parserFactory = XmlPullParserFactory.newInstance();
            XmlPullParser parser = parserFactory.newPullParser();
            InputStream is = getAssets().open("buffer.xml");
            parser.setFeature(XmlPullParser.FEATURE_PROCESS_NAMESPACES,false);
            parser.setInput(is,null);

            processParsing(parser);

        } catch (XmlPullParserException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void processParsing(XmlPullParser parser) throws IOException,XmlPullParserException {
        //https://www.youtube.com/watch?v=-deKKeEdpbw
        bufferZones = new ArrayList<>();
        int eventType = parser.getEventType();
        BufferZone currentBuffer = null;
        ArrayList<String> formerGln = new ArrayList<>();

        while (eventType!=XmlPullParser.END_DOCUMENT){
            String zone = null;
            //ArrayList<String> formerGln;

            switch (eventType){
                case XmlPullParser.START_TAG:
                    zone = parser.getName();


                    if("bufferzone".equals(zone)){
                        currentBuffer = new BufferZone();
                        bufferZones.add(currentBuffer);
                        formerGln = new ArrayList<>();
                    } else if (currentBuffer != null){
                        if ("name".equals(zone)){
                            currentBuffer.setName(parser.nextText());
                        } else if("gln".equals(zone)){
                            currentBuffer.setGln(parser.nextText());
                        } else if("formerGln".equals(zone)) {
                            formerGln.add(parser.nextText());
                        } else if ("latitude".equals(zone)){
                            currentBuffer.setLatitude(parser.nextText());
                        } else if ("longitude".equals(zone)){
                            currentBuffer.setLongitude(parser.nextText());
                        } else if("location".equals(zone)){
                            currentBuffer.setLocationName(parser.nextText());
                        }

                        currentBuffer.setFormerGln(formerGln);
                    }
                    break;

            }
            eventType = parser.next();

        }
    }
    //To test
    public void setBufferZones(ArrayList<BufferZone>buffer){this.bufferZones=buffer;}
    public void setTrackEventArrayList(ArrayList<TrackEvent>tracks){this.trackEventArrayList=tracks;}

    public void sendBroadcast(){
        Intent broadcastIntent = new Intent();
        broadcastIntent.setAction("data");
        LocalBroadcastManager.getInstance(DataService.this).sendBroadcast(broadcastIntent);
        Log.d(TAG,"Broadcast sent");
    }

}
