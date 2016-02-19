package ca.delilaheve.timetable.database;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.ArrayList;

import ca.delilaheve.timetable.MainActivity;
import ca.delilaheve.timetable.data.Course;
import ca.delilaheve.timetable.data.Event;
import ca.delilaheve.timetable.data.User;

public class Database extends SQLiteOpenHelper {

    public static final String TABLE_PEOPLE = "people";
    public static final Column[] COL_PEOPLE = {
            new Column("_id", Column.COL_TYPE_INT, true),
            new Column("name", Column.COL_TYPE_TEXT, false),
            new Column("password", Column.COL_TYPE_TEXT, false),
            new Column("accountType", Column.COL_TYPE_TEXT, false)
    };

    public static final String TABLE_EVENTS = "event";
    public static final Column[] COL_EVENTS = {
            new Column("_id", Column.COL_TYPE_INT, true),
            new Column("courseID", Column.COL_TYPE_INT, false),
            new Column("teacherName", Column.COL_TYPE_TEXT, false),
            new Column("day", Column.COL_TYPE_TEXT, false),
            new Column("startTime", Column.COL_TYPE_TEXT, false),
            new Column("endTime", Column.COL_TYPE_TEXT, false),
            new Column("location", Column.COL_TYPE_TEXT, false),
            new Column("campus", Column.COL_TYPE_TEXT, false)
    };

    public static final String TABLE_USER_EVENTS = "userEvents";
    public static final Column[] COL_USER_EVENTS = {
            new Column("_id", Column.COL_TYPE_INT, true),
            new Column("eventName", Column.COL_TYPE_TEXT, false),
            new Column("day", Column.COL_TYPE_TEXT, false),
            new Column("startTime", Column.COL_TYPE_TEXT, false),
            new Column("endTime", Column.COL_TYPE_TEXT, false),
            new Column("location", Column.COL_TYPE_TEXT, false),
            new Column("notes", Column.COL_TYPE_TEXT, false)
    };

    public static final String TABLE_CLASS_LIST = "courseList";
    public static final Column[] COL_CLASS_LIST = {
            new Column("_id", Column.COL_TYPE_INT, true),
            new Column("courseID", Column.COL_TYPE_INT, false),
            new Column("studentID", Column.COL_TYPE_INT, false)
    };


    public static final String TABLE_CLASSES = "course";
    public static final Column[] COL_CLASSES = {
            new Column("_id", Column.COL_TYPE_INT, true),
            new Column("courseName", Column.COL_TYPE_INT, false),
            new Column("courseCode", Column.COL_TYPE_INT, false)
    };


    public Table people;
    public Table events;
    public Table userEvents;
    public Table classList;
    public Table classes;

    // Increment upon schema change to DB:
    public static final int DATABASE_VERSION = 5;

    public static final String DATABASE_NAME = "Timetable.db";

    private SQLiteDatabase db;

    public Database(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);

        db = getWritableDatabase();

        people = new Table(TABLE_PEOPLE, COL_PEOPLE, db);
        events = new Table(TABLE_EVENTS, COL_EVENTS, db);
        userEvents = new Table(TABLE_USER_EVENTS, COL_USER_EVENTS, db);
        classList = new Table(TABLE_CLASS_LIST, COL_CLASS_LIST, db);
        classes = new Table(TABLE_CLASSES, COL_CLASSES, db);

