package ca.delilaheve.timetable.database;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.ArrayList;

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
            new Column("classID", Column.COL_TYPE_INT, false),
            new Column("teacherID", Column.COL_TYPE_INT, false),
            new Column("day", Column.COL_TYPE_TEXT, false),
            new Column("startTime", Column.COL_TYPE_TEXT, false),
            new Column("endTime", Column.COL_TYPE_TEXT, false),
            new Column("location", Column.COL_TYPE_TEXT, false),
            new Column("campus", Column.COL_TYPE_TEXT, false)
    };


    public static final String TABLE_CLASS_LIST = "classList";
    public static final Column[] COL_CLASS_LIST = {
            new Column("_id", Column.COL_TYPE_INT, true),
            new Column("classID", Column.COL_TYPE_INT, false),
            new Column("studentID", Column.COL_TYPE_INT, false)
    };


    public static final String TABLE_CLASSES = "class";
    public static final Column[] COL_CLASSES = {
            new Column("_id", Column.COL_TYPE_INT, true),
            new Column("className", Column.COL_TYPE_INT, false),
            new Column("courseCode", Column.COL_TYPE_INT, false)
    };


    public Table people;
    public Table events;
    public Table classList;
    public Table classes;

    // Increment upon schema change to DB:
    public static final int DATABASE_VERSION = 1;

    public static final String DATABASE_NAME = "Timetable.db";

    private SQLiteDatabase db;

    public Database(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);

        db = getWritableDatabase();

        people = new Table(TABLE_PEOPLE, COL_PEOPLE, db);
        events = new Table(TABLE_EVENTS, COL_EVENTS, db);
        classList = new Table(TABLE_CLASS_LIST, COL_CLASS_LIST, db);
        classes = new Table(TABLE_CLASSES, COL_CLASSES, db);

    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        people.makeTable();
        events.makeTable();
        classList.makeTable();
        classes.makeTable();
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // What to do upon upgrading the database schema
    }

    // ----------------------------------- Queries -------------------------------------------- //

    public ArrayList<Event> getEventList() {
        String[] projection = {
                TABLE_CLASSES + "." + COL_CLASSES[0].getColumnName(),
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

        query += " FROM ";

        ArrayList<Event> events = new ArrayList<>();

        //

        return events;
    }

}
