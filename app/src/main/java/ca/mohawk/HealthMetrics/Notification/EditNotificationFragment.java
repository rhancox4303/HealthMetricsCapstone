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
import ca.mohawk.HealthMetrics.Models.Metric;
import ca.mohawk.HealthMetrics.Models.Notification;
import ca.mohawk.HealthMetrics.Models.PhotoGallery;
import ca.mohawk.HealthMetrics.Models.Prescription;
import ca.mohawk.HealthMetrics.NotificationReceiver;
import ca.mohawk.HealthMetrics.R;
import ca.mohawk.HealthMetrics.TimePickerFragment;

/**
 * A simple {@link Fragment} subclass.
 */
public class EditNotificationFragment extends Fragment implements View.OnClickListener, TimePickerDialog.OnTimeSetListener, DatePickerDialog.OnDateSetListener {

    private int NotificationId;
    private int Minute;
    private int Hour;
    private int Day;
    private int Month;
    private int Year;
    private EditText DateTimeEditText;

    private HealthMetricsDbHelper healthMetricsDbHelper;

    private String time;
    private Notification notification;

    public EditNotificationFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        healthMetricsDbHelper = HealthMetricsDbHelper.getInstance(getActivity());
        // Inflate the layout for this fragment
        View rootView = inflater.inflate(R.layout.fragment_edit_notification, container, false);

        TextView typeTextView = rootView.findViewById(R.id.textViewTypeEditNotification);
        TextView targetTextView = rootView.findViewById(R.id.textViewTargetEditNotification);

        DateTimeEditText = rootView.findViewById(R.id.editTextDateTimeEditNotification);
        Button editNotificationButton = rootView.findViewById(R.id.buttonEditNotification);

        DateTimeEditText.setOnClickListener(this);

        editNotificationButton.setOnClickListener(this);

        Bundle bundle = this.getArguments();
        if (bundle != null) {
            NotificationId = bundle.getInt("notification_selected_key", -1);
        }

        notification = healthMetricsDbHelper.getNotificationById(NotificationId);

        if (notification != null) {
            typeTextView.setText(notification.NotificationType);
            DateTimeEditText.setText(notification.TargetDateTime);
            switch (notification.NotificationType) {
                case "Enter Metric Data":
                    Metric targetMetric = healthMetricsDbHelper.getMetricById(notification.TargetId);
                    targetTextView.setText(targetMetric.Name);
                    break;
                case "Enter Gallery Data":
                    PhotoGallery targetGallery = healthMetricsDbHelper.getPhotoGalleryById(notification.TargetId);
                    targetTextView.setText(targetGallery.Name);
                    break;
                case "Refill Prescription":
                case "Take Prescription":
                    Prescription targetPrescription = healthMetricsDbHelper.getPrescriptionById(notification.TargetId);
                    targetTextView.setText(targetPrescription.Name);
                    break;
            }
        }

        return rootView;
    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.editTextDateTimeEditNotification) {
            TimePickerFragment timePickerFragment = new TimePickerFragment();
            timePickerFragment.setOnTimeSetListener(this);
            timePickerFragment.show(Objects.requireNonNull(getFragmentManager()).beginTransaction(), "timePicker");
        } else if (v.getId() == R.id.buttonEditNotification) {
            editNotification();
        }
    }

    private void editNotification() {
        if (validateUserInput()) {
            cancelNotification();
            notification.TargetDateTime = DateTimeEditText.getText().toString();
            healthMetricsDbHelper.updateNotification(notification);
            startAlarm(notification.Id);

            Fragment notificationList = new NotificationListFragment();

            Objects.requireNonNull(getActivity()).getSupportFragmentManager().beginTransaction()
                    .replace(R.id.fragmentContainer, notificationList)
                    .addToBackStack(null)
                    .commit();
        }
    }

    private boolean validateUserInput() {
        if (DateTimeEditText.getText().toString().trim().equals("")) {
            Toast.makeText(getActivity(), "The date and time cannot be empty.", Toast.LENGTH_SHORT).show();
            return false;
        }

        if (DateTimeEditText.getText().toString().trim().equals(notification.TargetDateTime)) {
            Toast.makeText(getActivity(), "Please enter a new date and time.", Toast.LENGTH_SHORT).show();
            return false;
        }

        if(!DateTimeEditText.getText().toString().matches("^(\\d+:\\d\\d)\\s(\\d+-\\d\\d-\\d+)$")){
            Toast.makeText(getActivity(), "Both a date and time is required.", Toast.LENGTH_SHORT).show();
            return false;
        }
        return true;
    }


    private void startAlarm(int id) {

        AlarmManager alarmManager = (AlarmManager) Objects.requireNonNull(getActivity()).getSystemService(Context.ALARM_SERVICE);
        Intent intent = new Intent(getActivity(), NotificationReceiver.class);
        intent.putExtra("id", id);

        PendingIntent pendingIntent = PendingIntent.getBroadcast(getActivity(), id, intent, 0);
        Calendar calendar = Calendar.getInstance();

        calendar.setTimeInMillis(System.currentTimeMillis());

        // Specify the date/time to trigger the alarm
        calendar.set(Calendar.YEAR, Year);
        calendar.set(Calendar.MONTH, Month);
        calendar.set(Calendar.DAY_OF_MONTH, Day);
        calendar.set(Calendar.HOUR_OF_DAY, Hour);
        calendar.set(Calendar.MINUTE, Minute);
        calendar.set(Calendar.SECOND, 0);

        alarmManager.setExact(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(), pendingIntent);
    }

    private void cancelNotification() {
        AlarmManager alarmManager = (AlarmManager) Objects.requireNonNull(getActivity()).getSystemService(Context.ALARM_SERVICE);
        Intent intent = new Intent(getActivity(), NotificationReceiver.class);
        intent.putExtra("id", NotificationId);

        PendingIntent pendingIntent = PendingIntent.getBroadcast(getActivity(), NotificationId, intent, 0);
        alarmManager.cancel(pendingIntent);
    }

    @Override
    public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
        Minute = minute;
        Hour = hourOfDay;
        if (minute < 10) {
            time = hourOfDay + ":0" + minute;
        } else {
            time = hourOfDay + ":" + minute;
        }

        DateTimeEditText.setText(time);

        DatePickerFragment datePickerFragment = new DatePickerFragment();
        datePickerFragment.setOnDateSetListener(this);
        datePickerFragment.show(Objects.requireNonNull(getFragmentManager()).beginTransaction(), "datePicker");
    }

    @Override
    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
        Year = year;
        Month = month;
        Day = dayOfMonth;
        if (dayOfMonth < 10) {
            DateTimeEditText.setText(new StringBuilder().append(time).append(" ").append(month + 1)
                    .append("-0").append(dayOfMonth).append("-").append(year).toString());
        } else {
            DateTimeEditText.setText(new StringBuilder().append(time).append(" ").append(month + 1)
                    .append("-").append(dayOfMonth).append("-").append(year).toString());
        }
    }
}