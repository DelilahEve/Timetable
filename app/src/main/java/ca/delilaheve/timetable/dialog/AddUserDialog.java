package ca.delilaheve.timetable.dialog;

import android.app.AlertDialog;
import android.content.ContentValues;
import android.content.Context;
import android.content.DialogInterface;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

import ca.delilaheve.timetable.R;
import ca.delilaheve.timetable.adapter.CourseListAdapter;
import ca.delilaheve.timetable.adapter.SimpleAdapter;
import ca.delilaheve.timetable.data.*;
import ca.delilaheve.timetable.database.Column;
import ca.delilaheve.timetable.database.Database;
import ca.delilaheve.timetable.database.Table;

public class AddUserDialog {

    private AlertDialog.Builder builder;

    private Context context;

    private ArrayList<Course> addedCourses;

    private CourseListAdapter courseListAdapter;

    private Spinner accountType;
    private ListView courseList;
    private TextView addCourseButton;
    private EditText firstName;
    private EditText lastName;
    private EditText password;

    public AddUserDialog(Context context) {
        addedCourses = new ArrayList<>();
        this.context = context;

        builder = new AlertDialog.Builder(context);

        builder.setTitle("Add User");

        LayoutInflater inflater  = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View view = inflater.inflate(R.layout.dialog_add_user, null, false);

        // Do stuff here
        accountType = (Spinner) view.findViewById(R.id.accountType);
        courseList = (ListView) view.findViewById(R.id.courseList);
        addCourseButton = (TextView) view.findViewById(R.id.addCourseButton);
        firstName = (EditText) view.findViewById(R.id.firstName);
        lastName = (EditText) view.findViewById(R.id.lastName);
        password = (EditText) view.findViewById(R.id.password);

        SimpleAdapter adapter = new SimpleAdapter(context, R.array.account_types);
        accountType.setAdapter(adapter);

        courseListAdapter = new CourseListAdapter(context, new ArrayList<Course>());
        courseList.setAdapter(courseListAdapter);

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
        String name, pass, account;

        String first, last;
        first = firstName.getText().toString();
        last = lastName.getText().toString();

        name = first + " " + last;

        pass = password.getText().toString();

        account = accountType.getSelectedItem().toString();

        if(first.equals("") || last.equals("") || pass.equals("")) {
            Toast.makeText(context, "Failed to save user", Toast.LENGTH_SHORT).show();
        }

        Database db = new Database(context);
        // Add people data

        Column[] peopleCol = Database.COL_PEOPLE;

        ContentValues values = new ContentValues();

        values.put(peopleCol[1].getColumnName(), name);
        values.put(peopleCol[2].getColumnName(), pass);
        values.put(peopleCol[3].getColumnName(), account);

        Table people = db.people;
        int peopleID = (int) people.add(values);

        // Add course list data
        Column[] courseListCol = Database.COL_CLASS_LIST;
        Table courseList = db.classList;

        for (Course course : addedCourses) {
            ContentValues v = new ContentValues();

            v.put(courseListCol[1].getColumnName(), course.getId());
            v.put(courseListCol[2].getColumnName(), peopleID);

            courseList.add(v);
        }
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
                courseListAdapter.addItem(course);
                if(!addedCourses.contains(course))
                    addedCourses.add(course);
                dialog.dismiss();
            }
        });

        builder.setNegativeButton("Cancel", null);

        AlertDialog dialog = builder.create();
        dialog.show();
    }
}
