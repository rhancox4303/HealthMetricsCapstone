package ca.mohawk.HealthMetrics.DataEntry;


import android.os.Bundle;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import ca.mohawk.HealthMetrics.HealthMetricsDbHelper;
import ca.mohawk.HealthMetrics.Models.Metric;
import ca.mohawk.HealthMetrics.Models.Unit;
import ca.mohawk.HealthMetrics.R;


/**
 * A simple {@link Fragment} subclass.
 */
public class MetricDataViewFragment extends Fragment implements View.OnClickListener {

    HealthMetricsDbHelper healthMetricsDbHelper;

    int MetricId;
    public MetricDataViewFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootView = inflater.inflate(R.layout.fragment_metric_data_view, container, false);

        healthMetricsDbHelper = HealthMetricsDbHelper.getInstance(getActivity());

        Bundle bundle = this.getArguments();
        if (bundle != null) {
            MetricId = bundle.getInt("metric_selected_key", -1);
        }

        Button addMetric = rootView.findViewById(R.id.buttonAddEntryMetricDataView);
        addMetric.setOnClickListener(this);
        return rootView;
    }

    @Override
    public void onClick(View v) {
        Fragment destinationFragment = null;
        switch (v.getId()) {
            case R.id.buttonAddEntryMetricDataView:
                destinationFragment = new AddDataEntryFragment();
                Bundle bundle = new Bundle();
                bundle.putInt("metric_id",MetricId);
                destinationFragment.setArguments(bundle);
                break;
        }
        getActivity().getSupportFragmentManager().beginTransaction()
                .replace(R.id.fragmentContainer, destinationFragment)
                .addToBackStack(null)
                .commit();
    }
}
