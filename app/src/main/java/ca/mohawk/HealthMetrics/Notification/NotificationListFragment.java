package ca.mohawk.HealthMetrics.Notification;


import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import java.util.ArrayList;
import java.util.Objects;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import ca.mohawk.HealthMetrics.Adapaters.NotificationRecyclerViewAdapter;
import ca.mohawk.HealthMetrics.HealthMetricsDbHelper;
import ca.mohawk.HealthMetrics.Models.Notification;
import ca.mohawk.HealthMetrics.R;


/**
 * The NotificationListFragment class is an extension of the Fragment class.
 * Allows the user to view all notification.
 */
public class NotificationListFragment extends Fragment implements View.OnClickListener {

    public NotificationListFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        // Inflate the layout for this fragment
        View rootView = inflater.inflate(R.layout.fragment_notification_list, container, false);

        // Get the healthMetricsDbHelper
        HealthMetricsDbHelper healthMetricsDbHelper = HealthMetricsDbHelper.getInstance(getActivity());

        // Get the views.
        Button createNotificationButton = rootView.findViewById(R.id.buttonCreateNotificationNotificationList);
        createNotificationButton.setOnClickListener(this);

        // Display all notifications from the Recycler View.
        RecyclerView notificationRecyclerView = rootView.findViewById(R.id.recyclerViewNotificationList);

        ArrayList<Notification> notifications = healthMetricsDbHelper.getAllNotifications();
        NotificationRecyclerViewAdapter adapter = new NotificationRecyclerViewAdapter(notifications, getActivity());
        notificationRecyclerView.setAdapter(adapter);
        notificationRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));

        // Return the rootView.
        return rootView;
    }
    
    /**
     * Runs when a view's onClickListener is activated.
     *
     * @param v Represents the view.
     */
    @Override
    public void onClick(View v) {

        CreateNotificationFragment createNotificationFragment = new CreateNotificationFragment();
        Objects.requireNonNull(getActivity()).getSupportFragmentManager().beginTransaction()
                .replace(R.id.fragmentContainer, createNotificationFragment)
                .addToBackStack(null)
                .commit();
    }
}
