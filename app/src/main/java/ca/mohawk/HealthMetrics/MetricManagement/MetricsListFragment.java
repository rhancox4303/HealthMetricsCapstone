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
import ca.mohawk.HealthMetrics.MainActivity;
import ca.mohawk.HealthMetrics.R;
import ca.mohawk.HealthMetrics.UserProfile.ViewProfileFragment;

/**
 * A simple {@link Fragment} subclass.
 */
public class MetricsListFragment extends Fragment implements View.OnClickListener{

    private List<MetricRecyclerViewObject> addedMetricsList;
    HealthMetricsDbHelper healthMetricsDbHelper;

    public MetricsListFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        ((MainActivity)getActivity()).getSupportActionBar().show();

        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_metrics_view, container, false);
        healthMetricsDbHelper = HealthMetricsDbHelper.getInstance(getActivity());

        Button addMetricButton =  view.findViewById(R.id.buttonAddMetricViewMetric);
        addMetricButton.setOnClickListener(this);

        // Lookup the recyclerview in activity layout
        RecyclerView metricRecylerView = (RecyclerView) view.findViewById(R.id.recyclerViewMetrics);

        addedMetricsList = healthMetricsDbHelper.getAddedMetricsAndGalleries();
        MetricRecyclerViewAdapter adapter = new MetricRecyclerViewAdapter(addedMetricsList,getActivity());
        metricRecylerView.setAdapter(adapter);
        metricRecylerView.setLayoutManager(new LinearLayoutManager(getActivity()));

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
