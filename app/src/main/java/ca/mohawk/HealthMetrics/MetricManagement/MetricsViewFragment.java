package ca.mohawk.HealthMetrics.MetricManagement;


import android.os.Bundle;
import androidx.fragment.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import ca.mohawk.HealthMetrics.R;
import ca.mohawk.HealthMetrics.UserProfile.ViewProfileFragment;

/**
 * A simple {@link Fragment} subclass.
 */
public class MetricsViewFragment extends Fragment implements View.OnClickListener{


    public MetricsViewFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_metrics_view, container, false);

        Button addMetricButton =  view.findViewById(R.id.buttonAddMetricViewMetric);
        addMetricButton.setOnClickListener(this);
        return view;
    }

    @Override
    public void onClick(View v) {
        AddMetricFragment addMetricFragment= new AddMetricFragment();
        getActivity().getSupportFragmentManager().beginTransaction()
                .replace(R.id.fragmentContainer, addMetricFragment)
                .addToBackStack(null)
                .commit();
    }
}
