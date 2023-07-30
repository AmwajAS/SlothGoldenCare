package com.example.slothgoldencare;

import android.telephony.SmsManager;

public class SmsUtil {

    //sending SMS via phone class
    public static void sendSms(String phoneNumber, String message) {
        try {
            SmsManager smsManager = SmsManager.getDefault();
            smsManager.sendTextMessage(phoneNumber, null, message, null, null);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
