package ca.delilaheve.timetable.dialog;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.Spinner;

import ca.delilaheve.timetable.R;
import ca.delilaheve.timetable.adapter.SimpleAdapter;

public class TimePickerDialog {

    private AlertDialog.Builder builder;

    private String time = "";

    public TimePickerDialog(final Context context, final EditText requester) {
        builder = new AlertDialog.Builder(context);

        builder.setTitle("Set Time");

        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View view = inflater.inflate(R.layout.dialog_time_picker, null, false);

        final Spinner hour, minute, timePeriod;

        hour = (Spinner) view.findViewById(R.id.hourSpinner);
        minute = (Spinner) view.findViewById(R.id.minuteSpinner);
        timePeriod = (Spinner) view.findViewById(R.id.timePeriodSpinner);

        SimpleAdapter hourAdapter, minAdapter, timeAdapter;

        hourAdapter = new SimpleAdapter(context, R.array.hours);
        minAdapter = new SimpleAdapter(context, R.array.minutes);
        timeAdapter = new SimpleAdapter(context, R.array.time_periods);

        hour.setAdapter(hourAdapter);
        minute.setAdapter(minAdapter);
        timePeriod.setAdapter(timeAdapter);

        builder.setView(view);

        builder.setPositiveButton("Done", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                time = hour.getSelectedItem().toString()
                        + ":" + minute.getSelectedItem().toString()
                        + " " + timePeriod.getSelectedItem().toString();

                requester.setText(time);
            }
        });

        builder.setNegativeButton("Cancel", null);
    }

    public void show() {
        builder.create().show();
    }
}
