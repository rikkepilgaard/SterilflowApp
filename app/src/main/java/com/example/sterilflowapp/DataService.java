package com.example.sterilflowapp;

import android.app.Service;
import android.content.Intent;
import android.os.Binder;
import android.os.IBinder;
import android.provider.ContactsContract;
import android.util.Log;
import android.widget.ProgressBar;

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

import static com.example.sterilflowapp.ConstantValues.BUFFER_105;
import static com.example.sterilflowapp.ConstantValues.BUFFER_109;
import static com.example.sterilflowapp.ConstantValues.BUFFER_120;
import static com.example.sterilflowapp.ConstantValues.BUFFER_202;
import static com.example.sterilflowapp.ConstantValues.BUFFER_232;
import static com.example.sterilflowapp.ConstantValues.BUFFER_236;
import static com.example.sterilflowapp.ConstantValues.BUFFER_NORDLAGER;
import static com.example.sterilflowapp.ConstantValues.BUFFER_STERILCENTRAL;

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
        DataService getService() {
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

                dataProcessor.wagonInBufferzones(trackEventArrayList,bufferZones);

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
