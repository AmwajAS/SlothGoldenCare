package com.example.slothgoldencare;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.FrameLayout;

import com.example.slothgoldencare.databinding.ActivityBottomNavigationBarBinding;

public class cardView1InHomePage extends AppCompatActivity implements View.OnClickListener{

    ActivityBottomNavigationBarBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.card_view1_in_home_page); // Set the layout here
        binding = ActivityBottomNavigationBarBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        startOpeningView(true);

        binding.bottomNavigationBar.setOnItemSelectedListener(item -> {
            Intent i;
            switch (item.getItemId()){
                case R.id.currentView:
                    FrameLayout f= (FrameLayout)findViewById(R.id.frame_layout);
                    LayoutInflater l= getLayoutInflater();
                    View view = l.inflate(R.layout.medical_services,null );
                    f.addView(view);
                    break;

                case R.id.home:
                    i = new Intent(this, HomePageActivity.class);
                    startActivity(i);
                    break;
                case R.id.profile:
                    replaceFragment(new ProfileFragment());
                    break;
                case R.id.settings:
                    replaceFragment(new SettingsFragment());
                    break;
            }
            return true;
        });
    }

    @Override
    public void onClick(View v) {
        Intent i;
        switch (v.getId()) {
            case R.id.d1:
                replaceFragment(new DoctorsFragment());
                break;
            case R.id.d2:
                replaceFragment(new DiagnosisTreatmentsFragment());
                break;
            case R.id.d3:
                replaceFragment(new MedicationsFragment());
                break;
        }
    }

    private void replaceFragment(Fragment fragment){
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.frame_layout, fragment);
        fragmentTransaction.commit();
    }
    //des func to inflate the view
    private void startOpeningView(boolean b){
        if (b) {
            FrameLayout f = (FrameLayout) findViewById(R.id.frame_layout);
            LayoutInflater l = (LayoutInflater) getSystemService(LAYOUT_INFLATER_SERVICE);
            View view = l.inflate(R.layout.medical_services, null, false);
            f.addView(view);
            CardView D1 = view.findViewById(R.id.d1);
            CardView D2 = view.findViewById(R.id.d2);
            CardView D3 = view.findViewById(R.id.d3);
            CardView D4 = view.findViewById(R.id.d4);
            D1.setOnClickListener(this);
            D2.setOnClickListener(this);
            D3.setOnClickListener(this);
            D4.setOnClickListener(this);
        }
    }

}