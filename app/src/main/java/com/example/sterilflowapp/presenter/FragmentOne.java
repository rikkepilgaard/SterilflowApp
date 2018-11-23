package com.example.sterilflowapp.presenter;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ExpandableListAdapter;
import android.widget.ExpandableListView;

import com.example.sterilflowapp.R;
import com.example.sterilflowapp.model.BufferZone;

import java.util.ArrayList;


public class FragmentOne extends Fragment {


    private ExpandableListView listView;
    private ExpandableListAdapter adapter;


    public FragmentOne() {
        // Required empty public constructor
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_fragment_one, container, false);

        listView = view.findViewById(R.id.listViewExpandable);

        listView.setOnGroupExpandListener(new ExpandableListView.OnGroupExpandListener() {

            int previousGroup = -1;
            @Override
            public void onGroupExpand(int groupPosition) {
                if(groupPosition != previousGroup)
                    listView.collapseGroup(previousGroup);
                previousGroup = groupPosition;
            }
        });

        return view;
    }

    void initData(ArrayList<BufferZone> bufferZones) {


        if(adapter == null) {
            adapter = new ExpandableListAdaptor(getActivity(), bufferZones);
            listView.setAdapter(adapter);
        }

        ((ExpandableListAdaptor) adapter).updateListView(bufferZones);
    }

    void expandSpecifiedGroup(ArrayList<BufferZone> bufferZones, String bufferName){
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
