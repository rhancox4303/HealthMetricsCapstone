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
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TimePicker;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Objects;

import androidx.fragment.app.Fragment;
import ca.mohawk.HealthMetrics.DatePickerFragment;
import ca.mohawk.HealthMetrics.DisplayObjects.MetricDisplayObject;
import ca.mohawk.HealthMetrics.DisplayObjects.PrescriptionDisplayObject;
import ca.mohawk.HealthMetrics.HealthMetricsDbHelper;
import ca.mohawk.HealthMetrics.Models.Notification;
import ca.mohawk.HealthMetrics.NotificationReceiver;
import ca.mohawk.HealthMetrics.R;
import ca.mohawk.HealthMetrics.TimePickerFragment;

/**
 * The CreateNotificationFragment class is an extension of the Fragment class.
 * Allows the user to create a notification.
 */
public class CreateNotificationFragment extends Fragment implements AdapterView.OnItemSelectedListener,
        View.OnClickListener, DatePickerDialog.OnDateSetListener, TimePickerDialog.OnTimeSetListener {

    // Instantiate the healthMetricsDbHelper.
    private HealthMetricsDbHelper healthMetricsDbHelper;

    // Instantiate the healthMetricsDbHelper.
    private List<MetricDisplayObject> metrics = new ArrayList<>();
    private List<MetricDisplayObject> galleries = new ArrayList<>();
    private List<PrescriptionDisplayObject> prescriptionDisplayObjects;

    // Instantiate views.
    private Spinner notificationTargetSpinner;
    private EditText dateEditText;

    // Instantiate the notificationType.
    private String notificationType;

    // Instantiate targetId.
    private int targetId = -1;

    // Instantiate the time.
    private String time;

    // Initialize the notification date variables.
    private int minute;
    private int hour;
    private int day;
    private int month;
    private int year;


    public CreateNotificationFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        // Inflate the layout for this fragment
        View rootView = inflater.inflate(R.layout.fragment_create_notification, container,
                false);

        // Get the healthMetricsDbHelper.
        healthMetricsDbHelper = HealthMetricsDbHelper.getInstance(getActivity());

        // Get the lists of user aded metrics, galleries and all prescriptions from the database.
        metrics = healthMetricsDbHelper.getAddedMetrics();
        galleries = healthMetricsDbHelper.getAddedPhotoGalleries();
        prescriptionDisplayObjects = healthMetricsDbHelper.getAllPrescriptions();

        // Get the views.
        Button createNotificationButton = rootView.findViewById(R.id.buttonCreateNotification);
        dateEditText = rootView.findViewById(R.id.editTextDateCreateNotification);
        Spinner notificationTypeSpinner = rootView.findViewById(R.id.spinnerNotificationType);
        notificationTargetSpinner = rootView.findViewById(R.id.spinnerNotificationTarget);

        // Set the OnClickListeners.
        dateEditText.setOnClickListener(this);
        createNotificationButton.setOnClickListener(this);

        // Set the OnItemSelectedListeners.
        notificationTargetSpinner.setOnItemSelectedListener(this);
        notificationTypeSpinner.setOnItemSelectedListener(this);

        // Create and set a CharSequence array adapter for the notifications type.
        ArrayAdapter<CharSequence> notificationTypeAdapter =
                ArrayAdapter.createFromResource(rootView.getContext()
                        , R.array.notification_type_array, android.R.layout.simple_spinner_item);

        notificationTypeAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        notificationTypeSpinner.setAdapter(notificationTypeAdapter);

        return rootView;
    }

    /**
     * Runs when a item in a spinner is selected.
     *
     * @param parent   The parent adapter view.
     * @param view     The selected view.
     * @param position The position of the selected item in the spinner.
     * @param id       The id.
     */
    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

        // If the parent spinner is the NotificationType spinner get the notification type.
        if (parent.getId() == R.id.spinnerNotificationType) {
            notificationType = parent.getSelectedItem().toString();
            populateTargetSpinner(notificationType);

            // If the parent spinner is the NotificationTarget spinner get the notification target.
        } else if (parent.getId() == R.id.spinnerNotificationTarget) {
            switch (notificationType) {
                case "Enter Metric Data":
                case "Enter Gallery Data":
                    targetId = ((MetricDisplayObject) parent.getSelectedItem()).id;
                    break;
                case "Refill Prescription":
                case "Take Prescription":
                    targetId = ((PrescriptionDisplayObject) parent.getSelectedItem()).id;
                    break;
            }
        }
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {
    }


    /**
     * Populates the target spinner based on the type.
     *
     * @param type Represents the notification type.
     */
    private void populateTargetSpinner(String type) {

        targetId = -1;
        switch (type) {

            case "Enter Metric Data":
                ArrayAdapter<MetricDisplayObject> metricDisplayObjectArrayAdapter =
                        new ArrayAdapter<>(Objects.requireNonNull(getActivity()).getBaseContext(),
                                android.R.layout.simple_spinner_item, metrics);

                metricDisplayObjectArrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                notificationTargetSpinner.setAdapter(metricDisplayObjectArrayAdapter);

                break;

            case "Enter Gallery Data":
                ArrayAdapter<MetricDisplayObject> galleryDisplayObjectArrayAdapter =
                        new ArrayAdapter<>(Objects.requireNonNull(getActivity()).getBaseContext(),
                                android.R.layout.simple_spinner_item, galleries);

                galleryDisplayObjectArrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                notificationTargetSpinner.setAdapter(galleryDisplayObjectArrayAdapter);

                break;
            case "Refill Prescription":
            case "Take Prescription":

                ArrayAdapter<PrescriptionDisplayObject> prescriptionDisplayObjectArrayAdapter =
                        new ArrayAdapter<>(Objects.requireNonNull(getActivity()).getBaseContext(),
                                android.R.layout.simple_spinner_item, prescriptionDisplayObjects);

                prescriptionDisplayObjectArrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                notificationTargetSpinner.setAdapter(prescriptionDisplayObjectArrayAdapter);

                break;
        }
    }

    /**
     * Runs when a view's onClickListener is activated.
     *
     * @param v Represents the view.
     */
    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.editTextDateCreateNotification) {
            TimePickerFragment timePickerFragment = new TimePickerFragment();
            timePickerFragment.setOnTimeSetListener(this);
            timePickerFragment.show(Objects.requireNonNull(getFragmentManager()).beginTransaction(),
                    "timePicker");
        } else {
            createNotification();
        }
    }

    /***
     * Creates the notification in the database.
     */
    private void createNotification() {

        // Validate the user input.
        if (validateUserInput()) {

            String dateTime = dateEditText.getText().toString();
            Notification notification = new Notification(targetId, notificationType, dateTime);

            // Add the Notification to the database and get the id.
            int id = healthMetricsDbHelper.addNotification(notification);

            // Validate the notification was created successfully.
            if (id > 0) {
                startAlarm(id);

                NotificationListFragment notificationListFragment = new NotificationListFragment();
                Objects.requireNonNull(getActivity()).getSupportFragmentManager().beginTransaction()
                        .replace(R.id.fragmentContainer, notificationListFragment)
                        .addToBackStack(null)
                        .commit();
            } else {
                Toast.makeText(getActivity(), "Unable to add notification to database.",
                        Toast.LENGTH_SHORT).show();
            }
        }
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
     * Validates the user inputs.
     *
     * @return Return a boolean based on whether the user input is valid.
     */
    private boolean validateUserInput() {

        //  If date of entry is empty then inform the user and return false.
        if (dateEditText.getText().toString().trim().equals("")) {
            Toast.makeText(getActivity(), "Date and time of notification cannot be empty.", Toast.LENGTH_SHORT).show();
            return false;
        }

        //  If target is -1 then inform the user and return false.
        if (targetId == -1) {
            Toast.makeText(getActivity(), "Select a notification target.", Toast.LENGTH_SHORT).show();
            return false;
        }

        // If date of entry is does not contain a date and time then inform the user and return false.
        if (!dateEditText.getText().toString().matches("^(\\d+:\\d\\d)\\s(\\d+-\\d\\d-\\d+)$")) {
            Toast.makeText(getActivity(), "Both a date and time is required.", Toast.LENGTH_SHORT).show();
            return false;
        }

        // Return true.
        return true;
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
            dateEditText.setText(new StringBuilder().append(time).append(" ").append(month + 1)
                    .append("-0").append(dayOfMonth).append("-").append(year).toString());
        } else {
            dateEditText.setText(new StringBuilder().append(time).append(" ").append(month + 1)
                    .append("-").append(dayOfMonth).append("-").append(year).toString());
        }
    }
}
