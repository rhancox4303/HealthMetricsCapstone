package ca.mohawk.HealthMetrics.MetricManagement;

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
import android.widget.TimePicker;
import android.widget.Toast;

import ca.mohawk.HealthMetrics.DatePickerFragment;
import ca.mohawk.HealthMetrics.HealthMetricContract;
import ca.mohawk.HealthMetrics.HealthMetricsDbHelper;
import ca.mohawk.HealthMetrics.Models.Note;
import ca.mohawk.HealthMetrics.R;
import ca.mohawk.HealthMetrics.TimePickerFragment;

/**
 * A simple {@link Fragment} subclass.
 */
public class CreateNoteInputFragment extends Fragment implements View.OnClickListener, TimePickerDialog.OnTimeSetListener, DatePickerDialog.OnDateSetListener {
    private HealthMetricsDbHelper healthMetricsDbHelper;
    private EditText dateOfEntryEditText;
    private EditText noteContentEditText;
    private String time;

    public CreateNoteInputFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        healthMetricsDbHelper = HealthMetricsDbHelper.getInstance(getActivity());
        View rootView = inflater.inflate(R.layout.fragment_create_note_input, container, false);

        dateOfEntryEditText = rootView.findViewById(R.id.editTextDateOfEntryCreateNoteInput);
        dateOfEntryEditText.setOnClickListener(this);

        Button createNoteButton = rootView.findViewById(R.id.buttonAddNoteCreateNoteInput);
        createNoteButton.setOnClickListener(this);

        noteContentEditText = rootView.findViewById(R.id.editTextNoteCreateNoteInput);
        return rootView;
    }
    public boolean validateUserInput(){
        if(noteContentEditText.getText().toString().trim().equals("") || dateOfEntryEditText.getText().toString().equals("")){
            Toast.makeText(getActivity(), "Please fill in all field", Toast.LENGTH_SHORT).show();
            return false;
        }else{
            return true;
        }
    }
    @Override
    public void onClick(View v) {
        if(v.getId() == R.id.editTextDateOfEntryCreateNoteInput){
            TimePickerFragment timePickerFragment = new TimePickerFragment();
            timePickerFragment.setOnTimeSetListener(this);
            timePickerFragment.show(getFragmentManager().beginTransaction(), "timePicker");
        }else if(v.getId() == R.id.buttonAddNoteCreateNoteInput && validateUserInput() ){

            String noteContent = noteContentEditText.getText().toString();
            String dateOfEntry = dateOfEntryEditText.getText().toString();
            healthMetricsDbHelper.addNote(new Note(dateOfEntry,noteContent));

            Toast.makeText(getActivity(), "Note created.", Toast.LENGTH_SHORT).show();

            MetricsListFragment metricsListFragment = new MetricsListFragment();
            getActivity().getSupportFragmentManager().beginTransaction()
                    .replace(R.id.fragmentContainer, metricsListFragment)
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