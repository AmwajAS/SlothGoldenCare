package com.example.slothgoldencare.Reminder;


import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.slothgoldencare.R;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class MyAdapter extends RecyclerView.Adapter<MyAdapter.myviewholder> {
    List<Reminder> dataholder = new ArrayList<Reminder>();                                               //array list to hold the reminders

    public MyAdapter(List<Reminder> dataholder) {
        this.dataholder = dataholder;
    }

    @NonNull
    @Override
    public myviewholder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.single_reminder_file, parent, false);  //inflates the xml file in recyclerview
        return new myviewholder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull myviewholder holder, int position) {
        holder.mTitle.setText(dataholder.get(position).getTitle());

        // Get the date from the Reminder object
        Date date = dataholder.get(position).getDate();

        // Check if the date is not null before formatting and setting it to the TextView
        if (date != null) {
            SimpleDateFormat dateFormat = new SimpleDateFormat("MMM dd, yyyy", Locale.getDefault());
            String formattedDate = dateFormat.format(date);
            holder.mDate.setText(formattedDate);
        } else {
            // Use the current date when the Date object is null
            SimpleDateFormat dateFormat = new SimpleDateFormat("MMM dd, yyyy", Locale.getDefault());
            String currentDate = dateFormat.format(new Date());
            holder.mDate.setText(currentDate);
        }

        holder.mTime.setText(dataholder.get(position).getTime());
    }



    @Override
    public int getItemCount() {
        return dataholder.size();
    }

    class myviewholder extends RecyclerView.ViewHolder {

        TextView mTitle, mDate, mTime;

        public myviewholder(@NonNull View itemView) {
            super(itemView);

            mTitle = (TextView) itemView.findViewById(R.id.txtTitle);                               //holds the reference of the materials to show data in recyclerview
            mDate = (TextView) itemView.findViewById(R.id.txtDate);
            mTime = (TextView) itemView.findViewById(R.id.txtTime);
        }
    }
}
