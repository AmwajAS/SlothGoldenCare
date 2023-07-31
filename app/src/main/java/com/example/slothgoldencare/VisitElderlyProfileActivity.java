package com.example.slothgoldencare;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import com.example.slothgoldencare.DataBaseHelper.DataBaseHelper;
import com.example.slothgoldencare.Doctor.DoctorsFragment;
import com.example.slothgoldencare.Model.Elder;
import com.example.slothgoldencare.Reminder.TODOActivity;

public class VisitElderlyProfileActivity  extends AppCompatActivity implements View.OnClickListener {

    private  Elder elder;
    private String elderlyId;


    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_visit_elderly_profile);

        //starting variables
        elderlyId = getIntent().getStringExtra("elderlyId");
        DataBaseHelper dataBaseHelper = new DataBaseHelper(this);
        elder = dataBaseHelper.getElderById(elderlyId);
        Button backBtn = findViewById(R.id.back_btn);
        backBtn.setOnClickListener(view -> this.finish());

        //edit layout
        TextView editTextUsername = findViewById(R.id.username);
        editTextUsername.setText(elder.getUsername());

        CardView d1 = findViewById(R.id.d1);
        CardView d2 = findViewById(R.id.d2);
        CardView d3 = findViewById(R.id.d3);
        CardView d4 = findViewById(R.id.d4);
        CardView d5 = findViewById(R.id.d5);
        CardView d6 = findViewById(R.id.d6);

        d1.setOnClickListener(this);
        d2.setOnClickListener(this);
        d3.setOnClickListener(this);
        d4.setOnClickListener(this);
        d5.setOnClickListener(this);
        d6.setOnClickListener(this);

    }

    @Override

    public void onClick(View v) {
        HealthStatusRelativeFragment healthStatusFragment;
        Intent i;
        switch (v.getId()) {
            case R.id.d1:
                Bundle args1 = new Bundle();
                replaceFragment(new DoctorsFragment());
                break;
            case R.id.d2:
                Intent intent = new Intent(this.getApplicationContext(), TODOActivity.class);
                intent.putExtra("elderlyId", elderlyId);
                startActivity(intent);
                break;
            case R.id.d3:
                Bundle args3 = new Bundle();
                args3.putString("Button","medicines");
                args3.putString("elderlyDocId",elder.getDocId());
                healthStatusFragment= HealthStatusRelativeFragment.newInstance(args3);
                replaceFragment(healthStatusFragment);
                break;
            case R.id.d4:
                Bundle args4 = new Bundle();
                args4.putString("Button","allergies");
                args4.putString("elderlyDocId",elder.getDocId());
                healthStatusFragment= HealthStatusRelativeFragment.newInstance(args4);
                replaceFragment(healthStatusFragment);
                break;
            case R.id.d5:
                Bundle args5 = new Bundle();
                args5.putString("Button","diagnosis");
                args5.putString("elderlyDocId",elder.getDocId());
                healthStatusFragment= HealthStatusRelativeFragment.newInstance(args5);
                replaceFragment(healthStatusFragment);
                break;
            case R.id.d6:
                Bundle args6 = new Bundle();
                args6.putString("Button","surgeries");
                args6.putString("elderlyDocId",elder.getDocId());
                healthStatusFragment= HealthStatusRelativeFragment.newInstance(args6);
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
