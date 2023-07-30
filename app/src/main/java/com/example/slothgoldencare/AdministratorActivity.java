package com.example.slothgoldencare;

import android.content.Intent;
import android.nfc.Tag;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.*;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;
import java.util.List;

public class AdministratorActivity extends AppCompatActivity{
    private static final String TAG = "ADMIN";

    private Button usersBtn;
    private Button elderliesBtn;
    private Button doctorsBtn;
    private Button payBtn;

    DataBaseHelper dbHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_administrator);
        dbHelper = new DataBaseHelper(this);


        usersBtn = findViewById(R.id.users_btn);
        elderliesBtn = findViewById(R.id.elderlies_btn);
        doctorsBtn = findViewById(R.id.doctors_btn);
        payBtn = findViewById(R.id.payBtn);

        usersBtn.setOnClickListener(v-> {
            Intent intent = new Intent(AdministratorActivity.this,AdministratorUsersActivity.class);
            startActivity(intent);
        });
        elderliesBtn.setOnClickListener(v-> {
            Intent intent = new Intent(AdministratorActivity.this,AdministratorElderliesActivity.class);
            startActivity(intent);
        });
        doctorsBtn.setOnClickListener(v-> {
            Intent intent = new Intent(AdministratorActivity.this,AdministratorDoctorsActivity.class);
            startActivity(intent);
        });
        payBtn.setOnClickListener(v-> {
            Intent intent = new Intent(AdministratorActivity.this,ManageDoctorActivity.class);
            startActivity(intent);
        });
    }

}
