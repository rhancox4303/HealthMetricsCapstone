package ca.mohawk.HealthMetrics.MetricManagement;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import ca.mohawk.HealthMetrics.R;

/**
 * A simple {@link Fragment} subclass.
 */
public class MetricsViewFragment extends Fragment {


    public MetricsViewFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_metrics_view, container, false);
    }

}
