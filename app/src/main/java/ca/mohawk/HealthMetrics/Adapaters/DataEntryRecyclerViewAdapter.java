package ca.mohawk.HealthMetrics.Adapaters;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;
import ca.mohawk.HealthMetrics.DataEntry.MetricDataViewFragment;
import ca.mohawk.HealthMetrics.DataEntry.ViewDataEntryFragment;
import ca.mohawk.HealthMetrics.DisplayObjects.DataEntryRecyclerViewObject;
import ca.mohawk.HealthMetrics.DisplayObjects.MetricRecyclerViewObject;
import ca.mohawk.HealthMetrics.MainActivity;
import ca.mohawk.HealthMetrics.PhotoGallery.ViewPhotoGalleryFragment;
import ca.mohawk.HealthMetrics.R;

public class DataEntryRecyclerViewAdapter extends
        RecyclerView.Adapter<DataEntryRecyclerViewAdapter.ViewHolder> {

    private Context Context;
    private List<DataEntryRecyclerViewObject> DataEntryRecyclerViewObjects ;

    public DataEntryRecyclerViewAdapter(List<DataEntryRecyclerViewObject> dataEntryRecyclerViewObjects, Context context) {
        DataEntryRecyclerViewObjects= dataEntryRecyclerViewObjects;
        Context = context;
    }

    /**
     * The ViewHolder class describes the item view and
     * metadata about it's place within the RecyclerView.
     */
    public class ViewHolder extends RecyclerView.ViewHolder {

        public TextView textViewDataEntry;
        public TextView textViewDateOfEntry;

        public ViewHolder(View itemView) {
            super(itemView);

            textViewDataEntry = (TextView) itemView.findViewById(R.id.textViewDataEntryRecyclerView);
            textViewDateOfEntry = (TextView) itemView.findViewById(R.id.textViewDateOfEntryRecyclerView);
        }
    }

    /**
     * The onCreateViewHolder method is used to inflate
     * the custom layout and create the viewholder.
     */
    @Override
    public DataEntryRecyclerViewAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        Context context = parent.getContext();
        LayoutInflater inflater = LayoutInflater.from(context);

        View contactView = inflater.inflate(R.layout.data_entry_recyclerview_layout, parent, false);

        DataEntryRecyclerViewAdapter.ViewHolder viewHolder = new DataEntryRecyclerViewAdapter.ViewHolder(contactView);
        return viewHolder;
    }

    /**
     * The onBindViewHolder method is used to set the item views in the recycler
     * view using the metricRecyclerViewObjectList and the view holder.
     */
    @Override
    public void onBindViewHolder(DataEntryRecyclerViewAdapter.ViewHolder viewHolder, int position) {
        //Get data object
        final DataEntryRecyclerViewObject dataEntry = DataEntryRecyclerViewObjects.get(position);

        // Set item views
        TextView textViewDataEntry = viewHolder.textViewDataEntry;
        textViewDataEntry.setText(dataEntry.getDataEntry());

        TextView textViewDateOfEntry = viewHolder.textViewDateOfEntry;
        textViewDateOfEntry.setText(dataEntry.getDateOfEntry());

        viewHolder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                changeFragment(dataEntry);
            }
        });
    }
    // Returns the total count of items in the list
    @Override
    public int getItemCount() {
        return DataEntryRecyclerViewObjects.size();
    }

    private void changeFragment(DataEntryRecyclerViewObject itemSelected) {

        Fragment fragment = new ViewDataEntryFragment();

        Bundle dataEntryBundle = new Bundle();
        dataEntryBundle.putInt("data_entry_selected_key", itemSelected.Id);

        fragment.setArguments(dataEntryBundle);
        switchContent(fragment);
    }

    public void switchContent(Fragment fragment) {
        if (Context == null)
            return;
        if (Context instanceof MainActivity) {
            MainActivity mainActivity = (MainActivity) Context;
            mainActivity.switchContent(fragment);
        }
    }
}
