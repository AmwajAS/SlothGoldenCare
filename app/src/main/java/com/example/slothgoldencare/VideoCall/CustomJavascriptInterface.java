package com.example.slothgoldencare.VideoCall;


import android.webkit.JavascriptInterface;

public class CustomJavascriptInterface {
    private CallElderActivity callActivity;

    public CustomJavascriptInterface(CallElderActivity callActivity) {
        this.callActivity = callActivity;
    }

    @JavascriptInterface
    public void onPeerConnected() {
        callActivity.onPeerConnected();
    }
}

