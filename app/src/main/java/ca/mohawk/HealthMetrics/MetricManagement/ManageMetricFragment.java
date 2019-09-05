package ca.mohawk.HealthMetrics.MetricManagement;


import android.os.Bundle;

import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import ca.mohawk.HealthMetrics.HealthMetricsDbHelper;
import ca.mohawk.HealthMetrics.Models.Metric;
import ca.mohawk.HealthMetrics.Models.Unit;
import ca.mohawk.HealthMetrics.R;


/**
 * A simple {@link Fragment} subclass.
 */
public class ManageMetricFragment extends Fragment implements View.OnClickListener {

    private int MetricId;
    private int UnitId;
    HealthMetricsDbHelper healthMetricsDbHelper;

    public ManageMetricFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootView = inflater.inflate(R.layout.fragment_manage_metric, container, false);

        healthMetricsDbHelper = HealthMetricsDbHelper.getInstance(getActivity());

        TextView metricNameTextView = rootView.findViewById(R.id.textViewMetricNameManageMetric);
        TextView unitTextView = rootView.findViewById(R.id.textViewUnitManageMetric);

        Button deleteButton = rootView.findViewById(R.id.buttonDeleteManageMetric);
        deleteButton.setOnClickListener(this);

        Button editButton = rootView.findViewById(R.id.buttonEditManageMetric);
        editButton.setOnClickListener(this);

        Bundle bundle = this.getArguments();
        if (bundle != null) {
            MetricId = bundle.getInt("metric_id", -1);
        }

        Metric metric = healthMetricsDbHelper.getMetricById(MetricId);
        UnitId = metric.UnitId;

        Unit unit = healthMetricsDbHelper.getUnitById(UnitId);

        metricNameTextView.setText(metric.Name);
        unitTextView.setText(unit.UnitName);
        return rootView;
    }

    @Override
    public void onClick(View v) {
        Fragment destinationFragment = new Fragment();

        switch (v.getId()){
            case R.id.buttonEditManageMetric:
                destinationFragment = new EditMetricFragment();

                Bundle bundle = new Bundle();
                bundle.putInt("metric_id",MetricId);
                destinationFragment.setArguments(bundle);

                getActivity().getSupportFragmentManager().beginTransaction()
                        .replace(R.id.fragmentContainer, destinationFragment)
                        .addToBackStack(null)
                        .commit();
                break;
            case R.id.buttonDeleteManageMetric:
                DialogFragment newFragment = DeleteMetricDialog.newInstance(MetricId);
                newFragment.show(getFragmentManager(), "dialog");
        }
    }
}
