package ca.mohawk.HealthMetrics.Note;


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
import ca.mohawk.HealthMetrics.HealthMetricsDbHelper;
import ca.mohawk.HealthMetrics.Models.Note;
import ca.mohawk.HealthMetrics.R;
import ca.mohawk.HealthMetrics.TimePickerFragment;

/**
 * A simple {@link Fragment} subclass.
 */
public class EditNoteFragment extends Fragment implements View.OnClickListener, TimePickerDialog.OnTimeSetListener, DatePickerDialog.OnDateSetListener {
    private int NoteId;
    private String time;
    private HealthMetricsDbHelper healthMetricsDbHelper;
    private EditText dateOfEntryEditText;
    private EditText noteContentEditText;

    public EditNoteFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        healthMetricsDbHelper = HealthMetricsDbHelper.getInstance(getActivity());

        // Inflate the layout for this fragment
        View rootView = inflater.inflate(R.layout.fragment_edit_note, container, false);

        noteContentEditText = rootView.findViewById(R.id.editTextNoteContentEditNote);
        dateOfEntryEditText = rootView.findViewById(R.id.editTextDateOfEntryEditNote);
        Button editNoteButton = rootView.findViewById(R.id.buttonEditNoteEditNote);

        dateOfEntryEditText.setOnClickListener(this);
        editNoteButton.setOnClickListener(this);

        Bundle bundle = this.getArguments();
        if (bundle != null) {
            NoteId = bundle.getInt("note_id_key", -1);
        }

        Note note = healthMetricsDbHelper.getNoteById(NoteId);

        noteContentEditText.setText(note.NoteContent);
        dateOfEntryEditText.setText(note.DateOfEntry);
        return rootView;
    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.editTextDateOfEntryEditNote) {
            TimePickerFragment timePickerFragment = new TimePickerFragment();
            timePickerFragment.setOnTimeSetListener(this);
            timePickerFragment.show(getFragmentManager().beginTransaction(), "timePicker");
        } else if (v.getId() == R.id.buttonEditNoteEditNote && validateUserInput()) {
            
            editNote();
            ViewNoteFragment viewNoteFragment = new ViewNoteFragment();

            Bundle metricBundle = new Bundle();
            metricBundle.putInt("selected_item_key", NoteId);
            viewNoteFragment.setArguments(metricBundle);

            getActivity().getSupportFragmentManager().beginTransaction()
                    .replace(R.id.fragmentContainer, viewNoteFragment)
                    .addToBackStack(null)
                    .commit();
        }
    }

    private void editNote() {
        String noteContent = noteContentEditText.getText().toString();
        String dateOfEntry = dateOfEntryEditText.getText().toString();
        if(healthMetricsDbHelper.updateNote(new Note(NoteId, dateOfEntry, noteContent))){
            Toast.makeText(getActivity(), "Edit Successful", Toast.LENGTH_SHORT).show();
        }else{
            Toast.makeText(getActivity(), "Edit Failed.", Toast.LENGTH_SHORT).show();
        }
    }

    private boolean validateUserInput() {
        if (noteContentEditText.getText().toString().trim().equals("") ||
                dateOfEntryEditText.getText().toString().trim().equals("")) {
            Toast.makeText(getActivity(), "Please fill in all fields.", Toast.LENGTH_SHORT).show();
            return false;
        }
        return true;
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
        datePickerFragment.show(getFragmentManager().beginTransaction(), "datePicker");
    }

    @Override
    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
        if (dayOfMonth < 10) {
            dateOfEntryEditText.setText(time + " " + (month + 1) + "-0" + dayOfMonth + "-" + year);
        } else {
            dateOfEntryEditText.setText(time + " " + (month + 1) + "-" + dayOfMonth + "-" + year);
        }
    }
}
