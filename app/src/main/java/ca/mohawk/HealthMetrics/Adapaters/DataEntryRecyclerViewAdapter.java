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
import ca.mohawk.HealthMetrics.DataEntry.ViewDataEntryFragment;
import ca.mohawk.HealthMetrics.DisplayObjects.DataEntryRecyclerViewObject;
import ca.mohawk.HealthMetrics.MainActivity;
import ca.mohawk.HealthMetrics.R;

/**
 * Acts as a custom adapter for the data entry objects
 * in the data entry list recycler view.
 */
public class DataEntryRecyclerViewAdapter extends
        RecyclerView.Adapter<DataEntryRecyclerViewAdapter.ViewHolder> {

    // Instantiate the context variable.
    private Context context;

    // Instantiate the list of DataEntryRecyclerViewObjects to use in the adapter.
    private List<DataEntryRecyclerViewObject> dataEntryRecyclerViewObjects;

    /**
     * Constructs the DataEntryRecyclerViewAdapter.
     *
     * @param dataEntryRecyclerViewObjects Represents the list of DataEntryRecyclerViewObjects.
     * @param context                      Represents the application context.
     */
    public DataEntryRecyclerViewAdapter(List<DataEntryRecyclerViewObject> dataEntryRecyclerViewObjects, Context context) {
        this.dataEntryRecyclerViewObjects = dataEntryRecyclerViewObjects;
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
    public DataEntryRecyclerViewAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        // Get the context.
        Context context = parent.getContext();

        // Inflate the the view.
        LayoutInflater inflater = LayoutInflater.from(context);
        View contactView = inflater.inflate(R.layout.data_entry_recyclerview_layout, parent, false);

        // Return the View Holder.
        return new ViewHolder(contactView);
    }

    /**
     * Sets the item views in the view holder.
     *
     * @param viewHolder Represents the view holder.
     * @param position   Represents the position of the DataEntryRecyclerViewObject that is being displayed.
     */
    @Override
    public void onBindViewHolder(DataEntryRecyclerViewAdapter.ViewHolder viewHolder, int position) {

        // Get DataEntryRecyclerViewObject object.
        final DataEntryRecyclerViewObject dataEntry = dataEntryRecyclerViewObjects.get(position);

        // Display the data entry in the recycler view.
        TextView textViewDataEntry = viewHolder.textViewDataEntry;
        textViewDataEntry.setText(dataEntry.getDataEntry());

        // Display the date of entry in the recycler view.
        TextView textViewDateOfEntry = viewHolder.textViewDateOfEntry;
        textViewDateOfEntry.setText(dataEntry.getDateOfEntryString());

        // Set the setOnClickListener for the item view.
        viewHolder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                // Call the switchFragment method with dataEntry passed in.
                switchFragment(dataEntry);
            }
        });
    }

    /**
     * Creates the new fragment and calls switch fragment on the main activity.
     *
     * @param selectedDataEntry Represents the selected data entry.
     */
    private void switchFragment(DataEntryRecyclerViewObject selectedDataEntry) {

        // Create the destination fragment.
        Fragment destinationFragment = new ViewDataEntryFragment();

        // Create and set the data entry id to a bundle.
        Bundle dataEntryBundle = new Bundle();
        dataEntryBundle.putInt("data_entry_selected_key", selectedDataEntry.Id);

        // Set the bundle to the destination fragment.
        destinationFragment.setArguments(dataEntryBundle);

        // If the context is an instance of MainActivity then call switchFragment.
        if (context instanceof MainActivity) {
            MainActivity mainActivity = (MainActivity) context;
            mainActivity.switchFragment(destinationFragment);
        }
    }

    // Get the size of dataEntryRecyclerViewObjects.
    @Override
    public int getItemCount() {
        return dataEntryRecyclerViewObjects.size();
    }

    /**
     * Defines the view of a row inside the recycler view.
     */
    class ViewHolder extends RecyclerView.ViewHolder {

        // Initialize the data entry and the date of entry text views.
        TextView textViewDataEntry;
        TextView textViewDateOfEntry;

        ViewHolder(View itemView) {
            super(itemView);

            // Get the text views from the data entry recycler view layout.
            textViewDataEntry = itemView.findViewById(R.id.textViewDataEntryRecyclerView);
            textViewDateOfEntry = itemView.findViewById(R.id.textViewDateOfEntryRecyclerView);
        }
    }
}
