package com.example.slothgoldencare;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.*;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;
import java.util.List;

public class AdministratorActivity extends AppCompatActivity implements View.OnClickListener{
    private ListView usersList;
    private List<User> users;
    DataBaseHelper dbHelper;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_administrator);
        dbHelper = new DataBaseHelper(this);
        usersList = findViewById(R.id.users_list);
        users = new ArrayList<>();
        users = dbHelper.getUsers();

        //users.add()
        ArrayAdapter<User> userAdapter = new ArrayAdapter<User>(this, R.layout.administrator_user_item, users){
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
                        Toast.makeText(AdministratorActivity.this, "User details changed successfully", Toast.LENGTH_SHORT).show();
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
                        }
                    }
                });
                return convertView;
            }
        };
        usersList.setAdapter(userAdapter);
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
