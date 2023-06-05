package com.example.slothgoldencare;

import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import java.sql.SQLDataException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;
import java.util.regex.Matcher;
import java.util.regex.Pattern;


public class ElderSignupActivity extends AppCompatActivity {

    private static final String TAG = "ElderSignupActivity";
    private EditText userID;
    private EditText userName;
    private EditText userPhone;
    private RadioButton maleBtn;
    private RadioButton femaleBtn;
    private Button signupBtn;
    private RadioGroup genderGroup;
    DataBaseManager dbManager;
    private EditText etSelectDate;

    DataBaseHelper dbHelper = new DataBaseHelper(this);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_elder_signup);
        userID = findViewById(R.id.userID);
        userName = findViewById(R.id.userName);
        userPhone = findViewById(R.id.userPhone);
        etSelectDate = findViewById(R.id.etSelectDate);
        maleBtn = findViewById(R.id.maleBtn);
        femaleBtn = findViewById(R.id.femaleBtn);

        dbManager = new DataBaseManager(this);
        try {
            dbManager.open();
        } catch (SQLDataException e) {
            throw new RuntimeException(e);
        }

        etSelectDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDatePickerDialog(etSelectDate);
            }
        });


        signupBtn = (Button) findViewById(R.id.signupBtn);
        signupBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String newID = userID.getText().toString();
                String newName = userName.getText().toString();
                String newPhone = userPhone.getText().toString();
                Date newDate = convertStringIntoDate(etSelectDate.getText().toString());
                Gender elderGender = onGenderSelection();
                Log.i(TAG, "This is a debug message" + userID.toString() + userName.toString() + userPhone.toString()); // Debug log

                if ((newID.length() != 0) && (newName.length() != 0) && (newPhone.length() != 0) && (newDate != null) && (elderGender != null)) {
                    if (!checkIDValidation(newID)) {
                        SimpleDialog dialog = null;
                        //show AlertDialog;
                        SimpleDialog.showAlertDialog(ElderSignupActivity.this, R.string.alert_title_signup, R.string.alert_message_id);

                    } else if (!validatePhoneNumber(newPhone)) {
                        SimpleDialog.showAlertDialog(ElderSignupActivity.this, R.string.alert_title_signup, R.string.alert_message_phone);

                    } else {
                        Elder elder = new Elder(newID, newName, newPhone, newDate, elderGender);
                        insertData(elder);
                        userID.setText("");
                        userName.setText("");
                        userPhone.setText("");
                    }


                } else {
                    SimpleDialog.showAlertDialog(ElderSignupActivity.this, R.string.alert_title_signup, R.string.alert_message_null);
                }
            }
        });
    }



    /*
     Since the Real Id Number consists of 9 digits Only, So in this method we check the Id Validation.
     9 numbers only between 0-9.
    */
    public static boolean checkIDValidation(String idV) {
        boolean valid = false;

        if (idV.matches("[0-9]{9}")) {
            return true;
        } else {
            return false;
        }
    }

    public static boolean validatePhoneNumber(String phoneNumber) {
        // Define the regular expression pattern
        String pattern = "^05\\d{8}$";

        // Create a pattern object
        Pattern regex = Pattern.compile(pattern);

        // Create a matcher object
        Matcher matcher = regex.matcher(phoneNumber);

        // Check if the phone number matches the pattern
        return matcher.matches();
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



    private void showDatePickerDialog(final TextView textView) {

        final Calendar calendar = Calendar.getInstance();
        final int year = calendar.get(Calendar.YEAR);
        final int month = calendar.get(Calendar.MONTH);
        final int day = calendar.get(Calendar.DAY_OF_MONTH);
        DatePickerDialog dialog = new DatePickerDialog(ElderSignupActivity.this, new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                month = month + 1;
                String date = dayOfMonth + "/" + month + "/" + year;
                textView.setText(date);
            }
        }, year, month, day);
        dialog.show();
    }

    private void toastMessage(String data_successfully_inserted) {
    }


}