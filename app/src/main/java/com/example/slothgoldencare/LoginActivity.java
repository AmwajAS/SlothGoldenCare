package com.example.slothgoldencare;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class LoginActivity extends AppCompatActivity {

    private static final String TAG = "LoginActivity";

    private EditText userid;
    DataBaseHelper dbHelper;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_login);
        Button elderBtn = (Button) findViewById(R.id.elderBtn);
        Button userBtn = (Button) findViewById(R.id.userBtn);
        userid = (EditText) findViewById(R.id.userid);
        Button loginBtn = (Button) findViewById(R.id.loginBtn);
        dbHelper = new DataBaseHelper(this);

        loginBtn.setOnClickListener(v -> {
            String uid = userid.getText().toString();
            if (uid.equals("")) { //check if the id number / field is empty.
                Toast.makeText(LoginActivity.this, "Please enter your ID", Toast.LENGTH_SHORT).show();
                Log.i(TAG, "This is a debug message " + uid); // Debug log

            } else {
             //   if (checkIDValidation(uid)) {
                    if (dbHelper.checkUserID(uid)) {
                        Log.i(TAG, "This is a debug message " + " Login Successfully"); // Debug log
                        Toast.makeText(LoginActivity.this, "Login Successfully", Toast.LENGTH_SHORT).show();
                        Intent intent = new Intent(getApplicationContext(), HomePageActivity.class);
                        startActivity(intent);
                    } else {
                        Log.i(TAG, "This is a debug message " + " Failed to Login"); // Debug log

                        Toast.makeText(LoginActivity.this, "Failed to Login", Toast.LENGTH_SHORT).show();

                    }
                //}
            }
        });


        elderBtn.setOnClickListener(v -> {
            Intent intent = new Intent(getApplicationContext(), ElderSignupActivity.class);
            startActivity(intent);
        });
        userBtn.setOnClickListener(v -> {
            Intent intent = new Intent(getApplicationContext(), UserSignupActivity.class);
            startActivity(intent);
        });


    }

    /*
    Since the Real Id Number consists of 9 digits Only, So in this method we check the Id Validation.
    9 numbers only between 0-9.
     */
    public boolean checkIDValidation(String idV) {
        return idV.matches("[0-9]{9}");
    }


//    public int checkUserType(User user){
//        if(user instanceof Elder){
//            return 1;
//        }else {
//            return 0;
//        }
//    }


//    private void toastMessage(String message) {
//        Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
//    }

}