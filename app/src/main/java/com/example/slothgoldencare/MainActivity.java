package com.example.slothgoldencare;

import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import com.example.slothgoldencare.DataBaseHelper.DataBaseHelper;

import java.io.File;
import java.util.Timer;
import java.util.TimerTask;

import static android.content.ContentValues.TAG;

public class MainActivity extends AppCompatActivity {


    Animation topAnim, bottomAnim;
    ImageView image;
    ImageView logo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        // Create an instance of DatabaseHelper
        String databasePath = this.getDatabasePath("GOLDEN_CARE.DB").getPath();
        Log.w(TAG,"This is the path for the db: "+databasePath);
        boolean isDatabaseExists = checkDatabaseExists(databasePath);

        // If the database file exists, delete it
        if (isDatabaseExists) {
            deleteDatabase("GOLDEN_CARE.DB");
        }
        DataBaseHelper databaseHelper = new DataBaseHelper(getApplicationContext());

        // Get a writable database (this will create the database file if it doesn't exist)
        SQLiteDatabase db = databaseHelper.getWritableDatabase();

        new Timer().schedule(new TimerTask() {
            @Override
            public void run() {
                startActivity(new Intent(getApplicationContext(), LoginActivity.class));
            }
        },3000);

        topAnim = AnimationUtils.loadAnimation(this, R.anim.top_animation);
        bottomAnim = AnimationUtils.loadAnimation(this, R.anim.bottom_animation);


        image = findViewById(R.id.image);
        logo = findViewById(R.id.logo);
        image.setAnimation(topAnim);
        logo.setAnimation(bottomAnim);


    }

    private boolean checkDatabaseExists(String databasePath) {
        File dbFile = new File(databasePath);
        return dbFile.exists();
    }
}