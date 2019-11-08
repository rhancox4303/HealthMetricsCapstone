package ca.mohawk.HealthMetrics.MetricManagement;


import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Objects;

import androidx.fragment.app.Fragment;
import ca.mohawk.HealthMetrics.HealthMetricsDbHelper;
import ca.mohawk.HealthMetrics.Models.Metric;
import ca.mohawk.HealthMetrics.R;


/**
 * A simple {@link Fragment} subclass.
 */
public class EditMetricFragment extends Fragment implements View.OnClickListener {

    private int metricId;
    private Metric metric;
    private HealthMetricsDbHelper healthMetricsDbHelper;
    private EditText metricNameEditText;

    public EditMetricFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        healthMetricsDbHelper = HealthMetricsDbHelper.getInstance(getActivity());

        // Inflate the layout for this fragment
        View rootView = inflater.inflate(R.layout.fragment_edit_metric, container, false);

        metricNameEditText = rootView.findViewById(R.id.editTextMetricNameEditMetric);
        Button editMetric = rootView.findViewById(R.id.buttonEditMetric);

        editMetric.setOnClickListener(this);

        Bundle bundle = this.getArguments();
        if (bundle != null) {
            metricId = bundle.getInt("metric_id_key", -1);
        }

        metric = healthMetricsDbHelper.getMetricById(metricId);

        if (metric != null) {
            metricNameEditText.setText(metric.name);
        }

        return rootView;
    }

    private boolean validateUserInput() {

        // Get the user inputted metric name.
        String metricName = metricNameEditText.getText().toString().trim();

        // Get all the metric names from the database.
        ArrayList<String> metricNameList = healthMetricsDbHelper.getAllMetricNames();

        // If metricName is empty then inform the user and return false.
        if (metricName.equals("")) {
            Toast.makeText(getActivity(), "The metric name cannot be empty.", Toast.LENGTH_SHORT).show();
            return false;
        }
        // If metricName is greater than 25 then inform the user and return false.
        if (metricName.length() > 25) {
            Toast.makeText(getActivity(), "Enter a name 25 characters or less.", Toast.LENGTH_SHORT).show();
            return false;
        }
        // If metricName does not only contain numbers, digits and spaces. then inform the user and return false.
        if (!metricName.matches("[a-zA-Z0-9 ]*")) {
            Toast.makeText(getActivity(), "The metric name may only contain letters and numbers", Toast.LENGTH_SHORT).show();
            return false;
        }

        // If the metric name already exists then inform the user and return false.
        if (metricNameList.contains(metricName.toLowerCase())) {
            Toast.makeText(getActivity(), metricName + " already exists.", Toast.LENGTH_SHORT).show();
            return false;
        }

        // Return true.
        return true;
    }

    private void editMetric() {

        if (validateUserInput()) {
            metric.name = metricNameEditText.getText().toString();

            if (healthMetricsDbHelper.updateMetric(metric)) {
                // Inform user.
                Toast.makeText(getActivity(), "Metric updated.", Toast.LENGTH_SHORT).show();
                // Create and display addMetricFragment.
                ManageMetricFragment manageMetricFragment = new ManageMetricFragment();

                Bundle metricBundle = new Bundle();
                metricBundle.putInt("metric_id_key", metricId);
                manageMetricFragment.setArguments(metricBundle);

                Objects.requireNonNull(getActivity()).getSupportFragmentManager().beginTransaction()
                        .replace(R.id.fragmentContainer, manageMetricFragment)
                        .addToBackStack(null)
                        .commit();
            } else {
                Toast.makeText(getActivity(), "Failed to update metric.", Toast.LENGTH_SHORT).show();
            }
        }
    }

    @Override
    public void onClick(View v) {
        editMetric();
    }
}
