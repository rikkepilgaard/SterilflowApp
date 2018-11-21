package com.example.sterilflowapp.dal;

import android.content.Context;

import com.example.sterilflowapp.model.Building;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;
import org.xmlpull.v1.XmlPullParserFactory;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;

import static com.example.sterilflowapp.ConstantValues.BUILDING;
import static com.example.sterilflowapp.ConstantValues.BUILDINGS_XML;
import static com.example.sterilflowapp.ConstantValues.BUILDING_LATITUDE;
import static com.example.sterilflowapp.ConstantValues.BUILDING_LONGITUDE;
import static com.example.sterilflowapp.ConstantValues.BUILDING_NAME;

class ParseBuildingXML {

    ParseBuildingXML(){}

    ArrayList<Building> parseBuildingsXML(Context context) {
        XmlPullParserFactory parserFactory;
        try {
            parserFactory = XmlPullParserFactory.newInstance();
            XmlPullParser parser = parserFactory.newPullParser();
            InputStream is = context.getAssets().open(BUILDINGS_XML);
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
            String name;

            switch (eventType) {
                case XmlPullParser.START_TAG:
                    name = parser.getName();


                    if (BUILDING.equals(name)) {
                        currentBuilding = new Building();
                        buildingList.add(currentBuilding);

                    } else if (currentBuilding != null) {
                        if (BUILDING_NAME.equals(name)) {
                            currentBuilding.setName(parser.nextText());
                        } else if (BUILDING_LATITUDE.equals(name)) {
                            currentBuilding.setLatitude(Double.parseDouble(parser.nextText()));
                        } else if (BUILDING_LONGITUDE.equals(name)) {
                            currentBuilding.setLongitude(Double.parseDouble(parser.nextText()));
                        }
                    }
                    break;

            }
            eventType = parser.next();

        }
    }
}
