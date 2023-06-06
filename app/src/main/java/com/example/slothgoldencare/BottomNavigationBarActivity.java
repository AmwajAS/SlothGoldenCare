package com.example.slothgoldencare;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;

import com.example.slothgoldencare.databinding.ActivityBottomNavigationBarBinding;


public class BottomNavigationBarActivity extends AppCompatActivity {
    ActivityBottomNavigationBarBinding binding;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityBottomNavigationBarBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        replaceFragment(new SettingsFragment());

        // Hide a specific menu item
        Menu menu = binding.bottomNavigationBar.getMenu();
        MenuItem itemToHide = menu.findItem(R.id.currentView);
        itemToHide.setVisible(false);


        binding.bottomNavigationBar.setOnItemSelectedListener(item -> {
            Intent i;
        switch (item.getItemId()){
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

    private void replaceFragment(Fragment fragment){
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.frame_layout, fragment);
        fragmentTransaction.commit();
    }

}