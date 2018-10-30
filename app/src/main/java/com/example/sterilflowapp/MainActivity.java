package com.example.sterilflowapp;

import com.google.android.material.tabs.TabLayout;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import androidx.appcompat.app.AlertDialog;
import androidx.viewpager.widget.ViewPager;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Html;
import android.util.Log;
import android.widget.TextView;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;
import org.xmlpull.v1.XmlPullParserFactory;

import java.io.IOException;
import java.io.InputStream;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

public class MainActivity extends AppCompatActivity {

    private static final String TAG = "MainActivity";
    private FragmentOne fragmentOne;
    private FragmentTwo fragmentTwo;

    private TabLayout tabLayout;
    private CustomViewPager viewPager;
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
                        TrackEvent trackEvent = ds1.getValue(TrackEvent.class);
                        trackEventArrayList.add(trackEvent);

                    }
                }

                wagonInBufferzones();

                if(fragmentOne!=null){
                    fragmentOne.initData(bufferZones);
                }
                if(fragmentTwo!= null){
                    fragmentTwo.addMarker();
                }



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

    public void wagonInBufferzones(){

        ArrayList<TrackEvent> arrayList = newestEvents(trackEventArrayList);


        for (int j = 0; j < bufferZones.size(); j++){
            bufferZones.get(j).setWagonList(null);
            ArrayList<TrackEvent> list = new ArrayList<>();

            switch (bufferZones.get(j).getGln()) {
                case "urn:epc:id:sgln:57980101.8705.0": //buffer 202
                case "urn:epc:id:sgln:57980101.5946.0": //buffer nordlager

                    for (int i = 0; i < arrayList.size() ; i++){
                        TrackEvent trackEvent = arrayList.get(i);
                        if (bufferZones.get(j).getGln().equals(trackEvent.getLocationSgln())) {
                            for (int h = 0; h < trackEventArrayList.size(); h++) {
                                TrackEvent event = trackEventArrayList.get(h);
                                if (event.getObjectkey().equals(trackEvent.getObjectkey())) {
                                    if (event.getEventTime().equals(trackEvent.getEventTime())) {
                                        if (event.getLocationSgln().equals(trackEvent.getLocationSgln())) {
                                            TrackEvent event1 = trackEventArrayList.get(h-1);
                                            for (String formerGln: bufferZones.get(j).getFormerGln() ) {
                                                if(event1.getLocationSgln().equals(formerGln)){
                                                    list.add(trackEvent);
                                                    bufferZones.get(j).setWagonList(list);
                                                }
                                            }
                                        }
                                    }
                                }
                            }
                        }
                    }

                    break;

                case "urn:epc:id:sgln:57980101.7856.0": //buffer ren steril
                case "urn:epc:id:sgln:57980102.6407.0": //buffer 109
                case "urn:epc:id:sgln:57980102.6410.0": //buffer 120
                case "urn:epc:id:sgln:57980101.3660.0": //buffer 232
                case "urn:epc:id:sgln:57980102.8548.0": //buffer 236

                    for (int i = 0; i < arrayList.size() ; i++) {
                        TrackEvent trackEvent = arrayList.get(i);
                        if (bufferZones.get(j).getGln().equals(trackEvent.getLocationSgln())) {
                            list.add(trackEvent);
                            bufferZones.get(j).setWagonList(list);
                        }
                    }

                    break;
            }
        }
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
        fragmentOne = new FragmentOne();
        fragmentTwo = new FragmentTwo();

        viewPagerAdapter.addFragment(fragmentOne,firstText);
        viewPagerAdapter.addFragment(fragmentTwo,secondText);
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
        ArrayList<String> formerGln = new ArrayList<>();

        while (eventType!=XmlPullParser.END_DOCUMENT){
            String zone = null;
            //ArrayList<String> formerGln;

            switch (eventType){
                case XmlPullParser.START_TAG:
                    zone = parser.getName();


                    if("bufferzone".equals(zone)){
                        currentBuffer = new BufferZone();
                        bufferZones.add(currentBuffer);
                        formerGln = new ArrayList<>();
                    } else if (currentBuffer != null){
                        if ("name".equals(zone)){
                            currentBuffer.setName(parser.nextText());
                        } else if("gln".equals(zone)){
                            currentBuffer.setGln(parser.nextText());
                        } else if("formerGln".equals(zone)) {
                            formerGln.add(parser.nextText());
                        } else if ("latitude".equals(zone)){
                            currentBuffer.setLatitude(parser.nextText());
                        } else if ("longitude".equals(zone)){
                            currentBuffer.setLongitude(parser.nextText());
                        } else if("location".equals(zone)){
                            currentBuffer.setLocationName(parser.nextText());
                        }

                      currentBuffer.setFormerGln(formerGln);
                    }
                break;

            }
            eventType = parser.next();

        }
    }

    public void timeCounter(final String date, final TextView textView){

        final Thread t = new Thread() {

            @Override
            public void run() {

                while (!isInterrupted()) {

                    try {

                        Thread.sleep(100);
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
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

                                if(diffDays != 0)
                                {
                                    textView.setText(diffDaysText + ":" + diffHoursText + ":" + diffMinutesText);
                                }
                                else textView.setText(Html.fromHtml(text));

//                                AlertDialog.Builder alert = new AlertDialog.Builder(getApplicationContext());
//                                alert.setTitle(R.string.alert_time_title);
//                                alert.setMessage(R.string.alert_time_title);

                                //textView.setText(String.valueOf(DateUtils.formatElapsedTime(diffSeconds)));
                            }
                        });


                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }
        };

        t.start();
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

}
