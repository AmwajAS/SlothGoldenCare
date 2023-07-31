package com.example.slothgoldencare;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TimePicker;
import androidx.appcompat.app.AppCompatActivity;
import android.util.Log;

import com.example.slothgoldencare.Doctor.DoctorsNotesActivity;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;
import java.util.Calendar;

public class TimeActivity extends AppCompatActivity {
    private String selectedDate; // To store the selected date from the previous activity
    private FirebaseFirestore db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_time);

        // Get the selected date as a String from the previous activity's Intent
        String selectedDateStr = getIntent().getStringExtra("selectedDate");

        // Parse the selected date String into year, month, and day components
        String[] dateComponents = selectedDateStr.split("-");
        int year = Integer.parseInt(dateComponents[0]);
        int month = Integer.parseInt(dateComponents[1]);
        int day = Integer.parseInt(dateComponents[2]);

        TimePicker timePicker = findViewById(R.id.timePicker);
        Button nextTimeButton = findViewById(R.id.nextButton2);

        nextTimeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Get the selected time from TimePicker
                int hour = timePicker.getHour();
                int minute = timePicker.getMinute();

                // Create a Calendar instance to combine the selected date and time
                Calendar calendar = Calendar.getInstance();
                calendar.set(Calendar.YEAR, year);
                calendar.set(Calendar.MONTH, month - 1); // Month is 0-based
                calendar.set(Calendar.DAY_OF_MONTH, day);
                calendar.set(Calendar.HOUR_OF_DAY, hour);
                calendar.set(Calendar.MINUTE, minute);

                // Convert the Calendar instance to a Timestamp object
//                Timestamp selectedDateTime = new Timestamp(calendar.getTime());

                // Fetch the list of doctors from Firebase Firestore
                db = FirebaseFirestore.getInstance();
                db.collection("Doctors").get()
                        .addOnSuccessListener(querySnapshot -> {
                            ArrayList<String> doctorsList = new ArrayList<>();
                            for (DocumentSnapshot document : querySnapshot.getDocuments()) {
                                String doctorName = document.getString("username");
                                String doctorSpecialization = document.getString("specialization");
                                String doctorInfo = doctorName + " - " + doctorSpecialization;
                                doctorsList.add(doctorInfo);
                            }
                            // Convert the selected time to milliseconds since Epoch (Unix time)
                            long selectedTimeMillis = calendar.getTimeInMillis();

                            // Start the next activity (DoctorsNotesActivity) and pass the selected date and time as extras
                            Intent intent = new Intent(TimeActivity.this, DoctorsNotesActivity.class);
                            intent.putExtra("selectedTimeMillis", selectedTimeMillis);
                            intent.putStringArrayListExtra("doctorsList", doctorsList);
                            startActivity(intent);
                        })
                        .addOnFailureListener(e -> {
                            // Handle the failure to fetch doctors data from the database
                            Log.e("TimeActivity", "Error fetching doctors data: " + e.getMessage());
                        });
            }
        });
    }
}