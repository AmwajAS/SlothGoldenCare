package com.example.slothgoldencare.sudoko;

import static com.example.slothgoldencare.R.*;


import android.os.Bundle;

import com.example.slothgoldencare.HomePageActivity;
import com.example.slothgoldencare.ProfileActivity;
import com.example.slothgoldencare.R;
import com.example.slothgoldencare.SettingsActivity;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.app.Activity;
import android.app.AlertDialog;
import android.util.Log;
import android.content.Intent;
import android.view.View.OnClickListener;


/**
 * The GameActivity represents the main activity of the Sudoku game.
 * It provides the user interface for starting a new game, continuing the game, and accessing game settings.
 */
public class GameActivity extends Activity implements OnClickListener {

    private static final String TAG = "Sudoku";

    /**
     * Called when the activity is first created.
     * Initializes the activity and sets up click listeners for the buttons.
     */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(layout.activity_game);
        //Set up click listeners for all the buttons
        View continueButton = findViewById(R.id.continue_button);
        continueButton.setOnClickListener(this);
        View newButton = findViewById(R.id.new_button);
        newButton.setOnClickListener(this);
        View aboutButton = findViewById(R.id.about_button);
        aboutButton.setOnClickListener(this);
        View exitButton = findViewById(R.id.exit_button);
        exitButton.setOnClickListener(this);

        bottomNavigationView();
    }

    /**
     * Handles the click events for the buttons.
     *
     * @param v The view that was clicked.
     */
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.about_button:
                Intent i = new Intent(this, About.class);
                startActivity(i);
                break;
            case R.id.new_button:
                openNewGameDialog();
                break;
            case R.id.exit_button:
                confirmExit();
                break;
            case R.id.continue_button:
                startGame(Game.DIFFICULTY_CONTINUE);
                break;
            // More buttons go here (if any) ...
        }
    }


    /**
     * Displays a confirmation dialog for exiting the game.
     * If the user confirms the exit, the activity is finished.
     */
    private void confirmExit() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage(R.string.confirm_exit)
                .setCancelable(false)
                .setPositiveButton(R.string.exit_yes_label, (dialog, id) -> {
                    // TODO Auto-generated method stub
                    GameActivity.this.finish();
                })
                .setNegativeButton(R.string.exit_no_label, (dialog, id) -> {
                    // TODO Auto-generated method stub
                    dialog.cancel();
                });
        builder.create();
        builder.show();
    }

    /**
     * Displays a dialog for selecting the difficulty level and starts a new game.
     */
    private void openNewGameDialog() {
        new AlertDialog.Builder(this)
                .setTitle(R.string.new_game_title)
                .setItems(R.array.difficulty,
                        (dialoginterface, i) -> startGame(i))
                .show();
    }

    /**
     * Starts a new game with the specified difficulty level.
     *
     * @param i The difficulty level.
     */
    private void startGame(int i) {
        Log.d(TAG, "clicked on " + i);
        //Start game here...
        Intent intent = new Intent(GameActivity.this, Game.class);
        intent.putExtra(Game.KEY_DIFFICULTY, i);
        startActivity(intent);
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