package com.example.sterilflowapp;

import com.google.android.material.tabs.TabLayout;

import androidx.appcompat.app.AlertDialog;
import androidx.core.app.ActivityCompat;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;
import androidx.viewpager.widget.ViewPager;
import androidx.appcompat.app.AppCompatActivity;

import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.ServiceConnection;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.os.IBinder;
import android.preference.PreferenceManager;
import android.util.Log;
import android.widget.Toast;
import android.Manifest;

import java.util.ArrayList;


public class MainActivity extends AppCompatActivity {

    private static final String TAG = "MainActivity";

    private DataService dataService;
    private TimeService timeService;

    private boolean dataServiceBound = false;
    private boolean timeServiceBound = false;

    private FragmentOne fragmentOne;
    private FragmentTwo fragmentTwo;

    private SharedPreferences sharedPreferences;
    private TabLayout tabLayout;
    private CustomViewPager viewPager;
    private ArrayList<BufferZone> bufferZones;
    //private ArrayList<TrackEvent> trackEventArrayList;

    //private FirebaseDatabase database;
    //private DatabaseReference reference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //https://stackoverflow.com/questions/34342816/android-6-0-multiple-permissions
        int PERMISSION_ALL = 1;
        String[] PERMISSIONS = {
                Manifest.permission.ACCESS_FINE_LOCATION,
                Manifest.permission.ACCESS_COARSE_LOCATION,
                Manifest.permission.READ_EXTERNAL_STORAGE,
                Manifest.permission.WRITE_EXTERNAL_STORAGE,
                Manifest.permission.INTERNET,
                Manifest.permission.ACCESS_WIFI_STATE,
                Manifest.permission.ACCESS_NETWORK_STATE
        };

        if(!hasPermissions(this, PERMISSIONS)){
            ActivityCompat.requestPermissions(this, PERMISSIONS, PERMISSION_ALL);
        }

        sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);

        tabLayout = findViewById(R.id.tabs);
        viewPager = findViewById(R.id.viewPager);

        setupViewPager(viewPager);
        tabLayout.setupWithViewPager(viewPager);

        viewPager.setPagingEnabled(false);

        tabLayout.getTabAt(0).setIcon(R.drawable.ic_icon_a_24);
        tabLayout.getTabAt(1).setIcon(R.drawable.ic_icon_b_24);



        tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                fragmentTwo.closeInfoWindows();
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
        dataFilter.addAction("data");
        dataFilter.addAction("time");
        dataFilter.addAction("time_wagon");
        dataFilter.addAction("changetab");
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



    private BroadcastReceiver broadcastReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            Log.d(TAG,"Broadcast received: " + intent.getAction());


            switch (intent.getAction()){
                case "time":
                    fragmentOne.initData(dataService.getBufferZoneList());

                    break;
                case "time_wagon":
                    setAlert();
                    break;

                case "data":
                    timeService.timeMethod();

                    fragmentOne.initData(dataService.getBufferZoneList());
                    fragmentTwo.addMarker(dataService.getBufferZoneList());
                    break;
                case "changetab":
                    viewPager.setCurrentItem(0);
                    fragmentOne.expandSpecifiedGroup(dataService.getBufferZoneList(),intent.getStringExtra("buffername"));
                    break;

            }


        }
    };

    public void setAlert(){
        AlertDialog.Builder alert = new AlertDialog.Builder(this);
        final String buffer = sharedPreferences.getString(getString(R.string.buffer_time),"'Not found'");
        String alertTitle = getString(R.string.alert_time_title);
        alert.setTitle(alertTitle);
        String alertMsg = getString(R.string.wagon_time) + " " + buffer;
        alert.setMessage(alertMsg);

        alert.setPositiveButton("Gå til vogn", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                viewPager.setCurrentItem(0);
                fragmentOne.expandSpecifiedGroup(dataService.getBufferZoneList(),buffer);
            }
        });
        alert.setNegativeButton("Luk", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                Toast.makeText(getApplicationContext(),"Du har trykket på 'Luk'",Toast.LENGTH_LONG).show();
            }
        });
        alert.show();

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
            bufferZones = dataService.getBufferZoneList();
            fragmentOne.initData(bufferZones);
            Log.d(TAG,"Connected to DataService");
        }

        @Override
        public void onServiceDisconnected(ComponentName name) {
            dataServiceBound = false;
        }
    };

    private void setupViewPager(ViewPager viewPager) {
        ViewPagerAdapter viewPagerAdapter = new ViewPagerAdapter(getSupportFragmentManager());

        String firstText = getResources().getString(R.string.tab_a_label);
        String secondText = getResources().getString(R.string.tab_b_label);

        fragmentOne = new FragmentOne();
        fragmentTwo = new FragmentTwo();

        viewPagerAdapter.addFragment(fragmentOne,firstText);
        viewPagerAdapter.addFragment(fragmentTwo,secondText);
        viewPager.setAdapter(viewPagerAdapter);
    }
}
