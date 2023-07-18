package com.example.slothgoldencare.sudoko;

import android.content.SharedPreferences;
import android.graphics.Typeface;
import android.os.Bundle;
import com.example.slothgoldencare.R;
import android.util.TypedValue;
import android.view.View;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.util.Log;
import android.content.Intent;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View.OnClickListener;
import android.widget.LinearLayout;
import android.widget.TextView;


public class GameActivity extends Activity implements OnClickListener {

    private static final String PREFS_NAME = "SudokuPrefs";
    private static final String SCORE_KEY = "Score";

    private int score = 0;


    @Override
        protected void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            setContentView(R.layout.fragment_main);
            View continueButton = findViewById(R.id.continue_button);
            continueButton.setOnClickListener(this);
            View newButton = findViewById(R.id.new_button);
            newButton.setOnClickListener(this);
            View aboutButton = findViewById(R.id.about_button);
            aboutButton.setOnClickListener(this);
            View exitButton = findViewById(R.id.exit_button);
            exitButton.setOnClickListener(this);

        }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);

        // Save the player's score in the saved instance state
        outState.putInt(SCORE_KEY, score);
    }

    @Override
    protected void onPause() {
        super.onPause();

        // Save the player's score in SharedPreferences
        SharedPreferences preferences = getSharedPreferences(PREFS_NAME, MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();
        editor.putInt(SCORE_KEY, score);
        editor.apply();
    }
        @Override
        public boolean onCreateOptionsMenu(Menu menu)
        {
            super.onCreateOptionsMenu(menu);
            MenuInflater inflater = getMenuInflater();
            inflater.inflate(R.menu.menu, menu);
            return true;
        }



        @Override
        public boolean onOptionsItemSelected(MenuItem item)
        {
            switch(item.getItemId())
            {
                case R.id.settings:
                    startActivity(new Intent(this,Prefs.class));
                    return true;
            }
            return false;
        }

        private static final String TAG="Sudoku";
        private void openNewGameDialog(){
            new AlertDialog.Builder(this)
                    .setTitle(R.string.new_game_title)
                    .setItems(R.array.difficulty,
                            new DialogInterface.OnClickListener(){
                                public void onClick(DialogInterface dialoginterface,
                                                    int i){
                                    startGame(i);
                                }
                            })
                    .show();
        }

        private void startGame(int i){
            Log.d(TAG, "clicked on " + i);
            Intent intent = new Intent(this, Game.class);
            intent.putExtra(Game.KEY_DIFFICULTY,i);
            startActivity(intent);
        }


        public void onClick(View v)
        {
            switch(v.getId())
            {
                case R.id.about_button:
                    Intent i = new Intent(this, About.class);
                    startActivity(i);
                    break;
                case R.id.new_button:
                    openNewGameDialog();
                    break;
                case R.id.exit_button:
                    finish();
                    break;
            }
        }

    }