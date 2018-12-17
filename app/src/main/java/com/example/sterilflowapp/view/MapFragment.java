package com.example.sterilflowapp.view;

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
import android.widget.CompoundButton;
import android.widget.ToggleButton;

import com.example.sterilflowapp.R;
import com.example.sterilflowapp.model.BufferZone;
import com.example.sterilflowapp.model.Building;

import org.osmdroid.tileprovider.tilesource.TileSourceFactory;
import org.osmdroid.util.BoundingBox;
import org.osmdroid.util.GeoPoint;
import org.osmdroid.views.MapController;
import org.osmdroid.views.MapView;
import org.osmdroid.views.overlay.ItemizedIconOverlay;
import org.osmdroid.views.overlay.Marker;
import org.osmdroid.views.overlay.Overlay;
import org.osmdroid.views.overlay.OverlayItem;

import java.util.ArrayList;
import java.util.Iterator;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;


public class MapFragment extends Fragment {

    private MapView osm;
    private MapController mc;
    private ToggleButton toggle;
    private ItemizedIconOverlay BuildingOverlay;
    private ArrayList<Building> bList;

    public MapFragment() {}


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);}

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_fragment_two, container, false);

        //Set default map settings

        osm = view.findViewById(R.id.mapview);
        osm.setTileSource(TileSourceFactory.MAPNIK);
        osm.setBuiltInZoomControls(false);
        osm.setMultiTouchControls(true);
        mc = (MapController)this.osm.getController();

        mc.setZoom(17);
        osm.setMinZoomLevel((double) 16);
        osm.setMaxZoomLevel((double) 20);
        GeoPoint center = new GeoPoint(56.190671, 10.170353);
        mc.setCenter(center);
        BoundingBox box = new BoundingBox(56.196023, 10.183009,56.185341, 10.161220);
        osm.setScrollableAreaLimitDouble(box);
        osm.setMapOrientation(21.05f);

        bList = new ArrayList<>();

        //If screen is rotated, check if building names were shown or not
        if(savedInstanceState!=null){
            if(savedInstanceState.getBoolean("ischecked")){
                addBuildings();
            }
        }

        toggle=view.findViewById(R.id.toggleBT);
        toggle.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    addBuildings();
                } else {
                    osm.getOverlays().remove(BuildingOverlay);
                    osm.invalidate();
                }
            }
        });
        return view;
    }

    //Adds a marker for every bufferzone on the map
    void addMarker(ArrayList<BufferZone> bufferZones){

        osm.getOverlays().clear();
        CustomCluster clusterMarker = new CustomCluster(getContext());
        clusterMarker.setBufferZoneList(bufferZones);

        if(toggle.isChecked()){
            addBuildings();
        }


        for (final BufferZone i: bufferZones){
            Marker marker = new Marker(osm);

            //Resize icon and set number of wagons inside icon
            if(i.getTrolleyList() != null) {
                marker.setIcon(createMarkerIcon(i.getTrolleyList().size(),i.containsExpiredWagon()));
            }
            else{
                marker.setIcon(createMarkerIcon(0,false));}

            marker.setAnchor(Marker.ANCHOR_CENTER, Marker.ANCHOR_BOTTOM);
            marker.setPosition(new GeoPoint(Double.parseDouble(i.getLatitude()),Double.parseDouble(i.getLongitude())));

            //Set infowindow with button
            marker.setInfoWindow(new CustomInfoWindow(osm,getActivity().getApplicationContext()));
            marker.setTitle(i.getName());
            marker.setSnippet(getString(R.string.location)+" "+i.getLocationName());
            marker.setSubDescription(getString(R.string.See_trolleys));
            marker.setPanToView(false);
            clusterMarker.add(marker);

        }
        osm.getOverlays().add(clusterMarker);
        osm.invalidate();

    }

    void setBuildingList(ArrayList<Building> buildingList){this.bList=buildingList;}


    private void addBuildings(){

        final ArrayList<OverlayItem> items = new ArrayList<>();

        //Creates an overlay with building names
        for (Building i: bList) {
            OverlayItem buildingOverlayItem = new OverlayItem("", "", new GeoPoint(i.getLatitude(),i.getLongitude()));
        buildingOverlayItem.setMarker(createTextBitmap(i.getName()));
        items.add(buildingOverlayItem);


        }
        BuildingOverlay = new ItemizedIconOverlay<>(items,null,getContext());
        osm.getOverlays().add(BuildingOverlay);
        osm.invalidate();
    }


    private Drawable createMarkerIcon(int numberWagons, boolean isExpired){
        Bitmap imageBitmap;

        //If isExpired is true (bufferzone contain expired trolley), marker icon is "redmarker". Otherwise
        //marker icon is "bluemarker".
        if(!isExpired){
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
        return new BitmapDrawable(this.getResources(), resizedBitmap);
    }

    //Creates building text
    private Drawable createTextBitmap(String text){
            Paint paint = new Paint(Paint.ANTI_ALIAS_FLAG);
            paint.setTextSize(22);
            paint.setTypeface(Typeface.DEFAULT_BOLD);
            paint.setColor(Color.BLACK);
            paint.setTextAlign(Paint.Align.LEFT);
            float baseline = -paint.ascent(); // ascent() is negative

            Bitmap image = Bitmap.createBitmap(50, 22, Bitmap.Config.ARGB_8888);
            Bitmap resizedBitmap = Bitmap.createScaledBitmap(image, 50, 22, false);
            Canvas canvas = new Canvas(resizedBitmap);
            canvas.drawText(text, 0, baseline, paint);
            return new BitmapDrawable(this.getResources(), resizedBitmap);
        }

        void closeInfoWindows(){
            CustomInfoWindow.closeAllInfoWindowsOn(osm);
        }

    @Override
    public void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);

        outState.putBoolean("ischecked", toggle.isChecked());
    }

    void zoomToSpecificBufferzone(String buffername){
        Iterator<Overlay> iterator = osm.getOverlays().iterator();
        while(iterator.hasNext()){
            Overlay next = iterator.next();
            if (next instanceof CustomCluster){
                for(Marker m: ((CustomCluster) next).getItems()){
                    if(m.getTitle().equals(buffername)){
                        m.showInfoWindow();
                        mc.setZoom(19);
                        mc.setCenter(m.getPosition());
                    }
                }
            }
        }
    }
}
