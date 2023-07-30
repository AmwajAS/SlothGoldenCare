package com.example.slothgoldencare;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import androidx.annotation.NonNull;

//Class for creating a dialog to add items
//The class shows a dialog depending on the type
public class AddItemDialog extends Dialog {

    //which type to add
    public enum ItemType {
        SURGERY, DIAGNOSIS,ALLERGY,MEDICINE
    }

    private EditText editTextItemName;
    private EditText editTextItemDate; // For surgeries
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
        } else if (itemType == ItemType.DIAGNOSIS || itemType == ItemType.ALLERGY || itemType == ItemType.MEDICINE) {
            // For diagnoses,medicine,allergy use the original layout
            setContentView(R.layout.dialog_add_item);
        }

        // Initialize the common views and buttons
        editTextItemName = findViewById(R.id.editTextItemName);
        Button btnCancel = findViewById(R.id.btnCancel);
        Button btnAddItem = findViewById(R.id.btnAddItem);

        btnCancel.setOnClickListener(v -> dismiss());

        btnAddItem.setOnClickListener(v -> {
            String itemDate = null;
            String itemName = editTextItemName.getText().toString().trim();
            if(itemType == ItemType.SURGERY) {
                itemDate = editTextItemDate.getText().toString().trim();
            }
            if (!itemName.isEmpty()) {
                listener.onAddItem(itemType, itemName, itemDate);
                dismiss();
            }
        });
    }
}
