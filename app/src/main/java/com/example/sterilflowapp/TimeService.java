package com.example.sterilflowapp;

import android.app.Service;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.AsyncTask;
import android.os.Binder;
import android.os.IBinder;
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

        return START_STICKY;
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




    private void timeMethod(String date) {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss");

        //https://www.mkyong.com/java/how-to-calculate-date-time-difference-in-java/

        Date currentDate = Calendar.getInstance().getTime();
        String format = simpleDateFormat.format(currentDate);
        currentDate = fromStringToDate(format);

        Date otherDate = fromStringToDate(date);

        long diff = currentDate.getTime() - otherDate.getTime();



        //long diffSeconds = diff / 1000;
        long diffMinutes = diff / (60 * 1000) % 60;
        long diffHours = diff / (60 * 60 * 1000) % 24;
        long diffDays = diff / (24 * 60 * 60 * 1000);

        String diffMinutesText = (diffMinutes < 10 ? "0" : "") + diffMinutes;
        String diffHoursText = (diffHours < 10 ? "0" : "") + diffHours;
        String diffDaysText = (diffDays < 10 ? "0" : "") + diffDays;

        String text = diffHoursText + "<b>t </b>" + diffMinutesText + "<b>m </b>";

//        if(diffDays != 0)
//        {
//            textView.setText(diffDaysText + ":" + diffHoursText + ":" + diffMinutesText);
//        }
//        else textView.setText(Html.fromHtml(text));
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

    public class timePassedTask extends AsyncTask<String,String,String>{

        @Override
        protected String doInBackground(String... params) {

            try {
                timeMethod(params[0]);
                Thread.sleep(100);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            return null;
        }

    }

    public void sendBroadcast(){
        Intent broadcastIntent = new Intent();
        broadcastIntent.setAction("time");
        LocalBroadcastManager.getInstance(TimeService.this).sendBroadcast(broadcastIntent);
        Log.d(TAG,"Broadcast sent");
    }


}
