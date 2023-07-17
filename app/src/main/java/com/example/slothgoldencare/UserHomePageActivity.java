package com.example.slothgoldencare;

import android.view.*;
import android.widget.*;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.Intent;
import android.os.Bundle;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class UserHomePageActivity extends AppCompatActivity {

    private Toolbar toolbar;
    private List<Elder> relatives;
    private RecyclerView relativesList;
    private Button addBtn;
    private TextView username;
    private FirebaseAuth auth;
    private FirebaseFirestore db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_home_page);
        //Firebase parameters, authenticated user.
        auth = FirebaseAuth.getInstance();
        db = FirebaseFirestore.getInstance();
        username = findViewById(R.id.username);
        username.setText(auth.getCurrentUser().getDisplayName());

        //Relatives list (in progress)
        relatives = new ArrayList<>();
        Elder elder = new Elder("123456789","habeb","0528222682",Gender.Male);
        relatives.add(elder);
        elder = new Elder("123456788","test","0528222688",Gender.Female);
        relatives.add(elder);
        toolbar = findViewById(R.id.actBar);
        addBtn = findViewById(R.id.addRelativeBtn);
        relativesList = findViewById(R.id.relativesListView);

        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false);
        relativesList.setLayoutManager(layoutManager);

        RecyclerView.Adapter<ElderViewHolder> eldersAdapter = new RecyclerView.Adapter<ElderViewHolder>() {

            @NonNull
            @Override
            public ElderViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.elder_list, parent, false);
                return new ElderViewHolder(itemView);
            }

            @Override
            public void onBindViewHolder(@NonNull ElderViewHolder holder, int position) {
                Elder elder = relatives.get(position);
                holder.relativeImg.setImageResource(R.drawable.man);
                holder.relativeName.setText(elder.getUsername());
            }

            @Override
            public int getItemCount() {
                return relatives.size();
            }
        };

        relativesList.setAdapter(eldersAdapter);

        setSupportActionBar(toolbar);

        addBtn.setOnClickListener(v -> {
            Intent intent = new Intent(getApplicationContext(), AddRelated.class);
            startActivity(intent);
        });

    }

    public static class ElderViewHolder extends RecyclerView.ViewHolder {
        ImageView relativeImg;
        TextView relativeName;

        public ElderViewHolder(View itemView) {
            super(itemView);
            relativeImg = itemView.findViewById(R.id.elder_image);
            relativeName = itemView.findViewById(R.id.name);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.action_menu, menu);
        return true;
    }

}