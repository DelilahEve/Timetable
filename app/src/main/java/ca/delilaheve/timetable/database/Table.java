package ca.delilaheve.timetable.database;

import android.content.ContentValues;
import android.database.sqlite.SQLiteDatabase;

public class Table {

    private String tableName;

    private Column[] columns;

    private SQLiteDatabase db;

    public Table(String tableName, Column[] columns, SQLiteDatabase db) {
        this.tableName = tableName;
        this.columns = columns;

        this.db = db;
    }

    public void makeTable() {
        String sql = "CREATE TABLE '" + tableName + "' (";

        for (Column column : columns)
            sql += column.getColumnDefinition() + ",";

        sql += ")";

        db.execSQL(sql);
    }

    public void add(ContentValues values) {
        db.insert(tableName, null, values);
    }

    public void update(ContentValues values, int rowID) {
        db.update(tableName, values, columns[0].getColumnName() + "=" + rowID, null);
    }

    public void delete(int rowID) {
        db.delete(tableName, columns[0].getColumnName() + "=" + rowID, null);
    }

    public Column[] getColumns() {
        return columns;
    }
}
