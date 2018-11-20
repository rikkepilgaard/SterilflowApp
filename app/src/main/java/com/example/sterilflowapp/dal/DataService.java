package com.example.sterilflowapp.dal;

import android.app.Service;
import android.content.Intent;
import android.os.Binder;
import android.os.IBinder;
import android.util.Log;

import com.example.sterilflowapp.model.BufferZone;
import com.example.sterilflowapp.model.Building;
import com.example.sterilflowapp.model.TrackEvent;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class DataService extends Service {

    private static final String TAG = "DataService";

    private FirebaseDatabase database;
    private DatabaseReference reference;

    private ArrayList<TrackEvent> trackEventArrayList;
    private ArrayList<BufferZone> bufferZones;
    private ArrayList<Building> buildingsList;
    private ParseBufferzoneXML parseBufferzones;
    private ParseBuildingXML parseBuildings;
    private BufferzoneDataProcessor dataProcessor;


    public DataService() {
    }

    @Override
    public IBinder onBind(Intent intent) {
        Log.d(TAG,"onBind");
        return binder;
    }

    public class DataServiceBinder extends Binder {
        public DataService getService() {
            return DataService.this;
        }
    }

    private IBinder binder = new DataServiceBinder();

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        dataProcessor=new BufferzoneDataProcessor(this);
        database = FirebaseDatabase.getInstance();
        reference = database.getReference().child("TrackEvents");
        Log.d(TAG,"DataService started");
        parseBufferzones=new ParseBufferzoneXML();
        parseBuildings=new ParseBuildingXML();
        buildingsList=parseBuildings.parseBuildingsXML(this);
        bufferZones=parseBufferzones.parseBufferzoneXML(this);

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

                dataProcessor.trolleyInBufferzones(trackEventArrayList,bufferZones);

            }

            @Override
            public void onCancelled(DatabaseError error) {
                // Failed to read value
                Log.w(TAG, "Failed to read value.", error.toException());
            }
        });

        return START_STICKY;
    }

    public ArrayList<Building> getBuildingsList(){return buildingsList;}

    public ArrayList<BufferZone> getBufferZoneList(){
        return bufferZones;
    }

}
