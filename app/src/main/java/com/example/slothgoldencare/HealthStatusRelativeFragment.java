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

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link HealthStatusFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class HealthStatusRelativeFragment extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    private ListView list;
    private List<Diagnosis> diagnosisList;
    private List<Surgery> surgeryList;
    private List<Allergy> allergyList;
    private List<Medicine> medicineList;
    private TextView list_title;
    private Button backBtn;


    private ImageView imageViewHealth;

    public HealthStatusRelativeFragment() {
        // Required empty public constructor
    }

    public static HealthStatusRelativeFragment newInstance(Bundle args) {
        HealthStatusRelativeFragment fragment = new HealthStatusRelativeFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_health_status, container, false);
        list = view.findViewById(R.id.health_status_list);
        list_title = view.findViewById(R.id.list_title);

        // back btn to remove the replaced view to the main one.
        backBtn = view.findViewById(R.id.back_btn);
        backBtn.setOnClickListener(view1 -> {
            requireActivity().getSupportFragmentManager().beginTransaction().remove(this).commit();
            requireActivity().getSupportFragmentManager().popBackStack();
        });

        DataBaseHelper dataBaseHelper = new DataBaseHelper(this.getContext());
        Bundle args = getArguments();
        if(args != null){
            String value = args.getString("Button");
            String elderlyDocId = args.getString("elderlyDocId");
            if(value.equals("diagnosis")){
                list_title.setText(R.string.diagnosis);
                diagnosisList = dataBaseHelper.getDiagnosisByElderlyDocId(elderlyDocId);
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
                list_title.setText(R.string.allergies);
                allergyList = dataBaseHelper.getAllergiesByElderlyDocId(elderlyDocId);

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

            }else if(value.equals("medicines")){
                list_title.setText(R.string.medicine);
                medicineList = dataBaseHelper.getMedicinesByElderlyDocId(elderlyDocId);

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
                list_title.setText(R.string.surgery);
                surgeryList = dataBaseHelper.getSurgeriesByElderlyDocId(elderlyDocId);

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