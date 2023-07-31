package com.example.slothgoldencare;

import static android.content.ContentValues.TAG;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;

import com.example.slothgoldencare.Model.Appointment;
import com.example.slothgoldencare.Model.Doctor;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;

import java.util.ArrayList;
import java.util.List;

public class AppointmentsPatientsListActivity extends AppCompatActivity {

    private RecyclerView recyclerView;
    private AppointmentsAdapter adapter;
    private List<Appointment> appointmentsList;
    private FirebaseAuth auth;
    private String connectedDoctorId;
    private String connectedDoctorName;
    private String connectedDoctorSpecialization;
    private DataBaseHelper db;
    private Button backBtn;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_appointments_patients_list);
        auth = FirebaseAuth.getInstance();
        recyclerView = findViewById(R.id.appointmentsRecyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        db = new DataBaseHelper(this);
        appointmentsList = new ArrayList<>();
        adapter = new AppointmentsAdapter(this, appointmentsList);
        recyclerView.setAdapter(adapter);


        // back btn to remove the replaced view to the main one.
        backBtn = findViewById(R.id.back_btn);
        backBtn.setOnClickListener(view1 -> {
            Intent intent = new Intent(AppointmentsPatientsListActivity.this, DoctorActivityMain.class);
            startActivity(intent);
        });

        // Load the appointments specific to the connected doctor
        loadAppointmentsData();
    }
    private void loadAppointmentsData() {
        String doctorDocId = auth.getUid();
        Log.w(TAG,"this is the doctor document id: "+doctorDocId);
        Doctor doctor = db.getDoctorByDocumentId(doctorDocId);
        Log.w(TAG,"this is the doctor id: "+doctor.getID());
        String doctorId = doctor.getID();
        FirebaseFirestore firestore = FirebaseFirestore.getInstance();
        // Query the "Doctor" collection to get the doctor's data based on the "id" field
        firestore.collection("Doctors").whereEqualTo("id", doctorId).get()
                .addOnSuccessListener(querySnapshot -> {
                    if (!querySnapshot.isEmpty()) {
                        // There should be only one document with the specified doctorId
                        for (QueryDocumentSnapshot documentSnapshot : querySnapshot) {
                            String doctorName = documentSnapshot.getString("username");
                            String specialization = documentSnapshot.getString("specialization");
                            String doctorFullNameAndSpecialization = doctorName + " - " + specialization;
//                            Log.d(TAG, "Doctor full name and specialization: " + doctorFullNameAndSpecialization);

                            // If the user exists, query the "Appointment" collection to get their appointments
                            CollectionReference appointmentsRef = firestore.collection("Appointment");
                            appointmentsRef.whereEqualTo("doctor", doctorFullNameAndSpecialization).get()
                                    .addOnSuccessListener(querySnapshot1 -> {
                                        appointmentsList.clear();
                                        for (QueryDocumentSnapshot document : querySnapshot1) {
                                            // Replace "Appointment" with your Appointment class model
                                            Appointment appointment = document.toObject(Appointment.class);
                                            appointmentsList.add(appointment);
                                        }
                                        adapter.notifyDataSetChanged();
                                    })
                                    .addOnFailureListener(e -> {
                                        // Handle the failure to fetch appointments data from the database
                                        // You can display an error message or handle it in any other way
                                        Log.d(TAG, "Error loading appointments data: " + e.getMessage());
                                    });
                        }
                    } else {
                        Log.d(TAG, "Doctor not found with ID: " + doctorId);
                    }
                })
                .addOnFailureListener(e -> {
                    Log.d(TAG, "Error loading doctor data: " + e.getMessage());
                });
    }
}
