package com.example.slothgoldencare;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import java.sql.SQLDataException;

public class DataBaseManager {
    private DataBaseHelper dbHelper;
    private Context context;
    private SQLiteDatabase database;
    String[] argss = null;

    public DataBaseManager(Context ctx){
        context = ctx;
    }

    public DataBaseManager open() throws SQLDataException {
        dbHelper = new DataBaseHelper(context);
        database = dbHelper.getWritableDatabase();
        return this;
    }

    public void close(){
        dbHelper.close();
    }

    public void insert (String ID, String username, String phoneNumber){
        ContentValues contentValues = new ContentValues();
        contentValues.put(DataBaseHelper.USER_ID, ID);
        contentValues.put(DataBaseHelper.USER_NAME, username);
        contentValues.put(DataBaseHelper.USER_PHONE, phoneNumber);
        database.insert(DataBaseHelper.DB_TABLE,null, contentValues );
    }

    public Cursor fetch(){
        String[] columns = new String[] {DataBaseHelper.USER_ID, DataBaseHelper.USER_NAME, DataBaseHelper.USER_PHONE};
        Cursor cursor = database.query(DataBaseHelper.DB_TABLE, columns, null, null, null, null, null);
        if (cursor != null){
            cursor.moveToFirst();
        }
        return cursor;
    }

    public int update(long _id, String username, String phoneNumber){
        ContentValues contentValues = new ContentValues();
        contentValues.put(DataBaseHelper.USER_NAME, username);
        contentValues.put(DataBaseHelper.USER_PHONE, phoneNumber);
        int ret = database.update(DataBaseHelper.DB_TABLE, contentValues, DataBaseHelper.USER_ID + "=" +_id, null );
        return ret;
    }

    public void delete( long _id){
        database.delete(DataBaseHelper.DB_TABLE, DataBaseHelper.USER_ID + "=" +_id, null);
    }


}
