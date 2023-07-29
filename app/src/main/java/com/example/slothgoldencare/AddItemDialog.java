package com.example.slothgoldencare;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import androidx.annotation.NonNull;

import java.lang.reflect.Type;

public class AddItemDialog extends Dialog {

    public enum ItemType {
        SURGERY, DIAGNOSIS,ALLERGY,MEDICINE
    }

    // Add more item types as needed

    // Other existing fields and methods...

    private EditText editTextItemName;
    private EditText editTextItemDate; // For surgeries
    private Button btnCancel, btnAddItem;
    private ItemType itemType;

    public interface OnAddItemClickListener {
        void onAddItem(ItemType itemType, String itemName, String itemDate);
    }

    private OnAddItemClickListener listener;

    public AddItemDialog(@NonNull Context context, ItemType itemType, OnAddItemClickListener listener) {
        super(context);
        this.itemType = itemType;
        this.listener = listener;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        WindowManager.LayoutParams layoutParams = new WindowManager.LayoutParams();
        layoutParams.copyFrom(getWindow().getAttributes());
        layoutParams.width = WindowManager.LayoutParams.MATCH_PARENT; // Set your desired width here
        layoutParams.height = WindowManager.LayoutParams.WRAP_CONTENT; // Set your desired height here
        getWindow().setAttributes(layoutParams);

        if (itemType == ItemType.SURGERY) {
            // For surgeries, use a different layout
            setContentView(R.layout.dialog_add_surgery);
            // Initialize the surgery-specific views and buttons
            editTextItemDate = findViewById(R.id.editTextItemDate);
            // Set up onClickListener for surgery-specific buttons...
        } else if (itemType == ItemType.DIAGNOSIS || itemType == ItemType.ALLERGY || itemType == ItemType.MEDICINE) {
            // For diagnoses, use the original layout
            setContentView(R.layout.dialog_add_item);
        }

        // Initialize the common views and buttons
        editTextItemName = findViewById(R.id.editTextItemName);
        btnCancel = findViewById(R.id.btnCancel);
        btnAddItem = findViewById(R.id.btnAddItem);

        btnCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
            }
        });

        btnAddItem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v){
                String itemDate = null;
                String itemName = editTextItemName.getText().toString().trim();
                if(itemType == ItemType.SURGERY) {
                    itemDate = editTextItemDate.getText().toString().trim();
                }
                if (!itemName.isEmpty()) {
                    listener.onAddItem(itemType, itemName, itemDate);
                    dismiss();
                }
            }
        });
    }
}
