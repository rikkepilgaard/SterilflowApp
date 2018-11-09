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
import android.widget.CompoundButton;
import android.widget.ToggleButton;

import org.osmdroid.bonuspack.clustering.RadiusMarkerClusterer;
import org.osmdroid.bonuspack.overlays.GroundOverlay;
import org.osmdroid.tileprovider.tilesource.TileSourceFactory;
import org.osmdroid.util.BoundingBox;
import org.osmdroid.util.GeoPoint;
import org.osmdroid.views.MapController;
import org.osmdroid.views.MapView;
import org.osmdroid.views.overlay.FolderOverlay;
import org.osmdroid.views.overlay.ItemizedIconOverlay;
import org.osmdroid.views.overlay.Marker;
import org.osmdroid.views.overlay.Overlay;
import org.osmdroid.views.overlay.OverlayItem;
import org.osmdroid.views.overlay.Polyline;
import org.osmdroid.views.overlay.gridlines.LatLonGridlineOverlay2;

import java.util.ArrayList;
import java.util.List;

import androidx.fragment.app.Fragment;


public class FragmentTwo extends Fragment {

    private MapView osm;
    private MapController mc;
    MainActivity activity;
    Marker marker;
    ToggleButton toggle;

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
        osm.setBuiltInZoomControls(false);
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

        toggle=view.findViewById(R.id.toggleBT);
        toggle.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    addBuildings();
                } else {
                    //BuildingOverlay.onDetach(osm);
                    osm.getOverlays().remove(BuildingOverlay);
                    //BuildingOverlay.removeAllItems();
                    osm.invalidate();
                }
            }
        });

        return view;
    }

    //FolderOverlay markerOverlay;
    CustomCluster radi;

    public void addMarker(ArrayList<BufferZone> bufferZones){

        osm.getOverlays().clear();
      //  markerOverlay = new FolderOverlay();
        radi=new CustomCluster(getContext());
        radi.setRadius(80);
        radi.setBufferZoneList(bufferZones);

        if(toggle.isChecked()){addBuildings();}



        activity = (MainActivity) getActivity();

        for (final BufferZone i: bufferZones){
            marker = new Marker(osm);
            boolean expired = false;
            //Resize icon and set number of wagons inside icon
            if(i.getWagonList() != null) {

                for (TrackEvent event : i.getWagonList()){
                    if(event.isExpired()){
                        expired = true;
                    }
                }
                marker.setIcon(createMarkerIcon(i.getWagonList().size(),expired));
            }
            else{marker.setIcon(createMarkerIcon(0,false));}

            marker.setAnchor(Marker.ANCHOR_CENTER, Marker.ANCHOR_BOTTOM);
            marker.setPosition(new GeoPoint(Double.parseDouble(i.getLatitude()),Double.parseDouble(i.getLongitude())));

            //Set infowindow with button
            marker.setInfoWindow(new CustomInfoWindow(osm,activity.getApplicationContext()));
            marker.setTitle(i.getName());
            marker.setSnippet(i.getLocationName());
            marker.setSubDescription("Se vogne");
            marker.setPanToView(false);
            radi.add(marker);

            //markerOverlay.add(marker);

        }
        //osm.getOverlays().add(markerOverlay);
        osm.getOverlays().add(radi);



        //radi.setIcon(createClusterIcon());
        osm.invalidate();

    }

    ItemizedIconOverlay BuildingOverlay;

    public void addBuildings(){
        Building b = new Building();
        ArrayList<Building> bList = b.getBuildings(activity.getApplicationContext());
        final ArrayList<OverlayItem> items = new ArrayList<OverlayItem>();

        for (Building i: bList) {
            OverlayItem buildingOverlayItem = new OverlayItem("", "", new GeoPoint(i.getLatitude(),i.getLongitude()));
        buildingOverlayItem.setMarker(createTextBitmap(i.getName()));
        items.add(buildingOverlayItem);


        }
        BuildingOverlay = new ItemizedIconOverlay<OverlayItem>(items,null,getContext());
        osm.getOverlays().add(BuildingOverlay);
        osm.invalidate();
    }


    public Drawable createMarkerIcon(int numberWagons, boolean isExpired){
        Bitmap imageBitmap;


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
        Drawable marker = new BitmapDrawable(this.getResources(), resizedBitmap);
        return marker;
    }

    public Drawable createTextBitmap(String text){
            Paint paint = new Paint(Paint.ANTI_ALIAS_FLAG);
            paint.setTextSize(18);
            paint.setTypeface(Typeface.DEFAULT_BOLD);
            paint.setColor(Color.BLACK);
            paint.setTextAlign(Paint.Align.LEFT);
            float baseline = -paint.ascent(); // ascent() is negative
            int width = (int) (paint.measureText(text) + 0.75f); // round
            int height = (int) (baseline + paint.descent() + 0.5f);
            Bitmap image = Bitmap.createBitmap(50, 18, Bitmap.Config.ARGB_8888);
            Canvas canvas = new Canvas(image);
            canvas.drawText(text, 0, baseline, paint);
            Drawable textMarker = new BitmapDrawable(this.getResources(), image);
            return textMarker;
        }

        public void closeInfoWindows(){
            CustomInfoWindow.closeAllInfoWindowsOn(osm);
        }

        public Bitmap createClusterIcon(){
            Drawable clusterIconD = getResources().getDrawable(R.drawable.marker_cluster);
            Bitmap clusterIcon = ((BitmapDrawable)clusterIconD).getBitmap();
            Bitmap resizedBitmap = Bitmap.createScaledBitmap(clusterIcon, 80, 80, false);




            return resizedBitmap;
        }


}
