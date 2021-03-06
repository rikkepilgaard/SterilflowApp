package com.example.sterilflowapp.view;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Typeface;
import android.preference.PreferenceManager;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.sterilflowapp.R;
import com.example.sterilflowapp.model.*;

import java.util.ArrayList;

import androidx.localbroadcastmanager.content.LocalBroadcastManager;

import static com.example.sterilflowapp.ConstantValues.ACTION_CHANGE_TAB;
import static com.example.sterilflowapp.ConstantValues.EXTRA_BUFFERZONE;

public class ExpandableListAdapter extends BaseExpandableListAdapter {

    private Context context;
    private ArrayList<BufferZone> bufferZones;
    private SharedPreferences sharedPreferences;


    ExpandableListAdapter(Context context, ArrayList<BufferZone> bufferZones) {
        this.context = context;
        this.bufferZones = bufferZones;
    }

    void updateListView(ArrayList<BufferZone> bufferZonesNew)
    {
        this.bufferZones = bufferZonesNew;

        notifyDataSetChanged();
    }

    @Override
    public int getGroupCount() {
        return bufferZones.size();
    }

    @Override
    public int getChildrenCount(int groupPosition) {

        //Makes childrenCount 1 bigger than actual size. Used for making a header.
        if(bufferZones.get(groupPosition).getTrolleyList()!=null){
            return bufferZones.get(groupPosition).getTrolleyList().size() +1;
        } else return 1;
    }

    @Override
    public BufferZone getGroup(int groupPosition) {
        return bufferZones.get(groupPosition);
    }

    @Override
    public TrackEvent getChild(int groupPosition, int childPosition) {

        return bufferZones.get(groupPosition).getTrolleyList().get(childPosition);
    }

    @Override
    public long getGroupId(int groupPosition) {
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

    @Override
    public View getGroupView(final int groupPosition, boolean isExpanded, View convertView, ViewGroup parent) {

        final BufferZone bufferZone = getGroup(groupPosition);

        String headerTitleBuffer = bufferZone.getName();
        String location =  bufferZone.getLocationName();
        int headerTitleWagon = 0;

        if(bufferZone.getTrolleyList() != null) {
            headerTitleWagon = bufferZone.getTrolleyList().size();
        }

        if(convertView == null) {
            LayoutInflater inflater = (LayoutInflater) this.context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            assert inflater != null;
            convertView = inflater.inflate(R.layout.list_group, parent,false);
        }

        convertView.setBackgroundColor(context.getResources().getColor(R.color.columnColor));


        //If bufferzone contains an expired trolley, set warning image
        ImageView timeImage = convertView.findViewById(R.id.lvImageTime);
        if(bufferZone.containsExpiredWagon()) {
            timeImage.setVisibility(View.VISIBLE);
        }
        else timeImage.setVisibility(View.INVISIBLE);


        TextView txtBufferName = convertView.findViewById(R.id.lvHeaderBuffer);
        txtBufferName.setText(headerTitleBuffer);
        TextView txtLocation = convertView.findViewById(R.id.lvLocation);
        txtLocation.setText(location);
        TextView txtNumberTrolleys = convertView.findViewById(R.id.lvHeaderNumberOfWagons);
        txtNumberTrolleys.setText(String.valueOf(headerTitleWagon));


        //Bold text when number of trolleys is not 0.
        if(bufferZone.getTrolleyList()!=null){
            txtNumberTrolleys.setTypeface(null,Typeface.BOLD);
        }
        if(bufferZone.getTrolleyList()==null){
            txtNumberTrolleys.setTypeface(null,Typeface.NORMAL);
            timeImage.setVisibility(View.INVISIBLE);
        }

        //OnClickListener for button "Show on map" ("Vis på kort"). Sends broadcast when clicked
        Button btnToMap = convertView.findViewById(R.id.btnToMap);
        btnToMap.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent broadcastIntent = new Intent();
                broadcastIntent.setAction(ACTION_CHANGE_TAB);
                broadcastIntent.putExtra(EXTRA_BUFFERZONE,bufferZone.getName());
                LocalBroadcastManager.getInstance(context).sendBroadcast(broadcastIntent);
            }
        });

        return convertView;
    }

    @Override
    public View getChildView(int groupPosition, int childPosition, boolean isLastChild, View convertView, ViewGroup parent) {

        LayoutInflater inflater = (LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        //When childrenCount is 1, no trolleys are in bufferzone. Inflate different view telling
        //user that no trolleys are in bufferzone.
        if(getChildrenCount(groupPosition)==1){
            assert inflater != null;
            convertView = inflater.inflate(R.layout.empty_bufferzone,parent,false);
        }

        //If bufferzone contains trolleys, make first line a header
       if(childPosition == 0 && getChildrenCount(groupPosition)>1){
           assert inflater != null;
           convertView = inflater.inflate(R.layout.child_header_layout,parent,false);
        }

        //Insert info about trolleys
        if(childPosition>0){

            TrackEvent trackEvent = getChild(groupPosition,childPosition-1);

            assert inflater != null;
            convertView = inflater.inflate(R.layout.list_item,parent,false);

            String id = trackEvent.getObjectkey();
            id = id.replace("urn:rm-trolley:","").toUpperCase();

            String placedAt = trackEvent.getEventTime();
            String sub1 = placedAt.substring(0,5);
            String sub2 = placedAt.substring(11,16);
            placedAt = sub1 + " " + sub2;

            sharedPreferences = PreferenceManager.getDefaultSharedPreferences(context);
            String beenSince = sharedPreferences.getString(trackEvent.getObjectkey(),"");

            TextView txtChildId = convertView.findViewById(R.id.lvItemID);
            txtChildId.setText(id);
            TextView txtChildPlaced = convertView.findViewById(R.id.lvItemPlaced);
            txtChildPlaced.setText(placedAt);
            TextView txtChildSince = convertView.findViewById(R.id.lvItemSince);
            txtChildSince.setText(beenSince);


            //If trolley is expired add warning image and change text color
            ImageView imageChild = convertView.findViewById(R.id.lvChildImageTime);
            boolean isExpired = sharedPreferences.getBoolean(trackEvent.getObjectkey()+"bool", false);
            if(isExpired){
                imageChild.setVisibility(View.VISIBLE);
                txtChildId.setTextColor(context.getResources().getColor(R.color.red));
                txtChildPlaced.setTextColor(context.getResources().getColor(R.color.red));
                txtChildSince.setTextColor(context.getResources().getColor(R.color.red));
            }

        }


        return convertView;
    }


    @Override
    public boolean isChildSelectable(int groupPosition, int childPosition) {
        return false;
    }


}
