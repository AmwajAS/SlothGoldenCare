package com.example.slothgoldencare;

import android.app.ProgressDialog;
import android.content.Intent;
import android.util.Patterns;
import android.widget.*;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserProfileChangeRequest;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;
import org.jetbrains.annotations.NotNull;

import java.sql.SQLDataException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;
import java.util.regex.Matcher;
import java.util.regex.Pattern;


public class ElderSignupActivity extends AppCompatActivity {

    private static final String TAG = "ElderSignupActivity";
    private EditText userID;
    private EditText userName;
    private EditText userPhone;
    private RadioButton maleBtn;
    private RadioButton femaleBtn;
    private EditText emailEditText;
    private EditText passwordEditText;
    private  boolean valid;
    private Button signupBtn;
    private RadioGroup genderGroup;
    private ProgressDialog progressDialog;;
    DataBaseManager dbManager;
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
        maleBtn = findViewById(R.id.maleBtn);
        femaleBtn = findViewById(R.id.femaleBtn);
        emailEditText = findViewById(R.id.email);
        passwordEditText = findViewById(R.id.password);

//        dbManager = new DataBaseManager(this);
//        try {
//            dbManager.open();
//        } catch (SQLDataException e) {
//            throw new RuntimeException(e);
//        }

        etSelectDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDatePickerDialog(etSelectDate);
            }
        });


        signupBtn = (Button) findViewById(R.id.signupBtn);
        signupBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
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
                    checkUsersIDExistence(newID, new ElderSignupActivity.IDExistenceCallback() {
                        @Override
                        public void onIDExistenceChecked(boolean isIDValid) {
                            if (isIDValid) {
                                checkElderliesIDExistence(newID, new ElderSignupActivity.IDExistenceCallback() {
                                    @Override
                                    public void onIDExistenceChecked(boolean isIDValid) {
                                        if(isIDValid){
                                            //checking email, phone validation
                                            if(!checkEmailValidation(newEmail)){
                                                SimpleDialog.showAlertDialog(ElderSignupActivity.this, R.string.alert_title_signup, R.string.alert_message_email);
                                            } else if (!validatePhoneNumber(newPhone)) {
                                                SimpleDialog.showAlertDialog(ElderSignupActivity.this, R.string.alert_title_signup, R.string.alert_message_phone);
                                            }
                                            else if(!checkEmailValidation(newEmail)){
                                                SimpleDialog.showAlertDialog(ElderSignupActivity.this, R.string.alert_title_signup, R.string.alert_message_email);
                                            } else {
                                                //Continue if all validations go well.
                                                progressDialog = ProgressDialog.show(ElderSignupActivity.this, "Creating Account", "Please wait...", true);
                                                Elder elder = new Elder(newID, newName, newPhone,newDate,elderGender,newEmail,newPassword);
                                                insertData(elder);
                                                userID.setText("");
                                                userName.setText("");
                                                userPhone.setText("");
                                            }
                                        }
                                        else{
                                            SimpleDialog.showAlertDialog(ElderSignupActivity.this, R.string.alert_title_signup, R.string.alert_message_id_exists);
                                        }
                                    }
                                });

                            } else {
                                SimpleDialog.showAlertDialog(ElderSignupActivity.this, R.string.alert_title_signup, R.string.alert_message_id_exists);
                            }
                        }
                    });

                } else {
                    SimpleDialog.showAlertDialog(ElderSignupActivity.this, R.string.alert_title_signup, R.string.alert_message_null);
                }
            }
        });
    }



    /*
     Since the Real Id Number consists of 9 digits Only, So in this method we check the Id Validation.
     9 numbers only between 0-9.
    */
    public static boolean checkIDValidation(String idV) {
        boolean valid = false;

        if (idV.matches("[0-9]{9}")) {
            return true;
        } else {
            return false;
        }
    }

    public interface IDExistenceCallback {
        void onIDExistenceChecked(boolean isIDValid);
    }
    //Checking if the Id exists for users

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
    //Checking if the Id exists for elderlies
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
                new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull @NotNull Task<AuthResult> task) {
                        if(task.isSuccessful()) {
                            FirebaseUser firebaseUser = auth.getCurrentUser();

                            //Send Verification Email
                            firebaseUser.sendEmailVerification();

                            //Save user data in firestore
                            db = FirebaseFirestore.getInstance();
                            db.collection("Elderlies").document(firebaseUser.getUid()).set(elder).addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull @NotNull Task<Void> task) {
                                    //updating the display name in the authenticator for the elderly username
                                    if (task.isSuccessful()) {
                                        UserProfileChangeRequest profileUpdates = new UserProfileChangeRequest.Builder()
                                                .setDisplayName(elder.getUsername())
                                                .build();
                                        firebaseUser.updateProfile(profileUpdates).addOnCompleteListener(new OnCompleteListener<Void>() {
                                            @Override
                                            public void onComplete(@NonNull @NotNull Task<Void> task) {
                                                //if all went good then we can declare that the user created successfully and move to the home page.
                                                if(task.isSuccessful()){
                                                    Toast.makeText(ElderSignupActivity.this, "User registered successfully", Toast.LENGTH_LONG).show();
                                                    Intent intent = new Intent(ElderSignupActivity.this, HomePageActivity.class);
                                                    intent.putExtra("userID", elder.getID());
                                                    intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                                                    progressDialog.dismiss();
                                                    startActivity(intent);
                                                    finish();
                                                }
                                                else{
                                                    Toast.makeText(ElderSignupActivity.this, task.getException().getMessage().toString(), Toast.LENGTH_LONG).show();
                                                }
                                            }
                                        });

                                    } else {
                                        Toast.makeText(ElderSignupActivity.this, task.getException().getMessage().toString(), Toast.LENGTH_LONG).show();
                                    }
                                }
                            });
                        }
                        //Open user profile

                        else{
                            Toast.makeText(ElderSignupActivity.this,task.getException().getMessage().toString(),Toast.LENGTH_LONG).show();
                        }
                    }
                });
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

    public String formatDateOfBirth(Date date) {
        SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
        return dateFormat.format(date);
    }



    private void showDatePickerDialog(final TextView textView) {

        final Calendar calendar = Calendar.getInstance();
        final int year = calendar.get(Calendar.YEAR);
        final int month = calendar.get(Calendar.MONTH);
        final int day = calendar.get(Calendar.DAY_OF_MONTH);
        DatePickerDialog dialog = new DatePickerDialog(ElderSignupActivity.this, new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                month = month + 1;
                String date = dayOfMonth + "/" + month + "/" + year;
                textView.setText(date);
            }
        }, year, month, day);
        dialog.show();
    }

    private void toastMessage(String data_successfully_inserted) {
    }


}