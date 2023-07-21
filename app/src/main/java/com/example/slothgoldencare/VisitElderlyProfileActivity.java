package com.example.slothgoldencare;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import com.example.slothgoldencare.Model.Elder;

public class VisitElderlyProfileActivity  extends AppCompatActivity implements View.OnClickListener {

    private String elderlyId;
    private DataBaseHelper dataBaseHelper;
    private TextView editTextUsername;
    private CardView D1, D2, D3, D4, D5, D6;


    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_visit_elderly_profile);

        //statring variables
        elderlyId = getIntent().getStringExtra("elderlyId");
        dataBaseHelper = new DataBaseHelper(this);
        Elder elder = dataBaseHelper.getElderById(elderlyId);

        //edit layout
        editTextUsername = findViewById(R.id.username);
        editTextUsername.setText(elder.getUsername());

        D1 = findViewById(R.id.d1);
        D2 = findViewById(R.id.d2);
        D3 = findViewById(R.id.d3);
        D4 = findViewById(R.id.d4);
        D5 = findViewById(R.id.d5);
        D6 = findViewById(R.id.d6);

        D1.setOnClickListener(this);
        D2.setOnClickListener(this);
        D3.setOnClickListener(this);
        D4.setOnClickListener(this);
        D5.setOnClickListener(this);
        D6.setOnClickListener(this);

    }

    @Override
    public void onClick(View v) {
        Intent i;
        switch (v.getId()) {
            case R.id.d1:
                i = new Intent(this, MedicalServices.class);
                startActivity(i);
                break;
            case R.id.d5:
                break;
            case R.id.d6:
                i = new Intent(this, BottomNavigationBarActivity.class);
                startActivity(i);
                break;
            case R.id.d4:
                i = new Intent(this, ContactsActivity.class);
                startActivity(i);
                break;
        }
    }
}
