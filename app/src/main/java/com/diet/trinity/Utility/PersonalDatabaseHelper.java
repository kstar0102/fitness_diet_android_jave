package com.diet.trinity.Utility;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class PersonalDatabaseHelper extends SQLiteOpenHelper {
    public static final String DATABASE_NAME="PersonalDatabase.db";
    public static final String TABLE_NAME="Personal";
    public static final String COL_1 = "ID";
    public static final String COL_2 = "weight";
    public static final String COL_3 = "height";
    public static final String COL_4 = "waist";
    public static final String COL_5 = "neck";
    public static final String COL_6 = "thigh";//hips
    public static final String COL_7 = "weekly_units";
    public static final String COL_8 = "daily_units";
    public static final String COL_9 = "date";
    public static final String COL_10 = "days";
    public static final String COL_11 = "next_units";
    public static final String COL_12 = "user_id";
    public PersonalDatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, 1);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("CREATE TABLE " + TABLE_NAME + " (ID INTEGER PRIMARY KEY AUTOINCREMENT, weight TEXT, height TEXT, waist TEXT, neck TEXT, thigh TEXT, weekly_units TEXT, daily_units TEXT, date TEXT, days TEXT, next_units INTEGER, user_id TEXT)");
    }
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " +TABLE_NAME);
        onCreate(db);
    }
}
