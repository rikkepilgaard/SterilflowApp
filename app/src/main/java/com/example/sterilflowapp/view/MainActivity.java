package com.example.sterilflowapp.view;

import com.example.sterilflowapp.R;
import com.example.sterilflowapp.interactor.DataService;
import com.example.sterilflowapp.model.BufferZone;
import com.example.sterilflowapp.presenter.CustomViewPager;
import com.example.sterilflowapp.presenter.InternetConnectionTask;
import com.example.sterilflowapp.presenter.TimeService;
import com.example.sterilflowapp.presenter.ViewPagerAdapter;
import com.google.android.material.tabs.TabLayout;
import androidx.appcompat.app.AlertDialog;
import androidx.core.app.ActivityCompat;
import androidx.core.app.NotificationCompat;
import androidx.fragment.app.FragmentManager;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;
import androidx.viewpager.widget.ViewPager;
import androidx.appcompat.app.AppCompatActivity;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.ServiceConnection;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.Bundle;
import android.os.IBinder;
import android.util.Log;
import android.view.View;
import android.widget.ProgressBar;

import java.util.ArrayList;

import static com.example.sterilflowapp.ConstantValues.*;
import static com.example.sterilflowapp.ConstantValues.PERMISSION_ALL;


public class MainActivity extends AppCompatActivity {

    private static final String TAG = "MainActivity";
    private ProgressBar progressbar;
    private DataService dataService;
    private TimeService timeService;

    private boolean dataServiceBound = false;
    private boolean timeServiceBound = false;

    private TableFragment tableFragment;
    private MapFragment mapFragment;

    private CustomViewPager viewPager;

    private FragmentManager fragmentManager;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //https://stackoverflow.com/questions/34342816/android-6-0-multiple-permissions
        if(!hasPermissions(this, PERMISSIONS)){
            ActivityCompat.requestPermissions(this, PERMISSIONS, PERMISSION_ALL);
        }
        InternetConnectionTask itTask = new InternetConnectionTask(this);
        itTask.execute();

        fragmentManager = getSupportFragmentManager();

        if(savedInstanceState!=null){
            tableFragment = (TableFragment)fragmentManager
                    .getFragment(savedInstanceState,FRAGMENT_ONE_TAG);
            mapFragment = (MapFragment)fragmentManager
                    .getFragment(savedInstanceState,FRAGMENT_TWO_TAG);
        } else {
            tableFragment = new TableFragment();
            mapFragment = new MapFragment();
        }

        TabLayout tabLayout = findViewById(R.id.tabs);
        viewPager = findViewById(R.id.viewPager);

        setupViewPager(viewPager);
        tabLayout.setupWithViewPager(viewPager);

        viewPager.setPagingEnabled(false);


        //Set tab icon with drawable icons in white
        Drawable listIcon = getResources().getDrawable(R.drawable.ic_icon_a_24);
        Drawable mapIcon = getResources().getDrawable(R.drawable.ic_icon_b_24);
        listIcon.setColorFilter(Color.WHITE, PorterDuff.Mode.SRC_ATOP);
        mapIcon.setColorFilter(Color.WHITE, PorterDuff.Mode.SRC_ATOP);
        tabLayout.getTabAt(0).setIcon(listIcon);
        tabLayout.getTabAt(1).setIcon(mapIcon);

        progressbar=findViewById(R.id.progressbar);
        progressbar.setVisibility(View.VISIBLE);
        progressbar.bringToFront();


        tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                if(mapFragment !=null) {
                    mapFragment.closeInfoWindows();
                }
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });
    }


    @Override
    protected void onResume() {
        super.onResume();
        IntentFilter dataFilter = new IntentFilter();
        dataFilter.addAction(ACTION_DATA);
        dataFilter.addAction(ACTION_TIME);
        dataFilter.addAction(ACTION_TIME_EXCEEDED);
        dataFilter.addAction(ACTION_CHANGE_TAB);
        dataFilter.addAction(ACTION_NULL);
        LocalBroadcastManager.getInstance(this).registerReceiver(broadcastReceiver,dataFilter);
    }

    @Override
    protected void onStart() {
        super.onStart();
        startServices();
        bindToServices();
    }

    @Override
    protected void onStop() {
        super.onStop();
        LocalBroadcastManager.getInstance(this).unregisterReceiver(broadcastReceiver);
        unbindServices();
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);

        //Save fragment states in case of orientation change
        fragmentManager.putFragment(outState,FRAGMENT_ONE_TAG, tableFragment);
        fragmentManager.putFragment(outState,FRAGMENT_TWO_TAG, mapFragment);
    }


    //https://stackoverflow.com/questions/34342816/android-6-0-multiple-permissions
    public static boolean hasPermissions(Context context, String... permissions) {
        if (context != null && permissions != null) {
            for (String permission : permissions) {
                if (ActivityCompat.checkSelfPermission(context, permission)
                        != PackageManager.PERMISSION_GRANTED) {
                    return false;
                }
            }
        }
        return true;
    }


    //Handles broadcasts according to actions
    private BroadcastReceiver broadcastReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            Log.d(TAG,"Broadcast received: " + intent.getAction());

            if(intent.getAction()!=null){
            switch (intent.getAction()){
                case ACTION_TIME:
                    if(tableFragment !=null) {
                        tableFragment.initData(dataService.getBufferZoneList());
                    }
                    break;

                case ACTION_TIME_EXCEEDED:

                    //Broadcasts are received from TimeService, when a trolley has exceeded the
                    //time limit (reached 3 hours)
                    setAlert(intent.getStringExtra(EXTRA_BUFFERZONE));
                    showNotification(intent.getStringExtra(EXTRA_BUFFERZONE));
                    break;

                case ACTION_DATA:
                    timeService.calculateTimeDifference();
                    if(tableFragment != null) {
                        tableFragment.initData(dataService.getBufferZoneList());
                    }
                    if(mapFragment != null) {
                        mapFragment.addMarker(dataService.getBufferZoneList());
                        mapFragment.setBuildingList(dataService.getBuildingsList());
                    }
                    break;

                case ACTION_CHANGE_TAB:
                    if(viewPager.getCurrentItem()==1){

                    viewPager.setCurrentItem(0);
                    tableFragment.expandSpecifiedGroup(dataService.getBufferZoneList(),intent.getStringExtra(EXTRA_BUFFERZONE));

                    } else{
                        viewPager.setCurrentItem(1);
                        mapFragment.zoomToSpecificBufferzone(intent.getStringExtra(EXTRA_BUFFERZONE));
                    }
                    break;

                case ACTION_NULL:
                    progressbar.setVisibility(View.INVISIBLE);

            }}


        }
    };


    public void setAlert(final String buffer){
        final AlertDialog.Builder alert = new AlertDialog.Builder(this,R.style.AlertTheme);
        alert.setTitle(getString(R.string.alert_time_title));
        alert.setMessage(getString(R.string.wagon_time) + "\n" + buffer);

        alert.setPositiveButton(getString(R.string.See_trolleys), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

                //Go to map overview and expand bufferzone with "expired" trolley(s).
                viewPager.setCurrentItem(0);
                tableFragment.expandSpecifiedGroup(dataService.getBufferZoneList(),buffer);

            }
        });
        alert.setNegativeButton(getString(R.string.close), null);
        alert.show();

    }
    public void showNotification(String buffer){
        //https://developer.android.com/training/notify-user/build-notification

        NotificationManager notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {

            NotificationChannel channel = new NotificationChannel(NOT_CHANNEL_ID,
                    NOT_CHANNEL_NAME,NotificationManager.IMPORTANCE_DEFAULT);

            channel.canShowBadge();
            if (notificationManager != null) {
                notificationManager.createNotificationChannel(channel);
            }
        }

        NotificationCompat.Builder notificationBuilder = new NotificationCompat.Builder(this,NOT_CHANNEL_ID)
                .setSmallIcon(R.drawable.ic_noti_icon)
                .setContentTitle(getString(R.string.alert_time_title))
                .setContentText(getString(R.string.wagon_time) + "\n" + buffer);


        if (notificationManager != null) {
            notificationManager.notify(1,notificationBuilder.build());
        }

    }


    private void startServices(){
        Intent dataIntent = new Intent(MainActivity.this,DataService.class);
        startService(dataIntent);
        Intent timeIntent = new Intent(MainActivity.this,TimeService.class);
        startService(timeIntent);
    }

    private void bindToServices(){
        Intent dataIntent = new Intent(MainActivity.this,DataService.class);
        bindService(dataIntent,dataConnection,Context.BIND_AUTO_CREATE);
        Intent timeIntent = new Intent(MainActivity.this,TimeService.class);
        bindService(timeIntent,timeConnection,Context.BIND_AUTO_CREATE);
    }

    private void unbindServices(){
        if(dataServiceBound){
            unbindService(dataConnection);
        }
        if(timeServiceBound){
            unbindService(timeConnection);
        }
    }

    private ServiceConnection timeConnection = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            TimeService.TimeServiceBinder timeBinder = (TimeService.TimeServiceBinder) service;
            timeService = timeBinder.getService();
            timeServiceBound = true;
            Log.d(TAG,"Connected to TimeService");
        }

        @Override
        public void onServiceDisconnected(ComponentName name) {
            timeServiceBound = false;
        }
    };

    private ServiceConnection dataConnection = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            DataService.DataServiceBinder dataBinder = (DataService.DataServiceBinder) service;
            dataService = dataBinder.getService();
            dataServiceBound = true;
            ArrayList<BufferZone> bufferZones = dataService.getBufferZoneList();
            if(tableFragment.isAdded()) {
                tableFragment.initData(bufferZones);

            }
            if(mapFragment.isAdded()){
                mapFragment.addMarker(bufferZones);
                mapFragment.setBuildingList(dataService.getBuildingsList());
            }
            Log.d(TAG,"Connected to DataService");
        }

        @Override
        public void onServiceDisconnected(ComponentName name) {
            dataServiceBound = false;
        }
    };

    private void setupViewPager(ViewPager viewPager) {
        ViewPagerAdapter viewPagerAdapter = new ViewPagerAdapter(getSupportFragmentManager());
        viewPagerAdapter.addFragment(tableFragment,getResources().getString(R.string.tab_a_label));
        viewPagerAdapter.addFragment(mapFragment,getResources().getString(R.string.tab_b_label));
        viewPager.setAdapter(viewPagerAdapter);
    }
}
