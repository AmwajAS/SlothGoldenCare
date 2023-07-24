package com.example.slothgoldencare;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import com.example.slothgoldencare.Model.Elder;
import com.example.slothgoldencare.Model.Gender;
import com.example.slothgoldencare.Model.User;
import com.example.slothgoldencare.R;


import java.util.Date;
import java.util.EnumMap;

import static android.content.ContentValues.TAG;

public class ElderlyItemDialog extends Dialog {

    public enum ItemType {
        USER,ELDER
    }


    private EditText etDocId, etId, etUsername, etEmail, etPassword, etPhoneNumber, etGender, etDOB;
    private TextView tvGender,tvDOB;
    private Button btnCancel, btnSaveChanges;
    private ItemType itemType;

    public void setEditTextValues(Elder oldElder,User oldUser) {
        if(itemType == ItemType.ELDER) {
            if (oldElder != null) {
                etDocId.setText(oldElder.getDocId());
                etId.setText(oldElder.getID());
                etUsername.setText(oldElder.getUsername());
                etEmail.setText(oldElder.getEmail());
                etPassword.setText(oldElder.getPassword());
                etPhoneNumber.setText(oldElder.getPhoneNumber());
                etGender.setText(oldElder.getGender().toString());
                etDOB.setText(oldElder.getDOB().toString());
            }
        }else if(itemType ==ItemType.USER){
            if(oldUser != null){
                etDocId.setText(oldUser.getDocId());
                etId.setText(oldUser.getID());
                etUsername.setText(oldUser.getUsername());
                etEmail.setText(oldUser.getEmail());
                etPassword.setText(oldUser.getPassword());
                etPhoneNumber.setText(oldUser.getPhoneNumber());
            }
        }
    }


    public interface OnSaveChangesListener {
        void onSaveChanges(Elder elder);
        void onSaveChanges(User user);
    }

    private OnSaveChangesListener listener;

    public ElderlyItemDialog(Context context, OnSaveChangesListener listener,ItemType type) {
        super(context);
        this.listener = listener;
        this.itemType = type;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dialog_elderly_item);

        etDocId = findViewById(R.id.editTextDocID);
        etId = findViewById(R.id.editTextID);
        etUsername = findViewById(R.id.editTextUsername);
        etEmail = findViewById(R.id.editTextEmail);
        etPassword = findViewById(R.id.editTextPassword);
        etPhoneNumber = findViewById(R.id.editTextPhoneNumber);
        etGender = findViewById(R.id.editTextGender);
        etDOB = findViewById(R.id.editTextDOB);
        tvGender = findViewById(R.id.textViewGender);
        tvDOB = findViewById(R.id.textViewDOB);

        if(itemType == ItemType.USER){
            etGender.setVisibility(View.GONE);
            etDOB.setVisibility(View.GONE);
            tvGender.setVisibility(View.GONE);
            tvDOB.setVisibility(View.GONE);
        }


        //set the data of the variables depending on the elderly.


        btnCancel = findViewById(R.id.btnCancel);
        btnSaveChanges = findViewById(R.id.saveChangesBtn);

        btnCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
            }
        });

        btnSaveChanges.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String docId = etDocId.getText().toString();
                String id = etId.getText().toString();
                String username = etUsername.getText().toString();
                String email = etEmail.getText().toString();
                String password = etPassword.getText().toString();
                String phoneNumber = etPhoneNumber.getText().toString();
                if(itemType == ItemType.ELDER) {
                    String genderString = etGender.getText().toString();
                    String dob = etDOB.getText().toString();
                    Date date = DataBaseHelper.parseDateString(dob);
                    Gender gender = Elder.GenderConvertor(genderString);
                    Elder newElder = new Elder(id, username, phoneNumber, date, gender, email, password);
                    newElder.setDocId(docId);
                    listener.onSaveChanges(newElder);
                } else if (itemType == ItemType.USER) {
                    User newUser = new User(id,username,phoneNumber,email,password);
                    newUser.setDocId(docId);
                    listener.onSaveChanges(newUser);
                }
                dismiss();
            }
        });
    }
}
