package com.example.slothgoldencare;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.slothgoldencare.Model.Appointment;
import com.google.firebase.Timestamp;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;
import java.util.Date;

public class DoctorsNotesActivity extends AppCompatActivity {

    private FirebaseFirestore db;
    private Spinner doctorsSpinner;
    private EditText notesEditText;
    private EditText idText;
    private Button scheduleButton;
    private Timestamp selectedDateTime;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_doctors_notes);

        long selectedTimeMillis = getIntent().getLongExtra("selectedTimeMillis", 0);
        selectedDateTime = new Timestamp(new Date(selectedTimeMillis));

        // Get the list of doctors from the previous activity's Intent
        ArrayList<String> doctorsList = getIntent().getStringArrayListExtra("doctorsList");
        setupDoctorsSpinner(doctorsList);

        notesEditText = findViewById(R.id.notesEditText);
        idText = findViewById(R.id.textViewId);
        scheduleButton = findViewById(R.id.scheduleButton);

        scheduleButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String selectedDoctor = doctorsSpinner.getSelectedItem().toString();
                String elderId= idText.getText().toString();
                String notes = notesEditText.getText().toString();
                saveAppointmentData(selectedDateTime, elderId, notes, selectedDoctor);
            }
        });
    }

    private void setupDoctorsSpinner(ArrayList<String> doctorsList) {
        doctorsSpinner = findViewById(R.id.doctorsSpinner);
        // Create an ArrayAdapter to populate the Spinner with the list of doctors
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, doctorsList);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        doctorsSpinner.setAdapter(adapter);
    }

    private void saveAppointmentData(Timestamp dateTime, String elder,  String notes, String doctor) {
        db = FirebaseFirestore.getInstance();

        // Create a new appointment object
        Appointment appointment = new Appointment(selectedDateTime, elder, notes, doctor);

        // Add the appointment to the "appointments" collection in Firestore
        db.collection("Appointment")
                .add(appointment)
                .addOnSuccessListener(documentReference -> {
                    // The appointment was successfully added to Firestore
                    // You can perform any further actions or show a success message here
                    Toast.makeText(this, "Appointment scheduled successfully!", Toast.LENGTH_SHORT).show();
//                    showAppointmentConfirmation();
                    finish();
                })
                .addOnFailureListener(e -> {
                    // Failed to add the appointment to Firestore
                    Toast.makeText(this, "Failed to schedule appointment. Please try again later.", Toast.LENGTH_SHORT).show();
                    Log.e("DoctorsNotesActivity", "Error saving appointment data: " + e.getMessage());
                    // You can show an error message here or handle the failure in any other way
                });
    }
}
