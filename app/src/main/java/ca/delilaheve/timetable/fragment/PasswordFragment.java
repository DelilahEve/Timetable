package ca.delilaheve.timetable.fragment;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;

import ca.delilaheve.timetable.MainActivity;
import ca.delilaheve.timetable.R;

public class PasswordFragment extends Fragment {

    private View view;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_password, container, false);

        final EditText username, password;
        TextView loginButton;

        username = (EditText) view.findViewById(R.id.username);
        password = (EditText) view.findViewById(R.id.password);

        loginButton = (TextView) view.findViewById(R.id.loginButton);

        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String user, pass;

                user = username.getText().toString();
                pass = password.getText().toString();

                if(user.equals("") || pass.equals(""))
                    return;

                ((MainActivity) getActivity()).login(user, pass);
            }
        });

        return view;
    }
}
