package gr.aueb.dsp.distributedsystemsproject;


import android.content.Intent;
import android.support.v4.app.FragmentActivity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity implements BindActivity{

    Intent map;
    RetObj result;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ActionBar actionBar = getSupportActionBar();
        actionBar.setIcon(R.mipmap.ic_launcher);
        actionBar.setTitle("DS Client");
        actionBar.setDisplayUseLogoEnabled(true);
        actionBar.setDisplayShowHomeEnabled(true);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        getMenuInflater().inflate(R.menu.main, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch(item.getItemId()){
            case R.id.app:
                recommendation();
                break;
            case R.id.team:
                startActivity(new Intent(this, TeamActivity.class));
                break;
            default:
                // refresh.
                //refresh
                try{
                    finish();
                    startActivity(getIntent());
                }catch (Exception e){
                    Toast.makeText(getApplicationContext(), "Error: refresh failed." + e, Toast.LENGTH_SHORT).show();
                }

        }
        return super.onOptionsItemSelected(item);
    }

    private void recommendation() {
        Client client = new Client();
        client.bind = this;
        client.execute(100, 40.759534, -73.991765, 10, 1.0, "10.0.2.2", 4200);
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

    @Override
    public void bind(RetObj ret) {
        result = ret;
        System.out.println(result);
        if(result != null){
            postExecute();
        }
    }

    private void postExecute() {
        ArrayList<POI> recommendation = result.getRecommendation();
        double lat = 40.759534;
        double lng = -73.991765;
        double range = 1.0;
        map = new Intent(getApplicationContext(), MapsActivity.class);
        map.putExtra("ArrayList<POI>", recommendation);
        map.putExtra("Latitude", lat);
        map.putExtra("Longitude", lng);
        map.putExtra("Radius", range);
        startActivity(map);
    }
}
