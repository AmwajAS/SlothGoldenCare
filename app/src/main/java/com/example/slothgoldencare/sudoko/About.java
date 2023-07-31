package com.example.slothgoldencare.sudoko;


import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Button;


import androidx.appcompat.app.AppCompatActivity;


import com.example.slothgoldencare.HomePageActivity;
import com.example.slothgoldencare.ProfileActivity;
import com.example.slothgoldencare.R;
import com.example.slothgoldencare.SettingsActivity;
import com.google.android.material.bottomnavigation.BottomNavigationView;


/**
 * The `About` class is an `Activity` class in Android that represents the screen
 * or UI component for displaying information about an application or any other relevant content.
 */
public class About extends AppCompatActivity {

    private Button backBtn;
    /**
     * This method is called when the `About` activity is created.
     * It initializes the activity and sets the content view to the layout defined in the `activity_about.xml` file.
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_about);
        bottomNavigationView();

        // back btn to remove the replaced view to the main one.
        backBtn = findViewById(R.id.back_btn);
        backBtn.setOnClickListener(view1 -> {
                    Intent intent = new Intent(About.this, GameActivity.class);
                    startActivity(intent);
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
