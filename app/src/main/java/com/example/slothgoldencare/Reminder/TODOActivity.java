package com.example.slothgoldencare.Reminder;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.ColorSpace;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.example.slothgoldencare.DataBaseHelper;
import com.example.slothgoldencare.R;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.ArrayList;

public class TODOActivity extends AppCompatActivity {


    FloatingActionButton mCreateRem;
    RecyclerView mRecyclerview;
    ArrayList<Model> dataholder = new ArrayList<Model>();                                               //Array list to add reminders and display in recyclerview
    MyAdapter adapter;
    private TextView txtCurrentDate;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_todoactivity);

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

        Cursor cursor = new DataBaseHelper(getApplicationContext()).readallreminders();
        dataholder.clear(); // Clear the dataholder list before adding new reminders

        while (cursor.moveToNext()) {
            Model model = new Model(cursor.getString(1), cursor.getString(2), cursor.getString(3));
            dataholder.add(model);
        }

        adapter = new MyAdapter(dataholder);
        mRecyclerview.setAdapter(adapter);                                                          //Binds the adapter with recyclerview

    }

    @Override
    public void onBackPressed() {
        finish();                                                                                   //Makes the user to exit from the app
        super.onBackPressed();

    }
}