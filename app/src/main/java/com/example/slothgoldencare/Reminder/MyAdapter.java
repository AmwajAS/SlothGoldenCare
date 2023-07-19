package com.example.slothgoldencare.Reminder;


import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.slothgoldencare.R;

import java.util.ArrayList;


/**
 * The MyAdapter class is a RecyclerView adapter responsible for displaying reminders in a list.
 */
public class MyAdapter extends RecyclerView.Adapter<MyAdapter.myviewholder> {
    ArrayList<Model> dataholder = new ArrayList<Model>();                                               //array list to hold the reminders


    /**
     * Constructor for the MyAdapter class.
     *
     * @param dataholder The ArrayList containing the reminder data.
     */
    public MyAdapter(ArrayList<Model> dataholder) {
        this.dataholder = dataholder;
    }

    @NonNull
    @Override
    public myviewholder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        // Inflates the XML file in RecyclerView
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.single_reminder_file, parent, false);
        return new myviewholder(view);
    }


    @Override
    public void onBindViewHolder(@NonNull myviewholder holder, int position) {
        // Binds the single reminder objects to the RecyclerView
        holder.mTitle.setText(dataholder.get(position).getTitle());
        holder.mDate.setText(dataholder.get(position).getDate());
        holder.mTime.setText(dataholder.get(position).getTime());
    }

    @Override
    public int getItemCount() {
        return dataholder.size();
    }


    /**
     * The myviewholder class represents the ViewHolder for the RecyclerView.
     */
    class myviewholder extends RecyclerView.ViewHolder {

        TextView mTitle, mDate, mTime;


        /**
         * Constructor for the myviewholder class.
         *
         * @param itemView The view associated with each item in the RecyclerView.
         */
        public myviewholder(@NonNull View itemView) {
            super(itemView);
            //holds the reference of the materials to show data in recyclerview
            mTitle = (TextView) itemView.findViewById(R.id.txtTitle);
            mDate = (TextView) itemView.findViewById(R.id.txtDate);
            mTime = (TextView) itemView.findViewById(R.id.txtTime);
        }
    }
}
