package com.example.sterilflowapp;

import com.google.android.material.tabs.TabLayout;

import androidx.fragment.app.Fragment;
import androidx.viewpager.widget.ViewPager;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;
import org.xmlpull.v1.XmlPullParserFactory;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    private FragmentOne fragmentOne;
    private FragmentTwo fragmentTwo;

    TabLayout tabLayout;
    ViewPager viewPager;
    private ArrayList<BufferZone> bufferZones;
    private ArrayList<TrackEvent> trackEventArrayList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        fragmentOne = new FragmentOne();
        fragmentTwo = new FragmentTwo();

        trackEventArrayList = new ArrayList<>();

        trackEventArrayList.add(new TrackEvent("dasda","dadas","fafaf","ggrgr","desad","fafa","fefa","fesfs","faafef","dada","daadwa"));

        tabLayout = findViewById(R.id.tabs);
        viewPager = findViewById(R.id.viewPager);

        setupViewPager(viewPager);
        tabLayout.setupWithViewPager(viewPager);

        tabLayout.getTabAt(0).setIcon(R.drawable.ic_icon_a_24);
        tabLayout.getTabAt(1).setIcon(R.drawable.ic_icon_b_24);


        parseXML();
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

                    if("Bufferzone".equals(zone)){
                        currentBuffer = new BufferZone();
                        bufferZones.add(currentBuffer);
                    } else if (currentBuffer != null){
                        if ("Name".equals(zone)){
                            currentBuffer.setName(parser.nextText());
                        } else if("gln".equals(zone)){
                            currentBuffer.setGln(parser.nextText());
                        } else if ("Latitude".equals(zone)){
                            currentBuffer.setLatitude(parser.nextText());
                        } else if ("Longitude".equals(zone)){
                            currentBuffer.setLongitude(parser.nextText());
                        }

                    }
                break;

            }
            eventType = parser.next();
        }

        //printBuffers(bufferZones);
    }

    /*
    private void printBuffers(ArrayList<BufferZone> bufferZones) {


        for(BufferZone buffer : bufferZones){
            TableRow row = new TableRow(this);

            TextView tv = new TextView(this);
            tv.setText(buffer.getName());

            row.addView(tv);
            tableLayout.addView(row);
        }


    }
*/
}
