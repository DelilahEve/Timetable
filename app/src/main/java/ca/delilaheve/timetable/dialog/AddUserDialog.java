package ca.delilaheve.timetable.dialog;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.view.LayoutInflater;
import android.view.View;

import ca.delilaheve.timetable.R;

public class AddUserDialog {

    private AlertDialog.Builder builder;

    public AddUserDialog(Context context) {
        builder = new AlertDialog.Builder(context);

        builder.setTitle("Add User");

        LayoutInflater inflater  = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View view = inflater.inflate(R.layout.dialog_add_user, null, false);

        // Do stuff here

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

}
