package ca.mohawk.HealthMetrics.DataEntry;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.utils.EntryXComparator;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import ca.mohawk.HealthMetrics.Adapaters.DataEntryRecyclerViewAdapter;
import ca.mohawk.HealthMetrics.DisplayObjects.DataEntryDisplayObject;
import ca.mohawk.HealthMetrics.HealthMetricsDbHelper;
import ca.mohawk.HealthMetrics.MetricManagement.ManageMetricFragment;
import ca.mohawk.HealthMetrics.R;

/**
 * The CreateDataEntryFragment class is an extension of the Fragment class.
 * Displays all data entries for the specified metric.
 */
public class DataEntryListFragment extends Fragment implements View.OnClickListener {

    // Instantiate the list of dataEntryDisplayObjects.
    private List<DataEntryDisplayObject> dataEntryDisplayObjects;

    // Instantiate the metricId variable.
    private int metricId;

    // Instantiate the LineChart object.
    private LineChart chart;

    public DataEntryListFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        // Inflate the layout for this fragment.
        View rootView = inflater.inflate(R.layout.fragment_data_entry_list, container,
                false);

        // Turn on the options menu.
        setHasOptionsMenu(true);


        // Get the views.
        Button addMetricButton = rootView.findViewById(R.id.buttonAddEntryMetricDataView);
        Button manageMetricButton = rootView.findViewById(R.id.buttonManageMetricMetricDataView);
        RecyclerView dataEntryRecyclerView = rootView.findViewById(R.id.recyclerViewDataEntryList);


        addMetricButton.setOnClickListener(this);
        manageMetricButton.setOnClickListener(this);

        // Get the line chart.
        chart = rootView.findViewById(R.id.chartMetricDataView);

        // Get the healthMetricsDbHelper.
        HealthMetricsDbHelper healthMetricsDbHelper = HealthMetricsDbHelper.getInstance(getActivity());

        // Get the metric id from the passed bundle.
        Bundle bundle = this.getArguments();
        if (bundle != null) {
            metricId = bundle.getInt("selected_item_key", -1);
        }

        // Get the list of dataEntries from the database.
        dataEntryDisplayObjects = healthMetricsDbHelper.getDataEntriesByMetricId(metricId);

        // Instantiate the DataEntryRecyclerViewAdapter.
        DataEntryRecyclerViewAdapter adapter = new DataEntryRecyclerViewAdapter(
                dataEntryDisplayObjects, getActivity());

        // Set the dataEntryRecyclerView adapter.
        dataEntryRecyclerView.setAdapter(adapter);

        // Set the dataEntryRecyclerView layout manager.
        dataEntryRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));

        // Call the createGraphView method.
        createGraphView();

        // Return the rootView.
        return rootView;
    }

    /**
     * Creates the line graph using the list of data entries from the database.
     * This uses the MPAndroid chart library.
     */
    private void createGraphView() {

        // Instantiate the list of Entries.
        List<Entry> entries = new ArrayList<>();

        // For every data entry object in createGraphView.
        for (DataEntryDisplayObject dataEntry : dataEntryDisplayObjects) {

            // Create a entry with the numeric data entry data and the date and time of entry.
            entries.add(new Entry(dataEntry.getDateOfEntry().getTime(), dataEntry.getNumericData()));
        }

        // Get the XAxis and set the axis view.
        XAxis xAxis = chart.getXAxis();
        xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);
        xAxis.setDrawLabels(false);
        xAxis.setDrawGridLines(false);

        // Disable the legend and description.
        chart.getLegend().setEnabled(false);
        chart.getDescription().setEnabled(false);

        // Sort the entries.
        Collections.sort(entries, new EntryXComparator());
        LineDataSet dataSet = new LineDataSet(entries, "Data Entries");

        // Create a new line data set with the list of entries.
        LineData lineData = new LineData(dataSet);

        // Set the data.
        chart.setData(lineData);

        // Refresh the chart.
        chart.invalidate();
    }

    /**
     * Runs when a view's onClickListener is activated.
     *
     * @param v Represents the view.
     */
    @Override
    public void onClick(View v) {

        // Initialize the destination fragment
        Fragment destinationFragment = new Fragment();


        // If the id is buttonAddEntryMetricDataView assign destinationFragment to a
        // CreateDataEntry Fragment.
        if (v.getId() == R.id.buttonAddEntryMetricDataView) {
            destinationFragment = new CreateDataEntryFragment();

            // Else if the id is buttonManageMetricMetricDataView assign destinationFragment to a
            // ManageMetricFragment Fragment.
        } else if (v.getId() == R.id.buttonManageMetricMetricDataView) {
            destinationFragment = new ManageMetricFragment();
        }

        // Create a bundle and set the metric id.
        Bundle bundle = new Bundle();
        bundle.putInt("metric_id_key", metricId);

        // Set the bundle to the destinationFragment fragment.
        destinationFragment.setArguments(bundle);

        // Replace the current fragment with the destinationFragment.
        Objects.requireNonNull(getActivity()).getSupportFragmentManager().beginTransaction()
                .replace(R.id.fragmentContainer, destinationFragment)
                .addToBackStack(null)
                .commit();
    }

    /**
     * Creates the Options menu.
     *
     * @param menu     Represents the menu.
     * @param inflater Represents the MenuInflater.
     */
    @Override
    public void onCreateOptionsMenu(@NonNull Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.email_menu, menu);
        super.onCreateOptionsMenu(menu, inflater);
    }


    /**
     * Calls the onOptionsItemSelected method when the menu item is pressed.
     *
     * @param item Represents the MenuItem.
     * @return Returns the results of super.onOptionsItemSelected.
     */
    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {

        // Create a EmailShareFragment.
        Fragment emailShareFragment = new EmailShareFragment();

        // Create a bundle and set the metric id.
        Bundle bundle = new Bundle();
        bundle.putInt("metric_id_key", metricId);
        emailShareFragment.setArguments(bundle);

        // Replace the current fragment with the emailShareFragment.
        Objects.requireNonNull(getActivity()).getSupportFragmentManager().beginTransaction()
                .replace(R.id.fragmentContainer, emailShareFragment)
                .addToBackStack(null)
                .commit();

        return super.onOptionsItemSelected(item);
    }
}