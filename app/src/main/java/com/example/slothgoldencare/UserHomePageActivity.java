package com.example.slothgoldencare;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.view.*;
import android.widget.*;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.Intent;
import android.os.Bundle;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.slothgoldencare.Model.Elder;
import com.example.slothgoldencare.Model.Gender;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.auth.FirebaseAuth;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class UserHomePageActivity extends AppCompatActivity {

    private List<Elder> relatives;
    private FirebaseAuth auth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_home_page);
        //authenticated user.
        auth = FirebaseAuth.getInstance();
        TextView username = findViewById(R.id.username);
        username.setText(auth.getCurrentUser().getDisplayName());

        bottomNavigationView();
        //Log out button to sign out of authentication
        Button logOutBtn = findViewById(R.id.log_out_btn);
        logOutBtn.setOnClickListener(view -> {
            auth.signOut();
            Intent intent = new Intent(this.getApplicationContext(), LoginActivity.class);
            startActivity(intent);
        });

        relatives = new ArrayList<>();
        relatives = GetRelativeElderlies();

        Toolbar toolbar = findViewById(R.id.actBar);
        Button addBtn = findViewById(R.id.addRelativeBtn);
        RecyclerView relativesList = findViewById(R.id.relativesListView);

        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false);
        relativesList.setLayoutManager(layoutManager);

        //Elderlies that are reletaive are shown in the bottom in a recycler view.
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

                //clicking on a specific elderly to show the profile.
                holder.itemView.setOnClickListener(view -> {
                    Toast.makeText(view.getContext(), "Elderly Clicked: " + elder.getUsername(), Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(UserHomePageActivity.this, VisitElderlyProfileActivity.class);
                    intent.putExtra("elderlyId", elder.getID());
                    startActivity(intent);
                });
            }

            @Override
            public int getItemCount() {
                return relatives.size();
            }
        };

        relativesList.setAdapter(eldersAdapter);
        setSupportActionBar(toolbar);
        //Add elderly to the list
        addBtn.setOnClickListener(v -> {
            Intent intent = new Intent(getApplicationContext(), AddRelatedActivity.class);
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

    /*
    This method retrieve the Elder and their Relatives from the FireStore.
     */
    public List<Elder> GetRelativeElderlies() {
        List<Elder> temp = new ArrayList<>();
        // Assuming you have a valid SQLiteDatabase object named "db"

        String query = "SELECT * FROM ELDER_RELATIVE INNER JOIN ELDERLIES ON ELDER_RELATIVE.ElderID = ELDERLIES.docId " +
                "WHERE ELDER_RELATIVE.RelativeID = ?";
        String[] selectionArgs = {auth.getUid()};
        DataBaseHelper dataBaseHelper = new DataBaseHelper(this);
        SQLiteDatabase sqLiteDatabase = dataBaseHelper.getWritableDatabase();
        Cursor cursor = sqLiteDatabase.rawQuery(query, selectionArgs);
        if (cursor != null && cursor.moveToFirst()) {
            do {

                // Extract data from the cursor for each matching row
                String documentId = cursor.getString(cursor.getColumnIndexOrThrow("docId"));
                String elderId = cursor.getString(cursor.getColumnIndexOrThrow("ID"));
                String name = cursor.getString(cursor.getColumnIndexOrThrow("name"));
                String email = cursor.getString(cursor.getColumnIndexOrThrow("email"));
                String password = cursor.getString(cursor.getColumnIndexOrThrow("password"));
                String phone = cursor.getString(cursor.getColumnIndexOrThrow("phone"));
                String dob = cursor.getString(cursor.getColumnIndexOrThrow("dateOfBirth"));
                String genderString = cursor.getString(cursor.getColumnIndexOrThrow("gender"));

                Gender gender = Elder.GenderConvertor(genderString);
                Date dateOfBirth = Elder.convertStringIntoDate(dob);

                Elder elder = new Elder(elderId, name, phone, dateOfBirth, gender, email, password);
                elder.setDocId(documentId);
                temp.add(elder);

            } while (cursor.moveToNext());
        }

// Close the cursor when done
        if (cursor != null) {
            cursor.close();
        }
        return temp;
    }

    /*
      this method handle the selected items / buttons of the bottom navigation bar.
       */
    public void bottomNavigationView() {
        BottomNavigationView bottomNavigationView = findViewById(R.id.bottomNavigation);
        bottomNavigationView.setSelectedItemId(R.id.home);
        Menu menu = bottomNavigationView.getMenu();
        MenuItem currentItem = menu.findItem(R.id.current);
        // Hiding the "current" menu item
        currentItem.setVisible(false);

        bottomNavigationView.setOnItemSelectedListener(item -> {
            switch (item.getItemId()) {
                case R.id.home:
                    replaceView(UserHomePageActivity.class);
                    return true;
                case R.id.settings:
                    replaceView(SettingsActivity.class);
                    return true;
                case R.id.profile:
                    return true;
            }
            return false;
        });

        MenuItem profileItem = menu.findItem(R.id.profile);
        profileItem.setVisible(false);
    }

    public void replaceView(Class classView) {
        startActivity(new Intent(getApplicationContext(), classView));
        overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
        finish();
    }


}