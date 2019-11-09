package ca.mohawk.HealthMetrics.Notification;


import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import java.util.Objects;

import androidx.fragment.app.Fragment;
import ca.mohawk.HealthMetrics.HealthMetricsDbHelper;
import ca.mohawk.HealthMetrics.MetricManagement.MetricsListFragment;
import ca.mohawk.HealthMetrics.Models.Metric;
import ca.mohawk.HealthMetrics.Models.Notification;
import ca.mohawk.HealthMetrics.Models.PhotoGallery;
import ca.mohawk.HealthMetrics.Models.Prescription;
import ca.mohawk.HealthMetrics.R;


/**
 * The ViewNotificationFragment class is an extension of the Fragment class.
 * Allows the user to view a notification.
 */
public class ViewNotificationFragment extends Fragment implements View.OnClickListener {

    // Instantiate the notificationId variable.
    private int notificationId;

    public ViewNotificationFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootView = inflater.inflate(R.layout.fragment_view_notification, container, false);

        // Get the healthMetricsDbHelper.
        HealthMetricsDbHelper healthMetricsDbHelper = HealthMetricsDbHelper.getInstance(getActivity());

        // Get the views.
        TextView typeTextView = rootView.findViewById(R.id.textViewTypeViewNotification);
        TextView targetTextView = rootView.findViewById(R.id.textViewTargetViewNotification);
        TextView dateTimeTextView = rootView.findViewById(R.id.textViewDateTimeViewNotification);

        Button deleteButton = rootView.findViewById(R.id.buttonDeleteNotificationViewNotification);
        Button editButton = rootView.findViewById(R.id.buttonEditNotificationViewNotification);

        // Set the OnClickListeners.
        editButton.setOnClickListener(this);
        deleteButton.setOnClickListener(this);

        // Get the notification id from the passed bundle.
        Bundle bundle = this.getArguments();
        if (bundle != null) {
            notificationId = bundle.getInt("notification_selected_key", -1);
        }

        // Get the notification from the database.
        Notification notification = healthMetricsDbHelper.getNotificationById(notificationId);

        // Validate the notification is not null.
        if (notification != null) {
            typeTextView.setText(notification.notificationType);
            dateTimeTextView.setText(notification.targetDateTime);
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
            navigateToMetricsListFragment();
        }

        // Return rootView.
        return rootView;
    }

    /**
     * Replaces the current fragment with a MetricsListFragment.
     */
    private void navigateToMetricsListFragment() {

        MetricsListFragment destinationFragment = new MetricsListFragment();

        Objects.requireNonNull(getActivity()).getSupportFragmentManager().beginTransaction()
                .replace(R.id.fragmentContainer, destinationFragment)
                .addToBackStack(null)
                .commit();
    }

    /**
     * Runs when a view's onClickListener is activated.
     *
     * @param v Represents the view.
     */
    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.buttonDeleteNotificationViewNotification) {
            DeleteNotificationDialog deleteNotificationDialog = DeleteNotificationDialog.newInstance(notificationId);
            deleteNotificationDialog.show(Objects.requireNonNull(getFragmentManager()), "deleteNotificationDialog");
        } else if (v.getId() == R.id.buttonEditNotificationViewNotification) {

            EditNotificationFragment editNotificationFragment = new EditNotificationFragment();

            Bundle bundle = new Bundle();
            bundle.putInt("notification_selected_key", notificationId);
            editNotificationFragment.setArguments(bundle);

            Objects.requireNonNull(getActivity()).getSupportFragmentManager().beginTransaction()
                    .replace(R.id.fragmentContainer, editNotificationFragment)
                    .addToBackStack(null)
                    .commit();
        }
    }
}
