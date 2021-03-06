package ca.delilaheve.timetable.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.ArrayList;

import ca.delilaheve.timetable.R;
import ca.delilaheve.timetable.data.Course;

public class CourseListAdapter extends BaseAdapter {

    private LayoutInflater inflater;

    private ArrayList<Course> courses;

    public CourseListAdapter(Context context, ArrayList<Course> courses) {
        inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        this.courses = courses;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View view = inflater.inflate(R.layout.item_simple, parent, false);

        TextView itemText = (TextView) view.findViewById(R.id.itemText);
        String text = courses.get(position).getCourseName() + " - " + courses.get(position).getCourseCode();
        itemText.setText(text);

        return view;
    }

    @Override
    public Course getItem(int position) {
        return courses.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public int getCount() {
        return courses.size();
    }

    public void addItem(Course course) {
        courses.add(course);
        notifyDataSetChanged();
    }
}
