package ca.mohawk.HealthMetrics.Adapaters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;

import androidx.recyclerview.widget.RecyclerView;
import ca.mohawk.HealthMetrics.DisplayObjects.MetricRecyclerViewObject;
import ca.mohawk.HealthMetrics.R;

/**
 * The MetricRecyclerViewAdapter is a custom adapater to display
 * MetricRecyclerViewObject objects in the Metrics View Recycler View.
 */
public class MetricRecyclerViewAdapter extends
        RecyclerView.Adapter<MetricRecyclerViewAdapter.ViewHolder>
{
    //The list of mtrics to be displayed in the recycler view.
    private List<MetricRecyclerViewObject> metricRecyclerViewObjectList;
    
    public MetricRecyclerViewAdapter(List<MetricRecyclerViewObject> metricRecyclerViewObjectList) {
        this.metricRecyclerViewObjectList = metricRecyclerViewObjectList;
    }
    
    /**
    * The ViewHolder class describes the item view and 
    * metadata about it's place within the RecyclerView.
    */
    public class ViewHolder extends RecyclerView.ViewHolder {

        public TextView textViewMetricName;
        public TextView latestMetricDataEntry;

        public ViewHolder(View itemView) {
            super(itemView);

            textViewMetricName = (TextView) itemView.findViewById(R.id.textViewMetricName);
            latestMetricDataEntry = (TextView) itemView.findViewById(R.id.textViewMetricDataEntry);
        }
    }
    
     /**
     * The onCreateViewHolder method is used to inflate 
     * the custom layoutand create the viewholder.
     */
    @Override
    public MetricRecyclerViewAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
    
        Context context = parent.getContext();
        LayoutInflater inflater = LayoutInflater.from(context);

        View contactView = inflater.inflate(R.layout.fragment_metrics_custom_recyclerview_layout, parent, false);

        ViewHolder viewHolder = new ViewHolder(contactView);
        return viewHolder;
    }

     /**
     * The onBindViewHolder method is used to set the item views in the recycler
     * view using the metricRecyclerViewObjectList and the view holder.
     */
    @Override
    public void onBindViewHolder(MetricRecyclerViewAdapter.ViewHolder viewHolder, int position) {
        //Get data object
        MetricRecyclerViewObject metric = metricRecyclerViewObjectList.get(position);

        // Set item views
        TextView metricTextView = viewHolder.textViewMetricName;
        metricTextView.setText(metric.getMetricName());
        TextView latestDataEntryTextView = viewHolder.latestMetricDataEntry;
        latestDataEntryTextView.setText(metric.getLatestMetricDataEntry());
    }

    // Returns the total count of items in the list
    @Override
    public int getItemCount() {
        return metricRecyclerViewObjectList.size();
    }
}
