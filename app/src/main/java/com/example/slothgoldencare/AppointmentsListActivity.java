package com.example.slothgoldencare;

import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.slothgoldencare.Model.Appointment;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import java.util.ArrayList;
import java.util.List;

public class AppointmentsListActivity extends AppCompatActivity {

    private RecyclerView recyclerView;
    private AppointmentsAdapter adapter;
    private List<Appointment> appointmentsList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_appointments_list);

        recyclerView = findViewById(R.id.appointmentsRecyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        appointmentsList = new ArrayList<>();
        adapter = new AppointmentsAdapter(this, appointmentsList);
        recyclerView.setAdapter(adapter);

        loadAppointmentsData();
    }

    private void loadAppointmentsData() {
        CollectionReference appointmentsRef = FirebaseFirestore.getInstance().collection("Appointment");
        appointmentsRef.get()
                .addOnSuccessListener(querySnapshot -> {
                    appointmentsList.clear();
                    for (QueryDocumentSnapshot document : querySnapshot) {
                        // Replace "Appointment" with your Appointment class model
                        Appointment appointment = document.toObject(Appointment.class);
                        appointmentsList.add(appointment);
                    }
                    adapter.notifyDataSetChanged();
                })
                .addOnFailureListener(e -> {
                    // Handle the failure to fetch appointments data from the database
                    // You can display an error message or handle it in any other way
                });
    }
}
