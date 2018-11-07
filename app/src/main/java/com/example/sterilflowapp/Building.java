package com.example.sterilflowapp;

import android.app.Application;
import android.content.Context;
import android.os.Build;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;
import org.xmlpull.v1.XmlPullParserFactory;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;

public class Building {

    private String name;
    private double latitude, longitude;

    public Building(String name, double latitude, double longitude) {
        this.name = name;
        this.latitude = latitude;
        this.longitude = longitude;
    }



    public Building() {
    }

    public ArrayList<Building> getBuildings(Context context) {
        XmlPullParserFactory parserFactory;
        try {
            parserFactory = XmlPullParserFactory.newInstance();
            XmlPullParser parser = parserFactory.newPullParser();
            InputStream is = context.getAssets().open("AUHbuildings.xml");
            parser.setFeature(XmlPullParser.FEATURE_PROCESS_NAMESPACES, false);
            parser.setInput(is, null);

            processParsing(parser);

        } catch (XmlPullParserException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }


        return buildingList;
    }

    ArrayList<Building> buildingList;

    private void processParsing(XmlPullParser parser) throws IOException, XmlPullParserException {
        //https://www.youtube.com/watch?v=-deKKeEdpbw
        buildingList = new ArrayList<>();
        int eventType = parser.getEventType();
        Building currentBuilding = null;


        while (eventType != XmlPullParser.END_DOCUMENT) {
            String name = null;

            switch (eventType) {
                case XmlPullParser.START_TAG:
                    name = parser.getName();


                    if ("Building".equals(name)) {
                        currentBuilding = new Building();
                        buildingList.add(currentBuilding);

                    } else if (currentBuilding != null) {
                        if ("name".equals(name)) {
                            currentBuilding.setName(parser.nextText());
                        } else if ("latitude".equals(name)) {
                            currentBuilding.setLatitude(Double.parseDouble(parser.nextText()));
                        } else if ("longitude".equals(name)) {
                            currentBuilding.setLongitude(Double.parseDouble(parser.nextText()));
                        }
                    }
                    break;

            }
            eventType = parser.next();

        }
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public double getLatitude() {
        return latitude;
    }

    public void setLatitude(double latitude) {
        this.latitude = latitude;
    }

    public double getLongitude() {
        return longitude;
    }

    public void setLongitude(double longitude) {
        this.longitude = longitude;
    }
}
