package com.example.slothgoldencare;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.slothgoldencare.DataBaseHelper.DataBaseHelper;
import com.example.slothgoldencare.Model.User;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;


import java.util.ArrayList;
import java.util.List;

public class ContactsActivity extends AppCompatActivity {


    private FirebaseAuth auth;
    private FirebaseFirestore db;
    private Toolbar toolbar;
    private List<User> relatives;
    private RecyclerView relativesList;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_contacts);
        //Firebase parameters, authenticated user.
        auth = FirebaseAuth.getInstance();
        db = FirebaseFirestore.getInstance();

        //Relatives list (in progress)
        relatives = new ArrayList<>();
        relatives = getRelativeElderlies();

        bottomNavigationView();

        //toolbar = findViewById(R.id.actBar);
        relativesList = findViewById(R.id.relativesListView);

        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        relativesList.setLayoutManager(layoutManager);

        RecyclerView.Adapter<ElderViewHolder> usersAdapter = new RecyclerView.Adapter<ElderViewHolder>() {

            @NonNull
            @Override
            public ElderViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.elder_list, parent, false);
                return new ElderViewHolder(itemView);
            }

            @Override
            public void onBindViewHolder(@NonNull ElderViewHolder holder, int position) {
                User user = relatives.get(position);
                holder.relativeImg.setImageResource(R.drawable.man);
                holder.relativeName.setText(user.getUsername());

                //clicking on a specific elderly to show the profile.
                holder.itemView.setOnClickListener(view -> {
                    Toast.makeText(view.getContext(), "" + user.getUsername(), Toast.LENGTH_LONG).show();

                });
            }


            @Override
            public int getItemCount() {
                return relatives.size();
            }
        };

        relativesList.setAdapter(usersAdapter);

        setSupportActionBar(toolbar);


    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                // myIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                Intent intent = new Intent(ContactsActivity.this, HomePageActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                // app icon in action bar clicked; go home
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    public boolean onSupportNavigateUp() {
        finish();
        return true;
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


    public List<User> getRelativeElderlies() {
        List<User> temp = new ArrayList<>();
        String query = "SELECT USERS.* \n" +
                "FROM ELDER_RELATIVE INNER JOIN USERS ON ELDER_RELATIVE.RelativeID = USERS.docId \n" +
                "INNER JOIN ELDERLIES ON  ELDER_RELATIVE.ElderID = ELDERLIES.docId\n" +
                "WHERE ELDER_RELATIVE.ElderID  = ?";

        String[] selectionArgs = {auth.getUid()}; // Make sure auth is initialized with FirebaseAuth.getInstance().

        // Initialize the DataBaseHelper with the correct context
        DataBaseHelper dataBaseHelper = new DataBaseHelper(this);
        SQLiteDatabase sqLiteDatabase = dataBaseHelper.getWritableDatabase();
        Cursor cursor = sqLiteDatabase.rawQuery(query, selectionArgs);

        if (cursor != null && cursor.moveToFirst()) {
            do {
                // Extract data from the cursor for each matching row
                // String documentId = cursor.getString(cursor.getColumnIndexOrThrow("docId"));
                String userId = cursor.getString(cursor.getColumnIndexOrThrow("ID"));
                String name = cursor.getString(cursor.getColumnIndexOrThrow("user_name"));
                String email = cursor.getString(cursor.getColumnIndexOrThrow("user_email"));
                String password = cursor.getString(cursor.getColumnIndexOrThrow("user_password"));
                String phone = cursor.getString(cursor.getColumnIndexOrThrow("user_phone"));

                User user = new User(userId, name, phone, email, password);
                //  user.setDocId(documentId);
                temp.add(user);

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
        bottomNavigationView.setSelectedItemId(R.id.current);
        Menu menu = bottomNavigationView.getMenu();
        MenuItem currentItem = menu.findItem(R.id.current);
        // Hiding the "current" menu item
        currentItem.setVisible(false);

        bottomNavigationView.setOnItemSelectedListener(item -> {
            switch (item.getItemId()) {
                case R.id.home:
                    replaceView(HomePageActivity.class);
                    return true;
                case R.id.settings:
                    replaceView(SettingsActivity.class);
                    return true;
                case R.id.profile:
                    replaceView(ProfileActivity.class);
                    return true;
            }
            return false;
        });
    }

    public void replaceView(Class classView) {
        startActivity(new Intent(getApplicationContext(), classView));
        overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
        finish();
    }

}
