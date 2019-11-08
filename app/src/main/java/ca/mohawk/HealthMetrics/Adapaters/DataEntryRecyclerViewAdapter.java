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
import ca.mohawk.HealthMetrics.DisplayObjects.DataEntryDisplayObject;
import ca.mohawk.HealthMetrics.MainActivity;
import ca.mohawk.HealthMetrics.R;

/**
 * Acts as a custom adapter for the data latestDataEntry objects
 * in the data latestDataEntry list recycler view.
 */
public class DataEntryRecyclerViewAdapter extends
        RecyclerView.Adapter<DataEntryRecyclerViewAdapter.ViewHolder> {

    // Instantiate the context variable.
    private Context context;

    // Instantiate the list of DataEntryRecyclerViewObjects to use in the adapter.
    private List<DataEntryDisplayObject> dataEntryDisplayObjects;

    /**
     * Constructs the DataEntryRecyclerViewAdapter.
     *
     * @param dataEntryDisplayObjects Represents the list of DataEntryRecyclerViewObjects.
     * @param context                      Represents the application context.
     */
    public DataEntryRecyclerViewAdapter(List<DataEntryDisplayObject> dataEntryDisplayObjects, Context context) {
        this.dataEntryDisplayObjects = dataEntryDisplayObjects;
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
     * @param position   Represents the position of the DataEntryDisplayObject that is being displayed.
     */
    @Override
    public void onBindViewHolder(DataEntryRecyclerViewAdapter.ViewHolder viewHolder, int position) {

        // Get DataEntryDisplayObject object.
        final DataEntryDisplayObject dataEntry = dataEntryDisplayObjects.get(position);

        // Display the data latestDataEntry in the recycler view.
        TextView textViewDataEntry = viewHolder.textViewDataEntry;
        textViewDataEntry.setText(dataEntry.getData());

        // Display the date of latestDataEntry in the recycler view.
        TextView textViewDateOfEntry = viewHolder.textViewDateOfEntry;
        textViewDateOfEntry.setText(dataEntry.dateOfEntry);

        // Set the setOnClickListener for the item view.
        viewHolder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                // Call the switchFragment method with data passed in.
                switchFragment(dataEntry);
            }
        });
    }

    /**
     * Creates the new fragment and calls switch fragment on the main activity.
     *
     * @param selectedDataEntry Represents the selected data latestDataEntry.
     */
    private void switchFragment(DataEntryDisplayObject selectedDataEntry) {

        // Create the destination fragment.
        Fragment destinationFragment = new ViewDataEntryFragment();

        // Create and set the data latestDataEntry id to a bundle.
        Bundle dataEntryBundle = new Bundle();
        dataEntryBundle.putInt("data_entry_selected_key", selectedDataEntry.id);

        // Set the bundle to the destination fragment.
        destinationFragment.setArguments(dataEntryBundle);

        // If the context is an instance of MainActivity then call switchFragment.
        if (context instanceof MainActivity) {
            MainActivity mainActivity = (MainActivity) context;
            mainActivity.switchFragment(destinationFragment);
        }
    }

    // Get the size of dataEntryDisplayObjects.
    @Override
    public int getItemCount() {
        return dataEntryDisplayObjects.size();
    }

    /**
     * Defines the view of a row inside the recycler view.
     */
    class ViewHolder extends RecyclerView.ViewHolder {

        // Initialize the data latestDataEntry and the date of latestDataEntry text views.
        TextView textViewDataEntry;
        TextView textViewDateOfEntry;

        ViewHolder(View itemView) {
            super(itemView);

            // Get the text views from the data latestDataEntry recycler view layout.
            textViewDataEntry = itemView.findViewById(R.id.textViewDataEntryRecyclerView);
            textViewDateOfEntry = itemView.findViewById(R.id.textViewDateOfEntryRecyclerView);
        }
    }
}
