package com.example.sterilflowapp.view;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;

import com.example.sterilflowapp.R;
import com.example.sterilflowapp.model.BufferZone;

import org.osmdroid.bonuspack.clustering.MarkerClusterer;
import org.osmdroid.bonuspack.clustering.StaticCluster;
import org.osmdroid.util.BoundingBox;
import org.osmdroid.util.GeoPoint;
import org.osmdroid.views.MapView;
import org.osmdroid.views.overlay.Marker;
import org.osmdroid.views.overlay.infowindow.MarkerInfoWindow;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

//Klassen er kopieret fra:https://github.com/MKergall/osmbonuspack/blob/master/OSMBonusPack/src/main/java/org/osmdroid/bonuspack/clustering/RadiusMarkerClusterer.java

public class CustomCluster extends MarkerClusterer {

    private int mMaxClusteringZoomLevel = 20;
    private int mRadiusInPixels = 90;
    private double mRadiusInMeters;
    private Paint mTextPaint;
    private ArrayList<Marker> mClonedMarkers;
    private ArrayList<BufferZone> bufferZones;

    /** cluster icon anchor */
    public float mAnchorU = Marker.ANCHOR_CENTER, mAnchorV = Marker.ANCHOR_CENTER;
    /** anchor point to draw the number of markers inside the cluster icon */
    public float mTextAnchorU = Marker.ANCHOR_CENTER, mTextAnchorV = Marker.ANCHOR_CENTER;

    Context ctx;
    public CustomCluster(Context ctx) {
        super();
        mTextPaint = new Paint();
        mTextPaint.setColor(Color.WHITE);
        mTextPaint.setTextSize(15 * ctx.getResources().getDisplayMetrics().density);
        mTextPaint.setFakeBoldText(true);
        mTextPaint.setTextAlign(Paint.Align.CENTER);
        mTextPaint.setAntiAlias(true);
        this.ctx=ctx;
        Drawable clusterIconD = ctx.getResources().getDrawable(R.drawable.bluemarker);
        Bitmap clusterIcon = ((BitmapDrawable) clusterIconD).getBitmap();
        Bitmap resizedBitmap = Bitmap.createScaledBitmap(clusterIcon, 80, 80, false);
        setIcon(resizedBitmap);
    }


    /** Radius-Based clustering algorithm */
    @Override public ArrayList<StaticCluster> clusterer(MapView mapView) {

        ArrayList<StaticCluster> clusters = new ArrayList<>();
        convertRadiusToMeters(mapView);

        mClonedMarkers = new ArrayList<>(mItems); //shallow copy
        while (!mClonedMarkers.isEmpty()) {
            Marker m = mClonedMarkers.get(0);
            StaticCluster cluster = createCluster(m, mapView);
            clusters.add(cluster);
        }
        return clusters;
    }

    private StaticCluster createCluster(Marker m, MapView mapView) {

        GeoPoint clusterPosition = m.getPosition();

        StaticCluster cluster = new StaticCluster(clusterPosition);
        cluster.add(m);

        mClonedMarkers.remove(m);

        if (mapView.getZoomLevelDouble() > mMaxClusteringZoomLevel) {
            //above max level => block clustering:

            return cluster;
        }

        Iterator<Marker> it = mClonedMarkers.iterator();
        while (it.hasNext()) {
            Marker neighbour = it.next();
            double distance = clusterPosition.distanceToAsDouble(neighbour.getPosition());
            if (distance <= mRadiusInMeters) {
                cluster.add(neighbour);
                it.remove();
            }
        }

        return cluster;
    }

    public void setBufferZoneList(ArrayList<BufferZone> buff){
        bufferZones=buff; }

    private MarkerInfoWindow markerInfoWindow;

    @Override public Marker buildClusterMarker(StaticCluster cluster, MapView mapView) {

        List<String> buffernames = new ArrayList<>();

        for(int i=0;i<cluster.getSize();i++){
            buffernames.add(cluster.getItem(i).getTitle());
        }


        markerInfoWindow= new MarkerInfoWindow(R.layout.cluster_layout,mapView);
        Marker m = new Marker(mapView);
        m.setPosition(cluster.getPosition());
        m.setInfoWindow(markerInfoWindow);
        m.setTitle(ctx.getString(R.string.infowindowCluster));
        m.setAnchor(mAnchorU, mAnchorV);
        m.setPanToView(false);
        int wagonNumber = 0;
        boolean expiredTrolley=false;
        for (String i: buffernames)
        {
            for (BufferZone j: bufferZones)
            {
                if(i.equals(j.getName())){
                    if(j.getTrolleyList()!=null)
                    wagonNumber=wagonNumber+j.getTrolleyList().size();
                    if(j.containsExpiredWagon()){
                        expiredTrolley = true;
                    }
                }

            }

        }

        Bitmap clusterIconD;

        if(expiredTrolley){

            clusterIconD=BitmapFactory.decodeResource(ctx.getResources(),ctx.getResources().getIdentifier("redmarker", "drawable", ctx.getPackageName()));}
        else{
            clusterIconD=BitmapFactory.decodeResource(ctx.getResources(),ctx.getResources().getIdentifier("bluemarker", "drawable", ctx.getPackageName()));}

        Bitmap resizedBitmap = Bitmap.createScaledBitmap(clusterIconD, 100, 100, false);

        Canvas iconCanvas = new Canvas(resizedBitmap);
        iconCanvas.drawBitmap(resizedBitmap, 0, 0, null);

        String text = Integer.toString(wagonNumber);
        int textHeight = (int) (mTextPaint.descent() + mTextPaint.ascent());
        iconCanvas.drawText(text,
                mTextAnchorU * resizedBitmap.getWidth(),
                mTextAnchorV * resizedBitmap.getHeight() - textHeight / 2,
                mTextPaint);
        m.setIcon(new BitmapDrawable(ctx.getResources(), resizedBitmap));
        return m;
    }

    @Override public void renderer(ArrayList<StaticCluster> clusters, Canvas canvas, MapView mapView) {
        for (StaticCluster cluster : clusters) {
            if (cluster.getSize() == 1) {
                //cluster has only 1 marker => use it as it is:
                cluster.setMarker(cluster.getItem(0));
            } else {
                //only draw 1 Marker at Cluster center, displaying number of Markers contained
                Marker m = buildClusterMarker(cluster, mapView);
                cluster.setMarker(m);
            }
        }
    }

    private void convertRadiusToMeters(MapView mapView) {

        Rect mScreenRect = mapView.getIntrinsicScreenRect(null);

        int screenWidth = mScreenRect.right - mScreenRect.left;
        int screenHeight = mScreenRect.bottom - mScreenRect.top;

        BoundingBox bb = mapView.getBoundingBox();

        double diagonalInMeters = bb.getDiagonalLengthInMeters();
        double diagonalInPixels = Math.sqrt(screenWidth * screenWidth + screenHeight * screenHeight);
        double metersInPixel = diagonalInMeters / diagonalInPixels;

        mRadiusInMeters = mRadiusInPixels * metersInPixel;
    }

}
