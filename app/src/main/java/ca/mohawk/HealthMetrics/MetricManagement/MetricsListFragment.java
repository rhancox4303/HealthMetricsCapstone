package ca.mohawk.HealthMetrics.MetricManagement;


import android.os.Bundle;
import androidx.fragment.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import java.util.List;
import java.util.Objects;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import ca.mohawk.HealthMetrics.Adapaters.MetricRecyclerViewAdapter;
import ca.mohawk.HealthMetrics.DisplayObjects.MetricDisplayObject;
import ca.mohawk.HealthMetrics.HealthMetricsDbHelper;
import ca.mohawk.HealthMetrics.MainActivity;
import ca.mohawk.HealthMetrics.R;

/**
 * The MetricsListFragment class is an extension of the Fragment class.
 * The Metrics List Fragment contains a recycler view of all metrics the user is currently tracking.
 */
public class MetricsListFragment extends Fragment implements View.OnClickListener{

    public MetricsListFragment() {
        // Required empty public constructor
    }

    /**
     * The onCreateView method initializes the rootView variables
     * and creates the metric RecyclerView when the fragment is created.
     */
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        // Show the action bar and set the title to "Health Metrics"
        Objects.requireNonNull(((MainActivity) Objects.requireNonNull(getActivity())).getSupportActionBar()).show();
        Objects.requireNonNull(((MainActivity) getActivity()).getSupportActionBar()).setTitle("Health Metrics");

        // Inflate the layout for this fragment.
        View rootView = inflater.inflate(R.layout.fragment_metrics_view, container, false);
        HealthMetricsDbHelper healthMetricsDbHelper = HealthMetricsDbHelper.getInstance(getActivity());

        //Instantiate the addMetricButton and set the OnClickListener.
        Button addMetricButton =  rootView.findViewById(R.id.buttonAddMetricViewMetric);
        addMetricButton.setOnClickListener(this);

        // Get the recycler view in the fragment layout.
        RecyclerView metricRecyclerView = rootView.findViewById(R.id.recyclerViewMetrics);

        // Get the list of added metrics from the database.
        List<MetricDisplayObject> addedMetricsList = healthMetricsDbHelper.getAddedMetricsAndGalleries();

        // Create a new MetricRecyclerViewAdapter and apply it to the metricRecyclerView.
        MetricRecyclerViewAdapter adapter = new MetricRecyclerViewAdapter(addedMetricsList,getActivity());
        metricRecyclerView.setAdapter(adapter);

        // Set a new LinearLayoutManager to the metricRecyclerView.
        metricRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));

        //Return the rootView.
        return rootView;
    }

    /**
     * The onClick method runs when the view's onClickListener is activated.
     * It runs the launches the AddMetricFragment when the addMetricButton is pressed.
     */
    @Override
    public void onClick(View v) {
        AddMetricFragment addMetricFragment= new AddMetricFragment();
        Objects.requireNonNull(getActivity()).getSupportFragmentManager().beginTransaction()
                .replace(R.id.fragmentContainer, addMetricFragment)
                .addToBackStack(null)
                .commit();
    }
}
