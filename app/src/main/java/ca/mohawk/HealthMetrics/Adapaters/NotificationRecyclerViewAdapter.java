package ca.mohawk.HealthMetrics.Adapaters;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;
import ca.mohawk.HealthMetrics.HealthMetricsDbHelper;
import ca.mohawk.HealthMetrics.MainActivity;
import ca.mohawk.HealthMetrics.Models.Metric;
import ca.mohawk.HealthMetrics.Models.Notification;
import ca.mohawk.HealthMetrics.Models.PhotoGallery;
import ca.mohawk.HealthMetrics.Models.Prescription;
import ca.mohawk.HealthMetrics.Notification.ViewNotificationFragment;
import ca.mohawk.HealthMetrics.R;

/**
 * Acts as a custom adapter to display
 * the notifications in the notification list Recycler View.
 */
public class NotificationRecyclerViewAdapter extends RecyclerView.Adapter<NotificationRecyclerViewAdapter.ViewHolder> {

    // Instantiate the context variable.
    private Context context;

    // Instantiate the list of notifications to use in the adapter.
    private ArrayList<Notification> notifications;

    /**
     * Creates the adapter.
     *
     * @param notifications Represents the list of notifications.
     * @param context       Represents the application context.
     */
    public NotificationRecyclerViewAdapter(ArrayList<Notification> notifications, Context context) {
        this.notifications = notifications;
        this.context = context;
    }

    /**
     * Creates the View Holder.
     *
     * @param parent   Represents the parent view group.
     * @param viewType Represents the view type.
     * @return A created view holder is returned.
     */
    @NonNull
    @Override
    public NotificationRecyclerViewAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        // Get the context.
        Context context = parent.getContext();

        // Inflate the view.
        LayoutInflater inflater = LayoutInflater.from(context);
        View contactView = inflater.inflate(R.layout.notification_list_recycler_view_layout, parent, false);

        // Return the View Holder.
        return new ViewHolder(contactView);
    }

    /**
     * Sets the item views in the view holder.
     *
     * @param viewHolder Represents the view holder.
     * @param position   Represents the position of the notification that is being displayed.
     */
    @Override
    public void onBindViewHolder(@NonNull NotificationRecyclerViewAdapter.ViewHolder viewHolder, int position) {

        // Get notification object.
        final Notification notification = notifications.get(position);

        // Instantiate the information variable.
        String information = "";

        // Instantiate the healthMetricsDbHelper.
        HealthMetricsDbHelper healthMetricsDbHelper = HealthMetricsDbHelper.getInstance(context);

        // Create the information variable based on the type of notification.
        // Verify that the data gotten from the database is not null.
        switch (notification.NotificationType) {
            case "Enter Metric Data":
                Metric metric = healthMetricsDbHelper.getMetricById(notification.TargetId);
                information = metric != null ? "Input " + metric.Name : "";
                break;
            case "Enter Gallery Data":
                PhotoGallery gallery = healthMetricsDbHelper.getPhotoGalleryById(notification.TargetId);
                information = gallery != null ? "Input " + gallery.Name : "";
                break;
            case "Refill Prescription":
                Prescription prescriptionRefill = healthMetricsDbHelper.getPrescriptionById(notification.TargetId);
                information = prescriptionRefill != null ? "Refill " + prescriptionRefill.Name : "";
                break;
            case "Take Prescription":
                Prescription prescriptionTake = healthMetricsDbHelper.getPrescriptionById(notification.TargetId);
                information = prescriptionTake != null ? "Take " + prescriptionTake.Name : "";
                break;
        }

        // Display the information in the recycler view.
        TextView notificationInformationTextView = viewHolder.notificationInformationTextView;
        notificationInformationTextView.setText(information);

        // Display the target date time in the recycler view.
        TextView notificationDateTextView = viewHolder.notificationDateTextView;
        notificationDateTextView.setText(notification.TargetDateTime);

        // Set the itemView onCLickListener.
        viewHolder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                switchFragment(notification);
            }
        });
    }

    /**
     * Gets the new fragment and calls switch fragment on the main activity.
     *
     * @param selectedNotification Represents the selected notification.
     */
    private void switchFragment(Notification selectedNotification) {

        // Create ViewNotificationFragment.
        Fragment destinationFragment = new ViewNotificationFragment();

        // Create and the notification id to a bundle.
        Bundle prescriptionBundle = new Bundle();
        prescriptionBundle.putInt("notification_selected_key", selectedNotification.Id);

        // Set the bundle to the destination fragment.
        destinationFragment.setArguments(prescriptionBundle);

        // If the context is an instance of MainActivity then call switchFragment.
        if (context instanceof MainActivity) {
            MainActivity mainActivity = (MainActivity) context;
            mainActivity.switchFragment(destinationFragment);
        }
    }

    // Returns the size of notifications.
    @Override
    public int getItemCount() {
        return notifications.size();
    }


    /**
     * Defines the view of a row inside the recycler view.
     */
    class ViewHolder extends RecyclerView.ViewHolder {

        // Initialize the information and the date text views.
        TextView notificationInformationTextView;
        TextView notificationDateTextView;

        ViewHolder(View itemView) {
            super(itemView);

            // Get the text views from the notifications list recycler view layout.
            notificationInformationTextView = itemView.findViewById(R.id.textViewNotificationInformation);
            notificationDateTextView = itemView.findViewById(R.id.textViewNotificationDate);
        }
    }
}
