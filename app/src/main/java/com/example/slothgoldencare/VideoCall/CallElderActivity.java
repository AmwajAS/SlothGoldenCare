package com.example.slothgoldencare.VideoCall;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Application;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.webkit.JavascriptInterface;
import android.widget.Button;
import android.widget.EditText;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.webkit.PermissionRequest;
import android.webkit.WebChromeClient;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import java.util.UUID;
import com.example.slothgoldencare.R;
import com.zegocloud.uikit.prebuilt.call.config.ZegoNotificationConfig;
import com.zegocloud.uikit.prebuilt.call.invite.ZegoUIKitPrebuiltCallInvitationConfig;
import com.zegocloud.uikit.prebuilt.call.invite.ZegoUIKitPrebuiltCallInvitationService;
import com.zegocloud.uikit.prebuilt.call.invite.widget.ZegoSendCallInvitationButton;
import com.zegocloud.uikit.service.defines.ZegoUIKitUser;


import java.util.Collections;

public class CallElderActivity extends AppCompatActivity {

    private String username = "";
    private String friendsUsername = "";

    private boolean isPeerConnected = false;

    private DatabaseReference firebaseRef = FirebaseDatabase.getInstance().getReference("users");

    private boolean isAudio = true;
    private boolean isVideo = true;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_call_elder);

        username = getIntent().getStringExtra("username");

        findViewById(R.id.callBtn).setOnClickListener(view -> {
            friendsUsername = ((EditText) findViewById(R.id.friendNameEdit)).getText().toString();
            sendCallRequest();
        });

        findViewById(R.id.toggleAudioBtn).setOnClickListener(view -> {
            isAudio = !isAudio;
            callJavascriptFunction("javascript:toggleAudio(\"" + isAudio + "\")");
            int audioIcon = isAudio ? R.drawable.baseline_mic_24 : R.drawable.baseline_mic_off_24;
            ((ImageView) findViewById(R.id.toggleAudioBtn)).setImageResource(audioIcon);
        });

        findViewById(R.id.toggleVideoBtn).setOnClickListener(view -> {
            isVideo = !isVideo;
            callJavascriptFunction("javascript:toggleVideo(\"" + isVideo + "\")");
            int videoIcon = isVideo ? R.drawable.baseline_videocam_24 : R.drawable.baseline_videocam_off_24;
            ((ImageView) findViewById(R.id.toggleVideoBtn)).setImageResource(videoIcon);
        });

        setupWebView();
    }

    private void sendCallRequest() {
        if (!isPeerConnected) {
            Toast.makeText(this, "You're not connected. Check your internet", Toast.LENGTH_LONG).show();
            return;
        }

        firebaseRef.child(friendsUsername).child("incoming").setValue(username);
        firebaseRef.child(friendsUsername).child("isAvailable").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot snapshot) {
                if (snapshot.getValue(String.class).equals("true")) {
                    listenForConnId();
                }
            }

            @Override
            public void onCancelled(DatabaseError error) {
            }
        });
    }

    private void listenForConnId() {
        firebaseRef.child(friendsUsername).child("connId").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot snapshot) {
                String connId = snapshot.getValue(String.class);
                if (connId != null) {
                    switchToControls();
                    callJavascriptFunction("javascript:startCall(\"" + connId + "\")");
                }
            }

            @Override
            public void onCancelled(DatabaseError error) {
            }
        });
    }

    private void setupWebView() {
        WebView webView = findViewById(R.id.webView);

        webView.setWebChromeClient(new WebChromeClient() {
            @Override
            public void onPermissionRequest(PermissionRequest request) {
                request.grant(request.getResources());
            }
        });

        webView.getSettings().setJavaScriptEnabled(true);
        webView.getSettings().setMediaPlaybackRequiresUserGesture(false);
        webView.addJavascriptInterface(new CustomJavascriptInterface(this), "Android");

        loadVideoCall(webView);
    }

    private void loadVideoCall(WebView webView) {
        String filePath = "file:android_asset/call.html";
        webView.loadUrl(filePath);

        webView.setWebViewClient(new WebViewClient() {
            @Override
            public void onPageFinished(WebView view, String url) {
                initializePeer();
            }
        });
    }

    private String uniqueId = "";

    private void initializePeer() {
        uniqueId = getUniqueID();
        callJavascriptFunction("javascript:init(\"" + uniqueId + "\")");

        firebaseRef.child(username).child("incoming").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot snapshot) {
                onCallRequest(snapshot.getValue(String.class));
            }

            @Override
            public void onCancelled(DatabaseError error) {
            }
        });
    }

    private void onCallRequest(String caller) {
        if (caller == null) return;

        findViewById(R.id.callLayout).setVisibility(View.VISIBLE);
        ((TextView) findViewById(R.id.incomingCallTxt)).setText(caller + " is calling...");

        findViewById(R.id.acceptBtn).setOnClickListener(view -> {
            firebaseRef.child(username).child("connId").setValue(uniqueId);
            firebaseRef.child(username).child("isAvailable").setValue(true);

            findViewById(R.id.callLayout).setVisibility(View.GONE);
            switchToControls();
        });

        findViewById(R.id.rejectBtn).setOnClickListener(view -> {
            firebaseRef.child(username).child("incoming").setValue(null);
            findViewById(R.id.callLayout).setVisibility(View.GONE);
        });
    }

    private void switchToControls() {
        findViewById(R.id.inputLayout).setVisibility(View.GONE);
        findViewById(R.id.callControlLayout).setVisibility(View.VISIBLE);
    }

    private String getUniqueID() {
        return UUID.randomUUID().toString();
    }

    private void callJavascriptFunction(String functionString) {
        WebView webView = findViewById(R.id.webView);
        webView.post(() -> webView.evaluateJavascript(functionString, null));
    }

    public void onPeerConnected() {
        isPeerConnected = true;
    }

    @Override
    public void onBackPressed() {
        finish();
    }

    @Override
    protected void onDestroy() {
        firebaseRef.child(username).removeValue();
        WebView webView = findViewById(R.id.webView);
        webView.loadUrl("about:blank");
        super.onDestroy();
    }

}
//        targetUser.addTextChangedListener(new TextWatcher() {
//            @Override
//            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
//
//            }
//
//            @Override
//            public void onTextChanged(CharSequence s, int start, int before, int count) {
//                startVideoCall(targetUser.getText().toString().trim());
//            }
//
//            @Override
//            public void afterTextChanged(Editable s) {
//
//            }
//        });




//    public void startVideoCall(String targetElderId){
//        callBtn.setIsVideoCall(true);
//        callBtn.setResourceID("zego_uikit_call");
//        callBtn.setInvitees(Collections.singletonList(new ZegoUIKitUser(targetElderId, targetElderId)));
//
//
//    }