        // uncomment if database refuses to re-create
//        deleteDatabase(db);
        onCreate(db);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        if(people != null)
            people.makeTable();
        if(events != null)
            events.makeTable();
        if(userEvents != null)
            userEvents.makeTable();
        if(classList != null)
            classList.makeTable();
        if(classes != null)
            classes.makeTable();
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // What to do upon upgrading the database schema
        deleteDatabase(db);
        onCreate(db);
    }

    private void deleteDatabase(SQLiteDatabase db) {
        if(people != null)
            people.deleteTable();
        if(events != null)
            events.deleteTable();
        if(userEvents != null)
            userEvents.makeTable();
        if(classList != null)
            classList.deleteTable();
        if(classes != null)
            classes.deleteTable();
    }

    // ----------------------------------- Queries -------------------------------------------- //

    public ArrayList<Event> getEventList() {

        String[] projection = {
                COL_EVENTS[0].getColumnName(),
                COL_EVENTS[1].getColumnName(),
                COL_EVENTS[2].getColumnName(),
                COL_EVENTS[3].getColumnName(),
                COL_EVENTS[4].getColumnName(),
                COL_EVENTS[5].getColumnName(),
                COL_EVENTS[6].getColumnName(),
                COL_EVENTS[7].getColumnName()
        };

        String query = "SELECT ";

        for(int i = 0; i < projection.length; i++) {
            query += projection[i];

            if(i != projection.length-1)
                query += ",";
        }

        query += " FROM " + TABLE_EVENTS;

        ArrayList<Event> events = new ArrayList<>();

        Cursor c = db.rawQuery(query, null);

        boolean cursorOK = c.moveToFirst();

        ArrayList<Course> courseList = getAllCourses();
        ArrayList<Integer> courses = getClasses();

        while(cursorOK) {
            // populate events list
            int id, courseID;
            String day, start, end, room, campus, teacher;

            String courseName = null, courseCode = null;

            id = c.getInt(0);
            courseID = c.getInt(1);
            teacher = c.getString(2);
            day = c.getString(3);
            start = c.getString(4);
            end = c.getString(5);
            room = c.getString(6);
            campus = c.getString(7);

            for(Course item : courseList) {
                if(item.getId() == courseID) {
                    courseName = item.getCourseName();
                    courseCode = item.getCourseCode();
                }
            }

            if(courseName != null && courseCode != null && courses.contains(courseID)) {
                Event event = new Event(id, courseID, teacher, day, start, end, room, campus, courseName, courseCode);
                events.add(event);
            }

            cursorOK = c.moveToNext();
        }
        c.close();

        return events;
    }

    public ArrayList<Course> getAllCourses() {
        String[] projection = {
                COL_CLASSES[0].getColumnName(),
                COL_CLASSES[1].getColumnName(),
                COL_CLASSES[2].getColumnName()
        };

        String query = "SELECT ";

        for(int i = 0; i < projection.length; i++) {
            query += projection[i];

            if(i != projection.length-1)
                query += ",";
        }

        query += " FROM " + TABLE_CLASSES;

        Cursor c = db.rawQuery(query, null);

        ArrayList<Course> courses = new ArrayList<>();

        boolean cursorOK = c.moveToFirst();

        while (cursorOK) {

            int id;
            String courseName, courseCode;

            id = c.getInt(0);
            courseName = c.getString(1);
            courseCode = c.getString(2);

            Course course = new Course(id, courseName, courseCode);

            courses.add(course);

            cursorOK = c.moveToNext();
        }

        return courses;
    }

    public ArrayList<User> getAllUsers() {
        String[] projection = {
                COL_PEOPLE[0].getColumnName(),
                COL_PEOPLE[1].getColumnName(),
                COL_PEOPLE[2].getColumnName(),
                COL_PEOPLE[3].getColumnName()
        };

        String query = "SELECT ";

        for(int i = 0; i < projection.length; i++) {
            query += projection[i];

            if(i != projection.length-1)
                query += ",";
        }

        query += " FROM " + TABLE_PEOPLE;

        Cursor c = db.rawQuery(query, null);

        ArrayList<User> users = new ArrayList<>();

        boolean cursorOK = c.moveToFirst();

        while (cursorOK) {

            // create and add course
            int id;
            String name, password, accountType;

            id = c.getInt(0);
            name = c.getString(1);
            password = c.getString(2);
            accountType = c.getString(3);

            User user = new User(id, name, password, accountType);

            users.add(user);

            cursorOK = c.moveToNext();
        }

        return users;
    }

    public ArrayList<Integer> getClasses() {
        String[] projection = {
                COL_CLASS_LIST[1].getColumnName(),
                COL_CLASS_LIST[2].getColumnName()
        };

        String query = "SELECT ";

        for(int i = 0; i < projection.length; i++) {
            query += projection[i];

            if(i != projection.length-1)
                query += ",";
        }

        query += " FROM " + TABLE_CLASS_LIST;

        Cursor c = db.rawQuery(query, null);

        boolean cursorOK = c.moveToFirst();

        ArrayList<Integer> ids = new ArrayList<>();

        while (cursorOK) {
            int courseID, studentID;
            courseID = c.getInt(0);
            studentID = c.getInt(1);

            if(studentID == MainActivity.userID)
                ids.add(courseID);

            cursorOK = c.moveToNext();
        }

        return ids;
    }
}
