package com.example.slothgoldencare;

import android.content.Context;
import android.database.sqlite.SQLiteOpenHelper;
import android.database.sqlite.SQLiteDatabase;
import android.support.annotation.Nullable;

public class DataBaseHelper extends SQLiteOpenHelper {

    static final String DB_NAME = "GOLDEN_CARE.DB";
    static final int DB_VERSION = 1;
    static final String DB_TABLE = "USERES";
    static final String USER_ID = "_ID";
    static final String USER_NAME = "user_name";
    static final String USER_PHONE = "user_phone";

    private static final String  CREATE_DB_QUERY = "CREATE TABLE " + DB_TABLE + " ( " + USER_ID + "INTEGER PRIMARY KEY, " + USER_ID + " TEXT NOT NULL, " +
            USER_NAME + " TEXT NOT NULL, " + USER_PHONE + " TEXT NOT NULL " + " );";




    public DataBaseHelper(Context context) {
        super(context, DB_NAME,  null , DB_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(CREATE_DB_QUERY);

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + DB_TABLE);

    }
}

