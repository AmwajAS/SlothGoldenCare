package com.example.slothgoldencare;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;

public class ConfirmationDialog extends Activity {


        public interface OnConfirmationListener {
            void onConfirmation();
        }

        public static void showConfirmationDialog(Context context, int titleResId, int messageResId, final OnConfirmationListener listener) {
            AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(context);
            alertDialogBuilder.setTitle(titleResId);
            alertDialogBuilder.setMessage(messageResId);
            alertDialogBuilder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {
                    // Call the callback interface method
                    if (listener != null) {
                        listener.onConfirmation();
                    }
                }
            });
            alertDialogBuilder.setNegativeButton("Cancel", null);
            AlertDialog alertDialog = alertDialogBuilder.create();
            alertDialog.show();
        }
    }





