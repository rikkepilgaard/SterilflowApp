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
import android.text.Html;
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
    private boolean isRunning = false;
    private DataService dataService;

    private SharedPreferences sharedPreferences;
    private SharedPreferences.Editor preferenceEditor;


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

                        //Used to check if time is updated
                        String lastDiff = sharedPreferences.getString(event.getObjectkey(),"0");


                        //Calculate time passed since trolley was placed
                        Date currentDate = Calendar.getInstance().getTime();
                        Date otherDate = fromStringToDate(event.getEventTime());

                        long diff = currentDate.getTime() - otherDate.getTime();
                        long diffMinutesLong = diff / (60 * 1000) % 60;
                        int diffMinutes = safeLongToInt(diffMinutesLong);
                        int diffHours = safeLongToInt(diff / (60 * 60 * 1000) % 24);
                        int diffDays = safeLongToInt(diff / (24 * 60 * 60 * 1000));
                        String diffMinutesText = (diffMinutes < 10 ? "0" : "") + diffMinutes;
                        String diffHoursText = (diffHours < 10 ? "0" : "") + diffHours;
                        String diffDaysText = (diffDays < 10 ? "0" : "") + diffDays;

                        //Make presentable text for showing the time passed since trolley was placed
                        String text;
                        if(diffDays==0) {
                            text = diffHoursText+ "t " + diffMinutesText+"m";
                        }else{
                            text = diffDaysText + "d " + diffHoursText + "t "
                                    + diffMinutesText + "m";
                        }

                        //Check whether time has updated
                        if(!lastDiff.equals(text)) {
                            preferenceEditor = sharedPreferences.edit();
                            preferenceEditor.putString(event.getObjectkey(),text);
                            preferenceEditor.commit();
                        }

                        //If trolley have been in bufferzone 3 hours or more, the trolley is "expired"
                        //Unless bufferzone contains only sterile trolleys.

                        if(diffHours>2) {

                            if(zone.getGln().equals(BUFFER_NORDLAGER) || zone.getGln().equals(BUFFER_STERILCENTRAL)){
                                preferenceEditor = sharedPreferences.edit();
                                preferenceEditor.putBoolean(event.getObjectkey() + "bool", false);
                                preferenceEditor.commit();
                            } else {
                                preferenceEditor = sharedPreferences.edit();
                                preferenceEditor.putBoolean(event.getObjectkey() + "bool", true);
                                preferenceEditor.commit();
                            }

                            zone.setContainsExpiredWagon(true);
                            if (zone.getGln().equals(BUFFER_NORDLAGER) || zone.getGln().equals(BUFFER_STERILCENTRAL)) {
                                zone.setContainsExpiredWagon(false);
                            }
                        } else {
                            preferenceEditor = sharedPreferences.edit();
                            preferenceEditor.putBoolean(event.getObjectkey() + "bool", false);
                            preferenceEditor.commit();
                        }

                        //Send broadcast to update time on UI every minute.
                        sendBroadcast(ACTION_TIME,null);


                        //When trolley have been in bufferzone for 3 hours, broadcast is sent to
                        //MainActivity and notification is given.
                        if (diffHours == 3 && diffMinutes==0) {
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

    //https://stackoverflow.com/questions/1590831/safely-casting-long-to-int-in-java
    //Convert long to int
    private static int safeLongToInt(long l) {
        if (l < Integer.MIN_VALUE || l > Integer.MAX_VALUE) {
            throw new IllegalArgumentException
                    (""+ l);
        }
        return (int) l;
    }
    

    //Convert string to Date
    private Date fromStringToDate(String stringDate){
        if(stringDate==null) {
            return null;
        } else {
            SimpleDateFormat simpledateformat = new SimpleDateFormat("dd-MM-yyyy HH:mm",Locale.getDefault());
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

            //As long as service is running, calculateTimeDifference() is called every 60 sec
            while(isRunning) {
                try {
                    calculateTimeDifference();
                    Thread.sleep(60000);
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
