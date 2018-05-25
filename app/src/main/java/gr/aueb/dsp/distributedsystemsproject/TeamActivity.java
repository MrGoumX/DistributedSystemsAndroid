package gr.aueb.dsp.distributedsystemsproject;

import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.EditText;
import android.widget.MultiAutoCompleteTextView;
import android.widget.TextView;

// TeamActivity prints information about project and developers.
public class TeamActivity extends AppCompatActivity {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_team);

        ActionBar actionBar = getSupportActionBar();
        actionBar.setIcon(R.mipmap.ic_launcher);
        actionBar.setTitle(" DS client");
        actionBar.setDisplayUseLogoEnabled(true);
        actionBar.setDisplayShowHomeEnabled(true);
        actionBar.setDisplayHomeAsUpEnabled(true);

        EditText textTeam = findViewById(R.id.textMult);
        textTeam.setText("Η εφαρμογή DSClient αναπτύχθηκε στα πλαίσια εργασίας στο μάθημα Κατανεμημένα Συστήματα του τμήματος Πληρφοφορικής ΟΠΑ.\n\nDevelopers:\n\t\tΧρήστος Γκούμας\n\t\tΆρης Ζιωτοπουλος\n\t\tΛαμπράκης Κωνσταντίνος\n\t\tΧρήστος Αγγέλου");
        textTeam.setKeyListener(null); // make it uneditable.
    }
}
