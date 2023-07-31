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

import java.text.SimpleDateFormat;
import java.util.List;
import java.util.Locale;

public class WorkAndPaymentAdapter extends RecyclerView.Adapter<WorkAndPaymentAdapter.WorkAndPaymentViewHolder> {

    private Context context;
    private List<WorkAndPayment> workAndPaymentList;

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
            // Convert Timestamp to a formatted date string
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault());
        String dateDay = dateFormat.format(workAndPayment.getDateDay().toDate());
        String paidDate = dateFormat.format(workAndPayment.getPaidDate().toDate());

        holder.dateDayTextView.setText("Date Day: " + dateDay);
        holder.doctorIdTextView.setText("Doctor ID: " + workAndPayment.getDoctorId());
        holder.hoursTextView.setText("Hours: " + workAndPayment.getHours());
        holder.paidDateTextView.setText("Paid Date: " +paidDate);
        holder.isPaidTextView.setText("Is Paid: " + workAndPayment.getIsPaid());

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


        public WorkAndPaymentViewHolder(@NonNull View itemView) {
            super(itemView);
            dateDayTextView = itemView.findViewById(R.id.dateDayTextView);
            doctorIdTextView = itemView.findViewById(R.id.doctorIdTextView);
            hoursTextView = itemView.findViewById(R.id.hoursTextView);
            isPaidTextView = itemView.findViewById(R.id.isPaidTextView);
            paidDateTextView = itemView.findViewById(R.id.paidDateTextView);
        }
    }
}
