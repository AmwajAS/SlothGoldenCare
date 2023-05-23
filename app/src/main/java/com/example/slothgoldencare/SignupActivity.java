package com.example.slothgoldencare;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;

import java.sql.SQLDataException;

public class SignupActivity extends AppCompatActivity {

    private EditText userID;
    private EditText userName;
    private EditText userPhone;
    private EditText userDOB;
    private ImageButton maleBtn;
    private ImageButton femaleBtn;
    private Button signup;
    DataBaseManager dbManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);
        userID = (EditText) findViewById(R.id.userID);
        userName = (EditText) findViewById(R.id.userName);
        userPhone = (EditText) findViewById(R.id.userPhone);
        userDOB = (EditText) findViewById(R.id.userDOB);

        dbManager = new DataBaseManager(this);
        try{
            dbManager.open();
        } catch (SQLDataException e) {
            throw new RuntimeException(e);
        }


    }

    public void btnSignUpPressed(View v){
        dbManager.insert(userID.getText().toString(), userName.getText().toString(), userPhone.getText().toString());
    }
}