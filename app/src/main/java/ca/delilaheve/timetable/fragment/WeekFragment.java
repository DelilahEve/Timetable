package ca.delilaheve.timetable.fragment;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.GridLayout;

import java.util.ArrayList;

import ca.delilaheve.timetable.R;
import ca.delilaheve.timetable.data.Event;
import ca.delilaheve.timetable.database.Database;

public class WeekFragment extends Fragment {

    private View view;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_weekview, container, false);

        GridLayout calendar = (GridLayout) view.findViewById(R.id.calendarItems);

        Database db = new Database(getActivity());
        ArrayList<Event> events = db.getEventList();

        if(events != null)
            for(Event event : events) {
                GridLayout.LayoutParams params = new GridLayout.LayoutParams();

                int rowStart, rowEnd, rowSpan, column, hour, minute;
                String[] time;

                // Determine start row
                time = event.getStartTime().split(":");
                hour = Integer.parseInt(time[0]);
                minute = Integer.parseInt(time[1]);
                rowStart = ((hour - 8) * 4) + (minute / 15);

                // Determine end row
                time = event.getEndTime().split(":");
                hour = Integer.parseInt(time[0]);
                minute = Integer.parseInt(time[1]);
                rowEnd = ((hour-8) * 4) + (minute / 15);

                // Determine row span
                rowSpan = rowEnd - rowStart;

                // Determine column
                String[] days = getResources().getStringArray(R.array.weekdays);

                column = -1;
                for(int i = 0; i < days.length; i++)
                    if(days[i].equalsIgnoreCase(event.getDay()))
                        column = i;

                // is invalid entry: ignore
                if(column == -1)
                    continue;

                // set row and column dimensions
                params.rowSpec = GridLayout.spec(rowStart, rowSpan);
                params.columnSpec = GridLayout.spec(column);

                // add view to grid
                View v = inflater.inflate(R.layout.item_calendar, container, false);
                v.setLayoutParams(params);
                calendar.addView(v);

                // set click listener for view
                setClick(v);
            }

        return view;
    }

    public void setClick(View v) {
        v.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Go to view event
            }
        });
    }
}
