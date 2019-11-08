package ca.mohawk.HealthMetrics;

import android.app.Dialog;
import android.app.TimePickerDialog;
import android.os.Bundle;
import android.text.format.DateFormat;

import java.util.Calendar;

import androidx.annotation.NonNull;
import androidx.fragment.app.DialogFragment;

/**
 * The TimePickerFragment class is an extension of the Dialog Fragment class.
 * Creates a standardized Time Picker Dialog used across the application.
 */
public class TimePickerFragment extends DialogFragment {

    // Initialize the onDateSetListener.
    private TimePickerDialog.OnTimeSetListener onTimeSetListener;

    /**
     * Sets the onTimeSetListener.
     *
     * @param onTimeSetListener Represents the onTimeSetListener.
     */
    public void setOnTimeSetListener(TimePickerDialog.OnTimeSetListener onTimeSetListener) {
        this.onTimeSetListener = onTimeSetListener;
    }

    @NonNull
    public Dialog onCreateDialog(Bundle savedInstanceState) {

        final Calendar c = Calendar.getInstance();
        int hour = c.get(Calendar.HOUR_OF_DAY);
        int minute = c.get(Calendar.MINUTE);

        // Return a new instance of TimePickerDialog
        return new TimePickerDialog(getActivity(), onTimeSetListener, hour, minute,
                DateFormat.is24HourFormat(getActivity()));
    }
}
