package ca.mohawk.HealthMetrics.Prescription;


import android.os.Bundle;
import androidx.fragment.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import java.util.List;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import ca.mohawk.HealthMetrics.Adapaters.PrescriptionRecyclerViewAdapter;
import ca.mohawk.HealthMetrics.DisplayObjects.PrescriptionRecyclerViewObject;
import ca.mohawk.HealthMetrics.HealthMetricsDbHelper;
import ca.mohawk.HealthMetrics.MetricManagement.AddMetricFragment;
import ca.mohawk.HealthMetrics.R;
import ca.mohawk.HealthMetrics.UserProfile.CreateUserFragment;


/**
 * A simple {@link Fragment} subclass.
 */
public class PrescriptionListFragment extends Fragment implements View.OnClickListener {

    private List<PrescriptionRecyclerViewObject> prescriptionRecyclerViewObjectList;
    HealthMetricsDbHelper healthMetricsDbHelper;

    public PrescriptionListFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        healthMetricsDbHelper = HealthMetricsDbHelper.getInstance(getActivity());
        prescriptionRecyclerViewObjectList = healthMetricsDbHelper.getAllPrescriptions();
        //Toast.makeText(getActivity(), prescriptionRecyclerViewObjectList.get(0).Name, Toast.LENGTH_SHORT).show();
        // Inflate the layout for this fragment
        View rootView = inflater.inflate(R.layout.fragment_prescription_list, container, false);

        Button addPrescriptionButton = rootView.findViewById(R.id.buttonAddPrescriptionPrescriptionList);
        addPrescriptionButton.setOnClickListener(this);

        RecyclerView prescriptionRecyclerView = rootView.findViewById(R.id.recyclerviewPrescriptionList);
        PrescriptionRecyclerViewAdapter adapter = new PrescriptionRecyclerViewAdapter(prescriptionRecyclerViewObjectList, getActivity());

        prescriptionRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        prescriptionRecyclerView.setAdapter(adapter);


        return rootView;
    }

    @Override
    public void onClick(View v) {

        CreatePrescriptionFragment createPrescriptionFragment= new CreatePrescriptionFragment();
        getActivity().getSupportFragmentManager().beginTransaction()
                .replace(R.id.fragmentContainer, createPrescriptionFragment)
                .addToBackStack(null)
                .commit();
    }
}
