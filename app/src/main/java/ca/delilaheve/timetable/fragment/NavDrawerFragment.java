package ca.delilaheve.timetable.fragment;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import ca.delilaheve.timetable.R;

public class NavDrawerFragment extends Fragment {

    private View view;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_nav_drawer, container, false);

        TextView dayButton, weekButton, monthButton;

        dayButton = (TextView) view.findViewById(R.id.dayViewButton);
        weekButton = (TextView) view.findViewById(R.id.weekViewButton);
        monthButton = (TextView) view.findViewById(R.id.monthViewButton);

        dayButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });

        weekButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Set to week view
            }
        });

        monthButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });

        return view;
    }
}
