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

public class MetricRecyclerViewAdapter extends
        RecyclerView.Adapter<MetricRecyclerViewAdapter.ViewHolder>
{
    private List<MetricRecyclerViewObject> metricRecyclerViewObjectList;

    public MetricRecyclerViewAdapter(List<MetricRecyclerViewObject> metricRecyclerViewObjectList) {
        this.metricRecyclerViewObjectList = metricRecyclerViewObjectList;
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        public TextView textViewMetricName;
        public TextView latestMetricDataEntry;

        public ViewHolder(View itemView) {
            super(itemView);

            textViewMetricName = (TextView) itemView.findViewById(R.id.textViewMetricName);
            latestMetricDataEntry = (TextView) itemView.findViewById(R.id.textViewMetricDataEntry);
        }
    }

    @Override
    public MetricRecyclerViewAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        LayoutInflater inflater = LayoutInflater.from(context);

        // Inflate the custom layout
        View contactView = inflater.inflate(R.layout.fragment_metrics_custom_recyclerview_layout, parent, false);

        // Return a new holder instance
        ViewHolder viewHolder = new ViewHolder(contactView);
        return viewHolder;
    }

    // Involves populating data into the item through holder
    @Override
    public void onBindViewHolder(MetricRecyclerViewAdapter.ViewHolder viewHolder, int position) {
        // Get the data model based on position
        MetricRecyclerViewObject metric = metricRecyclerViewObjectList.get(position);

        // Set item views based on your views and data model
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
