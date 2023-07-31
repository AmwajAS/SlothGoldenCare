package com.example.slothgoldencare;

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

import com.example.slothgoldencare.Model.Doctor;
import com.example.slothgoldencare.Model.Elder;
import com.example.slothgoldencare.Model.User;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;
import java.util.List;

public class AdministratorUsersActivity extends AppCompatActivity implements View.OnClickListener {
    private static final String TAG = "ADMIN";

    private ListView usersList;

    private List<User> users;
    private DataBaseHelper dbHelper;
    private FirebaseFirestore db;
    private Button buttonAddUser;
    private Button backBtn;
    private FirebaseAuth auth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_users);
        dbHelper = new DataBaseHelper(this);
        auth = FirebaseAuth.getInstance();
        usersList = findViewById(R.id.users_list);
        //eldersList = findViewById(R.id.elderly_list);
        users = new ArrayList<>();
        users = dbHelper.getUsers();
        buttonAddUser = findViewById(R.id.buttonAdd);

        // back btn to remove the replaced view to the main one.
        backBtn = findViewById(R.id.back_btn);
        backBtn.setOnClickListener(view1 -> {
            Intent intent = new Intent(AdministratorUsersActivity.this,AdministratorActivity.class);
            startActivity(intent);
        });


        ArrayAdapter<User> userAdapter = new ArrayAdapter<User>(this, R.layout.administrator_user_item, users) {
            @NonNull
            @Override
            public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {


                // Get the user object for the current position
                User user = getItem(position);

                // Inflate the list item layout
                if (convertView == null) {
                    convertView = LayoutInflater.from(getContext()).inflate(R.layout.administrator_user_item, parent, false);
                }

                // Set the username in the TextView
                TextView userNameTextView = convertView.findViewById(R.id.user_name);
                userNameTextView.setText(user.getUsername());
                ImageButton deleteButton = convertView.findViewById(R.id.delete_user_btn);
                ImageButton editButton = convertView.findViewById(R.id.edit_user_btn);




                // Set a click listener for the delete button
                deleteButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
                        builder.setTitle("Confirm Deletion");
                        builder.setMessage("Are you sure you want to delete this user?");
                        builder.setPositiveButton("Delete", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                users.remove(user);
                                deleteUser(user);
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

                            }

                            @Override
                            public void onSaveChanges(User user) {
                                if (user != null) {
                                    AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
                                    builder.setTitle("Confirm Update");
                                    builder.setMessage("Are you sure you want to update this user's information?");
                                    builder.setPositiveButton("Update", new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialog, int which) {
                                            db = FirebaseFirestore.getInstance();
                                            db.collection("Users").document(user.getDocId()).update(
                                                    "id", user.getID(),
                                                    "username", user.getUsername(),
                                                    "phoneNumber", user.getPhoneNumber(),
                                                    "email", user.getEmail(),
                                                    "password", user.getPassword()
                                            ).addOnCompleteListener(task -> {
                                                if (task.isSuccessful()) {
                                                    if (dbHelper.updateUserInfo(user)) {
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
                            public void onSaveChanges(Doctor doctor) {

                            }
                        }, ElderlyItemDialog.ItemType.USER);

                        elderlyItemDialog.show();
                        elderlyItemDialog.setEditTextValues(null,user,null);
                    }
                });
                return convertView;
            }
        };
        usersList.setAdapter(userAdapter);

        buttonAddUser.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                ElderlyItemDialog elderlyItemDialog = new ElderlyItemDialog(AdministratorUsersActivity.this, new ElderlyItemDialog.OnSaveChangesListener() {
                    @Override
                    public void onSaveChanges(Elder elder) {

                    }

                    @Override
                    public void onSaveChanges(User user) {
                        if(user != null){
                            db = FirebaseFirestore.getInstance();
                            auth.createUserWithEmailAndPassword(user.getEmail(),user.getPassword()).addOnCompleteListener(task1 -> {
                                if (task1.isSuccessful()) {
                                    user.setDocId(auth.getUid());
                                    db.collection("Users").document(user.getDocId()).set(user).addOnCompleteListener(task -> {
                                        if (task.isSuccessful()) {
                                            if (dbHelper.addUserData(user)) {
                                                userAdapter.notifyDataSetInvalidated();
                                                Toast.makeText(getApplicationContext(), R.string.info_add_success, Toast.LENGTH_LONG).show();
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
                    public void onSaveChanges(Doctor doctor) {

                    }

                }, ElderlyItemDialog.ItemType.USER);

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

    private void deleteUser(User user) {
        db = FirebaseFirestore.getInstance();
        db.collection("Users").document(user.getDocId()).delete().addOnCompleteListener(task -> {
            if(task.isSuccessful()){
                if(dbHelper.deleteUserByDocId(user.getDocId())){
                    Toast.makeText(getApplicationContext(),R.string.info_delete_success,Toast.LENGTH_LONG).show();
                }else{
                    Toast.makeText(getApplicationContext(),R.string.info_delete_fail+" || "+"Problem in Sqlite.",Toast.LENGTH_LONG).show();
                }
            }else{
                Toast.makeText(getApplicationContext(),R.string.info_delete_fail+" || "+task.getException().getMessage().toString(),Toast.LENGTH_LONG).show();
            }

        });
    }
}
