package ca.delilaheve.timetable.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.GridLayout;
import android.widget.TextView;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import ca.delilaheve.timetable.EventActivity;
import ca.delilaheve.timetable.R;
import ca.delilaheve.timetable.data.Event;
import ca.delilaheve.timetable.database.Database;

public class DayViewFragment extends Fragment {

    private View view;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_day_view, container, false);

        GridLayout calendar = (GridLayout) view.findViewById(R.id.calendarItems);

        Database db = new Database(getActivity());
        ArrayList<Event> events = db.getEventList();

        String currentDay = new SimpleDateFormat("EEEE").format(new Date());

        TextView day = (TextView) view.findViewById(R.id.day);
        day.setText(currentDay);

        if(events != null)
            for(Event event : events) {

                if(event.getDay().equals(currentDay))
                    continue;

                // Is for current day, add layout:

                GridLayout.LayoutParams params = new GridLayout.LayoutParams();
                int rowStart, rowEnd, rowSpan, hour, minute;
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

                // set row dimensions
                params.rowSpec = GridLayout.spec(rowStart, rowSpan);

                // add view to grid
                View v = inflater.inflate(R.layout.item_calendar, container, false);
                v.setLayoutParams(params);
                calendar.addView(v);

                // set click listener for view
                setClick(v, event);
            }

        return view;
    }

    public void setClick(View v, final Event event) {
        v.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(getActivity(), EventActivity.class);
                i.putExtra("event", event);
                startActivity(i);
            }
        });
    }
}
