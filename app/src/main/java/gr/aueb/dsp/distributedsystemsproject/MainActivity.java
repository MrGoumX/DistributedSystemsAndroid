package gr.aueb.dsp.distributedsystemsproject;


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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ActionBar actionBar = getSupportActionBar();
        actionBar.setIcon(R.mipmap.ic_launcher);
        actionBar.setTitle(" DS Client");
        actionBar.setDisplayUseLogoEnabled(true);
        actionBar.setDisplayShowHomeEnabled(true);

        userId = findViewById(R.id.textUserId);
        lat = findViewById(R.id.textLatitude);
        lng = findViewById(R.id.textLongtitude);
        k = findViewById(R.id.textK);
        range = findViewById(R.id.textRange);
        ip = findViewById(R.id.textIp);
        port = findViewById(R.id.textPort);
        Button submitButton = findViewById(R.id.submitButton);

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
                try{
                    startActivity(map);
                }catch (Exception e){
                    Toast.makeText(getApplicationContext(), "Please submit information first and wait for server to response.", Toast.LENGTH_SHORT).show();
                }

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

        try{ // makes input validation.
            Integer.parseInt(userId.getText().toString());
            Integer.parseInt(k.getText().toString());
            Integer.parseInt(port.getText().toString());
            Double.parseDouble(lat.getText().toString());
            Double.parseDouble(lng.getText().toString());
            Double.parseDouble(range.getText().toString());
            if(ip.getText().toString().matches("^(\\d{1,3})\\.(\\d{1,3})\\.(\\d{1,3})\\.(\\d{1,3})$")){
                client.execute(userId.getText().toString(), lat.getText().toString(), lng.getText().toString(), k.getText().toString(), range.getText().toString(), ip.getText().toString(), port.getText().toString());
            }else{
                Toast.makeText(getApplicationContext(), "Error: IP's format isn't valid.", Toast.LENGTH_SHORT).show();
            }

        }catch (Exception e){
            Toast.makeText(getApplicationContext(), "Error: An input field is invalid.", Toast.LENGTH_SHORT).show();
        }
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
    }

    // Initiate new Intent to MapActivity
    private void postExecute() {
        ArrayList<POI> recommendation = result.getRecommendation();
        map = new Intent(getApplicationContext(), MapsActivity.class);
        map.putExtra("ArrayList<POI>", recommendation);
        map.putExtra("Latitude", Double.parseDouble(lat.getText().toString()));
        map.putExtra("Longitude", Double.parseDouble(lng.getText().toString()));
        map.putExtra("Radius", Double.parseDouble(range.getText().toString()));
        startActivity(map);
    }


}
