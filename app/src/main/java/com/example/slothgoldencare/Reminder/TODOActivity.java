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
import com.example.slothgoldencare.Model.Elder;
import com.example.slothgoldencare.Model.User;
import com.example.slothgoldencare.ProfileActivity;
import com.example.slothgoldencare.R;
import com.example.slothgoldencare.SettingsActivity;
import com.example.slothgoldencare.UserHomePageActivity;
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
    private FirebaseAuth auth;
    private String elderlyId;
    private Elder elder;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_todoactivity);
        dataBaseHelper = new DataBaseHelper(this);
        elderlyId = getIntent().getStringExtra("elderlyId");
        elder = dataBaseHelper.getElderById(elderlyId);
        auth = FirebaseAuth.getInstance();


        txtCurrentDate = findViewById(R.id.txtCurrentDate);
        // Set the current date to the TextView
        SimpleDateFormat dateFormat = new SimpleDateFormat("MMM dd, yyyy", Locale.getDefault());
        String currentDate = dateFormat.format(new Date());
        txtCurrentDate.setText(currentDate);

        mRecyclerview = findViewById(R.id.recyclerView);
        mRecyclerview.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
        mCreateRem = findViewById(R.id.create_reminder);                     //Floating action button to change activity



        if(dataBaseHelper.getElderByDocumentId(auth.getUid()) != null){
            dataholder = dataBaseHelper.getReminderByElderlyDocId(FirebaseAuth.getInstance().getUid());
        }else if(dataBaseHelper.getUserByDocumentId(auth.getUid()) != null){
            dataholder = dataBaseHelper.getReminderByElderlyDocId(elder.getDocId());
            mCreateRem.setVisibility(View.GONE);
        }

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
        DataBaseHelper dbHelper = new DataBaseHelper(getApplicationContext());
        BottomNavigationView bottomNavigationView = findViewById(R.id.bottomNavigation);
        bottomNavigationView.setSelectedItemId(R.id.settings);
        Menu menu = bottomNavigationView.getMenu();
        MenuItem currentItem = menu.findItem(R.id.current);

        // Hiding the "current" menu item
        currentItem.setVisible(false);

        // Get the currently logged-in user (either elder or relative) from the database
        User currentUser = dbHelper.getUserByDocumentId(auth.getUid());
        Elder currentElder = dbHelper.getElderByDocumentId(auth.getUid());

        if (currentUser != null && currentElder == null) {
            // If the logged-in user is a relative, hide the "profile" menu item
            MenuItem profileItem = menu.findItem(R.id.profile);
            profileItem.setVisible(false);
        }

        bottomNavigationView.setOnItemSelectedListener(item -> {
            switch (item.getItemId()) {
                case R.id.home:
                    if (currentElder != null) {
                        replaceView(HomePageActivity.class);
                    } else if (currentUser != null) {
                        replaceView(UserHomePageActivity.class);
                    }
                    return true;
                case R.id.settings:
                    replaceView(SettingsActivity.class);
                    return true;
                case R.id.profile:
                    if (currentElder != null) {
                        replaceView(ProfileActivity.class);
                    } else if (currentUser != null) {
                        replaceView(UserHomePageActivity.class);
                    }
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
