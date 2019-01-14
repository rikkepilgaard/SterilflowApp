package com.example.sterilflowapp.interactor;

import android.content.Context;

import com.example.sterilflowapp.model.BufferZone;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;
import org.xmlpull.v1.XmlPullParserFactory;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;

import static com.example.sterilflowapp.ConstantValues.BUFFERZONE;
import static com.example.sterilflowapp.ConstantValues.BUFFER_FORMER_GLN;
import static com.example.sterilflowapp.ConstantValues.BUFFER_GLN;
import static com.example.sterilflowapp.ConstantValues.BUFFER_LATITUDE;
import static com.example.sterilflowapp.ConstantValues.BUFFER_LOCATION;
import static com.example.sterilflowapp.ConstantValues.BUFFER_LONGITUDE;
import static com.example.sterilflowapp.ConstantValues.BUFFER_NAME;
import static com.example.sterilflowapp.ConstantValues.BUFFER_XML;

class ParseBufferzoneXML {

    ParseBufferzoneXML(){}

    ArrayList<BufferZone> parseBufferzoneXML(Context context) {
        //https://www.youtube.com/watch?v=-deKKeEdpbw
        XmlPullParserFactory parserFactory;
        try {
            parserFactory = XmlPullParserFactory.newInstance();
            XmlPullParser parser = parserFactory.newPullParser();
            InputStream is = context.getAssets().open(BUFFER_XML);
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
            String zone;

            switch (eventType){
                case XmlPullParser.START_TAG:
                    zone = parser.getName();


                    if(BUFFERZONE.equals(zone)){
                        currentBuffer = new BufferZone();
                        bufferZones.add(currentBuffer);
                        formerGln = new ArrayList<>();
                    } else if (currentBuffer != null){
                        if (BUFFER_NAME.equals(zone)){
                            currentBuffer.setName(parser.nextText());
                        } else if(BUFFER_GLN.equals(zone)){
                            currentBuffer.setGln(parser.nextText());
                        } else if(BUFFER_FORMER_GLN.equals(zone)) {
                            formerGln.add(parser.nextText());
                        } else if (BUFFER_LATITUDE.equals(zone)){
                            currentBuffer.setLatitude(parser.nextText());
                        } else if (BUFFER_LONGITUDE.equals(zone)){
                            currentBuffer.setLongitude(parser.nextText());
                        } else if(BUFFER_LOCATION.equals(zone)){
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
