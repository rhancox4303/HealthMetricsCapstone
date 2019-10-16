package ca.mohawk.HealthMetrics.Notification;


import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import java.util.ArrayList;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import ca.mohawk.HealthMetrics.Adapaters.NotificationRecyclerViewAdapter;
import ca.mohawk.HealthMetrics.HealthMetricsDbHelper;
import ca.mohawk.HealthMetrics.Models.Notification;
import ca.mohawk.HealthMetrics.R;


/**
 * A simple {@link Fragment} subclass.
 */
public class NotificationListFragment extends Fragment implements View.OnClickListener {

    private HealthMetricsDbHelper healthMetricsDbHelper;

    public NotificationListFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        healthMetricsDbHelper = HealthMetricsDbHelper.getInstance(getActivity());
        // Inflate the layout for this fragment
        View rootView = inflater.inflate(R.layout.fragment_notification_list, container, false);
        Button createNotificationButton = rootView.findViewById(R.id.buttonCreateNotificationNotificationList);
        createNotificationButton.setOnClickListener(this);


        RecyclerView notificationRecyclerView = rootView.findViewById(R.id.recyclerViewNotificationList);

        ArrayList<Notification> notificationArrayList = healthMetricsDbHelper.getAllNotifications();
        NotificationRecyclerViewAdapter adapter = new NotificationRecyclerViewAdapter(notificationArrayList,getActivity());
        notificationRecyclerView.setAdapter(adapter);
        notificationRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        return rootView;
    }

    @Override
    public void onClick(View v) {

        CreateNotificationFragment createNotificationFragment = new CreateNotificationFragment();
        getActivity().getSupportFragmentManager().beginTransaction()
                .replace(R.id.fragmentContainer, createNotificationFragment)
                .addToBackStack(null)
                .commit();
    }
}
