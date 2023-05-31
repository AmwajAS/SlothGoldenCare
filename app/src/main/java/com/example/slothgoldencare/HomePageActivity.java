package com.example.slothgoldencare;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

public class HomePageActivity extends AppCompatActivity implements View.OnClickListener {
    private CardView D1,D2,D3,D4,D5,D6;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home_page);

        D1=(CardView) findViewById(R.id.d1);
        D2=(CardView) findViewById(R.id.d2);
        D3=(CardView) findViewById(R.id.d3);
        D4=(CardView) findViewById(R.id.d4);
        D5=(CardView) findViewById(R.id.d5);
        D6=(CardView) findViewById(R.id.d6);

        D1.setOnClickListener( (View.OnClickListener)this);
        D2.setOnClickListener( (View.OnClickListener)this);
        D3.setOnClickListener( (View.OnClickListener)this);
        D4.setOnClickListener( (View.OnClickListener)this);
        D5.setOnClickListener( (View.OnClickListener)this);
        D6.setOnClickListener( (View.OnClickListener)this);
    }

    @Override
    public void onClick(View v){
        Intent i;
        switch (v.getId()){
            case R.id.d1: i = new Intent(this, MedicalServicesActivity.class);
            startActivity(i);
            break;
            case R.id.d2: i = new Intent(this, BottomNavigationBarActivity.class);
                startActivity(i);
                break;

        }

    }
}