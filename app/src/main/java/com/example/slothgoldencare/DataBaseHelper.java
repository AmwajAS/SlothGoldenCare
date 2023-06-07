package com.example.slothgoldencare;

import android.annotation.SuppressLint;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteOpenHelper;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;
import android.widget.ArrayAdapter;

import androidx.annotation.Nullable;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class DataBaseHelper extends SQLiteOpenHelper {
    private static final String TAG = "DBHelper";
    static final String DB_NAME = "GOLDEN_CARE.DB";
    static final int DB_VERSION = 1;
    /*
    Users Table info.
     */
    static final String DB_TABLE = "USERES";
    private static final String COLUMN_ID = "rowID";
    static final String USER_ID = "_ID";
    static final String USER_NAME = "user_name";
    static final String USER_PHONE = "user_phone";

    /*
      Elders Table info.
       */
    static final String ELDER_TBL = "ELDERS";
    static final String ELDER_ID = "ID";
    static final String ELDER_NAME = "name";
    static final String ELDER_PHONE = "phone";
    static final String ELDER_DOB = "dateOfBirth";
    static final String ELDER_GENDER = "gender";

     /*
    Elders Relatives Table info.
     */

    static final String ELD_REL_TBL = "ELDER_RELATIVE";
    static final String ELD_ID = "ElderID";
    static final String REL_ID = "RelativeID";
    static final String RELATION = "Relation";
    /*
    creating the DB Tables Queries:
     */
    private static final String CREATE_DB_QUERY_USER = "CREATE TABLE " + DB_TABLE + " ( " + USER_ID + "INTEGER PRIMARY KEY, " + USER_ID + " TEXT NOT NULL, " +
            USER_NAME + " TEXT NOT NULL, " + USER_PHONE + " TEXT NOT NULL UNIQUE" + " );";

    private static final String CREATE_DB_QUERY_ELDER = "CREATE TABLE " + ELDER_TBL + " ( " + ELDER_ID + " INTEGER PRIMARY KEY, " + ELDER_ID + " TEXT NOT NULL, " +
            ELDER_NAME + " TEXT NOT NULL, " + ELDER_PHONE + " TEXT NOT NULL UNIQUE, " + ELDER_DOB + " TEXT NOT NULL CHECK(" +
            ELDER_DOB + " <= date('now')), " + ELDER_GENDER + " TEXT NOT NULL CHECK(" +
            ELDER_GENDER + " IN ('Male', 'Female', 'Other'))" + " );";

    private static final String CREATE_DB_QUERY_USER_ELDER = "CREATE TABLE " + ELD_REL_TBL + " ( " + ELD_ID + "INTEGER PRIMARY KEY, " + REL_ID + " INTEGER PRIMARY KEY, " +
            RELATION + " TEXT NOT NULL " + " );";


    public DataBaseHelper(Context context) {
        super(context, DB_NAME, null, DB_VERSION);
    }


    @Override
    public void onCreate(SQLiteDatabase db) {
        //db.execSQL(CREATE_DB_QUERY_ELDER);
        //  db.execSQL(CREATE_DB_QUERY_USER_ELDER);
        //  db.execSQL(CREATE_DB_QUERY_USER);
        //


    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        //db.execSQL("DROP TABLE IF EXISTS " + ELDER_TBL);

    }
    /*
    this method inserting data to the USER table in the DB.
     */

    public boolean addUserData(User user) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        //String id = "";
        contentValues.put(USER_ID, String.valueOf(user.getID().toString()));
        contentValues.put(USER_NAME, String.valueOf(user.getUsername().toString()));
        contentValues.put(USER_PHONE, String.valueOf(user.getPhoneNumber().toString()));

        long res = db.insert(DB_TABLE, null, contentValues);
        if (res == -1) {
            return false;
        } else {
            Log.i(TAG, "This is a debug message " + user.getID().toString() + user.getUsername().toString() + user.getPhoneNumber().toString()); // Debug log

            return true;
        }
    }

    public boolean addElderData(Elder elder) {

        SQLiteDatabase db = this.getWritableDatabase();
        onCreate(db);
        ContentValues contentValues = new ContentValues();

        contentValues.put(ELDER_ID, String.valueOf(elder.getID().toString()));
        contentValues.put(ELDER_NAME, String.valueOf(elder.getUsername().toString()));
        contentValues.put(ELDER_PHONE, String.valueOf(elder.getPhoneNumber().toString()));
        contentValues.put(ELDER_DOB, String.valueOf(elder.formatDateOfBirth(elder.getDOB())));
        contentValues.put(ELDER_GENDER, String.valueOf(elder.getGender().toString()));

        long res = db.insert(ELDER_TBL, null, contentValues);
        if (res == -1) {
            return false;
        } else {
            Log.i(TAG, "This is a debug message " + elder.getID().toString() + elder.getUsername().toString() + elder.getPhoneNumber().toString()); // Debug log

            return true;
        }
    }

    public boolean checkUserID(String uid) {
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery("SELECT * FROM USERES WHERE _ID=?", new String[]{uid});
        if (cursor.getCount() > 0) {
            Log.i(TAG, "This is a debug message " + " uid is exist"); // Debug log
            return true;
        } else {
            Log.i(TAG, "This is a debug message " + " uid is NOT exist"); // Debug log
            return false;
        }
    }

    public boolean checkElderID(String uid) {
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery("SELECT * FROM ELDERS WHERE ID=?", new String[]{uid});
        if (cursor.getCount() > 0) {
            Log.i(TAG, "This is a debug message " + " Elder ID is exist"); // Debug log
            return true;
        } else {
            Log.i(TAG, "This is a debug message " + " Elder ID is NOT exist"); // Debug log
            return false;
        }
    }

    public User findUserByID(String uid) {
        SQLiteDatabase db = this.getReadableDatabase();

        String[] projection = {
                "_ID",
                "user_name",
                "user_phone"
        };

        String selection = "_ID = ?";
        String[] selectionArgs = {uid};

        Cursor cursor = db.query(
                "USERES",
                projection,
                selection,
                selectionArgs,
                null,
                null,
                null
        );

        User user = null;
        if (cursor != null && cursor.moveToFirst()) {
            String userID = cursor.getString(cursor.getColumnIndexOrThrow("_ID"));
            String name = cursor.getString(cursor.getColumnIndexOrThrow("user_name"));
            String phone = cursor.getString(cursor.getColumnIndexOrThrow("user_phone"));
            user = new User(userID, name, phone);
        }

        if (cursor != null) {
            cursor.close();
        }
        db.close();

        return user;
    }

    public Elder findElderByID(String uid) {
        SQLiteDatabase db = this.getReadableDatabase();

        String[] projection = {
                "ID",
                "name",
                "phone",
                "dateOfBirth",
                "gender"

        };

        String selection = "ID = ?";
        String[] selectionArgs = {uid};

        Cursor cursor = db.query(
                "ELDERS",
                projection,
                selection,
                selectionArgs,
                null,
                null,
                null
        );

        Elder elder = null;
        if (cursor != null && cursor.moveToFirst()) {
            String userID = cursor.getString(cursor.getColumnIndexOrThrow("ID"));
            String name = cursor.getString(cursor.getColumnIndexOrThrow("name"));
            String phone = cursor.getString(cursor.getColumnIndexOrThrow("phone"));
            String date = cursor.getString(cursor.getColumnIndexOrThrow("dateOfBirth"));
            String gender = cursor.getString(cursor.getColumnIndexOrThrow("gender"));

            elder = new Elder(userID, name, phone, ElderSignupActivity.convertStringIntoDate(date), Elder.GenderConvertor(gender));
        }

        if (cursor != null) {
            cursor.close();
        }
        db.close();

        return elder;
    }


    public void deleteAllData() {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(DB_TABLE, null, null);
        db.close();
    }


    // Method to drop the table
    public void dropTable() {
        // Get a writable database
        SQLiteDatabase db = this.getWritableDatabase();

        // Drop the table
        db.execSQL("DROP TABLE IF EXISTS " + ELDER_TBL);

        // Close the database connection
        db.close();
    }


    public void deleteUserById(String userId) {
        SQLiteDatabase db = getWritableDatabase();
        String whereClause = "ID = ?";
        String[] whereArgs = {String.valueOf(userId)};
        int rowsDeleted = db.delete("ELDERS", whereClause, whereArgs);
        db.close();

        // Optional: Check the number of rows deleted for verification
        if (rowsDeleted > 0) {
            // Rows deleted successfully
        } else {
            // No rows deleted or an error occurred
        }
    }

    public List<User> getUsers() {
        List<User> userList = new ArrayList<>();

        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.query("USERES", null, null, null, null, null, null);

        if (cursor.moveToFirst()) {
            do {
                @SuppressLint("Range") String id = cursor.getString(cursor.getColumnIndex("_ID"));
                @SuppressLint("Range") String name = cursor.getString(cursor.getColumnIndex("user_name"));
                @SuppressLint("Range") String phone = cursor.getString(cursor.getColumnIndex("user_phone"));

                User user = new User(id, name, phone);
                userList.add(user);
            } while (cursor.moveToNext());
        }

        cursor.close();
        db.close();

        return userList;
    }

    public List<Elder> getElders() {
        List<Elder> eldersList = new ArrayList<>();

        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.query("ELDERS", null, null, null, null, null, null);

        if (cursor.moveToFirst()) {
            do {
                String userID = cursor.getString(cursor.getColumnIndexOrThrow("ID"));
                String name = cursor.getString(cursor.getColumnIndexOrThrow("name"));
                String phone = cursor.getString(cursor.getColumnIndexOrThrow("phone"));
                String date = cursor.getString(cursor.getColumnIndexOrThrow("dateOfBirth"));
                String gender = cursor.getString(cursor.getColumnIndexOrThrow("gender"));
                Log.i(TAG, "This is the date 1:" + date);
                Elder elder = new Elder(userID, name, phone, ElderSignupActivity.convertStringIntoDate(date), Elder.GenderConvertor(gender));
                Log.i(TAG, "This is the date 2:" + elder.getDOB());
                eldersList.add(elder);
            } while (cursor.moveToNext());
        }

        cursor.close();
        db.close();

        return eldersList;
    }

    public void updateUserInfo(String userId, String newName, String newPhoneNumber) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put("user_name", newName);
        values.put("user_phone", newPhoneNumber);

        String whereClause = "_ID = ?";
        String[] whereArgs = {String.valueOf(userId)};

        db.update("USERES", values, whereClause, whereArgs);

        db.close();
    }


    public void updateElderInfo(String elderId, String newName, String newPhoneNumber, String newDateOfBirth,String newGender) {
        SQLiteDatabase db = this.getWritableDatabase();
       Log.i(TAG, "This is the USER IN DB:" + elderId + newName + newPhoneNumber + newDateOfBirth +newGender);

        ContentValues values = new ContentValues();
        values.put("name", newName);
        values.put("phone", newPhoneNumber);
        values.put("dateOfBirth", newDateOfBirth);
        values.put("gender", newGender);

        String whereClause = "ID = ?";
        String[] whereArgs = {String.valueOf(elderId)};

        db.update("ELDERS", values, whereClause, whereArgs);

        db.close();
    }


}

