package ca.mohawk.HealthMetrics.Adapaters;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.Collections;
import java.util.List;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;
import ca.mohawk.HealthMetrics.DataEntry.MetricDataViewFragment;
import ca.mohawk.HealthMetrics.DisplayObjects.MetricRecyclerViewObject;
import ca.mohawk.HealthMetrics.MainActivity;
import ca.mohawk.HealthMetrics.Note.ViewNoteFragment;
import ca.mohawk.HealthMetrics.PhotoGallery.ViewPhotoGalleryFragment;
import ca.mohawk.HealthMetrics.R;

/**
 * The MetricRecyclerViewAdapter is a custom adapater to display
 * MetricRecyclerViewObject objects in the Metrics View Recycler View.
 */

public class MetricRecyclerViewAdapter extends
        RecyclerView.Adapter<MetricRecyclerViewAdapter.ViewHolder>
{
    private Context context;

    //The list of metrics to be displayed in the recycler view.
    private List<MetricRecyclerViewObject> metricRecyclerViewObjectList;
    
    public MetricRecyclerViewAdapter(List<MetricRecyclerViewObject> metricRecyclerViewObjectList, Context context) {
        this.metricRecyclerViewObjectList = metricRecyclerViewObjectList;
        this.context = context;
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
     * the custom layout and create the viewholder.
     */
    @Override
    public MetricRecyclerViewAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
    
        Context context = parent.getContext();
        LayoutInflater inflater = LayoutInflater.from(context);

        View contactView = inflater.inflate(R.layout.metrics_view_recyclerview_layout, parent, false);

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
        final MetricRecyclerViewObject metric = metricRecyclerViewObjectList.get(position);

        // Set item views
        TextView metricTextView = viewHolder.textViewMetricName;
        metricTextView.setText(metric.getName());
        TextView latestDataEntryTextView = viewHolder.latestMetricDataEntry;
        latestDataEntryTextView.setText(metric.getEntry());

        viewHolder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                changeFragment(metric);
            }
        });
    }

    // Returns the total count of items in the list
    @Override
    public int getItemCount() {
        return metricRecyclerViewObjectList.size();
    }

    private void changeFragment(MetricRecyclerViewObject itemSelected) {
        Fragment fragment = null;
        if(itemSelected.Category.equals("Quantitative")){
            fragment = new MetricDataViewFragment();
        }else if(itemSelected.Category.equals("Gallery")){
            fragment = new ViewPhotoGalleryFragment();
        }else if(itemSelected.Category.equals("Note")){
            fragment = new ViewNoteFragment();
        }

        Bundle metricBundle = new Bundle();
        metricBundle.putInt("selected_item_key", itemSelected.Id);

        fragment.setArguments(metricBundle);
        switchContent(fragment);
    }

    public void switchContent(Fragment fragment) {
        if (context == null)
            return;
        if (context instanceof MainActivity) {
            MainActivity mainActivity = (MainActivity) context;
            mainActivity.switchContent(fragment);
        }
    }
}
