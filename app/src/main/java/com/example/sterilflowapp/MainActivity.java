package com.example.sterilflowapp;

import com.google.android.material.tabs.TabLayout;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import androidx.fragment.app.Fragment;
import androidx.viewpager.widget.ViewPager;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;
import org.xmlpull.v1.XmlPullParserFactory;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    private static final String TAG = "MainActivity";
    //private FragmentOne fragmentOne;
    //private FragmentTwo fragmentTwo;

    TabLayout tabLayout;
    CustumViewPager viewPager;
    private ArrayList<BufferZone> bufferZones;
    private ArrayList<TrackEvent> trackEventArrayList;

    private FirebaseDatabase database;
    private DatabaseReference reference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Kun nødvendigt når fragment 1 skal opdateres med info fra fragment 2.
        //fragmentOne = new FragmentOne();
        //fragmentTwo = new FragmentTwo();


        database = FirebaseDatabase.getInstance();
        reference = database.getReference().child("TrackEvents");


        tabLayout = findViewById(R.id.tabs);
        viewPager = findViewById(R.id.viewPager);

        setupViewPager(viewPager);
        tabLayout.setupWithViewPager(viewPager);


        viewPager.setPagingEnabled(false);

        tabLayout.getTabAt(0).setIcon(R.drawable.ic_icon_a_24);
        tabLayout.getTabAt(1).setIcon(R.drawable.ic_icon_b_24);

        reference.addValueEventListener(new ValueEventListener() {

            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                // This method is called once with the initial value and again
                // whenever data at this location is updated.

                trackEventArrayList = new ArrayList<>();

                for(DataSnapshot ds : dataSnapshot.getChildren()){
                    for (DataSnapshot ds1 : ds.getChildren()) {
                        for (DataSnapshot ds2 : ds1.getChildren()) {
                            TrackEvent trackEvent = ds2.getValue(TrackEvent.class);
                            trackEventArrayList.add(trackEvent);
                        }
                    }
                }

                /*
                Iterable<DataSnapshot> snapshots = dataSnapshot.getChildren();
                while (snapshots.iterator().hasNext())
                {
                    trackEventsList.add(snapshots.iterator().next().getValue(TrackEvent.class));
                }
                */

                //createSamples(trackEventsList);

            }

            @Override
            public void onCancelled(DatabaseError error) {
                // Failed to read value
                Log.w(TAG, "Failed to read value.", error.toException());
            }
        });

        parseXML();

        //bufferZones.get(0).setFormerGln("hej123"); //TEST FOR AT SE OM METODER I FRAGMENTONE VIRKER.
    }


    public ArrayList<BufferZone> getBufferZoneList(){
        return bufferZones;
    }

    public ArrayList<TrackEvent> getTrackEventArrayList(){
        return trackEventArrayList;
    }

    private void setupViewPager(ViewPager viewPager) {
        ViewPagerAdapter viewPagerAdapter = new ViewPagerAdapter(getSupportFragmentManager());
        String firstText = getResources().getString(R.string.tab_a_label);
        String secondText = getResources().getString(R.string.tab_b_label);
        viewPagerAdapter.addFragment(new FragmentOne(),firstText);
        viewPagerAdapter.addFragment(new FragmentTwo(),secondText);
        viewPager.setAdapter(viewPagerAdapter);
    }

    private void parseXML() {
        //https://www.youtube.com/watch?v=-deKKeEdpbw
        XmlPullParserFactory parserFactory;
        try {
            parserFactory = XmlPullParserFactory.newInstance();
            XmlPullParser parser = parserFactory.newPullParser();
            InputStream is = getAssets().open("buffer.xml");
            parser.setFeature(XmlPullParser.FEATURE_PROCESS_NAMESPACES,false);
            parser.setInput(is,null);

            processParsing(parser);

        } catch (XmlPullParserException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void processParsing(XmlPullParser parser) throws IOException,XmlPullParserException {
        //https://www.youtube.com/watch?v=-deKKeEdpbw
        bufferZones = new ArrayList<>();
        int eventType = parser.getEventType();
        BufferZone currentBuffer = null;

        while (eventType!=XmlPullParser.END_DOCUMENT){
            String zone = null;

            switch (eventType){
                case XmlPullParser.START_TAG:
                    zone = parser.getName();

                    if("bufferzone".equals(zone)){
                        currentBuffer = new BufferZone();
                        bufferZones.add(currentBuffer);
                    } else if (currentBuffer != null){
                        if ("name".equals(zone)){
                            currentBuffer.setName(parser.nextText());
                        } else if("gln".equals(zone)){
                            currentBuffer.setGln(parser.nextText());
                        } else if("formerGln".equals(zone)) {
                            currentBuffer.setFormerGln(zone);
                        } else if ("latitude".equals(zone)){
                            currentBuffer.setLatitude(parser.nextText());
                        } else if ("longitude".equals(zone)){
                            currentBuffer.setLongitude(parser.nextText());
                        } else if("location".equals(zone)){
                            currentBuffer.setLocationName(parser.nextText());
                        }

                    }
                break;

            }
            eventType = parser.next();
        }

    }

}
