package ca.delilaheve.timetable;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.inputmethod.InputMethodManager;
import android.widget.FrameLayout;

import java.util.ArrayList;

import ca.delilaheve.timetable.data.User;
import ca.delilaheve.timetable.database.Database;
import ca.delilaheve.timetable.dialog.AddEventDialog;
import ca.delilaheve.timetable.fragment.DayViewFragment;
import ca.delilaheve.timetable.fragment.NavDrawerFragment;
import ca.delilaheve.timetable.fragment.PasswordFragment;
import ca.delilaheve.timetable.fragment.SettingsFragment;
import ca.delilaheve.timetable.fragment.WeekFragment;

public class MainActivity extends AppCompatActivity {

    private static Boolean debug = false;

    private String ACCOUNT_ADMIN = "Admin";
    private String PASS_ADMIN = "admin";

    public static int userID = -1;

    private static Boolean unlocked = false;

    public static final int MODE_DAY = 0;
    public static final int MODE_WEEK = 1;

    private PasswordFragment passwordFragment;

    private DrawerLayout drawer;

    private Toolbar toolbar;

    private int viewMode;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Check Preferences
        SharedPreferences preferences = getSharedPreferences(SettingsFragment.prefsFile, 0);
        viewMode = preferences.getInt(SettingsFragment.KEY_DEFAULT_VIEW, MODE_WEEK);

//        userID = preferences.getInt("userID", -1);
//        if(userID != -1)
//            unlocked = true;

        // Set toolbar as Action bar
        toolbar = (Toolbar) findViewById(R.id.toolbar);
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

        passwordFragment = new PasswordFragment();

        if(!unlocked)
            manager.beginTransaction().add(R.id.passwordFrame, passwordFragment, "password").commit();
        else
            setViewMode(viewMode);
    }

    @Override
    public void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);

        if(debug)
            unlock();
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
                manager.beginTransaction().replace(R.id.contentFrame, new DayViewFragment()).commit();
                break;
            case MODE_WEEK:
                manager.beginTransaction().replace(R.id.contentFrame, new WeekFragment()).commit();
                break;
        }
    }

    public void openDrawer() {
        drawer.openDrawer(GravityCompat.START);
    }

    public void closeDrawer() {
        drawer.closeDrawer(GravityCompat.START);
    }

    public void login(String username, String password) {
        if(username.equals(ACCOUNT_ADMIN) && password.equals(PASS_ADMIN)) {
            unlock();
            return;
        }

        Database db = new Database(this);
        ArrayList<User> users = db.getAllUsers();

        for(User user : users)
            if(user.getName().equals(username) && user.getPassword().equals(password)) {
                userID = user.getId();
                unlock();
            }
    }

    private void unlock() {
        // Hide Password fragment
        getSupportFragmentManager().beginTransaction().remove(passwordFragment).commit();

        // Hide keyboard
        InputMethodManager imm = (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(findViewById(R.id.contentFrame).getWindowToken(), 0);

        // hide password FrameLayout
        DrawerLayout layout = (DrawerLayout) findViewById(R.id.drawer_layout);
        FrameLayout passFrame = (FrameLayout) findViewById(R.id.passwordFrame);
        layout.removeView(passFrame);

        // set state to unlocked
        unlocked = true;

        SharedPreferences preferences = getSharedPreferences(SettingsFragment.prefsFile, 0);
        preferences.edit().putInt("userID", userID).apply();

        setViewMode(viewMode);
    }
}
