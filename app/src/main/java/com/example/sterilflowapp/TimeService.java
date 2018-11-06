package com.example.sterilflowapp;

import android.app.Service;
import android.content.BroadcastReceiver;
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
import android.widget.TextView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

import androidx.localbroadcastmanager.content.LocalBroadcastManager;

public class TimeService extends Service {

    private static final String TAG = "TimeService";

    private boolean serviceBound = false;
    private ArrayList<BufferZone> bufferZones;
    SharedPreferences sharedPreferences;
    SharedPreferences.Editor preferenceEditor;
    private boolean isRunning = false;

    private DataService dataService;

    public TimeService() {
    }


    public class TimeServiceBinder extends Binder {
        TimeService getService() {
            return TimeService.this;
        }
    }

    private IBinder binder = new TimeService.TimeServiceBinder();

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {

        bindToService();
        Log.d(TAG,"DataService started");

        sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);
        preferenceEditor = sharedPreferences.edit();

        isRunning = true;

        TimePassedTask timePassedTask = new TimePassedTask();
        timePassedTask.execute();

        return START_STICKY;
    }

    @Override
    public void onDestroy() {
        Log.d(TAG,"Service destroyed");
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

    private void unbindServices(){
        if(serviceBound){
            unbindService(dataConnection);
        }
    }


    private ServiceConnection dataConnection = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            DataService.DataServiceBinder dataBinder = (DataService.DataServiceBinder) service;
            dataService = dataBinder.getService();
            serviceBound = true;
            bufferZones = dataService.getBufferZoneList();
            Log.d(TAG,"Connected to DataService");
        }

        @Override
        public void onServiceDisconnected(ComponentName name) {
            serviceBound = false;
        }
    };


    private void timeMethod() {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss");

        if(dataService!=null) {
            bufferZones = dataService.getBufferZoneList();
        }

        if(bufferZones != null) {
            for (BufferZone zone : bufferZones) {
                if(zone.getWagonList()!=null) {
                    for (TrackEvent event : zone.getWagonList()) {

                        long lastDiff = sharedPreferences.getLong(event.getObjectkey(), 0);
                        long lastMinutes = lastDiff / (60 * 1000) % 60;
                        long lastHours = lastDiff / (60 * 60 * 1000) % 24;

                        Date currentDate = Calendar.getInstance().getTime();
                        String format = simpleDateFormat.format(currentDate);
                        currentDate = fromStringToDate(format);

                        Date otherDate = fromStringToDate(event.getEventTime());

                        long diff = currentDate.getTime() - otherDate.getTime();

                        long diffMinutes = diff / (60 * 1000) % 60;
                        long diffHours = diff / (60 * 60 * 1000) % 24;
                        //long diffDays = diff / (24 * 60 * 60 * 1000);

                        if (lastMinutes != diffMinutes) {
                            preferenceEditor.putLong(event.getObjectkey(), diff);
                            preferenceEditor.commit();

                            sendBroadcast("time");
                        }

                        if (diffHours == 3 && lastHours!=diffHours) {
                            preferenceEditor.putString(getResources().getString(R.string.wagon_time), event.getObjectkey());
                            preferenceEditor.putString(getResources().getString(R.string.buffer_time),zone.getName());
                            preferenceEditor.commit();

                            sendBroadcast("time_wagon");
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
            //ParsePosition pos = new ParsePosition(0);
            SimpleDateFormat simpledateformat = new SimpleDateFormat("dd-MM-yyyy hh:mm:ss");
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
                    timeMethod();
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
            return null;
        }

    }



    public void sendBroadcast(String action){
        Intent broadcastIntent = new Intent();
        if(action.equals("time_wagon")) {
            broadcastIntent.setAction("time_wagon");
        }
        if(action.equals("time")){
            broadcastIntent.setAction("time");
        }
        LocalBroadcastManager.getInstance(TimeService.this).sendBroadcast(broadcastIntent);
        Log.d(TAG,"Broadcast sent");
    }


}
