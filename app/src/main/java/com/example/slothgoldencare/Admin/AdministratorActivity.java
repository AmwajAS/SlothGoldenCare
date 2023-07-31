package com.example.slothgoldencare.Admin;

import android.content.Intent;
import android.os.Bundle;
import android.widget.*;

import androidx.appcompat.app.AppCompatActivity;
import com.example.slothgoldencare.DataBaseHelper.DataBaseHelper;
import com.example.slothgoldencare.LoginActivity;
import com.example.slothgoldencare.ManageDoctorActivity;
import com.example.slothgoldencare.R;
import com.google.firebase.auth.FirebaseAuth;

public class AdministratorActivity extends AppCompatActivity{
    private static final String TAG = "ADMIN";

    private Button usersBtn;
    private Button elderliesBtn;
    private Button doctorsBtn;
    private Button payBtn;
    private Button backBtn;

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
        backBtn = findViewById(R.id.back_btn);

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
            Intent intent = new Intent(AdministratorActivity.this, ManageDoctorActivity.class);
            startActivity(intent);
        });

        backBtn.setOnClickListener(v->{
            FirebaseAuth auth = FirebaseAuth.getInstance();
            if(auth.getCurrentUser() != null){
                auth.signOut();
            }
            Intent intent = new Intent(AdministratorActivity.this, LoginActivity.class);
            startActivity(intent);
            this.finish();
        });
    }

}
