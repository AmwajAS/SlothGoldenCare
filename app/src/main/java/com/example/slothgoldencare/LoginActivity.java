package com.example.slothgoldencare;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class LoginActivity extends AppCompatActivity {

    private static final String TAG = "LoginActivity";

    private EditText userid;
    private Button loginBtn;
    private Button elderBtn;
    private Button userBtn;
    DataBaseHelper dbHelper;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_login);
        elderBtn =(Button) findViewById(R.id.elderBtn);
        userBtn = (Button) findViewById(R.id.userBtn);
        userid = (EditText) findViewById(R.id.userid);
        loginBtn = (Button) findViewById(R.id.loginBtn);
        dbHelper = new DataBaseHelper(this);
        loginBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String uid = userid.getText().toString();
                if(uid.equals("")){
                    Toast.makeText(LoginActivity.this, "Please enter your ID", Toast.LENGTH_SHORT).show();
                    Log.i(TAG, "This is a debug message " + uid); // Debug log

                }else{
                    Boolean checkUID =dbHelper.checkUserID(uid);
                    if(checkUID){
                        Log.i(TAG, "This is a debug message " + " Login Successfully"); // Debug log

                        Toast.makeText(LoginActivity.this, "Login Successfully", Toast.LENGTH_SHORT).show();
                        Intent intent = new Intent(getApplicationContext(), HomePageActivity.class);
                        startActivity(intent);
                    }
                    else{
                        Log.i(TAG, "This is a debug message " + " Failed to Login"); // Debug log

                        Toast.makeText(LoginActivity.this, "Failed to Login", Toast.LENGTH_SHORT).show();

                    }
                }
            }
        });



        elderBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), ElderSignupActivity.class);
                startActivity(intent);
            }
        });
        userBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), UserSignupActivity.class);
                startActivity(intent);
            }
        });





    }
//    public void checkID() {
//        loginBtn.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                // Perform action when button is clicked
//                // Create an Intent to start the new activity
//                String uid = userid.getText().toString();
//
//                if (uid.length() == 0 ) {
//                    toastMessage("Please fill ID field.");
//                    Log.i(TAG, "This is a debug message"); // Debug log
//
//                } else {
//                    Log.i(TAG, "This is a debug message "+ uid);
//                    Boolean checkUID = dbHelper.checkUserID(uid);
//                    Log.i(TAG, "This is a debug message"+ checkUID); // Debug log
//
//                    if (checkUID) {
//                        Intent intent = new Intent(LoginActivity.this, HomePageActivity.class);
//                        startActivity(intent);
//                    } else {
//                        toastMessage("Something Went Wrong - ID doesn't exist");
//                    }
//                }
//
//            }
//        });
//
//
//
//    }


    private void toastMessage(String message) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
    }

}