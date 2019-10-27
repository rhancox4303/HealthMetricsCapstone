package ca.mohawk.HealthMetrics.Notification;


import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import ca.mohawk.HealthMetrics.HealthMetricsDbHelper;
import ca.mohawk.HealthMetrics.Models.Metric;
import ca.mohawk.HealthMetrics.Models.Notification;
import ca.mohawk.HealthMetrics.Models.PhotoGallery;
import ca.mohawk.HealthMetrics.Models.Prescription;
import ca.mohawk.HealthMetrics.R;

/**
 * A simple {@link Fragment} subclass.
 */
public class EditNotificationFragment extends Fragment implements View.OnClickListener {


    private int NotificationId;
    private int Minute;
    private int Hour;
    private int Day;
    private int Month;
    private int Year;

    HealthMetricsDbHelper healthMetricsDbHelper;

    public EditNotificationFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        healthMetricsDbHelper = HealthMetricsDbHelper.getInstance(getActivity());
        // Inflate the layout for this fragment
        View rootView = inflater.inflate(R.layout.fragment_edit_notification, container, false);

        Bundle bundle = this.getArguments();
        if (bundle != null) {
            NotificationId = bundle.getInt("notification_selected_key", -1);
        }

        Notification notification = healthMetricsDbHelper.getNotificationById(NotificationId);

        TextView typeTextView = rootView.findViewById(R.id.textViewTypeEditNotification);
        TextView targetTextView = rootView.findViewById(R.id.textViewTargetEditNotification);

        EditText dateTimeEditText = rootView.findViewById(R.id.editTextDateTimeEditNotification);
        Button editNotificationButton = rootView.findViewById(R.id.buttonEditNotification);

        dateTimeEditText.setOnClickListener(this);
        editNotificationButton.setOnClickListener(this);

        typeTextView.setText(notification.NotificationType);

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

        return rootView;
    }

    @Override
    public void onClick(View v) {

    }
}
