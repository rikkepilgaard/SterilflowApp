package com.example.sterilflowapp.dal;

import android.content.Context;

import com.example.sterilflowapp.model.BufferZone;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;
import org.xmlpull.v1.XmlPullParserFactory;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;

public class ParseBufferzoneXML {

    public ParseBufferzoneXML(){}

    public ArrayList<BufferZone> parseBufferzoneXML(Context context) {
        //https://www.youtube.com/watch?v=-deKKeEdpbw
        XmlPullParserFactory parserFactory;
        try {
            parserFactory = XmlPullParserFactory.newInstance();
            XmlPullParser parser = parserFactory.newPullParser();
            InputStream is = context.getAssets().open("buffer.xml");
            parser.setFeature(XmlPullParser.FEATURE_PROCESS_NAMESPACES,false);
            parser.setInput(is,null);

            processParsing(parser);

        } catch (XmlPullParserException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return bufferZones;
    }

    private ArrayList<BufferZone> bufferZones;

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
                        currentBuffer.setContainsExpiredWagon(false);
                    }
                    break;

            }
            eventType = parser.next();
        }
    }

}
