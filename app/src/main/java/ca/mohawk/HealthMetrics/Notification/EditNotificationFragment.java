package ca.mohawk.HealthMetrics.Notification;


import android.app.AlarmManager;
import android.app.DatePickerDialog;
import android.app.PendingIntent;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import java.util.Calendar;
import java.util.Objects;

import androidx.fragment.app.Fragment;
import ca.mohawk.HealthMetrics.DatePickerFragment;
import ca.mohawk.HealthMetrics.HealthMetricsDbHelper;
import ca.mohawk.HealthMetrics.MetricManagement.MetricsListFragment;
import ca.mohawk.HealthMetrics.Models.Metric;
import ca.mohawk.HealthMetrics.Models.Notification;
import ca.mohawk.HealthMetrics.Models.PhotoGallery;
import ca.mohawk.HealthMetrics.Models.Prescription;
import ca.mohawk.HealthMetrics.NotificationReceiver;
import ca.mohawk.HealthMetrics.R;
import ca.mohawk.HealthMetrics.TimePickerFragment;

/**
 * The EditNotificationFragment class is an extension of the Fragment class.
 * Allows the user to edit a notification.
 */
public class EditNotificationFragment extends Fragment implements View.OnClickListener,
        TimePickerDialog.OnTimeSetListener, DatePickerDialog.OnDateSetListener {

    // Instantiate the notificationId variable.
    private int notificationId;

    // Initialize the notification date variables.
    private int minute;
    private int hour;
    private int day;
    private int month;
    private int year;

    // Instantiate the dateTimeEditText
    private EditText dateTimeEditText;

    // Instantiate the healthMetricsDbHelper.
    private HealthMetricsDbHelper healthMetricsDbHelper;

    // Instantiate the time.
    private String time;

    // Instantiate the notification
    private Notification notification;

    public EditNotificationFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        // Inflate the layout for this fragment
        View rootView = inflater.inflate(R.layout.fragment_edit_notification,
                container, false);

        // Get the healthMetricsDbHelper.
        healthMetricsDbHelper = HealthMetricsDbHelper.getInstance(getActivity());

        // Get the views.
        TextView typeTextView = rootView.findViewById(R.id.textViewTypeEditNotification);
        TextView targetTextView = rootView.findViewById(R.id.textViewTargetEditNotification);
        Button editNotificationButton = rootView.findViewById(R.id.buttonEditNotification);
        dateTimeEditText = rootView.findViewById(R.id.editTextDateTimeEditNotification);

        // Set the OnClickListener.
        dateTimeEditText.setOnClickListener(this);
        editNotificationButton.setOnClickListener(this);

        // Get the notification id from the passed bundle.
        Bundle bundle = this.getArguments();
        if (bundle != null) {
            notificationId = bundle.getInt("notification_selected_key", -1);
        }

        // Get the notification from the database.
        notification = healthMetricsDbHelper.getNotificationById(notificationId);

        // Validate the notification is not null.
        if (notification != null) {
            typeTextView.setText(notification.notificationType);
            dateTimeEditText.setText(notification.targetDateTime);
            // Validate the target is not null and display the target.
            switch (notification.notificationType) {
                case "Enter Metric Data":
                    Metric metric = healthMetricsDbHelper.getMetricById(notification.targetId);
                    String targetMetric = metric != null ? metric.name : "";
                    targetTextView.setText(targetMetric);
                    break;
                case "Enter Gallery Data":
                    PhotoGallery gallery = healthMetricsDbHelper.getPhotoGalleryById(notification.targetId);
                    String targetGallery = gallery != null ? gallery.name : "";
                    targetTextView.setText(targetGallery);
                    break;
                case "Refill Prescription":
                case "Take Prescription":
                    Prescription prescription = healthMetricsDbHelper.getPrescriptionById(notification.targetId);
                    String targetPrescription = prescription != null ? prescription.name : "";
                    targetTextView.setText(targetPrescription);
                    break;
            }
        } else {
            Toast.makeText(getActivity(), "Cannot load notification from database.",
                    Toast.LENGTH_SHORT).show();
            navigateToNotificationListFragment();
        }

        // Return rootView.
        return rootView;
    }

    /**
     * Replaces the current fragment with a NotificationList Fragment.
     */
    private void navigateToNotificationListFragment() {

        NotificationListFragment destinationFragment = new NotificationListFragment();

        Objects.requireNonNull(getActivity()).getSupportFragmentManager().beginTransaction()
                .replace(R.id.fragmentContainer, destinationFragment)
                .addToBackStack(null)
                .commit();
    }

    /**
     * Runs when the date and time edit text and edit notification button are pressed.
     *
     * @param v Represents the view.
     */
    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.editTextDateTimeEditNotification) {
            TimePickerFragment timePickerFragment = new TimePickerFragment();
            timePickerFragment.setOnTimeSetListener(this);
            timePickerFragment.show(Objects.requireNonNull(getFragmentManager()).beginTransaction(), "timePicker");
        } else if (v.getId() == R.id.buttonEditNotification) {
            updateNotification();
        }
    }

    /**
     * Update the notification with the user input.
     */
    private void updateNotification() {

        // Validate the user input.
        if (validateUserInput()) {

            // Cancel the alarm.
            cancelNotification();

            // Set the targetDateTime.
            notification.targetDateTime = dateTimeEditText.getText().toString();

            // Validate the update was successful.
            if (healthMetricsDbHelper.updateNotification(notification)) {
                startAlarm(notification.id);

                Fragment notificationList = new NotificationListFragment();

                Objects.requireNonNull(getActivity()).getSupportFragmentManager().beginTransaction()
                        .replace(R.id.fragmentContainer, notificationList)
                        .addToBackStack(null)
                        .commit();
            } else {
                Toast.makeText(getActivity(), "Failed to update alarm.", Toast.LENGTH_SHORT).show();
            }
        }
    }

    /**
     * Validates the user inputs.
     *
     * @return Return a boolean based on whether the user input is valid.
     */
    private boolean validateUserInput() {

        //  If date of entry is empty then inform the user and return false.
        if (dateTimeEditText.getText().toString().trim().equals("")) {
            Toast.makeText(getActivity(), "The date and time cannot be empty.", Toast.LENGTH_SHORT).show();
            return false;
        }
        //  If date of entry has not changed then inform the user and return false.
        if (dateTimeEditText.getText().toString().trim().equals(notification.targetDateTime)) {
            Toast.makeText(getActivity(), "Please enter a new date and time.", Toast.LENGTH_SHORT).show();
            return false;
        }

        // If date of entry is does not contain a date and time then inform the user and return false.
        if (!dateTimeEditText.getText().toString().matches("^(\\d+:\\d\\d)\\s(\\d+-\\d\\d-\\d+)$")) {
            Toast.makeText(getActivity(), "Both a date and time is required.", Toast.LENGTH_SHORT).show();
            return false;
        }

        // Return true.
        return true;
    }


    /**
     * Starts the alarm using the notification.
     *
     * @param id Represents the notification id.
     */
    private void startAlarm(int id) {

        // Get alarmManager.
        AlarmManager alarmManager = (AlarmManager) Objects.requireNonNull(getActivity())
                .getSystemService(Context.ALARM_SERVICE);

        Intent intent = new Intent(getActivity(), NotificationReceiver.class);
        intent.putExtra("id", id);

        PendingIntent pendingIntent = PendingIntent.getBroadcast(getActivity(), id, intent, 0);

        //  Get the calendar.
        Calendar calendar = Calendar.getInstance();

        calendar.setTimeInMillis(System.currentTimeMillis());

        // Specify the date and time to trigger the alarm.
        calendar.set(Calendar.YEAR, year);
        calendar.set(Calendar.MONTH, month);
        calendar.set(Calendar.DAY_OF_MONTH, day);
        calendar.set(Calendar.HOUR_OF_DAY, hour);
        calendar.set(Calendar.MINUTE, minute);
        calendar.set(Calendar.SECOND, 0);

        // Set the alarm.
        alarmManager.setExact(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(), pendingIntent);
    }

    /**
     * Cancel the alarm that contains the notification.
     */
    private void cancelNotification() {
        AlarmManager alarmManager = (AlarmManager) Objects.requireNonNull(getActivity()).getSystemService(Context.ALARM_SERVICE);
        Intent intent = new Intent(getActivity(), NotificationReceiver.class);
        intent.putExtra("id", notificationId);

        PendingIntent pendingIntent = PendingIntent.getBroadcast(getActivity(), notificationId, intent, 0);
        alarmManager.cancel(pendingIntent);
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
        this.minute = minute;
        hour = hourOfDay;
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
        this.year = year;
        this.month = month;
        day = dayOfMonth;
        if (dayOfMonth < 10) {
            dateTimeEditText.setText(new StringBuilder().append(time).append(" ").append(month + 1)
                    .append("-0").append(dayOfMonth).append("-").append(year).toString());
        } else {
            dateTimeEditText.setText(new StringBuilder().append(time).append(" ").append(month + 1)
                    .append("-").append(dayOfMonth).append("-").append(year).toString());
        }
    }
}