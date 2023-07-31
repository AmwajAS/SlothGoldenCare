package com.example.slothgoldencare;

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
import com.example.slothgoldencare.Model.Elder;
import com.google.firebase.auth.FirebaseAuth;

import java.util.ArrayList;
import java.util.List;

public class DoctorActivity extends AppCompatActivity{
    private static final int CALL_PERMISSION_REQUEST_CODE = 100;

    private static final String TAG = "ADMIN";
    private List<Elder> elderlies;
    private ListView elderliesList;

    private String username;
    private FirebaseAuth auth;
    private DataBaseHelper dbHelper;
    private Elder currElder;
    private SearchView searchBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_doctor);


        auth = FirebaseAuth.getInstance();
        searchBar = findViewById(R.id.searchBar);
        username = getIntent().getStringExtra("username");

        //getting elderlies from databaseHelper
        dbHelper = new DataBaseHelper(this);
        elderlies = new ArrayList<>();
        elderliesList = findViewById(R.id.elderly_list);
        elderlies = dbHelper.getElders();


        //building the list of the elderlies
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
                idText.setText(elder.getID());

                //call elderly function using phone
                ImageButton callBtn = convertView.findViewById(R.id.call_btn);
                callBtn.setOnClickListener(view -> {
                    currElder = elder;
                    callElder(elder);
                });

                //set the list item clickable to open the profile of the elderly item.
                //opening new activity for the visited elderly
                convertView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Intent intent = new Intent(getApplicationContext(), DoctorVisitElderlyActivity.class);
                        intent.putExtra("elderlyId", elder.getID());
                        startActivity(intent);
                        Toast.makeText(getContext(), "Elder ID: " + elder.getID() + " is clicked.", Toast.LENGTH_SHORT).show();
                    }
                });

                return convertView;
            }

            @NonNull
            @Override
            public Filter getFilter() {
                Filter filter = new Filter() {
                    @Override
                    protected FilterResults performFiltering(CharSequence constraint) {
                        FilterResults results = new FilterResults();
                        List<Elder> filteredList = new ArrayList<>();

                        if (constraint == null || constraint.length() == 0) {
                            // If the search bar is empty, show the original data
                            filteredList.addAll(elderlies);
                        } else {
                            // If there's a query, apply custom filtering for partial matches
                            String filterPattern = constraint.toString();
                            for (Elder elder : elderlies) {
                                if (elder.getID().contains(filterPattern)) {
                                    filteredList.add(elder);
                                }
                            }
                        }

                        results.values = filteredList;
                        results.count = filteredList.size();
                        return results;
                    }

                    @Override
                    protected void publishResults(CharSequence constraint, FilterResults results) {
                        clear();
                        addAll((List<Elder>) results.values);
                        notifyDataSetChanged();
                    }
                };
                return filter;
            }
        };
        elderliesList.setAdapter(elderAdapter);

        searchBar.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                if (newText.isEmpty()) {
                    // If the search bar is empty, refresh the list by setting the original data
                    elderAdapter.clear();
                    elderAdapter.addAll(elderlies);
                    elderAdapter.notifyDataSetChanged();
                } else {
                    // If there's a query, apply filtering
                    elderAdapter.getFilter().filter(newText);
                }
                return false;
            }
        });
        // Set the OnCloseListener on the SearchView
        searchBar.setOnCloseListener(new SearchView.OnCloseListener() {
            @Override
            public boolean onClose() {
                // If the user clicks the close button (X), show all elderlies
                elderAdapter.clear();
                elderAdapter.addAll(elderlies);
                elderAdapter.notifyDataSetChanged();
                return false;
            }
        });


    }
    //call elderly function, when the button is clicked it opens a call in phone.
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