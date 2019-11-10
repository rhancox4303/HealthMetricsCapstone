package ca.mohawk.HealthMetrics.MetricManagement;

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

import androidx.fragment.app.Fragment;

import java.util.Objects;

import ca.mohawk.HealthMetrics.DatePickerFragment;
import ca.mohawk.HealthMetrics.HealthMetricsDbHelper;
import ca.mohawk.HealthMetrics.Models.Note;
import ca.mohawk.HealthMetrics.R;
import ca.mohawk.HealthMetrics.TimePickerFragment;

/**
 * The CreateNoteInputFragment class is an extension of the Fragment class.
 * <p>
 * Allows the user create notes.
 */
public class CreateNoteInputFragment extends Fragment implements View.OnClickListener,
        TimePickerDialog.OnTimeSetListener, DatePickerDialog.OnDateSetListener {

    // Initialize the healthMetricsDbHelper.
    private HealthMetricsDbHelper healthMetricsDbHelper;

    // Initialize the edit texts.
    private EditText dateOfEntryEditText;
    private EditText noteContentEditText;

    // Initialize the time variable.
    private String time;

    public CreateNoteInputFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        // Inflate the layout for this fragment.
        View rootView = inflater.inflate(R.layout.fragment_create_note_input, container, false);

        // Instantiate the healthMetricsDbHelper.
        healthMetricsDbHelper = HealthMetricsDbHelper.getInstance(getActivity());

        // Get the views.
        Button createNoteButton = rootView.findViewById(R.id.buttonAddNoteCreateNoteInput);
        dateOfEntryEditText = rootView.findViewById(R.id.editTextDateOfEntryCreateNoteInput);
        noteContentEditText = rootView.findViewById(R.id.editTextNoteCreateNoteInput);

        // Set the OnClickListeners.
        createNoteButton.setOnClickListener(this);
        dateOfEntryEditText.setOnClickListener(this);

        return rootView;
    }

    /**
     * Runs when the add note button or date of entry edit text are pressed.
     *
     * @param v Represents the view.
     */
    @Override
    public void onClick(View v) {

        if (v.getId() == R.id.editTextDateOfEntryCreateNoteInput) {
            TimePickerFragment timePickerFragment = new TimePickerFragment();
            timePickerFragment.setOnTimeSetListener(this);
            timePickerFragment.show(Objects.requireNonNull(getFragmentManager()).beginTransaction(), "timePicker");

        } else if (v.getId() == R.id.buttonAddNoteCreateNoteInput) {
            createNote();
        }
    }

    /**
     * Validates the user inputs.
     *
     * @return Return a boolean based on whether the user input is valid.
     */
    private boolean validateUserInput() {

        // Get the note contents from the user.
        String noteContents = noteContentEditText.getText().toString().trim();

        // If noteContents is empty then inform the user and return false.
        if (noteContents.equals("")) {
            Toast.makeText(getActivity(), "The note content cannot be empty.", Toast.LENGTH_SHORT).show();
            return false;
        }

        // If noteContents is greater than 140 then inform the user and return false.
        if (noteContents.length() > 140) {
            Toast.makeText(getActivity(), "Enter a note 140 characters or less.", Toast.LENGTH_SHORT).show();
            return false;
        }

        // If date of entry is empty then inform the user and return false.
        if (dateOfEntryEditText.getText().toString().trim().equals("")) {
            Toast.makeText(getActivity(), "The date of entry cannot be empty.", Toast.LENGTH_SHORT).show();
            return false;
        }

        // If date of entry is does not contain a date and time then inform the user and return false.
        if (!dateOfEntryEditText.getText().toString().matches("^(\\d+:\\d\\d)\\s(\\d+-\\d\\d-\\d+)$")) {
            Toast.makeText(getActivity(), "Both a date and time is required.", Toast.LENGTH_SHORT).show();
            return false;
        }
        // Return true.
        return true;
    }

    /**
     * Creates a note and adds it to the database.
     */
    private void createNote() {

        // Validate the user input.
        if (validateUserInput()) {

            String noteContent = noteContentEditText.getText().toString().trim();
            String dateOfEntry = dateOfEntryEditText.getText().toString().trim();

            // Add the note to the database and verify it was successful.
            if (healthMetricsDbHelper.addNote(new Note(dateOfEntry, noteContent))) {

                MetricsListFragment metricsListFragment = new MetricsListFragment();

                Objects.requireNonNull(getActivity()).getSupportFragmentManager().beginTransaction()
                        .replace(R.id.fragmentContainer, metricsListFragment)
                        .addToBackStack(null)
                        .commit();
            } else {
                Toast.makeText(getActivity(), "Failed to create note.", Toast.LENGTH_SHORT).show();
            }
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
                    .append(month + 1).append("-0").append(dayOfMonth).append("-").append(year).toString());
        } else {
            dateOfEntryEditText.setText(new StringBuilder().append(time).append(" ")
                    .append(month + 1).append("-").append(dayOfMonth).append("-").append(year).toString());
        }
    }
}