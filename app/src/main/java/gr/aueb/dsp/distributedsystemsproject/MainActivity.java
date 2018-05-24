package gr.aueb.dsp.distributedsystemsproject;


import android.content.Intent;
import android.support.v4.app.FragmentActivity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity implements BindActivity{

    private Intent map;
    private RetObj result;
    private EditText userId, lat, lng, k, range, ip, port;
    private Button submitButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ActionBar actionBar = getSupportActionBar();
        actionBar.setIcon(R.mipmap.ic_launcher);
        actionBar.setTitle("DS Client");
        actionBar.setDisplayUseLogoEnabled(true);
        actionBar.setDisplayShowHomeEnabled(true);

        userId = findViewById(R.id.textUserId);
        lat = findViewById(R.id.textLatitude);
        lng = findViewById(R.id.textLongtitude);
        k = findViewById(R.id.textK);
        range = findViewById(R.id.textRange);
        ip = findViewById(R.id.textIp);
        port = findViewById(R.id.textPort);
        submitButton = findViewById(R.id.submitButton);

        submitButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                recommendation();
            }
        });
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
                startActivity(map);
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

    // The method that calls the AsyncTask
    private void recommendation() {
        // Initiate Client
        Client client = new Client();
        // Bind Activity
        client.bind = this;
        client.execute(userId.getText().toString(), lat.getText().toString(), lng.getText().toString(), k.getText().toString(), range.getText().toString(), ip.getText().toString(), port.getText().toString());

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

    // Get the response from the AsyncTask
    @Override
    public void bind(RetObj ret) {
        result = ret;
        System.out.println(result.getRecommendation().toString());
        if(result != null){
            // If RetObj not null execute
            postExecute();
        }
        //TODO CREATE ALERT THAT THE CONNECTION TO MASTER FAILED
    }

    // Initiate new Intent to MapActivity
    private void postExecute() {
        ArrayList<POI> recommendation = result.getRecommendation();
        map = new Intent(getApplicationContext(), MapsActivity.class);
        map.putExtra("ArrayList<POI>", recommendation);
        map.putExtra("Latitude", lat.getText().toString());
        map.putExtra("Longitude", lng.getText().toString());
        map.putExtra("Radius", range.getText().toString());
        startActivity(map);
    }
}
