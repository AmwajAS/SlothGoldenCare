package com.example.slothgoldencare;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.net.Uri;
import android.provider.Settings;
import android.widget.ImageButton;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Bundle;
import java.util.*;
import android.view.View;
import android.widget.Toast;
import com.example.slothgoldencare.Model.HealthTip;
import com.example.slothgoldencare.Model.User;
import com.example.slothgoldencare.Reminder.TODOActivity;
import com.example.slothgoldencare.sudoko.GameActivity;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.auth.FirebaseAuth;
import java.util.List;

public class HomePageActivity extends AppCompatActivity implements View.OnClickListener {
    private FusedLocationProviderClient fusedLocationProviderClient;
    private LocationCallback locationCallback;
    private ProgressDialog progressDialog;
    private List<HealthTip> healthTipList;
    private FirebaseAuth auth;
    private List<User> relativesArrayList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home_page);
        auth = FirebaseAuth.getInstance();
        DataBaseHelper dbHelper = new DataBaseHelper(this);

        TextView editTextUsername = findViewById(R.id.username);

        healthTipList = new ArrayList<>();
        healthTipList = dbHelper.getHealthTips();

        //relatives of elderly
        relativesArrayList = new ArrayList<>();
        relativesArrayList = dbHelper.GetRelativesByElderly(auth.getUid());

        editTextUsername.setText(auth.getCurrentUser().getDisplayName());

        //Showing Health Tip when clicking on the icon health Tip.
        //choosing a random health tip from the list
        ImageButton healthTipBut = findViewById(R.id.health_tip_button);
        healthTipBut.setOnClickListener(view -> {
            Random random = new Random();
            int listSize = healthTipList.size();
            int randomIndex = random.nextInt(listSize);
            HealthTip randomHealthTip = healthTipList.get(randomIndex);
            showHealthTipContent(randomHealthTip);
        });

        //Initializing card views for click listener
        CardView d1 = findViewById(R.id.d1);
        CardView d2 = findViewById(R.id.d2);
        CardView d3 = findViewById(R.id.d3);
        CardView d4 = findViewById(R.id.d4);
        CardView d5 = findViewById(R.id.d5);
        CardView d6 = findViewById(R.id.d6);
        CardView d7 = findViewById(R.id.d7);

        d1.setOnClickListener(this);
        d2.setOnClickListener(this);
        d3.setOnClickListener(this);
        d4.setOnClickListener(this);
        d5.setOnClickListener(this);
        d6.setOnClickListener(this);
        d7.setOnClickListener(this);

        //Location of user
        fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(this);
        locationCallback = new LocationCallback() {
            @Override
            public void onLocationResult(LocationResult locationResult) {
                Location location = locationResult.getLastLocation();
                if (location != null) {
                    double latitude = location.getLatitude();
                    double longitude = location.getLongitude();
                    LatLng userLocation = new LatLng(latitude, longitude);
                    saveUserLocation(userLocation);
                }
            }
        };
    }

    @Override
    public void onClick(View v) {
        Intent i;
        switch (v.getId()) {
            case R.id.d1:
                i = new Intent(this, MedicalServices.class);
                startActivity(i);
                break;
            case R.id.d2:
                i = new Intent(this, TODOActivity.class);
                startActivity(i);
                break;
            case R.id.d3:
                i = new Intent(this, GameActivity.class);
                startActivity(i);
                break;
            case R.id.d4:
                i = new Intent(this, ContactsActivity.class);
                startActivity(i);
                break;
            case R.id.d5:
                //SOS button
                progressDialog = new ProgressDialog(HomePageActivity.this);
                progressDialog.setMessage("Loading..."); // Set your desired loading message
                progressDialog.setCancelable(false); // Set whether the dialog can be canceled
                progressDialog.show();
                retrieveUserLocation();
                break;
            case R.id.d6:
                i = new Intent(this, SettingsActivity.class);
                startActivity(i);
                break;
            case R.id.d7:
                i = new Intent(this, AppointmentSchedulingActivity.class);
                startActivity(i);
                break;
        }
    }

    private void retrieveUserLocation() {
        //retrieving user location
        if (checkSelfPermission(Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED
                && checkSelfPermission(Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            fusedLocationProviderClient.requestLocationUpdates(createLocationRequest(), locationCallback, null);
        } else {
            //if there is no permission for location
            Toast.makeText(this, "Location permission not granted", Toast.LENGTH_SHORT).show();
            showPermissionDeniedDialog();
        }
    }

    private LocationRequest createLocationRequest() {
        LocationRequest locationRequest = LocationRequest.create();
        locationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
        locationRequest.setInterval(10000); // Update interval in milliseconds
        return locationRequest;
    }

    private void saveUserLocation(LatLng location) {
        // Save the user's location here
        double latitude = location.latitude;
        double longitude = location.longitude;
        // Perform the necessary operations to save the location
        Toast.makeText(this, "Location saved - Lat: " + latitude + ", Lng: " + longitude, Toast.LENGTH_SHORT).show();
        sendLocationToRelatives(location);
        onStop();
    }

    //Sending the location to the relatives via phone number
    private void sendLocationToRelatives(LatLng location) {
        // Iterate through the ArrayList of relatives
        for (User relative : relativesArrayList) {
            // Check if the relative has a phone number
            if (relative.getPhoneNumber() != null && !relative.getPhoneNumber().isEmpty()) {
                String mapLink = "https://maps.google.com/maps?q=" + location.latitude + "," + location.longitude;
                String smsMessage = auth.getCurrentUser().getDisplayName()+" Sent An Emergency Location : Click on the link to view the location on the map: " + mapLink;
                SmsUtil.sendSms(relative.getPhoneNumber(), smsMessage);
            }
        }
        Snackbar.make(getWindow().getDecorView(),"Messages sent!",Snackbar.LENGTH_LONG).show();
        progressDialog.dismiss();
    }




    @Override
    protected void onStop() {
        super.onStop();
        fusedLocationProviderClient.removeLocationUpdates(locationCallback);
    }
    private void showPermissionDeniedDialog() {
        //Permissiong dialog for location
        progressDialog.dismiss();
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Permission Denied");
        builder.setMessage("Location permission has been denied. You can enable it in the application settings.");
        builder.setPositiveButton("Settings", (dialog, which) -> openGameSettings("com.example.slothgoldencare"));
        builder.setNegativeButton("Cancel", (dialog, which) -> {
            // Handle cancel button click
        });
        builder.show();
    }
    //Open settings to do the permission
    private void openGameSettings(String gamePackageName) {
        Intent intent = new Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
        intent.setData(Uri.fromParts("package", gamePackageName, null));
        startActivity(intent);
    }

    //function to show health tip
    private void showHealthTipContent(HealthTip healthTip) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle(healthTip.getTitle());
        builder.setMessage(healthTip.getContent());
        builder.setPositiveButton("OK", null);
        AlertDialog dialog = builder.create();
        dialog.show();
    }



}

