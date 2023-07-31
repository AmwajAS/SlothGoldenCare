package com.example.slothgoldencare;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;

import com.example.slothgoldencare.Model.Elder;
import com.example.slothgoldencare.Model.User;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import androidx.appcompat.app.AppCompatActivity;

import android.preference.Preference;
import android.view.Menu;
import android.view.MenuItem;

import com.google.firebase.auth.FirebaseAuth;

public class SettingsActivity extends AppCompatActivity {
    private FirebaseAuth auth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setteings);
        bottomNavigationView();
        auth = FirebaseAuth.getInstance();

        if (findViewById(R.id.fragment_container) != null) {
            if (savedInstanceState != null) {
                return;
            }
            getFragmentManager().beginTransaction().add(R.id.fragment_container, new SettingsFragment()).commit();
        }
    }

    /*
        this method handle the selected items / buttons of the bottom navigation bar.
         */
    public void bottomNavigationView() {
        DataBaseHelper dbHelper = new DataBaseHelper(this);

        BottomNavigationView bottomNavigationView = findViewById(R.id.bottomNavigation);
        bottomNavigationView.setSelectedItemId(R.id.settings);
        Menu menu = bottomNavigationView.getMenu();
        MenuItem currentItem = menu.findItem(R.id.current);

        // Hiding the "current" menu item
        currentItem.setVisible(false);
        bottomNavigationView.setOnItemSelectedListener(item -> {
            switch (item.getItemId()) {
                case R.id.home:
                    if (dbHelper.getElderByDocumentId(auth.getUid()) != null) {
                        replaceView(HomePageActivity.class);
                    } else if (dbHelper.getUserByDocumentId(auth.getUid()) != null) {
                        replaceView(UserHomePageActivity.class);
                    }
                    return true;
                case R.id.settings:
                    replaceView(SettingsActivity.class);
                    return true;
                case R.id.profile:
                    if (dbHelper.getElderByDocumentId(auth.getUid()) != null) {
                        replaceView(ProfileActivity.class);
                    } else if (dbHelper.getUserByDocumentId(auth.getUid()) != null) {
                        replaceView(UserHomePageActivity.class);

                    }
                    return true;
            }
            return false;
        });
    }

    /*
    This method used to replace the View to other one.
     */
    public void replaceView(Class classView) {
        startActivity(new Intent(getApplicationContext(), classView));
        overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
        finish();
    }

}