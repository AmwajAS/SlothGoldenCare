package com.example.slothgoldencare.Reminder;

import android.util.Log;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.example.slothgoldencare.DataBaseHelper;
import com.example.slothgoldencare.R;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.ArrayList;

import static android.content.ContentValues.TAG;

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
        Log.w(TAG,"TESTING THE SIZE :"+dataholder.size());

        txtCurrentDate = findViewById(R.id.txtCurrentDate);

        // Set the current date to the TextView
        SimpleDateFormat dateFormat = new SimpleDateFormat("MMM dd, yyyy", Locale.getDefault());
        String currentDate = dateFormat.format(new Date());
        txtCurrentDate.setText(currentDate);

        mRecyclerview = (RecyclerView) findViewById(R.id.recyclerView);
        mRecyclerview.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
        mCreateRem = (FloatingActionButton) findViewById(R.id.create_reminder);                     //Floating action button to change activity
        mCreateRem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), ReminderActivity.class);
                startActivity(intent);                                                              //Starts the new activity to add Reminders
            }
        });

      //  Cursor cursor = new DataBaseHelper(getApplicationContext()).readallreminders();
      //  dataholder.clear(); // Clear the dataholder list before adding new reminders

//        while (cursor.moveToNext()) {
//            Reminder model = new Reminder(cursor.getString(1), cursor.getString(2), cursor.getString(3));
//            dataholder.add(model);
//        }

        adapter = new MyAdapter(dataholder);
        mRecyclerview.setAdapter(adapter);                                                          //Binds the adapter with recyclerview

    }

    @Override
    public void onBackPressed() {
        finish();                                                                                   //Makes the user to exit from the app
        super.onBackPressed();

    }
}