package com.example.slothgoldencare;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link MedicationsFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class MedicationsFragment extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private ImageView imageViewMed;
    private String mParam1;
    private String mParam2;
//    private TextView medicationNameTextView;
//    private TextView dosageTextView;
//    private TextView frequencyTextView;
//    private TextView timeToTakeTextView;
//    private TextView durationTextView;
//    private TextView instructionsTextView;
//    private TextView sideEffectsTextView;

    public MedicationsFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment MedicationsFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static MedicationsFragment newInstance(String param1, String param2) {
        MedicationsFragment fragment = new MedicationsFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
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
        // Inflate the layout for this fragment
//        return inflater.inflate(R.layout.fragment_medications, container, false);
        View rootView = inflater.inflate(R.layout.fragment_medications, container, false);

        // Find the ImageView in the layout
        imageViewMed = rootView.findViewById(R.id.imageViewMed);
        imageViewMed.setImageResource(R.drawable.pills_medicine);
//        // Set the image resource for the ImageView

//
//        medicationNameTextView = rootView.findViewById(R.id.medicationNameTextView);
//        dosageTextView = rootView.findViewById(R.id.dosageTextView);
//        frequencyTextView = rootView.findViewById(R.id.frequencyTextView);
//        timeToTakeTextView = rootView.findViewById(R.id.timeToTakeTextView);
//        durationTextView = rootView.findViewById(R.id.durationTextView);
//        instructionsTextView = rootView.findViewById(R.id.instructionsTextView);
//        sideEffectsTextView = rootView.findViewById(R.id.sideEffectsTextView);
//
//        // Set the attribute values for the medication
//        medicationNameTextView.append("Medication Name: Paracetamol");
//        dosageTextView.append("Dosage: 500 mg");
//        frequencyTextView.append("Frequency: 3 times a day");
//        timeToTakeTextView.append("Time to Take: After meals");
//        durationTextView.append("Duration: 7 days");
//        instructionsTextView.append("Instructions: Take with water");
//        sideEffectsTextView.append("Side Effects: Mild headache, nausea");

        return rootView;
    }
}