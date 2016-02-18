package ca.delilaheve.timetable.fragment;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.Spinner;
import android.widget.Switch;
import android.widget.TextView;

import ca.delilaheve.timetable.MainActivity;
import ca.delilaheve.timetable.R;
import ca.delilaheve.timetable.adapter.SimpleAdapter;
import ca.delilaheve.timetable.dialog.AddCourseDialog;
import ca.delilaheve.timetable.dialog.AddUserDialog;

public class SettingsFragment extends Fragment {

    public static final String prefsFile = "TimetableSettings";

    public static final String KEY_DEFAULT_VIEW = "default_view";
    public static final String KEY_DO_NOTIFY = "notify";
    public static final String KEY_AUTO_MUTE = "auto_mute";
    public static final String KEY_SERVER = "is_server";
    public static final String KEY_NOTIFY_TIME = "notification_start";
    public static final String KEY_MUTE_MODE = "mute_mode";

    private SharedPreferences preferences;

    private View view;

    private RadioButton dayView;
    private RadioButton weekView;
    private Switch notifications;
    private Switch automuteToggle;
    private Switch serverToggle;
    private EditText notifyTime;
    private Spinner muteMode;
    private TextView addCourse;
    private TextView addUser;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_settings, container, false);
        preferences = getActivity().getSharedPreferences(prefsFile, 0);

        dayView = (RadioButton) view.findViewById(R.id.dayViewRadio);
        weekView = (RadioButton) view.findViewById(R.id.weekViewRadio);

        notifications = (Switch) view.findViewById(R.id.showNotifications);
        automuteToggle = (Switch) view.findViewById(R.id.automuteToggle);
        serverToggle = (Switch) view.findViewById(R.id.serverToggle);

        notifyTime = (EditText) view.findViewById(R.id.notifyTime);

        muteMode = (Spinner) view.findViewById(R.id.muteSpinner);

        addCourse = (TextView) view.findViewById(R.id.addCourse);
        addUser = (TextView) view.findViewById(R.id.addUser);

        int defaultViewMode, notificationStart, mute;
        boolean notify, automute, server;

        defaultViewMode = preferences.getInt(KEY_DEFAULT_VIEW, MainActivity.MODE_WEEK);
        notify = preferences.getBoolean(KEY_DO_NOTIFY, true);
        automute = preferences.getBoolean(KEY_AUTO_MUTE, true);
        server = preferences.getBoolean(KEY_SERVER, true);
        notificationStart = preferences.getInt(KEY_NOTIFY_TIME, 15);
        mute = preferences.getInt(KEY_MUTE_MODE, 1);

        // Default view
        switch (defaultViewMode) {
            case MainActivity.MODE_WEEK:
                weekView.setChecked(true);
                break;
            case MainActivity.MODE_DAY:
                dayView.setChecked(true);
                break;
        }

        // Notifications
        notifications.setChecked(notify);

        // Auto Mute
        automuteToggle.setChecked(automute);

        // Server
        serverToggle.setChecked(server);

        // Notify Time
        notifyTime.setText(String.valueOf(notificationStart));

        // Mute Mode
        SimpleAdapter adapter = new SimpleAdapter(getActivity(), R.array.mute_modes);
        muteMode.setAdapter(adapter);
        muteMode.setSelection(mute);

        // Add Course Button
        addCourse.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new AddCourseDialog(getActivity()).show();
            }
        });

        // Add User Button
        addUser.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new AddUserDialog(getActivity()).show();
            }
        });

        return view;
    }

    @Override
    public void onStop() {
        super.onStop();

        // Save info
        SharedPreferences.Editor editor = preferences.edit();

        int defaultViewMode, notificationStart, mute;
        boolean notify, automute, server;

        if(weekView.isChecked())
            defaultViewMode = MainActivity.MODE_WEEK;
        else
            defaultViewMode = MainActivity.MODE_DAY;
        notify = notifications.isChecked();
        automute = automuteToggle.isChecked();
        server = serverToggle.isChecked();
        notificationStart = Integer.parseInt(notifyTime.getText().toString());
        mute = muteMode.getSelectedItemPosition();

        editor.putInt(KEY_DEFAULT_VIEW, defaultViewMode);
        editor.putInt(KEY_NOTIFY_TIME, notificationStart);
        editor.putInt(KEY_MUTE_MODE, mute);
        editor.putBoolean(KEY_DO_NOTIFY, notify);
        editor.putBoolean(KEY_AUTO_MUTE, automute);
        editor.putBoolean(KEY_SERVER, server);

        editor.commit();
    }
}
