package ca.mohawk.HealthMetrics.DataEntry;


import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import java.util.Objects;

import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.Fragment;
import ca.mohawk.HealthMetrics.HealthMetricsDbHelper;
import ca.mohawk.HealthMetrics.MetricManagement.MetricsListFragment;
import ca.mohawk.HealthMetrics.Models.DataEntry;
import ca.mohawk.HealthMetrics.Models.Metric;
import ca.mohawk.HealthMetrics.Models.Unit;
import ca.mohawk.HealthMetrics.R;

/**
 * The ViewDataEntryFragment class is an extension of the Fragment class.
 * Allows the user to view a data latestDataEntry.
 */
public class ViewDataEntryFragment extends Fragment implements View.OnClickListener {

    // Instantiate the dataEntryId variable.
    private int dataEntryId;

    // Instantiate the metricId variable.
    private int metricId;

    public ViewDataEntryFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        // Inflate the layout for this fragment.
        View rootView = inflater.inflate(R.layout.fragment_view_data_entry, container,
                false);

        // Get the views.
        TextView metricNameTextView = rootView.findViewById(R.id.textViewMetricDisplayViewDataEntry);
        TextView dataEntryTextView = rootView.findViewById(R.id.textViewDataDisplayViewDataEntry);
        TextView dateOfEntryTextView = rootView.findViewById(R.id.textViewDateOfEntryViewDataEntry);

        Button editDataEntryButton = rootView.findViewById(R.id.buttonEditEntryViewDataEntry);
        Button deleteDataEntryButton = rootView.findViewById(R.id.buttonDeleteEntryViewDataEntry);

        // Set the editDataEntryButton OnClickListener.
        editDataEntryButton.setOnClickListener(this);

        // Set the deleteDataEntryButton OnClickListener.
        deleteDataEntryButton.setOnClickListener(this);

        // Get the healthMetricsDbHelper.
        HealthMetricsDbHelper healthMetricsDbHelper = HealthMetricsDbHelper.getInstance(getActivity());

        // Get the data latestDataEntry id from the passed bundle.
        Bundle bundle = this.getArguments();
        if (bundle != null) {
            dataEntryId = bundle.getInt("data_entry_selected_key", -1);
        }

        // Get the data from the database.
        DataEntry dataEntry = healthMetricsDbHelper.getDataEntryById(dataEntryId);

        String dataEntryString = "";

        // Validate the data latestDataEntry is not null.
        if (dataEntry == null) {
            // Inform the user of the error and call the navigateToMetricsListFragment method.
            Toast.makeText(getActivity(), "Cannot load data latestDataEntry from database.",
                    Toast.LENGTH_SHORT).show();
            navigateToMetricsListFragment();

        } else {
            // Display the date of latestDataEntry and the data latestDataEntry.
            dateOfEntryTextView.setText(dataEntry.DateOfEntry);

            // Set the dataEntryString.
            dataEntryString = dataEntry.DataEntry;

            // Set the metricId.
            metricId = dataEntry.MetricId;
        }

        // Get the metric from the database.
        Metric metric = healthMetricsDbHelper.getMetricById(metricId);

        Unit unit = null;

        // Validate the metric is not null.
        if (metric == null) {
            // Inform the user of the error and call the navigateToMetricsListFragment method.
            Toast.makeText(getActivity(), "Cannot load metric from database.",
                    Toast.LENGTH_SHORT).show();
            navigateToMetricsListFragment();
        } else {

            // Display the metric name.
            metricNameTextView.setText(metric.Name);

            // Get the unit from the database.
            unit = healthMetricsDbHelper.getUnitById(metric.UnitId);
        }

        // Validate the unit is not null.
        if (unit == null) {
            // Inform the user of the error and call the navigateToMetricsListFragment method.
            Toast.makeText(getActivity(), "Cannot load unit from database.",
                    Toast.LENGTH_SHORT).show();
            navigateToMetricsListFragment();
        } else {
            // Display the unit abbreviation.
            dataEntryTextView.setText(new StringBuilder().append(dataEntryString)
                    .append(" ").append(unit.UnitAbbreviation).toString());
        }

        // Return rootView.
        return rootView;
    }

    /**
     * Replaces the current fragment with a MetricsListFragment.
     */
    private void navigateToMetricsListFragment() {

        MetricsListFragment destinationFragment = new MetricsListFragment();

        Objects.requireNonNull(getActivity()).getSupportFragmentManager().beginTransaction()
                .replace(R.id.fragmentContainer, destinationFragment)
                .addToBackStack(null)
                .commit();
    }

    /**
     * Runs when a view's onClickListener is activated.
     *
     * @param v Represents the view.
     */
    @Override
    public void onClick(View v) {

        // If the view id buttonEditEntryViewDataEntry then navigate to a EditDataEntryFragment.
        if (v.getId() == R.id.buttonEditEntryViewDataEntry) {

            // Create a new EditDataEntryFragment Fragment.
            Fragment destinationFragment = new EditDataEntryFragment();

            // Create a bundle and set the data latestDataEntry id.
            Bundle bundle = new Bundle();
            bundle.putInt("data_entry_selected_key", dataEntryId);

            // Set the bundle to the destinationFragment fragment.
            destinationFragment.setArguments(bundle);

            // Replace the current fragment with the destinationFragment.
            Objects.requireNonNull(getActivity()).getSupportFragmentManager().beginTransaction()
                    .replace(R.id.fragmentContainer, destinationFragment)
                    .addToBackStack(null)
                    .commit();

            // If the view id is buttonDeleteEntryViewDataEntry then display the TimePickerFragment
        } else if (v.getId() == R.id.buttonDeleteEntryViewDataEntry) {

            DialogFragment deleteDataEntryDialog =
                    DeleteDataEntryDialog.newInstance(dataEntryId, metricId);

            deleteDataEntryDialog.show(Objects.requireNonNull(getFragmentManager()),
                    "DeleteDataEntryDialog");
        }
    }
}