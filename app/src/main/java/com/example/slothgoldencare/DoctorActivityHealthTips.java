package com.example.slothgoldencare;

import android.app.AlertDialog;
import android.content.Intent;
import android.nfc.Tag;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.*;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import com.example.slothgoldencare.Model.Diagnosis;
import com.example.slothgoldencare.Model.Doctor;
import com.example.slothgoldencare.Model.HealthTip;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import org.checkerframework.checker.units.qual.A;

import java.util.ArrayList;
import java.util.List;

public class DoctorActivityHealthTips extends AppCompatActivity{
    private static final String TAG = "ADMIN";

    private Button addBtn;
    private Button backBtn;
    private ListView healthTipsList;
    private List<HealthTip> healthTips;
    private FirebaseFirestore db;
    private  ArrayAdapter<HealthTip> healthTipAdapter;
    DataBaseHelper dbHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_doctor_health_tips);
        dbHelper = new DataBaseHelper(this);
        healthTipsList = findViewById(R.id.health_tips_list);
        healthTips = new ArrayList<>();
        healthTips = dbHelper.getHealthTips();
        db = FirebaseFirestore.getInstance();

        backBtn = findViewById(R.id.back_btn);
        backBtn.setOnClickListener(v -> {
            finish();
        });

        addBtn = findViewById(R.id.add_to_list);
        addBtn.setOnClickListener(v -> {
            showAddHealthTipDialog();
        });
        healthTipAdapter = new ArrayAdapter<HealthTip>(this.getApplicationContext(),R.layout.health_status_item_layout_doctor,healthTips){
            public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {

                // Get the user object for the current position
                HealthTip healthTip = getItem(position);
                // Inflate the list item layout
                if (convertView == null) {
                    convertView = LayoutInflater.from(getContext()).inflate(R.layout.health_status_item_layout_doctor, parent, false);
                }

                TextView idText = convertView.findViewById(R.id.health_status_item);
                idText.setText(healthTip.getTitle());
                ImageButton deletBtn = convertView.findViewById(R.id.delete_btn);
                deletBtn.setOnClickListener(view->{
                    db.collection("HealthTips")
                            .whereEqualTo("title",healthTip.getTitle())
                            .whereEqualTo("content",healthTip.getContent())
                            .get().addOnCompleteListener(task -> {
                                if(task.isSuccessful()){
                                    for (QueryDocumentSnapshot document : task.getResult()) {
                                        // Delete the matching document from Firestore
                                        db.collection("HealthTips").document(document.getId()).delete();
                                        if(dbHelper.deleteHealthTip(healthTip)){
                                            healthTips.remove(position);
                                            Toast.makeText(getContext(), R.string.info_delete_success, Toast.LENGTH_LONG).show();
                                            healthTipAdapter.notifyDataSetChanged();
                                        }else{
                                            Toast.makeText(getContext(), R.string.info_delete_fail, Toast.LENGTH_LONG).show();
                                        }
                                    }
                                } else {
                                    Toast.makeText(getContext(), task.getException().getMessage().toString(), Toast.LENGTH_LONG).show();
                                }
                            });
                });

                convertView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        // Show the health tip content when clicked
                        HealthTip healthTip = getItem(position);
                        if (healthTip != null) {
                            showHealthTipContent(healthTip);
                        }
                    }
                });

                return convertView;
            }
        };
        healthTipsList.setAdapter(healthTipAdapter);

    }

    private void showHealthTipContent(HealthTip healthTip) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle(healthTip.getTitle());
        builder.setMessage(healthTip.getContent());
        builder.setPositiveButton("OK", null);
        AlertDialog dialog = builder.create();
        dialog.show();
    }

    private void showAddHealthTipDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        LayoutInflater inflater = getLayoutInflater();
        View dialogView = inflater.inflate(R.layout.dialog_add_health_tip, null);
        builder.setView(dialogView);

        EditText titleEditText = dialogView.findViewById(R.id.editTextTitle);
        EditText contentEditText = dialogView.findViewById(R.id.editTextContent);

        builder.setPositiveButton("Add", (dialog, which) -> {
            String title = titleEditText.getText().toString().trim();
            String content = contentEditText.getText().toString().trim();

            // Check if the title and content are not empty
            if (!title.isEmpty() && !content.isEmpty()) {
                // Create a new HealthTip object with the input values
                HealthTip newHealthTip = new HealthTip(title, content);
                db.collection("HealthTips").add(newHealthTip).addOnCompleteListener(task -> {
                    if(task.isSuccessful()){
                        healthTips.add(newHealthTip);
                        healthTipAdapter.notifyDataSetChanged();
                        if(dbHelper.addHealthTip(newHealthTip)){
                            Toast.makeText(this, R.string.info_add_success, Toast.LENGTH_SHORT).show();
                        }else{
                            Toast.makeText(this, R.string.info_add_fail, Toast.LENGTH_SHORT).show();
                        }
                    }else{
                        Toast.makeText(this, R.string.info_add_fail, Toast.LENGTH_SHORT).show();
                    }
                });
            } else {
                Toast.makeText(this, "Please enter both title and content", Toast.LENGTH_SHORT).show();
            }
        });
        builder.setNegativeButton("Cancel", null);
        AlertDialog dialog = builder.create();
        dialog.show();
    }



}


