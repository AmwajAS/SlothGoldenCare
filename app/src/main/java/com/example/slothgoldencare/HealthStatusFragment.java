package com.example.slothgoldencare;

import android.os.Bundle;

import android.widget.*;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.slothgoldencare.Model.Allergy;
import com.example.slothgoldencare.Model.Diagnosis;
import com.example.slothgoldencare.Model.Medicine;
import com.example.slothgoldencare.Model.Surgery;
import com.google.firebase.auth.FirebaseAuth;

import java.util.List;

//This fragment shows the health status depends on which you choose.
public class HealthStatusFragment extends Fragment {

    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    private List<Diagnosis> diagnosisList;
    private List<Surgery> surgeryList;
    private List<Allergy> allergyList;
    private List<Medicine> medicineList;


    public HealthStatusFragment() {
        // Required empty public constructor
    }

    public static HealthStatusFragment newInstance(Bundle args) {
        HealthStatusFragment fragment = new HealthStatusFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            // TODO: Rename and change types of parameters
            String mParam1 = getArguments().getString(ARG_PARAM1);
            String mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_health_status, container, false);
        ListView list = view.findViewById(R.id.health_status_list);
        TextView list_title = view.findViewById(R.id.list_title);

        // back btn to remove the replaced view to the main one.
        Button backBtn = view.findViewById(R.id.back_btn);
        backBtn.setOnClickListener(view1 -> {
            requireActivity().getSupportFragmentManager().beginTransaction().remove(this).commit();
            requireActivity().getSupportFragmentManager().popBackStack();
        });

        //When creating this fragment, args is sent which has the values of the health status item.
        DataBaseHelper dataBaseHelper = new DataBaseHelper(this.getContext());
        Bundle args = getArguments();
        if(args != null){
            String value = args.getString("Button");
            //if value is diagnosis then fill the list with diagnosis
            if(value.equals("diagnosis")){
                list_title.setText(R.string.diagnosis);
                diagnosisList = dataBaseHelper.getDiagnosisByElderlyDocId(FirebaseAuth.getInstance().getUid());
                ArrayAdapter<Diagnosis> diagnosisAdapter = new ArrayAdapter<Diagnosis>(this.getContext(), R.layout.health_status_item_layout, diagnosisList) {
                    @NonNull
                    @Override
                    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {


                        // Get the user object for the current position
                        Diagnosis diagnosis = getItem(position);

                        // Inflate the list item layout
                        if (convertView == null) {
                            convertView = LayoutInflater.from(getContext()).inflate(R.layout.health_status_item_layout, parent, false);
                        }
                        TextView idText = convertView.findViewById(R.id.health_status_item);
                        idText.setText(diagnosis.getDiagnosis());

                        return convertView;
                    }
                };
                list.setAdapter(diagnosisAdapter);

            }else if(value.equals("allergies")){
                //if value is allergies then fill the list with allergies
                list_title.setText(R.string.allergies);
                allergyList = dataBaseHelper.getAllergiesByElderlyDocId(FirebaseAuth.getInstance().getUid());

                ArrayAdapter<Allergy> allergyAdapter = new ArrayAdapter<Allergy>(this.getContext(), R.layout.health_status_item_layout, allergyList) {
                    @NonNull
                    @Override
                    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {


                        // Get the user object for the current position
                        Allergy allergy = getItem(position);

                        // Inflate the list item layout
                        if (convertView == null) {
                            convertView = LayoutInflater.from(getContext()).inflate(R.layout.health_status_item_layout, parent, false);
                        }
                        TextView idText = convertView.findViewById(R.id.health_status_item);
                        idText.setText(allergy.getAllergy());

                        return convertView;
                    }
                };
                list.setAdapter(allergyAdapter);

            }else if (value.equals("medicines")){
                //if value is medicines then fill the list with medicines
                list_title.setText(R.string.medicine);
                medicineList = dataBaseHelper.getMedicinesByElderlyDocId(FirebaseAuth.getInstance().getUid());

                ArrayAdapter<Medicine> medicineAdapter = new ArrayAdapter<Medicine>(this.getContext(), R.layout.health_status_item_layout, medicineList) {
                    @NonNull
                    @Override
                    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {


                        // Get the user object for the current position
                        Medicine medicine = getItem(position);

                        // Inflate the list item layout
                        if (convertView == null) {
                            convertView = LayoutInflater.from(getContext()).inflate(R.layout.health_status_item_layout, parent, false);
                        }
                        TextView idText = convertView.findViewById(R.id.health_status_item);
                        idText.setText(medicine.getMedicine());

                        return convertView;
                    }
                };
                list.setAdapter(medicineAdapter);

            }else if(value.equals("surgeries")){
                //if value is surgeries then fill the list with surgeries
                list_title.setText(R.string.surgery);
                surgeryList = dataBaseHelper.getSurgeriesByElderlyDocId(FirebaseAuth.getInstance().getUid());

                ArrayAdapter<Surgery> surgeryAdapter = new ArrayAdapter<Surgery>(this.getContext(), R.layout.surgery_item_layout, surgeryList) {
                    @NonNull
                    @Override
                    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {


                        // Get the user object for the current position
                        Surgery surgery = getItem(position);

                        // Inflate the list item layout
                        if (convertView == null) {
                            convertView = LayoutInflater.from(getContext()).inflate(R.layout.surgery_item_layout, parent, false);
                        }
                        TextView idText = convertView.findViewById(R.id.surgery_list_item);
                        TextView dateText = convertView.findViewById(R.id.surgery_list_date);
                        idText.setText(surgery.getSurgery());
                        dateText.setText(surgery.getDate().toString());

                        return convertView;
                    }
                };
                list.setAdapter(surgeryAdapter);
            }
        }

        return view;
    }
}