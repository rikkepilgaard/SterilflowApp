package com.example.sterilflowapp;

import android.content.Context;
import android.graphics.Typeface;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.TextView;

import java.nio.Buffer;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;

public class ExpandableListAdaptor extends BaseExpandableListAdapter {
    private static final String TAG = "ExpandableListAdaptor";

    private Context context;
    private ArrayList<BufferZone> bufferZones;
    private HashMap<BufferZone, ArrayList<TrackEvent>> listHashMap;
    //private MainActivity activity = new MainActivity();

    public ExpandableListAdaptor(Context context, ArrayList<BufferZone> bufferZones, HashMap<BufferZone,ArrayList<TrackEvent>> hashMap) {
        this.context = context;
        this.bufferZones = bufferZones;
        this.listHashMap = hashMap;
    }

    public void updateListView(ArrayList<BufferZone> bufferZonesNew, HashMap<BufferZone,ArrayList<TrackEvent>> hashmap)
    {

        this.listHashMap = hashmap;
        this.bufferZones = bufferZonesNew;

        notifyDataSetChanged();
    }

    @Override
    public int getGroupCount() {
        return bufferZones.size();
    }

    @Override
    public int getChildrenCount(int groupPosition) {
        if(listHashMap.get(bufferZones.get(groupPosition)) != null) {
            return listHashMap.get(bufferZones.get(groupPosition)).size() + 1;
        } else return 0;
    }

    @Override
    public BufferZone getGroup(int groupPosition) {
        return bufferZones.get(groupPosition);
    }

    @Override
    public TrackEvent getChild(int groupPosition, int childPosition) {
        if (listHashMap.get(bufferZones.get(groupPosition))!= null) {
            return listHashMap.get(bufferZones.get(groupPosition)).get(childPosition);
        } else return null;
    }

    @Override
    public long getGroupId(int groupPosition) {
        Log.d(TAG,"getGroupID method entered");
        return groupPosition;
    }

    @Override
    public long getChildId(int groupPosition, int childPosition) {
        return childPosition;
    }

    @Override
    public boolean hasStableIds() {
        return true;
    }
    //https://robusttechhouse.com/how-to-add-header-footer-to-expandablelistview-childview/
    @Override
    public View getGroupView(int groupPosition, boolean isExpanded, View convertView, ViewGroup parent) {
        Log.d(TAG,"getGroupView method entered");

        BufferZone bufferZone = getGroup(groupPosition);

        String headerTitleBuffer = bufferZone.getName();
        int headerTitleWagon = 0;

        if(bufferZone.getWagonList() != null) {
            headerTitleWagon = bufferZone.getWagonList().size();
        }
        if(convertView == null) {
            LayoutInflater inflater = (LayoutInflater) this.context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(R.layout.list_group, null);
        }

        TextView lblListHeader = (TextView)convertView.findViewById(R.id.lvHeaderBuffer);
        lblListHeader.setTypeface(null,Typeface.BOLD);
        lblListHeader.setText(headerTitleBuffer);
        TextView lblListHeaderWagons = (TextView)convertView.findViewById(R.id.lvHeaderWagons);
        lblListHeaderWagons.setTypeface(null,Typeface.BOLD);
        lblListHeaderWagons.setText(String.valueOf(headerTitleWagon));


        return convertView;
    }

    @Override
    public View getChildView(int groupPosition, int childPosition, boolean isLastChild, View convertView, ViewGroup parent) {

        LayoutInflater inflater = (LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        if(childPosition == 0){
            convertView = inflater.inflate(R.layout.child_header_layout,null);
            TextView txtHeaderID = convertView.findViewById(R.id.txtChildHeaderID);
            txtHeaderID.setText("Vogn ID");
            TextView txtHeaderPlaced = convertView.findViewById(R.id.txtChildHeaderPlaced);
            txtHeaderPlaced.setText("Placeret kl.");
            TextView txtHeaderSince = convertView.findViewById(R.id.txtChildHeaderSince);
            txtHeaderSince.setText("Stået i");
        }

        if(childPosition>0){

            TrackEvent trackEvent = getChild(groupPosition,childPosition-1);

            convertView = inflater.inflate(R.layout.list_item,null);

            String id = trackEvent.getObjectkey();
            id = id.replace("urn:rm-trolley:","").toUpperCase();

            String placedAt = trackEvent.getEventTime();
            String sub1 = placedAt.substring(0,5);
            String sub2 = placedAt.substring(11,16);
            placedAt = sub1 + " " + sub2;

            TextView txtChildId = (TextView) convertView.findViewById(R.id.lvItemID);
            txtChildId.setText(id);
            TextView txtChildPlaced = (TextView) convertView.findViewById(R.id.lvItemPlaced);
            txtChildPlaced.setText(placedAt);
            TextView txtChildSince = (TextView) convertView.findViewById(R.id.lvItemSince);
           // activity.timeCounter(trackEvent.getEventTime(),txtChildSince);
        }

        return convertView;
    }

    @Override
    public boolean isChildSelectable(int groupPosition, int childPosition) {
        return false;
    }


}
