package ca.delilaheve.timetable.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;

import ca.delilaheve.timetable.MainActivity;
import ca.delilaheve.timetable.R;
import ca.delilaheve.timetable.SettingsActivity;
import ca.delilaheve.timetable.data.User;
import ca.delilaheve.timetable.database.Database;

public class NavDrawerFragment extends Fragment {

    private View view;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_nav_drawer, container, false);

        TextView dayButton, weekButton, settingsButton, userName, logOutButton;

        dayButton = (TextView) view.findViewById(R.id.dayViewButton);
        weekButton = (TextView) view.findViewById(R.id.weekViewButton);
        settingsButton = (TextView) view.findViewById(R.id.settingsButton);
        userName = (TextView) view.findViewById(R.id.userName);
        logOutButton = (TextView) view.findViewById(R.id.logoutButton);

        dayButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                MainActivity activity = (MainActivity) getActivity();
                activity.setViewMode(MainActivity.MODE_DAY);
                activity.closeDrawer();
            }
        });

        weekButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                MainActivity activity = (MainActivity) getActivity();
                activity.setViewMode(MainActivity.MODE_WEEK);
                activity.closeDrawer();
            }
        });

        settingsButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(getActivity(), SettingsActivity.class);
                startActivity(i);
                ((MainActivity) getActivity()).closeDrawer();
            }
        });

//        logOutButton.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                getActivity().getSharedPreferences(SettingsFragment.prefsFile, 0).edit().putInt("userID", -1).commit();
//
//                Intent i = new Intent(getActivity(), MainActivity.class);
//                i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
//                getActivity().finish();
//                startActivity(i);
//            }
//        });

        if(MainActivity.userID != -1) {
            String name = "";
            Database db = new Database(getActivity());
            ArrayList<User> users = db.getAllUsers();

            for (User user : users)
                if (user.getId() == MainActivity.userID) {
                    name = user.getName();
                    break;
                }

            userName.setText(name);
        }

        return view;
    }
}
