package com.example.slothgoldencare;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.bottomnavigation.BottomNavigationView;

import java.util.Calendar;

public class AppointmentSchedulingActivity extends AppCompatActivity {

        @Override
        protected void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            setContentView(R.layout.activity_appointment_scheduling);

            DatePicker datePicker = findViewById(R.id.datePicker);
            Button nextDateButton = findViewById(R.id.nextButton1);
            Button listButton = findViewById(R.id.listButton);

            listButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(AppointmentSchedulingActivity.this, AppointmentsListActivity.class);
                    startActivity(intent);
                }
            });
            nextDateButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    // Get the selected date from DatePicker
                    int year = datePicker.getYear();
                    int month = datePicker.getMonth();
                    int day = datePicker.getDayOfMonth();

                    // Create a Calendar instance for the selected date
                    Calendar selectedDateCalendar = Calendar.getInstance();
                    selectedDateCalendar.set(year, month, day);

                    // Get the current date and time
                    Calendar currentDateCalendar = Calendar.getInstance();

                    // Check if the selected date is before the current date
                    if (selectedDateCalendar.before(currentDateCalendar)) {
                        // Show a toast message to pick the right date
                        Toast.makeText(AppointmentSchedulingActivity.this, "Please select a date that is after the current date", Toast.LENGTH_SHORT).show();
                    } else {
                        // Start the next activity (TimeActivity) and pass the selected date and time as a Timestamp extra
                        Intent intent = new Intent(AppointmentSchedulingActivity.this, TimeActivity.class);
                        intent.putExtra("selectedDate", year + "-" + (month + 1) + "-" + day);
                        startActivity(intent);
                    }
                }
            });
        }


    /*
 this method handle the selected items / buttons of the bottom navigation bar.
  */
    public void bottomNavigationView() {
        BottomNavigationView bottomNavigationView = findViewById(R.id.bottomNavigation);
        bottomNavigationView.setSelectedItemId(R.id.current);
        Menu menu = bottomNavigationView.getMenu();
        MenuItem currentItem = menu.findItem(R.id.current);
        // Hiding the "current" menu item
        currentItem.setVisible(false);

        bottomNavigationView.setOnItemSelectedListener(item -> {
            switch (item.getItemId()) {
                case R.id.home:
                    replaceView(HomePageActivity.class);
                    return true;
                case R.id.settings:
                    replaceView(SettingsActivity.class);
                    return true;
                case R.id.profile:
                    replaceView(ProfileActivity.class);
                    return true;
            }
            return false;
        });
    }

    public void replaceView(Class classView) {
        startActivity(new Intent(getApplicationContext(), classView));
        overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
        finish();
    }


    }
