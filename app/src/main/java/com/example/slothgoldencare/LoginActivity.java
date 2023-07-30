package com.example.slothgoldencare;

import android.content.res.Configuration;
import android.util.Log;
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
import java.util.Locale;

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
    private PopupMenu popupMenu;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.d(TAG, "onCreate() called");
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
            } else {
                progressBar.setVisibility(View.VISIBLE);
                if (checkIDValidation(uid)) {
                    if (relativeRadioBtn.isChecked()) {
                        query = db.collection("Users").whereEqualTo("id", uid).get();
                    } else if (doctorRadioBtn.isChecked()) {
                        query = db.collection("Doctors").whereEqualTo("id", uid).get();
                        query.addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                            @Override
                            public void onComplete(@NonNull @NotNull Task<QuerySnapshot> task) {
                                Intent intent = new Intent(LoginActivity.this, DoctorActivityMain.class);
                                intent.putExtra("doctorUid", uid);
                                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                                startActivity(intent);
                                finish();
                            }
                        });
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

                                                }
//                                                else if(doctorRadioBtn.isChecked()){
//                                                    Intent intent = new Intent(LoginActivity.this, DoctorActivity.class);
//                                                    intent.putExtra("userID", snapshot.get("id").toString());
//                                                    intent.putExtra("username", snapshot.get("username").toString());
//                                                    intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
//                                                    startActivity(intent);
//                                                    finish();
//                                                }
                                                else {
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

    /*

    This method is responsible for showing the language selection menu as a PopupMenu when
     the language button (langBtn) is clicked. It inflates the menu layout (R.menu.language_menu)
     and handles the click events for each menu item to determine the selected language.
     When a language is selected, it calls the changeLanguage() method to update the app's locale.
     */
    private void showLanguageMenu() {
        Log.d(TAG, "showLanguageMenu() called"); // Add this log message

        // Initializing the popup menu and giving the reference as current context
        popupMenu = new PopupMenu(LoginActivity.this, langBtn);

        // Inflating popup menu from popup_menu.xml file
        popupMenu.getMenuInflater().inflate(R.menu.language_menu, popupMenu.getMenu());

        popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem menuItem) {
                // Toast message on menu item clicked
                String selectedLang = "";

                if (menuItem.getTitle().equals("Hebrew")) {
                    selectedLang = "iw";

                } else if (menuItem.getTitle().equals("Arabic")) {
                    selectedLang = "ar";

                } else if (menuItem.getTitle().equals("English")) {
                    selectedLang = "en";

                } else if (menuItem.getTitle().equals("Russian")) {
                    selectedLang = "ru";

                }
                changeLanguage(selectedLang);
                return true;
            }
        });

        // Set the OnDismissListener to handle cleanup when the PopupMenu is dismissed
        popupMenu.setOnDismissListener(new PopupMenu.OnDismissListener() {
            @Override
            public void onDismiss(PopupMenu menu) {
                // Do any necessary cleanup here (if required)
                popupMenu = null; // Clear the reference to the PopupMenu
            }
        });

        // Showing the popup menu
        popupMenu.show();
    }

    /*

    This is a method from the AppCompatActivity class that we have overridden. It gets called when the activity is being destroyed.
    we are checking if the PopupMenu (popupMenu) is still showing and dismiss it to avoid any memory leaks.
     */
    @Override
    protected void onDestroy() {
        super.onDestroy();

        // Dismiss the PopupMenu if it is still showing
        if (popupMenu != null) {
            popupMenu.dismiss();
        }
    }

    /*
    This method is responsible for changing the language of the application.
    It takes a language code ("iw" for Hebrew, "ar" for Arabic, "en" for English and "ru" for Russian)
    as input and sets it as the default locale for the application context and the current activity.
    Then, it calls the recreate() method to reload the activity, which will apply the language changes.
     */
    private void changeLanguage(String languageCode) {
        Locale locale = new Locale(languageCode);
        Log.d(TAG, "changeLanguage() called with languageCode: " + languageCode); // Add this log message
        Locale.setDefault(locale);

        Configuration configuration = new Configuration();
        configuration.setLocale(locale);

        // Update the configuration for the application context
        getApplicationContext().getResources().updateConfiguration(configuration, getApplicationContext().getResources().getDisplayMetrics());

        // Update the configuration for the current activity
        getResources().updateConfiguration(configuration, getResources().getDisplayMetrics());

        // Reload the activity to apply language changes
        recreate();
    }

}

