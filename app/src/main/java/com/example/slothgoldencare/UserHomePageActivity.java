package com.example.slothgoldencare;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.widget.Button;

public class UserHomePageActivity extends AppCompatActivity {

    private Toolbar toolbar;
    private Button addBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_home_page);
        toolbar = findViewById(R.id.actBar);
        addBtn = findViewById(R.id.addBtn);
        setSupportActionBar(toolbar);

        addBtn.setOnClickListener(v -> {
            Intent intent = new Intent(getApplicationContext(), AddRelated.class);
            startActivity(intent);
        });



    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.action_menu, menu);
        return true;
    }


}