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

import ca.mohawk.HealthMetrics.DatePickerFragment;
import ca.mohawk.HealthMetrics.R;
import ca.mohawk.HealthMetrics.TimePickerFragment;

/**
 * A simple {@link Fragment} subclass.
 */
public class CreateNoteInputFragment extends Fragment implements View.OnClickListener, TimePickerDialog.OnTimeSetListener, DatePickerDialog.OnDateSetListener {

    private EditText dateOfEntryEditText;
    private EditText noteContentEditText;
    private String time;

    public CreateNoteInputFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.fragment_create_note_input, container, false);

        dateOfEntryEditText = rootView.findViewById(R.id.editTextDateOfEntryCreateNoteInput);
        dateOfEntryEditText.setOnClickListener(this);

        Button createNoteButton = rootView.findViewById(R.id.buttonAddNoteCreateNoteInput);
        createNoteButton.setOnClickListener(this);

        noteContentEditText = rootView.findViewById(R.id.editTextNoteCreateNoteInput);
        return rootView;
    }

    @Override
    public void onClick(View v) {
        if(v.getId() == R.id.editTextDateOfEntryCreateNoteInput){

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