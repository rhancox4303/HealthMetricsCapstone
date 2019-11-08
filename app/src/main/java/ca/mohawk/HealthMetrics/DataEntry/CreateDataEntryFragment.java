package ca.mohawk.HealthMetrics.DataEntry;


import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import java.util.Objects;

import androidx.fragment.app.Fragment;
import ca.mohawk.HealthMetrics.DatePickerFragment;
import ca.mohawk.HealthMetrics.HealthMetricsDbHelper;
import ca.mohawk.HealthMetrics.MetricManagement.MetricsListFragment;
import ca.mohawk.HealthMetrics.Models.DataEntry;
import ca.mohawk.HealthMetrics.Models.Metric;
import ca.mohawk.HealthMetrics.Models.Unit;
import ca.mohawk.HealthMetrics.R;
import ca.mohawk.HealthMetrics.TimePickerFragment;


/**
 * The CreateDataEntryFragment class is an extension of the Fragment class.
 * Allows the user to create data entries for metrics.
 */
public class CreateDataEntryFragment extends Fragment implements View.OnClickListener,
        TimePickerDialog.OnTimeSetListener, DatePickerDialog.OnDateSetListener {


    private HealthMetricsDbHelper healthMetricsDbHelper;

    // Instantiate the EditText variables.
    private EditText dateOfEntryEditText;
    private EditText dataEntryEditText;

    // Instantiate the metricId variable.
    private int metricId = -1;

    // Instantiate the time variable.
    private String time;

    public CreateDataEntryFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        // Inflate the layout for this fragment.
        View rootView = inflater.inflate(R.layout.fragment_add_data_entry, container,
                false);

        // Get the views.
        TextView unitTextView = rootView.findViewById(R.id.textViewUnitAddDataEntry);
        TextView metricNameTextView = rootView.findViewById(R.id.textViewMetricDisplayAddDataEntry);
        Button addDataEntryButton = rootView.findViewById(R.id.buttonAddEntryAddDataEntry);
        dataEntryEditText = rootView.findViewById(R.id.editTextDataEntryAddDataEntry);
        dateOfEntryEditText = rootView.findViewById(R.id.editTextDateOfEntryAddDataEntry);

        // Set the dateOfEntryEditText OnClickListener.
        dateOfEntryEditText.setOnClickListener(this);

        // Set the addDataEntryButton OnClickListener.
        addDataEntryButton.setOnClickListener(this);

        // Get the healthMetricsDbHelper.
        healthMetricsDbHelper = HealthMetricsDbHelper.getInstance(getActivity());

        // Get the metric id from the passed bundle.
        Bundle bundle = this.getArguments();
        if (bundle != null) {
            metricId = bundle.getInt("metric_id_key", -1);
        }

        // Get the metric from the database.
        Metric metric = healthMetricsDbHelper.getMetricById(metricId);

        // Validate the metric is not null.
        if (metric != null) {
            // Get the unit from the database.
            Unit unit = healthMetricsDbHelper.getUnitById(metric.UnitId);
            // Validate the unit is not null.
            if (unit != null) {
                // Display the unit abbreviation and the metric name.
                unitTextView.setText(unit.UnitAbbreviation);
                metricNameTextView.setText(metric.Name);
            } else {
                // Inform the user of the error and call the navigateToMetricsListFragment method.
                Toast.makeText(getActivity(), "Cannot load unit from database.",
                        Toast.LENGTH_SHORT).show();

                navigateToMetricsListFragment();
            }
        } else {
            // Inform the user of the error and call the navigateToMetricsListFragment method.
            Toast.makeText(getActivity(), "Cannot load metric from database.",
                    Toast.LENGTH_SHORT).show();
            navigateToMetricsListFragment();
        }

        // Return rootView.
        return rootView;
    }

    /**
     * Validates the user inputs.
     *
     * @return Return a boolean based on whether the user input is valid.
     */
    private boolean validateUserInput() {

        // If the date of entry string is empty, inform the user and return false.
        if (dateOfEntryEditText.getText().toString().trim().equals("")) {
            Toast.makeText(getActivity(), "The date of entry field cannot be empty.",
                    Toast.LENGTH_SHORT).show();
            return false;
        }

        // If the date of entry does not contain a date and time, inform the user and return false.
        if (!dateOfEntryEditText.getText().toString().matches("^(\\d+:\\d\\d)\\s(\\d+-\\d\\d-\\d+)$")) {
            Toast.makeText(getActivity(), "Both a date and time is required.", Toast.LENGTH_SHORT).show();
            return false;
        }

        // If the data entry string is empty, inform the user and return false.
        if (dataEntryEditText.getText().toString().trim().equals("")) {
            Toast.makeText(getActivity(), "The data entry field cannot be empty.",
                    Toast.LENGTH_SHORT).show();
            return false;
        }

        // If the data entry string does not only contain digits, inform the user and return false.
        if (!dataEntryEditText.getText().toString().trim().matches("^[1-9]\\d*(\\.\\d+)?$")) {
            Toast.makeText(getActivity(), "The data entry field can only contain digits.",
                    Toast.LENGTH_SHORT).show();
            return false;
        }

        // Return true.
        return true;
    }

    /**
     * Create a new data entry and add it to the database.
     */
    private void createDataEntry() {

        // Validate the user input.
        if (validateUserInput()) {

            // Get the date and entry from the edit texts.
            String date = dateOfEntryEditText.getText().toString();
            String entry = dataEntryEditText.getText().toString();

            // Validate the data entry was added to the database successfully.
            if (healthMetricsDbHelper.addDataEntry(new DataEntry(metricId, entry, date))) {

                // Create a new dataEntryList Fragment.
                DataEntryListFragment dataEntryListFragment = new DataEntryListFragment();

                // Create a bundle and set the metric id.
                Bundle metricBundle = new Bundle();
                metricBundle.putInt("selected_item_key", metricId);

                // Set the bundle to the dataEntryList fragment.
                dataEntryListFragment.setArguments(metricBundle);

                // Replace the current fragment with the dataEntryListFragment.
                Objects.requireNonNull(getActivity()).getSupportFragmentManager().beginTransaction()
                        .replace(R.id.fragmentContainer, dataEntryListFragment)
                        .addToBackStack(null)
                        .commit();

                // Else, inform the user of the error.
            } else {
                Toast.makeText(getActivity(), "Unable to add data entry to database.",
                        Toast.LENGTH_SHORT).show();
            }
        }
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

        // If the view id is editTextDateOfEntryAddDataEntry then display the TimePickerFragment.
        if (v.getId() == R.id.editTextDateOfEntryAddDataEntry) {

            TimePickerFragment timePickerFragment = new TimePickerFragment();
            timePickerFragment.setOnTimeSetListener(this);
            timePickerFragment.show(Objects.requireNonNull(getFragmentManager()).beginTransaction(),
                    "timePicker");

            // Else if the view id is buttonAddEntryAddDataEntry call the createDataEntry method.
        } else if (v.getId() == R.id.buttonAddEntryAddDataEntry) {
            createDataEntry();
        }
    }

    /**
     * Runs when the TimePickerFragment onTimeSet listener is called.
     *
     * @param view      Represents the TimePicker view.
     * @param hourOfDay Represents the selected hour.
     * @param minute    Represents the selected minute.
     */
    @Override
    public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
        if (minute < 10) {
            time = hourOfDay + ":0" + minute;
        } else {
            time = hourOfDay + ":" + minute;
        }

        // Create and show the DatePickerFragment.
        DatePickerFragment datePickerFragment = new DatePickerFragment();
        datePickerFragment.setOnDateSetListener(this);
        datePickerFragment.show(Objects.requireNonNull(getFragmentManager()).beginTransaction(),
                "datePicker");
    }

    /**
     * Runs when the DatePickerFragment onDateSet listener is called.
     *
     * @param view       Represents the DatePicker view.
     * @param year       Represents the selected year.
     * @param month      Represents the selected month.
     * @param dayOfMonth Represents the selected dayOfMonth.
     */
    @Override
    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
        if (dayOfMonth < 10) {
            dateOfEntryEditText.setText(new StringBuilder().append(time).append(" ").append(month + 1).append("-0").append(dayOfMonth).append("-").append(year).toString());
        } else {
            dateOfEntryEditText.setText(new StringBuilder().append(time).append(" ").append(month + 1).append("-").append(dayOfMonth).append("-").append(year).toString());
        }
    }
}