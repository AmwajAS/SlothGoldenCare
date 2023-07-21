package com.example.slothgoldencare.Reminder;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.slothgoldencare.DataBaseHelper;
import com.example.slothgoldencare.HomePageActivity;
import com.example.slothgoldencare.ProfileActivity;
import com.example.slothgoldencare.R;
import com.example.slothgoldencare.SettingsActivity;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.auth.FirebaseAuth;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.ArrayList;

public class TODOActivity extends AppCompatActivity {

    FloatingActionButton mCreateRem;
    RecyclerView mRecyclerview;
    List<Reminder> dataholder = new ArrayList<Reminder>();                                               //Array list to add reminders and display in recyclerview
    MyAdapter adapter;
    private TextView txtCurrentDate;
    private DataBaseHelper dataBaseHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_todoactivity);
        dataBaseHelper = new DataBaseHelper(this);
        dataholder = dataBaseHelper.getReminderByElderlyDocId(FirebaseAuth.getInstance().getUid());

        txtCurrentDate = findViewById(R.id.txtCurrentDate);

        // Set the current date to the TextView
        SimpleDateFormat dateFormat = new SimpleDateFormat("MMM dd, yyyy", Locale.getDefault());
        String currentDate = dateFormat.format(new Date());
        txtCurrentDate.setText(currentDate);

        mRecyclerview = findViewById(R.id.recyclerView);
        mRecyclerview.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
        mCreateRem = findViewById(R.id.create_reminder);                     //Floating action button to change activity
        mCreateRem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), ReminderActivity.class);
                startActivity(intent);                                                              //Starts the new activity to add Reminders
            }
        });

        adapter = new MyAdapter(dataholder);
        mRecyclerview.setAdapter(adapter);//Binds the adapter with recyclerview

        bottomNavigationView();
    }


    //Makes the user exit from the app
    @Override
    public void onBackPressed() {
        finish();
        super.onBackPressed();
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
