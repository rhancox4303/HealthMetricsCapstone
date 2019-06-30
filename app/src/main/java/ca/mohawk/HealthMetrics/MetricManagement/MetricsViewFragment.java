package ca.mohawk.HealthMetrics.MetricManagement;


import android.os.Bundle;
import androidx.fragment.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import java.util.ArrayList;
import java.util.List;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import ca.mohawk.HealthMetrics.Adapaters.MetricRecyclerViewAdapter;
import ca.mohawk.HealthMetrics.DisplayObjects.MetricRecyclerViewObject;
import ca.mohawk.HealthMetrics.HealthMetricsDbHelper;
import ca.mohawk.HealthMetrics.R;
import ca.mohawk.HealthMetrics.UserProfile.ViewProfileFragment;

/**
 * A simple {@link Fragment} subclass.
 */
public class MetricsViewFragment extends Fragment implements View.OnClickListener{

    private List<MetricRecyclerViewObject> addedMetricsList;
    HealthMetricsDbHelper healthMetricsDbHelper;

    public MetricsViewFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_metrics_view, container, false);
        healthMetricsDbHelper = HealthMetricsDbHelper.getInstance(getActivity());

        Button addMetricButton =  view.findViewById(R.id.buttonAddMetricViewMetric);
        addMetricButton.setOnClickListener(this);

        // Lookup the recyclerview in activity layout
        RecyclerView metricRecylerView = (RecyclerView) view.findViewById(R.id.recyclerViewMetrics);

        // Initialize contacts
        addedMetricsList = healthMetricsDbHelper.getAddedMetricsAndGalleries();
        // Create adapter passing in the sample user data
        MetricRecyclerViewAdapter adapter = new MetricRecyclerViewAdapter(addedMetricsList);
        // Attach the adapter to the recyclerview to populate items
        metricRecylerView.setAdapter(adapter);
        // Set layout manager to position the items
        metricRecylerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        // That's all!
        return view;
    }

    @Override
    public void onClick(View v) {
        AddMetricFragment addMetricFragment= new AddMetricFragment();
        getActivity().getSupportFragmentManager().beginTransaction()
                .replace(R.id.fragmentContainer, addMetricFragment)
                .addToBackStack(null)
                .commit();
    }
}
