package ca.delilaheve.timetable.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import ca.delilaheve.timetable.R;

public class SimpleAdapter extends BaseAdapter {

    private String[] options;

    private LayoutInflater inflater;

    public SimpleAdapter(Context context, int arrayID) {
        inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        options = context.getResources().getStringArray(arrayID);
    }

    public SimpleAdapter(Context context, String[] options) {
        inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        this.options = options;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View view = inflater.inflate(R.layout.item_simple, parent, false);

        TextView itemText = (TextView) view.findViewById(R.id.itemText);
        itemText.setText(options[position]);

        return view;
    }

    @Override
    public Object getItem(int position) {
        return options[position];
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public int getCount() {
        return options.length;
    }
}
