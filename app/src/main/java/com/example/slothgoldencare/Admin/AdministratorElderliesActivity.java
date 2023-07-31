package com.example.slothgoldencare.Admin;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.*;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.example.slothgoldencare.DataBaseHelper.DataBaseHelper;
import com.example.slothgoldencare.ElderlyItemDialog;
import com.example.slothgoldencare.Model.Doctor;
import com.example.slothgoldencare.Model.Elder;
import com.example.slothgoldencare.Model.User;
import com.example.slothgoldencare.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;
import java.util.List;

public class AdministratorElderliesActivity extends AppCompatActivity implements View.OnClickListener {
    private static final String TAG = "ADMIN";

    private ListView eldersList;
    private List<Elder> elders;
    private Button buttonAddElderly;
    private ArrayAdapter<Elder> elderAdapter;
    DataBaseHelper dbHelper;
    FirebaseFirestore db;
    private Button backBtn;
    private FirebaseAuth auth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_elderlies);
        dbHelper = new DataBaseHelper(this);
        eldersList = findViewById(R.id.elderly_list);
        elders = new ArrayList<>();
        elders = dbHelper.getElders();
        buttonAddElderly = findViewById(R.id.buttonAdd);
        auth = FirebaseAuth.getInstance();

        // back btn to remove the replaced view to the main one.
        backBtn = findViewById(R.id.back_btn);
        backBtn.setOnClickListener(view1 -> {
            Intent intent = new Intent(AdministratorElderliesActivity.this,AdministratorActivity.class);
            startActivity(intent);
        });


        elderAdapter = new ArrayAdapter<Elder>(this, R.layout.administrator_user_item, elders) {
            @NonNull
            @Override
            public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {


                // Get the user object for the current position
                Elder elder = getItem(position);

                // Inflate the list item layout
                if (convertView == null) {
                    convertView = LayoutInflater.from(getContext()).inflate(R.layout.administrator_user_item, parent, false);
                }
                // Set the username in the TextView
                TextView userNameTextView = convertView.findViewById(R.id.user_name);
                userNameTextView.setText(elder.getUsername());
                ImageButton deleteButton = convertView.findViewById(R.id.delete_user_btn);
                ImageButton editButton = convertView.findViewById(R.id.edit_user_btn);

                deleteButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
                        builder.setTitle("Confirm Deletion");
                        builder.setMessage("Are you sure you want to delete this entry?");
                        builder.setPositiveButton("Delete", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                elders.remove(elder);
                                deleteUser(elder);
                                notifyDataSetChanged();
                            }
                        });
                        builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.dismiss();
                            }
                        });

                        AlertDialog alertDialog = builder.create();
                        alertDialog.show();
                    }
                });

                editButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        ElderlyItemDialog elderlyItemDialog = new ElderlyItemDialog(getContext(), new ElderlyItemDialog.OnSaveChangesListener() {
                            @Override
                            public void onSaveChanges(Elder elder) {
                                if (elder != null) {
                                    AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
                                    builder.setTitle("Confirm Update");
                                    builder.setMessage("Are you sure you want to update this entry?");
                                    builder.setPositiveButton("Update", new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialog, int which) {
                                            db = FirebaseFirestore.getInstance();
                                            db.collection("Elderlies").document(elder.getDocId()).update(
                                                    "id", elder.getID(),
                                                    "username", elder.getUsername(),
                                                    "phoneNumber", elder.getPhoneNumber(),
                                                    "email", elder.getEmail(),
                                                    "password", elder.getPassword(),
                                                    "gender", elder.getGender().toString(),
                                                    "dob", elder.getDOB()
                                            ).addOnCompleteListener(task -> {
                                                if (task.isSuccessful()) {
                                                    if (dbHelper.updateElderlyInfo(elder)) {
                                                        recreate();
                                                        Toast.makeText(getApplicationContext(), R.string.info_updated_success, Toast.LENGTH_LONG).show();
                                                    } else {
                                                        Toast.makeText(getApplicationContext(), R.string.info_updated_failed, Toast.LENGTH_LONG).show();
                                                    }
                                                }
                                            });
                                        }
                                    });
                                    builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialog, int which) {
                                            dialog.dismiss();
                                        }
                                    });

                                    AlertDialog alertDialog = builder.create();
                                    alertDialog.show();
                                }
                            }


                            @Override
                            public void onSaveChanges(User user) {

                            }

                            @Override
                            public void onSaveChanges(Doctor doctor) {

                            }

                        }, ElderlyItemDialog.ItemType.ELDER);

                        elderlyItemDialog.show();
                        elderlyItemDialog.setEditTextValues(elder,null,null);
                    }
                });

                return convertView;
            }
        };
        eldersList.setAdapter(elderAdapter);
        buttonAddElderly.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                ElderlyItemDialog elderlyItemDialog = new ElderlyItemDialog(AdministratorElderliesActivity.this, new ElderlyItemDialog.OnSaveChangesListener() {
                    @Override
                    public void onSaveChanges(Elder elder) {
                        if(elder != null){
                            db = FirebaseFirestore.getInstance();
                            auth.createUserWithEmailAndPassword(elder.getEmail(),elder.getPassword()).addOnCompleteListener(task1 -> {
                                if(task1.isSuccessful()) {
                                    elder.setDocId(auth.getUid());
                                    db.collection("Elderlies").document(elder.getDocId()).set(elder).addOnCompleteListener(task -> {
                                        if(task.isSuccessful()){
                                            if (dbHelper.addElderData(elder)) {
                                                Toast.makeText(getApplicationContext(), R.string.info_add_success, Toast.LENGTH_LONG).show();
                                                elderAdapter.notifyDataSetInvalidated();
                                            } else {
                                                Toast.makeText(getApplicationContext(), R.string.info_add_fail, Toast.LENGTH_LONG).show();
                                            }
                                        }
                                    });
                                }
                            });
                            auth.signOut();
                        }
                    }

                    @Override
                    public void onSaveChanges(User user) {

                    }

                    @Override
                    public void onSaveChanges(Doctor doctor) {

                    }

                }, ElderlyItemDialog.ItemType.ELDER);

                elderlyItemDialog.show();
                elderlyItemDialog.setEditTextValues(null, null, null);
            }
        });
    }

    @Override
    public void onClick(View view) {

    }

    @Override
    public void onPointerCaptureChanged(boolean hasCapture) {
        super.onPointerCaptureChanged(hasCapture);
    }

    private void deleteUser(Elder elder) {
        db = FirebaseFirestore.getInstance();
        db.collection("Elderlies").document(elder.getDocId()).delete().addOnCompleteListener(task -> {
            if(task.isSuccessful()){
                if(dbHelper.deleteElderByDocId(elder.getDocId())){
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
