package ca.mohawk.HealthMetrics.DataEntry;


import android.os.Bundle;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import ca.mohawk.HealthMetrics.HealthMetricsDbHelper;
import ca.mohawk.HealthMetrics.Models.Metric;
import ca.mohawk.HealthMetrics.Models.Unit;
import ca.mohawk.HealthMetrics.R;


/**
 * A simple {@link Fragment} subclass.
 */
public class AddDataEntryFragment extends Fragment {

    HealthMetricsDbHelper healthMetricsDbHelper;
    int MetricId;

    public AddDataEntryFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootView =  inflater.inflate(R.layout.fragment_add_data_entry, container, false);

        healthMetricsDbHelper = HealthMetricsDbHelper.getInstance(getActivity());

        Bundle bundle = this.getArguments();
        if (bundle != null) {
            MetricId = bundle.getInt("metric_id", -1);
        }

        Metric metric = healthMetricsDbHelper.getMetricById(MetricId);
        Unit unit = healthMetricsDbHelper.getUnitById(metric.UnitId);

        TextView unitTextView = rootView.findViewById(R.id.textViewUnitAddDataEntry);
        TextView metricNameTextView = rootView.findViewById(R.id.textViewMetricDisplayAddDataEntry);

        unitTextView.setText(unit.UnitAbbreviation);
        metricNameTextView.setText(metric.Name);
        return rootView;
    }

}
