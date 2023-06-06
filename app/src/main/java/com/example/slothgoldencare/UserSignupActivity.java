package com.example.slothgoldencare;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import android.util.Log;


import java.sql.SQLDataException;

public class UserSignupActivity extends AppCompatActivity {
    private static final String TAG = "UserSignupActivity";
    private EditText userID;
    private EditText userName;
    private EditText userPhone;
    private Button signup;
    DataBaseManager dbManager;

    DataBaseHelper dbHelper = new DataBaseHelper(this);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_signup);
        userID = findViewById(R.id.userID);
        userName = findViewById(R.id.userName);
        userPhone = findViewById(R.id.userPhone);
        dbManager = new DataBaseManager(this);
        try {
            dbManager.open();
        } catch (SQLDataException e) {
            throw new RuntimeException(e);
        }
        signup = (Button) findViewById(R.id.signupBtn);
        signup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String newID = userID.getText().toString();
                String newName = userName.getText().toString();
                String newPhone = userPhone.getText().toString();
                //Log.i(TAG, "This is a debug message" + userID.toString() + userName.toString() + userPhone.toString()); // Debug log

                if ((newID.length() != 0) && (newName.length() != 0) && (newPhone.length() != 0)) {
                    if (!ElderSignupActivity.checkIDValidation(newID)) {
                        SimpleDialog.showAlertDialog(UserSignupActivity.this, R.string.alert_title_signup, R.string.alert_message_id);
                    } else if (ElderSignupActivity.validatePhoneNumber(newPhone)) {
                        SimpleDialog.showAlertDialog(UserSignupActivity.this, R.string.alert_title_signup, R.string.alert_message_phone);
                    } else {
                        User user = new User(newID, newName, newPhone);
                        insertData(user);
                        userID.setText("");
                        userName.setText("");
                        userPhone.setText("");
                    }
                }
            }
        });

    }


    public void insertData(User newEntry) {
        boolean insertData;
        try {
            insertData = dbHelper.addUserData(newEntry);

        } catch (NullPointerException e) {
            throw new RuntimeException(e);
        }
        if (insertData) {
            toastMessage("Data Successfully inserted");
        } else {
            toastMessage("Something Went Wrong");

        }
    }

    private void toastMessage(String message) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
    }


}