package com.example.slothgoldencare.Reminder;

import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.app.AlarmManager;
import android.app.DatePickerDialog;
import android.app.PendingIntent;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TimePicker;
import android.widget.Toast;

import com.example.slothgoldencare.DataBaseHelper.DataBaseHelper;
import com.example.slothgoldencare.ElderSignupActivity;
import com.example.slothgoldencare.HomePageActivity;
import com.example.slothgoldencare.ProfileActivity;
import com.example.slothgoldencare.R;
import com.example.slothgoldencare.SettingsActivity;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;


/**
 * This class is used to take reminders from the user and insert them into the database.
 */
public class ReminderActivity extends AppCompatActivity {

    // Declare the RadioGroup and RadioButtons
    RadioGroup radioGroupFrequency;
    RadioButton radioButtonOneTime;
    RadioButton radioButtonDaily;
    Button mSubmitbtn, mDatebtn, mTimebtn;
    EditText mTitledit;
    String timeTonotify;
    private DataBaseHelper dataBaseHelper;
    private Button backBtn;

    /**
     * This method is called when the activity is created.
     * It initializes the UI components and sets the click listeners for buttons.
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reminder);

        dataBaseHelper = new DataBaseHelper(this);

        mTitledit = (EditText) findViewById(R.id.editTitle);
        mDatebtn = findViewById(R.id.btnDate);                                             //assigned all the material reference to get and set data
        mTimebtn = (Button) findViewById(R.id.btnTime);
        mSubmitbtn = (Button) findViewById(R.id.btnSubmit);


        // back btn to remove the replaced view to the main one.
        backBtn = findViewById(R.id.back_btn);
        backBtn.setOnClickListener(view1 -> {
            AlertDialog.Builder builder = new AlertDialog.Builder(ReminderActivity.this);
            builder.setTitle("Confirm Exit");
            builder.setMessage("Are you sure you want to exit this Reminder, Data will not be Saved?");
            builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    Intent intent = new Intent(ReminderActivity.this, TODOActivity.class);
                    startActivity(intent);
                }
            });
            builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    dialog.dismiss();
                }
            });

            AlertDialog alertDialog = builder.create();
            alertDialog.show();

        });

        bottomNavigationView();
        mTimebtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                selectTime();                                                                       //when we click on the choose time button it calls the select time method
            }
        });

        mDatebtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                selectDate();
            }   //when we click on the choose date button it calls the select date method
        });

        mSubmitbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String title = mTitledit.getText().toString().trim();                //access the data from the input field
                String date = mDatebtn.getText().toString();                         //access the date from the choose date button
                String time = mTimebtn.getText().toString().trim();                 //access the time from the choose time button
                // Get the selected task frequency
                if (checkEmptyFields(title, date, time)) {
                    processinsert(title, date, time);
                }
            }
        });
    }

    /**
     * Checks if the fields (title, date, time) and frequency are empty.
     * If any field is empty, shows an AlertDialog indicating the empty field.
     *
     * @param title The reminder title
     * @param date  The reminder date
     * @param time  The reminder time
     * @return True if all fields are filled, False otherwise
     */
    private boolean checkEmptyFields(String title, String date, String time) {
        if (title.isEmpty() || date.equals("date") || time.equals("time")) {
            // Show AlertDialog indicating the empty field
            AlertDialog.Builder builder = new AlertDialog.Builder(ReminderActivity.this);
            builder.setTitle("Warning");
            builder.setMessage("Please fill in all the fields and select a frequency");
            builder.setPositiveButton("OK", null);
            AlertDialog alertDialog = builder.create();
            alertDialog.show();
            return false;
        } else {
            return true;
        }

    }

    /**
     * Before the Processes of insertion of the reminder into the database.
     * Checks if the selected date and time are in the past.
     *
     * @param date The reminder date
     * @param time The reminder time
     */

