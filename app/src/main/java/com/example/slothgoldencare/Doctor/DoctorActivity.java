package com.example.slothgoldencare.Doctor;

import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.*;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import androidx.core.app.ActivityCompat;
import com.example.slothgoldencare.DataBaseHelper.DataBaseHelper;
import com.example.slothgoldencare.ElderAdapter;
import com.example.slothgoldencare.Model.Doctor;
import com.example.slothgoldencare.Model.Elder;
import com.example.slothgoldencare.R;
import com.example.slothgoldencare.UserHomePageActivity;
import com.example.slothgoldencare.VisitElderlyProfileActivity;
import com.google.firebase.auth.FirebaseAuth;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.List;


public class DoctorActivity extends AppCompatActivity {
    private static final int CALL_PERMISSION_REQUEST_CODE = 100;


    private static final String TAG = "ADMIN";
    private List<Elder> elderlies;
    private ListView elderliesList;

    private String username;
    private FirebaseAuth auth;
    private DataBaseHelper dbHelper;
    private String userId;
    private Button backBtn;
    private Elder currElder;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_doctor);

        auth = FirebaseAuth.getInstance();
        username = getIntent().getStringExtra("username");
        userId = getIntent().getStringExtra("userID");

        dbHelper = new DataBaseHelper(this);
        elderlies = new ArrayList<>();
        elderliesList = findViewById(R.id.elderly_list);
        elderlies = dbHelper.getElders();



        // back btn to remove the replaced view to the main one.
        backBtn = findViewById(R.id.back_btn);
        backBtn.setOnClickListener(view1 -> {
            Intent intent = new Intent(DoctorActivity.this, DoctorActivityMain.class);
            startActivity(intent);
        });
       ArrayAdapter<Elder> elderAdapter = new ArrayAdapter<Elder>(this, R.layout.doctor_elderly_item, elderlies) {
            @NonNull
            @Override
            public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {


                // Get the user object for the current position
                Elder elder = getItem(position);

                // Inflate the list item layout
                if (convertView == null) {
                    convertView = LayoutInflater.from(getContext()).inflate(R.layout.doctor_elderly_item, parent, false);
                }

                //handle variables in the elderly item
                TextView idText = convertView.findViewById(R.id.elderly_id);
                TextView usernameText = convertView.findViewById(R.id.elderly_username);
                idText.setText(elder.getID());
                usernameText.setText(elder.getUsername());


                //call elderly function using phone
                ImageButton callBtn = convertView.findViewById(R.id.call_btn);
                callBtn.setOnClickListener(view -> {
                    currElder = elder;
                    callElder(elder);
                });
                convertView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Intent intent = new Intent(getContext(), DoctorVisitElderlyActivity.class);
                        intent.putExtra("elderlyId",elder.getID());
                        intent.putExtra("doctorId",auth.getUid());
                        startActivity(intent);
                    }
                });
                return convertView;
            }
        };
        elderliesList.setAdapter(elderAdapter);

    }
    private void callElder(Elder elder) {
        // Check if the CALL_PHONE permission is granted
        if (ActivityCompat.checkSelfPermission(this, android.Manifest.permission.CALL_PHONE) == PackageManager.PERMISSION_GRANTED) {
            // Create an intent to make a phone call
            Intent callIntent = new Intent(Intent.ACTION_CALL);
            callIntent.setData(Uri.parse("tel:" + elder.getPhoneNumber()));

            // Check if the device has a calling app to handle the intent
            if (callIntent.resolveActivity(getPackageManager()) != null) {
                startActivity(callIntent);
            } else {
                // If no calling app is available, show a message or handle the situation accordingly
                Toast.makeText(this, "No calling app available.", Toast.LENGTH_SHORT).show();
            }
        } else {
            // Request the CALL_PHONE permission if not granted
            ActivityCompat.requestPermissions(this, new String[]{android.Manifest.permission.CALL_PHONE}, CALL_PERMISSION_REQUEST_CODE);
        }
    }

    // Handle the result of the permission request
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == CALL_PERMISSION_REQUEST_CODE) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                // Permission granted, call the doctor again
                callElder(currElder);
            } else {
                // Permission denied, show a message or handle the situation accordingly
                Toast.makeText(this, "Call permission denied.", Toast.LENGTH_SHORT).show();
            }
        }
    }
}