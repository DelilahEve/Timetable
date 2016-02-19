package ca.delilaheve.timetable.fragment;

import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.GridLayout;
import android.widget.Space;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Random;

import ca.delilaheve.timetable.R;
import ca.delilaheve.timetable.data.Event;
import ca.delilaheve.timetable.database.Database;

public class WeekFragment extends Fragment {

    private View view;

    private LayoutInflater inflater;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        this.inflater = inflater;
        view = inflater.inflate(R.layout.fragment_weekview, container, false);

        if(savedInstanceState == null)
            update();

        return view;
    }

    public void setClick(View v, Event event) {
        v.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Go to view event
            }
        });
    }

    @Override
    public void onResume() {
        super.onResume();
        update();
    }

    public void update() {
        if(view == null)
            return;

        GridLayout calendar = (GridLayout) view.findViewById(R.id.calendarItems);

        Database db = new Database(getActivity());
        ArrayList<Event> events = db.getEventList();

        for(Event event : events)
            System.out.println("Event: " + event.getCourseCode());

        int colCount = 5;
        int rowCount = 44;

        Event[][] eventsGrid = sortEvents(events.toArray(new Event[events.size()]));

        for(int c = 0; c < colCount; c++) {

            Event[] eventList = eventsGrid[c];
            int i = 0;

            for(int r = 0; r < rowCount && i <= eventList.length; r++) {
                GridLayout.LayoutParams params = new GridLayout.LayoutParams();
                int rowEnd, rowSpan;
                int eventStart;

                if(eventList.length != 0 && i < eventList.length) {
                    Event event = eventList[i];
                    eventStart = getRowFromTime(event.getStartTime());

                    // Event begins further up, add padding
                    if(eventStart > r) {
                        Space padBefore = new Space(getActivity());

                        rowSpan = eventStart - r;

                        System.out.println("Start: " + event.getStartTime());
                        System.out.println("Col: " + c + " | Row Start: " + r + " | Event Start: " + eventStart + " | Row Span: " + rowSpan);

                        params.rowSpec = GridLayout.spec(r, rowSpan, GridLayout.FILL, 1f * rowSpan);
                        params.columnSpec = GridLayout.spec(c);
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

                    System.out.println("Col: " + c + " | Row Start: " + r + " | Row End: " + rowEnd + " | Row Span: " + rowSpan);

                    params.rowSpec = GridLayout.spec(r, rowSpan, GridLayout.FILL, 1f * rowSpan);
                    params.columnSpec = GridLayout.spec(c);
                    params.width = calendar.getWidth() / 5;

                    eventView.setLayoutParams(params);
                    calendar.addView(eventView);

                    i++;
                    r = rowEnd;
                }
                else if(eventList.length == 0 && r == 0) {
                    Space padBefore = new Space(getActivity());

                    System.out.println("Col: " + c + " | Row Start: " + r + " | Row Span: " + String.valueOf(rowCount-1));

                    params.rowSpec = GridLayout.spec(r, rowCount - 1, 1f * rowCount);
                    params.columnSpec = GridLayout.spec(c);
                    params.width = calendar.getWidth() / 5;

                    padBefore.setLayoutParams(params);
                    calendar.addView(padBefore);

                    r = rowCount;
                }
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

    private Event[][] sortEvents(Event[] events) {
        ArrayList<Event> monday, tuesday, wednesday, thursday, friday;
        monday = new ArrayList<>();
        tuesday = new ArrayList<>();
        wednesday = new ArrayList<>();
        thursday = new ArrayList<>();
        friday = new ArrayList<>();

        for(Event event : events) {

            switch (event.getDay()) {
                case "Monday":
                    monday.add(event);
                    break;
                case "Tuesday":
                    tuesday.add(event);
                    break;
                case "Wednesday":
                    wednesday.add(event);
                    break;
                case "Thursday":
                    thursday.add(event);
                    break;
                case "Friday":
                    friday.add(event);
                    break;
            }
        }

        return new Event[][] {
                monday.toArray(new Event[monday.size()]),
                tuesday.toArray(new Event[tuesday.size()]),
                wednesday.toArray(new Event[wednesday.size()]),
                thursday.toArray(new Event[thursday.size()]),
                friday.toArray(new Event[friday.size()])
        };
    }
}
