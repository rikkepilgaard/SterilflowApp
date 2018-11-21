package com.example.sterilflowapp.presenter;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Typeface;
import android.preference.PreferenceManager;
import android.text.Html;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.sterilflowapp.R;
import com.example.sterilflowapp.model.*;

import java.util.ArrayList;
import java.util.HashMap;

import androidx.localbroadcastmanager.content.LocalBroadcastManager;

public class ExpandableListAdaptor extends BaseExpandableListAdapter {
    private static final String TAG = "ExpandableListAdaptor";

    SharedPreferences sharedPreferences;

    private Context context;
    private ArrayList<BufferZone> bufferZones;
    private HashMap<BufferZone, ArrayList<TrackEvent>> listHashMap;


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
        } else return 1;
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
    public View getGroupView(final int groupPosition, boolean isExpanded, View convertView, ViewGroup parent) {

        final BufferZone bufferZone = getGroup(groupPosition);

        String headerTitleBuffer = bufferZone.getName();
        int headerTitleWagon = 0;

        if(bufferZone.getTrolleyList() != null) {
            headerTitleWagon = bufferZone.getTrolleyList().size();
        }
        if(convertView == null) {
            LayoutInflater inflater = (LayoutInflater) this.context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(R.layout.list_group, null);
        }

        convertView.setBackgroundColor(context.getResources().getColor(R.color.columnColor));

        ImageView timeImage = convertView.findViewById(R.id.lvImageTime);

        if(bufferZone.containsExpiredWagon()) {
            timeImage.setVisibility(View.VISIBLE);
        }
        else timeImage.setVisibility(View.INVISIBLE);


        TextView lblListHeader = convertView.findViewById(R.id.lvHeaderBuffer);
        lblListHeader.setText(headerTitleBuffer);
        TextView lblListHeaderWagons = convertView.findViewById(R.id.lvHeaderNumberOfWagons);
        lblListHeaderWagons.setText(String.valueOf(headerTitleWagon));

        if(bufferZone.getTrolleyList()!=null){
            lblListHeaderWagons.setTypeface(null,Typeface.BOLD);
        }
        if(bufferZone.getTrolleyList()==null){
            lblListHeaderWagons.setTypeface(null,Typeface.NORMAL);
            timeImage.setVisibility(View.INVISIBLE);
        }

        Button btnToMap = convertView.findViewById(R.id.btnToMap);
        btnToMap.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent broadcastIntent = new Intent();
                broadcastIntent.setAction("changetab");
                broadcastIntent.putExtra("buffername",bufferZone.getName());
                LocalBroadcastManager.getInstance(context).sendBroadcast(broadcastIntent);
            }
        });

        return convertView;
    }

    @Override
    public View getChildView(int groupPosition, int childPosition, boolean isLastChild, View convertView, ViewGroup parent) {

        LayoutInflater inflater = (LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        if(getChildrenCount(groupPosition)==1){
            convertView = inflater.inflate(R.layout.empty_bufferzone,null);
        }

       if(childPosition == 0 && getChildrenCount(groupPosition)>1){
            convertView = inflater.inflate(R.layout.child_header_layout,null);
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

            sharedPreferences = PreferenceManager.getDefaultSharedPreferences(context);
            long timeDifference = sharedPreferences.getLong(trackEvent.getObjectkey(),0);
            int diffMinutes = safeLongToInt(timeDifference / (60 * 1000) % 60);
            int diffHours = safeLongToInt(timeDifference / (60 * 60 * 1000) % 24);
            int diffDays = safeLongToInt(timeDifference / (24 * 60 * 60 * 1000));
            String diffMinutesText = (diffMinutes < 10 ? "0" : "") + diffMinutes;
            String diffHoursText = (diffHours < 10 ? "0" : "") + diffHours;
            String diffDaysText = (diffDays < 10 ? "0" : "") + diffDays;
            String text = diffHoursText + "<b>t </b>" + diffMinutesText + "<b>m </b>";


            TextView txtChildId = (TextView) convertView.findViewById(R.id.lvItemID);
            txtChildId.setText(id);
            TextView txtChildPlaced = (TextView) convertView.findViewById(R.id.lvItemPlaced);
            txtChildPlaced.setText(placedAt);
            TextView txtChildSince = (TextView) convertView.findViewById(R.id.lvItemSince);
            if (diffDays != 0) {
                txtChildSince.setText(diffDaysText + ":" + diffHoursText + ":" + diffMinutesText);
            } else txtChildSince.setText(Html.fromHtml(text));

            ImageView imageChild = convertView.findViewById(R.id.lvChildImageTime);

            if(trackEvent.isExpired()){
                imageChild.setVisibility(View.VISIBLE);
                txtChildId.setTextColor(context.getResources().getColor(R.color.red));
                txtChildPlaced.setTextColor(context.getResources().getColor(R.color.red));
                txtChildSince.setTextColor(context.getResources().getColor(R.color.red));
                //convertView.setBackgroundColor(context.getResources().getColor(R.color.warningColor));
                
            }

        }


        return convertView;
    }


    @Override
    public boolean isChildSelectable(int groupPosition, int childPosition) {
        return false;
    }



    //https://stackoverflow.com/questions/1590831/safely-casting-long-to-int-in-java
    public static int safeLongToInt(long l) {
        if (l < Integer.MIN_VALUE || l > Integer.MAX_VALUE) {
            throw new IllegalArgumentException
                    (l + " cannot be cast to int without changing its value.");
        }
        return (int) l;
    }


}
