package ca.delilaheve.timetable.dialog;

import android.app.AlertDialog;
import android.content.ContentValues;
import android.content.Context;
import android.content.DialogInterface;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.Spinner;

import ca.delilaheve.timetable.R;
import ca.delilaheve.timetable.adapter.SimpleAdapter;
import ca.delilaheve.timetable.data.Color;
import ca.delilaheve.timetable.database.Column;
import ca.delilaheve.timetable.database.Database;

public class AddEventDialog implements ColorPick {

    private AlertDialog.Builder builder;

    private View colorPreview;

    private Color color;

    public AddEventDialog(final Context context) {
        builder = new AlertDialog.Builder(context);

        builder.setTitle("Add Event");

        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View view = inflater.inflate(R.layout.dialog_add_event, null, false);

        // Do stuff here
        final EditText eventName, startTime, endTime, location, eventDescription;
        final Spinner weekdays;

        eventName = (EditText) view.findViewById(R.id.eventName);
        startTime = (EditText) view.findViewById(R.id.startTime);
        endTime = (EditText) view.findViewById(R.id.endTime);
        location = (EditText) view.findViewById(R.id.location);
        eventDescription = (EditText) view.findViewById(R.id.eventDescription);
        colorPreview = view.findViewById(R.id.colorPreview);

        weekdays = (Spinner) view.findViewById(R.id.dayOfWeek);

        startTime.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                TimePickerDialog timePicker = new TimePickerDialog(context, startTime);
                timePicker.show();
            }
        });

        endTime.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                TimePickerDialog timePicker = new TimePickerDialog(context, endTime);
                timePicker.show();
            }
        });

        colorPreview.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new ColorPickerDialog(context, AddEventDialog.this);
            }
        });

        SimpleAdapter adapter = new SimpleAdapter(context, R.array.weekdays);
        weekdays.setAdapter(adapter);

        builder.setView(view);

        builder.setPositiveButton("Save", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                Database db = new Database(context);

                Column[] columns = Database.COL_USER_EVENTS;

                String event, day, start, end, loc, notes;

                event = eventName.getText().toString();
                day = weekdays.getSelectedItem().toString();
                start = startTime.getText().toString();
                end = endTime.getText().toString();
                loc = location.getText().toString();
                notes = eventDescription.getText().toString();

                ContentValues values = new ContentValues();
                values.put(columns[1].getColumnName(), event);
                values.put(columns[2].getColumnName(), day);
                values.put(columns[3].getColumnName(), start);
                values.put(columns[4].getColumnName(), end);
                values.put(columns[5].getColumnName(), loc);
                values.put(columns[6].getColumnName(), notes);

                db.userEvents.add(values);
            }
        });

        builder.setNegativeButton("Cancel", null);
    }

    public void show() {
        builder.create().show();
    }

    public void setColor(Color c) {
        colorPreview.setBackgroundColor(c.makeColor());
        color = c;
    }

}
