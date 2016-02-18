package ca.delilaheve.timetable;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;

import ca.delilaheve.timetable.fragment.SettingsFragment;

public class SettingsActivity extends AppCompatActivity {

    private SettingsFragment settingsFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_app_bar);

        // Set toolbar as Action bar
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        settingsFragment = new SettingsFragment();
        getSupportFragmentManager().beginTransaction().replace(R.id.contentFrame, settingsFragment).commit();
    }
}
