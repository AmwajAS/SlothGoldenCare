package com.example.slothgoldencare;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.*;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.example.slothgoldencare.Model.Elder;
import com.google.firebase.auth.FirebaseAuth;

import java.util.ArrayList;
import java.util.List;

public class DoctorActivity extends AppCompatActivity{
    private static final String TAG = "ADMIN";
    private List<Elder> elderlies;
    private ListView elderliesList;

    private String username;
    private FirebaseAuth auth;
    private DataBaseHelper dbHelper;
    private String userId;
    private SearchView searchBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_doctor);

        auth = FirebaseAuth.getInstance();
        searchBar = findViewById(R.id.searchBar);
        username = getIntent().getStringExtra("username");
        userId = getIntent().getStringExtra("userID");

        dbHelper = new DataBaseHelper(this);
        elderlies = new ArrayList<>();
        elderliesList = findViewById(R.id.elderly_list);
        elderlies = dbHelper.getElders();


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
                TextView idText = convertView.findViewById(R.id.elderly_id);
                idText.setText(elder.getID());

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
                            String filterPattern = constraint.toString().toLowerCase().trim();
                            for (Elder elder : elderlies) {
                                if (elder.getID().toLowerCase().contains(filterPattern)) {
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

}