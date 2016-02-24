package ca.delilaheve.timetable;

import android.os.Bundle;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;

import ca.delilaheve.timetable.data.Event;
import ca.delilaheve.timetable.fragment.EditEventFragment;
import ca.delilaheve.timetable.fragment.ViewEventFragment;

public class EventActivity extends AppCompatActivity {

    private EditEventFragment editEventFragment;
    private ViewEventFragment viewEventFragment;

    private Event event;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_app_bar);

        // Get event from Intent
        Bundle extras = getIntent().getExtras();
        if(extras.containsKey("")) {
            // Do stuff

        }

        // Set toolbar as Action bar
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        editEventFragment = new EditEventFragment();
        viewEventFragment = new ViewEventFragment();

        getSupportFragmentManager().beginTransaction().replace(R.id.contentFrame, viewEventFragment).commit();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();

        switch (item.getItemId()) {
            case R.id.action_edit:
                transaction.replace(R.id.contentFrame, editEventFragment);
                transaction.commit();
                return true;
            case R.id.action_done:
                transaction.replace(R.id.contentFrame, viewEventFragment);
                transaction.commit();
                return true;
        }

        return super.onOptionsItemSelected(item);
    }

    private void fillEvent() {
        // Fill layout with event details.
    }
}
