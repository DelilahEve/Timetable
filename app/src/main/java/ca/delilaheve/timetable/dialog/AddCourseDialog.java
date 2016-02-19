package ca.delilaheve.timetable.dialog;

import android.app.AlertDialog;
import android.content.ContentValues;
import android.content.Context;
import android.content.DialogInterface;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

import ca.delilaheve.timetable.R;
import ca.delilaheve.timetable.adapter.ClassEventListAdapter;
import ca.delilaheve.timetable.data.Event;
import ca.delilaheve.timetable.database.Column;
import ca.delilaheve.timetable.database.Database;
import ca.delilaheve.timetable.database.Table;

public class AddCourseDialog {

    private AlertDialog.Builder builder;

    private Context context;

    private EditText courseName;
    private EditText courseCode;
    private ListView classTimes;
    private TextView addClassButton;

    private ArrayList<Event> classEvents;
    private ClassEventListAdapter adapter;

    private Event current = null;

    public AddCourseDialog(Context context) {
        classEvents = new ArrayList<>();
        this.context = context;
        builder = new AlertDialog.Builder(context);

        builder.setTitle("Add Course");

        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View view = inflater.inflate(R.layout.dialog_add_course, null, false);

        courseName = (EditText) view.findViewById(R.id.courseName);
        courseCode = (EditText) view.findViewById(R.id.courseCode);
        classTimes = (ListView) view.findViewById(R.id.classList);
        addClassButton = (TextView) view.findViewById(R.id.addClassButton);

        addClassButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addClass();
            }
        });

        adapter = new ClassEventListAdapter(context, classEvents);
        classTimes.setAdapter(adapter);

        builder.setView(view);

        builder.setPositiveButton("Save", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                saveCourse();
            }
        });

        builder.setNegativeButton("Cancel", null);
    }

    public void show() {
        builder.create().show();
    }

    private void saveCourse() {
        String name, code;
        int courseID;

        name = courseName.getText().toString();
        code = courseCode.getText().toString();

        if(name.equals("") || code.equals("")) {
            Toast.makeText(context, "Failed to save course", Toast.LENGTH_SHORT).show();
            return;
        }

        Database db = new Database(context);

        // Save course info
        Column[] columns = Database.COL_CLASSES;
        Table courses = db.classes;

        ContentValues values = new ContentValues();
        values.put(columns[1].getColumnName(), name);
        values.put(columns[2].getColumnName(), code);

        courseID = (int) courses.add(values);

        // Save Event info

        columns = Database.COL_EVENTS;
        Table eventTable = db.events;

        for(Event event : classEvents) {
            ContentValues v = new ContentValues();
            v.put(columns[1].getColumnName(), courseID);
            v.put(columns[2].getColumnName(), event.getTeacher());
            v.put(columns[3].getColumnName(), event.getDay());
            v.put(columns[4].getColumnName(), event.getStartTime());
            v.put(columns[5].getColumnName(), event.getEndTime());
            v.put(columns[6].getColumnName(), event.getRoom());
            v.put(columns[7].getColumnName(), event.getCampus());


            eventTable.add(v);
        }
    }

    private void addClass() {
        // Add class
        AddClassDialog dialog = new AddClassDialog(context, this);
        dialog.show();
    }

    public void addEvents(Event[] events){
        for(int i = 0; i < events.length; i++) {
            classEvents.add(events[i]);
            //adapter.addItem(events[i]);
            adapter.notifyDataSetChanged();
        }
    }

    private void setEvent(Event event){
        current = event;


    }
}
