package com.example.slothgoldencare.sudoko;


import android.app.Activity;
import android.os.Bundle;

import com.example.slothgoldencare.R;


/**
 * The `About` class is an `Activity` class in Android that represents the screen
 * or UI component for displaying information about an application or any other relevant content.
 */
public class About extends Activity {

    /**
     * This method is called when the `About` activity is created.
     * It initializes the activity and sets the content view to the layout defined in the `activity_about.xml` file.
     *
     * @param savedInstanceState A Bundle object containing the activity's previously saved state,
     *                           or null if the activity is being created for the first time.
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_about);
    }
}
