package com.example.slothgoldencare;


import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.widget.*;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import com.example.slothgoldencare.VideoCall.CallElderActivity;
import android.Manifest;


public class DoctorActivityMain extends AppCompatActivity{
    private static final String TAG = "ADMIN";

    private Button healthTipsBtn;
    private Button elderliesBtn;
    private Button appointmentsBtn;
    private Button workPayBtn;
    private Button callBtn;
    DataBaseHelper dbHelper;

    private final String[] permissions = {Manifest.permission.CAMERA, Manifest.permission.RECORD_AUDIO};
    private final int requestcode = 1;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_doctor_main);
        dbHelper = new DataBaseHelper(this);
        String doctorUid = getIntent().getStringExtra("doctorUid");

        elderliesBtn = findViewById(R.id.elderlies_btn);
        healthTipsBtn = findViewById(R.id.health_tips_btn);
        appointmentsBtn = findViewById(R.id.appointments_button);
        workPayBtn= findViewById(R.id.work_pay_button);

        callBtn = findViewById(R.id.callBtn);



        elderliesBtn.setOnClickListener(v-> {
            Intent intent = new Intent(DoctorActivityMain.this, DoctorActivity.class);
            startActivity(intent);
        });

        healthTipsBtn.setOnClickListener(v-> {
            Intent intent = new Intent(DoctorActivityMain.this, DoctorActivityHealthTips.class);
            startActivity(intent);
        });

        appointmentsBtn.setOnClickListener(v-> {
            Intent intent = new Intent(DoctorActivityMain.this, AppointmentsPatientsListActivity.class);
            intent.putExtra("doctorUid", doctorUid);
            startActivity(intent);
        });
        workPayBtn.setOnClickListener(v-> {
            Intent intent = new Intent(DoctorActivityMain.this, DoctorReportActivity.class);
            intent.putExtra("doctorUid", doctorUid);
            startActivity(intent);
        });

        callBtn.setOnClickListener(v-> {
           // startmyservice("987654321");

            if (!isPermissionGranted()) {
                askPermissions();
            }
            Intent intent = new Intent(this, CallElderActivity.class);
            intent.putExtra("username", doctorUid);
            startActivity(intent);
        });
    }

//    public void startmyservice(String userid) {
//        Application application = getApplication(); // Android's application context
//        long appID = 2078788422;   // yourAppID
//        String appSign = "397a9d1ae5d480580bc9758b53f142d60157c2643d29b3beb079848ba5f0dbbc";  // yourAppSign
//        String userID = userid; // yourUserID, userID should only contain numbers, English characters, and '_'.
//        String userName = userid;   // yourUserName
//
//        ZegoUIKitPrebuiltCallInvitationConfig callInvitationConfig = new ZegoUIKitPrebuiltCallInvitationConfig();
//        callInvitationConfig.notifyWhenAppRunningInBackgroundOrQuit = true;
//        ZegoNotificationConfig notificationConfig = new ZegoNotificationConfig();
//        notificationConfig.sound = "zego_uikit_sound_call";
//        notificationConfig.channelID = "CallInvitation";
//        notificationConfig.channelName = "CallInvitation";
//        ZegoUIKitPrebuiltCallInvitationService.init(getApplication(), appID, appSign, userID, userName, callInvitationConfig);
//
//    }
//
//    @Override
//    protected void onDestroy() {
//        super.onDestroy();
//        ZegoUIKitPrebuiltCallInvitationService.unInit();
//    }

    private void askPermissions() {
        ActivityCompat.requestPermissions(this, permissions, requestcode);
    }

    private boolean isPermissionGranted() {
        for (String permission : permissions) {
            if (ActivityCompat.checkSelfPermission(this, permission) != PackageManager.PERMISSION_GRANTED)
                return false;
        }

        return true;
    }

}
