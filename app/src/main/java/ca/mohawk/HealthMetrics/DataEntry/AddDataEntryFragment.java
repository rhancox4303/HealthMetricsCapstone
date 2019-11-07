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
import ca.mohawk.HealthMetrics.Models.DataEntry;
import ca.mohawk.HealthMetrics.Models.Metric;
import ca.mohawk.HealthMetrics.Models.Unit;
import ca.mohawk.HealthMetrics.R;
import ca.mohawk.HealthMetrics.TimePickerFragment;


/**
 * A simple {@link Fragment} subclass.
 */

public class AddDataEntryFragment extends Fragment implements View.OnClickListener, TimePickerDialog.OnTimeSetListener, DatePickerDialog.OnDateSetListener {

    private HealthMetricsDbHelper healthMetricsDbHelper;

    private EditText dateOfEntryEditText;
    private EditText dataEntryEditText;

    private int MetricId;
    private String time;

    public AddDataEntryFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootView = inflater.inflate(R.layout.fragment_add_data_entry, container, false);
        dataEntryEditText = rootView.findViewById(R.id.editTextDataEntryAddDataEntry);

        dateOfEntryEditText = rootView.findViewById(R.id.editTextDateOfEntryAddDataEntry);
        dateOfEntryEditText.setOnClickListener(this);

        healthMetricsDbHelper = HealthMetricsDbHelper.getInstance(getActivity());

        Bundle bundle = this.getArguments();
        if (bundle != null) {
            MetricId = bundle.getInt("metric_id_key", -1);
        }

        Metric metric = healthMetricsDbHelper.getMetricById(MetricId);
        Unit unit = healthMetricsDbHelper.getUnitById(metric.UnitId);

        TextView unitTextView = rootView.findViewById(R.id.textViewUnitAddDataEntry);
        TextView metricNameTextView = rootView.findViewById(R.id.textViewMetricDisplayAddDataEntry);
        Button addDataEntry = rootView.findViewById(R.id.buttonAddEntryAddDataEntry);

        addDataEntry.setOnClickListener(this);

        unitTextView.setText(unit.UnitAbbreviation);
        metricNameTextView.setText(metric.Name);

        return rootView;
    }

    public boolean validateUserInput() {
        if (dateOfEntryEditText.getText().toString().trim().equals("")) {
            Toast.makeText(getActivity(), "The date of entry field cannot be empty.", Toast.LENGTH_SHORT).show();
            return false;
        }

        if (dataEntryEditText.getText().toString().trim().equals("")) {
            Toast.makeText(getActivity(), "The data entry field cannot be empty.", Toast.LENGTH_SHORT).show();
            return false;
        }

        if (!dataEntryEditText.getText().toString().trim().matches("^[1-9]\\d*(\\.\\d+)?$")) {
            Toast.makeText(getActivity(), "The data entry field can only contain digits.", Toast.LENGTH_SHORT).show();
            return false;
        }
        return true;
    }

    private void addDataEntry() {
        if (validateUserInput()) {
            String date = dateOfEntryEditText.getText().toString();
            String entry = dataEntryEditText.getText().toString();

            if (healthMetricsDbHelper.addDataEntry(new DataEntry(MetricId, entry, date))) {
                DataEntryListFragment dataViewFragment = new DataEntryListFragment();

                Bundle metricBundle = new Bundle();
                metricBundle.putInt("selected_item_key", MetricId);
                dataViewFragment.setArguments(metricBundle);

                Objects.requireNonNull(getActivity()).getSupportFragmentManager().beginTransaction()
                        .replace(R.id.fragmentContainer, dataViewFragment)
                        .addToBackStack(null)
                        .commit();
            } else {
                Toast.makeText(getActivity(), "Unable to add data entry to database.", Toast.LENGTH_SHORT).show();
            }
        }
    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.editTextDateOfEntryAddDataEntry) {

            TimePickerFragment timePickerFragment = new TimePickerFragment();
            timePickerFragment.setOnTimeSetListener(this);
            timePickerFragment.show(Objects.requireNonNull(getFragmentManager()).beginTransaction(), "timePicker");

        } else if (v.getId() == R.id.buttonAddEntryAddDataEntry) {
            addDataEntry();
        }
    }

    @Override
    public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
        if (minute < 10) {
            time = hourOfDay + ":0" + minute;
        } else {
            time = hourOfDay + ":" + minute;
        }

        dateOfEntryEditText.setText(time);

        DatePickerFragment datePickerFragment = new DatePickerFragment();
        datePickerFragment.setOnDateSetListener(this);
        datePickerFragment.show(Objects.requireNonNull(getFragmentManager()).beginTransaction(), "datePicker");
    }

    @Override
    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
        if (dayOfMonth < 10) {
            dateOfEntryEditText.setText(new StringBuilder().append(time).append(" ").append(month + 1).append("-0").append(dayOfMonth).append("-").append(year).toString());
        } else {
            dateOfEntryEditText.setText(new StringBuilder().append(time).append(" ").append(month + 1).append("-").append(dayOfMonth).append("-").append(year).toString());
        }
    }
}