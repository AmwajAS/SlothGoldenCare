package com.example.slothgoldencare;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TimePicker;
import androidx.appcompat.app.AppCompatActivity;
public class TimeActivity extends AppCompatActivity {
    private String selectedDate; // To store the selected date from the previous activity

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_time);

        // Get the selected date from the previous activity's Intent
        selectedDate = getIntent().getStringExtra("selectedDate");

        TimePicker timePicker = findViewById(R.id.timePicker);
        Button nextTimeButton = findViewById(R.id.nextButton2);

        nextTimeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Get the selected time from TimePicker
                int hour = timePicker.getHour();
                int minute = timePicker.getMinute();
                String selectedTime = hour + ":" + minute;

                // Start the next activity (DoctorsNotesActivity) and pass the selected date and time as extras
                Intent intent = new Intent(TimeActivity.this, DoctorsNotesActivity.class);
                intent.putExtra("selectedDate", selectedDate);
                intent.putExtra("selectedTime", selectedTime);
                startActivity(intent);
            }
        });
    }
}