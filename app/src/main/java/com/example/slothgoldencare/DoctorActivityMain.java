package com.example.slothgoldencare;
import android.content.Intent;
import android.os.Bundle;
import android.widget.*;
import androidx.appcompat.app.AppCompatActivity;
import com.example.slothgoldencare.Model.Doctor;
import com.google.firebase.auth.FirebaseAuth;


public class DoctorActivityMain extends AppCompatActivity{
    FirebaseAuth auth;
    DataBaseHelper dbHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_doctor_main);
        dbHelper = new DataBaseHelper(this);
        auth = FirebaseAuth.getInstance();
        Doctor doctor = dbHelper.getDoctorByDocumentId(auth.getUid());

        Button elderliesBtn = findViewById(R.id.elderlies_btn);
        Button healthTipsBtn = findViewById(R.id.health_tips_btn);
        Button appointmentsBtn = findViewById(R.id.appointments_button);
        Button workPayBtn = findViewById(R.id.work_pay_button);
        Button signOutButton = findViewById(R.id.log_out_btn);
        TextView doctorUsername = findViewById(R.id.doctor_username);

        doctorUsername.setText(doctor.getUsername());


        //new activity for a list of elderlies
        elderliesBtn.setOnClickListener(v-> {
            Intent intent = new Intent(DoctorActivityMain.this, DoctorActivity.class);
            startActivity(intent);
        });

        //new activity for a list of health tips
        healthTipsBtn.setOnClickListener(v-> {
            Intent intent = new Intent(DoctorActivityMain.this, DoctorActivityHealthTips.class);
            startActivity(intent);
        });

        //new activity for appointments
        appointmentsBtn.setOnClickListener(v-> {
            Intent intent = new Intent(DoctorActivityMain.this, AppointmentsPatientsListActivity.class);
            startActivity(intent);
        });

        //new activity for work and pay
        workPayBtn.setOnClickListener(v-> {
            Intent intent = new Intent(DoctorActivityMain.this, DoctorReportActivity.class);
            startActivity(intent);
        });

        signOutButton.setOnClickListener(v->{
            auth.signOut();
            Intent intent = new Intent(DoctorActivityMain.this, LoginActivity.class);
            startActivity(intent);
        });

    }
}
