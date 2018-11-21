package com.example.sterilflowapp.presenter;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.Service;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Binder;
import android.os.Build;
import android.os.IBinder;
import android.preference.PreferenceManager;
import android.util.Log;

import com.example.sterilflowapp.R;
import com.example.sterilflowapp.dal.DataService;
import com.example.sterilflowapp.model.BufferZone;
import com.example.sterilflowapp.model.TrackEvent;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

import androidx.core.app.NotificationCompat;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;

import static com.example.sterilflowapp.ConstantValues.BUFFER_NORDLAGER;
import static com.example.sterilflowapp.ConstantValues.BUFFER_STERILCENTRAL;

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
                            preferenceEditor.putLong(event.getObjectkey(), diff);
                            preferenceEditor.commit();

                            sendBroadcast("time");
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
                            if(!zone.getGln().equals(BUFFER_NORDLAGER) || !zone.getGln().equals(BUFFER_STERILCENTRAL)) {

                                preferenceEditor.putString(getResources().getString(R.string.buffer_time), zone.getName());
                                preferenceEditor.commit();

                                showNotification(zone.getName());

                                sendBroadcast("time_wagon");

                            }
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
            SimpleDateFormat simpledateformat = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss");
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

    public void showNotification(String buffer){

        NotificationManager notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            String channelId = "channel_1";
            CharSequence channelName = "My_Channel";
            int importance = NotificationManager.IMPORTANCE_DEFAULT;

            NotificationChannel channel = new NotificationChannel(channelId,channelName,importance);
            channel.canShowBadge();
            if (notificationManager != null) {
                notificationManager.createNotificationChannel(channel);
            }
        }

        NotificationCompat.Builder notificationBuilder = new NotificationCompat.Builder(this,"channel_1")
                .setSmallIcon(R.drawable.ic_noti_icon)
                .setContentTitle(getString(R.string.alert_time_title))
                .setContentText(getString(R.string.wagon_time) + "\n" + buffer);


        if (notificationManager != null) {
            notificationManager.notify(1,notificationBuilder.build());
        }

    }

    public void sendBroadcast(String action){
        Intent broadcastIntent = new Intent();
        broadcastIntent.setAction(action);

        LocalBroadcastManager.getInstance(TimeService.this).sendBroadcast(broadcastIntent);
        Log.d(TAG,"Broadcast sent");
    }


}
