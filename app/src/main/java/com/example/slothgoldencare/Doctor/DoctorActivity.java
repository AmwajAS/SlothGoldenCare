package com.example.slothgoldencare.Doctor;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.*;

import androidx.appcompat.app.AppCompatActivity;

import com.example.slothgoldencare.DataBaseHelper.DataBaseHelper;
import com.example.slothgoldencare.Model.Elder;
import com.example.slothgoldencare.R;
import com.google.firebase.auth.FirebaseAuth;

import java.util.ArrayList;
import java.util.List;


public class DoctorActivity extends AppCompatActivity implements SearchView.OnQueryTextListener {
    private static final String TAG = "ADMIN";
    private List<Elder> elderlies;
    private ListView elderliesList;

    private String username;
    private FirebaseAuth auth;
    private DataBaseHelper dbHelper;
    private String userId;
    private SearchView searchBar;
    private Button backBtn;

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

        ElderListAdapter elderAdapter = new ElderListAdapter(this, elderlies);
        elderliesList.setAdapter(elderAdapter);


        // back btn to remove the replaced view to the main one.
        backBtn = findViewById(R.id.back_btn);
        backBtn.setOnClickListener(view1 -> {
            Intent intent = new Intent(DoctorActivity.this, DoctorActivityMain.class);
            startActivity(intent);
        });


        searchBar.setOnQueryTextListener(this);
        searchBar.setOnCloseListener(new SearchView.OnCloseListener() {
            @Override
            public boolean onClose() {
                elderAdapter.clearFilter();
                return false;
            }
        });

        elderliesList.setOnItemClickListener((parent, view, position, id) -> {
            Elder elder = elderAdapter.getItem(position);
            if (elder != null) {
                Intent intent = new Intent(getApplicationContext(), DoctorVisitElderlyActivity.class);
                intent.putExtra("elderlyId", elder.getID());
                startActivity(intent);
                Toast.makeText(DoctorActivity.this, "Elder ID: " + elder.getID() + " is clicked.", Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    public boolean onQueryTextSubmit(String query) {
        return false;
    }

    @Override
    public boolean onQueryTextChange(String newText) {
        ElderListAdapter elderAdapter = (ElderListAdapter) elderliesList.getAdapter();
        if (elderAdapter != null) {
            elderAdapter.getFilter().filter(newText);
        }
        return false;
    }
}

class ElderListAdapter extends BaseAdapter implements Filterable {
    private List<Elder> originalData;
    private List<Elder> filteredData;
    private LayoutInflater inflater;
    private ElderFilter filter = new ElderFilter();

    public ElderListAdapter(Context context, List<Elder> data) {
        this.originalData = data;
        this.filteredData = data;
        this.inflater = LayoutInflater.from(context);
    }

    @Override
    public int getCount() {
        return filteredData.size();
    }

    @Override
    public Elder getItem(int position) {
        return filteredData.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View view = convertView;
        if (view == null) {
            view = inflater.inflate(R.layout.doctor_elderly_item, parent, false);
        }

        Elder elder = getItem(position);

        TextView idText = view.findViewById(R.id.elderly_id);
        idText.setText(elder.getID());

        TextView usernameText = view.findViewById(R.id.elderly_username);
        usernameText.setText(elder.getUsername());

        return view;
    }

    @Override
    public Filter getFilter() {
        return filter;
    }

    public void clearFilter() {
        filteredData = originalData;
        notifyDataSetChanged();
    }

    private class ElderFilter extends Filter {
        @Override
        protected FilterResults performFiltering(CharSequence constraint) {
            FilterResults results = new FilterResults();

            if (constraint == null || constraint.length() == 0) {
                results.values = originalData;
                results.count = originalData.size();
            } else {
                List<Elder> filteredList = new ArrayList<>();
                String filterPattern = constraint.toString().toLowerCase().trim();
                for (Elder elder : originalData) {
                    if (elder.getID().contains(filterPattern)) {
                        filteredList.add(elder);
                    }
                }
                results.values = filteredList;
                results.count = filteredList.size();
            }

            return results;
        }

        @Override
        protected void publishResults(CharSequence constraint, FilterResults results) {
            filteredData = (List<Elder>) results.values;
            notifyDataSetChanged();
        }
    }
}