package com.example.slothgoldencare;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.TextView;

import com.example.slothgoldencare.Model.Elder;

import java.util.ArrayList;
import java.util.List;
public class ElderAdapter {


    public class ElderListAdapter extends BaseAdapter implements Filterable {
        private List<Elder> originalData;
        private List<Elder> filteredData;
        private LayoutInflater inflater;
        private ElderFilter filter = new ElderFilter();

        public ElderListAdapter(Context context, List<Elder> data) {
            this.originalData = data;
            this.filteredData = data;
            this.inflater = LayoutInflater.from(context);
        }

        @Override
        public int getCount() {
            return filteredData.size();
        }

        @Override
        public Elder getItem(int position) {
            return filteredData.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            View view = convertView;
            if (view == null) {
                view = inflater.inflate(R.layout.doctor_elderly_item, parent, false);
            }

            // Get the Elder object for the current position
            Elder elder = getItem(position);

            // Find the TextView for the ID and set the text
            TextView idText = view.findViewById(R.id.elderly_id);
            idText.setText(elder.getID());

            // Find the TextView for the username and set the text (for debugging)
            TextView usernameText = view.findViewById(R.id.elderly_username);
            usernameText.setText(elder.getUsername());

            return view;
        }

        @Override
        public Filter getFilter() {
            return filter;
        }

        private class ElderFilter extends Filter {
            @Override
            protected FilterResults performFiltering(CharSequence constraint) {
                FilterResults results = new FilterResults();

                if (constraint == null || constraint.length() == 0) {
                    // If the search bar is empty, show the original data
                    results.values = originalData;
                    results.count = originalData.size();
                } else {
                    // If there's a query, apply custom filtering for partial matches anywhere in the ID
                    List<Elder> filteredList = new ArrayList<>();
                    String filterPattern = constraint.toString().toLowerCase().trim();
                    for (Elder elder : originalData) {
                        if (elder.getID().contains(filterPattern)) {
                            filteredList.add(elder);
                        }
                    }
                    results.values = filteredList;
                    results.count = filteredList.size();
                }

                return results;
            }

            @Override
            protected void publishResults(CharSequence constraint, FilterResults results) {
                filteredData = (List<Elder>) results.values;
                notifyDataSetChanged();
            }
        }
    }

}

