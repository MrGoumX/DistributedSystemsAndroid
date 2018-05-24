package gr.aueb.dsclient.dsclient;


import android.content.Intent;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;

import java.util.ArrayList;

public class MainActivity extends FragmentActivity{

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Intent map = new Intent(getApplicationContext(), MapsActivity.class);
        map.putExtra("ArrayList<POI>", new ArrayList<POI>());
        map.putExtra("Latitude", 37.9940805);
        map.putExtra("Longitude", 23.7302467);
        map.putExtra("Radius", 2.0);
        startActivity(map);
    }

    private boolean isInt(String num){
        return num.matches("\\d+");
    }

    private boolean isPort(String port){
        return port.matches("\\d{1,5}");
    }

    private boolean isDouble(String num){
        return num.matches("\\d+[.,]\\d+");
    }

    private boolean isIpV4(String ip){
        return ip.matches("(\\d{1,3}.){3}\\d{1,3}");
    }

}
