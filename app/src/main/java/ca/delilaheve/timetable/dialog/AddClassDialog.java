package ca.delilaheve.timetable.dialog;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import java.util.ArrayList;

import ca.delilaheve.timetable.R;
import ca.delilaheve.timetable.adapter.SimpleAdapter;
import ca.delilaheve.timetable.data.Event;

public class AddClassDialog {

    private AlertDialog.Builder builder;

    private ArrayList<Event> events;

    private Context context;

    private CheckBox[] days;

    private EditText startTime;
    private EditText endTime;
    private EditText room;
    private EditText teacher;

    private Spinner campus;

    private AddCourseDialog parent;

    public AddClassDialog(final Context context, AddCourseDialog parent) {
        this.context = context;
        this.parent = parent;
        events = new ArrayList<>();
        builder = new AlertDialog.Builder(context);

        builder.setTitle("Add Class");

        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View view = inflater.inflate(R.layout.dialog_add_class, null, false);

        days = new CheckBox[] {
                (CheckBox) view.findViewById(R.id.monday),
                (CheckBox) view.findViewById(R.id.tuesday),
                (CheckBox) view.findViewById(R.id.wednesday),
                (CheckBox) view.findViewById(R.id.thursday),
                (CheckBox) view.findViewById(R.id.friday)
        };

        startTime = (EditText) view.findViewById(R.id.startTime);
        endTime = (EditText) view.findViewById(R.id.endTime);

        startTime.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new TimePickerDialog(context, startTime).show();
            }
        });
        endTime.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new TimePickerDialog(context, endTime).show();
            }
        });

        teacher = (EditText) view.findViewById(R.id.teacher);

        room = (EditText) view.findViewById(R.id.room);

        campus = (Spinner) view.findViewById(R.id.campus);
        campus.setAdapter(new SimpleAdapter(context, R.array.campuses));

        builder.setView(view);

        builder.setPositiveButton("Save", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                saveClass();
            }
        });

        builder.setNegativeButton("Cancel", null);
    }

    public void show() {
        builder.create().show();
    }

    private void saveClass() {
        String day, start, end, rm, camp, prof;
        String[] weekdays = context.getResources().getStringArray(R.array.weekdays);

        start = startTime.getText().toString();
        end = endTime.getText().toString();
        rm = room.getText().toString();
        camp = campus.getSelectedItem().toString();
        prof = teacher.getText().toString();

        if(start.equals("") || end.equals("") || rm.equals("") || camp.equals("") || prof.equals("")) {
            Toast.makeText(context, "Failed to save class", Toast.LENGTH_SHORT).show();
            return;
        }

        int i = 0;
        for(CheckBox d : days) {
            if(!d.isChecked()) {
                i++;
                continue;
            }

            day = weekdays[i];

            Event event = new Event(0, 0, prof, day, start, end, rm, camp, "", "");
            events.add(event);

            i++;
        }

        parent.addEvents(getResult());
    }

    public Event[] getResult() {
        return events.toArray(new Event[events.size()]);
    }
}
