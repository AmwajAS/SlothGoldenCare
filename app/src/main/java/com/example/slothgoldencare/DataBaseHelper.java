package com.example.slothgoldencare;

import android.annotation.SuppressLint;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteOpenHelper;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteStatement;
import android.util.Log;
import android.widget.ArrayAdapter;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import com.example.slothgoldencare.Reminder.Reminder;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.firebase.Timestamp;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import org.jetbrains.annotations.NotNull;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class DataBaseHelper extends SQLiteOpenHelper {
    private static final String TAG = "DBHelper";
    static final String DB_NAME = "GOLDEN_CARE.DB";
    static final int DB_VERSION = 1;
    /*
    Users Table info.
     */
    static final String USERS_TBL = "USERS";
    private static final String COLUMN_ID = "rowID";
    static final String USER_ID = "ID";
    static final String USER_EMAIL = "user_email";
    static final String USER_PASSWORD = "user_password";
    static final String USER_NAME = "user_name";
    static final String USER_PHONE = "user_phone";

    /*
      Elders Table info.
       */
    static final String ELDER_TBL = "ELDERLIES";
    static final String ELDER_ID = "ID";
    static final String DOCUMNET_ID = "docId";
    static final String ELDER_NAME = "name";
    static final String ELDER_EMAIL = "email";
    static final String ELDER_PASSWORD = "password";
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
    Reminers Table info.
     */
    static final String REMINDER_TBL = "Reminders";
    static final String REMINDER_ELD_ID = "ELderID";
    static final String REMINDER_TITLE = "Title";
    static final String REMINDER_DATE = "Date";
    static final String REMINDER_TIME = "Time";
    /*
    creating the DB Tables Queries:
     */
    private static final String CREATE_DB_QUERY_USER = "CREATE TABLE " + USERS_TBL + " ( " +
            DOCUMNET_ID + " TEXT PRIMARY KEY, " +
            USER_ID + " INTEGER NOT NULL, " +
            USER_EMAIL + " TEXT NOT NULL, " +
            USER_PASSWORD + " TEXT NOT NULL, " +
            USER_NAME + " TEXT NOT NULL, " +
            USER_PHONE + " TEXT NOT NULL UNIQUE" +
            " );";


    private static final String CREATE_DB_QUERY_ELDER = "CREATE TABLE " + ELDER_TBL + " ( " +
            DOCUMNET_ID + " TEXT PRIMARY KEY, " +
            ELDER_ID + " INTEGER NOT NULL, " +
            ELDER_EMAIL + " TEXT NOT NULL, " +
            ELDER_PASSWORD + " TEXT NOT NULL, " +
            ELDER_PHONE + " TEXT NOT NULL UNIQUE, " +
            ELDER_NAME + " TEXT NOT NULL, " +
            ELDER_DOB + " TEXT NOT NULL, " +
            ELDER_GENDER + " TEXT NOT NULL CHECK(" + ELDER_GENDER + " IN ('Male', 'Female', 'Other'))" +
            " );";

    private static final String CREATE_DB_QUERY_ELD_REL = "CREATE TABLE " + ELD_REL_TBL + " ( " +
            DOCUMNET_ID + " TEXT PRIMARY KEY, " +
            ELD_ID + " INTEGER NOT NULL, " +
            REL_ID + " INTEGER NOT NULL, " +
            RELATION + " TEXT NOT NULL" +
            " );";


    private static final String CREATE_DB_QUERY_REMINDER = "CREATE TABLE " + REMINDER_TBL + " ( " +
            REMINDER_ELD_ID + " TEXT NOT NULL," +
            REMINDER_TITLE + " TEXT NOT NULL, " +
            REMINDER_DATE + " TEXT NOT NULL," +
            REMINDER_TIME+ " TEXT NOT NULL" +
            " );";
    private static final String CREATE_DB_QUERY_USER_ELDER = "CREATE TABLE " + ELD_REL_TBL + " ( " + ELD_ID + "INTEGER PRIMARY KEY, " + REL_ID + " INTEGER PRIMARY KEY, " +
            RELATION + " TEXT NOT NULL " + " );";


    public static final String CREATE_REMINDER_QUERY =  "create table tbl_reminder(id integer primary key autoincrement,title text,date text,time text)";
    public DataBaseHelper(Context context) {
        super(context, DB_NAME, null, DB_VERSION);
    }


    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(CREATE_DB_QUERY_ELDER);
        db.execSQL(CREATE_DB_QUERY_USER);
        db.execSQL(CREATE_DB_QUERY_ELD_REL);
        db.execSQL(CREATE_DB_QUERY_REMINDER);
        FetchDataFromFirestore();

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

        ContentValues values = new ContentValues();
        values.put(DOCUMNET_ID,user.getDocId());
        values.put(USER_ID, user.getID());
        values.put(USER_EMAIL , user.getEmail());
        values.put(USER_PASSWORD , user.getPassword());
        values.put(USER_NAME, user.getUsername());
        values.put(USER_PHONE, user.getPhoneNumber());

        long result = db.insert(USERS_TBL, null, values);
        return result != -1;
    }

    public boolean addElderRelative(ElderRelative elderRelative) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(DOCUMNET_ID,elderRelative.getDocId());
        values.put(ELD_ID, elderRelative.getElderlyId());
        values.put(REL_ID, elderRelative.getRelativeId());
        values.put(RELATION, elderRelative.getRelation());

        long result = db.insert(ELD_REL_TBL, null, values);
        return result != -1; // Return true if insertion was successful
    }


    public boolean addElderData(Elder elder) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(DOCUMNET_ID,elder.getDocId());
        values.put(ELDER_ID, elder.getID());
        values.put(ELDER_EMAIL , elder.getEmail());
        values.put(ELDER_PASSWORD , elder.getPassword());
        values.put(ELDER_NAME, elder.getUsername());
        values.put(ELDER_PHONE, elder.getPhoneNumber());
        values.put(ELDER_DOB,elder.getDOB().toString());
        values.put(ELDER_GENDER,elder.getGender().toString());

        long result = db.insert(USERS_TBL, null, values);
        return result != -1;
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
/*
This method check if the input ID is already exists in the DB, and return true if yes, otherwise false.
 */
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
    /*
    This method takes as a input a user ID and return the whole row - User object.
     */
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

    public Elder getElderById(String elderId) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.query(ELDER_TBL, null, ELDER_ID + "=?", new String[]{elderId}, null, null, null);
        if (cursor.moveToFirst()) {
            String docId = cursor.getString(cursor.getColumnIndexOrThrow(DOCUMNET_ID));
            String id = cursor.getString(cursor.getColumnIndexOrThrow(ELDER_ID));
            String name = cursor.getString(cursor.getColumnIndexOrThrow(ELDER_NAME));
            String email = cursor.getString(cursor.getColumnIndexOrThrow(ELDER_EMAIL));
            String password = cursor.getString(cursor.getColumnIndexOrThrow(ELDER_PASSWORD));
            String phone = cursor.getString(cursor.getColumnIndexOrThrow(ELDER_PHONE));
            String dobString = cursor.getString(cursor.getColumnIndexOrThrow(ELDER_DOB));
            String genderString = cursor.getString(cursor.getColumnIndexOrThrow(ELDER_GENDER));

            Date dob = null;
            try {
                SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());
                dob = sdf.parse(dobString);
            } catch (ParseException e) {
                e.printStackTrace();
            }
            Gender gender = Elder.GenderConvertor(genderString);
            // Create and return the Elder object
            Elder elder = new Elder(id, name,phone,dob, gender ,email, password);
            elder.setDocId(docId);
            return elder;
        }
        cursor.close();
        // Return null if no matching elder found
        return null;
    }


    /*
   This method takes as a input a Elder ID and return the whole row - Elder object.
    */
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
/*
method to delete al data in User Tbl.
 */

    public void deleteAllData() {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(USERS_TBL, null, null);
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
/*
this method take as an input Elder ID and delete it from the DB.
 */

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
/*
This method connects to the DB and returns all the data in the Users TBL.
 */
    public List<User> getUsers() {
        List<User> userList = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.query(USERS_TBL, null, null, null, null, null, null);

        if (cursor.moveToFirst()) {
            do {
                String docId = cursor.getString(cursor.getColumnIndexOrThrow(DOCUMNET_ID));
                String id = cursor.getString(cursor.getColumnIndexOrThrow(USER_ID));
                String name = cursor.getString(cursor.getColumnIndexOrThrow(USER_NAME));
                String phone = cursor.getString(cursor.getColumnIndexOrThrow(USER_PHONE));
                String email = cursor.getString(cursor.getColumnIndexOrThrow(USER_EMAIL));
                String password = cursor.getString(cursor.getColumnIndexOrThrow(USER_PASSWORD));

                User user = new User(id, name, phone,email,password);
                user.setDocId(docId);
                userList.add(user);
            } while (cursor.moveToNext());
        }

        cursor.close();
        db.close();

        return userList;
    }
    /*
    This method connects to the DB and returns all the data in the Elders TBL.
     */
    public List<Elder> getElders() {
        List<Elder> eldersList = new ArrayList<>();

        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.query(ELDER_TBL, null, null, null, null, null, null);

        if (cursor.moveToFirst()) {
            do {
                String docId = cursor.getString(cursor.getColumnIndexOrThrow(DOCUMNET_ID));
                String id = cursor.getString(cursor.getColumnIndexOrThrow(ELDER_ID));
                String name = cursor.getString(cursor.getColumnIndexOrThrow(ELDER_NAME));
                String phone = cursor.getString(cursor.getColumnIndexOrThrow(ELDER_PHONE));
                String email = cursor.getString(cursor.getColumnIndexOrThrow(ELDER_EMAIL));
                String password = cursor.getString(cursor.getColumnIndexOrThrow(ELDER_PASSWORD));
                String dobString = cursor.getString(cursor.getColumnIndexOrThrow(ELDER_DOB));
                String genderString = cursor.getString(cursor.getColumnIndexOrThrow(ELDER_GENDER));
                Gender gender = Elder.GenderConvertor(genderString);
                Date dob = parseDateString(dobString);
                Elder elder = new Elder(id, name, phone,dob,gender,email,password);
                elder.setDocId(docId);

                eldersList.add(elder);
            } while (cursor.moveToNext());
        }

        cursor.close();
        db.close();

        return eldersList;
    }
/*
this method updated the changed values of the User TBL fileds.
 */
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

    /*
this method updated the changed values of the Elder TBL fileds.
 */

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

    public void FetchElderliesDataFromFirestore(){
        FirebaseFirestore firestore = FirebaseFirestore.getInstance();
        firestore.collection("Elderlies").get().addOnSuccessListener(querySnapshot -> {
            SQLiteDatabase db = this.getWritableDatabase();

            // Iterate over documents
            for (DocumentSnapshot document : querySnapshot.getDocuments()) {
                try {
                    // Extract required fields from the document
                    String docId = document.getId().toString();
                    String elderId = document.getString("id").toString();
                    String elderName = document.getString("username").toString();
                    String elderPhone = document.getString("phoneNumber").toString();
                    Timestamp elderDOB = document.getTimestamp("dob");
                    String elderGender = document.getString("gender").toString();
                    String elderEmail = document.getString("email").toString();
                    String elderPassword = document.getString("password").toString();

                    // Execute the INSERT statement for the "Elderlies" table
                    String insertQuery = "INSERT INTO " + ELDER_TBL + " ("+DOCUMNET_ID+"," + ELDER_ID + ", " + ELDER_NAME + ", " + ELDER_PHONE + ", " + ELDER_DOB + ", " + ELDER_GENDER + ", " + ELDER_EMAIL + ", " + ELDER_PASSWORD + ") VALUES (?, ?, ?, ?, ?, ?, ?, ?)";
                    SQLiteStatement statement = db.compileStatement(insertQuery);
                    statement.bindString(1,docId);
                    statement.bindString(2, elderId);
                    statement.bindString(3, elderName);
                    statement.bindString(4, elderPhone);
                    statement.bindString(5, elderDOB.toDate().toString());
                    statement.bindString(6, elderGender);
                    statement.bindString(7, elderEmail);
                    statement.bindString(8, elderPassword);
                    long rowId = statement.executeInsert();

                }catch (Exception e){
                    Log.w(TAG,"Specific elderly data error: "+e.getMessage().toString());
                }
            }
        }).addOnFailureListener(e -> {
            Log.w(TAG,"Error fetching Elderlies data from firestore to SQLite: "+e.getMessage().toString());
        });
    }
    public void FetchUsersDataFromFirestore(){
        FirebaseFirestore firestore = FirebaseFirestore.getInstance();
        firestore.collection("Users").get().addOnSuccessListener(querySnapshot -> {
            // Open connection to SQLite database
            SQLiteDatabase db = this.getWritableDatabase();

            // Iterate over documents
            for (DocumentSnapshot document : querySnapshot.getDocuments()) {
                try {
                    // Extract required fields from the document
                    String docId = document.getId().toString();
                    String userId = document.getString("id").toString();
                    String userName = document.getString("username").toString();
                    String userPhone = document.getString("phoneNumber").toString();
                    String userEmail = document.getString("email").toString();
                    String userPassword = document.getString("password").toString();

                    // Execute the INSERT statement for the "Users" table
                    String insertQuery = "INSERT INTO Users ("+DOCUMNET_ID+"," + USER_ID + ", " + USER_NAME + ", " + USER_PHONE + ", " + USER_EMAIL + ", " + USER_PASSWORD + ") VALUES (?, ?, ?, ?, ?, ?)";
                    SQLiteStatement statement = db.compileStatement(insertQuery);
                    statement.bindString(1,docId);
                    statement.bindString(2, userId);
                    statement.bindString(3, userName);
                    statement.bindString(4, userPhone);
                    statement.bindString(5, userEmail);
                    statement.bindString(6, userPassword);
                    long rowId = statement.executeInsert();

                }catch (Exception e){
                    Log.w(TAG,"Specific user data error : "+e.getMessage().toString());
                }
            }

            // Close the database connection
            db.close();
        }).addOnFailureListener(e -> {
            Log.w(TAG,"Error fetching Users data from firestore to SQLite: "+e.getMessage().toString());
        });
    }

    public void FetchElderlyRelativesDataFromFirestore(){
        FirebaseFirestore firestore = FirebaseFirestore.getInstance();
        firestore.collection("ElderlyRelative").get().addOnSuccessListener(querySnapshot -> {
            // Open connection to SQLite database
            SQLiteDatabase db = this.getWritableDatabase();

            // Iterate over documents
            for (DocumentSnapshot document : querySnapshot.getDocuments()) {
                try {
                    // Extract required fields from the document
                    String docId = document.getId().toString();
                    String relativeId = document.getString("relativeId").toString();
                    String elderlyId = document.getString("elderlyId").toString();
                    String relation = document.getString("relation").toString();

                    // Execute the INSERT statement for the "Users" table
                    String insertQuery = "INSERT INTO "+ELD_REL_TBL+"("+DOCUMNET_ID+"," + ELD_ID + ", " + REL_ID + ", " + RELATION + ") VALUES (?, ?, ?, ?)";
                    SQLiteStatement statement = db.compileStatement(insertQuery);
                    statement.bindString(1,docId);
                    statement.bindString(2, elderlyId);
                    statement.bindString(3, relativeId);
                    statement.bindString(4, relation);
                    long rowId = statement.executeInsert();

                }catch (Exception e){
                    Log.w(TAG,"Specific ElderlyRelative data error : "+e.getMessage().toString());
                }
            }

            // Close the database connection
            db.close();
        }).addOnFailureListener(e -> {
            Log.w(TAG,"Error fetching ElderlyRelative data from firestore to SQLite: "+e.getMessage().toString());
        });
    }
    public void FetchRemindersFromFirestore(){
        FirebaseFirestore firestore = FirebaseFirestore.getInstance();
        firestore.collection("Reminders").get().addOnSuccessListener(querySnapshot -> {
            // Open connection to SQLite database
            SQLiteDatabase db = this.getWritableDatabase();

            // Iterate over documents
            for (DocumentSnapshot document : querySnapshot.getDocuments()) {
                try {
                    // Extract required fields from the document
                    String docId = document.get("elderlyDocId").toString();
                    String title = document.get("title").toString();
                    Timestamp date = document.getTimestamp("date");
                    String time = document.get("time").toString();

                    // Execute the INSERT statement for the "Users" table
                    String insertQuery = "INSERT INTO "+REMINDER_TBL+"("+REMINDER_ELD_ID+"," + REMINDER_TITLE + ", " + REMINDER_DATE + ", " + REMINDER_TIME + ") VALUES (?, ?, ?, ?)";
                    SQLiteStatement statement = db.compileStatement(insertQuery);
                    statement.bindString(1,docId);
                    statement.bindString(2, title);
                    statement.bindString(3, date.toDate().toString());
                    statement.bindString(4, time);
                    long rowId = statement.executeInsert();

                }catch (Exception e){
                    Log.w(TAG,"Specific Reminder data error : "+e.getMessage().toString());
                }
            }

            // Close the database connection
            db.close();
        }).addOnFailureListener(e -> {
            Log.w(TAG,"Error fetching Reminders data from firestore to SQLite: "+e.getMessage().toString());
        });
    }
    public void FetchDataFromFirestore(){
       FetchUsersDataFromFirestore();
       FetchElderliesDataFromFirestore();
       FetchElderlyRelativesDataFromFirestore();
       FetchRemindersFromFirestore();
    }

    public Date convertStringToDate(String dobString){
        Date dob = null;
        try {
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());
            dob = sdf.parse(dobString);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return dob;
    }

    public String addReminder(Reminder reminder) {
        SQLiteDatabase database = this.getReadableDatabase();

        ContentValues contentValues = new ContentValues();
        contentValues.put(REMINDER_ELD_ID,reminder.getElderlyDocId());
        contentValues.put(REMINDER_TITLE,reminder.getTitle());                                                          //Inserts  data into sqllite database
        contentValues.put(REMINDER_DATE, reminder.getDate().toString());
        contentValues.put(REMINDER_TIME, reminder.getTime());

        float result = database.insert(REMINDER_TBL, null, contentValues);    //returns -1 if data successfully inserts into database

        if (result == -1) {
            return "Failed";
        } else {
            return "Successfully inserted";
        }
    }

    public List<Reminder> getReminderByElderlyDocId(String elderlyDocId) {
        List<Reminder> reminders = new ArrayList<>();

        SQLiteDatabase db = this.getReadableDatabase();

        // Define the WHERE clause to filter reminders by the given elderlyDocId
        String selection = REMINDER_ELD_ID + " = ?";
        String[] selectionArgs = {elderlyDocId};

        Cursor cursor = db.query(REMINDER_TBL, null, selection, selectionArgs, null, null, null);

        if (cursor.moveToFirst()) {
            do {
                String tempElderlyDocId = cursor.getString(cursor.getColumnIndexOrThrow(REMINDER_ELD_ID));
                String title = cursor.getString(cursor.getColumnIndexOrThrow(REMINDER_TITLE));
                String dateString = cursor.getString(cursor.getColumnIndexOrThrow(REMINDER_DATE));
                String time = cursor.getString(cursor.getColumnIndexOrThrow(REMINDER_TIME));
                Date dob = parseDateString(dateString);

                Reminder reminder = new Reminder(tempElderlyDocId, title, dob, time);
                reminders.add(reminder);

            } while (cursor.moveToNext());
        }

        cursor.close();
        db.close();

        return reminders;
    }


    public Cursor readallreminders() {
        SQLiteDatabase database = this.getWritableDatabase();
        String query = "select * from tbl_reminder order by id desc";                               //Sql query to  retrieve  data from the database
        Cursor cursor = database.rawQuery(query, null);
        return cursor;
    }

    public static Date parseDateString(String dateString) {
        SimpleDateFormat sdf = new SimpleDateFormat("EEE MMM dd HH:mm:ss z yyyy");
        try {
            return sdf.parse(dateString);
        } catch (ParseException e) {
            e.printStackTrace();
            return null;
        }
    }
}

