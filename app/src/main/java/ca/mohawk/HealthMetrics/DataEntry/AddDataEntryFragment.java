package ca.mohawk.HealthMetrics.DataEntry;


import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.os.Bundle;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.TimePicker;

import java.util.Date;

import ca.mohawk.HealthMetrics.DatePickerFragment;
import ca.mohawk.HealthMetrics.HealthMetricsDbHelper;
import ca.mohawk.HealthMetrics.Models.Metric;
import ca.mohawk.HealthMetrics.Models.Unit;
import ca.mohawk.HealthMetrics.R;
import ca.mohawk.HealthMetrics.TimePickerFragment;


/**
 * A simple {@link Fragment} subclass.
 */
public class AddDataEntryFragment extends Fragment implements View.OnClickListener, TimePickerDialog.OnTimeSetListener, DatePickerDialog.OnDateSetListener {

    HealthMetricsDbHelper healthMetricsDbHelper;
    private EditText dateOfEntryEditText;

    private int MetricId;
    private String time;

    public AddDataEntryFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootView =  inflater.inflate(R.layout.fragment_add_data_entry, container, false);

        dateOfEntryEditText = rootView.findViewById(R.id.editTextDateOfEntryAddDataEntry);
        dateOfEntryEditText.setOnClickListener(this);
        healthMetricsDbHelper = HealthMetricsDbHelper.getInstance(getActivity());

        Bundle bundle = this.getArguments();
        if (bundle != null) {
            MetricId = bundle.getInt("metric_id", -1);
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

    @Override
    public void onClick(View v) {
        if(v.getId() == R.id.editTextDateOfEntryAddDataEntry) {
            TimePickerFragment timePickerFragment = new TimePickerFragment();
            timePickerFragment.setOnTimeSetListener(this);
            timePickerFragment.show(getFragmentManager().beginTransaction(), "timePicker");
        }
    }

    @Override
    public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
        if(minute < 10) {
            time = hourOfDay + ":0" + minute;
        }else{
            time = hourOfDay + ":" + minute;
        }
        dateOfEntryEditText.setText(time);

        DatePickerFragment datePickerFragment = new DatePickerFragment();
        datePickerFragment.setOnDateSetListener(this);
        datePickerFragment.show(getFragmentManager().beginTransaction(), "datePicker");
    }

    @Override
    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
        if (dayOfMonth < 10) {
            dateOfEntryEditText.setText(time + " " + (month + 1) + "-0" + dayOfMonth + "-" + year);
        }else{
            dateOfEntryEditText.setText(time + " " + (month + 1) + "-" + dayOfMonth + "-" + year);
        }


    }
}
