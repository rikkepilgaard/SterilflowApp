package com.example.sterilflowapp;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Bundle;
import androidx.fragment.app.Fragment;

import android.text.Html;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ExpandableListAdapter;
import android.widget.ExpandableListView;
import android.widget.ImageView;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;


public class FragmentOne extends Fragment {

    //TableLayout tableLayout;
    MainActivity activity;

    private ExpandableListView listView;
    private ExpandableListAdapter adapter;

    private boolean isOpen = false;


    public FragmentOne() {
        // Required empty public constructor
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_fragment_one, container, false);

        //tableLayout = view.findViewById(R.id.tableLayoutOverview);

        listView = view.findViewById(R.id.listViewExpandable);

        // Inflate the layout for this fragment
        return view;
    }

    public void initData(ArrayList<BufferZone> bufferZones) {

        HashMap<BufferZone, ArrayList<TrackEvent>> hashMap = new HashMap<>();

        for (BufferZone bufferZone : bufferZones){
            ArrayList<TrackEvent> trackEvents = bufferZone.getWagonList();
            hashMap.put(bufferZone, trackEvents);
        }

        if(adapter == null) {

            adapter = new ExpandableListAdaptor(getActivity(), bufferZones, hashMap);
            listView.setAdapter(adapter);
        }

        ((ExpandableListAdaptor) adapter).updateListView(bufferZones,hashMap);
    }

    public void expandSpecifiedGroup(ArrayList<BufferZone> bufferZones, String bufferName){
        for (int i = 0; i < bufferZones.size() ; i++){
            if(bufferZones.get(i).getName().equals(bufferName)){
                listView.expandGroup(i);
                listView.setSelectedGroup(i);
            }
            else {
                listView.collapseGroup(i);
            }
        }
    }



    public Bitmap resizeImageBitmap(String iconName, int width, int height){
        //https://stackoverflow.com/questions/14851641/change-marker-size-in-google-maps-api-v2

        Bitmap imageBitmap = BitmapFactory.decodeResource(getResources(),
                getResources().getIdentifier(iconName, "drawable", getActivity().getPackageName()));
        Bitmap resizedBitmap = Bitmap.createScaledBitmap(imageBitmap, width, height, false);
        return resizedBitmap;
    }




    public void timeCounter(final String date, final TextView textView){

        final Thread t = new Thread() {

            @Override
            public void run() {

                while (!isInterrupted()) {

                    try {

                        Thread.sleep(100);
                        getActivity().runOnUiThread(new Runnable() {
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
