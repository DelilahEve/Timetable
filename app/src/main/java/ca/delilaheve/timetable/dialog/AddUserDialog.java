package ca.delilaheve.timetable.dialog;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import ca.delilaheve.timetable.R;
import ca.delilaheve.timetable.adapter.CourseListAdapter;
import ca.delilaheve.timetable.adapter.SimpleAdapter;
import ca.delilaheve.timetable.data.*;
import ca.delilaheve.timetable.database.Database;

public class AddUserDialog {

    private AlertDialog.Builder builder;

    private Context context;

    private ArrayList<Course> addedCourses;

    private CourseListAdapter courseListAdapter;

    public AddUserDialog(Context context) {
        addedCourses = new ArrayList<>();
        this.context = context;

        builder = new AlertDialog.Builder(context);

        builder.setTitle("Add User");

        LayoutInflater inflater  = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View view = inflater.inflate(R.layout.dialog_add_user, null, false);

        // Do stuff here
        Spinner accountType = (Spinner) view.findViewById(R.id.accountType);
        ListView courseList = (ListView) view.findViewById(R.id.courseList);
        final TextView addCourseButton = (TextView) view.findViewById(R.id.addCourseButton);

        SimpleAdapter adapter = new SimpleAdapter(context, R.array.account_types);
        accountType.setAdapter(adapter);

        addCourseButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addCourse();
            }
        });

        builder.setView(view);

        builder.setPositiveButton("Save", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                saveUser();
            }
        });

        builder.setNegativeButton("Cancel", null);
    }

    public void show() {
        builder.create().show();
    }

    private void saveUser() {
        // Save user to db
    }

    private void addCourse() {
        AlertDialog.Builder builder = new AlertDialog.Builder(context);

        builder.setTitle("Pick Course");

        Database db = new Database(context);
        final ArrayList<Course> courses = db.getAllCourses();

        String[] options = new String[courses.size()];
        for(int i = 0; i < courses.size(); i++)
            options[i] = courses.get(i).getCourseName() + " - " + courses.get(i).getCourseCode();

        builder.setItems(options, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                Course course = courses.get(which);
                addedCourses.add(course);
                dialog.dismiss();
            }
        });

        builder.setNegativeButton("Cancel", null);

        AlertDialog dialog = builder.create();
        dialog.show();
    }
}
