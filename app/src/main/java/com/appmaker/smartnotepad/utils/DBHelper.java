package com.appmaker.smartnotepad.utils;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.appmaker.smartnotepad.model.Note;

import java.util.ArrayList;
import java.util.List;


//This is the place where I put logic for saving
//notes to the database. It is used for create
//update and deleting notes.
public class DBHelper extends SQLiteOpenHelper {

    private static final String DATABASE_NAME = "smart_note.db";
    private static final String NOTE_TB = "notes";
    private static final String NOTE_TB_ID = "id";
    private static final String NOTE_TB_TIME = "time";
    private static final String NOTE_TB_TEXT = "detail";
    private static DBHelper helper;
    private static SQLiteDatabase database;

    private static final int DATABASE_VERSION = 1;
    private static final String SQL_CREATE_NOTE_TB =
            "CREATE TABLE " + NOTE_TB + " (" +
                    NOTE_TB_ID + " INTEGER PRIMARY KEY AUTOINCREMENT," +
                    NOTE_TB_TEXT + " TEXT," +
                    NOTE_TB_TIME + " INTEGER)";

    private static final String SQL_DELETE_DEL_TB =
            "DROP TABLE IF EXISTS " + NOTE_TB;



    private DBHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    //This method updates notes
    public boolean updateNote(int id, String text)
    {
        ContentValues cv = new ContentValues();
        cv.put(NOTE_TB_TEXT, text);
        return database.update(NOTE_TB, cv, NOTE_TB_ID + "=?", new String[]{String.valueOf(id)}) != -1;
    }

    public static synchronized DBHelper getInstance(Context context)
    {
        if(helper == null)
        {
            helper = new DBHelper(context);
            database = helper.getWritableDatabase();
        }

        return helper;
    }


    //This method is used for selecting all notes.
    public List<Note> getAvailableNotes()
    {
        List<Note> list = new ArrayList<>();
        String[] projection = {
                NOTE_TB_ID,
                NOTE_TB_TEXT,
                NOTE_TB_TIME
        };

        String sortOrder =
                NOTE_TB_TIME + " DESC";
        Cursor cursor = database.query(
                NOTE_TB,
                projection,
                null,
                null,
                null,
                null,
                sortOrder
        );

        if(cursor.moveToFirst())
        {
            do {
                int id = cursor.getInt(0);
                String text = cursor.getString(1);
                long time = cursor.getLong(2);

                Note note = new Note(id, text, time);
                list.add(note);


            }while(cursor.moveToNext());

            return list;
        }
        return null;
    }

    public synchronized void closeDB()
    {
        database.close();
        helper = null;
    }

    //This method is used for deleting notes.
    public void deleteNote(int id)
    {
        String whereClause = NOTE_TB_ID +"=?";
        String[] whereArgs = new String[] { String.valueOf(id) };
        database.delete(NOTE_TB, whereClause, whereArgs);
    }

    //this method is used for saving notes.
    public boolean saveNote(Note note)
    {
        ContentValues values = new ContentValues();
        values.put(NOTE_TB_TEXT, note.getText());
        values.put(NOTE_TB_TIME, note.getTime());
        return database.insert(NOTE_TB, null, values) != -1;
    }


    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        sqLiteDatabase.execSQL(SQL_CREATE_NOTE_TB);
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {
        sqLiteDatabase.execSQL(SQL_DELETE_DEL_TB);
        onCreate(sqLiteDatabase);
    }
}
