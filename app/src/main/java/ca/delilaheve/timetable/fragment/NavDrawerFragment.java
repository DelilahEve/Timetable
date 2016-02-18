package ca.delilaheve.timetable.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import ca.delilaheve.timetable.MainActivity;
import ca.delilaheve.timetable.R;
import ca.delilaheve.timetable.SettingsActivity;

public class NavDrawerFragment extends Fragment {

    private View view;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_nav_drawer, container, false);

        TextView dayButton, weekButton, settingsButton;

        dayButton = (TextView) view.findViewById(R.id.dayViewButton);
        weekButton = (TextView) view.findViewById(R.id.weekViewButton);
        settingsButton = (TextView) view.findViewById(R.id.settingsButton);

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

        return view;
    }
}
