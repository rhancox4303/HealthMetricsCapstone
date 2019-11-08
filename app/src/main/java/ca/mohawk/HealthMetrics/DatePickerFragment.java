package ca.mohawk.HealthMetrics;


import android.app.DatePickerDialog;
import android.app.Dialog;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.Fragment;

import java.util.Calendar;
import java.util.Objects;

/**
 * The DatePickerFragment class is an extension of the Dialog Fragment class.
 * Creates a standardized Date Picker Dialog used across the application.
 */
public class DatePickerFragment extends DialogFragment{

    // Initialize the onDateSetListener.
    private DatePickerDialog.OnDateSetListener onDateSetListener;

    /**
     * Sets the onDateSetListener.
     *
     * @param onDateSetListener Represents the onDateSetListener.
     */
    public void setOnDateSetListener(DatePickerDialog.OnDateSetListener onDateSetListener) {
        this.onDateSetListener = onDateSetListener;
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        // Use the current date as the default date in the picker
        final Calendar c = Calendar.getInstance();
        int year = c.get(Calendar.YEAR);
        int month = c.get(Calendar.MONTH);
        int day = c.get(Calendar.DAY_OF_MONTH);

        // Create a new instance of DatePickerDialog and return it
        return new DatePickerDialog(Objects.requireNonNull(getActivity()), onDateSetListener, year, month, day);
    }
}
