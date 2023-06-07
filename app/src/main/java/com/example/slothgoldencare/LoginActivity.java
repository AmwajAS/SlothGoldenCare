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
    public static User user;
    public static Elder elder;
    public static boolean flag;


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
            if (uid.equals("")) {
                //check if the id number / field is empty.
                SimpleDialog.showAlertDialog(LoginActivity.this, R.string.alert_title_login, R.string.alert_message_idEmtpy);

            }
            else if(uid.equals("admin")){
                Intent intent = new Intent(getApplicationContext(),AdministratorActivity.class);
                startActivity(intent);
                userid.setText("");

            }else {
                //   if (checkIDValidation(uid)) {
                if ((user = dbHelper.findUserByID(uid)) != null) {
                   // Log.i(TAG, "This is a debug message " + " Login Successfully" + user.getUsername()); // Debug log
                    flag = true;
                    Toast.makeText(LoginActivity.this, "Login Successfully", Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(getApplicationContext(), UserHomePageActivity.class);
                    startActivity(intent);
                    userid.setText("");
                } else if ((elder = dbHelper.findElderByID(uid)) != null) {
                    flag = false;
                    Intent intent = new Intent(getApplicationContext(), HomePageActivity.class);
                    startActivity(intent);
                    userid.setText("");
                } else {
                    //Log.i(TAG, "This is a debug message " + " Failed to Login"); // Debug log
                    SimpleDialog.showAlertDialog(LoginActivity.this, R.string.alert_title_login, R.string.alert_message_failed);
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

    public static User getUser() {
        return user;
    }

    public static Elder getElder() {
        return elder;
    }

    public static boolean getFlag() {
        return flag;
    }
}