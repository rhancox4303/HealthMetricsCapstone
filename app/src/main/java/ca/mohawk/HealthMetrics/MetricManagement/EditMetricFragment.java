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
 * EditMetricFragment extends the Fragment class.
 * Allows the user to the edit a metric.
 */
public class EditMetricFragment extends Fragment implements View.OnClickListener {

    // Initialise the HealthMetricsDbHelper.
    private HealthMetricsDbHelper healthMetricsDbHelper;

    // Initialize the metric Id and metric.
    private int metricId;
    private Metric metric;

    // Initialize the metricNameEditText.
    private EditText metricNameEditText;

    public EditMetricFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        // Inflate the layout for this fragment
        View rootView = inflater.inflate(R.layout.fragment_edit_metric, container, false);

        // Get the healthMetricsDbHelper.
        healthMetricsDbHelper = HealthMetricsDbHelper.getInstance(getActivity());

        // Set the views.
        metricNameEditText = rootView.findViewById(R.id.editTextMetricNameEditMetric);
        Button editMetricButton = rootView.findViewById(R.id.buttonEditMetric);

        // Set the editMetricButton onClickListener.
        editMetricButton.setOnClickListener(this);

        // Get the metric id from the bundle.
        Bundle bundle = this.getArguments();
        if (bundle != null) {
            metricId = bundle.getInt("metric_id_key", -1);
        }

        // Get the metric fom the database.
        metric = healthMetricsDbHelper.getMetricById(metricId);

        // Validate the metric is not null
        if (metric != null) {
            metricNameEditText.setText(metric.name);
        } else {
            Toast.makeText(getContext(), "Error getting metric from the database.",
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
     * Validates the user inputs.
     *
     * @return Return a boolean based on whether the user input is valid.
     */
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

    /**
     * Updates the metric in the database.
     */
    private void updateMetric() {

        if (validateUserInput()) {
            metric.name = metricNameEditText.getText().toString();

            if (healthMetricsDbHelper.updateMetric(metric)) {
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

    /**
     * Runs when a view's onClickListener is activated.
     *
     * @param v Represents the view.
     */
    @Override
    public void onClick(View v) {
        updateMetric();
    }
}
