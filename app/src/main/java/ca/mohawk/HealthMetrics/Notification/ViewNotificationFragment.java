package ca.mohawk.HealthMetrics.Notification;


import android.os.Bundle;
import androidx.fragment.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
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
public class ViewNotificationFragment extends Fragment {

    private int NotificationId;
    private HealthMetricsDbHelper healthMetricsDbHelper;
    public ViewNotificationFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        healthMetricsDbHelper = HealthMetricsDbHelper.getInstance(getActivity());

        View rootView =  inflater.inflate(R.layout.fragment_view_notification, container, false);
        TextView typeTextView = rootView.findViewById(R.id.textViewTypeViewNotification);
        TextView targetTextView = rootView.findViewById(R.id.textViewTargetViewNotification);
        TextView dateTimeTextView = rootView.findViewById(R.id.textViewDateTimeViewNotification);


        Bundle bundle = this.getArguments();
        if (bundle != null) {
            NotificationId = bundle.getInt("notification_selected_key", -1);
        }

        Notification notification = healthMetricsDbHelper.getNotificationById(NotificationId);
        typeTextView.setText(notification.NotificationType);
        dateTimeTextView.setText(notification.TargetDateTime);

        switch (notification.NotificationType) {
            case "Enter Metric Data":
                Metric metric = healthMetricsDbHelper.getMetricById(notification.TargetId);
                targetTextView.setText(metric.Name);
                break;
            case "Enter Gallery Data":
                PhotoGallery gallery = healthMetricsDbHelper.getPhotoGalleryById(notification.TargetId);
                targetTextView.setText(gallery.Name);
                break;
            case "Refill Prescription":
            case "Take Prescription":
                Prescription prescription = healthMetricsDbHelper.getPrescriptionById(notification.TargetId);
                targetTextView.setText(prescription.Name);
                break;
        }
        return rootView;
    }
}
