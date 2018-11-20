package com.example.sterilflowapp.dal;

import android.content.Context;

import com.example.sterilflowapp.model.Building;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;
import org.xmlpull.v1.XmlPullParserFactory;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;

public class ParseBuildingXML {

    public ParseBuildingXML(){}

    public ArrayList<Building> parseBuildingsXML(Context context) {
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

    private ArrayList<Building> buildingList;

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
}
