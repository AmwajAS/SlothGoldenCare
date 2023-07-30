package com.example.slothgoldencare;

import android.content.res.Configuration;
import android.util.Log;
import android.view.View;
import android.widget.*;
import androidx.appcompat.app.AppCompatActivity;
import android.content.Intent;
import android.os.Bundle;
import com.example.slothgoldencare.Model.Doctor;
import com.example.slothgoldencare.Model.Elder;
import com.example.slothgoldencare.Model.User;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import java.util.List;
import java.util.Locale;
import java.util.Objects;

public class LoginActivity extends AppCompatActivity {

    private static final String TAG = "LoginActivity";

    private EditText userid;
    DataBaseHelper dbHelper;
    private FirebaseAuth auth;
    private RadioButton relativeRadioBtn;
    private RadioButton doctorRadioBtn;
    private RadioButton adminRadioBtn;
    private ProgressBar progressBar;
    private ImageButton langBtn;
    private PopupMenu popupMenu;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //Firebase initialization
        auth = FirebaseAuth.getInstance();

        setContentView(R.layout.activity_login);

        Button elderBtn =  findViewById(R.id.elderBtn);
        Button userBtn =  findViewById(R.id.userBtn);
        userid =  findViewById(R.id.userid);
        langBtn =  findViewById(R.id.langBtn);

        //Login Radio Buttons and group
        relativeRadioBtn = findViewById(R.id.relative_login_radio_button);
        doctorRadioBtn = findViewById(R.id.doctor_login_radio_button);
        adminRadioBtn = findViewById(R.id.admin_login_radio_button);

        Button loginBtn = findViewById(R.id.loginBtn);
        progressBar = findViewById(R.id.progress_bar_login);

        dbHelper = new DataBaseHelper(this);
        //Saved lists of all the users.
        List<User> usersList = dbHelper.getUsers();
        List<Elder> eldersList = dbHelper.getElders();
        List<Doctor> doctorsList = dbHelper.getDoctors();

        //Log in button function.
        loginBtn.setOnClickListener(v -> {
            String uid = userid.getText().toString();
            if (uid.equals("")) {
                //check if the id number / field is empty.
                SimpleDialog.showAlertDialog(LoginActivity.this, R.string.alert_title_login, R.string.alert_message_idEmtpy);
            //Check if log in as admin
            } else if (adminRadioBtn.isChecked()) {
                if (uid.equals("admin")) {
                    Intent intent = new Intent(getApplicationContext(), AdministratorActivity.class);
                    startActivity(intent);
                    userid.setText("");
                }
            } else {
                progressBar.setVisibility(View.VISIBLE);
                //checking if id is valid
                if (checkIDValidation(uid)) {
                    //checking if log in as doctor
                    if (doctorRadioBtn.isChecked()) {
                        for(Doctor doctor : doctorsList){
                            if(doctor.getID().equals(uid)){
                                progressBar.setVisibility(View.GONE);
                                Intent intent = new Intent(LoginActivity.this, DoctorActivityMain.class);
                                intent.putExtra("doctorUid", uid);
                                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                                startActivity(intent);
                                finish();
                            }
                        }
                        progressBar.setVisibility(View.GONE);
                        Snackbar.make(getWindow().getDecorView(), R.string.alert_message_failed_sign_in, Snackbar.LENGTH_LONG).show();
                    }
                    //check if log in as relative
                    else if (relativeRadioBtn.isChecked()) {
                        for(User user : usersList){
                            if(user.getID().equals(uid)){
                                //sign in with Authentication if user found
                                auth.signInWithEmailAndPassword(user.getEmail(),user.getPassword()).addOnCompleteListener(task -> {
                                    if(task.isSuccessful()){
                                        //if successful, call UpdateUI function for relative.
                                        progressBar.setVisibility(View.GONE);
                                        UpdateUI(null,auth.getUid());
                                    }else{
                                        progressBar.setVisibility(View.GONE);
                                        Snackbar.make(getWindow().getDecorView(), Objects.requireNonNull(Objects.requireNonNull(task.getException()).getMessage()), Snackbar.LENGTH_LONG).show();
                                    }
                                });
                            }
                        }
                        progressBar.setVisibility(View.GONE);
                        Snackbar.make(getWindow().getDecorView(), R.string.alert_message_failed_sign_in, Snackbar.LENGTH_LONG).show();
                    } else  {
                        //if no radio is checked or elderly is checked then log in as elderly.
                        for(Elder elder : eldersList){
                            if(elder.getID().equals(uid)){
                                //sign in with Authentication if user found
                                auth.signInWithEmailAndPassword(elder.getEmail(),elder.getPassword()).addOnCompleteListener(task -> {
                                    //if successful, call UpdateUI function for elderly.
                                    if(task.isSuccessful()){
                                        progressBar.setVisibility(View.GONE);
                                        UpdateUI(auth.getUid(),null);
                                    }else{
                                        progressBar.setVisibility(View.GONE);
                                        Snackbar.make(getWindow().getDecorView(), Objects.requireNonNull(Objects.requireNonNull(task.getException()).getMessage()), Snackbar.LENGTH_LONG).show();
                                    }
                                });
                            }
                        }
                        progressBar.setVisibility(View.GONE);
                        Snackbar.make(getWindow().getDecorView(), R.string.alert_message_failed_sign_in, Snackbar.LENGTH_LONG).show();
                    }
                } else {
                    //ID not valid.
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
        langBtn.setOnClickListener(view -> showLanguageMenu());
    }

    //On start function to check if there is a user already logged in
    //and update the UI.
    @Override
    protected void onStart() {
        super.onStart();
        FirebaseUser currUser = auth.getCurrentUser();
        if(currUser != null){
            Elder elder = dbHelper.getElderByDocumentId(currUser.getUid());
            User user = dbHelper.getUserByDocumentId(currUser.getUid());
            if(elder != null){
                UpdateUI(elder.getDocId(),null);
            }else if (user != null){
                UpdateUI(null,user.getDocId());
            }
        }
    }
    //UpdateUI function that opens the relevant page.
    public void UpdateUI(String elderDocId, String userDocId){
        if(elderDocId != null){
            Intent intent = new Intent(LoginActivity.this, HomePageActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(intent);
            finish();
        }else if(userDocId != null) {
            Intent intent = new Intent(LoginActivity.this, UserHomePageActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(intent);
            finish();
        }

    }

    /*
        Since the Real ID Number consists of 9 digits Only, So in this method we check the ID Validation.
        9 numbers only between 0-9.
         */
    public boolean checkIDValidation(String idV) {
        return idV.matches("[0-9]{9}");
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

        popupMenu.setOnMenuItemClickListener(menuItem -> {
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
        });

        // Set the OnDismissListener to handle cleanup when the PopupMenu is dismissed
        popupMenu.setOnDismissListener(menu -> {
            // Do any necessary cleanup here (if required)
            popupMenu = null; // Clear the reference to the PopupMenu
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
    Then, it calls the re,create() method to reload the activity, which will apply the language changes.
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

