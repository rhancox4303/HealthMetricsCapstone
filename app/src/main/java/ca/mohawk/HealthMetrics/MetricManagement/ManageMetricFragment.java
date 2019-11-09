package ca.mohawk.HealthMetrics.MetricManagement;


import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import java.util.Objects;

import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.Fragment;
import ca.mohawk.HealthMetrics.HealthMetricsDbHelper;
import ca.mohawk.HealthMetrics.Models.Metric;
import ca.mohawk.HealthMetrics.Models.Unit;
import ca.mohawk.HealthMetrics.R;


/**
 * The ManageMetricFragment class is an extension of the Fragment class.
 * <p>
 * Allows the user to manage a metric.
 */
public class ManageMetricFragment extends Fragment implements View.OnClickListener {

    // Instantiate the metric id.
    private int metricId;

    public ManageMetricFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        // Inflate the layout for this fragment
        View rootView = inflater.inflate(R.layout.fragment_manage_metric, container, false);

        // Get the healthMetricsDbHelper.
        HealthMetricsDbHelper healthMetricsDbHelper = HealthMetricsDbHelper.getInstance(getActivity());

        // Get the views.
        TextView metricNameTextView = rootView.findViewById(R.id.textViewMetricNameManageMetric);
        TextView unitTextView = rootView.findViewById(R.id.textViewUnitManageMetric);

        Button editButton = rootView.findViewById(R.id.buttonEditManageMetric);
        Button deleteButton = rootView.findViewById(R.id.buttonDeleteManageMetric);
        Button removeButton = rootView.findViewById(R.id.buttonRemoveManageMetric);

        // Set setOnClickListeners.
        deleteButton.setOnClickListener(this);
        removeButton.setOnClickListener(this);
        editButton.setOnClickListener(this);

        // Get the metric id from the bundle.
        Bundle bundle = this.getArguments();
        if (bundle != null) {
            metricId = bundle.getInt("metric_id_key", -1);
        }

        // Get the metric from the database.
        Metric metric = healthMetricsDbHelper.getMetricById(metricId);


        // Validate the metric and unit retrieved from the database are not null.
        if (metric != null) {
            metricNameTextView.setText(metric.name);
            int unitId = metric.unitId;
            Unit unit = healthMetricsDbHelper.getUnitById(unitId);

            if (unit != null) {
                unitTextView.setText(unit.unitName);
            } else {
                Toast.makeText(getActivity(), "Error getting the unit from the database.",
                        Toast.LENGTH_SHORT).show();
                navigateToMetricsListFragment();
            }
        } else {
            Toast.makeText(getActivity(), "Error getting the metric from the database.",
                    Toast.LENGTH_SHORT).show();
            navigateToMetricsListFragment();
        }

        return rootView;
    }

    /**
     * Replaces the current fragment with a MetricsListFragment.
     */
    private void navigateToMetricsListFragment() {

        MetricsListFragment destinationFragment = new MetricsListFragment();

        Objects.requireNonNull(getActivity()).getSupportFragmentManager().beginTransaction()
                .replace(R.id.fragmentContainer, destinationFragment)
                .addToBackStack(null)
                .commit();
    }

    /**
     * Runs when a view's onClickListener is activated.
     *
     * @param v Represents the view.
     */
    @Override
    public void onClick(View v) {

        switch (v.getId()) {
            case R.id.buttonEditManageMetric:
                Fragment destinationFragment = new EditMetricFragment();

                Bundle bundle = new Bundle();
                bundle.putInt("metric_id_key", metricId);
                destinationFragment.setArguments(bundle);

                Objects.requireNonNull(getActivity()).getSupportFragmentManager().beginTransaction()
                        .replace(R.id.fragmentContainer, destinationFragment)
                        .addToBackStack(null)
                        .commit();
                break;
            case R.id.buttonDeleteManageMetric:
                DialogFragment deleteMetricDialog = DeleteMetricDialog.newInstance(metricId);
                deleteMetricDialog.show(Objects.requireNonNull(getFragmentManager()), "deleteMetricDialog");
                break;
            case R.id.buttonRemoveManageMetric:
                DialogFragment removeMetricDialog = RemoveMetricDialog.newInstance(metricId);
                removeMetricDialog.show(Objects.requireNonNull(getFragmentManager()), "removeMetricDialog");
                break;
        }
    }
}