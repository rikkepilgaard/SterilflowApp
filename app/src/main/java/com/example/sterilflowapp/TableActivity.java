package com.example.sterilflowapp;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;
import org.xmlpull.v1.XmlPullParserFactory;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;

public class TableActivity extends AppCompatActivity {

    TextView txt;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_table);

        txt = findViewById(R.id.textView);

        parseXML();
    }

    private void parseXML() {
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
        ArrayList<BufferZone> bufferZones = new ArrayList<>();
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
                            currentBuffer.name = parser.nextText();
                        } else if("gln".equals(zone)){
                            currentBuffer.gln = parser.nextText();
                        } else if ("Latitude".equals(zone)){
                            currentBuffer.latitude = parser.nextText();
                        } else if ("Longitude".equals(zone)){
                            currentBuffer.longitude = parser.nextText();
                        }

                    }
                break;

            }
            eventType = parser.next();
        }

        printBuffers(bufferZones);
    }

    private void printBuffers(ArrayList<BufferZone> bufferZones) {
        StringBuilder builder = new StringBuilder();

        for(BufferZone buffer : bufferZones){
            builder.append(buffer.name).append("\n")
                    .append(buffer.gln).append("\n")
                    .append(buffer.latitude).append("\n")
                    .append(buffer.longitude).append("\n\n");
        }

        txt.setText(builder.toString());

    }

}
