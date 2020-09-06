package com.diet.trinity.Utility;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class FoodDatabaseHelper extends SQLiteOpenHelper {
    public static final String DATABASE_NAME="FoodItems.db";
    public static final String TABLE_NAME="FoodItem";
    public static final String COL_1="ID";
    public static final String COL_2="foodname";
    public static final String COL_3="carbon";
    public static final String COL_4="protein";
    public static final String COL_5="fat";
    public static final String COL_6="gram";
    public static final String COL_7="categoryid";
    public FoodDatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, 1);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("CREATE TABLE " + TABLE_NAME + " (ID INTEGER ,foodname TEXT,carbon TEXT, protein TEXT, fat TEXT, gram TEXT, categoryid TEXT)");
    }
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " +TABLE_NAME);
        onCreate(db);
    }
}