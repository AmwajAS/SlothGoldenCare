package com.example.slothgoldencare;

import static android.content.ContentValues.TAG;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;

public class ProfileActivity extends AppCompatActivity {

    private TextView usernameTextView;
    private TextView emailTextView;
    private TextView phoneNumberTextView;
    private EditText emailEditText;
    private EditText phoneNumberEditText;
    private Button editButton;
    private Button logOutBtn;
    private Button saveButton;
    private FirebaseAuth auth;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        // Initialize views
        usernameTextView = findViewById(R.id.usernameTextView);
        emailTextView = findViewById(R.id.emailTextView);
        phoneNumberTextView = findViewById(R.id.phoneNumberTextView);
        emailEditText = findViewById(R.id.emailEditText);
        phoneNumberEditText = findViewById(R.id.phoneNumberEditText);
        editButton = findViewById(R.id.editButton);
        saveButton = findViewById(R.id.saveButton);
        logOutBtn = findViewById(R.id.log_out_btn);

        logOutBtn.setOnClickListener(view -> {
            auth = FirebaseAuth.getInstance();
            auth.signOut();
            Intent intent = new Intent(this.getApplicationContext(), LoginActivity.class);
            startActivity(intent);
        });

        // Set initial visibility
        emailTextView.setVisibility(View.VISIBLE);
        phoneNumberTextView.setVisibility(View.VISIBLE);
        emailEditText.setVisibility(View.GONE);
        phoneNumberEditText.setVisibility(View.GONE);
        saveButton.setVisibility(View.GONE);

        String userId = FirebaseAuth.getInstance().getCurrentUser().getUid();
//        Log.d(TAG, "Inserted Row Id: " + userId);
        FirebaseFirestore firestore = FirebaseFirestore.getInstance();
        // Query the "users" collection
        firestore.collection("Elderlies").document(userId).get()
                .addOnSuccessListener(documentSnapshot -> {
                    if (documentSnapshot.exists()) {
                        String username = documentSnapshot.getString("username");
                        String email = documentSnapshot.getString("email");
                        String phoneNumber = documentSnapshot.getString("phoneNumber");
                        usernameTextView.setText(username);
                        emailTextView.setText(email);
                        phoneNumberTextView.setText(phoneNumber);
                    }
                })
                .addOnFailureListener(e -> {
                    Log.d(TAG, "Error in loading the data." + userId);
                });
        // Set click listener for Edit button
        editButton.setOnClickListener(v -> {
            // Hide TextViews and show EditTexts and Save button
            emailTextView.setVisibility(View.GONE);
            phoneNumberTextView.setVisibility(View.GONE);
            emailEditText.setVisibility(View.VISIBLE);
            phoneNumberEditText.setVisibility(View.VISIBLE);
            saveButton.setVisibility(View.VISIBLE);

            // Set initial values in EditTexts
            emailEditText.setText(emailTextView.getText().toString());
            phoneNumberEditText.setText(phoneNumberTextView.getText().toString());
        });
        // Set click listener for Save button
        saveButton.setOnClickListener(v -> {
            // Get the updated email and phone number
            String newEmail = emailEditText.getText().toString();
            String newPhoneNumber = phoneNumberEditText.getText().toString();
            // Update the user data in Firestore
            firestore.collection("Elderlies").document(userId)
                    .update("email", newEmail, "phoneNumber", newPhoneNumber)
                    .addOnSuccessListener(aVoid -> {
                        // Update TextViews with the new values
                        emailTextView.setText(newEmail);
                        phoneNumberTextView.setText(newPhoneNumber);

                        // Show TextViews and hide EditTexts and Save button
                        emailTextView.setVisibility(View.VISIBLE);
                        phoneNumberTextView.setVisibility(View.VISIBLE);
                        emailEditText.setVisibility(View.GONE);
                        phoneNumberEditText.setVisibility(View.GONE);
                        saveButton.setVisibility(View.GONE);
                    })
                    .addOnFailureListener(e -> {
                        Log.d(TAG, "Error in updating the data." + userId);
                    });
        });
        bottomNavigationView();
    }
    /*
        this method handle the selected items / buttons of the bottom navigation bar.
         */
    public void bottomNavigationView() {
        BottomNavigationView bottomNavigationView = findViewById(R.id.bottomNavigation);
        Menu menu = bottomNavigationView.getMenu();
        MenuItem currentItem = menu.findItem(R.id.current);
        // Hiding the "current" menu item
        currentItem.setVisible(false);
        bottomNavigationView.setSelectedItemId(R.id.profile);
        bottomNavigationView.setOnItemSelectedListener(item -> {
            switch (item.getItemId()) {
                case R.id.home:
                    replaceView(HomePageActivity.class);
                    return true;
                case R.id.settings:
                    replaceView(SettingsActivity.class);
                    return true;
                case R.id.profile:
                    replaceView(ProfileActivity.class);
                    return true;
            }
            return false;
        });
    }

    public void replaceView(Class classView) {
        startActivity(new Intent(getApplicationContext(), classView));
        overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
        finish();
    }

}