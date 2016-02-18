package ca.delilaheve.timetable.database;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.ArrayList;

import ca.delilaheve.timetable.MainActivity;
import ca.delilaheve.timetable.data.Event;

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
            new Column("teacherID", Column.COL_TYPE_INT, false),
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
    public static final int DATABASE_VERSION = 4;

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
                TABLE_CLASS_LIST + "." + COL_CLASS_LIST[1].getColumnName(),
                TABLE_CLASS_LIST + "." + COL_CLASS_LIST[2].getColumnName(),
                TABLE_CLASSES + "." + COL_CLASSES[1].getColumnName(),
                TABLE_CLASSES + "." + COL_CLASSES[2].getColumnName(),
                TABLE_EVENTS + "." + COL_EVENTS[0].getColumnName(),
                TABLE_EVENTS + "." + COL_EVENTS[1].getColumnName(),
                TABLE_EVENTS + "." + COL_EVENTS[2].getColumnName(),
                TABLE_EVENTS + "." + COL_EVENTS[3].getColumnName(),
                TABLE_EVENTS + "." + COL_EVENTS[4].getColumnName(),
                TABLE_EVENTS + "." + COL_EVENTS[5].getColumnName(),
                TABLE_EVENTS + "." + COL_EVENTS[6].getColumnName(),
                TABLE_EVENTS + "." + COL_EVENTS[7].getColumnName()
        };

        String query = "SELECT ";

        for(int i = 0; i < projection.length; i++) {
            query += projection[i];

            if(i != projection.length-1)
                query += ",";
        }

        query += " FROM " + TABLE_CLASS_LIST
                + " INNER JOIN " + TABLE_EVENTS + " ON "
                + TABLE_CLASS_LIST + "." + COL_CLASS_LIST[2].getColumnName()
                + "="
                + TABLE_EVENTS + "." + COL_EVENTS[1].getColumnName()
                + " INNER JOIN " + TABLE_CLASSES + " ON "
                + TABLE_CLASS_LIST + "." + COL_CLASS_LIST[2].getColumnName()
                + "="
                + TABLE_CLASSES + "." + COL_CLASSES[0].getColumnName();

        ArrayList<Event> events = new ArrayList<>();

        Cursor c = db.rawQuery(query, null);

        boolean cursorOK = c.moveToFirst();

        while(cursorOK) {
            // populate events list
            int id, classID, teacherID, studentID;
            String day, start, end, room, campus;

            String courseName, courseCode;

            courseName = c.getString(2);
            courseCode = c.getString(3);

            studentID = c.getInt(1);

            id = c.getInt(4);
            classID = c.getInt(5);
            teacherID = c.getInt(6);
            day = c.getString(7);
            start = c.getString(8);
            end = c.getString(9);
            room = c.getString(10);
            campus = c.getString(11);

            Event event = new Event(id, classID, teacherID, day, start, end, room, campus, courseName, courseCode);

            if(studentID == MainActivity.userID)
                events.add(event);

            cursorOK = c.moveToNext();
        }


        return null;
    }

}
