package ca.mohawk.HealthMetrics.Notification;


import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TimePicker;

import java.util.ArrayList;
import java.util.List;

import ca.mohawk.HealthMetrics.DatePickerFragment;
import ca.mohawk.HealthMetrics.DisplayObjects.MetricDisplayObject;
import ca.mohawk.HealthMetrics.DisplayObjects.PrescriptionDisplayObject;
import ca.mohawk.HealthMetrics.HealthMetricsDbHelper;
import ca.mohawk.HealthMetrics.Models.Prescription;
import ca.mohawk.HealthMetrics.R;
import ca.mohawk.HealthMetrics.TimePickerFragment;


/**
 * A simple {@link Fragment} subclass.
 */
public class CreateNotificationFragment extends Fragment implements AdapterView.OnItemSelectedListener, View.OnClickListener, DatePickerDialog.OnDateSetListener, TimePickerDialog.OnTimeSetListener {

    HealthMetricsDbHelper healthMetricsDbHelper;
    List<MetricDisplayObject> metricArrayList = new ArrayList<>();

    List<MetricDisplayObject> galleryArrayList = new ArrayList<>();
    List<PrescriptionDisplayObject> prescriptionArrayList;
    Spinner notificationTargetSpinner;
    EditText dateEditText;
    private String time;

    public CreateNotificationFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        healthMetricsDbHelper = HealthMetricsDbHelper.getInstance(getActivity());

        metricArrayList = healthMetricsDbHelper.getAddedMetrics(metricArrayList);
        galleryArrayList = healthMetricsDbHelper.getAddedPhotoGalleries(galleryArrayList);
        prescriptionArrayList = healthMetricsDbHelper.getAllPrescriptions();


        // Inflate the layout for this fragment
        View rootView = inflater.inflate(R.layout.fragment_create_notification, container, false);

        Button createNotificationButton = rootView.findViewById(R.id.buttonCreateNotification);
        createNotificationButton.setOnClickListener(this);

        dateEditText = rootView.findViewById(R.id.editTextDateCreateNotification);
        dateEditText.setOnClickListener(this);

        Spinner notificationTypeSpinner = rootView.findViewById(R.id.spinnerNotificationType);
        notificationTargetSpinner = rootView.findViewById(R.id.spinnerNotificationTarget);

        notificationTargetSpinner.setOnItemSelectedListener(this);
        notificationTypeSpinner.setOnItemSelectedListener(this);

        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(rootView.getContext(),
                R.array.notification_type_array, android.R.layout.simple_spinner_item);

        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        notificationTypeSpinner.setAdapter(adapter);

        return rootView;
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        if (parent.getId() == R.id.spinnerNotificationType) {
            String type = parent.getSelectedItem().toString();
            populateTargetSpinner(type);
        }
    }

    private void populateTargetSpinner(String type) {
        notificationTargetSpinner.setAdapter(null);
        switch (type) {
            case "Enter Metric Data":
                ArrayAdapter<MetricDisplayObject> metricDisplayObjectArrayAdapter = new ArrayAdapter<>(getActivity().getBaseContext(), android.R.layout.simple_spinner_item, metricArrayList);
                metricDisplayObjectArrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                notificationTargetSpinner.setAdapter(metricDisplayObjectArrayAdapter);
                break;
            case "Enter Gallery Data":
                ArrayAdapter<MetricDisplayObject> galleryDisplayObjectArrayAdapter = new ArrayAdapter<>(getActivity().getBaseContext(), android.R.layout.simple_spinner_item, galleryArrayList);
                galleryDisplayObjectArrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                notificationTargetSpinner.setAdapter(galleryDisplayObjectArrayAdapter);
                break;
            case "Refill Prescription":
            case "Take Prescription":
                ArrayAdapter<PrescriptionDisplayObject> prescriptionDisplayObjectArrayAdapter = new ArrayAdapter<>(getActivity().getBaseContext(), android.R.layout.simple_spinner_item, prescriptionArrayList);
                prescriptionDisplayObjectArrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                notificationTargetSpinner.setAdapter(prescriptionDisplayObjectArrayAdapter);
                break;
        }
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }

    @Override
    public void onClick(View v) {
        if(v.getId() == R.id.editTextDateCreateNotification) {
            TimePickerFragment timePickerFragment = new TimePickerFragment();
            timePickerFragment.setOnTimeSetListener(this);
            timePickerFragment.show(getFragmentManager().beginTransaction(), "timePicker");
        }else{

        }
    }

    @Override
    public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
        if(minute < 10) {
            time = hourOfDay + ":0" + minute;
        }else{
            time = hourOfDay + ":" + minute;
        }
        dateEditText.setText(time);

        DatePickerFragment datePickerFragment = new DatePickerFragment();
        datePickerFragment.setOnDateSetListener(this);
        datePickerFragment.show(getFragmentManager().beginTransaction(), "datePicker");
    }

    @Override
    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
        if (dayOfMonth < 10) {
            dateEditText.setText(time + " " + (month + 1) + "-0" + dayOfMonth + "-" + year);
        }else{
            dateEditText.setText(time + " " + (month + 1) + "-" + dayOfMonth + "-" + year);
        }
    }

}
