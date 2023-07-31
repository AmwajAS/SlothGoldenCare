package com.example.slothgoldencare;

import android.content.Intent;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.*;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;

import com.example.slothgoldencare.DataBaseHelper.DataBaseHelper;
import com.example.slothgoldencare.Model.Elder;
import com.example.slothgoldencare.Model.ElderRelative;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.List;

public class AddRelatedActivity extends AppCompatActivity implements AdapterView.OnItemSelectedListener {

    private FirebaseFirestore db;
    private FirebaseUser currUser;
    private EditText related_id_text;
    private DataBaseHelper dataBaseHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_related);
        db = FirebaseFirestore.getInstance();
        dataBaseHelper = new DataBaseHelper(this);
        FirebaseAuth auth = FirebaseAuth.getInstance();
        currUser = auth.getCurrentUser();
        Button add_related_btn = findViewById(R.id.addEldBtn);
        related_id_text = findViewById(R.id.related_id);
        bottomNavigationView();

        List<ElderRelative> elderRelativeList = dataBaseHelper.getElderlyRelatives();
        //relation spinner
        Spinner spinner = findViewById(R.id.relation);
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this, R.array.Relation, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);
        spinner.setOnItemSelectedListener(this);

        //Add Related button
        add_related_btn.setOnClickListener(v -> {
            String relatedID = related_id_text.getText().toString();

            //returning the elder with the specific ID, if not exist returns null
            Elder elder = dataBaseHelper.getElderById(relatedID);
            if(elder == null){
                //if null show fit text
                Snackbar.make(getWindow().getDecorView(),R.string.alert_message_failed_sign_in, Snackbar.LENGTH_LONG).show();
            }else{
                //if exists, adding.
                ElderRelative elderRelative = new ElderRelative(currUser.getUid(),elder.getDocId(),spinner.getSelectedItem().toString());
                if(!elderRelativeList.contains(elderRelative)) {
                    db.collection("ElderlyRelative").add(elderRelative).addOnSuccessListener(documentReference -> {
                        elderRelative.setDocId(documentReference.getId());
                        if (dataBaseHelper.addElderRelative(elderRelative)) {
                            Snackbar.make(getWindow().getDecorView(), "Added User " + elder.getUsername() + " Successfully", Snackbar.LENGTH_LONG).show();
                        }
                    }).addOnFailureListener(e -> Toast.makeText(AddRelatedActivity.this, e.getMessage(), Toast.LENGTH_LONG).show());
                }
                else{
                    Snackbar.make(getWindow().getDecorView(), "This elderly is already in your list!", Snackbar.LENGTH_LONG).show();
                }
            }
        });
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

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
                    replaceView(UserHomePageActivity.class);
                    return true;
                case R.id.settings:
                    replaceView(SettingsActivity.class);
                    return true;
                case R.id.profile:
                    replaceView(VisitElderlyProfileActivity.class);
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