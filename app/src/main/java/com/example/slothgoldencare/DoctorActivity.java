package com.example.slothgoldencare;

import android.content.Intent;
import android.nfc.Tag;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.*;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;
import java.util.List;

public class DoctorActivity extends AppCompatActivity{
    private static final String TAG = "ADMIN";

    private List<Elder> elderlies;
    private ListView elderliesList;
    private SearchView searchView;

    private DataBaseHelper dbHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_doctor);
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
                        Intent intent = new Intent(getApplicationContext(),DoctorVisitElderlyActivity.class);
                        intent.putExtra("elderlyId",elder.getID());
                        startActivity(intent);
                        Toast.makeText(getContext(), "Elder ID: " + elder.getID() + " is clicked.", Toast.LENGTH_SHORT).show();
                    }
                });

                return convertView;
            }
        };
        elderliesList.setAdapter(elderAdapter);
    }
}

