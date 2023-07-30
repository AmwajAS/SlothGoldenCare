package com.example.slothgoldencare;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.slothgoldencare.Model.WorkAndPayment;

import java.util.List;

public class WorkAndPaymentAdapter extends RecyclerView.Adapter<WorkAndPaymentAdapter.WorkAndPaymentViewHolder> {

    private Context context;
    private List<WorkAndPayment> workAndPaymentList;
    private OnWorkAndPaymentClickListener onWorkAndPaymentClickListener;

    public interface OnWorkAndPaymentClickListener {
        void onWorkAndPaymentClick(int position);
    }
    public WorkAndPaymentAdapter(Context context, List<WorkAndPayment> workAndPaymentList, OnWorkAndPaymentClickListener listener) {
        this.context = context;
        this.workAndPaymentList = workAndPaymentList;
        this.onWorkAndPaymentClickListener = listener;
    }

    public WorkAndPaymentAdapter(Context context, List<WorkAndPayment> workAndPaymentList) {
        this.context = context;
        this.workAndPaymentList = workAndPaymentList;
    }

    @NonNull
    @Override
    public WorkAndPaymentViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_work_and_payment, parent, false);
        return new WorkAndPaymentViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull WorkAndPaymentViewHolder holder, int position) {
        WorkAndPayment workAndPayment = workAndPaymentList.get(position);

        holder.dateDayTextView.setText("Date Day: " + workAndPayment.getDateDay());
        holder.doctorIdTextView.setText("Doctor ID: " + workAndPayment.getDoctorId());
        holder.hoursTextView.setText("Hours: " + workAndPayment.getHours());
        holder.isPaidTextView.setText("Is Paid: " + workAndPayment.isPaid());

        // Display the paid date if the workAndPayment is paid, otherwise hide it
        if (workAndPayment.isPaid()) {
            holder.paidDateTextView.setVisibility(View.VISIBLE);
            holder.paidDateTextView.setText("Paid Date: " + workAndPayment.getPaidDate());
            holder.viewConfirmationButton.setVisibility(View.VISIBLE);
        } else {
            holder.paidDateTextView.setVisibility(View.GONE);
            holder.viewConfirmationButton.setVisibility(View.GONE);
        }
        // Set a click listener for the viewConfirmationButton
        holder.viewConfirmationButton.setOnClickListener(v -> {
            if (onWorkAndPaymentClickListener != null) {
                onWorkAndPaymentClickListener.onWorkAndPaymentClick(position);
            }
        });
    }

    @Override
    public int getItemCount() {
        return workAndPaymentList.size();
    }

    static class WorkAndPaymentViewHolder extends RecyclerView.ViewHolder {
        TextView dateDayTextView;
        TextView doctorIdTextView;
        TextView hoursTextView;
        TextView isPaidTextView;
        TextView paidDateTextView;
        Button viewConfirmationButton;

        public WorkAndPaymentViewHolder(@NonNull View itemView) {
            super(itemView);
            dateDayTextView = itemView.findViewById(R.id.dateDayTextView);
            doctorIdTextView = itemView.findViewById(R.id.doctorIdTextView);
            hoursTextView = itemView.findViewById(R.id.hoursTextView);
            isPaidTextView = itemView.findViewById(R.id.isPaidTextView);
            paidDateTextView = itemView.findViewById(R.id.paidDateTextView);
            viewConfirmationButton = itemView.findViewById(R.id.viewConfirmationButton);
        }
    }
}
