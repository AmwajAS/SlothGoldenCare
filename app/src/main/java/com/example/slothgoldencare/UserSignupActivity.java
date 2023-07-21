package com.example.slothgoldencare;

import android.app.ProgressDialog;
import android.content.Intent;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.slothgoldencare.Model.User;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserProfileChangeRequest;
import com.google.firebase.firestore.FirebaseFirestore;

import org.jetbrains.annotations.NotNull;


import java.sql.SQLDataException;

public class UserSignupActivity extends AppCompatActivity {
    private static final String TAG = "UserSignupActivity";
    private EditText userID;
    private EditText userName;
    private EditText userPhone;
    private EditText userEmail;
    private EditText userPassword;
    private Button signup;
    private boolean valid;

    private FirebaseFirestore db;
    private ProgressDialog progressDialog;;
    DataBaseManager dbManager;

    DataBaseHelper dbHelper = new DataBaseHelper(this);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        db = FirebaseFirestore.getInstance();
        setContentView(R.layout.activity_user_signup);
        userID = findViewById(R.id.userID);
        userName = findViewById(R.id.userName);
        userPhone = findViewById(R.id.userPhone);
        userEmail = findViewById(R.id.userEmail);
        userPassword = findViewById(R.id.userPassword);
        dbManager = new DataBaseManager(this);
        try {
            dbManager.open();
        } catch (SQLDataException e) {
            throw new RuntimeException(e);
        }
        signup = (Button) findViewById(R.id.signupBtn);
        signup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String newID = userID.getText().toString();
                String newName = userName.getText().toString();
                String newPhone = userPhone.getText().toString();
                String newEmail = userEmail.getText().toString();
                String newPassword = userPassword.getText().toString();
                //Log.i(TAG, "This is a debug message" + userID.toString() + userName.toString() + userPhone.toString()); // Debug log

                if ((newID.length() != 0) && (newName.length() != 0) && (newPhone.length() != 0)) {
                    if (!ElderSignupActivity.checkIDValidation(newID)) {
                        SimpleDialog.showAlertDialog(UserSignupActivity.this, R.string.alert_title_signup, R.string.alert_message_id);
                    }
                    ElderSignupActivity.checkUsersIDExistence(newID, new ElderSignupActivity.IDExistenceCallback() {
                        @Override
                        public void onIDExistenceChecked(boolean isIDValid) {
                            if (isIDValid) {
                                ElderSignupActivity.checkElderliesIDExistence(newID, new ElderSignupActivity.IDExistenceCallback() {
                                    @Override
                                    public void onIDExistenceChecked(boolean isIDValid) {
                                        if(isIDValid){
                                            if(!ElderSignupActivity.checkEmailValidation(newEmail)){
                                                SimpleDialog.showAlertDialog(UserSignupActivity.this, R.string.alert_title_signup, R.string.alert_message_email);
                                            } else if (!ElderSignupActivity.validatePhoneNumber(newPhone)) {
                                                SimpleDialog.showAlertDialog(UserSignupActivity.this, R.string.alert_title_signup, R.string.alert_message_phone);
                                            }
                                            else if(!ElderSignupActivity.checkEmailValidation(newEmail)){
                                                SimpleDialog.showAlertDialog(UserSignupActivity.this, R.string.alert_title_signup, R.string.alert_message_email);
                                            } else {
                                                progressDialog = ProgressDialog.show(UserSignupActivity.this, "Creating Account", "Please wait...", true);
                                                User user = new User(newID, newName, newPhone,newEmail,newPassword);
                                                insertData(user);
                                                userID.setText("");
                                                userName.setText("");
                                                userPhone.setText("");
                                                userEmail.setText("");
                                                userPassword.setText("");
                                            }
                                        }
                                        else{
                                            SimpleDialog.showAlertDialog(UserSignupActivity.this, R.string.alert_title_signup, R.string.alert_message_id_exists);
                                        }
                                    }
                                });

                            } else {
                                   SimpleDialog.showAlertDialog(UserSignupActivity.this, R.string.alert_title_signup, R.string.alert_message_id_exists);
                            }
                        }
                    });
                } else{
                    SimpleDialog.showAlertDialog(UserSignupActivity.this, R.string.alert_title_signup, R.string.alert_message_null);

                }
            }
        });

    }


    public void DBinsertData(User newEntry) {
        boolean insertData;
        try {
            insertData = dbHelper.addUserData(newEntry);

        } catch (NullPointerException e) {
            throw new RuntimeException(e);
        }
        if (insertData) {
            toastMessage("Data Successfully inserted");
        } else {
            toastMessage("Something Went Wrong");

        }
    }
    private void insertData(User user) {

        FirebaseAuth auth = FirebaseAuth.getInstance();
        //creating user in Authenticator
        auth.createUserWithEmailAndPassword(user.getEmail(),user.getPassword()).addOnCompleteListener(UserSignupActivity.this,
                new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull @NotNull Task<AuthResult> task) {
                        if(task.isSuccessful()) {
                            FirebaseUser firebaseUser = auth.getCurrentUser();

                            //Send Verification Email
                            firebaseUser.sendEmailVerification();

                            //Save user data in firestore
                            db = FirebaseFirestore.getInstance();
                            db.collection("Users").document(firebaseUser.getUid()).set(user).addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull @NotNull Task<Void> task) {
                                    //updating the display name in the authenticator for the elderly username
                                    if (task.isSuccessful()) {
                                        UserProfileChangeRequest profileUpdates = new UserProfileChangeRequest.Builder()
                                                .setDisplayName(user.getUsername())
                                                .build();
                                        firebaseUser.updateProfile(profileUpdates).addOnCompleteListener(new OnCompleteListener<Void>() {
                                            @Override
                                            public void onComplete(@NonNull @NotNull Task<Void> task) {
                                                //if all went good then we can declare that the user created successfully and move to the home page.
                                                if(task.isSuccessful()){
                                                    DataBaseHelper dataBaseHelper = new DataBaseHelper(getApplicationContext());
                                                    user.setDocId(firebaseUser.getUid());
                                                    if(dataBaseHelper.addUserData(user)){
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
                                                    Toast.makeText(UserSignupActivity.this, task.getException().getMessage().toString(), Toast.LENGTH_LONG).show();
                                                }
                                            }
                                        });

                                    } else {
                                        Toast.makeText(UserSignupActivity.this, task.getException().getMessage().toString(), Toast.LENGTH_LONG).show();
                                    }
                                }
                            });
                        }
                        //Open user profile

                        else{
                            Toast.makeText(UserSignupActivity.this,task.getException().getMessage().toString(),Toast.LENGTH_LONG).show();
                        }
                    }
                });
        progressDialog.dismiss();
    }

    private void toastMessage(String message) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
    }

    //Need to check ID existence function.


}