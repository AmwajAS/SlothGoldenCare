package com.example.slothgoldencare.VideoCall;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Application;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.example.slothgoldencare.ProfileActivity;
import com.example.slothgoldencare.R;
import com.google.firebase.auth.FirebaseAuth;
import com.zegocloud.uikit.prebuilt.call.config.ZegoNotificationConfig;
import com.zegocloud.uikit.prebuilt.call.invite.ZegoUIKitPrebuiltCallInvitationConfig;
import com.zegocloud.uikit.prebuilt.call.invite.ZegoUIKitPrebuiltCallInvitationService;
import com.zegocloud.uikit.prebuilt.call.invite.widget.ZegoSendCallInvitationButton;
import com.zegocloud.uikit.prebuilt.videoconference.ZegoUIKitPrebuiltVideoConferenceConfig;
import com.zegocloud.uikit.prebuilt.videoconference.ZegoUIKitPrebuiltVideoConferenceFragment;
import com.zegocloud.uikit.prebuilt.videoconference.config.ZegoMenuBarButtonName;
import com.zegocloud.uikit.service.defines.ZegoUIKitUser;

import java.util.Arrays;
import java.util.Collections;

public class VideoCallActivity extends AppCompatActivity {

    private Button call;
    private FirebaseAuth auth;
    ZegoSendCallInvitationButton callBtn;




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_video_call);

        String userid=getIntent().getStringExtra("userid");
        String confrid=getIntent().getStringExtra("confrid");
//        addFragment(userid,confrid);
//        callBtn = findViewById(R.id.CallBtn);
//        callBtn.setOnClickListener(v -> {
//            startmyservice(userid);
//            setVideoCall(confrid);
//
//        });

    }
//
//    public void addFragment(String u, String c)
//    {
//
//        long appID =2078788422; // your appID, get from ZEGOCLOUD console
//        String appSign = "397a9d1ae5d480580bc9758b53f142d60157c2643d29b3beb079848ba5f0dbbc"; //your appSign, get from ZEGOCLOUD console
//
//        String userID =  u;  // your userID
//        String userName =  u;  // your userName
//        String conferenceID =c;  // conferenceID
//
//        ZegoUIKitPrebuiltVideoConferenceConfig config= new ZegoUIKitPrebuiltVideoConferenceConfig();
//        config.turnOnCameraWhenJoining = false;
//        config.audioVideoViewConfig.useVideoViewAspectFill = true;
//        config.bottomMenuBarConfig.buttons = Arrays.asList(
//                ZegoMenuBarButtonName.TOGGLE_CAMERA_BUTTON,
//                ZegoMenuBarButtonName.LEAVE_BUTTON,
//                ZegoMenuBarButtonName.CHAT_BUTTON
//        );
//        config.topMenuBarConfig.isVisible = false;
//        config.inRoomNotificationViewConfig.notifyUserLeave = false;
//        config.memberListConfig.showCameraState = false;
//
//        ZegoUIKitPrebuiltVideoConferenceFragment fragment = ZegoUIKitPrebuiltVideoConferenceFragment.newInstance(
//                appID,appSign,userID,userName,conferenceID,config);
//        getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,fragment).commitNow();
//    }
//public void startmyservice(String userid) {
//    Application application = getApplication(); // Android's application context
//    long appID = 2078788422;   // yourAppID
//    String appSign = "397a9d1ae5d480580bc9758b53f142d60157c2643d29b3beb079848ba5f0dbbc";  // yourAppSign
//    String userID = userid; // yourUserID, userID should only contain numbers, English characters, and '_'.
//    String userName = userid;   // yourUserName
//
//    ZegoUIKitPrebuiltCallInvitationConfig callInvitationConfig = new ZegoUIKitPrebuiltCallInvitationConfig();
//    callInvitationConfig.notifyWhenAppRunningInBackgroundOrQuit = true;
//    ZegoNotificationConfig notificationConfig = new ZegoNotificationConfig();
//    notificationConfig.sound = "zego_uikit_sound_call";
//    notificationConfig.channelID = "CallInvitation";
//    notificationConfig.channelName = "CallInvitation";
//    ZegoUIKitPrebuiltCallInvitationService.init(getApplication(), appID, appSign, userID, userName, callInvitationConfig);
//
//}
//
//    @Override
//    protected void onDestroy() {
//        super.onDestroy();
//        ZegoUIKitPrebuiltCallInvitationService.unInit();
//    }
//
//
//    void setVideoCall(String targetElderId){
//        callBtn.setIsVideoCall(true);
//        callBtn.setResourceID("zego_uikit_call");
//        callBtn.setInvitees(Collections.singletonList(new ZegoUIKitUser(targetElderId)));
//
//
//    }



}