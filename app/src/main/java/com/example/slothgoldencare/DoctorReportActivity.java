package com.example.slothgoldencare;

import static android.content.ContentValues.TAG;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;
import com.example.slothgoldencare.Model.WorkAndPayment;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import java.util.ArrayList;
import java.util.List;

public class DoctorReportActivity extends AppCompatActivity {
    private List<WorkAndPayment> workAndPaymentList;
    private RecyclerView recyclerView;
    private WorkAndPaymentAdapter adapter;
    private Button viewConfirmationButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_doctor_report);

        recyclerView = findViewById(R.id.workAndPaymentRecyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        workAndPaymentList = new ArrayList<>();
        adapter = new WorkAndPaymentAdapter(this, workAndPaymentList);
        recyclerView.setAdapter(adapter);

        viewConfirmationButton = findViewById(R.id.viewConfirmationButton);
        viewConfirmationButton.setOnClickListener(v -> showConfirmationData());

        loadWorkAndPaymentData();
    }

    private void loadWorkAndPaymentData() {
        String doctorId = getIntent().getStringExtra("doctorUid");
        FirebaseFirestore firestore = FirebaseFirestore.getInstance();

        // Query the "WorkAndPayment" collection to get the work and payment data for the connected doctor
        CollectionReference workAndPaymentRef = firestore.collection("WorkAndPayment");
        workAndPaymentRef.whereEqualTo("doctorId", doctorId).get()
                .addOnSuccessListener(querySnapshot -> {
                    workAndPaymentList.clear();
                    for (QueryDocumentSnapshot document : querySnapshot) {
                        // Replace "WorkAndPayment" with your WorkAndPayment class model
                        WorkAndPayment workAndPayment = document.toObject(WorkAndPayment.class);
                        workAndPaymentList.add(workAndPayment);
                    }
                    adapter.notifyDataSetChanged();
                })
                .addOnFailureListener(e -> {
                    // Handle the failure to fetch work and payment data from the database
                    // You can display an error message or handle it in any other way
                });
    }

    private void showConfirmationData() {
        List<WorkAndPayment> confirmedPayments = new ArrayList<>();

        for (WorkAndPayment workAndPayment : workAndPaymentList) {
//            Log.d(TAG, "check ispaid" +workAndPayment.isPaid() );
            if (workAndPayment.isPaid()) {
                confirmedPayments.add(workAndPayment);
            }
        }

        if (confirmedPayments.isEmpty()) {
            Toast.makeText(this, "No confirmed payments found.", Toast.LENGTH_SHORT).show();
        } else {
            // Show the confirmed payments in a new activity or dialog
            // For simplicity, we'll just display a Toast message with the details
            StringBuilder message = new StringBuilder();
            for (WorkAndPayment payment : confirmedPayments) {
                message.append("Date: ").append(payment.getDateDay().toDate().toString()).append("\n");
                message.append("Doctor ID: ").append(payment.getDoctorId()).append("\n");
                message.append("Hours: ").append(payment.getHours()).append("\n");
                message.append("Paid Date: ").append(payment.getPaidDate()).append("\n\n");
            }
            Toast.makeText(this, message.toString(), Toast.LENGTH_LONG).show();
        }
    }
}

