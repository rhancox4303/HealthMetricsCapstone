package ca.mohawk.HealthMetrics.Adapaters;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;
import ca.mohawk.HealthMetrics.DataEntry.DataEntryListFragment;
import ca.mohawk.HealthMetrics.DisplayObjects.MetricDisplayObject;
import ca.mohawk.HealthMetrics.MainActivity;
import ca.mohawk.HealthMetrics.Note.ViewNoteFragment;
import ca.mohawk.HealthMetrics.PhotoGallery.ViewPhotoGalleryFragment;
import ca.mohawk.HealthMetrics.R;

/**
 * The MetricRecyclerViewAdapter is a custom adapter to display
 * MetricDisplayObject objects in the Metrics List Recycler View.
 */

public class MetricRecyclerViewAdapter extends
        RecyclerView.Adapter<MetricRecyclerViewAdapter.ViewHolder> {
    private Context context;

    //The list of metrics to be displayed in the recycler view.
    private List<MetricDisplayObject> metricDisplayObjectList;

    /**
     * The MetricRecyclerViewAdapter constructor instantiates the adapter.
     *
     * @param metricDisplayObjectList The metricDisplayObjectList is a list of metrics the user is tracking.
     * @param context                 The context contains the application context.
     */
    public MetricRecyclerViewAdapter(List<MetricDisplayObject> metricDisplayObjectList, Context context) {
        this.metricDisplayObjectList = metricDisplayObjectList;
        this.context = context;
    }

    /**
     * The onCreateViewHolder method inflates the view holder.
     *
     * @param parent The parent view group.
     * @param viewType The view type.
     *
     * @return The view holder is returned.
     */
    @NonNull
    @Override
    public MetricRecyclerViewAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        // Get the context.
        Context context = parent.getContext();

        // Inflate the layout.
        LayoutInflater inflater = LayoutInflater.from(context);

        // Inflate the view with the recycler view layout.
        View contactView = inflater.inflate(R.layout.metrics_view_recyclerview_layout, parent, false);

        // Return the View Holder.
        return new ViewHolder(contactView);
    }

    /**
     * The onBindViewHolder method binds the view holder.
     *
     * @param viewHolder The view holder.
     *
     * @param position The position of the metricDisplayObjectList object that is being displayed.
     */
    @Override
    public void onBindViewHolder(MetricRecyclerViewAdapter.ViewHolder viewHolder, int position) {

        // Get data object
        final MetricDisplayObject metric = metricDisplayObjectList.get(position);

        // Set item text views.
        TextView metricTextView = viewHolder.textViewMetricName;
        TextView latestDataEntryTextView = viewHolder.latestMetricDataEntry;

        // Set the metric's name and the latest data entry the user inserted.
        metricTextView.setText(metric.getName());
        latestDataEntryTextView.setText(metric.getEntry());

        // Set the itemView onCLickListener.
        viewHolder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                // Call the changeFragment method.
                changeFragment(metric);
            }
        });
    }

    /**
     * The getItemCount method gets the size of the metricDisplayObjectList.
     *
     * @return the size of the metricDisplayObjectList.
     */
    @Override
    public int getItemCount() {
        return metricDisplayObjectList.size();
    }

    /**
     * The changeFragment method loads a fragment based on the selected metric.
     *
     * @param selectedMetric is the selected MetricDisplayObject.
     */
    private void changeFragment(MetricDisplayObject selectedMetric) {

        // Instantiate the fragment.
        Fragment fragment = new Fragment();

        // Switch statement with the selected Metric's Category
        switch (selectedMetric.Category) {
            case "Quantitative":
                //Set fragment to a DataEntryListFragment.
                fragment = new DataEntryListFragment();
                break;
            case "Gallery":
                //Set fragment to a DataEntryListFragment.
                fragment = new ViewPhotoGalleryFragment();
                break;
            case "Note":
                //Set the fragment to a ViewNoteFragment.
                fragment = new ViewNoteFragment();
                break;
        }

        // Create a new Bundle object.
        Bundle metricBundle = new Bundle();

        // Put the selected metric id into the bundle.
        metricBundle.putInt("selected_item_key", selectedMetric.Id);

        // Pass the bundle into the fragment.
        fragment.setArguments(metricBundle);

        // Call the switchContent method with the fragment passed in.
        switchContent(fragment);
    }

    /**
     * The switchContent method instantiates a MainActivity object
     * and calls it's switchContent method.
     *
     * @param fragment The fragment that will be passed to switchContent.
     */
    private void switchContent(Fragment fragment) {

        // If the context is null then return.
        if (context == null)
            return;

        // If the context is an instance of MainActivity...
        if (context instanceof MainActivity) {
            // Instantiate a mainActivity object.
            MainActivity mainActivity = (MainActivity) context;

            // Call switchContent with fragment passed in.
            mainActivity.switchContent(fragment);
        }
    }

    /**
     * The ViewHolder class describes the item view and
     * metadata about it's place within the RecyclerView.
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
