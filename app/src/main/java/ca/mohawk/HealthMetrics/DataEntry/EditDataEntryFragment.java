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
 * The EditDataEntryFragment class is an extension of the Fragment class.
 * Allows the user to edit a specified data entry.
 */
public class EditDataEntryFragment extends Fragment implements View.OnClickListener, TimePickerDialog.OnTimeSetListener, DatePickerDialog.OnDateSetListener {

    // Instantiate the HealthMetricsDbHelper variable.
    private HealthMetricsDbHelper healthMetricsDbHelper;

    // Instantiate the EditText variables.
    private EditText dateOfEntryEditText;
    private EditText dataEntryEditText;

    // Instantiate the dataEntryId variable.
    private int dataEntryId = -1;

    // Instantiate the metricId variable.
    private int metricId = -1;

    // Instantiate the time variable.
    private String time;

    public EditDataEntryFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootView = inflater.inflate(R.layout.fragment_edit_data_entry, container,
                false);

        // Get the views.
        TextView metricNameTextView = rootView.findViewById(R.id.textViewMetricDisplayEditDataEntry);
        TextView unitAbbreviationTextView = rootView.findViewById(R.id.textViewUnitAbbreviationEditDataEntry);

        Button editDataEntryButton = rootView.findViewById(R.id.ButtonEditDataEntry);

        dataEntryEditText = rootView.findViewById(R.id.editTextDataEntryEditDataEntry);
        dateOfEntryEditText = rootView.findViewById(R.id.editTextDateOfEntryEditDataEntry);

        // Set the dateOfEntryEditText OnClickListener.
        dateOfEntryEditText.setOnClickListener(this);

        // Set the editDataEntryButton OnClickListener.
        editDataEntryButton.setOnClickListener(this);

        // Get the healthMetricsDbHelper.
        healthMetricsDbHelper = HealthMetricsDbHelper.getInstance(getActivity());

        // Get the data entry id from the passed bundle.
        Bundle bundle = this.getArguments();
        if (bundle != null) {
            dataEntryId = bundle.getInt("data_entry_selected_key", -1);
        }

        // Get the dataEntry from the database.
        DataEntry dataEntry = healthMetricsDbHelper.getDataEntryById(dataEntryId);

        // Validate the data entry is not null.
        if (dataEntry == null) {
            // Inform the user of the error and call the navigateToMetricsListFragment method.
            Toast.makeText(getActivity(), "Cannot load data entry from database.",
                    Toast.LENGTH_SHORT).show();
            navigateToMetricsListFragment();

        } else {
            // Display the date of entry and the data entry.
            dateOfEntryEditText.setText(dataEntry.DateOfEntry);
            dataEntryEditText.setText(dataEntry.DataEntry);

            // Set the metricId.
            metricId = dataEntry.MetricId;
        }

        // Get the metric from the database.
        Metric metric = healthMetricsDbHelper.getMetricById(metricId);

        Unit unit = null;

        // Validate the metric is not null.
        if (metric == null) {
            // Inform the user of the error and call the navigateToMetricsListFragment method.
            Toast.makeText(getActivity(), "Cannot load metric from database.",
                    Toast.LENGTH_SHORT).show();
            navigateToMetricsListFragment();
        } else {

            // Display the metric name.
            metricNameTextView.setText(metric.Name);

            // Get the unit from the database.
            unit = healthMetricsDbHelper.getUnitById(metric.UnitId);
        }

        // Validate the unit is not null.
        if (unit == null) {
            // Inform the user of the error and call the navigateToMetricsListFragment method.
            Toast.makeText(getActivity(), "Cannot load unit from database.",
                    Toast.LENGTH_SHORT).show();
            navigateToMetricsListFragment();
        } else {
            // Display the unit abbreviation.
            unitAbbreviationTextView.setText(unit.UnitAbbreviation);
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
     * Updates the data entry in the database.
     */
    private void editDataEntry() {

        // Validate the user input.
        if (validateUserInput()) {

            // Get the date and entry from the edit texts.
            String date = dateOfEntryEditText.getText().toString();
            String entry = dataEntryEditText.getText().toString();

            // Validate the data entry was updated in the database successfully.
            if (healthMetricsDbHelper.updateDataEntry(new DataEntry(dataEntryId, metricId, entry,
                    date))) {

                // Create a new viewDataEntryFragment Fragment.
                ViewDataEntryFragment viewDataEntryFragment = new ViewDataEntryFragment();

                // Create a bundle and set the data entry id.
                Bundle dataEntryBundle = new Bundle();
                dataEntryBundle.putInt("data_entry_selected_key", dataEntryId);

                // Set the bundle to the viewDataEntryFragment fragment.
                viewDataEntryFragment.setArguments(dataEntryBundle);

                // Replace the current fragment with the dataEntryListFragment.
                Objects.requireNonNull(getActivity()).getSupportFragmentManager().beginTransaction()
                        .replace(R.id.fragmentContainer, viewDataEntryFragment)
                        .addToBackStack(null)
                        .commit();

                // Else, inform the user of the error
            } else {
                Toast.makeText(getActivity(), "Unable to update data entry.", Toast.LENGTH_SHORT).show();
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

        // If the view id is editTextDateOfEntryEditDataEntry then display the TimePickerFragment.
        if (v.getId() == R.id.editTextDateOfEntryEditDataEntry) {

            TimePickerFragment timePickerFragment = new TimePickerFragment();
            timePickerFragment.setOnTimeSetListener(this);
            timePickerFragment.show(Objects.requireNonNull(getFragmentManager()).beginTransaction(),
                    "timePicker");
            // Else if the view id is buttonEditDataEntry call the editDataEntry method.
        } else if (v.getId() == R.id.ButtonEditDataEntry) {
            editDataEntry();
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
        datePickerFragment.show(Objects.requireNonNull(getFragmentManager()).beginTransaction(), "datePicker");
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
            dateOfEntryEditText.setText(new StringBuilder().append(time).append(" ")
                    .append(month + 1).append("-0").append(dayOfMonth).append("-")
                    .append(year).toString());
        } else {
            dateOfEntryEditText.setText(new StringBuilder().append(time).append(" ")
                    .append(month + 1).append("-").append(dayOfMonth)
                    .append("-").append(year).toString());
        }
    }
}