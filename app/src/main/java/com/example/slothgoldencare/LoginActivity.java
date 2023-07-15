package com.example.slothgoldencare;

import android.widget.CheckBox;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
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
    private CheckBox relativeCheckBox;
    private Task<QuerySnapshot> query;

    public static boolean flag;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        db = FirebaseFirestore.getInstance();
        setContentView(R.layout.activity_login);
        Button elderBtn = (Button) findViewById(R.id.elderBtn);
        Button userBtn = (Button) findViewById(R.id.userBtn);
        userid = (EditText) findViewById(R.id.userid);
        relativeCheckBox = (CheckBox) findViewById(R.id.relative_login_checkbox);
        Button loginBtn = (Button) findViewById(R.id.loginBtn);
        dbHelper = new DataBaseHelper(this);
        //dbHelper.dropTable();

        loginBtn.setOnClickListener(v -> {
                    String uid = userid.getText().toString();
                    if (uid.equals("")) {
                        //check if the id number / field is empty.
                        SimpleDialog.showAlertDialog(LoginActivity.this, R.string.alert_title_login, R.string.alert_message_idEmtpy);

                    } else if (uid.equals("admin")) {
                        Intent intent = new Intent(getApplicationContext(), AdministratorActivity.class);
                        startActivity(intent);
                        userid.setText("");

                    } else {
                        if (checkIDValidation(uid)) {
                            if(relativeCheckBox.isChecked()){
                                query = db.collection("Users").whereEqualTo("id",uid).get();
                            }
                            else {
                                query = db.collection("Elderlies").whereEqualTo("id", uid).get();
                            }
                               query.addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                                    @Override
                                    public void onComplete(@NonNull @NotNull Task<QuerySnapshot> task) {
                                        if (task.isSuccessful()) {
                                            auth = FirebaseAuth.getInstance();
                                            List<DocumentSnapshot> documentSnapshot = task.getResult().getDocuments();
                                            DocumentSnapshot snapshot = documentSnapshot.get(0);
                                            auth.signInWithEmailAndPassword(snapshot.get("email").toString(), snapshot.get("password").toString()).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                                                @Override
                                                public void onComplete(@NonNull @NotNull Task<AuthResult> task) {
                                                    if (task.isSuccessful()) {
                                                        if(relativeCheckBox.isChecked()){
                                                            Intent intent = new Intent(LoginActivity.this, UserHomePageActivity.class);
                                                            intent.putExtra("userID", snapshot.get("id").toString());
                                                            intent.putExtra("username", snapshot.get("username").toString());
                                                            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                                                            startActivity(intent);
                                                            finish();

                                                        }else{
                                                            Intent intent = new Intent(LoginActivity.this, HomePageActivity.class);
                                                            intent.putExtra("userID", snapshot.get("id").toString());
                                                            intent.putExtra("username", snapshot.get("username").toString());
                                                            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                                                            startActivity(intent);
                                                            finish();
                                                        }
                                                    } else {
                                                        Toast.makeText(LoginActivity.this, task.getException().getMessage().toString(), Toast.LENGTH_LONG).show();
                                                    }
                                                }
                                            });
                                        } else {
                                            Toast.makeText(LoginActivity.this, task.getException().getMessage().toString(), Toast.LENGTH_LONG).show();
                                        }
                                    }
                                });
                        }else{
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

                }

    /*
    Since the Real Id Number consists of 9 digits Only, So in this method we check the Id Validation.
    9 numbers only between 0-9.
     */
        public boolean checkIDValidation (String idV){
            return idV.matches("[0-9]{9}");
        }

        public static User getUser () {
            return user;
        }

        public static Elder getElder () {
            return elder;
        }

        public static boolean getFlag () {
            return flag;
        }
    }