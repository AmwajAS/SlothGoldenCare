package com.example.slothgoldencare;

import android.app.ProgressDialog;
import android.content.Intent;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import com.example.slothgoldencare.Model.User;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserProfileChangeRequest;
import com.google.firebase.firestore.FirebaseFirestore;
import java.util.Objects;


public class UserSignupActivity extends AppCompatActivity {
    private EditText userID;
    private EditText userName;
    private EditText userPhone;
    private EditText userEmail;
    private EditText userPassword;

    private FirebaseFirestore db;
    private ProgressDialog progressDialog;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        db = FirebaseFirestore.getInstance();
        setContentView(R.layout.activity_user_signup);

        //values in the layout
        userID = findViewById(R.id.userID);
        userName = findViewById(R.id.userName);
        userPhone = findViewById(R.id.userPhone);
        userEmail = findViewById(R.id.userEmail);
        userPassword = findViewById(R.id.userPassword);

        //signup button function
        Button signup = findViewById(R.id.signupBtn);
        signup.setOnClickListener(v -> {
            String newID = userID.getText().toString();
            String newName = userName.getText().toString();
            String newPhone = userPhone.getText().toString();
            String newEmail = userEmail.getText().toString();
            String newPassword = userPassword.getText().toString();

            //checking if empty
            if ((newID.length() != 0) && (newName.length() != 0) && (newPhone.length() != 0)) {
                if (!ElderSignupActivity.checkIDValidation(newID)) {
                    //checking if id is 9 digits
                    SimpleDialog.showAlertDialog(UserSignupActivity.this, R.string.alert_title_signup, R.string.alert_message_id);
                }else {
                    //checking if the id exists in elderlies or users
                    ElderSignupActivity.checkUsersIDExistence(newID, isIDValid -> {
                        if (isIDValid) {
                            ElderSignupActivity.checkElderliesIDExistence(newID, isIDValid1 -> {
                                if (isIDValid1) {
                                    //checking validation of emails and phone
                                    if (!ElderSignupActivity.checkEmailValidation(newEmail)) {
                                        Snackbar.make(getWindow().getDecorView(), R.string.alert_message_email,Snackbar.LENGTH_LONG).show();
                                    } else if (!ElderSignupActivity.validatePhoneNumber(newPhone)) {
                                        Snackbar.make(getWindow().getDecorView(), R.string.alert_message_phone,Snackbar.LENGTH_LONG).show();
                                    } else {
                                        //if successful start creating the account
                                        progressDialog = ProgressDialog.show(UserSignupActivity.this, "Creating Account", "Please wait...", true);
                                        User user = new User(newID, newName, newPhone, newEmail, newPassword);
                                        insertData(user);
                                        userID.setText("");
                                        userName.setText("");
                                        userPhone.setText("");
                                        userEmail.setText("");
                                        userPassword.setText("");
                                    }
                                } else {
                                    SimpleDialog.showAlertDialog(UserSignupActivity.this, R.string.alert_title_signup, R.string.alert_message_id_exists);
                                }
                            });

                        } else {
                            SimpleDialog.showAlertDialog(UserSignupActivity.this, R.string.alert_title_signup, R.string.alert_message_id_exists);
                        }
                    });
                }
            } else{
                SimpleDialog.showAlertDialog(UserSignupActivity.this, R.string.alert_title_signup, R.string.alert_message_null);

            }
        });

    }

    //Function for inserting data and creating account
    private void insertData(User user) {

        FirebaseAuth auth = FirebaseAuth.getInstance();
        //creating user in Authenticator
        auth.createUserWithEmailAndPassword(user.getEmail(),user.getPassword()).addOnCompleteListener(UserSignupActivity.this,
                task -> {
                    if(task.isSuccessful()) {
                        FirebaseUser firebaseUser = auth.getCurrentUser();

                        //Send Verification Email
                        firebaseUser.sendEmailVerification();

                        //Save user data in firestore
                        db = FirebaseFirestore.getInstance();
                        db.collection("Users").document(firebaseUser.getUid()).set(user).addOnCompleteListener(task1 -> {
                            //updating the display name in the authenticator for the elderly username
                            if (task1.isSuccessful()) {
                                UserProfileChangeRequest profileUpdates = new UserProfileChangeRequest.Builder()
                                        .setDisplayName(user.getUsername())
                                        .build();
                                firebaseUser.updateProfile(profileUpdates).addOnCompleteListener(task11 -> {
                                    //if all went good then we can declare that the user created successfully and move to the home page.
                                    if(task11.isSuccessful()){
                                        DataBaseHelper dataBaseHelper = new DataBaseHelper(getApplicationContext());
                                        user.setDocId(firebaseUser.getUid());
                                        if(dataBaseHelper.addUserData(user)){
                                            //if adding account worked go to Home page.
                                            Toast.makeText(UserSignupActivity.this, "User registered successfully", Toast.LENGTH_LONG).show();
                                            Intent intent = new Intent(UserSignupActivity.this, UserHomePageActivity.class);
                                            intent.putExtra("userID", user.getID());
                                            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                                            progressDialog.dismiss();
                                            startActivity(intent);
                                            finish();
                                        }else{
                                            SimpleDialog.showAlertDialog(UserSignupActivity.this, R.string.alert_title_signup, R.string.sqlite_adding_user_data_error);
                                        }
                                    }
                                    else{
                                        Snackbar.make(getWindow().getDecorView(), Objects.requireNonNull(task11.getException()).getMessage(), Snackbar.LENGTH_LONG).show();
                                    }
                                });

                            } else {
                                Snackbar.make(getWindow().getDecorView(), Objects.requireNonNull(task1.getException()).getMessage(), Snackbar.LENGTH_LONG).show();
                            }
                        });
                    }
                    else{
                        Snackbar.make(getWindow().getDecorView(), Objects.requireNonNull(task.getException()).getMessage(), Snackbar.LENGTH_LONG).show();
                    }
                });
        progressDialog.dismiss();
    }

}