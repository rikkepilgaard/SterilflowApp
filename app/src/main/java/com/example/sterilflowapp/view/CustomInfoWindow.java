package com.example.sterilflowapp.view;

import android.content.Context;
import android.content.Intent;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.example.sterilflowapp.R;

import org.osmdroid.views.MapView;
import org.osmdroid.views.overlay.infowindow.MarkerInfoWindow;

import androidx.localbroadcastmanager.content.LocalBroadcastManager;

import static com.example.sterilflowapp.ConstantValues.ACTION_CHANGE_TAB;
import static com.example.sterilflowapp.ConstantValues.EXTRA_BUFFERZONE;

//https://code.google.com/archive/p/osmbonuspack/wikis/Tutorial_2.wiki
public class CustomInfoWindow extends MarkerInfoWindow {

private Context context;
    CustomInfoWindow(MapView mapView, Context context){
        super(R.layout.bubble_layout,mapView);
        this.context = context;
    }

    @Override
    public void onOpen(Object item){
        super.onOpen(item);
        Button btn = (mView.findViewById(R.id.bubble_moreinfo));
        final TextView txtTitle = mView.findViewById(R.id.bubble_title);
        mView.findViewById(R.id.bubble_moreinfo).setVisibility(View.VISIBLE);
        btn.setBackgroundResource(R.drawable.moreinfo);

        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String bufferName = txtTitle.getText().toString();
                Intent broadcastIntent = new Intent();
                broadcastIntent.setAction(ACTION_CHANGE_TAB);
                broadcastIntent.putExtra(EXTRA_BUFFERZONE,bufferName);
                LocalBroadcastManager.getInstance(context).sendBroadcast(broadcastIntent);

        }

    });

}
}