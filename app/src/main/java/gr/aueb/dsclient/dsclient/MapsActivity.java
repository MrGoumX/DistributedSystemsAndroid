package gr.aueb.dsclient.dsclient;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Typeface;
import android.net.Uri;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.Circle;
import com.google.android.gms.maps.model.CircleOptions;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import java.util.ArrayList;

public class MapsActivity extends FragmentActivity implements OnMapReadyCallback {
    private GoogleMap mMap;
    private ArrayList<POI> recommendation = new ArrayList<>(); // ArrayList that contains the POIS
    private ArrayList<LatLng> positions = new ArrayList<>(); // ArrayList that contains all the POIS positions
    private ArrayList<Marker> markers = new ArrayList<>(); // ArrayList that contains all the POIS markers
    private LatLng curr_pos; // user current position
    private double lat, lng; // user lat, lng because LatLng not Serializable
    private double radius; // user defined range

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        //Default Android Maps Activity Start Lines
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
        //Get recommendation arraylist, lat, lng, current position and radius from previous intent
        recommendation = (ArrayList<POI>) getIntent().getSerializableExtra("ArrayList<POI>");
        lat = (Double) getIntent().getSerializableExtra("Latitude");
        lng = (Double) getIntent().getSerializableExtra("Longitude");
        curr_pos = new LatLng(lat, lng);
        radius = (Double) getIntent().getSerializableExtra("Radius");
        //Multiply radius * 1000 to convert to meters
        radius *= 1000;
    }


    /**
     * Manipulates the map once available.
     * This callback is triggered when the map is ready to be used.
     * This is where we can add markers or lines, add listeners or move the camera. In this case,
     * we just add a marker near Sydney, Australia.
     * If Google Play services is not installed on the device, the user will be prompted to install
     * it inside the SupportMapFragment. This method will only be triggered once the user has
     * installed Google Play services and returned to the app.
     */
    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;

        //Create circle based on radius given and zoom in to that specific radius
        Circle circle = mMap.addCircle(new CircleOptions().center(curr_pos).radius(radius).strokeColor(Color.BLUE).strokeWidth(10).visible(true));
        //Get Zoom and calculate animated zoom(anim_zoom)
        float zoom = getZoom(circle);
        float anim_zoom = zoom + 5;
        //Move the camera
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(curr_pos, anim_zoom));
        mMap.animateCamera(CameraUpdateFactory.zoomTo(zoom), 2000, null);
        //Create marker options for all markers
        MarkerOptions options = new MarkerOptions();
        //Customize marker
        options.position(curr_pos);
        options.title("You are here");
        options.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_AZURE));
        Marker here = mMap.addMarker(options);
        here.showInfoWindow();
        //For recommended pois add them to the arraylist as new LatLng Objects
        for(int i = 0; i < recommendation.size(); i++){
            double p_lat = recommendation.get(i).getLatitude();
            double p_lng = recommendation.get(i).getLongitude();
            positions.add(new LatLng(p_lat, p_lng));
        }
        //Create markers based on positions and add them to the map and the arraylist
        for(int i = 0; i < positions.size(); i++){
            options.position(positions.get(i));
            options.title(recommendation.get(i).getName());
            options.snippet("Distance: " + Math.round(recommendation.get(i).getDistance())+" meters\n" + "POI: " + recommendation.get(i).getR_id() + "\n" + "Category: " + recommendation.get(i).getCat() + "\nTap to view photo");
            options.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_RED));
            markers.add(mMap.addMarker(options));
        }

        //Create Custom Info Window Adapter
        mMap.setInfoWindowAdapter(new GoogleMap.InfoWindowAdapter() {

            //Create Custom Info Window
            @Override
            public View getInfoWindow(Marker marker) {
                return null;
            }

            //Create Custom Info Window Contents
            @Override
            public View getInfoContents(Marker marker) {
                //Define Layout and Contex
                //Get Current Context
                Context mContext = getApplicationContext();
                //Create Layout and customize orientation
                LinearLayout info = new LinearLayout(mContext);
                info.setOrientation(LinearLayout.VERTICAL);
                //Customize title in info window
                TextView title = new TextView(mContext);
                title.setTextColor(Color.BLACK);
                title.setGravity(Gravity.CENTER);
                title.setTypeface(null, Typeface.BOLD);
                title.setText(marker.getTitle());
                //Customize snippet in info window
                TextView snippet = new TextView(mContext);
                snippet.setTextColor(Color.GRAY);
                snippet.setText(marker.getSnippet());
                //Add title and snippet to InfoContents
                info.addView(title);
                info.addView(snippet);
                //Return InfoContents
                return info;
            }
        });

        //Method that opens new browser to show photo to user when he selects a poi snippet
        mMap.setOnInfoWindowClickListener(new GoogleMap.OnInfoWindowClickListener() {
            @Override
            public void onInfoWindowClick(Marker marker) {
                // Find the poi selected and get the link
                String title = marker.getTitle();
                String link = null;
                for(int i = 0; i < markers.size(); i++){
                    if(title.equalsIgnoreCase(recommendation.get(i).getName())){
                        link = recommendation.get(i).getPhoto();
                    }
                }
                // If the link exists parse URL and open browser
                if(link!=null){
                    Intent browser = new Intent(Intent.ACTION_VIEW, Uri.parse(link));
                    startActivity(browser);
                }
                // If not show alert message
                else{
                    // Create Alert, customize it and show it
                    AlertDialog alert = new AlertDialog.Builder(MapsActivity.this).create();
                    alert.setTitle("Error");
                    alert.setMessage("Photo does not exist");
                    alert.setButton(AlertDialog.BUTTON_NEUTRAL, "OK", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.dismiss();
                        }
                    });
                    alert.show();
                }
            }
        });
    }


    //Finds the exact zoom leven to zoom in
    private float getZoom(Circle circle){
        //Initiate zoom level to 0
        float zoom = 0F;
        //If circle not null create the circle
        if(circle!=null){
            double radius = circle.getRadius();
            double scale = radius/500;
            zoom = (float)(14.85-Math.log(scale)/Math.log(2));
        }
        //return zoom + .5 for error
        return zoom + .5F;
    }


}
