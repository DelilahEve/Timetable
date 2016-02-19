package ca.delilaheve.timetable.fragment;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.GridLayout;
import android.widget.Space;
import android.widget.TextView;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Random;

import ca.delilaheve.timetable.EventActivity;
import ca.delilaheve.timetable.R;
import ca.delilaheve.timetable.data.Event;
import ca.delilaheve.timetable.database.Database;

public class DayViewFragment extends Fragment {

    private View view;

    private LayoutInflater inflater;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        this.inflater = inflater;
        view = inflater.inflate(R.layout.fragment_day_view, container, false);

        String currentDay = new SimpleDateFormat("EEEE").format(new Date());

        TextView day = (TextView) view.findViewById(R.id.day);
        day.setText(currentDay);

        if(savedInstanceState == null)
            update();

        return view;
    }

    public void update() {int i = 0;
        if(view == null)
            return;

        GridLayout calendar = (GridLayout) view.findViewById(R.id.calendarItems);

        Database db = new Database(getActivity());
        ArrayList<Event> events = db.getEventList();

        int rowCount = 44;

        for(int r = 0; r < rowCount && i <= events.size(); r++) {
            GridLayout.LayoutParams params = new GridLayout.LayoutParams();
            int rowEnd, rowSpan;
            int eventStart;

            if(events.size() != 0 && i < events.size()) {
                Event event = events.get(i);
                eventStart = getRowFromTime(event.getStartTime());

                // Event begins further up, add padding
                if(eventStart > r) {
                    Space padBefore = new Space(getActivity());

                    rowSpan = eventStart - r;

                    System.out.println("Start: " + event.getStartTime());
                    System.out.println("Col: " + 0 + " | Row Start: " + r + " | Event Start: " + eventStart + " | Row Span: " + rowSpan);

                    params.rowSpec = GridLayout.spec(r, rowSpan, GridLayout.FILL, 1f * rowSpan);
                    params.columnSpec = GridLayout.spec(0);
                    params.width = calendar.getWidth() / 5;

                    padBefore.setLayoutParams(params);
                    calendar.addView(padBefore);

                    r = eventStart;
                }

                // Add Event here
                View eventView = inflater.inflate(R.layout.item_calendar, null, false);
                Random random = new Random();
                eventView.setBackgroundColor(Color.argb(50, random.nextInt(255), random.nextInt(255), random.nextInt(255)));

                TextView eventName, eventLoc;

                eventName = (TextView) eventView.findViewById(R.id.eventName);
                eventLoc = (TextView) eventView.findViewById(R.id.location);

                eventName.setText(event.getCourseCode());
                eventLoc.setText(event.getRoom());

                setClick(eventView, event);

                rowEnd = getRowFromTime(event.getEndTime());
                rowSpan = rowEnd - r;

                System.out.println("Col: " + 0 + " | Row Start: " + r + " | Row End: " + rowEnd + " | Row Span: " + rowSpan);

                params.rowSpec = GridLayout.spec(r, rowSpan, GridLayout.FILL, 1f * rowSpan);
                params.columnSpec = GridLayout.spec(0);
                params.width = calendar.getWidth() / 5;

                eventView.setLayoutParams(params);
                calendar.addView(eventView);

                i++;
                r = rowEnd;
            }
            else if(events.size() == 0 && r == 0) {
                Space padBefore = new Space(getActivity());

                System.out.println("Col: " + 0 + " | Row Start: " + r + " | Row Span: " + String.valueOf(rowCount-1));

                params.rowSpec = GridLayout.spec(r, rowCount - 1, 1f * rowCount);
                params.columnSpec = GridLayout.spec(0);
                params.width = calendar.getWidth() / 5;

                padBefore.setLayoutParams(params);
                calendar.addView(padBefore);

                r = rowCount;
            }
        }

        calendar.invalidate();
    }

    private int getRowFromTime(String time) {
        String[] first = time.split(":");
        String[] second = first[1].split(" ");

        int hour, minute, row;
        String p;

        hour = Integer.parseInt(first[0]);
        minute = Integer.parseInt(second[0]);
        p = second[1];

        if(p.equals("PM") && hour != 12)
            hour += 12;

        row  = (hour - 8) * 4;
        if(minute != 0)
            row += (minute / 15);

        return row;
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
