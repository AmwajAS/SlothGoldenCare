package com.example.slothgoldencare;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;

import androidx.appcompat.app.AppCompatActivity;

    public class AppointmentSchedulingActivity extends AppCompatActivity {

        @Override
        protected void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            setContentView(R.layout.activity_appointment_scheduling);

            DatePicker datePicker = findViewById(R.id.datePicker);
            Button nextDateButton = findViewById(R.id.nextButton1);

            nextDateButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    // Get the selected date from DatePicker
                    int year = datePicker.getYear();
                    int month = datePicker.getMonth() + 1; // Month is 0-based
                    int day = datePicker.getDayOfMonth();

                    // Start the next activity (TimeActivity) and pass the selected date and time as a Timestamp extra
                    Intent intent = new Intent(AppointmentSchedulingActivity.this, TimeActivity.class);
                    intent.putExtra("selectedDate", year + "-" + month + "-" + day);
                    startActivity(intent);
                }
            });
        }
    }
