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
import com.example.slothgoldencare.Model.Doctor;

import java.util.ArrayList;
import java.util.List;

public class DoctorActivityMain extends AppCompatActivity{
    private static final String TAG = "ADMIN";

    private Button healthTipsBtn;
    private Button elderliesBtn;
    private Button appointmentsBtn;
    private Button workPayBtn;

    DataBaseHelper dbHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_doctor_main);
        dbHelper = new DataBaseHelper(this);
        String doctorUid = getIntent().getStringExtra("doctorUid");

        elderliesBtn = findViewById(R.id.elderlies_btn);
        healthTipsBtn = findViewById(R.id.health_tips_btn);
        appointmentsBtn = findViewById(R.id.appointments_button);
        workPayBtn= findViewById(R.id.work_pay_button);


        elderliesBtn.setOnClickListener(v-> {
            Intent intent = new Intent(DoctorActivityMain.this, DoctorActivity.class);
            startActivity(intent);
        });

        healthTipsBtn.setOnClickListener(v-> {
            Intent intent = new Intent(DoctorActivityMain.this, DoctorActivityHealthTips.class);
            startActivity(intent);
        });

        appointmentsBtn.setOnClickListener(v-> {
            Intent intent = new Intent(DoctorActivityMain.this, AppointmentsPatientsListActivity.class);
            intent.putExtra("doctorUid", doctorUid);
            startActivity(intent);
        });
        workPayBtn.setOnClickListener(v-> {
            Intent intent = new Intent(DoctorActivityMain.this, DoctorReportActivity.class);
            intent.putExtra("doctorUid", doctorUid);
            startActivity(intent);
        });
    }

}
