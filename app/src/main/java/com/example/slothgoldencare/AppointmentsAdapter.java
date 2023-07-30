package com.example.slothgoldencare;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.slothgoldencare.Model.Appointment;

import java.text.SimpleDateFormat;
import java.util.List;
import java.util.Locale;

public class AppointmentsAdapter extends RecyclerView.Adapter<AppointmentsAdapter.AppointmentViewHolder> {

    private Context context;
    private List<Appointment> appointmentsList;

    public AppointmentsAdapter(Context context, List<Appointment> appointmentsList) {
        this.context = context;
        this.appointmentsList = appointmentsList;
    }

    @NonNull
    @Override
    public AppointmentViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_appointment, parent, false);
        return new AppointmentViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull AppointmentViewHolder holder, int position) {
        Appointment appointment = appointmentsList.get(position);
        // Convert Timestamp to a formatted date string
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault());
        String formattedDate = dateFormat.format(appointment.getDate().toDate());
        holder.doctorTextView.setText("Doctor: " + appointment.getDoctor());
        holder.dateTextView.setText("Date and Time: " + formattedDate);
        holder.elderIdTextView.setText("Elder Id: " + appointment.getElderId());
        holder.notesTextView.setText("Notes: " + appointment.getNotes());
    }

    @Override
    public int getItemCount() {
        return appointmentsList.size();
    }

    static class AppointmentViewHolder extends RecyclerView.ViewHolder {
        TextView doctorTextView;
        TextView dateTextView;
        TextView elderIdTextView;
        TextView notesTextView;

        public AppointmentViewHolder(@NonNull View itemView) {
            super(itemView);
            doctorTextView = itemView.findViewById(R.id.doctorTextView);
            dateTextView = itemView.findViewById(R.id.dateTextView);
            elderIdTextView = itemView.findViewById(R.id.elderIdTextView);
            notesTextView = itemView.findViewById(R.id.notesTextView);
        }
    }
}
