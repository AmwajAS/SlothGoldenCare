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
import com.example.slothgoldencare.Model.Doctor;
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
    private DataBaseHelper dataBaseHelper;
    private FirebaseAuth auth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_doctor_report);

        recyclerView = findViewById(R.id.workAndPaymentRecyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        workAndPaymentList = new ArrayList<>();
        adapter = new WorkAndPaymentAdapter(this, workAndPaymentList);
        recyclerView.setAdapter(adapter);

        dataBaseHelper = new DataBaseHelper(this);
        auth = FirebaseAuth.getInstance();
        viewConfirmationButton = findViewById(R.id.viewConfirmationButton);
        viewConfirmationButton.setOnClickListener(v -> showConfirmationData());
        loadWorkAndPaymentData();
    }

    private void loadWorkAndPaymentData() {
        Doctor doctor = dataBaseHelper.getDoctorByDocumentId(auth.getUid());
        String doctorId = doctor.getID();
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

                        if(workAndPayment.getIsPaid().equals("Yes")){
                            viewConfirmationButton.setVisibility(View.VISIBLE);
                        }
                    }
                    adapter.notifyDataSetChanged();
                })
                .addOnFailureListener(e -> {
                 Log.d(TAG, "Cant load the documents!");
                });
    }
    private void showConfirmationData() {
        List<WorkAndPayment> confirmedPayments = new ArrayList<>();

        for (WorkAndPayment workAndPayment : workAndPaymentList) {
            if (workAndPayment.getIsPaid().equals("Yes")) {
                confirmedPayments.add(workAndPayment);
            }
        }

        if (confirmedPayments.isEmpty()) {
            Toast.makeText(this, "No confirmed payments found.", Toast.LENGTH_SHORT).show();
        } else {
            // Show the confirmation dialog
            ConfirmationDialogFragment dialogFragment = new ConfirmationDialogFragment(confirmedPayments);
            dialogFragment.show(getSupportFragmentManager(), "ConfirmationDialog");
        }
    }

}

