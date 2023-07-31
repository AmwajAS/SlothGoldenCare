package com.example.slothgoldencare;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.widget.Button;

import com.example.slothgoldencare.Model.WorkAndPayment;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;

import java.util.ArrayList;
import java.util.List;

public class ManageDoctorActivity extends AppCompatActivity{
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
        loadWorkAndPaymentData();
    }

    private void loadWorkAndPaymentData() {
        FirebaseFirestore firestore = FirebaseFirestore.getInstance();

        CollectionReference workAndPaymentRef = firestore.collection("WorkAndPayment");
        workAndPaymentRef.get()
                .addOnSuccessListener(querySnapshot -> {
                    workAndPaymentList.clear();
                    for (QueryDocumentSnapshot document : querySnapshot) {
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

    public void onWorkAndPaymentClick(int position) {
        // Get the clicked item from the workAndPaymentList based on the position
        WorkAndPayment clickedItem = workAndPaymentList.get(position);
        updateIsPaidStatusInFirestore(clickedItem);
        // After handling the click event, you may want to refresh the RecyclerView to reflect the changes
        adapter.notifyDataSetChanged();
    }
    private void updateIsPaidStatusInFirestore(WorkAndPayment workAndPayment) {
        // Get the Firestore instance
        FirebaseFirestore firestore = FirebaseFirestore.getInstance();

        // Reference to the "WorkAndPayment" collection
        CollectionReference workAndPaymentRef = firestore.collection("WorkAndPayment");

        // Get the document ID of the clicked item (assuming you have a method to retrieve it)
        String documentId = workAndPayment.getDoctorId();

        // Get a reference to the specific document in the collection
        DocumentReference documentReference = workAndPaymentRef.document(documentId);

        // Update the "isPaid" field to true
        documentReference.update("isPaid", true)
                .addOnSuccessListener(aVoid -> {
                    // Update successful
                    // You can display a success message or perform any other action here
                })
                .addOnFailureListener(e -> {
                    // Update failed
                    // You can display an error message or handle the failure in any other way
                });
    }
    }
