package com.example.slothgoldencare;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteOpenHelper;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteStatement;
import android.util.Log;
import com.example.slothgoldencare.Model.*;
import com.example.slothgoldencare.Reminder.Reminder;
import com.google.firebase.Timestamp;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

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
    Reminders Table info.
     */
    static final String REMINDER_TBL = "Reminders";
    static final String REMINDER_ELD_ID = "ElderID";
    static final String REMINDER_TITLE = "Title";
    static final String REMINDER_DATE = "Date";
    static final String REMINDER_TIME = "Time";

    /*
    Allergies Table info.
     */
    static final String ALLERGY_TBL = "Allergies";
    static final String ALLERGY_ELD_ID = "ElderID";
    static final String ALLERGY_TITLE = "Allergy";
    /*
    Diagnosis Table info.
    */
    static final String DIAGNOSIS_TBL = "Diagnosis";
    static final String DIAGNOSIS_ELD_ID = "ElderID";
    static final String DIAGNOSIS_TITLE = "Diagnosis";
    /*
    Medicine Table info.
    */
    static final String MEDICINE_TBL = "Medicines";
    static final String MEDICINE_ELD_ID = "ElderID";
    static final String MEDICINE_TITLE = "Medicine";

    /*
    HealthTip Table info.
    */
    static final String HEALTH_TIP_TBL = "HealthTips";
    static final String HEALTH_TIP_TITLE = "Title";
    static final String HEALTH_TIP_CONTENT = "Content";
    /*
    Surgeries Table info.
    */
    static final String SURGERY_TBL = "Surgeries";
    static final String SURGERY_ELD_ID = "ElderID";
    static final String SURGERY_TITLE = "Surgery";
    static final String SURGERY_DATE = "SurgeryDate";

    /*
    Doctors Table info.
 */
    static final String DOCTORS_TBL = "DOCTORS";
    //private static final String COLUMN_ID = "rowID";
    static final String DOCTOR_ID = "ID";
    static final String DOCTOR_EMAIL = "user_email";
    static final String DOCTOR_PASSWORD = "user_password";
    static final String DOCTOR_NAME = "user_name";
    static final String DOCTOR_PHONE = "user_phone";
    static final String DOCTOR_SPECIALIZATION = "specialization";

    /*
  Appointment Table info.
*/
    static final String APPOINTMENT_TBL = "Appointment";
    static final String THE_DOCTOR_NAME= "doctor";
    static final String THE_ELDER_ID = "elder_id";
    static final String APPOINTMENT_DATE = "date";
    static final String NOTES = "notes";

    /*
    WorkAndPayment Table info.
 */
    static final String WORK_AND_PAYMENT_TBL = "WORK_AND_PAYMENT";
    static final String WORK_AND_PAYMENT_DATE_DAY = "dateDay";
    static final String WORK_AND_PAYMENT_DOCTOR_ID = "doctorId";
    static final String WORK_AND_PAYMENT_HOURS = "hours";
    static final String WORK_AND_PAYMENT_IS_PAID = "isPaid";
    static final String WORK_AND_PAYMENT_PAID_DATE = "paidDate";

    private SimpleDateFormat sdf; //for the Timestamp firbase

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

    private static final String CREATE_DB_QUERY_DOCTOR = "CREATE TABLE " + DOCTORS_TBL + " ( " +
            DOCUMNET_ID + " TEXT PRIMARY KEY, " +
            DOCTOR_ID + " INTEGER NOT NULL, " +
            DOCTOR_NAME + " TEXT NOT NULL, " +
            DOCTOR_PHONE + " TEXT NOT NULL UNIQUE, " +
            DOCTOR_EMAIL + " TEXT NOT NULL, " +
            DOCTOR_PASSWORD + " TEXT NOT NULL, " +
            DOCTOR_SPECIALIZATION + " TEXT NOT NULL " +
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

    private static final String CREATE_DB_QUERY_ALLERGY = "CREATE TABLE " + ALLERGY_TBL + " ( " +
            ALLERGY_ELD_ID + " TEXT NOT NULL," +
            ALLERGY_TITLE + " TEXT NOT NULL " +
            " );";
    private static final String CREATE_DB_QUERY_DIAGNOSIS = "CREATE TABLE " + DIAGNOSIS_TBL + " ( " +
            DIAGNOSIS_ELD_ID + " TEXT NOT NULL," +
            DIAGNOSIS_TITLE + " TEXT NOT NULL " +
            " );";
    private static final String CREATE_DB_QUERY_MEDICINE = "CREATE TABLE " + MEDICINE_TBL + " ( " +
            MEDICINE_ELD_ID + " TEXT NOT NULL," +
            MEDICINE_TITLE + " TEXT NOT NULL " +
            " );";


    private static final String CREATE_DB_QUERY_SURGERY = "CREATE TABLE " + SURGERY_TBL + " ( " +
            SURGERY_ELD_ID + " TEXT NOT NULL," +
            SURGERY_TITLE + " TEXT NOT NULL, " +
            SURGERY_DATE + " TEXT NOT NULL " +
            " );";

    private static final String CREATE_DB_QUERY_APPOINTMENT = "CREATE TABLE " + APPOINTMENT_TBL + " ( " +
            THE_DOCTOR_NAME + " TEXT NOT NULL, " +
            THE_ELDER_ID + " TEXT NOT NULL, " +
            APPOINTMENT_DATE + " TEXT NOT NULL, " +
            NOTES + " TEXT" +
            " );";
    private static final String CREATE_DB_QUERY_HEALTH_TIP = "CREATE TABLE " + HEALTH_TIP_TBL + " ( " +
            HEALTH_TIP_TITLE + " TEXT NOT NULL," +
            HEALTH_TIP_CONTENT + " TEXT NOT NULL " +
            " );";

    private static final String CREATE_DB_QUERY_WORK_AND_PAYMENT = "CREATE TABLE " + WORK_AND_PAYMENT_TBL + " ( " +
            WORK_AND_PAYMENT_DATE_DAY + " TEXT NOT NULL, " +
            WORK_AND_PAYMENT_DOCTOR_ID + " TEXT NOT NULL, " +
            WORK_AND_PAYMENT_HOURS + " TEXT NOT NULL, " +
            WORK_AND_PAYMENT_IS_PAID + " TEXT NOT NULL, " +
            WORK_AND_PAYMENT_PAID_DATE + " TEXT " +
            " );";

    public DataBaseHelper(Context context) {
        super(context, DB_NAME, null, DB_VERSION);
        sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault());
    }


    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(CREATE_DB_QUERY_ELDER);
        db.execSQL(CREATE_DB_QUERY_USER);
        db.execSQL(CREATE_DB_QUERY_ELD_REL);
        db.execSQL(CREATE_DB_QUERY_REMINDER);
        db.execSQL(CREATE_DB_QUERY_ALLERGY);
        db.execSQL(CREATE_DB_QUERY_DIAGNOSIS);
        db.execSQL(CREATE_DB_QUERY_SURGERY);
        db.execSQL(CREATE_DB_QUERY_DOCTOR);
        db.execSQL(CREATE_DB_QUERY_APPOINTMENT);
        db.execSQL(CREATE_DB_QUERY_MEDICINE);
        db.execSQL(CREATE_DB_QUERY_HEALTH_TIP);
        db.execSQL(CREATE_DB_QUERY_WORK_AND_PAYMENT);
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

        long result = db.insert(ELDER_TBL, null, values);
        return result != -1;
    }


    public boolean addDoctorData(Doctor doctor) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();

        values.put(DOCUMNET_ID, doctor.getDocId());
        values.put(DOCTOR_ID, doctor.getID());
        values.put(DOCTOR_NAME, doctor.getUsername());
        values.put(DOCTOR_PHONE, doctor.getPhoneNumber());
        values.put(DOCTOR_EMAIL, doctor.getEmail());
        values.put(DOCTOR_PASSWORD, doctor.getPassword());
        values.put(DOCTOR_SPECIALIZATION, doctor.getSpecialization());

        long result = db.insert(DOCTORS_TBL, null, values);
        return result != -1;
    }
    public boolean addAppointmentData(Appointment appointment) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();

        values.put(THE_DOCTOR_NAME, appointment.getDoctor());
        values.put(THE_ELDER_ID, appointment.getElderId());
        values.put(APPOINTMENT_DATE, sdf.format(appointment.getDate().toDate()));
        values.put(NOTES, appointment.getNotes());

        long result = db.insert(APPOINTMENT_TBL, null, values);
        return result != -1;
    }

    public boolean addWorkAndPaymentData(WorkAndPayment workAndPayment) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();

        values.put(WORK_AND_PAYMENT_DATE_DAY, workAndPayment.getDateDay());
        values.put(WORK_AND_PAYMENT_DOCTOR_ID, workAndPayment.getDoctorId());
        values.put(WORK_AND_PAYMENT_HOURS, workAndPayment.getHours());
        values.put(WORK_AND_PAYMENT_IS_PAID, workAndPayment.isPaid() ? "1" : "0");
        values.put(WORK_AND_PAYMENT_PAID_DATE, workAndPayment.getPaidDate());

        long result = db.insert(WORK_AND_PAYMENT_TBL, null, values);
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

    public Doctor getDoctorById(String doctorId) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.query(DOCTORS_TBL, null, DOCTOR_ID + "=?", new String[]{doctorId}, null, null, null);
        if (cursor.moveToFirst()) {
            String docId = cursor.getString(cursor.getColumnIndexOrThrow(DOCUMNET_ID));
            String id = cursor.getString(cursor.getColumnIndexOrThrow(DOCTOR_ID));
            String name = cursor.getString(cursor.getColumnIndexOrThrow(DOCTOR_NAME));
            String phone = cursor.getString(cursor.getColumnIndexOrThrow(DOCTOR_PHONE));
            String email = cursor.getString(cursor.getColumnIndexOrThrow(DOCTOR_EMAIL));
            String password = cursor.getString(cursor.getColumnIndexOrThrow(DOCTOR_PASSWORD));
            String specialization = cursor.getString(cursor.getColumnIndexOrThrow(DOCTOR_SPECIALIZATION));



            // Create and return the Doctor object
            Doctor doctor = new Doctor(id, name, phone, email, password, specialization);
            doctor.setDocId(docId);
            return doctor;
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

    public boolean deleteElderByDocId(String docId) {
        SQLiteDatabase db = this.getWritableDatabase();
        String whereClause = DOCUMNET_ID+" = ?";
        String[] whereArgs = {docId};
        int result = db.delete(ELDER_TBL, whereClause, whereArgs);
        db.close();

        // If the result is greater than 0, the deletion was successful
        return result > 0;
    }

    public boolean deleteUserByDocId(String docId) {
        SQLiteDatabase db = this.getWritableDatabase();
        String whereClause = DOCUMNET_ID+" = ?";
        String[] whereArgs = {docId};
        int result = db.delete(USERS_TBL, whereClause, whereArgs);
        db.close();

        // If the result is greater than 0, the deletion was successful
        return result > 0;
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
     This method connects to the DB and returns all the data in the Doctors TBL.
      */
    public List<Doctor> getDoctors() {
        List<Doctor> doctorsList = new ArrayList<>();

        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.query(DOCTORS_TBL, null, null, null, null, null, null);

        if (cursor.moveToFirst()) {
            do {
                String docId = cursor.getString(cursor.getColumnIndexOrThrow(DOCUMNET_ID));
                String id = cursor.getString(cursor.getColumnIndexOrThrow(DOCTOR_ID));
                String name = cursor.getString(cursor.getColumnIndexOrThrow(DOCTOR_NAME));
                String phone = cursor.getString(cursor.getColumnIndexOrThrow(DOCTOR_PHONE));
                String email = cursor.getString(cursor.getColumnIndexOrThrow(DOCTOR_EMAIL));
                String password = cursor.getString(cursor.getColumnIndexOrThrow(DOCTOR_PASSWORD));
                String specialization = cursor.getString(cursor.getColumnIndexOrThrow(DOCTOR_SPECIALIZATION));
                Doctor doctor = new Doctor(id, name, phone, email, password, specialization);
                doctor.setDocId(docId);

                doctorsList.add(doctor);
            } while (cursor.moveToNext());
        }

        cursor.close();
        db.close();

        return doctorsList;
    }
    //Getting all the relatives for a specific Elderly.
    public List<User> GetRelativesByElderly(String id){
        SQLiteDatabase db = this.getReadableDatabase();

        List<User> relatives = new ArrayList<>();
        // Assuming you have a valid SQLiteDatabase object named "db"

        String query = "SELECT * FROM "+ELD_REL_TBL+" INNER JOIN "+USERS_TBL+" ON "+ELD_REL_TBL+"."+REL_ID+" = "+USERS_TBL+"."+DOCUMNET_ID+
                " WHERE "+ELD_ID+" = ?";
        String[] selectionArgs = {id};
        Cursor cursor = db.rawQuery(query, selectionArgs);
        if (cursor != null && cursor.moveToFirst()) {
            do {
                // Extract data from the cursor for each matching row
                String documentId = cursor.getString(cursor.getColumnIndexOrThrow(DOCUMNET_ID));
                String userId = cursor.getString(cursor.getColumnIndexOrThrow(USER_ID));
                String name = cursor.getString(cursor.getColumnIndexOrThrow(USER_NAME));
                String email = cursor.getString(cursor.getColumnIndexOrThrow(USER_EMAIL));
                String password = cursor.getString(cursor.getColumnIndexOrThrow(USER_PASSWORD));
                String phone = cursor.getString(cursor.getColumnIndexOrThrow(USER_PHONE));

                User user = new User(userId,name,phone,email,password);
                user.setDocId(documentId);
                relatives.add(user);

            } while (cursor.moveToNext());
        }

        if (cursor != null) {
            cursor.close();
        }
        return relatives;
    }
    public List<HealthTip> getHealthTips() {
        List<HealthTip> healthTipsList = new ArrayList<>();

        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.query(HEALTH_TIP_TBL, null, null, null, null, null, null);

        if (cursor.moveToFirst()) {
            do {
                String title = cursor.getString(cursor.getColumnIndexOrThrow(HEALTH_TIP_TITLE));
                String content = cursor.getString(cursor.getColumnIndexOrThrow(HEALTH_TIP_CONTENT));
                HealthTip healthTip = new HealthTip(title,content);

                healthTipsList.add(healthTip);
            } while (cursor.moveToNext());
        }

        cursor.close();
        db.close();

        return healthTipsList;
    }

    public List<Appointment> getAppointments() {
        List<Appointment> appointmentsList = new ArrayList<>();

        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.query(APPOINTMENT_TBL, null, null, null, null, null, null);

        if (cursor.moveToFirst()) {
            do {
                String doctorId = cursor.getString(cursor.getColumnIndexOrThrow(THE_DOCTOR_NAME));
                String elderId = cursor.getString(cursor.getColumnIndexOrThrow(THE_ELDER_ID));
                String appointmentDateStr = cursor.getString(cursor.getColumnIndexOrThrow(APPOINTMENT_DATE));
                String notes = cursor.getString(cursor.getColumnIndexOrThrow(NOTES));

                SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault());
                Date appointmentDate = null;
                try {
                    appointmentDate = sdf.parse(appointmentDateStr);
                } catch (ParseException e) {
                    e.printStackTrace();
                }

                Timestamp timestamp = new Timestamp(appointmentDate);
                Appointment appointment = new Appointment(timestamp, elderId, notes, doctorId);
                appointmentsList.add(appointment);
            } while (cursor.moveToNext());
        }

        cursor.close();
        db.close();

        return appointmentsList;
    }

    public List<WorkAndPayment> getWorkAndPaymentData() {
        List<WorkAndPayment> workAndPaymentList = new ArrayList<>();

        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.query(WORK_AND_PAYMENT_TBL, null, null, null, null, null, null);

        if (cursor.moveToFirst()) {
            do {
                String dateDay = cursor.getString(cursor.getColumnIndexOrThrow(WORK_AND_PAYMENT_DATE_DAY));
                String doctorId = cursor.getString(cursor.getColumnIndexOrThrow(WORK_AND_PAYMENT_DOCTOR_ID));
                String hours = cursor.getString(cursor.getColumnIndexOrThrow(WORK_AND_PAYMENT_HOURS));
                boolean isPaid = cursor.getString(cursor.getColumnIndexOrThrow(WORK_AND_PAYMENT_IS_PAID)).equals("1");
                String paidDate = cursor.getString(cursor.getColumnIndexOrThrow(WORK_AND_PAYMENT_PAID_DATE));

                WorkAndPayment workAndPayment = new WorkAndPayment(dateDay, doctorId, hours, isPaid, paidDate);
                workAndPaymentList.add(workAndPayment);
            } while (cursor.moveToNext());
        }

        cursor.close();
        db.close();

        return workAndPaymentList;
    }


    /*
    this method updated the changed values of the User TBL fileds.
     */
    public boolean updateUserInfo(User user) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();

        values.put(USER_ID, user.getID());
        values.put(USER_NAME, user.getUsername());
        values.put(USER_EMAIL, user.getEmail());
        values.put(USER_PHONE, user.getPhoneNumber());
        values.put(USER_PASSWORD, user.getPassword());

        int rowsAffected = db.update(USERS_TBL, values, DOCUMNET_ID+"=?", new String[]{user.getDocId()});
        db.close();

        return rowsAffected > 0;
    }

    /*
    this method updated the changed values of the Elder TBL fileds.
    */

    public boolean updateElderlyInfo(Elder elder) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();

        values.put(ELDER_ID, elder.getID());
        values.put(ELDER_NAME, elder.getUsername());
        values.put(ELDER_EMAIL, elder.getEmail());
        values.put(ELDER_PHONE, elder.getPhoneNumber());
        values.put(ELDER_GENDER, elder.getGender().toString());
        values.put(ELDER_DOB, elder.getDOB().toString());
        values.put(ELDER_PASSWORD, elder.getPassword());

        int rowsAffected = db.update(ELDER_TBL, values, DOCUMNET_ID+"=?", new String[]{elder.getDocId()});
        db.close();

        return rowsAffected > 0;
    }

    public boolean updateDoctorsInfo(Doctor doctor) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();

        values.put(DOCTOR_ID, doctor.getID());
        values.put(DOCTOR_NAME, doctor.getUsername());
        values.put(DOCTOR_PHONE, doctor.getPhoneNumber());
        values.put(DOCTOR_EMAIL, doctor.getEmail());
        values.put(DOCTOR_PASSWORD, doctor.getPassword());
        values.put(DOCTOR_SPECIALIZATION, doctor.getSpecialization());

        int rowsAffected = db.update(DOCTORS_TBL, values, DOCUMNET_ID+"=?", new String[]{doctor.getDocId()});
        db.close();

        return rowsAffected > 0;
    }
    public boolean updateWorkAndPaymentData(WorkAndPayment workAndPayment) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();

        values.put(WORK_AND_PAYMENT_DATE_DAY, workAndPayment.getDateDay());
        values.put(WORK_AND_PAYMENT_DOCTOR_ID, workAndPayment.getDoctorId());
        values.put(WORK_AND_PAYMENT_HOURS, workAndPayment.getHours());
        values.put(WORK_AND_PAYMENT_IS_PAID, workAndPayment.isPaid() ? "1" : "0");
        values.put(WORK_AND_PAYMENT_PAID_DATE, workAndPayment.getPaidDate());

        int rowsAffected = db.update(WORK_AND_PAYMENT_TBL, values, WORK_AND_PAYMENT_DATE_DAY + "=? AND " +
                WORK_AND_PAYMENT_DOCTOR_ID + "=?", new String[]{workAndPayment.getDateDay(), workAndPayment.getDoctorId()});
        db.close();

        return rowsAffected > 0;
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

    // Fetch doctor data from Firestore and insert it into the SQLite database
    // Fetch doctor data from Firestore and insert it into the SQLite database
    private void FetchDoctorsDataFromFirestore(){
        FirebaseFirestore firestore = FirebaseFirestore.getInstance();
        firestore.collection("Doctors").get().addOnSuccessListener(querySnapshot -> {
            // Open connection to SQLite database
            SQLiteDatabase db = this.getWritableDatabase();

            // Iterate over documents
            for (DocumentSnapshot document : querySnapshot.getDocuments()) {
                try {
                    // Extract required fields from the document
                    String docId = document.getId(); // Use the automatically generated document ID
                    String userId = document.getString("id");
                    String userName = document.getString("username");
                    String userPhone = document.getString("phoneNumber");
                    String userEmail = document.getString("email");
                    String userPassword = document.getString("password");
                    String specialization = document.getString("specialization");

                    // Execute the INSERT statement for the "Doctors" table
                    String insertQuery = "INSERT INTO Doctors (" + DOCUMNET_ID + ", " + DOCTOR_ID + ", " + DOCTOR_NAME + ", " + DOCTOR_PHONE + ", " + DOCTOR_EMAIL + ", " + DOCTOR_PASSWORD + ", " + DOCTOR_SPECIALIZATION + ") VALUES (?, ?, ?, ?, ?, ?, ?)";
                    SQLiteStatement statement = db.compileStatement(insertQuery);
                    statement.bindString(1, docId);
                    statement.bindString(2, userId);
                    statement.bindString(3, userName);
                    statement.bindString(4, userPhone);
                    statement.bindString(5, userEmail);
                    statement.bindString(6, userPassword);
                    statement.bindString(7, specialization);
                    long rowId = statement.executeInsert();

                } catch (Exception e) {
                    Log.w(TAG, "Specific Doctors data error: " + e.getMessage());
                }
            }

            // Close the database connection
            db.close();
        }).addOnFailureListener(e -> {
            Log.w(TAG, "Error fetching Doctors data from Firestore to SQLite: " + e.getMessage());
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
    public void FetchAllergiesFromFirestore(){
        FirebaseFirestore firestore = FirebaseFirestore.getInstance();
        firestore.collection("Allergies").get().addOnSuccessListener(querySnapshot -> {
            // Open connection to SQLite database
            SQLiteDatabase db = this.getWritableDatabase();

            // Iterate over documents
            for (DocumentSnapshot document : querySnapshot.getDocuments()) {
                try {
                    // Extract required fields from the document
                    String docId = document.get("elderlyDocId").toString();
                    String title = document.get("allergy").toString();

                    // Execute the INSERT statement for the "Users" table
                    String insertQuery = "INSERT INTO "+ALLERGY_TBL+"("+ALLERGY_ELD_ID+"," + ALLERGY_TITLE +") VALUES (?, ?)";
                    SQLiteStatement statement = db.compileStatement(insertQuery);
                    statement.bindString(1,docId);
                    statement.bindString(2, title);
                    long rowId = statement.executeInsert();
                }catch (Exception e){
                    Log.w(TAG,"Specific Allergy data error : "+e.getMessage().toString());
                }
            }
            // Close the database connection
            db.close();
        }).addOnFailureListener(e -> {
            Log.w(TAG,"Error fetching Allergies data from firestore to SQLite: "+e.getMessage().toString());
        });
    }
    public void FetchDiagnosisFromFirestore(){
        FirebaseFirestore firestore = FirebaseFirestore.getInstance();
        firestore.collection("Diagnosis").get().addOnSuccessListener(querySnapshot -> {
            // Open connection to SQLite database
            SQLiteDatabase db = this.getWritableDatabase();

            // Iterate over documents
            for (DocumentSnapshot document : querySnapshot.getDocuments()) {
                try {
                    // Extract required fields from the document
                    String docId = document.get("elderlyDocId").toString();
                    String title = document.get("diagnosis").toString();

                    // Execute the INSERT statement for the "Users" table
                    String insertQuery = "INSERT INTO "+DIAGNOSIS_TBL+"("+DIAGNOSIS_ELD_ID+"," + DIAGNOSIS_TITLE +") VALUES (?, ?)";
                    SQLiteStatement statement = db.compileStatement(insertQuery);
                    statement.bindString(1,docId);
                    statement.bindString(2, title);
                    long rowId = statement.executeInsert();
                }catch (Exception e){
                    Log.w(TAG,"Specific Diagnosis data error : "+e.getMessage().toString());
                }
            }
            // Close the database connection
            db.close();
        }).addOnFailureListener(e -> {
            Log.w(TAG,"Error fetching diagnosis data from firestore to SQLite: "+e.getMessage().toString());
        });
    }
    public void FetchMedicinesFromFirestore(){
        FirebaseFirestore firestore = FirebaseFirestore.getInstance();
        firestore.collection("Medicines").get().addOnSuccessListener(querySnapshot -> {
            // Open connection to SQLite database
            SQLiteDatabase db = this.getWritableDatabase();

            // Iterate over documents
            for (DocumentSnapshot document : querySnapshot.getDocuments()) {
                try {
                    // Extract required fields from the document
                    String docId = document.get("elderlyDocId").toString();
                    String title = document.get("medicine").toString();

                    // Execute the INSERT statement for the "Users" table
                    String insertQuery = "INSERT INTO "+MEDICINE_TBL+"("+MEDICINE_ELD_ID+"," + MEDICINE_TITLE +") VALUES (?, ?)";
                    SQLiteStatement statement = db.compileStatement(insertQuery);
                    statement.bindString(1,docId);
                    statement.bindString(2, title);
                    long rowId = statement.executeInsert();
                }catch (Exception e){
                    Log.w(TAG,"Specific Medicine data error : "+e.getMessage().toString());
                }
            }
            // Close the database connection
            db.close();
        }).addOnFailureListener(e -> {
            Log.w(TAG,"Error fetching medicines data from firestore to SQLite: "+e.getMessage().toString());
        });
    }
    public void FetchSurgeriesFromFirestore(){
        FirebaseFirestore firestore = FirebaseFirestore.getInstance();
        firestore.collection("Surgeries").get().addOnSuccessListener(querySnapshot -> {
            // Open connection to SQLite database
            SQLiteDatabase db = this.getWritableDatabase();

            // Iterate over documents
            for (DocumentSnapshot document : querySnapshot.getDocuments()) {
                try {
                    // Extract required fields from the document
                    String docId = document.get("elderlyDocId").toString();
                    String title = document.get("surgery").toString();
                    Timestamp date = document.getTimestamp("date");

                    // Execute the INSERT statement for the "Users" table
                    String insertQuery = "INSERT INTO "+SURGERY_TBL+"("+SURGERY_ELD_ID+"," + SURGERY_TITLE +","+SURGERY_DATE+") VALUES (?, ?, ?)";
                    SQLiteStatement statement = db.compileStatement(insertQuery);
                    statement.bindString(1,docId);
                    statement.bindString(2, title);
                    statement.bindString(3,date.toDate().toString());
                    long rowId = statement.executeInsert();
                }catch (Exception e){
                    Log.w(TAG,"Specific Diagnosis data error : "+e.getMessage().toString());
                }
            }
            // Close the database connection
            db.close();
        }).addOnFailureListener(e -> {
            Log.w(TAG,"Error fetching diagnosis data from firestore to SQLite: "+e.getMessage().toString());
        });
    }
    // Fetch appointment data from Firestore and insert it into the SQLite database
    public void FetchAppointmentsFromFirestore() {
        FirebaseFirestore firestore = FirebaseFirestore.getInstance();
        firestore.collection("Appointment").get().addOnSuccessListener(querySnapshot -> {
            // Open connection to SQLite database
            SQLiteDatabase db = this.getWritableDatabase();

            // Iterate over documents
            for (DocumentSnapshot document : querySnapshot.getDocuments()) {
                try {
                    // Extract required fields from the document
                    String doctorId = document.getString("doctor");
                    String elderId = document.getString("elderId");
                    String notes = document.getString("notes");
                    Timestamp appointmentDate = document.getTimestamp("date");

                    // Convert Timestamp to string representation
                    SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault());
                    String appointmentDateStr = sdf.format(appointmentDate.toDate());

                    // Execute the INSERT statement for the "Appointment" table
                    String insertQuery = "INSERT INTO " + APPOINTMENT_TBL + " (" + THE_DOCTOR_NAME + ", " + THE_ELDER_ID + ", " + APPOINTMENT_DATE + ", " + NOTES + ") VALUES (?, ?, ?, ?)";
                    SQLiteStatement statement = db.compileStatement(insertQuery);
                    statement.bindString(1, doctorId);
                    statement.bindString(2, elderId);
                    statement.bindString(3, appointmentDateStr);
                    statement.bindString(4, notes);
                    long rowId = statement.executeInsert();

                    // Print the rowId for debugging
//                    Log.d(TAG, "Inserted Row Id: " + rowId);

                } catch (Exception e) {
                    Log.w(TAG, "Error inserting specific appointment data: " + e.getMessage());
                }
            }

            // Close the database connection
            db.close();
        }).addOnFailureListener(e -> {
            Log.w(TAG, "Error fetching appointments data from Firestore to SQLite: " + e.getMessage());
        });
    }
    public void FetchHealthTipsFromFirestore(){
        FirebaseFirestore firestore = FirebaseFirestore.getInstance();
        firestore.collection("HealthTips").get().addOnSuccessListener(querySnapshot -> {
            // Open connection to SQLite database
            SQLiteDatabase db = this.getWritableDatabase();

            // Iterate over documents
            for (DocumentSnapshot document : querySnapshot.getDocuments()) {
                try {
                    // Extract required fields from the document
                    String title = document.get("title").toString();
                    String content = document.get("content").toString();

                    // Execute the INSERT statement for the "Users" table
                    String insertQuery = "INSERT INTO "+HEALTH_TIP_TBL+"("+HEALTH_TIP_TITLE+"," + HEALTH_TIP_CONTENT +") VALUES (?, ?)";
                    SQLiteStatement statement = db.compileStatement(insertQuery);
                    statement.bindString(1,title);
                    statement.bindString(2, content);
                    long rowId = statement.executeInsert();
                }catch (Exception e){
                    Log.w(TAG,"Specific Health Tip data error : "+e.getMessage().toString());
                }
            }
            // Close the database connection
            db.close();
        }).addOnFailureListener(e -> {
            Log.w(TAG,"Error fetching Health Tips data from firestore to SQLite: "+e.getMessage().toString());
        });
    }
    public void fetchWorkAndPaymentDataFromFirestore() {
        FirebaseFirestore firestore = FirebaseFirestore.getInstance();
        firestore.collection("WorkAndPayment").get().addOnSuccessListener(querySnapshot -> {
            // Open connection to SQLite database
            SQLiteDatabase db = this.getWritableDatabase();

            // Iterate over documents
            for (DocumentSnapshot document : querySnapshot.getDocuments()) {
                try {
                    // Extract required fields from the document
                    String dateDay = document.getTimestamp("dateDay").toDate().toString();
                    String doctorId = document.getString("doctorId");
                    String hours = document.getString("hours");
                    boolean isPaid = document.getBoolean("isPaid");
                    String paidDate = document.getTimestamp("paidDate").toDate().toString();

                    // Execute the INSERT statement for the "WorkAndPayment" table
                    String insertQuery = "INSERT INTO " + WORK_AND_PAYMENT_TBL + " (" +
                            WORK_AND_PAYMENT_DATE_DAY + "," + WORK_AND_PAYMENT_DOCTOR_ID + "," +
                            WORK_AND_PAYMENT_HOURS + "," + WORK_AND_PAYMENT_IS_PAID + "," +
                            WORK_AND_PAYMENT_PAID_DATE + ") VALUES (?, ?, ?, ?, ?)";
                    SQLiteStatement statement = db.compileStatement(insertQuery);
                    statement.bindString(1, dateDay);
                    statement.bindString(2, doctorId);
                    statement.bindString(3, hours);
                    statement.bindString(4, isPaid ? "true" : "false");
                    statement.bindString(5, paidDate);
                    long rowId = statement.executeInsert();
                } catch (Exception e) {
                    Log.w(TAG, "Specific WorkAndPayment data error: " + e.getMessage());
                }
            }
            // Close the database connection
            db.close();
        }).addOnFailureListener(e -> {
            Log.w(TAG, "Error fetching WorkAndPayment data from Firestore to SQLite: " + e.getMessage());
        });
    }




    public void FetchDataFromFirestore(){
        FetchUsersDataFromFirestore();
        FetchElderliesDataFromFirestore();
        FetchDoctorsDataFromFirestore();
        FetchElderlyRelativesDataFromFirestore();
        FetchRemindersFromFirestore();
        FetchAllergiesFromFirestore();
        FetchDiagnosisFromFirestore();
        FetchMedicinesFromFirestore();
        FetchSurgeriesFromFirestore();
        FetchHealthTipsFromFirestore();
        FetchAppointmentsFromFirestore();
        fetchWorkAndPaymentDataFromFirestore();
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
    public boolean addDiagnosis(Diagnosis diagnosis){
        SQLiteDatabase database = this.getReadableDatabase();

        ContentValues contentValues = new ContentValues();
        contentValues.put(DIAGNOSIS_ELD_ID,diagnosis.getElderlyDocId());
        contentValues.put(DIAGNOSIS_TITLE,diagnosis.getDiagnosis());                                                          //Inserts  data into sqllite database

        float result = database.insert(DIAGNOSIS_TBL, null, contentValues);    //returns -1 if data successfully inserts into database

        if (result == -1) {
            return false;
        } else {
            return true;
        }
    }
    public boolean addAllergy(Allergy allergy){
        SQLiteDatabase database = this.getReadableDatabase();

        ContentValues contentValues = new ContentValues();
        contentValues.put(ALLERGY_ELD_ID,allergy.getElderlyDocId());
        contentValues.put(ALLERGY_TITLE,allergy.getAllergy());                                                          //Inserts  data into sqllite database

        float result = database.insert(ALLERGY_TBL, null, contentValues);    //returns -1 if data successfully inserts into database

        if (result == -1) {
            return false;
        } else {
            return true;
        }
    }
    public boolean addMedicine(Medicine medicine){
        SQLiteDatabase database = this.getReadableDatabase();

        ContentValues contentValues = new ContentValues();
        contentValues.put(MEDICINE_ELD_ID,medicine.getElderlyDocId());
        contentValues.put(MEDICINE_TITLE,medicine.getMedicine());                                                          //Inserts  data into sqllite database

        float result = database.insert(MEDICINE_TBL, null, contentValues);    //returns -1 if data successfully inserts into database

        if (result == -1) {
            return false;
        } else {
            return true;
        }
    }
    public boolean addSurgery(Surgery surgery){
        SQLiteDatabase database = this.getReadableDatabase();

        ContentValues contentValues = new ContentValues();
        contentValues.put(SURGERY_ELD_ID,surgery.getElderlyDocId());
        contentValues.put(SURGERY_TITLE,surgery.getSurgery());                                                          //Inserts  data into sqllite database
        contentValues.put(SURGERY_DATE,surgery.getDate().toString());

        float result = database.insert(SURGERY_TBL, null, contentValues);    //returns -1 if data successfully inserts into database

        if (result == -1) {
            return false;
        } else {
            return true;
        }
    }
    public boolean addHealthTip(HealthTip healthTip){
        SQLiteDatabase database = this.getReadableDatabase();

        ContentValues contentValues = new ContentValues();
        contentValues.put(HEALTH_TIP_TITLE,healthTip.getTitle());
        contentValues.put(HEALTH_TIP_CONTENT,healthTip.getContent());                                                          //Inserts  data into sqllite database

        float result = database.insert(HEALTH_TIP_TBL, null, contentValues);    //returns -1 if data successfully inserts into database

        if (result == -1) {
            return false;
        } else {
            return true;
        }
    }
    public boolean deleteDiagnosis(Diagnosis diagnosis) {
        SQLiteDatabase db = this.getWritableDatabase();
        String whereClause = ""+DIAGNOSIS_ELD_ID+" = ? AND "+DIAGNOSIS_TITLE+" = ?";
        String[] whereArgs = {diagnosis.getElderlyDocId(), diagnosis.getDiagnosis()};
        int result = db.delete(DIAGNOSIS_TBL, whereClause, whereArgs);
        db.close();
        if(result <= 0){
            return false;

        }else{
            return true;
        }
    }
    public boolean deleteHealthTip(HealthTip healthTip) {
        SQLiteDatabase db = this.getWritableDatabase();
        String whereClause = ""+HEALTH_TIP_TITLE+" = ? AND "+HEALTH_TIP_CONTENT+" = ?";
        String[] whereArgs = {healthTip.getTitle(), healthTip.getContent()};
        int result = db.delete(HEALTH_TIP_TBL, whereClause, whereArgs);
        db.close();
        if(result <= 0){
            return false;

        }else{
            return true;
        }
    }
    public boolean deleteMedicine(Medicine medicine) {
        SQLiteDatabase db = this.getWritableDatabase();
        String whereClause = ""+MEDICINE_ELD_ID+" = ? AND "+MEDICINE_TITLE+" = ?";
        String[] whereArgs = {medicine.getElderlyDocId(), medicine.getMedicine()};
        int result = db.delete(MEDICINE_TBL, whereClause, whereArgs);
        db.close();
        if(result <= 0){
            return false;

        }else{
            return true;
        }
    }
    public boolean deleteAllergy(Allergy allergy) {
        SQLiteDatabase db = this.getWritableDatabase();
        String whereClause = ""+ALLERGY_ELD_ID+" = ? AND "+ALLERGY_TITLE+" = ?";
        String[] whereArgs = {allergy.getElderlyDocId(), allergy.getAllergy()};
        int result = db.delete(ALLERGY_TBL, whereClause, whereArgs);
        db.close();
        if(result <= 0){
            return false;

        }else{
            return true;
        }
    }
    public boolean deleteSurgery(Surgery surgery) {
        SQLiteDatabase db = this.getWritableDatabase();
        String whereClause = ""+SURGERY_ELD_ID+" = ? AND "+SURGERY_TITLE+" = ? AND "+SURGERY_DATE+" = ?";
        String[] whereArgs = {surgery.getElderlyDocId(), surgery.getSurgery(),surgery.getDate().toString()};
        int result = db.delete(SURGERY_TBL, whereClause, whereArgs);
        db.close();
        if(result <= 0){
            return false;

        }else{
            return true;
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
    public List<Allergy> getAllergiesByElderlyDocId(String elderlyDocId) {
        List<Allergy> allergies = new ArrayList<>();

        SQLiteDatabase db = this.getReadableDatabase();

        // Define the WHERE clause to filter reminders by the given elderlyDocId
        String selection = ALLERGY_ELD_ID + " = ?";
        String[] selectionArgs = {elderlyDocId};

        Cursor cursor = db.query(ALLERGY_TBL, null, selection, selectionArgs, null, null, null);

        if (cursor.moveToFirst()) {
            do {
                String tempElderlyDocId = cursor.getString(cursor.getColumnIndexOrThrow(ALLERGY_ELD_ID));
                String title = cursor.getString(cursor.getColumnIndexOrThrow(ALLERGY_TITLE));

                Allergy allergy = new Allergy(tempElderlyDocId, title);
                allergies.add(allergy);

            } while (cursor.moveToNext());
        }

        cursor.close();
        db.close();

        return allergies;
    }


    public List<Diagnosis> getDiagnosisByElderlyDocId(String elderlyDocId) {
        List<Diagnosis> diagnosis = new ArrayList<>();

        SQLiteDatabase db = this.getReadableDatabase();

        // Define the WHERE clause to filter reminders by the given elderlyDocId
        String selection = DIAGNOSIS_ELD_ID + " = ?";
        String[] selectionArgs = {elderlyDocId};

        Cursor cursor = db.query(DIAGNOSIS_TBL, null, selection, selectionArgs, null, null, null);

        if (cursor.moveToFirst()) {
            do {
                String tempElderlyDocId = cursor.getString(cursor.getColumnIndexOrThrow(DIAGNOSIS_ELD_ID));
                String title = cursor.getString(cursor.getColumnIndexOrThrow(DIAGNOSIS_TITLE));

                Diagnosis diagnosisItem = new Diagnosis(tempElderlyDocId, title);
                diagnosis.add(diagnosisItem);

            } while (cursor.moveToNext());
        }

        cursor.close();
        db.close();

        return diagnosis;
    }
    public List<Medicine> getMedicinesByElderlyDocId(String elderlyDocId) {
        List<Medicine> medicines = new ArrayList<>();

        SQLiteDatabase db = this.getReadableDatabase();

        // Define the WHERE clause to filter reminders by the given elderlyDocId
        String selection = MEDICINE_ELD_ID + " = ?";
        String[] selectionArgs = {elderlyDocId};

        Cursor cursor = db.query(MEDICINE_TBL, null, selection, selectionArgs, null, null, null);

        if (cursor.moveToFirst()) {
            do {
                String tempElderlyDocId = cursor.getString(cursor.getColumnIndexOrThrow(MEDICINE_ELD_ID));
                String title = cursor.getString(cursor.getColumnIndexOrThrow(MEDICINE_TITLE));

                Medicine medicineItem = new Medicine(tempElderlyDocId, title);
                medicines.add(medicineItem);

            } while (cursor.moveToNext());
        }

        cursor.close();
        db.close();

        return medicines;
    }
    public List<Surgery> getSurgeriesByElderlyDocId(String elderlyDocId) {
        List<Surgery> surgeries = new ArrayList<>();

        SQLiteDatabase db = this.getReadableDatabase();

        // Define the WHERE clause to filter reminders by the given elderlyDocId
        String selection = SURGERY_ELD_ID + " = ?";
        String[] selectionArgs = {elderlyDocId};

        Cursor cursor = db.query(SURGERY_TBL, null, selection, selectionArgs, null, null, null);

        if (cursor.moveToFirst()) {
            do {
                String tempElderlyDocId = cursor.getString(cursor.getColumnIndexOrThrow(SURGERY_ELD_ID));
                String title = cursor.getString(cursor.getColumnIndexOrThrow(SURGERY_TITLE));
                String dateString = cursor.getString(cursor.getColumnIndexOrThrow(SURGERY_DATE));
                Date date = parseDateString(dateString);

                Surgery surgery = new Surgery(tempElderlyDocId, title,date);
                surgeries.add(surgery);

            } while (cursor.moveToNext());
        }

        cursor.close();
        db.close();

        return surgeries;
    }


    public Cursor readAllReminders() {
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

    public boolean deleteDoctorByDocId(String docId) {
        SQLiteDatabase db = this.getWritableDatabase();
        String whereClause = DOCUMNET_ID+" = ?";
        String[] whereArgs = {docId};
        int result = db.delete(DOCTORS_TBL, whereClause, whereArgs);
        db.close();

        // If the result is greater than 0, the deletion was successful
        if (result > 0){
            Log.w(TAG,"Deleted: ");
        }
        return result > 0;
    }

}