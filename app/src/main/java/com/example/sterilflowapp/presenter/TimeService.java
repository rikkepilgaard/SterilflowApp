package com.example.sterilflowapp.presenter;

import android.app.Service;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Binder;
import android.os.IBinder;
import android.preference.PreferenceManager;
import android.util.Log;

import com.example.sterilflowapp.interactor.DataService;
import com.example.sterilflowapp.model.BufferZone;
import com.example.sterilflowapp.model.TrackEvent;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

import androidx.localbroadcastmanager.content.LocalBroadcastManager;

import static com.example.sterilflowapp.ConstantValues.ACTION_TIME;
import static com.example.sterilflowapp.ConstantValues.ACTION_TIME_EXCEEDED;
import static com.example.sterilflowapp.ConstantValues.BUFFER_NORDLAGER;
import static com.example.sterilflowapp.ConstantValues.BUFFER_STERILCENTRAL;
import static com.example.sterilflowapp.ConstantValues.EXTRA_BUFFERZONE;

public class TimeService extends Service {

    private static final String TAG = "TimeService";

    private ArrayList<BufferZone> bufferZones;
    SharedPreferences sharedPreferences;
    SharedPreferences.Editor preferenceEditor;
    private boolean isRunning = false;

    private DataService dataService;

    public TimeService() {
    }


    public class TimeServiceBinder extends Binder {
        public TimeService getService() {
            return TimeService.this;
        }
    }

    private IBinder binder = new TimeService.TimeServiceBinder();

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {

        bindToService();
        Log.d(TAG,"DataService started");

        sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);

        isRunning = true;

        TimePassedTask timePassedTask = new TimePassedTask();
        timePassedTask.execute();

        return START_STICKY;
    }

    @Override
    public void onDestroy() {
        isRunning = false;
        super.onDestroy();
    }

    @Override
    public IBinder onBind(Intent intent) {
        return binder;
    }

    private void bindToService(){
        Intent dataIntent = new Intent(TimeService.this,DataService.class);
        bindService(dataIntent,dataConnection,Context.BIND_AUTO_CREATE);
    }

    private ServiceConnection dataConnection = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            DataService.DataServiceBinder dataBinder = (DataService.DataServiceBinder) service;
            dataService = dataBinder.getService();
            bufferZones = dataService.getBufferZoneList();
            Log.d(TAG,"Connected to DataService");
        }

        @Override
        public void onServiceDisconnected(ComponentName name) {

        }
    };


    public void calculateTimeDifference() {

        if(dataService!=null) {
            bufferZones = dataService.getBufferZoneList();
        }

        if(bufferZones != null) {
            for (BufferZone zone : bufferZones) {
                zone.setContainsExpiredWagon(false);
                if(zone.getTrolleyList()!=null) {
                    for (TrackEvent event : zone.getTrolleyList()) {

                        long lastDiff = sharedPreferences.getLong(event.getObjectkey(), 0);
                        long lastMinutes = lastDiff / (60 * 1000) % 60;
                        long lastHours = lastDiff / (60 * 60 * 1000) % 24;

                        Date currentDate = Calendar.getInstance().getTime();
                        Date otherDate = fromStringToDate(event.getEventTime());

                        //Wrong time zone in trolley eventtime

//                        Calendar calender = Calendar.getInstance();
//                        calender.setTime(otherDate);
//                        calender.add(calender.HOUR,1);

                        //otherDate = calender.getTime();

                        long diff = currentDate.getTime() - otherDate.getTime();

                        long diffMinutes = diff / (60 * 1000) % 60;
                        long diffHours = diff / (60 * 60 * 1000) % 24;

                        if (lastMinutes != diffMinutes) {
                            preferenceEditor = sharedPreferences.edit();
                            preferenceEditor.putLong(event.getObjectkey(), diff);
                            preferenceEditor.commit();

                            sendBroadcast(ACTION_TIME,null);
                        }

                        //If trolley have been in bufferzone 3 hours or more, the trolley is "expired"
                        //Unless bufferzone contains only sterile trolleys.
                        if(diffHours>2){
                            event.setExpired(true);
                            zone.setContainsExpiredWagon(true);
                            if(zone.getGln().equals(BUFFER_NORDLAGER) || zone.getGln().equals(BUFFER_STERILCENTRAL)) {
                                event.setExpired(false);
                                zone.setContainsExpiredWagon(false);
                            }

                        }

                        //When trolley have been in bufferzone for 3 hours, broadcast is sent to
                        //MainActivity and notification is given.
                        if (diffHours == 3 && lastHours!=diffHours) {
                            if(zone.getGln().equals(BUFFER_NORDLAGER)
                                    || zone.getGln().equals(BUFFER_STERILCENTRAL)) {

                                return;

                            }

                            sendBroadcast(ACTION_TIME_EXCEEDED,zone.getName());
                        }
                    }
                }
            }
        }
    }
    

    private Date fromStringToDate(String stringDate){
        //Vær opmærksom på formatet af den String dato, der kommer med metoden.
        if(stringDate==null) {
            return null;
        } else {
            SimpleDateFormat simpledateformat = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss",Locale.getDefault());
            Date dateDate = null;
            try {
                dateDate = simpledateformat.parse(stringDate);
            } catch (ParseException e) {
                e.printStackTrace();
            }
            return dateDate;
        }
    }

    public class TimePassedTask extends AsyncTask<String,String,String>{

        @Override
        protected String doInBackground(String... params) {

            while(isRunning) {
                try {
                    calculateTimeDifference();
                    Thread.sleep(30000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
            return null;
        }

    }

    public void sendBroadcast(String action, String buffername){
        Intent broadcastIntent = new Intent();
        broadcastIntent.setAction(action);
        broadcastIntent.putExtra(EXTRA_BUFFERZONE,buffername);
        LocalBroadcastManager.getInstance(TimeService.this).sendBroadcast(broadcastIntent);
        Log.d(TAG,"Broadcast sent");
    }


}
