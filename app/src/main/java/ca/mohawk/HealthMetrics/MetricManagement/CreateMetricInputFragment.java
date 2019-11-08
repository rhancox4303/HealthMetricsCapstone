package ca.mohawk.HealthMetrics.MetricManagement;


import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import androidx.fragment.app.Fragment;
import ca.mohawk.HealthMetrics.HealthMetricsDbHelper;
import ca.mohawk.HealthMetrics.Models.Metric;
import ca.mohawk.HealthMetrics.Models.UnitCategory;
import ca.mohawk.HealthMetrics.R;

/**
 * The CreateMetricInputFragment class is an extension of the Fragment class.
 * <p>
 * Allows the user create metrics.
 */
public class CreateMetricInputFragment extends Fragment implements View.OnClickListener,
        AdapterView.OnItemSelectedListener {

    // Initialize the healthMetricsDbHelper
    private HealthMetricsDbHelper healthMetricsDbHelper;

    // Initialize the unitCategoryId to -1.
    private int unitCategoryId = -1;

    // Initialize the metricNameEditText.
    private EditText metricNameEditText;

    public CreateMetricInputFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        //Initialize the view.
        View rootView = inflater.inflate(R.layout.fragment_create_metric_input, container, false);

        // Instantiate the healthMetricsDbHelper.
        healthMetricsDbHelper = HealthMetricsDbHelper.getInstance(getActivity());

        // Get the list of unit categories from the database.
        List<UnitCategory> unitCategoriesList = healthMetricsDbHelper.getAllUnitCategories();

        Button createMetricButton = rootView.findViewById(R.id.buttonCreateMetricInput);
        createMetricButton.setOnClickListener(this);

        // Get the metricNameEditText.
        metricNameEditText = rootView.findViewById(R.id.editTextMetricNameCreateMetricInput);

        // Initialize the unitCategorySpinner.
        Spinner unitCategorySpinner = rootView.findViewById(R.id.spinnerUnitCategoryCreateMetricInput);

        // Initialize the unitCategoryAdapater.
        ArrayAdapter<UnitCategory> unitCategoryAdapater = new ArrayAdapter<>(Objects.requireNonNull(getActivity()), android.R.layout.simple_spinner_item, unitCategoriesList);

        // Assign the adapter.
        unitCategorySpinner.setAdapter(unitCategoryAdapater);

        // Set the OnItemSelectedListener.
        unitCategorySpinner.setOnItemSelectedListener(this);

        // Return the rootView.
        return rootView;
    }

    /**
     * Runs when a view's onClickListener is activated.
     *
     * @param v Represents the view.
     */
    @Override
    public void onClick(View v) {
        createMetric();
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
     * Creates the metric if the user input is valid.
     */
    private void createMetric() {

        // Validate the user input.
        if (validateUserInput()) {

            // Get the metric name.
            String metricName = metricNameEditText.getText().toString();

            // Create metric in the database, verify it was successful.
            if (healthMetricsDbHelper.addMetric(new Metric(0, metricName, unitCategoryId, 0))) {
                // Create and display addMetricFragment.
                AddMetricFragment addMetricFragment = new AddMetricFragment();

                Objects.requireNonNull(getActivity()).getSupportFragmentManager().beginTransaction()
                        .replace(R.id.fragmentContainer, addMetricFragment)
                        .addToBackStack(null)
                        .commit();
            } else {
                // Inform user of error.
                Toast.makeText(getActivity(), "Failed to create metric.", Toast.LENGTH_SHORT).show();
            }
        }
    }

    /**
     * The onItemSelected method is called when a item in the spinner is selected.
     *
     * @param parent   The parent adapter view.
     * @param view     The selected view.
     * @param position The position of the selected item in the spinner.
     * @param id       The id.
     */
    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

        // Get the unitCategoryId.
        unitCategoryId = ((UnitCategory) parent.getSelectedItem()).id;
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }
}