    private boolean checkPastDateTime(String date, String time) {
        DateFormat dateFormat = new SimpleDateFormat("d-M-yyyy hh:mm");
        try {
            Date selectedDateTime = dateFormat.parse(date + " " + time);
            Date currentDateTime = new Date();

            if (selectedDateTime.before(currentDateTime)) {
                // Show AlertDialog indicating that the selected date and time are in the past
                AlertDialog.Builder builder = new AlertDialog.Builder(ReminderActivity.this);
                builder.setTitle("Warning");
                builder.setMessage("Please select a future date and time");
                builder.setPositiveButton("OK", null);
                AlertDialog alertDialog = builder.create();
                alertDialog.show();
                return false;
            }
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return true;
    }


    /**
     * Processes the insertion of the reminder into the database.
     * Checks if the fields are empty and if the selected date and time are in the past.
     * If all checks pass, inserts the reminder into the database and sets the alarm.
     *
     * @param title The reminder title
     * @param date  The reminder date
     * @param time  The reminder time
     */
    private void processinsert(String title, String date, String time) {
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        Reminder reminder = new Reminder(FirebaseAuth.getInstance().getUid(), title, ElderSignupActivity.convertStringIntoDate(date), time);
        db.collection("Reminders").add(reminder).addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                dataBaseHelper.addReminder(reminder);
                Toast.makeText(this, R.string.reminder_add_success, Toast.LENGTH_SHORT).show();
                if (checkPastDateTime(date, time)) {
                    setAlarm(reminder);
                    mTitledit.setText("");
                }
            }
        });


    }

    /**
     * Displays a TimePickerDialog and allows the user to select the time.
     * Sets the selected time as the text for the time button.
     */
    private void selectTime() {                                                                     //this method performs the time picker task
        Calendar calendar = Calendar.getInstance();
        int hour = calendar.get(Calendar.HOUR_OF_DAY);
        int minute = calendar.get(Calendar.MINUTE);
        TimePickerDialog timePickerDialog = new TimePickerDialog(this, new TimePickerDialog.OnTimeSetListener() {
            @Override
            public void onTimeSet(TimePicker timePicker, int i, int i1) {
                timeTonotify = i + ":" + i1;                                                        //temp variable to store the time to set alarm
                mTimebtn.setText(FormatTime(i, i1));                                                //sets the button text as selected time
            }
        }, hour, minute, false);
        timePickerDialog.show();
    }

    /**
     * Displays a DatePickerDialog and allows the user to select the date.
     * Sets the selected date as the text for the date button.
     */
    private void selectDate() {                                                                     //this method performs the date picker task
        Calendar calendar = Calendar.getInstance();
        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH);
        int day = calendar.get(Calendar.DAY_OF_MONTH);
        DatePickerDialog datePickerDialog = new DatePickerDialog(this, new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker datePicker, int year, int month, int day) {
                String date = day + "/" + month + "/" + year;
                mDatebtn.setText(date);                             //sets the selected date as test for button
            }
        }, year, month, day);
        datePickerDialog.show();
    }

    /**
     * This method converts the time into 12hr format and assigns am or pm
     *
     * @param hour   The selected hour
     * @param minute The selected minute
     * @return The formatted time string
     */
    public String FormatTime(int hour, int minute) {

        String time;
        time = "";
        String formattedMinute;

        if (minute / 10 == 0) {
            formattedMinute = "0" + minute;
        } else {
            formattedMinute = "" + minute;
        }


        if (hour == 0) {
            time = "12" + ":" + formattedMinute + " AM";
        } else if (hour < 12) {
            time = hour + ":" + formattedMinute + " AM";
        } else if (hour == 12) {
            time = "12" + ":" + formattedMinute + " PM";
        } else {
            int temp = hour - 12;
            time = temp + ":" + formattedMinute + " PM";
        }


        return time;
    }

    /**
     * Sets the alarm for the reminder.
     * Creates an alarm using AlarmManager and schedules a PendingIntent.
     *
     * @param reminder title The reminder text/event
     * @param reminder date The reminder date
     * @param reminder time The reminder time
     */
    private void setAlarm(Reminder reminder) {
        AlarmManager am = (AlarmManager) getSystemService(Context.ALARM_SERVICE);                   //assigning alarm manager object to set alarm

        Intent intent = new Intent(getApplicationContext(), AlarmBroadcast.class);
        intent.putExtra("event", reminder.getTitle());                                                       //sending data to alarm class to create channel and notification
        intent.putExtra("time", reminder.getDate());
        intent.putExtra("date", reminder.getTime());

        PendingIntent pendingIntent = PendingIntent.getBroadcast(getApplicationContext(), 0, intent, PendingIntent.FLAG_ONE_SHOT);
        String dateandtime = reminder.getDate() + " " + timeTonotify;
        DateFormat formatter = new SimpleDateFormat("d-M-yyyy hh:mm");
        try {
            Date date1 = formatter.parse(dateandtime);
            am.set(AlarmManager.RTC_WAKEUP, date1.getTime(), pendingIntent);
            Toast.makeText(getApplicationContext(), "Alarm", Toast.LENGTH_SHORT).show();

        } catch (ParseException e) {
            e.printStackTrace();
        }

        Intent intentBack = new Intent(getApplicationContext(), TODOActivity.class);                //this intent will be called once the setting alarm is complete
        intentBack.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intentBack);                                                                  //navigates from adding reminder activity to mainactivity

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