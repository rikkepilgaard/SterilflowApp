package com.example.sterilflowapp;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Typeface;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import org.osmdroid.tileprovider.tilesource.TileSourceFactory;
import org.osmdroid.util.BoundingBox;
import org.osmdroid.util.GeoPoint;
import org.osmdroid.views.MapController;
import org.osmdroid.views.MapView;
import org.osmdroid.views.overlay.Marker;

import java.util.ArrayList;

import androidx.fragment.app.Fragment;


public class FragmentTwo extends Fragment {

    private MapView osm;
    private MapController mc;
    MainActivity activity;
    private ArrayList<BufferZone> bufferZones;
    Marker marker;
    public FragmentTwo() {
        // Required empty public constructor
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_fragment_two, container, false);
        osm=(MapView) view.findViewById(R.id.mapview);
        osm.setTileSource(TileSourceFactory.MAPNIK);
        osm.setBuiltInZoomControls(true);
        osm.setMultiTouchControls(true);
        mc=(MapController) this.osm.getController();
        mc.setZoom(17);
        osm.setMinZoomLevel((double) 16);
        osm.setMaxZoomLevel((double) 20);
        GeoPoint center = new GeoPoint(56.190671, 10.170353);
        mc.setCenter(center);
        BoundingBox box = new BoundingBox(56.196023, 10.183009,56.185341, 10.161220);
        osm.setScrollableAreaLimitDouble(box);
        osm.setMapOrientation(21.05f);
        //addMarker();

        // Inflate the layout for this fragment
        return view;
    }
    public void addMarker(ArrayList<BufferZone> bufferZones){
        osm.getOverlays().clear();
        activity = (MainActivity) getActivity();
        //bufferZones = activity.getBufferZoneList();

        for (final BufferZone i: bufferZones){
            marker = new Marker(osm);
            //Resize icon and set number of wagons inside icon
            if(i.getWagonList() != null) {
                marker.setIcon(createMarkerIcon(i.getWagonList().size()));
            }
            else{marker.setIcon(createMarkerIcon(0));}

            marker.setAnchor(Marker.ANCHOR_CENTER, Marker.ANCHOR_BOTTOM);
            marker.setPosition(new GeoPoint(Double.parseDouble(i.getLatitude()),Double.parseDouble(i.getLongitude())));

            //Set infowindow with button
            marker.setInfoWindow(new CustomInfoWindow(osm,activity));
            marker.setTitle(i.getName());
            marker.setSnippet(i.getLocationName());
            marker.setSubDescription("Se vogne");
            marker.setPanToView(false);
            osm.getOverlays().add(marker);
            osm.invalidate();
        }
    }


    public Drawable createMarkerIcon(int numberWagons){
        Bitmap imageBitmap;

        //Dette skal bruges til tidsgrænserne!!
        if(numberWagons>=2){
        imageBitmap = BitmapFactory.decodeResource(getResources(),getResources().getIdentifier("bluemarker", "drawable", getActivity().getPackageName()));}
        else{imageBitmap = BitmapFactory.decodeResource(getResources(),getResources().getIdentifier("redmarker", "drawable", getActivity().getPackageName()));}

        Bitmap resizedBitmap = Bitmap.createScaledBitmap(imageBitmap, 80, 80, false);

        Paint paint = new Paint();
        paint.setStyle(Paint.Style.FILL);
        paint.setColor(Color.WHITE);
        paint.setTypeface(Typeface.create(Typeface.DEFAULT, Typeface.BOLD));
        paint.setTextSize(40);
        Canvas canvas = new Canvas(resizedBitmap);
        if(numberWagons>9){
            canvas.drawText(Integer.toString(numberWagons), 15, resizedBitmap.getHeight()/1.5f, paint);}
        else{
            canvas.drawText(Integer.toString(numberWagons), 25, resizedBitmap.getHeight()/1.5f, paint);
        }
        Drawable marker = new BitmapDrawable(this.getResources(), resizedBitmap);
        return marker;
    }
}
