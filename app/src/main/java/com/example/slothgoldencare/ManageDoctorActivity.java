package com.example.slothgoldencare;

import static android.content.ContentValues.TAG;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.Toast;

import com.example.slothgoldencare.Model.WorkAndPayment;
import com.google.firebase.Timestamp;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.WriteBatch;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class ManageDoctorActivity extends AppCompatActivity{
    FirebaseFirestore firestore = FirebaseFirestore.getInstance();
    private List<WorkAndPayment> workAndPaymentList;
    private RecyclerView recyclerView;
    private WorkAndPaymentAdapter adapter;
    private Button payBtn;
    private Button backBtn;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_manage_doctor);

        recyclerView = findViewById(R.id.workAndPaymentRecyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        workAndPaymentList = new ArrayList<>();
        adapter = new WorkAndPaymentAdapter(this, workAndPaymentList);
        recyclerView.setAdapter(adapter);

        // back btn to remove the replaced view to the main one.
        backBtn = findViewById(R.id.back_btn);
        backBtn.setOnClickListener(view1 -> {
            Intent intent = new Intent(ManageDoctorActivity.this, AdministratorActivity.class);
            startActivity(intent);
        });

        payBtn=findViewById(R.id.payButton);
        payBtn.setOnClickListener(v -> updateAllPaymentsToPaid());
        loadWorkAndPaymentData();

    }

    private void loadWorkAndPaymentData() {
        CollectionReference workAndPaymentRef = firestore.collection("WorkAndPayment");
        workAndPaymentRef.get()
                .addOnSuccessListener(querySnapshot -> {
                    workAndPaymentList.clear();
                    for (QueryDocumentSnapshot document : querySnapshot) {
                        WorkAndPayment workAndPayment = document.toObject(WorkAndPayment.class);
                        if(workAndPayment.getIsPaid().equals("No")){
                            workAndPaymentList.add(workAndPayment);
                        }
                    }
                    adapter.notifyDataSetChanged();
                })
                .addOnFailureListener(e -> {
                    Log.d(TAG, "Cant load the documents!");
                });
    }
    private void updateAllPaymentsToPaid() {
        if(workAndPaymentList.isEmpty()){
            showAlert("Alert","There are no payments request!") ;
        }
        Date currentDate = new Date();
        Timestamp currentTimestamp = new Timestamp(currentDate);

        for (WorkAndPayment workAndPayment : workAndPaymentList) {
            if(workAndPayment.getIsPaid().equals("No")){
                workAndPayment.setIsPaid("Yes");
                workAndPayment.setPaidDate(currentTimestamp);
                updateInFireBase(currentTimestamp);
                showAlert("Success","Payment Successful!") ;
            }else{
                showAlert("Alert","There are no payments request!") ;
            }
        }
        adapter.notifyDataSetChanged();
    }
    private void showAlert(String title, String message) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle(title);
        builder.setMessage(message);
        builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                // The user clicked "OK," so dismiss the dialog
                dialog.dismiss();
            }
        });
        AlertDialog alert = builder.create();
        alert.show();
    }

    public void updateInFireBase(Timestamp updatedDate){
        CollectionReference workAndPaymentRef = firestore.collection("WorkAndPayment");
        // Create a query to get the documents where isPaid == "No"
        Query query = workAndPaymentRef.whereEqualTo("isPaid", "No");

        // Execute the query
        query.get()
                .addOnSuccessListener(querySnapshot -> {
                    // Create a batch write
                    WriteBatch batch = firestore.batch();
                    for (QueryDocumentSnapshot document : querySnapshot) {
                        // Get the WorkAndPayment object from the document
                        WorkAndPayment workAndPayment = document.toObject(WorkAndPayment.class);
                        // Update the isPaid and the paid date
                        workAndPayment.setIsPaid("Yes");
                        workAndPayment.setPaidDate(updatedDate);

                        DocumentReference docRef = document.getReference();
                        batch.set(docRef, workAndPayment);
                    }
                    // Commit the batch write
                    batch.commit()
                            .addOnSuccessListener(aVoid -> {
                                // Handle the successful update if needed
                                Toast.makeText(this, "All payments updated to Paid.", Toast.LENGTH_SHORT).show();
                            })
                            .addOnFailureListener(e -> {
                                // Handle the failure to update if needed
                                Log.d(TAG, "Batch write failed: " + e.getMessage());
                            });
                })
                .addOnFailureListener(e -> {
                    // Handle the failure to fetch documents if needed
                    Log.d(TAG, "Error fetching documents: " + e.getMessage());
                });
    }
}
