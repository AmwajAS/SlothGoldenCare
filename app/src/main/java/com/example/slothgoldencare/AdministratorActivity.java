package com.example.slothgoldencare;

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

import java.util.ArrayList;
import java.util.List;

public class AdministratorActivity extends AppCompatActivity implements View.OnClickListener {
    private static final String TAG = "ADMIN";

    private ListView usersList;
    private ListView eldersList;
    private List<User> users;
    private List<Elder> elders;
    DataBaseHelper dbHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_administrator);
        dbHelper = new DataBaseHelper(this);
        usersList = findViewById(R.id.users_list);
        eldersList = findViewById(R.id.elderly_list);
        users = new ArrayList<>();
        elders = new ArrayList<>();
        users = dbHelper.getUsers();
        elders = dbHelper.getElders();

        //users.add()
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
                LinearLayout editLayout = convertView.findViewById(R.id.user_edit);
                EditText idText = convertView.findViewById(R.id.user_id_text);
                EditText nameText = convertView.findViewById(R.id.user_name_text);
                EditText phoneText = convertView.findViewById(R.id.user_phone_text);
                // Set the username in the TextView
                TextView userNameTextView = convertView.findViewById(R.id.user_name);
                userNameTextView.setText(user.getUsername());
                ImageButton deleteButton = convertView.findViewById(R.id.delete_user_btn);
                ImageButton editButton = convertView.findViewById(R.id.edit_user_btn);
                Button saveUserChanges = convertView.findViewById(R.id.save_user_changes);
                saveUserChanges.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        user.setID(idText.getText().toString());
                        user.setUsername(nameText.getText().toString());
                        user.setPhoneNumber(phoneText.getText().toString());

                        String newID = idText.getText().toString();
                        String newName = nameText.getText().toString();
                        String newPhone = phoneText.getText().toString();


                        Toast.makeText(AdministratorActivity.this, "User details changed successfully", Toast.LENGTH_SHORT).show();
                        dbHelper.updateUserInfo(newID, newName, newPhone);
                    }
                });


                // Set a click listener for the delete button
                deleteButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        users.remove(user);
                        deleteUser(user);
                        notifyDataSetChanged();
                    }
                });
                editButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        if (editLayout.getVisibility() == View.VISIBLE) {
                            editLayout.setVisibility(View.GONE);
                        } else {
                            editLayout.setVisibility(View.VISIBLE);
                            idText.setText(user.getID());
                            nameText.setText(user.getUsername());
                            phoneText.setText(user.getPhoneNumber());

                            Toast.makeText(AdministratorActivity.this, "User details changed successfully", Toast.LENGTH_SHORT).show();


                        }
                    }
                });
                return convertView;
            }
        };
        ArrayAdapter<Elder> elderAdapter = new ArrayAdapter<Elder>(this, R.layout.administrator_user_item, elders) {
            @NonNull
            @Override
            public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {


                // Get the user object for the current position
                Elder elder = getItem(position);

                // Inflate the list item layout
                if (convertView == null) {
                    convertView = LayoutInflater.from(getContext()).inflate(R.layout.administrator_user_item, parent, false);
                }
                LinearLayout editLayout = convertView.findViewById(R.id.user_edit);
                EditText idText = convertView.findViewById(R.id.user_id_text);
                EditText nameText = convertView.findViewById(R.id.user_name_text);
                EditText phoneText = convertView.findViewById(R.id.user_phone_text);
                // Set the username in the TextView
                TextView userNameTextView = convertView.findViewById(R.id.user_name);
                userNameTextView.setText(elder.getUsername());
                ImageButton deleteButton = convertView.findViewById(R.id.delete_user_btn);
                ImageButton editButton = convertView.findViewById(R.id.edit_user_btn);
                Button saveUserChanges = convertView.findViewById(R.id.save_user_changes);
                saveUserChanges.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        elder.setID(idText.getText().toString());
                        elder.setUsername(nameText.getText().toString());
                        elder.setPhoneNumber(phoneText.getText().toString());


                        String newID = idText.getText().toString();
                        String newName = nameText.getText().toString();
                        String newPhone = phoneText.getText().toString();

                        Log.i(TAG, "This is a new elder message " + newID + newName + newPhone + elder.formatDateOfBirth(elder.getDOB())+ elder.getGender().toString()); // Debug log
                        dbHelper.updateElderInfo(newID, newName, newPhone, elder.formatDateOfBirth(elder.getDOB()), elder.getGender().toString());
                        Log.i(TAG, "This is a new elder message " + elder.formatDateOfBirth(elder.getDOB()) ); // Debug log

                        Toast.makeText(AdministratorActivity.this, "User details changed successfully", Toast.LENGTH_SHORT).show();
                    }
                });


                // Set a click listener for the delete button
                deleteButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        elders.remove(elder);
                        deleteUser(elder);
                        notifyDataSetChanged();
                    }
                });
                editButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        if (editLayout.getVisibility() == View.VISIBLE) {
                            editLayout.setVisibility(View.GONE);
                        } else {
                            editLayout.setVisibility(View.VISIBLE);
                            idText.setText(elder.getID());
                            nameText.setText(elder.getUsername());
                            phoneText.setText(elder.getPhoneNumber());
                        }
                    }
                });
                return convertView;
            }
        };
        usersList.setAdapter(userAdapter);
        eldersList.setAdapter(elderAdapter);
    }

    @Override
    public void onClick(View view) {

    }

    @Override
    public void onPointerCaptureChanged(boolean hasCapture) {
        super.onPointerCaptureChanged(hasCapture);
    }

    private void deleteUser(User user) {
        dbHelper.deleteUserById(user.getID());
    }
}
