package com.example.sterilflowapp;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
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
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

       // if (savedInstanceState != null)
    }

    @Override
    public void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
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

}
