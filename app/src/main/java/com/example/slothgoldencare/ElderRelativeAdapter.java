package com.example.slothgoldencare;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import java.util.ArrayList;


public class ElderRelativeAdapter extends ArrayAdapter<Elder> {

    public ElderRelativeAdapter(Context context, ArrayList<Elder> relatedElders){
        super(context, R.layout.list_item, relatedElders);

    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {

        User user = getItem(position);
        if(convertView == null){
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.list_item,parent, false);
        }
        ImageView img = convertView.findViewById(R.id.profile);
        TextView username = convertView.findViewById(R.id.personName);

        img.setImageResource(user.getImgID());
        username.setText(user.getUsername());


        return super.getView(position, convertView, parent);
    }
}
