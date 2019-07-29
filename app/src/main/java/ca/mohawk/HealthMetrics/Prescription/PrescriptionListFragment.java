package ca.mohawk.HealthMetrics.Prescription;


import android.os.Bundle;
import androidx.fragment.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import ca.mohawk.HealthMetrics.MetricManagement.AddMetricFragment;
import ca.mohawk.HealthMetrics.R;
import ca.mohawk.HealthMetrics.UserProfile.CreateUserFragment;


/**
 * A simple {@link Fragment} subclass.
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

        Button addPrescriptionButton = rootView.findViewById(R.id.buttonAddPrescriptionPrescriptionList);
        addPrescriptionButton.setOnClickListener(this);

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
