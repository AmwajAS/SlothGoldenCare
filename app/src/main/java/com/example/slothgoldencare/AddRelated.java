package com.example.slothgoldencare;

import android.content.Intent;
import android.provider.ContactsContract;
import android.widget.*;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;
import org.w3c.dom.Text;

public class AddRelated extends AppCompatActivity implements AdapterView.OnItemSelectedListener {

    private FirebaseFirestore db;
    private FirebaseAuth auth;
    private Button add_related_btn;
    private FirebaseUser currUser;
    private EditText related_id_text;
    private ImageButton back_btn;
    private DataBaseHelper dataBaseHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_related);
        db = FirebaseFirestore.getInstance();
        dataBaseHelper = new DataBaseHelper(this);
        auth = FirebaseAuth.getInstance();
        currUser = auth.getCurrentUser();
        add_related_btn = findViewById(R.id.addEldBtn);
        related_id_text = findViewById(R.id.related_id);

        //back button

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
                Toast.makeText(AddRelated.this,R.string.alert_message_failed_sign_in, Toast.LENGTH_LONG).show();
            }else{
                //if exists, adding. STILL NEED TO ADD IN DATABASE.
                ElderRelative elderRelative = new ElderRelative(currUser.getUid(),elder.getDocId(),spinner.getSelectedItem().toString());
                db.collection("ElderlyRelative").add(elderRelative).addOnSuccessListener(documentReference -> {
                    Toast.makeText(AddRelated.this,"Added User "+elder.getUsername()+" Successfuly", Toast.LENGTH_LONG).show();
                }).addOnFailureListener(e -> {
                    Toast.makeText(AddRelated.this,e.getMessage().toString(), Toast.LENGTH_LONG).show();
                });
            }
        });
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }



    public void showAlertDialog(View view){

    }


}