package ca.mohawk.HealthMetrics.MetricManagement;


import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import ca.mohawk.HealthMetrics.R;

/**
 * A simple {@link Fragment} subclass.
 */
public class CreateMetricInputFragment extends Fragment {


    public CreateMetricInputFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_create_metric_input, container, false);
    }

}
