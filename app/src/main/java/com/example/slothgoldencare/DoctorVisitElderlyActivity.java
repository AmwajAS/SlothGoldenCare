package com.example.slothgoldencare;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.view.View;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import com.example.slothgoldencare.Model.Elder;

public class DoctorVisitElderlyActivity  extends AppCompatActivity implements View.OnClickListener {

    private String elderlyId;
    private DataBaseHelper dataBaseHelper;
    private TextView editTextUsername;
    private CardView D1, D2, D3, D4, D5;
    private Elder elder;


    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_doctor_visit_elderly);

        //statring variables
        elderlyId = getIntent().getStringExtra("elderlyId");
        dataBaseHelper = new DataBaseHelper(this);
        elder = dataBaseHelper.getElderById(elderlyId);

        //edit layout
        editTextUsername = findViewById(R.id.username);
        editTextUsername.setText(elder.getUsername());

        D1 = findViewById(R.id.d1);
        D2 = findViewById(R.id.d2);
        D3 = findViewById(R.id.d3);
        D4 = findViewById(R.id.d4);
        D5 = findViewById(R.id.d5);

        D1.setOnClickListener(this);
        D2.setOnClickListener(this);
        D3.setOnClickListener(this);
        D4.setOnClickListener(this);
        D5.setOnClickListener(this);


    }

    @Override
    public void onClick(View v) {
        HealthStatusDoctorFragment healthStatusFragment;
        Intent i;
        switch (v.getId()) {
            case R.id.d1:
                Bundle args1 = new Bundle();
                replaceFragment(new DoctorsFragment());
                break;
            case R.id.d2:
                Bundle args2 = new Bundle();
                args2.putString("elderly",elder.getDocId());
                args2.putString("Button","diagnosis");
                healthStatusFragment = HealthStatusDoctorFragment.newInstance(args2);
                replaceFragment(healthStatusFragment);
                break;
            case R.id.d3:
                Bundle args3 = new Bundle();
                args3.putString("elderly",elder.getDocId());
                args3.putString("Button","medicine");
                healthStatusFragment= HealthStatusDoctorFragment.newInstance(args3);
                replaceFragment(healthStatusFragment);
                break;
            case R.id.d4:
                Bundle args4 = new Bundle();
                args4.putString("elderly",elder.getDocId());
                args4.putString("Button","allergies");
                healthStatusFragment= HealthStatusDoctorFragment.newInstance(args4);
                replaceFragment(healthStatusFragment);
                break;
            case R.id.d5:
                Bundle args5 = new Bundle();
                args5.putString("elderly",elder.getDocId());
                args5.putString("Button","surgeries");
                healthStatusFragment= HealthStatusDoctorFragment.newInstance(args5);
                replaceFragment(healthStatusFragment);
                break;

        }
    }
    private void replaceFragment(Fragment fragment) {
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.MedicalServicesFrameLayout, fragment);
        fragmentTransaction.commit();
    }

}
