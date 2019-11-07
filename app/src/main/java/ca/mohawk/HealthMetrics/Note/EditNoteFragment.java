package ca.mohawk.HealthMetrics.Note;


import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TimePicker;
import android.widget.Toast;

import java.util.Objects;

import androidx.fragment.app.Fragment;
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
        if (note != null) {
            noteContentEditText.setText(note.NoteContent);
            dateOfEntryEditText.setText(note.DateOfEntry);
        }

        return rootView;
    }


    private void editNote() {
        if (validateUserInput()) {

            String noteContent = noteContentEditText.getText().toString();
            String dateOfEntry = dateOfEntryEditText.getText().toString();

            if (healthMetricsDbHelper.updateNote(new Note(NoteId, dateOfEntry, noteContent))) {
                Toast.makeText(getActivity(), "Edit Successful", Toast.LENGTH_SHORT).show();

                ViewNoteFragment viewNoteFragment = new ViewNoteFragment();

                Bundle metricBundle = new Bundle();
                metricBundle.putInt("selected_item_key", NoteId);
                viewNoteFragment.setArguments(metricBundle);

                Objects.requireNonNull(getActivity()).getSupportFragmentManager().beginTransaction()
                        .replace(R.id.fragmentContainer, viewNoteFragment)
                        .addToBackStack(null)
                        .commit();

            } else {
                Toast.makeText(getActivity(), "Edit Failed.", Toast.LENGTH_SHORT).show();
            }
        }
    }

    private boolean validateUserInput() {

        String noteContents = noteContentEditText.getText().toString().trim();

        // If metricName is empty then inform the user and return false.
        if (noteContents.equals("")) {
            Toast.makeText(getActivity(), "The note content cannot be empty.", Toast.LENGTH_SHORT).show();
            return false;
        }

        // If metricName is greater than 140 then inform the user and return false.
        if (noteContents.length() > 140) {
            Toast.makeText(getActivity(), "Enter a note 140 characters or less.", Toast.LENGTH_SHORT).show();
            return false;
        }

        if (dateOfEntryEditText.getText().toString().trim().equals("")) {
            Toast.makeText(getActivity(), "The date of entry cannot be empty.", Toast.LENGTH_SHORT).show();
            return false;
        }

        // Return true.
        return true;
    }


    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.editTextDateOfEntryEditNote) {
            TimePickerFragment timePickerFragment = new TimePickerFragment();
            timePickerFragment.setOnTimeSetListener(this);
            timePickerFragment.show(Objects.requireNonNull(getFragmentManager()).beginTransaction(), "timePicker");
        } else if (v.getId() == R.id.buttonEditNoteEditNote) {
            editNote();
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
            dateOfEntryEditText.setText(new StringBuilder().append(time).append(" ")
                    .append(month + 1).append("-0").append(dayOfMonth).append("-")
                    .append(year).toString());
        } else {
            dateOfEntryEditText.setText(new StringBuilder().append(time).append(" ")
                    .append(month + 1).append("-").append(dayOfMonth).append("-")
                    .append(year).toString());
        }
    }
}
