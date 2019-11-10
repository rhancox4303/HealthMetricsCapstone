package ca.mohawk.HealthMetrics.Adapaters;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

import ca.mohawk.HealthMetrics.DataEntry.DataEntryListFragment;
import ca.mohawk.HealthMetrics.DisplayObjects.MetricDisplayObject;
import ca.mohawk.HealthMetrics.MainActivity;
import ca.mohawk.HealthMetrics.Note.ViewNoteFragment;
import ca.mohawk.HealthMetrics.PhotoGallery.PhotoEntryList;
import ca.mohawk.HealthMetrics.R;

/**
 * Acts as a custom adapter to display
 * the user added metrics in the metrics recycler view.
 */
public class MetricRecyclerViewAdapter extends
        RecyclerView.Adapter<MetricRecyclerViewAdapter.ViewHolder> {

    // Instantiate the context variable.
    private Context context;

    // Instantiate the list of MetricDisplayObjects to use in the adapter.
    private List<MetricDisplayObject> metricDisplayObjects;


    /**
     * Creates the adapter.
     *
     * @param metricDisplayObjects Represents the list of metricDisplayObjects.
     * @param context              Represents the application context.
     */
    public MetricRecyclerViewAdapter(List<MetricDisplayObject> metricDisplayObjects, Context context) {
        this.metricDisplayObjects = metricDisplayObjects;
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
    public MetricRecyclerViewAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        // Get the context.
        Context context = parent.getContext();

        // Inflate the view.
        LayoutInflater inflater = LayoutInflater.from(context);
        View contactView = inflater.inflate(R.layout.metrics_recyclerview_layout, parent, false);

        // Return the View Holder.
        return new ViewHolder(contactView);
    }

    /**
     * Sets the item views in the view holder.
     *
     * @param viewHolder Represents the view holder.
     * @param position   Represents the position of the metricDisplayObject that is being displayed.
     */
    @Override
    public void onBindViewHolder(MetricRecyclerViewAdapter.ViewHolder viewHolder, int position) {

        // Get MetricDisplayObject from the metricDisplayObjects list
        final MetricDisplayObject metric = metricDisplayObjects.get(position);

        // Display the metric name in the recycler view.
        TextView metricTextView = viewHolder.textViewMetricName;
        metricTextView.setText(metric.name);

        // Display the latest data latestDataEntry in the recycler view.
        TextView latestDataEntryTextView = viewHolder.latestMetricDataEntry;
        latestDataEntryTextView.setText(metric.getLatestDataEntry());

        // Set the itemView onCLickListener.
        viewHolder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                // Call the switchFragment method with the metrics passed in.
                switchFragment(metric);
            }
        });
    }

    /**
     * Creates and returns a fragment based on the selectedMetric's category.
     *
     * @param selectedMetric Represents the selected metric.
     * @return the created fragment is returned.
     */
    private Fragment getFragment(MetricDisplayObject selectedMetric) {

        // Instantiate the fragment.
        Fragment fragment = new Fragment();

        // Switch statement with the selected metric's category.
        switch (selectedMetric.category) {
            case "Quantitative":
                //Set fragment to a DataEntryListFragment.
                fragment = new DataEntryListFragment();
                break;
            case "Gallery":
                //Set fragment to a DataEntryListFragment.
                fragment = new PhotoEntryList();
                break;
            case "Note":
                //Set the fragment to a ViewNoteFragment.
                fragment = new ViewNoteFragment();
                break;
        }

        // Create and the metric id to a bundle.
        Bundle metricBundle = new Bundle();
        metricBundle.putInt("selected_item_key", selectedMetric.id);

        // Pass the bundle into the fragment.
        fragment.setArguments(metricBundle);

        // Return the fragment.
        return fragment;
    }

    /**
     * Gets the new fragment and calls switch fragment on the main activity.
     *
     * @param selectedMetric Represents the selected metric.
     */
    private void switchFragment(MetricDisplayObject selectedMetric) {

        // Get the fragment from the getFragment method with the selectedMetric passed in.
        Fragment destinationFragment = getFragment(selectedMetric);

        // If the context is an instance of MainActivity then call switchFragment.
        if (context instanceof MainActivity) {
            MainActivity mainActivity = (MainActivity) context;
            mainActivity.switchFragment(destinationFragment);
        }
    }

    // Get the size of metricDisplayObjects.
    @Override
    public int getItemCount() {
        return metricDisplayObjects.size();
    }

    /**
     * Defines the view of a row inside the recycler view.
     */
    class ViewHolder extends RecyclerView.ViewHolder {

        TextView textViewMetricName;
        TextView latestMetricDataEntry;

        ViewHolder(View itemView) {
            super(itemView);

            textViewMetricName = itemView.findViewById(R.id.textViewMetricName);
            latestMetricDataEntry = itemView.findViewById(R.id.textViewMetricDataEntry);
        }
    }
}
