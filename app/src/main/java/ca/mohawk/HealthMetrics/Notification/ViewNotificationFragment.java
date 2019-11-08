package ca.mohawk.HealthMetrics.Notification;


import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import java.util.Objects;

import androidx.fragment.app.Fragment;
import ca.mohawk.HealthMetrics.HealthMetricsDbHelper;
import ca.mohawk.HealthMetrics.Models.Metric;
import ca.mohawk.HealthMetrics.Models.Notification;
import ca.mohawk.HealthMetrics.Models.PhotoGallery;
import ca.mohawk.HealthMetrics.Models.Prescription;
import ca.mohawk.HealthMetrics.R;


/**
 * A simple {@link Fragment} subclass.
 */
public class ViewNotificationFragment extends Fragment implements View.OnClickListener {

    private int NotificationId;

    public ViewNotificationFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        HealthMetricsDbHelper healthMetricsDbHelper = HealthMetricsDbHelper.getInstance(getActivity());

        View rootView = inflater.inflate(R.layout.fragment_view_notification, container, false);
        TextView typeTextView = rootView.findViewById(R.id.textViewTypeViewNotification);
        TextView targetTextView = rootView.findViewById(R.id.textViewTargetViewNotification);
        TextView dateTimeTextView = rootView.findViewById(R.id.textViewDateTimeViewNotification);

        Button deleteButton = rootView.findViewById(R.id.buttonDeleteNotificationViewNotification);
        Button editButton = rootView.findViewById(R.id.buttonEditNotificationViewNotification);

        editButton.setOnClickListener(this);
        deleteButton.setOnClickListener(this);

        Bundle bundle = this.getArguments();
        if (bundle != null) {
            NotificationId = bundle.getInt("notification_selected_key", -1);
        }

        Notification notification = healthMetricsDbHelper.getNotificationById(NotificationId);

        if (notification != null) {
            typeTextView.setText(notification.notificationType);
            dateTimeTextView.setText(notification.targetDateTime);
            switch (notification.notificationType) {
                case "Enter Metric Data":
                    Metric metric = healthMetricsDbHelper.getMetricById(notification.targetId);
                    targetTextView.setText(metric.name);
                    break;
                case "Enter Gallery Data":
                    PhotoGallery gallery = healthMetricsDbHelper.getPhotoGalleryById(notification.targetId);
                    targetTextView.setText(gallery.name);
                    break;
                case "Refill Prescription":
                case "Take Prescription":
                    Prescription prescription = healthMetricsDbHelper.getPrescriptionById(notification.targetId);
                    targetTextView.setText(prescription.name);
                    break;
            }
        }
        return rootView;
    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.buttonDeleteNotificationViewNotification) {
            DeleteNotificationDialog deleteNotificationDialog = DeleteNotificationDialog.newInstance(NotificationId);
            deleteNotificationDialog.show(Objects.requireNonNull(getFragmentManager()), "dialog");
        } else if (v.getId() == R.id.buttonEditNotificationViewNotification) {

            EditNotificationFragment editNotificationFragment = new EditNotificationFragment();

            Bundle bundle = new Bundle();
            bundle.putInt("notification_selected_key", NotificationId);
            editNotificationFragment.setArguments(bundle);

            Objects.requireNonNull(getActivity()).getSupportFragmentManager().beginTransaction()
                    .replace(R.id.fragmentContainer, editNotificationFragment)
                    .addToBackStack(null)
                    .commit();
        }
    }
}
