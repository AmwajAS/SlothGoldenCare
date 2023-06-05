package com.example.slothgoldencare;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;
import androidx.fragment.app.DialogFragment;

import java.util.ArrayList;

public class SimpleDialog extends Activity {

    private Context context;
    private ArrayList<Integer> mSelectedItems;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        this.context = this;
    }


    public static void showAlertDialog(Context context, int titleResId, int messageResId) {
        String title = context.getString(titleResId);
        String message = context.getString(messageResId);

        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setTitle(title);
        builder.setMessage(message);
        builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });

        AlertDialog alertDialog = builder.create();
        alertDialog.show();
    }


}
