package com.example.sterilflowapp.presenter;

import android.content.Context;
import android.content.Intent;
import android.util.Log;

import com.example.sterilflowapp.model.BufferZone;
import com.example.sterilflowapp.model.TrackEvent;

import java.util.ArrayList;

import androidx.localbroadcastmanager.content.LocalBroadcastManager;

import static com.example.sterilflowapp.ConstantValues.ACTION_DATA;
import static com.example.sterilflowapp.ConstantValues.ACTION_NULL;
import static com.example.sterilflowapp.ConstantValues.BUFFER_105;
import static com.example.sterilflowapp.ConstantValues.BUFFER_109;
import static com.example.sterilflowapp.ConstantValues.BUFFER_120;
import static com.example.sterilflowapp.ConstantValues.BUFFER_202;
import static com.example.sterilflowapp.ConstantValues.BUFFER_232;
import static com.example.sterilflowapp.ConstantValues.BUFFER_236;
import static com.example.sterilflowapp.ConstantValues.BUFFER_NORDLAGER;
import static com.example.sterilflowapp.ConstantValues.BUFFER_STERILCENTRAL;

public class BufferzoneDataProcessor {

    private static final String TAG = "BufferzoneDataProcessor";

    private ArrayList<TrackEvent> trackEventArrayList;
    private ArrayList<BufferZone> bufferZonesList;
    private Context context;

    public BufferzoneDataProcessor(Context context){

        this.context = context.getApplicationContext();
    }

    //Constructor to test
    public BufferzoneDataProcessor(){}


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

    public void trolleyInBufferzones(ArrayList<TrackEvent> trackEventArrayList, ArrayList<BufferZone> bufferZones){

        ArrayList<TrackEvent> arrayList = newestEvents(trackEventArrayList);

        if(bufferZones!= null) {
            for (int j = 0; j < bufferZones.size(); j++) {
                ArrayList<TrackEvent> oldTrolleyList = bufferZones.get(j).getTrolleyList();
                bufferZones.get(j).setTrolleyList(null);
                ArrayList<TrackEvent> list = new ArrayList<>();

                switch (bufferZones.get(j).getGln()) {
                    case BUFFER_202:
                    case BUFFER_NORDLAGER:

                        for (int i = 0; i < arrayList.size(); i++) {
                            TrackEvent trackEvent = arrayList.get(i);
                            if (bufferZones.get(j).getGln().equals(trackEvent.getLocationSgln())) {
                                for (int h = 0; h < trackEventArrayList.size(); h++) {
                                    TrackEvent event = trackEventArrayList.get(h);
                                    if (event.getObjectkey().equals(trackEvent.getObjectkey())
                                            && event.getEventTime().equals(trackEvent.getEventTime())) {
                                        TrackEvent event1 = trackEventArrayList.get(h - 1);
                                        for (String formerGln : bufferZones.get(j).getFormerGln()) {
                                            if (event1.getLocationSgln().equals(formerGln)) {
                                                list.add(trackEvent);
                                                bufferZones.get(j).setTrolleyList(list);
                                            }
                                        }
                                    }
                                }
                            }
                        }

                        break;
                    case BUFFER_105:
                        for (int i = 0; i < arrayList.size(); i++) {
                            TrackEvent trackEvent = arrayList.get(i);
                            if (bufferZones.get(j).getGln().equals(trackEvent.getLocationSgln())) {
                                for (int h = 0; h < trackEventArrayList.size(); h++) {
                                    TrackEvent event = trackEventArrayList.get(h);
                                    if (event.getObjectkey().equals(trackEvent.getObjectkey())
                                            && event.getEventTime().equals(trackEvent.getEventTime())) {
                                        TrackEvent event1 = trackEventArrayList.get(h - 1);
                                        if (Integer.valueOf(event1.getFloor()) != 4) {
                                            list.add(trackEvent);
                                            bufferZones.get(j).setTrolleyList(list);
                                        }
                                    }
                                }
                            }
                        }

                        break;

                    case BUFFER_STERILCENTRAL:
                    case BUFFER_109:
                    case BUFFER_120:
                    case BUFFER_232:
                    case BUFFER_236:

                        for (int i = 0; i < arrayList.size(); i++) {
                            TrackEvent trackEvent = arrayList.get(i);
                            if (bufferZones.get(j).getGln().equals(trackEvent.getLocationSgln())) {
                                list.add(trackEvent);
                                bufferZones.get(j).setTrolleyList(list);
                            }
                        }

                        break;
                }
                int newSize = 0;
                if (bufferZones.get(j).getTrolleyList() != null) {
                    newSize = bufferZones.get(j).getTrolleyList().size();
                }

                int oldSize = 0;
                if (oldTrolleyList != null) {
                    oldSize = oldTrolleyList.size();
                }

                //Check whether status is changed (trolley moved in or out of bufferzone)
                if (newSize != oldSize) {
                   sendBroadcast(ACTION_DATA);
                }
            }
        }
        if(this.trackEventArrayList==null){
            sendBroadcast(ACTION_NULL);
        }
        this.trackEventArrayList = trackEventArrayList;
        bufferZonesList=bufferZones;
    }

    public ArrayList<BufferZone> getBufferZones(){
        return bufferZonesList;
    }

    private void sendBroadcast(String action){
        Intent broadcastIntent = new Intent();
        broadcastIntent.setAction(action);
        LocalBroadcastManager.getInstance(context).sendBroadcast(broadcastIntent);
        Log.d(TAG,"Broadcast sent");
    }

}
