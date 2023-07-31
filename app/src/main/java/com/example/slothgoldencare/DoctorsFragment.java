package com.example.slothgoldencare;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.TextView;

import com.example.slothgoldencare.Model.Doctor;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 * Use the  factory method to
 * create an instance of this fragment.
 */
public class DoctorsFragment extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    private List<Doctor> doctorsList;
    private TextView list_title;
    private Button backBtn;

    private DataBaseHelper dbHelper;
    private ArrayAdapter<Doctor> doctorsAdapter;
    private FirebaseAuth auth;

    private FirebaseFirestore db;
    private ListView list;

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;


    public DoctorsFragment() {
        // Required empty public constructor
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    // Initialize views and variables & Get data from the database and populate the list.
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_doctors, container, false);

        initViews(view);
        initFirebase();
        loadDoctorsList();

        return view;
    }

    // Initialize views and set click listeners
    private void initViews(View view) {
        list = view.findViewById(R.id.doctors_list);
        backBtn = view.findViewById(R.id.back_btn);
        backBtn.setOnClickListener(view1 -> {
            requireActivity().getSupportFragmentManager().beginTransaction().remove(this).commit();
            requireActivity().getSupportFragmentManager().popBackStack();
        });
    }

    // Initialize Firebase instance
    private void initFirebase() {
        db = FirebaseFirestore.getInstance();
        dbHelper = new DataBaseHelper(this.getContext());
        auth = FirebaseAuth.getInstance();
    }

    // Load data from the database and populate the list
    private void loadDoctorsList() {
        doctorsList = dbHelper.getDoctors();
        doctorsAdapter = new ArrayAdapter<Doctor>(this.getContext(), R.layout.health_status_item_layout_doctor, doctorsList) {
            @NonNull
            @Override
            public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
                // Get the user object for the current position
                Doctor doctor = getItem(position);
                // Inflate the list item layout
                if (convertView == null) {
                    convertView = LayoutInflater.from(getContext()).inflate(R.layout.doctor_list_item, parent, false);
                }

                TextView idText = convertView.findViewById(R.id.health_status_item);
                TextView specText = convertView.findViewById(R.id.spec_item);

                idText.setText(doctor.getUsername());
                specText.setText(doctor.getSpecialization());
                ImageButton appBtn = convertView.findViewById(R.id.appointment_btn);

                // Hide appointment button if the user type is not elder
                if (!checkUserType()) {
                    appBtn.setVisibility(View.GONE);
                } else {
                    appBtn.setOnClickListener(view -> {
                        // Handle click event here
                        openAppointmentActivity(doctor.getID());
                    });
                }
                return convertView;
            }
        };

        list.setAdapter(doctorsAdapter);
    }

    // Check the user type (elder or not)
    public boolean checkUserType() {
        if (dbHelper.getElderByDocumentId(auth.getUid()) != null) {
            return true;
        } else if (dbHelper.getUserByDocumentId(auth.getUid()) != null) {
            return false;
        }
        return false;
    }

    // Open the AppointmentSchedulingActivity with the selected doctor ID
    private void openAppointmentActivity(String doctorId) {
        Intent intent = new Intent(getContext(), AppointmentSchedulingActivity.class);
        intent.putExtra("doctorId", doctorId);
        startActivity(intent);
    }

}