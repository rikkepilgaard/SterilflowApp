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

    TableLayout tableLayout;
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

        adapter = new ExpandableListAdaptor(getActivity(),bufferZones);
        listView.setAdapter(adapter);

    }


    private void addColumns() {

        TableRow row = new TableRow(getActivity().getApplicationContext());

        row.setBackgroundColor(getResources().getColor(R.color.columnColor));

        TextView tv2 = new TextView(getActivity().getApplicationContext());
        tv2.setText(R.string.bufferomrade);
        tv2.setGravity(Gravity.CENTER);
        tv2.setPadding(5, 30, 5, 30);
        tv2.setTypeface(null, Typeface.BOLD);
        //tv2.setBackgroundColor(getResources().getColor(R.color.columnColor));

        TextView tv3 = new TextView(getActivity().getApplicationContext());
        tv3.setText(R.string.number_of_wagons);
        tv3.setPadding(5, 30, 5, 30);
        tv3.setTypeface(null, Typeface.BOLD);
        tv3.setGravity(Gravity.CENTER);
        //tv3.setBackgroundColor(Color.parseColor("#bdbdbd"));


        TextView tv4 = new TextView(getActivity().getApplicationContext());
        tv4.setText("");
        tv4.setTypeface(null, Typeface.BOLD);
        tv4.setPadding(5, 30, 5, 30);
        //tv4.setBackgroundColor(Color.parseColor("#bdbdbd"));
        //tv4.setTextSize(TypedValue.COMPLEX_UNIT_PX,
                //getResources().getDimension(R.dimen.bigger_textsize));

        //row.addView(tv1);
        row.addView(tv2);
        row.addView(tv4);
        row.addView(tv3);

        tableLayout.addView(row);
    }

    public void addDataToTable(ArrayList<BufferZone> bufferZones) {

        //TableRow row = new TableRow(this);

        tableLayout.removeAllViews();
        addColumns();

        for (int i = 0; i < bufferZones.size() ; i++) {

            TableRow row = new TableRow(getActivity().getApplicationContext());

            row.setLayoutParams(new TableRow.LayoutParams(TableLayout.LayoutParams.WRAP_CONTENT));
            final BufferZone zone = bufferZones.get(i);

            String buffer = zone.getName();
            String antalVogne = "0";

            if(zone.getWagonList()!= null) {
                antalVogne = String.valueOf(zone.getWagonList().size());
            }
            TextView tv2 = new TextView(getActivity().getApplicationContext());
            tv2.setText(buffer);
            tv2.setGravity(Gravity.CENTER);
            tv2.setTypeface(null, Typeface.BOLD);
            tv2.setPadding(5, 30, 5, 30);

            ImageView image = new ImageView(getActivity().getApplicationContext());
            Bitmap bitmap = resizeImageBitmap("pil_ned", 20, 20);
            image.setImageBitmap(bitmap);
            image.setScaleType(ImageView.ScaleType.FIT_CENTER);
            TableRow.LayoutParams layoutParams = new TableRow.LayoutParams(30, 30);
            layoutParams.setMargins(5, 40, 5, 10);
            image.setLayoutParams(layoutParams);
            image.setTag("image" + i);


            TextView tv3 = new TextView(getActivity().getApplicationContext());
            tv3.setText(antalVogne);
            tv3.setTypeface(null, Typeface.BOLD);
            tv3.setGravity(Gravity.CENTER);
            tv3.setPadding(5, 30, 5, 30);


            //row.addView(tv1);
            row.addView(tv2);
            row.addView(image);
            row.addView(tv3);


            row.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    int index = tableLayout.indexOfChild(v);
                    addRows(v,zone ,index);
                }
            });

            tableLayout.addView(row);
        }
    }





    private void addRows(View v, BufferZone bz, int index){

        if(!isOpen) {

            v.setBackgroundColor(Color.parseColor("#e0e0e0"));

            TableRow row = new TableRow(getActivity().getApplicationContext());
            row.setBackgroundColor(Color.parseColor("#eeeeee"));

            String id1 = "Vogn ID";
            String placedAtTime1 = "Har stået siden";
            String beenThereSince1 = "Har stået i";

            TextView tv11 = new TextView(getActivity().getApplicationContext());
            tv11.setText(id1);
            tv11.setGravity(Gravity.CENTER);
            tv11.setTypeface(null, Typeface.BOLD);
            //tv11.setBackgroundColor(Color.parseColor("#eeeeee"));
            tv11.setPadding(5, 30, 5, 30);

            TextView tv21 = new TextView(getActivity().getApplicationContext());
            tv21.setText(placedAtTime1);
            tv21.setGravity(Gravity.CENTER);
            tv21.setTypeface(null, Typeface.BOLD);
            //tv21.setBackgroundColor(Color.parseColor("#eeeeee"));
            tv21.setPadding(5, 30, 5, 30);

            TextView tv22 = new TextView(getActivity().getApplicationContext());
            tv22.setText(beenThereSince1);
            tv22.setGravity(Gravity.CENTER);
            tv22.setTypeface(null, Typeface.BOLD);
            //tv22.setBackgroundColor(Color.parseColor("#eeeeee"));
            tv22.setPadding(5, 30, 5, 30);

            row.addView(tv11);
            row.addView(tv21);
            row.addView(tv22);

            tableLayout.addView(row,index+1);

            if(bz.getWagonList() != null) {
                for (TrackEvent wagon : bz.getWagonList()) {
                    TableRow row1 = new TableRow(getActivity().getApplicationContext());

                    String objectKey = wagon.getObjectkey();
                    String id = objectKey.replace("urn:rm-trolley:","").toUpperCase();
                    //String bufferZone = wagon.getLocationSgln();
                    String placedAtTime = wagon.getEventTime();

                    TextView tv1 = new TextView(getActivity().getApplicationContext());
                    tv1.setText(id);
                    tv1.setPadding(5, 30, 5, 30);
                    tv1.setGravity(Gravity.CENTER);

                    TextView tv2 = new TextView(getActivity().getApplicationContext());
                    tv2.setText(placedAtTime);
                    tv2.setPadding(5, 30, 5, 30);
                    tv2.setGravity(Gravity.CENTER);

                /*
                TextView tv3 = new TextView(this);
                tv3.setText(bufferZone);
                tv3.setPadding(5, 30, 5, 30); */

                    TextView tv4 = new TextView(getActivity().getApplicationContext());
                    timeCounter(wagon.getEventTime(), tv4);
                    tv4.setPadding(5, 30, 5, 30);
                    tv4.setGravity(Gravity.CENTER);

                    row1.addView(tv1);
                    row1.addView(tv2);
                    row1.addView(tv4);
                    //row1.addView(tv3);

                    tableLayout.addView(row1, index + 2);
                }
            }
            isOpen = true;
        } else {
            tableLayout.removeAllViews();
            addColumns();
            //addDataToTable();
            isOpen = false;
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
