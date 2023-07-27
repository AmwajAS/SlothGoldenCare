package com.example.slothgoldencare;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import androidx.appcompat.app.AppCompatActivity;

public class DoctorsNotesActivity extends AppCompatActivity {

    private String selectedDate;
    private String selectedTime; // To store the selected date and time from the previous activity

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_doctors_notes);

        // Get the selected date and time from the previous activity's Intent
        selectedDate = getIntent().getStringExtra("selectedDate");
        selectedTime = getIntent().getStringExtra("selectedTime");

        // Get references to your Spinner and EditText views for doctors and notes selection
        Spinner doctorsSpinner = findViewById(R.id.doctorsSpinner);
        EditText notesEditText = findViewById(R.id.notesEditText);
        Button scheduleButton = findViewById(R.id.scheduleButton);

        scheduleButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Handle the appointment scheduling logic here
                // Retrieve the selected date, time, doctor, and notes and proceed with scheduling
                String selectedDoctor = doctorsSpinner.getSelectedItem().toString();
                String notes = notesEditText.getText().toString();

                // Save the appointment data to Firebase or any other storage solution
                saveAppointmentData(selectedDate, selectedTime, selectedDoctor, notes);

                // After scheduling, you can show a confirmation or perform any other actions
                showAppointmentConfirmation();
            }
        });
    }

    private void saveAppointmentData(String date, String time, String doctor, String notes) {
        // Implement the code to save the appointment data to Firebase
        // Use Firebase Realtime Database or Firestore to store the data
        // Example:
        // DatabaseReference appointmentRef = FirebaseDatabase.getInstance().getReference("appointments");
        // String appointmentId = appointmentRef.push().getKey();
        // Appointment appointment = new Appointment(appointmentId, date, time, doctor, notes);
        // appointmentRef.child(appointmentId).setValue(appointment);
    }

    private void showAppointmentConfirmation() {
        // Implement logic to show a confirmation dialog or perform any other actions after scheduling
        // You can use Toast, AlertDialog, or any other UI element to display the confirmation message.
    }
}
