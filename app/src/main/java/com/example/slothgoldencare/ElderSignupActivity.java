package com.example.slothgoldencare;

import androidx.appcompat.app.AppCompatActivity;

import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;

import java.sql.SQLDataException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;


public class ElderSignupActivity extends AppCompatActivity {

    private static final String TAG = "ElderSignupActivity";
    private EditText userID;
    private EditText userName;
    private EditText userPhone;
    private EditText userDob;
    private RadioButton maleBtn;
    private RadioButton femaleBtn;
    private Button signupBtn;
    private RadioGroup genderGroup;
    DataBaseManager dbManager;

    DataBaseHelper dbHelper = new DataBaseHelper(this);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_elder_signup);
        userID = findViewById(R.id.userID);
        userName = findViewById(R.id.userName);
        userPhone = findViewById(R.id.userPhone);
        userDob = findViewById(R.id.userDob);
        maleBtn = findViewById(R.id.maleBtn);
        femaleBtn = findViewById(R.id.femaleBtn);

        dbManager = new DataBaseManager(this);
        try {
            dbManager.open();
        } catch (SQLDataException e) {
            throw new RuntimeException(e);
        }
       // dbHelper.dropTable();




        signupBtn = (Button) findViewById(R.id.signupBtn);
        signupBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Log.i(TAG, "asdxawcswc");
                String newID = userID.getText().toString();
                String newName = userName.getText().toString();
                String newPhone = userPhone.getText().toString();
                Date newDate = convertStringIntoDate(userDob.getText().toString());
                //Log.i(TAG, "This is a debug message" + userDob +"after converting "  +newDate); // Debug log
                String strDate = formatDateOfBirth(newDate);
               // Log.i(TAG, "This is a debug message using strDate" + strDate );
                Gender elderGender = onGenderSelection();
                Log.i(TAG, "This is a debug message" + userID.toString() + userName.toString() + userPhone.toString()); // Debug log

                if ((newID.length() != 0) && (newName.length() != 0) && (newPhone.length() != 0)) {

                    Elder elder = new Elder(newID, newName, newPhone, newDate, elderGender);
                    insertData(elder);
                    userID.setText("");
                    userName.setText("");
                    userPhone.setText("");
                    userDob.setText("");
                }
            }
        });
    }

    private void insertData(Elder elder) {
        boolean insertData;
        try {
            insertData = dbHelper.addElderData(elder);

        } catch (NullPointerException e) {
            throw new RuntimeException(e);
        }
        if (insertData) {
            toastMessage("Data Successfully inserted");
        } else {
            toastMessage("Something Went Wrong, Please Try again");

        }


    }


    public Gender onGenderSelection() {
        Gender userGender = null;

        RadioGroup radioGroup = findViewById(R.id.genderGroup);
        switch (radioGroup.getCheckedRadioButtonId()) {
            case R.id.maleBtn:
                userGender = Gender.Male;
                break;
            case R.id.femaleBtn:
                userGender = Gender.Female;
                break;
        }
        return userGender;
    }


    public Date convertStringIntoDate(String dateString) {
        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
        Date date = null;
        try {
            date = sdf.parse(dateString);
            // Your code to handle the parsed date
        } catch (ParseException e) {
            // Handle the case where the input is not a valid date
        }
        return date;
    }

    public String formatDateOfBirth(Date date) {
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());
        return dateFormat.format(date);
    }


    private void toastMessage(String data_successfully_inserted) {
    }
}