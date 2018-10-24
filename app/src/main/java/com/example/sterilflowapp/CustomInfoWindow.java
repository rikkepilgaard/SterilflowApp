package com.example.sterilflowapp;

import android.app.Activity;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.content.Context;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import org.osmdroid.views.MapView;
import org.osmdroid.views.overlay.infowindow.MarkerInfoWindow;

import androidx.fragment.app.Fragment;
import androidx.viewpager.widget.ViewPager;

//https://code.google.com/archive/p/osmbonuspack/wikis/Tutorial_2.wiki
public class CustomInfoWindow extends MarkerInfoWindow {
    Activity activity;

    public CustomInfoWindow(MapView mapView,Activity activity){
        super(R.layout.bubble_layout,mapView);
        this.activity=activity;
    }

    @Override
    public void onOpen(Object item){
        super.onOpen(item);
        Button btn = (Button)(mView.findViewById(R.id.bubble_moreinfo));
        TextView txtTitle = (TextView) mView.findViewById(R.id.bubble_title);
        TextView txtDescription = (TextView) mView.findViewById(R.id.bubble_description);
        TextView txtSubdescription = (TextView) mView.findViewById(R.id.bubble_subdescription);
        mView.findViewById(R.id.bubble_moreinfo).setVisibility(View.VISIBLE);
        btn.setBackgroundResource(R.drawable.moreinfo);

        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                ViewPager viewPager = (ViewPager) activity.findViewById(R.id.viewPager);
                viewPager.setCurrentItem(0);

        };

    });

}
}