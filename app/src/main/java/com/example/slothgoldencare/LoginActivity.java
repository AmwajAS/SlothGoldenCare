package com.example.slothgoldencare;

import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.*;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;

import com.example.slothgoldencare.Model.Elder;
import com.example.slothgoldencare.Model.User;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

import org.jetbrains.annotations.NotNull;

import java.util.List;

public class LoginActivity extends AppCompatActivity {

    private static final String TAG = "LoginActivity";

    private EditText userid;
    DataBaseHelper dbHelper;
    public static User user;
    public static Elder elder;
    private FirebaseFirestore db;
    private FirebaseAuth auth;
    private RadioButton relativeRadioBtn;
    private RadioButton doctorRadioBtn;
    private RadioButton adminRadioBtn;
    private RadioGroup loginOptionRadio;
    private ProgressBar progressBar;
    private Task<QuerySnapshot> query;

    private ImageButton langBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        db = FirebaseFirestore.getInstance();
        setContentView(R.layout.activity_login);
        Button elderBtn = (Button) findViewById(R.id.elderBtn);
        Button userBtn = (Button) findViewById(R.id.userBtn);
        userid = (EditText) findViewById(R.id.userid);
        langBtn = (ImageButton) findViewById(R.id.langBtn);

        //Login Radio Buttons and group
        relativeRadioBtn = findViewById(R.id.relative_login_radio_button);
        doctorRadioBtn = findViewById(R.id.doctor_login_radio_button);
        adminRadioBtn = findViewById(R.id.admin_login_radio_button);
        loginOptionRadio = findViewById(R.id.login_option_radio_group);

        Button loginBtn = (Button) findViewById(R.id.loginBtn);
        progressBar = findViewById(R.id.progress_bar_login);
        dbHelper = new DataBaseHelper(this);
        //dbHelper.dropTable();

        loginBtn.setOnClickListener(v -> {
            String uid = userid.getText().toString();
            if (uid.equals("")) {
                //check if the id number / field is empty.
                SimpleDialog.showAlertDialog(LoginActivity.this, R.string.alert_title_login, R.string.alert_message_idEmtpy);

            } else if (adminRadioBtn.isChecked()) {
                if (uid.equals("admin")) {
                    Intent intent = new Intent(getApplicationContext(), AdministratorActivity.class);
                    startActivity(intent);
                    userid.setText("");
                }
            } else if (doctorRadioBtn.isChecked()) {
                if (uid.equals("doctor")) {
                    Intent intent = new Intent(getApplicationContext(), DoctorActivity.class);
                    startActivity(intent);
                    userid.setText("");
                }
            } else {
                progressBar.setVisibility(View.VISIBLE);
                if (checkIDValidation(uid)) {
                    if (relativeRadioBtn.isChecked()) {
                        query = db.collection("Users").whereEqualTo("id", uid).get();
                    } else {
                        query = db.collection("Elderlies").whereEqualTo("id", uid).get();
                    }
                    query.addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                        @Override
                        public void onComplete(@NonNull @NotNull Task<QuerySnapshot> task) {
                            if (task.isSuccessful()) {
                                auth = FirebaseAuth.getInstance();
                                List<DocumentSnapshot> documentSnapshot = task.getResult().getDocuments();
                                if (!documentSnapshot.isEmpty()) {
                                    DocumentSnapshot snapshot = documentSnapshot.get(0);
                                    auth.signInWithEmailAndPassword(snapshot.get("email").toString(), snapshot.get("password").toString()).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                                        @Override
                                        public void onComplete(@NonNull @NotNull Task<AuthResult> task) {
                                            if (task.isSuccessful()) {
                                                progressBar.setVisibility(View.GONE);
                                                if (relativeRadioBtn.isChecked()) {
                                                    Intent intent = new Intent(LoginActivity.this, UserHomePageActivity.class);
                                                    intent.putExtra("userID", snapshot.get("id").toString());
                                                    intent.putExtra("username", snapshot.get("username").toString());
                                                    intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                                                    startActivity(intent);
                                                    finish();

                                                } else {
                                                    Intent intent = new Intent(LoginActivity.this, HomePageActivity.class);
                                                    intent.putExtra("userID", snapshot.get("id").toString());
                                                    intent.putExtra("username", snapshot.get("username").toString());
                                                    intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                                                    startActivity(intent);
                                                    finish();
                                                }
                                            } else {
                                                progressBar.setVisibility(View.GONE);
                                                Toast.makeText(LoginActivity.this, task.getException().getMessage().toString(), Toast.LENGTH_LONG).show();
                                            }
                                        }
                                    });
                                } else {
                                    progressBar.setVisibility(View.GONE);
                                    Toast.makeText(LoginActivity.this, R.string.alert_message_failed_sign_in, Toast.LENGTH_LONG).show();
                                }
                            } else {
                                progressBar.setVisibility(View.GONE);
                                Toast.makeText(LoginActivity.this, task.getException().getMessage().toString(), Toast.LENGTH_LONG).show();
                            }
                        }
                    });
                } else {
                    progressBar.setVisibility(View.GONE);
                    SimpleDialog.showAlertDialog(LoginActivity.this, R.string.alert_title_login, R.string.alert_message_idEmtpy);
                }
            }
        });

        elderBtn.setOnClickListener(v -> {
            Intent intent = new Intent(getApplicationContext(), ElderSignupActivity.class);
            startActivity(intent);
        });
        userBtn.setOnClickListener(v -> {
            Intent intent = new Intent(getApplicationContext(), UserSignupActivity.class);
            startActivity(intent);
        });

        // Setting onClick behavior to the button
        langBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showLanguageMenu();
            }
        });
    }



    /*
    Since the Real Id Number consists of 9 digits Only, So in this method we check the Id Validation.
    9 numbers only between 0-9.
     */
    public boolean checkIDValidation(String idV) {
        return idV.matches("[0-9]{9}");
    }

    public static User getUser() {
        return user;
    }

    public static Elder getElder() {
        return elder;
    }

    private void showLanguageMenu() {
        // Initializing the popup menu and giving the reference as current context
        PopupMenu popupMenu = new PopupMenu(LoginActivity.this, langBtn);

        // Inflating popup menu from popup_menu.xml file
        popupMenu.getMenuInflater().inflate(R.menu.language_menu, popupMenu.getMenu());
        popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem menuItem) {
                // Toast message on menu item clicked
                Toast.makeText(LoginActivity.this, "You Clicked " + menuItem.getTitle(), Toast.LENGTH_SHORT).show();
                return true;
            }
        });
        // Showing the popup menu
        popupMenu.show();
    }

}