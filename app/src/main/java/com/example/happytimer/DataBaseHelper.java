package com.example.happytimer;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import java.util.ArrayList;

public class DataBaseHelper extends SQLiteOpenHelper {

    private static final String TAG = "SQLite";

    // Database Version
    private static final int DATABASE_VERSION = 1;

    // Database Name
    private static final String DATABASE_NAME = "Timer_List";

    // Table name: timer.
    private static final String TABLE_TIMER = "Timer";
    private static final String TABLE_PERIOD = "Period";

    private static final String COLUMN_TIMER_ID ="Timer_Id";
    private static final String COLUMN_TIMER_TITLE ="Timer_Title";
    private static final String COLUMN_TIMER_COLOUR = "Timer_Colour";
    private static final String COLUMN_PERIOD_ID ="Period_Id";
    private static final String COLUMN_PERIOD_TITLE ="Period_Title";
    private static final String COLUMN_PERIOD_TIMER = "Period_Timer";

    public DataBaseHelper(Context context) {

        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {

        // Script to create table.
        String script = "CREATE TABLE " + TABLE_TIMER + "("
                + COLUMN_TIMER_ID + " INTEGER PRIMARY KEY AUTOINCREMENT," + COLUMN_TIMER_TITLE + " TEXT,"
                + COLUMN_TIMER_COLOUR + " TEXT" + ")";
        String script2 = "CREATE TABLE "+ TABLE_PERIOD + "("
                + COLUMN_PERIOD_ID + " INTEGER PRIMARY KEY AUTOINCREMENT," + COLUMN_PERIOD_TITLE + " TEXT,"
                + COLUMN_PERIOD_TIMER + " INTEGER," + COLUMN_TIMER_ID + " REFERENCES Timer(Timer_Id)" + ")";
        // Execute script.
        db.execSQL(script);
        db.execSQL(script2);
    }


    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

        // Drop table
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_TIMER);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_PERIOD);


        // Recreate
        onCreate(db);
    }

    public void addTimer(Timer timer) {
        Log.i(TAG, "MyDatabaseHelper.addTimer ... " + timer.getTitle());

        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(COLUMN_TIMER_TITLE, timer.getTitle());
        values.put(COLUMN_TIMER_COLOUR, timer.getColour());

        // Inserting Row
        db.insert(TABLE_TIMER, null, values);

        // Closing database connection
        db.close();
    }

    public void addPeriod(Period period) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(COLUMN_PERIOD_TITLE, period.getTitle());
        values.put(COLUMN_PERIOD_TIMER, period.getSeconds());
        values.put(COLUMN_TIMER_ID, period.getTimerID());

        // Inserting Row
        db.insert(TABLE_PERIOD, null, values);

        // Closing database connection
        db.close();
    }

    public Timer getTimer(int id) {
        Log.i(TAG, "MyDatabaseHelper.getTimer ... " + id);

        SQLiteDatabase db = this.getReadableDatabase();

        Cursor cursor = db.query(TABLE_TIMER, new String[] { COLUMN_TIMER_ID,
                        COLUMN_TIMER_TITLE, COLUMN_TIMER_COLOUR }, COLUMN_TIMER_ID + "=?",
                new String[] { String.valueOf(id) }, null, null, null, null);
        if (cursor != null)
            cursor.moveToFirst();

        Timer timer = new Timer(Integer.parseInt(cursor.getString(0)),
                cursor.getString(1),
                cursor.getString(2));
        // return timer
        return timer;
    }

    public Period getPeriod(int id) {
        Log.i(TAG, "MyDatabaseHelper.getPeriod ... " + id);

        SQLiteDatabase db = this.getReadableDatabase();

        Cursor cursor = db.query(TABLE_PERIOD, new String[] { COLUMN_PERIOD_ID,
                        COLUMN_PERIOD_TITLE, COLUMN_PERIOD_TIMER,COLUMN_TIMER_ID }, COLUMN_PERIOD_ID + "=?",
                new String[] { String.valueOf(id) }, null, null, null, null);
        if (cursor != null)
            cursor.moveToFirst();

        Period period = new Period(Integer.parseInt(cursor.getString(0)),
                cursor.getString(1),
                cursor.getInt(2),
                cursor.getInt(3));
        // return timer
        return period;
    }

    public int getNotesCount() {
        Log.i(TAG, "MyDatabaseHelper.getTimersCount ... " );

        String countQuery = "SELECT  * FROM " + TABLE_TIMER;
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(countQuery, null);

        int count = cursor.getCount();

        cursor.close();

        // return count
        return count;
    }

    public int updateTimer(Timer timer) {
        Log.i(TAG, "MyDatabaseHelper.updateTimer ... "  + timer.getTitle());

        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(COLUMN_TIMER_TITLE, timer.getTitle());
        values.put(COLUMN_TIMER_COLOUR, timer.getColour());

        // updating row
        return db.update(TABLE_TIMER, values, COLUMN_TIMER_ID + " = ?",
                new String[]{String.valueOf(timer.getId())});
    }

    public ArrayList<Timer> getAllTimers() {
        Log.i(TAG, "MyDatabaseHelper.getAllTimers ... " );

        ArrayList<Timer> timerList = new ArrayList<Timer>();
        // Select All Query
        String selectQuery = "SELECT  * FROM " + TABLE_TIMER;

        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);

        // looping through all rows and adding to list
        if (cursor.moveToFirst()) {
            do {
                Timer timer = new Timer();
                timer.setId(Integer.parseInt(cursor.getString(0)));
                timer.setTitle(cursor.getString(1));
                timer.setColour(cursor.getString(2));
                // Adding note to list
                timerList.add(timer);
            } while (cursor.moveToNext());
        }

        // return note list
        return timerList;
    }

    public ArrayList<Period> getPeriods(int id) {
        ArrayList<Period> periodList = new ArrayList<Period>();
        // Select All Query
       //String selectQuery = "SELECT * FROM " + TABLE_PERIOD;
        String selectQuery = "SELECT * FROM " + TABLE_PERIOD +" WHERE "+COLUMN_TIMER_ID+" = " + id;

        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);

        // looping through all rows and adding to list
        if (cursor.moveToFirst()) {
            do {
                Period period = new Period();
                period.setId(Integer.parseInt(cursor.getString(0)));
                period.setTitle(cursor.getString(1));
                period.setSeconds(cursor.getInt(2));
                period.setTimerID(cursor.getInt(3));
                // Adding note to list
                periodList.add(period);
            } while (cursor.moveToNext());
        }


        cursor.close();
        return periodList;
    }

    public void deleteTimer(Timer timer) {
        Log.i(TAG, "MyDatabaseHelper.updateTimer ... " + timer.getTitle() );

        SQLiteDatabase db = this.getWritableDatabase();
        ArrayList<Period> periods = getPeriods(timer.getId());
        for(Period p : periods)
        {
            db.delete(TABLE_PERIOD, COLUMN_PERIOD_ID + " = ?",
                    new String[] { String.valueOf(p.getId()) });
        }
        db.delete(TABLE_TIMER, COLUMN_TIMER_ID + " = ?",
                new String[] { String.valueOf(timer.getId()) });
        db.close();
    }

    public void deletePeriod(Period period) {
        SQLiteDatabase db = this.getWritableDatabase();

        db.delete(TABLE_PERIOD, COLUMN_PERIOD_ID + " = ?",
                new String[] { String.valueOf(period.getId()) });
        db.close();
    }

}
