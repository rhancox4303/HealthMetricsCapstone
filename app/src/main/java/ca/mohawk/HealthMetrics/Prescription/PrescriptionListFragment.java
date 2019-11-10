package ca.mohawk.HealthMetrics.Prescription;


import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;
import java.util.Objects;

import ca.mohawk.HealthMetrics.Adapaters.PrescriptionRecyclerViewAdapter;
import ca.mohawk.HealthMetrics.DisplayObjects.PrescriptionDisplayObject;
import ca.mohawk.HealthMetrics.HealthMetricsDbHelper;
import ca.mohawk.HealthMetrics.R;

/**
 * The PrescriptionListFragment class is an extension of the Fragment class.
 * Contains a recycler view of all prescriptions in the database.
 */
public class PrescriptionListFragment extends Fragment implements View.OnClickListener {

    public PrescriptionListFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        // Inflate the layout for this fragment
        View rootView = inflater.inflate(R.layout.fragment_prescription_list, container, false);

        // Get the healthMetricsDbHelper.
        HealthMetricsDbHelper healthMetricsDbHelper = HealthMetricsDbHelper.getInstance(getActivity());

        // Get all prescriptions from the database.
        List<PrescriptionDisplayObject> prescriptionDisplayObjects = healthMetricsDbHelper.getAllPrescriptions();

        // Get addPrescriptionButton
        Button addPrescriptionButton = rootView.findViewById(R.id.buttonAddPrescriptionPrescriptionList);
        addPrescriptionButton.setOnClickListener(this);

        // Get the prescriptionRecyclerView.
        RecyclerView prescriptionRecyclerView = rootView.findViewById(R.id.recyclerviewPrescriptionList);

        // Create and set the prescriptionRecyclerViewAdapter.
        PrescriptionRecyclerViewAdapter prescriptionRecyclerViewAdapter = new PrescriptionRecyclerViewAdapter(prescriptionDisplayObjects, getActivity());
        prescriptionRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        prescriptionRecyclerView.setAdapter(prescriptionRecyclerViewAdapter);

        // Return the rootView.
        return rootView;
    }

    /**
     * Runs when the add prescription button is pressed.
     *
     * @param v Represents the view.
     */
    @Override
    public void onClick(View v) {

        CreatePrescriptionFragment createPrescriptionFragment = new CreatePrescriptionFragment();
        Objects.requireNonNull(getActivity()).getSupportFragmentManager().beginTransaction()
                .replace(R.id.fragmentContainer, createPrescriptionFragment)
                .addToBackStack(null)
                .commit();
    }
}
