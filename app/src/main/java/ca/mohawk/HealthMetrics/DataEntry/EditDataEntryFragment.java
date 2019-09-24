package ca.mohawk.HealthMetrics.DataEntry;


import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.os.Bundle;
import androidx.fragment.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import ca.mohawk.HealthMetrics.DatePickerFragment;
import ca.mohawk.HealthMetrics.HealthMetricsDbHelper;
import ca.mohawk.HealthMetrics.Models.Metric;
import ca.mohawk.HealthMetrics.Models.DataEntry;
import ca.mohawk.HealthMetrics.Models.Unit;
import ca.mohawk.HealthMetrics.R;
import ca.mohawk.HealthMetrics.TimePickerFragment;


/**
 * A simple {@link Fragment} subclass.
 */
public class EditDataEntryFragment extends Fragment implements View.OnClickListener, TimePickerDialog.OnTimeSetListener, DatePickerDialog.OnDateSetListener{

    private EditText dateOfEntryEditText;
    private EditText dataEntryEditText;

    private int DataEntryId;
    private int MetricId;
    private String time;
    HealthMetricsDbHelper healthMetricsDbHelper;

    public EditDataEntryFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootView = inflater.inflate(R.layout.fragment_edit_data_entry, container, false);
        TextView metricNameTextView = rootView.findViewById(R.id.textViewMetricDisplayEditDataEntry);
        TextView unitAbbreviationTextView = rootView.findViewById(R.id.textViewUnitAbbreviationEditDataEntry);
        Button editDataEntryButton = rootView.findViewById(R.id.ButtonEditDataEntry);

        dataEntryEditText = rootView.findViewById(R.id.editTextDataEntryEditDataEntry);
        dateOfEntryEditText = rootView.findViewById(R.id.editTextDateOfEntryEditDataEntry);

        dateOfEntryEditText.setOnClickListener(this);
        editDataEntryButton.setOnClickListener(this);

        healthMetricsDbHelper = HealthMetricsDbHelper.getInstance(getActivity());

        Bundle bundle = this.getArguments();
        if (bundle != null) {
            DataEntryId = bundle.getInt("data_entry_selected_key", -1);
        }

        DataEntry dataEntry = healthMetricsDbHelper.getDataEntryById(DataEntryId);

        MetricId = dataEntry.MetricId;

        Metric metric = healthMetricsDbHelper.getMetricById(MetricId);
        Unit unit = healthMetricsDbHelper.getUnitById(metric.UnitId);
        dateOfEntryEditText.setText(dataEntry.DateOfEntry);

        metricNameTextView.setText(metric.Name);
        unitAbbreviationTextView.setText(unit.UnitAbbreviation);
        dataEntryEditText.setText(dataEntry.DataEntry);
        return rootView;
    }

    public Boolean validateUserInput(){
        if(dateOfEntryEditText.getText().toString().equals("") || dataEntryEditText.getText().toString().equals("")){
            Toast.makeText(getActivity(), "Please enter all fields.", Toast.LENGTH_SHORT).show();
            return false;
        }else{
            return true;
        }
    }

    public void editDataEntry(){
        String date = dateOfEntryEditText.getText().toString();
        String entry = dataEntryEditText.getText().toString();
        healthMetricsDbHelper.updateDataEntry(new DataEntry(DataEntryId,MetricId,entry,date));
    }

    @Override
    public void onClick(View v) {
        if(v.getId() == R.id.editTextDateOfEntryEditDataEntry) {
            TimePickerFragment timePickerFragment = new TimePickerFragment();
            timePickerFragment.setOnTimeSetListener(this);
            timePickerFragment.show(getFragmentManager().beginTransaction(), "timePicker");
        } else if(v.getId() == R.id.ButtonEditDataEntry && validateUserInput()){
            editDataEntry();

            MetricDataViewFragment dataViewFragment = new MetricDataViewFragment();

            Bundle metricBundle = new Bundle();
            metricBundle.putInt("selected_item_key", MetricId);
            dataViewFragment.setArguments(metricBundle);

            getActivity().getSupportFragmentManager().beginTransaction()
                    .replace(R.id.fragmentContainer, dataViewFragment)
                    .addToBackStack(null)
                    .commit();
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
