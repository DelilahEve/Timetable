package ca.delilaheve.timetable.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.ArrayList;

import ca.delilaheve.timetable.R;
import ca.delilaheve.timetable.data.Event;

public class ClassEventListAdapter extends BaseAdapter {

    private LayoutInflater inflater;

    private ArrayList<Event> classEvents;

    public ClassEventListAdapter(Context context, ArrayList<Event> classEvents) {
        inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        this.classEvents = classEvents;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View view = inflater.inflate(R.layout.item_simple, parent, false);

        TextView itemText = (TextView) view.findViewById(R.id.itemText);
        Event event = classEvents.get(position);
        String text = event.getStartTime() + " - " + event.getEndTime() + " on " + event.getDay();
        itemText.setText(text);

        return view;
    }

    @Override
    public Event getItem(int position) {
        return classEvents.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public int getCount() {
        return classEvents.size();
    }

    public void addItem(Event event) {
        classEvents.add(event);
        notifyDataSetChanged();
    }

    public boolean hasItem(Event event) {
        for(Event e : classEvents)
            if(event.getDay().equals(e.getDay()) && event.getRoom().equals(e.getRoom()))
                return true;

        return false;
    }
}
