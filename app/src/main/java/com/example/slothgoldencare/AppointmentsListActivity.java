package com.example.slothgoldencare;

import static android.content.ContentValues.TAG;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.slothgoldencare.Model.Appointment;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.auth.FirebaseAuth;
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
        bottomNavigationView();
        loadAppointmentsData();
    }

    private void loadAppointmentsData() {
        String userId = FirebaseAuth.getInstance().getCurrentUser().getUid();
        FirebaseFirestore firestore = FirebaseFirestore.getInstance();

        // Query the "Elderlies" collection to get the user's ID
        firestore.collection("Elderlies").document(userId).get()
                .addOnSuccessListener(documentSnapshot -> {
                    if (documentSnapshot.exists()) {
                        String userID = documentSnapshot.getString("id");

                        // If the user exists, query the "Appointment" collection to get their appointments
                        CollectionReference appointmentsRef = firestore.collection("Appointment");
                        appointmentsRef.whereEqualTo("elderId", userID).get()
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
                    } else {
                        Log.d(TAG, "User not found with ID: " + userId);
                    }
                })
                .addOnFailureListener(e -> {
                    Log.d(TAG, "Error loading user data: " + e.getMessage());
                });
    }

    public void bottomNavigationView() {
        BottomNavigationView bottomNavigationView = findViewById(R.id.bottomNavigation);
        bottomNavigationView.setSelectedItemId(R.id.settings);
        Menu menu = bottomNavigationView.getMenu();
        MenuItem currentItem = menu.findItem(R.id.current);
        // Hiding the "current" menu item
        currentItem.setVisible(false);
        bottomNavigationView.setOnItemSelectedListener(item -> {
            switch (item.getItemId()) {
                case R.id.home:
                    replaceView(HomePageActivity.class);
                    return true;
                case R.id.settings:
                    replaceView(SettingsActivity.class);
                    return true;
                case R.id.profile:
                    replaceView(ProfileActivity.class);
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
