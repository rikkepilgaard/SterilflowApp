package com.example.sterilflowapp;

import android.os.Bundle;
import androidx.fragment.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import org.osmdroid.tileprovider.tilesource.TileSourceFactory;
import org.osmdroid.util.BoundingBox;
import org.osmdroid.util.GeoPoint;
import org.osmdroid.views.MapController;
import org.osmdroid.views.MapView;


public class FragmentTwo extends Fragment {

    TextView textView;
    private MapView osm;
    private MapController mc;
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



        // Inflate the layout for this fragment
        return view;
    }

}
