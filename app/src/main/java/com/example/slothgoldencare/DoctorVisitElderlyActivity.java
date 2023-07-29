package com.example.slothgoldencare;

import android.app.Application;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.example.slothgoldencare.Model.Doctor;
import com.example.slothgoldencare.Model.Elder;
import com.google.firebase.auth.FirebaseAuth;
import com.zegocloud.uikit.prebuilt.call.config.ZegoNotificationConfig;
import com.zegocloud.uikit.prebuilt.call.invite.ZegoUIKitPrebuiltCallInvitationConfig;
import com.zegocloud.uikit.prebuilt.call.invite.ZegoUIKitPrebuiltCallInvitationService;
import com.zegocloud.uikit.prebuilt.call.invite.widget.ZegoSendCallInvitationButton;
import com.zegocloud.uikit.service.defines.ZegoUIKitUser;

import java.util.Collections;

public class DoctorVisitElderlyActivity extends AppCompatActivity implements View.OnClickListener {

    private String elderlyId;
    private DataBaseHelper dataBaseHelper;
    private TextView editTextUsername;
    private CardView D1, D2, D3, D4, D5;
    private Elder elder;
    private ZegoSendCallInvitationButton callBtn;
    private FirebaseAuth auth;
    private String doctorId;
    private Doctor doctorCaller;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_doctor_visit_elderly);

        //statring variables
        elderlyId = getIntent().getStringExtra("elderlyId");
        dataBaseHelper = new DataBaseHelper(this);
        elder = dataBaseHelper.getElderById(elderlyId);

        auth = FirebaseAuth.getInstance();
        doctorId = auth.getCurrentUser().getUid();
        doctorCaller = dataBaseHelper.getDoctorById(doctorId);

        //edit layout
        editTextUsername = findViewById(R.id.username);
        editTextUsername.setText(elder.getUsername());
        callBtn = findViewById(R.id.CallBtn);

        D1 = findViewById(R.id.d1);
        D2 = findViewById(R.id.d2);
        D3 = findViewById(R.id.d3);
        D4 = findViewById(R.id.d4);
        D5 = findViewById(R.id.d5);

        D1.setOnClickListener(this);
        D2.setOnClickListener(this);
        D3.setOnClickListener(this);
        D4.setOnClickListener(this);
        D5.setOnClickListener(this);


        callBtn = findViewById(R.id.CallBtn);
        callBtn.setOnClickListener(v -> {
            startmyservice(doctorId);
            setVideoCall(elder.getID());

        });
    }

    @Override
    public void onClick(View v) {
        HealthStatusDoctorFragment healthStatusFragment;
        Intent i;
        switch (v.getId()) {
            case R.id.d1:
                Bundle args1 = new Bundle();
                replaceFragment(new DoctorsFragment());
                break;
            case R.id.d2:
                Bundle args2 = new Bundle();
                args2.putString("elderly", elder.getDocId());
                args2.putString("Button", "diagnosis");
                healthStatusFragment = HealthStatusDoctorFragment.newInstance(args2);
                replaceFragment(healthStatusFragment);
                break;
            case R.id.d3:
                Bundle args3 = new Bundle();
                args3.putString("elderly", elder.getDocId());
                args3.putString("Button", "medicines");
                healthStatusFragment = HealthStatusDoctorFragment.newInstance(args3);
                replaceFragment(healthStatusFragment);
                break;
            case R.id.d4:
                Bundle args4 = new Bundle();
                args4.putString("elderly", elder.getDocId());
                args4.putString("Button", "allergies");
                healthStatusFragment = HealthStatusDoctorFragment.newInstance(args4);
                replaceFragment(healthStatusFragment);
                break;
            case R.id.d5:
                Bundle args5 = new Bundle();
                args5.putString("elderly", elder.getDocId());
                args5.putString("Button", "surgeries");
                healthStatusFragment = HealthStatusDoctorFragment.newInstance(args5);
                replaceFragment(healthStatusFragment);
                break;

        }
    }

    private void replaceFragment(Fragment fragment) {
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.MedicalServicesFrameLayout, fragment);
        fragmentTransaction.commit();
    }


    public void startmyservice(String userid) {
        Application application = getApplication(); // Android's application context
        long appID = 2078788422;   // yourAppID
        String appSign = "397a9d1ae5d480580bc9758b53f142d60157c2643d29b3beb079848ba5f0dbbc";  // yourAppSign
        String userID = userid; // yourUserID, userID should only contain numbers, English characters, and '_'.
        String userName = userid;   // yourUserName

        ZegoUIKitPrebuiltCallInvitationConfig callInvitationConfig = new ZegoUIKitPrebuiltCallInvitationConfig();
        callInvitationConfig.notifyWhenAppRunningInBackgroundOrQuit = true;
        ZegoNotificationConfig notificationConfig = new ZegoNotificationConfig();
        notificationConfig.sound = "zego_uikit_sound_call";
        notificationConfig.channelID = "CallInvitation";
        notificationConfig.channelName = "CallInvitation";
        ZegoUIKitPrebuiltCallInvitationService.init(getApplication(), appID, appSign, userID, userName, callInvitationConfig);

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        ZegoUIKitPrebuiltCallInvitationService.unInit();
    }


    void setVideoCall(String targetElderId){
        callBtn.setIsVideoCall(true);
        callBtn.setResourceID("zego_uikit_call");
        callBtn.setInvitees(Collections.singletonList(new ZegoUIKitUser(targetElderId)));


    }

}
