package com.example.slothgoldencare;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.slothgoldencare.Model.Doctor;
import com.example.slothgoldencare.Model.Elder;
import com.example.slothgoldencare.Model.User;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;
import java.util.List;

public class AdministratorDoctorsActivity extends AppCompatActivity {

    private static final String TAG = "ADMIN";

    private ListView doctorsList;
    private List<Doctor> doctors;
    private Button addDrBtn;
    private ArrayAdapter<Doctor> doctorAdapter;
    DataBaseHelper dbHelper;
    FirebaseFirestore db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_administrator_doctors);


        dbHelper = new DataBaseHelper(this);
        doctorsList = findViewById(R.id.doctors_list);
        doctors = new ArrayList<>();
        doctors.addAll(dbHelper.getDoctors());
        addDrBtn = findViewById(R.id.buttonAdd);

        // Create the doctorAdapter and set it to the ListView
        doctorAdapter = new ArrayAdapter<Doctor>(this, R.layout.administrator_user_item, doctors);
        doctorsList.setAdapter(doctorAdapter);

        updateDoctorsList();


        doctorAdapter = new ArrayAdapter<Doctor>(this, R.layout.administrator_user_item, doctors) {
            @NonNull
            @Override
            public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {


                // Get the user object for the current position
                Doctor doctor = getItem(position);

                // Inflate the list item layout
                if (convertView == null) {
                    convertView = LayoutInflater.from(getContext()).inflate(R.layout.administrator_user_item, parent, false);
                }
                // Set the username in the TextView
                TextView userNameTextView = convertView.findViewById(R.id.user_name);
                userNameTextView.setText(doctor.getUsername());
                ImageButton deleteButton = convertView.findViewById(R.id.delete_user_btn);
                ImageButton editButton = convertView.findViewById(R.id.edit_user_btn);

                deleteButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        doctors.remove(doctor);
                        deleteDoctor(doctor);
                        notifyDataSetChanged();
                    }
                });
                editButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        ElderlyItemDialog elderlyItemDialog = new ElderlyItemDialog(getContext(), new ElderlyItemDialog.OnSaveChangesListener() {
                            @Override
                            public void onSaveChanges(Doctor doctor) {
                                if(doctor != null){
                                    db = FirebaseFirestore.getInstance();
                                    db.collection("Doctors").document(doctor.getDocId()).update(
                                            "id",doctor.getID(),
                                            "username",doctor.getUsername(),
                                            "phoneNumber",doctor.getPhoneNumber(),
                                            "email",doctor.getEmail(),
                                            "password",doctor.getPassword(),
                                            "specialization", doctor.getSpecialization()
                                    ).addOnCompleteListener(task -> {
                                        if(task.isSuccessful()){
                                            if(dbHelper.updateDoctorsInfo(doctor)){
                                                recreate();
                                                Toast.makeText(getApplicationContext(),R.string.info_updated_success,Toast.LENGTH_LONG).show();
                                            }else{
                                                Toast.makeText(getApplicationContext(),R.string.info_updated_failed,Toast.LENGTH_LONG).show();
                                            }
                                        }
                                    });
                                }
                            }

                            @Override
                            public void onSaveChanges(Elder elder) {

                            }

                            @Override
                            public void onSaveChanges(User user) {

                            }

                        }, ElderlyItemDialog.ItemType.DOCTOR);

                        elderlyItemDialog.show();
                        elderlyItemDialog.setEditTextValues(null,null,doctor);
                    }
                });

                return convertView;
            }
        };
        doctorsList.setAdapter(doctorAdapter);
        addDrBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                ElderlyItemDialog elderlyItemDialog = new ElderlyItemDialog(AdministratorDoctorsActivity.this, new ElderlyItemDialog.OnSaveChangesListener() {
                    @Override
                    public void onSaveChanges(Doctor doctor) {
                        //CHECK THIS SECTION!
                        if(doctor != null){
                            db = FirebaseFirestore.getInstance();
                            db.collection("Doctors").add(doctor).addOnCompleteListener(task -> {
                                if(task.isSuccessful()){
                                    if(dbHelper.addDoctorData(doctor)){
                                        Toast.makeText(getApplicationContext(),R.string.info_add_success,Toast.LENGTH_LONG).show();
                                    }else{
                                        Toast.makeText(getApplicationContext(),R.string.info_add_fail,Toast.LENGTH_LONG).show();
                                    }
                                }
                            });
                        }
                    }

                    @Override
                    public void onSaveChanges(Elder elder) {

                    }

                    @Override
                    public void onSaveChanges(User user) {

                    }
                }, ElderlyItemDialog.ItemType.DOCTOR);

                elderlyItemDialog.show();
                elderlyItemDialog.setEditTextValues(null,null,null );
            }
        });
    }

    private void updateDoctorsList() {
        // Fetch doctors data from the SQLite database and update the list
        doctors.clear();
        doctors.addAll(dbHelper.getDoctors());
        doctorAdapter.notifyDataSetChanged();
    }


    @Override
    public void onPointerCaptureChanged(boolean hasCapture) {
        super.onPointerCaptureChanged(hasCapture);
    }

    private void deleteDoctor(Doctor doctor) {
        db = FirebaseFirestore.getInstance();
        db.collection("Doctors").document(doctor.getDocId()).delete().addOnCompleteListener(task -> {
            if(task.isSuccessful()){
                if(dbHelper.deleteDoctorByDocId(doctor.getDocId())){
                    Toast.makeText(getApplicationContext(),R.string.info_delete_success,Toast.LENGTH_LONG).show();
                }else{
                    Toast.makeText(getApplicationContext(),R.string.info_delete_fail,Toast.LENGTH_LONG).show();
                }
            }else{
                Toast.makeText(getApplicationContext(),R.string.info_delete_fail+" || "+task.getException().getMessage().toString(),Toast.LENGTH_LONG).show();
            }

        });
    }
    }



