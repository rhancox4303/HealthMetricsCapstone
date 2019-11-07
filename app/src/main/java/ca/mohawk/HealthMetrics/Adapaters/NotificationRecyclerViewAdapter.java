package ca.mohawk.HealthMetrics.Adapaters;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

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

public class NotificationRecyclerViewAdapter extends RecyclerView.Adapter<NotificationRecyclerViewAdapter.ViewHolder> {

    private Context context;

    //The list of prescription to be displayed in the recycler view.
    private ArrayList<Notification> notificationList;

    public NotificationRecyclerViewAdapter(ArrayList<Notification> notificationList, Context context) {
        this.notificationList = notificationList;
        this.context = context;
    }

    class ViewHolder extends RecyclerView.ViewHolder {
        TextView notificationInformationTextView;
        TextView notificationDateTextView;
        // public Button incrementAmountButton;
        // public Button decrementAmountButton;

        ViewHolder(View itemView) {
            super(itemView);

            notificationInformationTextView = itemView.findViewById(R.id.textViewNotificationInformation);
            notificationDateTextView = itemView.findViewById(R.id.textViewNotificationDate);
        }
    }

    /**
     * The onCreateViewHolder method is used to inflate
     * the custom layout and create the viewholder.
     */
    @NonNull
    @Override
    public NotificationRecyclerViewAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        Context context = parent.getContext();
        LayoutInflater inflater = LayoutInflater.from(context);

        View contactView = inflater.inflate(R.layout.notification_list_recycler_view_layout, parent, false);

        return new ViewHolder(contactView);
    }

    /**
     * The onBindViewHolder method is used to set the item views in the recycler
     * view using the metricRecyclerViewObjectList and the view holder.
     */
    @Override
    public void onBindViewHolder(@NonNull NotificationRecyclerViewAdapter.ViewHolder viewHolder, int position) {
        //Get data object
        final Notification notification = notificationList.get(position);

        String information = "";

        HealthMetricsDbHelper healthMetricsDbHelper = HealthMetricsDbHelper.getInstance(context);

        switch (notification.NotificationType) {
            case "Enter Metric Data":
                Metric metric = healthMetricsDbHelper.getMetricById(notification.TargetId);
                information = "Input " + metric.Name;
                break;
            case "Enter Gallery Data":
                PhotoGallery gallery = healthMetricsDbHelper.getPhotoGalleryById(notification.TargetId);
                information = "Input " + gallery.Name;
                break;
            case "Refill Prescription":
                Prescription prescriptionRefill = healthMetricsDbHelper.getPrescriptionById(notification.TargetId);
                information = "Refill " + prescriptionRefill.Name;
                break;
            case "Take Prescription":
                Prescription prescriptionTake = healthMetricsDbHelper.getPrescriptionById(notification.TargetId);
                information = "Take " + prescriptionTake.Name;
                break;
        }

        TextView notificationInformationTextView = viewHolder.notificationInformationTextView;
        TextView notificationDateTextView = viewHolder.notificationDateTextView;

        notificationInformationTextView.setText(information);
        notificationDateTextView.setText(notification.TargetDateTime);

        viewHolder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                changeFragment(notification);
            }
        });
    }

    private void changeFragment(Notification itemSelected) {
        Fragment fragment = new ViewNotificationFragment();

        Bundle prescriptionBundle = new Bundle();
        prescriptionBundle.putInt("notification_selected_key", itemSelected.Id);

        fragment.setArguments(prescriptionBundle);
        switchContent(fragment);
    }

    private void switchContent(Fragment fragment) {
        if (context == null)
            return;
        if (context instanceof MainActivity) {
            MainActivity mainActivity = (MainActivity) context;
            mainActivity.switchContent(fragment);
        }
    }

    // Returns the total count of items in the list
    @Override
    public int getItemCount() {
        return notificationList.size();
    }
}
