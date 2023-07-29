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
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;

import java.util.Date;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link HealthStatusFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class HealthStatusDoctorFragment extends Fragment {

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
    private Button addBtn;
    private String elderlyDocId;
    private DataBaseHelper dataBaseHelper;
    private ArrayAdapter<Allergy> allergiesAdapter;
    private ArrayAdapter<Diagnosis> diagnosisAdapter;
    private ArrayAdapter<Medicine> medicinesAdapter;
    private ArrayAdapter<Surgery> surgeriesAdapter;
    private FirebaseFirestore db;


    private ImageView imageViewHealth;

    public HealthStatusDoctorFragment() {
        // Required empty public constructor
    }

    public static HealthStatusDoctorFragment newInstance(Bundle args) {
        HealthStatusDoctorFragment fragment = new HealthStatusDoctorFragment();
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
        View view = inflater.inflate(R.layout.fragment_health_status_doctor, container, false);
        list = view.findViewById(R.id.health_status_list);
        list_title = view.findViewById(R.id.list_title);

        //Variables of args sent by the activity.
        db = FirebaseFirestore.getInstance();
        dataBaseHelper = new DataBaseHelper(this.getContext());
        Bundle args = getArguments();
        String value = args.getString("Button");
        elderlyDocId = args.getString("elderly");

        // back btn to remove the replaced view to the main one.
        backBtn = view.findViewById(R.id.back_btn);
        backBtn.setOnClickListener(view1 -> {
            requireActivity().getSupportFragmentManager().beginTransaction().remove(this).commit();
            requireActivity().getSupportFragmentManager().popBackStack();
        });

        addBtn = view.findViewById(R.id.add_to_list);
        addBtn.setOnClickListener(view1 -> {
            if(value.equals("diagnosis")){
                showAddItemDialog(AddItemDialog.ItemType.DIAGNOSIS);
            }else if(value.equals("surgeries")){
                showAddItemDialog(AddItemDialog.ItemType.SURGERY);
            }else if(value.equals("allergies")){
                showAddItemDialog(AddItemDialog.ItemType.ALLERGY);
            }else if(value.equals("medicines")){
                showAddItemDialog(AddItemDialog.ItemType.MEDICINE);
            }

        });



        if(args != null){

            if(value.equals("diagnosis")){
                list_title.setText(R.string.diagnosis);
                diagnosisList = dataBaseHelper.getDiagnosisByElderlyDocId(elderlyDocId);
                diagnosisAdapter = new ArrayAdapter<Diagnosis>(this.getContext(), R.layout.health_status_item_layout_doctor, diagnosisList) {
                    @NonNull
                    @Override
                    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {

                        // Get the user object for the current position
                        Diagnosis diagnosis = getItem(position);
                        // Inflate the list item layout
                        if (convertView == null) {
                            convertView = LayoutInflater.from(getContext()).inflate(R.layout.health_status_item_layout_doctor, parent, false);
                        }

                        TextView idText = convertView.findViewById(R.id.health_status_item);
                        idText.setText(diagnosis.getDiagnosis());
                        ImageButton deletBtn = convertView.findViewById(R.id.delete_btn);
                        deletBtn.setOnClickListener(view->{
                            db.collection("Diagnosis")
                                    .whereEqualTo("diagnosis",diagnosis.getDiagnosis())
                                    .whereEqualTo("elderlyDocId",diagnosis.getElderlyDocId())
                                    .get().addOnCompleteListener(task -> {
                                        if(task.isSuccessful()){
                                            for (QueryDocumentSnapshot document : task.getResult()) {
                                                // Delete the matching document from Firestore
                                                db.collection("Diagnosis").document(document.getId()).delete();
                                                if(dataBaseHelper.deleteDiagnosis(diagnosis)){
                                                    diagnosisList.remove(position);
                                                    Toast.makeText(getContext(), R.string.info_delete_success, Toast.LENGTH_LONG).show();
                                                    diagnosisAdapter.notifyDataSetChanged();
                                                }else{
                                                    Toast.makeText(getContext(), R.string.info_delete_fail, Toast.LENGTH_LONG).show();
                                                }
                                            }
                                        } else {
                                            Toast.makeText(getContext(), task.getException().getMessage().toString(), Toast.LENGTH_LONG).show();
                                        }
                                    });
                        });

                        return convertView;
                    }
                };
                list.setAdapter(diagnosisAdapter);

            }else if(value.equals("medicines")){
                list_title.setText(R.string.medicine);
                medicineList = dataBaseHelper.getMedicinesByElderlyDocId(elderlyDocId);
               medicinesAdapter = new ArrayAdapter<Medicine>(this.getContext(), R.layout.health_status_item_layout_doctor, medicineList) {
                    @NonNull
                    @Override
                    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {

                        // Get the user object for the current position
                        Medicine medicine = getItem(position);
                        // Inflate the list item layout
                        if (convertView == null) {
                            convertView = LayoutInflater.from(getContext()).inflate(R.layout.health_status_item_layout_doctor, parent, false);
                        }

                        TextView idText = convertView.findViewById(R.id.health_status_item);
                        idText.setText(medicine.getMedicine());
                        ImageButton deletBtn = convertView.findViewById(R.id.delete_btn);
                        deletBtn.setOnClickListener(view->{
                            db.collection("Medicines")
                                    .whereEqualTo("medicine",medicine.getMedicine())
                                    .whereEqualTo("elderlyDocId",medicine.getElderlyDocId())
                                    .get().addOnCompleteListener(task -> {
                                        if(task.isSuccessful()){
                                            for (QueryDocumentSnapshot document : task.getResult()) {
                                                // Delete the matching document from Firestore
                                                db.collection("Medicines").document(document.getId()).delete();
                                                if(dataBaseHelper.deleteMedicine(medicine)){
                                                    medicineList.remove(position);
                                                    Toast.makeText(getContext(), R.string.info_delete_success, Toast.LENGTH_LONG).show();
                                                    medicinesAdapter.notifyDataSetChanged();
                                                }else{
                                                    Toast.makeText(getContext(), R.string.info_delete_fail, Toast.LENGTH_LONG).show();
                                                }
                                            }
                                        } else {
                                            Toast.makeText(getContext(), task.getException().getMessage().toString(), Toast.LENGTH_LONG).show();
                                        }
                                    });
                        });

                        return convertView;
                    }
                };
                list.setAdapter(medicinesAdapter);
            }else if(value.equals("allergies")){
                list_title.setText(R.string.allergies);
                allergyList = dataBaseHelper.getAllergiesByElderlyDocId(elderlyDocId);

                allergiesAdapter = new ArrayAdapter<Allergy>(this.getContext(), R.layout.health_status_item_layout_doctor, allergyList) {
                    @NonNull
                    @Override
                    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {


                        // Get the user object for the current position
                        Allergy allergy = getItem(position);

                        // Inflate the list item layout
                        if (convertView == null) {
                            convertView = LayoutInflater.from(getContext()).inflate(R.layout.health_status_item_layout_doctor, parent, false);
                        }
                        TextView idText = convertView.findViewById(R.id.health_status_item);
                        idText.setText(allergy.getAllergy());
                        ImageButton deletBtn = convertView.findViewById(R.id.delete_btn);
                        deletBtn.setOnClickListener(view->{
                            db.collection("Allergies")
                                    .whereEqualTo("allergy",allergy.getAllergy())
                                    .whereEqualTo("elderlyDocId",allergy.getElderlyDocId())
                                    .get().addOnCompleteListener(task -> {
                                        if(task.isSuccessful()){
                                            for (QueryDocumentSnapshot document : task.getResult()) {
                                                // Delete the matching document from Firestore
                                                db.collection("Allergies").document(document.getId()).delete();
                                                if(dataBaseHelper.deleteAllergy(allergy)){
                                                   allergyList.remove(position);
                                                    Toast.makeText(getContext(), R.string.info_delete_success, Toast.LENGTH_LONG).show();
                                                    allergiesAdapter.notifyDataSetChanged();
                                                }else{
                                                    Toast.makeText(getContext(), R.string.info_delete_fail, Toast.LENGTH_LONG).show();
                                                }
                                            }
                                        } else {
                                            Toast.makeText(getContext(), task.getException().getMessage().toString(), Toast.LENGTH_LONG).show();
                                        }
                                    });
                        });

                        return convertView;
                    }
                };
                list.setAdapter(allergiesAdapter);

            }else if(value.equals("surgeries")){
                list_title.setText(R.string.surgery);
                surgeryList = dataBaseHelper.getSurgeriesByElderlyDocId(elderlyDocId);

                surgeriesAdapter = new ArrayAdapter<Surgery>(this.getContext(), R.layout.surgery_item_layout_doctor, surgeryList) {
                    @NonNull
                    @Override
                    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {


                        // Get the user object for the current position
                        Surgery surgery = getItem(position);

                        // Inflate the list item layout
                        if (convertView == null) {
                            convertView = LayoutInflater.from(getContext()).inflate(R.layout.surgery_item_layout_doctor, parent, false);
                        }
                        TextView idText = convertView.findViewById(R.id.surgery_list_item);
                        TextView dateText = convertView.findViewById(R.id.surgery_list_date);
                        idText.setText(surgery.getSurgery());
                        dateText.setText(surgery.getDate().toString());
                        ImageButton deletBtn = convertView.findViewById(R.id.delete_btn);
                        deletBtn.setOnClickListener(view->{
                            db.collection("Surgeries")
                                    .whereEqualTo("surgery",surgery.getSurgery())
                                    .whereEqualTo("elderlyDocId",surgery.getElderlyDocId())
                                    .whereEqualTo("date",surgery.getDate())
                                    .get().addOnCompleteListener(task -> {
                                        if(task.isSuccessful()){
                                            for (QueryDocumentSnapshot document : task.getResult()) {
                                                // Delete the matching document from Firestore
                                                db.collection("Surgeries").document(document.getId()).delete();
                                                if(dataBaseHelper.deleteSurgery(surgery)){
                                                    surgeryList.remove(position);
                                                    Toast.makeText(getContext(), R.string.info_delete_success, Toast.LENGTH_LONG).show();
                                                    surgeriesAdapter.notifyDataSetChanged();
                                                }else{
                                                    Toast.makeText(getContext(), R.string.info_delete_fail, Toast.LENGTH_LONG).show();
                                                }
                                            }
                                        } else {
                                            Toast.makeText(getContext(), task.getException().getMessage().toString(), Toast.LENGTH_LONG).show();
                                        }
                                    });
                        });

                        return convertView;
                    }
                };
                list.setAdapter(surgeriesAdapter);
            }
        }

        return view;
    }

    private void showAddItemDialog(AddItemDialog.ItemType itemType) {
        AddItemDialog addItemDialog = new AddItemDialog(requireContext(), itemType, new AddItemDialog.OnAddItemClickListener() {
            @Override
            public void onAddItem(AddItemDialog.ItemType itemType, String itemName, String itemDate) {
                if (itemType == AddItemDialog.ItemType.SURGERY) {
                    Date date = ElderSignupActivity.convertStringIntoDate(itemDate);
                    // Add the surgery to the surgeryList
                    Surgery newSurgery = new Surgery(elderlyDocId,itemName, date);
                    db.collection("Surgeries").add(newSurgery).addOnCompleteListener(task -> {
                        if(task.isSuccessful()) {
                            boolean addCheck = dataBaseHelper.addSurgery(newSurgery);
                            if (addCheck) {
                                surgeryList.add(newSurgery);
                                surgeriesAdapter.notifyDataSetChanged();
                                Toast.makeText(getContext(), R.string.info_add_success, Toast.LENGTH_LONG).show();
                            } else {
                                Toast.makeText(getContext(), R.string.info_add_fail, Toast.LENGTH_LONG).show();
                            }
                        }
                        else{
                            Toast.makeText(getContext(), task.getException().getMessage().toString(), Toast.LENGTH_LONG).show();
                        }
                    });
                } else if (itemType == AddItemDialog.ItemType.DIAGNOSIS) {
                    // Add the diagnosis to the diagnosisList
                    Diagnosis newDiagnosis = new Diagnosis(elderlyDocId,itemName);
                    db.collection("Diagnosis").add(newDiagnosis).addOnCompleteListener(task -> {
                        if (task.isSuccessful()) {
                            boolean addCheck =  dataBaseHelper.addDiagnosis(newDiagnosis);
                            if(addCheck){
                                diagnosisList.add(newDiagnosis);
                                diagnosisAdapter.notifyDataSetChanged();
                                Toast.makeText(getContext(), R.string.info_add_success, Toast.LENGTH_LONG).show();
                            } else{
                                Toast.makeText(getContext(), R.string.info_add_fail, Toast.LENGTH_LONG).show();
                            }
                        } else{
                            Toast.makeText(getContext(), task.getException().getMessage().toString(), Toast.LENGTH_LONG).show();
                        }
                    });
                } else if (itemType == AddItemDialog.ItemType.ALLERGY) {
                    // Add the diagnosis to the diagnosisList
                    Allergy newAllergy = new Allergy(elderlyDocId,itemName);
                    db.collection("Allergies").add(newAllergy).addOnCompleteListener(task -> {
                        if(task.isSuccessful()){
                            boolean addCheck =  dataBaseHelper.addAllergy(newAllergy);
                            if(addCheck){
                                allergyList.add(newAllergy);
                                allergiesAdapter.notifyDataSetChanged();
                                Toast.makeText(getContext(), R.string.info_add_success, Toast.LENGTH_LONG).show();
                            }
                            else{
                                Toast.makeText(getContext(), R.string.info_add_fail, Toast.LENGTH_LONG).show();
                            }
                        }else{
                            Toast.makeText(getContext(), task.getException().getMessage().toString(), Toast.LENGTH_LONG).show();
                        }
                    });
                }else if (itemType == AddItemDialog.ItemType.MEDICINE) {
                    // Add the diagnosis to the diagnosisList
                    Medicine newMedicine = new Medicine(elderlyDocId,itemName);
                    db.collection("Medicines").add(newMedicine).addOnCompleteListener(task -> {
                        if(task.isSuccessful()){
                            boolean addCheck =  dataBaseHelper.addMedicine(newMedicine);
                            if(addCheck){
                                medicineList.add(newMedicine);
                                medicinesAdapter.notifyDataSetChanged();
                                Toast.makeText(getContext(), R.string.info_add_success, Toast.LENGTH_LONG).show();
                            }
                            else{
                                Toast.makeText(getContext(), R.string.info_add_fail, Toast.LENGTH_LONG).show();
                            }
                        }else{
                            Toast.makeText(getContext(), task.getException().getMessage().toString(), Toast.LENGTH_LONG).show();
                        }
                    });
                }
            }
        });
        addItemDialog.show();
    }
}