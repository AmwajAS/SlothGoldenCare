package com.example.slothgoldencare;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.net.Uri;
import android.provider.Settings;
import android.widget.EditText;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import com.example.slothgoldencare.Reminder.TODOActivity;
import com.example.slothgoldencare.sudoko.GameActivity;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.model.LatLng;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;

public class HomePageActivity extends AppCompatActivity implements View.OnClickListener {
    private CardView D1, D2, D3, D4, D5, D6;
    private FusedLocationProviderClient fusedLocationProviderClient;
    private LocationCallback locationCallback;
    private ProgressDialog progressDialog;
    private TextView editTextUsername;
    private String username;
    private FirebaseAuth auth;
    private FirebaseFirestore db;
    private String userId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        auth = FirebaseAuth.getInstance();
        setContentView(R.layout.activity_home_page);
        editTextUsername = findViewById(R.id.username);
        username = getIntent().getStringExtra("username");
        userId = getIntent().getStringExtra("userID");

        editTextUsername.setText(auth.getCurrentUser().getDisplayName());

        D1 = findViewById(R.id.d1);
        D2 = findViewById(R.id.d2);
        D3 = findViewById(R.id.d3);
        D4 = findViewById(R.id.d4);
        D5 = findViewById(R.id.d5);
        D6 = findViewById(R.id.d6);

        D1.setOnClickListener(this);
        D2.setOnClickListener(this);
        D3.setOnClickListener(this);
        D4.setOnClickListener(this);
        D5.setOnClickListener(this);
        D6.setOnClickListener(this);

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
                i = new Intent(this, cardView1InHomePage.class);
                startActivity(i);
                break;
            case R.id.d2:
                i = new Intent(this, TODOActivity.class);
                startActivity(i);
                break;
            case R.id.d5:
                progressDialog = new ProgressDialog(HomePageActivity.this);
                progressDialog.setMessage("Loading..."); // Set your desired loading message
                progressDialog.setCancelable(false); // Set whether the dialog can be canceled
                progressDialog.show();
                retrieveUserLocation();
                break;
            case R.id.d6:
                i = new Intent(this, BottomNavigationBarActivity.class);
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
        }
    }

    private void retrieveUserLocation() {
        if (checkSelfPermission(Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED
                && checkSelfPermission(Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            fusedLocationProviderClient.requestLocationUpdates(createLocationRequest(), locationCallback, null);
        } else {

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
        progressDialog.dismiss();
        Toast.makeText(this, "Location saved - Lat: " + latitude + ", Lng: " + longitude, Toast.LENGTH_SHORT).show();
        onStop();
    }

    @Override
    protected void onStop() {
        super.onStop();
        fusedLocationProviderClient.removeLocationUpdates(locationCallback);
    }
    private void showPermissionDeniedDialog() {
        progressDialog.dismiss();
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Permission Denied");
        builder.setMessage("Location permission has been denied. You can enable it in the application settings.");
        builder.setPositiveButton("Settings", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                openGameSettings("com.example.slothgoldencare");
            }
        });
        builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                // Handle cancel button click
            }
        });
        builder.show();
    }
    private void openGameSettings(String gamePackageName) {
        Intent intent = new Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
        intent.setData(Uri.fromParts("package", gamePackageName, null));
        startActivity(intent);
    }

}

