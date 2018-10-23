package com.example.sterilflowapp;

import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import org.osmdroid.views.MapView;
import org.osmdroid.views.overlay.infowindow.MarkerInfoWindow;

//https://code.google.com/archive/p/osmbonuspack/wikis/Tutorial_2.wiki
public class CustomInfoWindow extends MarkerInfoWindow {
    public CustomInfoWindow(MapView mapView){
        super(R.layout.bubble_layout,mapView);
    }

    @Override
    public void onOpen(Object item){
        super.onOpen(item);
        Button btn = (Button)(mView.findViewById(R.id.bubble_moreinfo));
        TextView txtTitle = (TextView) mView.findViewById(R.id.bubble_title);
        TextView txtDescription = (TextView) mView.findViewById(R.id.bubble_description);
        TextView txtSubdescription = (TextView) mView.findViewById(R.id.bubble_subdescription);
        mView.findViewById(R.id.bubble_moreinfo).setVisibility(View.VISIBLE);

        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(mView.getContext(), "Button Clicked", Toast.LENGTH_SHORT).show();
            }
        });

    }

}