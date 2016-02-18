package ca.delilaheve.timetable.dialog;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.view.LayoutInflater;
import android.view.View;

import ca.delilaheve.timetable.R;

public class AddCourseDialog {

    private AlertDialog.Builder builder;

    public AddCourseDialog(Context context) {
        builder = new AlertDialog.Builder(context);

        builder.setTitle("Add Course");

        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View view = inflater.inflate(R.layout.dialog_add_course, null, false);

        // Do stuff here

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
        // Save course to db
    }

}
