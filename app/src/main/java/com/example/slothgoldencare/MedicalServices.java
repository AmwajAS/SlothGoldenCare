package com.example.slothgoldencare;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import com.example.slothgoldencare.Doctor.DoctorsFragment;
import com.google.android.material.bottomnavigation.BottomNavigationView;

public class MedicalServices extends AppCompatActivity implements View.OnClickListener{
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.medical_services);

        //initializing card views with on click listeners
        CardView d1 = findViewById(R.id.d1);
        CardView d2 = findViewById(R.id.d2);
        CardView d3 = findViewById(R.id.d3);
        CardView d4 = findViewById(R.id.d4);
        CardView d5 = findViewById(R.id.d5);

        d1.setOnClickListener(this);
        d2.setOnClickListener(this);
        d3.setOnClickListener(this);
        d4.setOnClickListener(this);
        d5.setOnClickListener(this);

        bottomNavigationView();

    }

    //depending on which one is clicked it sends a different args that can be handled in the fragment.
    @Override
    public void onClick(View v) {
        HealthStatusFragment healthStatusFragment;
        switch (v.getId()) {
            case R.id.d1:
                Bundle args1 = new Bundle();
                replaceFragment(new DoctorsFragment());
                break;
            case R.id.d2:
                Bundle args2 = new Bundle();
                args2.putString("Button","diagnosis");
                healthStatusFragment= HealthStatusFragment.newInstance(args2);
                replaceFragment(healthStatusFragment);
                break;
            case R.id.d3:
                Bundle args3 = new Bundle();
                args3.putString("Button","medicines");
                healthStatusFragment= HealthStatusFragment.newInstance(args3);
                replaceFragment(healthStatusFragment);
                break;
            case R.id.d4:
                Bundle args4 = new Bundle();
                args4.putString("Button","allergies");
                healthStatusFragment= HealthStatusFragment.newInstance(args4);
                replaceFragment(healthStatusFragment);
                break;
            case R.id.d5:
                Bundle args5 = new Bundle();
                args5.putString("Button","surgeries");
                healthStatusFragment= HealthStatusFragment.newInstance(args5);
                replaceFragment(healthStatusFragment);
                break;
        }
    }

    //replace fragment function
    private void replaceFragment(Fragment fragment) {
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.MedicalServicesFrameLayout, fragment);
        fragmentTransaction.commit();
    }



    /*
this method handle the selected items / buttons of the bottom navigation bar.
 */
    public void bottomNavigationView() {
        BottomNavigationView bottomNavigationView = findViewById(R.id.bottomNavigation);
        bottomNavigationView.setSelectedItemId(R.id.current);
        Menu menu = bottomNavigationView.getMenu();
        MenuItem currentItem = menu.findItem(R.id.current);
        // Hiding the "current" menu item
        currentItem.setVisible(false);

        bottomNavigationView.setOnItemSelectedListener(item -> {
            switch (item.getItemId()) {
                case R.id.home:
                    replaceView(HomePageActivity.class);
                    return true;
                case R.id.settings:
                    replaceView(SettingsActivity.class);
                    return true;
                case R.id.profile:
                    replaceView(ProfileActivity.class);
                    return true;
            }
            return false;
        });
    }

    public void replaceView(Class classView) {
        startActivity(new Intent(getApplicationContext(), classView));
        overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
        finish();
    }

}