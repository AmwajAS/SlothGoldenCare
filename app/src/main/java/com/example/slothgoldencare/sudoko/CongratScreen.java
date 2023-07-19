package com.example.slothgoldencare.sudoko;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;

import com.example.slothgoldencare.R;

/**
 * The CongratScreen activity is displayed when the user completes the Sudoku game successfully.
 * It provides options to go back to the main menu or start a new game.
 */
public class CongratScreen extends Activity implements OnClickListener {
    private static final String TAG = "Sudoku";

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_congrat_screen);
        View menuButton = findViewById(R.id.menu_button);
        menuButton.setOnClickListener(this);
        View replayButton = findViewById(R.id.replay_button);
        replayButton.setOnClickListener(this);
    }

    /**
     * Handles click events for the menu and replay buttons.
     *
     * @param v The view that was clicked.
     */
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.menu_button:
                finish();
                break;
            case R.id.replay_button:
                openNewGameDialog();
                break;
            // More buttons go here (if any) ...
        }
    }

    /**
     * Opens a dialog to select the difficulty level for the new game.
     */
    private void openNewGameDialog() {
        new AlertDialog.Builder(this)
                .setTitle(R.string.new_game_title)
                .setItems(R.array.difficulty,
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialoginterface, int i) {
                                startGame(i);
                            }
                        })
                .show();
    }

    /**
     * Starts a new Sudoku game with the selected difficulty level.
     *
     * @param i The selected difficulty level.
     */
    private void startGame(int i) {
        Log.d(TAG, "clicked on " + i);
        //Start game here...
        Intent intent = new Intent(this, Game.class);
        intent.putExtra(Game.KEY_DIFFICULTY, i);
        startActivity(intent);
        finish();
    }

    public void onClick(DialogInterface dialog, int which) {
        // TODO Auto-generated method stub

    }

}