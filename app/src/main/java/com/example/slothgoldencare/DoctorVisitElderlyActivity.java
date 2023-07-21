package com.example.slothgoldencare;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import com.example.slothgoldencare.Model.Elder;

public class DoctorVisitElderlyActivity  extends AppCompatActivity implements View.OnClickListener {

    private String elderlyId;
    private DataBaseHelper dataBaseHelper;
    private TextView editTextUsername;
    private CardView D1, D2, D4;


    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_doctor_visit_elderly);

        //statring variables
        elderlyId = getIntent().getStringExtra("elderlyId");
        dataBaseHelper = new DataBaseHelper(this);
        Elder elder = dataBaseHelper.getElderById(elderlyId);

        //edit layout
        editTextUsername = findViewById(R.id.username);
        editTextUsername.setText(elder.getUsername());

        D1 = findViewById(R.id.d1);
        D2 = findViewById(R.id.d2);
        D4 = findViewById(R.id.d4);

        D1.setOnClickListener(this);
        D2.setOnClickListener(this);
        D4.setOnClickListener(this);


    }

    @Override
    public void onClick(View v) {
        Intent i;
        switch (v.getId()) {
            case R.id.d1:
                i = new Intent(this, MedicalServices.class);
                startActivity(i);
                break;

            case R.id.d2:
                break;

            case R.id.d4:
                i = new Intent(this, ContactsActivity.class);
                startActivity(i);
                break;
        }
    }
}
