package ca.delilaheve.timetable.fragment;

import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Random;

import ca.delilaheve.timetable.R;
import ca.delilaheve.timetable.data.Event;
import ca.delilaheve.timetable.database.Database;
import ca.delilaheve.timetable.view.CalendarLayout;

public class WeekFragment extends Fragment {

    private View view;

    private LayoutInflater inflater;

    private CalendarLayout calendar;

    private boolean vtoComplete = false;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        this.inflater = inflater;
        view = inflater.inflate(R.layout.fragment_week_view, container, false);

        calendar = (CalendarLayout) view.findViewById(R.id.calendarItems);

        return view;
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        ViewTreeObserver vto = calendar.getViewTreeObserver();
        vto.addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                if(!vtoComplete) {
                    update();
                    vtoComplete = true;
                }
            }
        });
    }

    private void setHeaders() {
        String[] times = {
                "8", "9", "10", "11", "12", "1", "2", "3", "4", "5"
        };

        String p = "AM";

        for (int i = 0; i < times.length; i++) {
            int rowStart, rowSpan = 4;

            if(Integer.parseInt(times[i]) == 12)
                p = "PM";

            String t = times[i] + ":00 " + p;

            rowStart = getRowFromTime(t);

            View h = inflater.inflate(R.layout.item_calendar_header, calendar, false);

            TextView itemText = (TextView) h.findViewById(R.id.itemText);
            itemText.setText(times[i] + " " + p);

            int c = Color.argb(80, 200, 200, 200);
            if(rowStart % 4 == 0 && rowStart % 8 != 0)
                c = Color.argb(190, 200, 200, 200);

                h.setBackgroundColor(c);

            System.out.println("  |  Start Row: " + rowStart + "  |  Row Span: " + rowSpan);

            calendar.addHeader(h, rowStart, rowSpan, true);
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        update();
    }

    public void update() {
        if(view == null)
            return;

        // Get calendar view
        calendar.removeAllViews();

        // get events list
        Database db = new Database(getActivity());
        ArrayList<Event> events = db.getEventList();

        // set grid size (static for testing phase)
        int cols = 5;
        int rows = 40;
        calendar.setGrid(rows, cols, calendar.getHeight(), calendar.getWidth());

        setHeaders();

        // Sort events by day
        Event[][] eventsGrid = sortEvents(events);

        // Iterate over events to add to calendar
        for(int c = 0; c < cols; c++) {
            // Iterate Columns

            Event[] day = eventsGrid[c];

            for(Event e : day) {
                // Iterate Rows
                int rowStart, rowEnd, rowSpan;

                rowStart = getRowFromTime(e.getStartTime());
                rowEnd = getRowFromTime(e.getEndTime());

                rowSpan = rowEnd - rowStart;

                View v = inflater.inflate(R.layout.item_calendar, calendar, false);

                // Modify View here
                Random r = new Random(e.getClassID());
                v.setBackgroundColor(Color.argb(50, r.nextInt(255), r.nextInt(255), r.nextInt(255)));

                System.out.println("Event added in Week: " + e.getCourseCode());
                System.out.println("Col: " + c + "  |  Start Row: " + rowStart + "  |  End Row: " + rowEnd + "  |  Row Span: " + rowSpan);
                System.out.println();

                TextView eventName, place;
                eventName = (TextView) v.findViewById(R.id.eventName);
                place = (TextView) v.findViewById(R.id.location);

                eventName.setText(e.getCourseCode());
                place.setText(e.getRoom());

                // Set click and add to calendar
                setClick(v, e);
                calendar.addChild(v, rowStart, rowSpan, c);
            }

        }

    }

    public void setClick(View v, Event event) {
        v.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Go to view event
            }
        });
    }

    private int getRowFromTime(String time) {
        String[] first = time.split(":");
        String[] second = first[1].split(" ");

        int hour, minute, row = 0;
        String p;

        hour = Integer.parseInt(first[0]);
        minute = Integer.parseInt(second[0]);
        p = second[1];

        if(p.equals("PM") && hour != 12) {
            hour += 12;
        }

        row  += ((hour - 8) * 4) + (minute / 60);

        return row;
    }

    private Event[][] sortEvents(ArrayList<Event> events) {
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
