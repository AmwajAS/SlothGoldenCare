package com.example.slothgoldencare;

import android.app.Dialog;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;

import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.DialogFragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.slothgoldencare.Model.WorkAndPayment;

import java.util.List;

public class ConfirmationDialogFragment extends DialogFragment {
    private List<WorkAndPayment> confirmedPayments;

    public ConfirmationDialogFragment(List<WorkAndPayment> confirmedPayments) {
        this.confirmedPayments = confirmedPayments;
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setTitle("Payment Confirmation For Your Working Days")
                .setPositiveButton("OK", null) // Set a positive button (OK) without any action listener
                .setNegativeButton("Cancel", null); // Set a negative button (Cancel) without any action listener

        // Create a custom view for the dialog
        LayoutInflater inflater = requireActivity().getLayoutInflater();
        View view = inflater.inflate(R.layout.dialog_confirmation, null);
        builder.setView(view);

        RecyclerView recyclerView = view.findViewById(R.id.confirmedPaymentsRecyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        WorkAndPaymentAdapter adapter = new WorkAndPaymentAdapter(getActivity(), confirmedPayments);
        recyclerView.setAdapter(adapter);

        return builder.create();
    }
}