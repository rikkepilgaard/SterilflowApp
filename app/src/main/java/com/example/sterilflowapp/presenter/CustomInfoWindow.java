package com.example.sterilflowapp.presenter;

import android.content.Context;
import android.content.Intent;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.example.sterilflowapp.R;

import org.osmdroid.views.MapView;
import org.osmdroid.views.overlay.infowindow.MarkerInfoWindow;

import androidx.localbroadcastmanager.content.LocalBroadcastManager;

//https://code.google.com/archive/p/osmbonuspack/wikis/Tutorial_2.wiki
public class CustomInfoWindow extends MarkerInfoWindow {

Context context;
    public CustomInfoWindow(MapView mapView, Context context){
        super(R.layout.bubble_layout,mapView);
        this.context=context;
    }

    @Override
    public void onOpen(Object item){
        super.onOpen(item);
        Button btn = (Button)(mView.findViewById(R.id.bubble_moreinfo));
        final TextView txtTitle = (TextView) mView.findViewById(R.id.bubble_title);
        TextView txtDescription = (TextView) mView.findViewById(R.id.bubble_description);
        TextView txtSubdescription = (TextView) mView.findViewById(R.id.bubble_subdescription);
        mView.findViewById(R.id.bubble_moreinfo).setVisibility(View.VISIBLE);
        btn.setBackgroundResource(R.drawable.moreinfo);

        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String buffername = txtTitle.getText().toString();
                Intent broadcastIntent = new Intent();
                broadcastIntent.setAction("changetab");
                broadcastIntent.putExtra("buffername",buffername);
                LocalBroadcastManager.getInstance(context).sendBroadcast(broadcastIntent);

        };

    });

}
}