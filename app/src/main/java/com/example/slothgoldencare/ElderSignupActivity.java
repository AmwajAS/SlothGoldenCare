package com.example.slothgoldencare;

import android.app.ProgressDialog;
import android.content.Intent;
import android.util.Patterns;
import android.widget.*;
import androidx.appcompat.app.AppCompatActivity;
import android.app.DatePickerDialog;
import android.os.Bundle;
import android.util.Log;
import com.example.slothgoldencare.Model.Elder;
import com.example.slothgoldencare.Model.Gender;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserProfileChangeRequest;
import com.google.firebase.firestore.FirebaseFirestore;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class ElderSignupActivity extends AppCompatActivity {

    private static final String TAG = "ElderSignupActivity";
    private EditText userID;
    private EditText userName;
    private EditText userPhone;
    private EditText emailEditText;
    private EditText passwordEditText;
    private ProgressDialog progressDialog;
    private EditText etSelectDate;
    private FirebaseFirestore db;

    DataBaseHelper dbHelper = new DataBaseHelper(this);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_elder_signup);
        db = FirebaseFirestore.getInstance();
        userID = findViewById(R.id.userID);
        userName = findViewById(R.id.userName);
        userPhone = findViewById(R.id.userPhone);
        etSelectDate = findViewById(R.id.etSelectDate);
        emailEditText = findViewById(R.id.email);
        passwordEditText = findViewById(R.id.password);


        etSelectDate.setOnClickListener(v -> showDatePickerDialog(etSelectDate));


        Button signupBtn = findViewById(R.id.signupBtn);
        signupBtn.setOnClickListener(v -> {
            String newID = userID.getText().toString();
            String newName = userName.getText().toString();
            String newPhone = userPhone.getText().toString();
            String newEmail = emailEditText.getText().toString();
            String newPassword = passwordEditText.getText().toString();
            Date newDate = convertStringIntoDate(etSelectDate.getText().toString());
            Gender elderGender = onGenderSelection();
            Log.i(TAG, "This is a debug message" + newID + newName + newPhone + newDate + elderGender); // Debug log


            //Checking validation of input.
            if ((newID.length() != 0) && (newName.length() != 0) && (newPhone.length() != 0) && (newDate != null) && (elderGender != null)) {
                if (!checkIDValidation(newID)) {
                    SimpleDialog.showAlertDialog(ElderSignupActivity.this, R.string.alert_title_signup, R.string.alert_message_id);
                }
                //checking existence of ID
                checkUsersIDExistence(newID, isIDValid -> {
                    if (isIDValid) {
                        checkElderliesIDExistence(newID, isIDValid1 -> {
                            if(isIDValid1){
                                //checking email, phone validation
                                if(!checkEmailValidation(newEmail)){
                                    Snackbar.make(getWindow().getDecorView(), R.string.alert_message_email,Snackbar.LENGTH_LONG).show();
                                } else if (!validatePhoneNumber(newPhone)) {
                                    Snackbar.make(getWindow().getDecorView(), R.string.alert_message_phone,Snackbar.LENGTH_LONG).show();
                                } else {
                                    //Continue if all validations go well.
                                    progressDialog = ProgressDialog.show(ElderSignupActivity.this, "Creating Account", "Please wait...", true);
                                    Elder elder = new Elder(newID, newName, newPhone,newDate,elderGender,newEmail,newPassword);
                                    insertData(elder);
                                    userID.setText("");
                                    userName.setText("");
                                    userPhone.setText("");
                                    emailEditText.setText("");
                                    passwordEditText.setText("");
                                }
                            }
                            else{
                                SimpleDialog.showAlertDialog(ElderSignupActivity.this, R.string.alert_title_signup, R.string.alert_message_id_exists);
                            }
                        });

                    } else {
                        SimpleDialog.showAlertDialog(ElderSignupActivity.this, R.string.alert_title_signup, R.string.alert_message_id_exists);
                    }
                });

            } else {
                SimpleDialog.showAlertDialog(ElderSignupActivity.this, R.string.alert_title_signup, R.string.alert_message_null);
            }
        });
    }



    /*
     Since the Real ID Number consists of 9 digits Only, So in this method we check the ID Validation.
     9 numbers only between 0-9.
    */
    public static boolean checkIDValidation(String idV) {
        if (idV.matches("[0-9]{9}")) {
            return true;
        } else {
            return false;
        }
    }

    public interface IDExistenceCallback {
        void onIDExistenceChecked(boolean isIDValid);
    }
    //Checking if the ID exists for users

    public static void checkUsersIDExistence(String idV, IDExistenceCallback callback) {
        FirebaseFirestore db;
        db = FirebaseFirestore.getInstance();
        db.collection("Users").whereEqualTo("id", idV).get().addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                boolean isIDValid = task.getResult().getDocuments().isEmpty();
                callback.onIDExistenceChecked(isIDValid);
            } else {
                // Handle any errors or exceptions
                callback.onIDExistenceChecked(false); // Assume invalid ID if there's an error
            }
        });
    }
    //Checking if the ID exists for elderlies
    public static void checkElderliesIDExistence(String idV, IDExistenceCallback callback) {
        FirebaseFirestore db;
        db = FirebaseFirestore.getInstance();
        db.collection("Elderlies").whereEqualTo("id", idV).get().addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                boolean isIDValid = task.getResult().getDocuments().isEmpty();
                callback.onIDExistenceChecked(isIDValid);
            } else {
                // Handle any errors or exceptions
                callback.onIDExistenceChecked(false); // Assume invalid ID if there's an error
            }
        });
    }
    //Email validation function
    public static boolean checkEmailValidation(String email){
        boolean valid = true;
        if(!Patterns.EMAIL_ADDRESS.matcher(email).matches()){
            valid = false;
        }
        return valid;
    }

    public static boolean validatePhoneNumber(String phoneNumber) {
        // Define the regular expression pattern
        String pattern = "^05\\d{8}$";

        // Create a pattern object
        Pattern regex = Pattern.compile(pattern);

        // Create a matcher object
        Matcher matcher = regex.matcher(phoneNumber);

        // Check if the phone number matches the pattern
        return matcher.matches();
    }


    private void DBinsertData(Elder elder) {
        boolean insertData;
        try {
            insertData = dbHelper.addElderData(elder);

        } catch (NullPointerException e) {
            throw new RuntimeException(e);
        }
        if (insertData) {
            toastMessage("Data Successfully inserted");
        } else {
            toastMessage("Something Went Wrong, Please Try again");

        }

    }
    //Creating user with Authentication and Firestore
    private void insertData(Elder elder) {

        FirebaseAuth auth = FirebaseAuth.getInstance();
        //creating user in Authenticator
        auth.createUserWithEmailAndPassword(elder.getEmail(),elder.getPassword()).addOnCompleteListener(ElderSignupActivity.this,
                task -> {
                    if(task.isSuccessful()) {
                        FirebaseUser firebaseUser = auth.getCurrentUser();

                        //Send Verification Email
                        firebaseUser.sendEmailVerification();

                        //Save user data in firestore
                        db = FirebaseFirestore.getInstance();
                        db.collection("Elderlies").document(firebaseUser.getUid()).set(elder).addOnCompleteListener(task1 -> {
                            //updating the display name in the authenticator for the elderly username
                            if (task1.isSuccessful()) {
                                UserProfileChangeRequest profileUpdates = new UserProfileChangeRequest.Builder()
                                        .setDisplayName(elder.getUsername())
                                        .build();
                                firebaseUser.updateProfile(profileUpdates).addOnCompleteListener(task11 -> {
                                    //if all went good then we can declare that the user created successfully and move to the home page.
                                    if(task11.isSuccessful()){
                                        DataBaseHelper dataBaseHelper = new DataBaseHelper(getApplicationContext());
                                        elder.setDocId(firebaseUser.getUid());
                                        if(dataBaseHelper.addElderData(elder)){
                                            Toast.makeText(ElderSignupActivity.this, "User registered successfully", Toast.LENGTH_LONG).show();
                                            Intent intent = new Intent(ElderSignupActivity.this, HomePageActivity.class);
                                            intent.putExtra("userID", elder.getID());
                                            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                                            progressDialog.dismiss();
                                            startActivity(intent);
                                            finish();
                                        }else{
                                            SimpleDialog.showAlertDialog(ElderSignupActivity.this, R.string.alert_title_signup, R.string.sqlite_adding_elderly_data_error);
                                        }
                                    }
                                    else{
                                        Toast.makeText(ElderSignupActivity.this, task11.getException().getMessage(), Toast.LENGTH_LONG).show();
                                    }
                                });

                            } else {
                                Toast.makeText(ElderSignupActivity.this, task1.getException().getMessage(), Toast.LENGTH_LONG).show();
                            }
                        });
                    }
                    //Open user profile

                    else{
                        Toast.makeText(ElderSignupActivity.this, task.getException().getMessage(),Toast.LENGTH_LONG).show();
                    }
                });
        progressDialog.dismiss();
    }


    public Gender onGenderSelection() {
        Gender userGender = null;

        RadioGroup radioGroup = findViewById(R.id.genderGroup);
        switch (radioGroup.getCheckedRadioButtonId()) {
            case R.id.maleBtn:
                userGender = Gender.Male;
                break;
            case R.id.femaleBtn:
                userGender = Gender.Female;
                break;
        }
        return userGender;
    }


    public static Date convertStringIntoDate(String dateString) {
        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
        Date date = null;
        try {
            date = sdf.parse(dateString);
            // Your code to handle the parsed date
        } catch (ParseException e) {
            // Handle the case where the input is not a valid date
        }
        Log.i(TAG,"This is the date 3:"+ date);
        return date;
    }

    private void showDatePickerDialog(final TextView textView) {

        final Calendar calendar = Calendar.getInstance();
        final int year = calendar.get(Calendar.YEAR);
        final int month = calendar.get(Calendar.MONTH);
        final int day = calendar.get(Calendar.DAY_OF_MONTH);
        DatePickerDialog dialog = new DatePickerDialog(ElderSignupActivity.this, (view, year1, month1, dayOfMonth) -> {
            month1 = month1 + 1;
            String date = dayOfMonth + "/" + month1 + "/" + year1;
            textView.setText(date);
        }, year, month, day);
        dialog.show();
    }

    private void toastMessage(String data_successfully_inserted) {
    }


}