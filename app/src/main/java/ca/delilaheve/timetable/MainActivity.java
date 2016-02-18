package ca.delilaheve.timetable;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;

import ca.delilaheve.timetable.dialog.AddEventDialog;
import ca.delilaheve.timetable.fragment.DayViewFragment;
import ca.delilaheve.timetable.fragment.NavDrawerFragment;
import ca.delilaheve.timetable.fragment.SettingsFragment;
import ca.delilaheve.timetable.fragment.WeekViewFragment;

public class MainActivity extends AppCompatActivity {

    public static final int MODE_DAY = 0;
    public static final int MODE_WEEK = 1;

    private WeekViewFragment weekFragment;
    private DayViewFragment dayFragment;

    private DrawerLayout drawer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Set toolbar as Action bar
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        // Set Drawer toggle
        drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        // Get fragment manager
        FragmentManager manager = getSupportFragmentManager();

        // Add Nav drawer
        manager.beginTransaction().replace(R.id.leftDrawer, new NavDrawerFragment()).commit();

        weekFragment = new WeekViewFragment();
        dayFragment = new DayViewFragment();

        // Check Preferences
        SharedPreferences preferences = getSharedPreferences(SettingsFragment.prefsFile, 0);
        int viewMode = preferences.getInt(SettingsFragment.KEY_DEFAULT_VIEW, MODE_WEEK);

        // Add Fragment
        FragmentTransaction transaction = manager.beginTransaction();
        switch (viewMode) {
            case MODE_DAY:
                transaction.replace(R.id.contentFrame, dayFragment);
                break;
            case MODE_WEEK:
            default:
                transaction.replace(R.id.contentFrame, weekFragment);
                break;
        }
        transaction.commit();
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_add_event:
                AddEventDialog dialog = new AddEventDialog(this);
                dialog.show();
                return true;
        }

        return super.onOptionsItemSelected(item);
    }

    public void setViewMode(int viewMode) {
        FragmentManager manager = getSupportFragmentManager();

        switch (viewMode) {
            case MODE_DAY:
                manager.beginTransaction().replace(R.id.contentFrame, dayFragment).commit();
                break;
            case MODE_WEEK:
                manager.beginTransaction().replace(R.id.contentFrame, weekFragment).commit();
                break;
        }
    }

    public void openDrawer() {
        drawer.openDrawer(GravityCompat.START);
    }

    public void closeDrawer() {
        drawer.closeDrawer(GravityCompat.START);
    }

    private void addUserEvent() {
        // Add a user event
    }
}
