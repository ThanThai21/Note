package com.esp.note;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.DatabaseErrorHandler;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by Admin on 20/6/2017.
 */

public class Database extends SQLiteOpenHelper {

    private final static String DB_NAME = "note.db";
    private final static String TABLE_NAME = "Note";
    private final static String ID_NOTE = "Id";
    private final static String TITLE_NOTE = "Title";
    private final static String CONTENT_NOTE = "Content";


    public Database(Context context, String name, SQLiteDatabase.CursorFactory factory, int version) {
        super(context, DB_NAME, null, version);
    }

    public Database(Context context, String name, SQLiteDatabase.CursorFactory factory, int version, DatabaseErrorHandler errorHandler) {
        super(context, DB_NAME, null, version, errorHandler);
    }

    public void createTable() {
        String sql = "CREATE TABLE IF NOT EXISTS " + TABLE_NAME + " (" +
                ID_NOTE + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                TITLE_NOTE + " TEXT, " +
                CONTENT_NOTE + " TEXT)";

        SQLiteDatabase database = getWritableDatabase();
        database.execSQL(sql);

    }

    public boolean insertRecord(String title, String content) {
        ContentValues values = new ContentValues();
        values.put(TITLE_NOTE, title);
        values.put(CONTENT_NOTE, content);
        SQLiteDatabase database = getWritableDatabase();
        long index = database.insert(TABLE_NAME, null, values);
        return index > 0;
    }

    public boolean updateRecord(String id, String title, String content) {
        SQLiteDatabase database = getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(TITLE_NOTE, title);
        values.put(CONTENT_NOTE, content);
        long index = database.update(TABLE_NAME, values, ID_NOTE + " = " + id, null);
        return index > 0;
    }

    public boolean deleteRecord(String id) {
        SQLiteDatabase database = getWritableDatabase();
        long index = database.delete(TABLE_NAME, ID_NOTE + " = " + id, null);
        return index > 0;
    }

    public Cursor query() {
        String sql = "SELECT * FROM " + TABLE_NAME;
        SQLiteDatabase database = getReadableDatabase();
        return database.rawQuery(sql, null);
    }


    @Override
    public void onCreate(SQLiteDatabase db) {

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }
}
