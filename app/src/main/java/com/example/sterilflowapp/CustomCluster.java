package com.example.sterilflowapp;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;

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

    protected int mMaxClusteringZoomLevel = 20;
    protected int mRadiusInPixels = 90;
    protected double mRadiusInMeters;
    protected Paint mTextPaint;
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




    /** If you want to change the default text paint (color, size, font) */
    public Paint getTextPaint(){
        return mTextPaint;
    }

    /** Set the radius of clustering in pixels. Default is 100px. */
    public void setRadius(int radius){
        mRadiusInPixels = radius;
    }

    /** Set max zoom level with clustering. When zoom is higher or equal to this level, clustering is disabled.
     * You can put a high value to disable this feature. */
    public void setMaxClusteringZoomLevel(int zoom){
        mMaxClusteringZoomLevel = zoom;
    }



    /** Radius-Based clustering algorithm */
    @Override public ArrayList<StaticCluster> clusterer(MapView mapView) {

        ArrayList<StaticCluster> clusters = new ArrayList<StaticCluster>();
        convertRadiusToMeters(mapView);

        mClonedMarkers = new ArrayList<Marker>(mItems); //shallow copy
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

    MarkerInfoWindow markerInfoWindow;

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
                    if(j.getWagonList()!=null)
                    wagonNumber=wagonNumber+j.getWagonList().size();
                    if(j.getWagonList() != null) {

                        for (TrackEvent event : j.getWagonList()){
                            if(event.isExpired()){
                                expiredTrolley = true;
                            }
                        }
                    }
                }

            }

        }
        Drawable clusterIconD;


        if(expiredTrolley){
        clusterIconD = ctx.getResources().getDrawable(R.drawable.redmarker);}
        else{
            clusterIconD = ctx.getResources().getDrawable(R.drawable.bluemarker);}

        Bitmap clusterIcon = ((BitmapDrawable) clusterIconD).getBitmap();
        Bitmap resizedBitmap = Bitmap.createScaledBitmap(clusterIcon, 90, 90, false);



        //Bitmap finalIcon = Bitmap.createBitmap(mClusterIcon.getWidth(), mClusterIcon.getHeight(), mClusterIcon.getConfig());
        Canvas iconCanvas = new Canvas(resizedBitmap);
        iconCanvas.drawBitmap(clusterIcon, 0, 0, null);


        String text = Integer.toString(wagonNumber);
        int textHeight = (int) (mTextPaint.descent() + mTextPaint.ascent());
        iconCanvas.drawText(text,
                mTextAnchorU * resizedBitmap.getWidth(),
                mTextAnchorV * resizedBitmap.getHeight() - textHeight / 2,
                mTextPaint);
        m.setIcon(new BitmapDrawable(mapView.getContext().getResources(), resizedBitmap));
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
