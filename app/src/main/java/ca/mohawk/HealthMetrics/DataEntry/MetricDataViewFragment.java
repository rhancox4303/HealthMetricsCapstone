package ca.mohawk.HealthMetrics.DataEntry;


import android.os.Bundle;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.components.AxisBase;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.formatter.IAxisValueFormatter;
import com.github.mikephil.charting.utils.EntryXComparator;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import ca.mohawk.HealthMetrics.Adapaters.DataEntryRecyclerViewAdapter;
import ca.mohawk.HealthMetrics.Adapaters.MetricRecyclerViewAdapter;
import ca.mohawk.HealthMetrics.DisplayObjects.DataEntryRecyclerViewObject;
import ca.mohawk.HealthMetrics.HealthMetricsDbHelper;
import ca.mohawk.HealthMetrics.MetricManagement.ManageMetricFragment;
import ca.mohawk.HealthMetrics.R;


/**
 * A simple {@link Fragment} subclass.
 */
public class MetricDataViewFragment extends Fragment implements View.OnClickListener {

    private List<DataEntryRecyclerViewObject> dataEntryRecyclerViewObjectList;
    HealthMetricsDbHelper healthMetricsDbHelper;

    int MetricId;
    LineChart chart;
    public MetricDataViewFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootView = inflater.inflate(R.layout.fragment_metric_data_view, container, false);
        setHasOptionsMenu(true);

        chart = (LineChart) rootView.findViewById(R.id.chartMetricDataView);
        healthMetricsDbHelper = HealthMetricsDbHelper.getInstance(getActivity());

        Bundle bundle = this.getArguments();
        if (bundle != null) {
            MetricId = bundle.getInt("selected_item_key", -1);
        }

        Button addMetric = rootView.findViewById(R.id.buttonAddEntryMetricDataView);
        addMetric.setOnClickListener(this);

        Button manageMetricButton = rootView.findViewById(R.id.buttonManageMetricMetricDataView);
        manageMetricButton.setOnClickListener(this);

        RecyclerView dataEntryRecylerView = (RecyclerView) rootView.findViewById(R.id.recyclerviewMetricDataView);

        dataEntryRecyclerViewObjectList = healthMetricsDbHelper.getDataEntriesByMetricId(MetricId);
        DataEntryRecyclerViewAdapter adapter = new DataEntryRecyclerViewAdapter(dataEntryRecyclerViewObjectList,getActivity());
        dataEntryRecylerView.setAdapter(adapter);
        dataEntryRecylerView.setLayoutManager(new LinearLayoutManager(getActivity()));

        setUpGraphView();
        return rootView;
    }

    public void setUpGraphView(){

        List<Entry> entries = new ArrayList<Entry>();
        final List <String> dates = new ArrayList<String>();

        for (DataEntryRecyclerViewObject dataEntry : dataEntryRecyclerViewObjectList) {
           Log.d("CHART",dataEntry.getDataEntry());
            // turn your data into Entry objects
            entries.add(new Entry(dataEntry.getDateOfEntry().getTime(), dataEntry.getNumericDataEntry()));
            dates.add(dataEntry.getDateOfEntryString());
        }

        XAxis xAxis = chart.getXAxis();

        xAxis.setDrawLabels(false);
        chart.getLegend().setEnabled(false);   // Hide the legend
        xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);
        xAxis.setDrawGridLines(false);

        Collections.sort(entries, new EntryXComparator());
        LineDataSet dataSet = new LineDataSet(entries, "Label"); // add entries to dataset

        LineData lineData = new LineData(dataSet);
        chart.setData(lineData);
        chart.invalidate(); // refresh
    }

    @Override
    public void onClick(View v) {
        Fragment destinationFragment = null;
        switch (v.getId()) {
            case R.id.buttonAddEntryMetricDataView:
                destinationFragment = new AddDataEntryFragment();
                break;
            case R.id.buttonManageMetricMetricDataView:
                destinationFragment = new ManageMetricFragment();
        }

        Bundle bundle = new Bundle();
        bundle.putInt("metric_id",MetricId);
        destinationFragment.setArguments(bundle);

        getActivity().getSupportFragmentManager().beginTransaction()
                .replace(R.id.fragmentContainer, destinationFragment)
                .addToBackStack(null)
                .commit();
    }
    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.email_menu, menu);
        super.onCreateOptionsMenu(menu, inflater);
    }
    
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        Fragment destinationFragment = new EmailShareFragment();
        Bundle bundle = new Bundle();
        bundle.putInt("metric_id",MetricId);
        destinationFragment.setArguments(bundle);

        getActivity().getSupportFragmentManager().beginTransaction()
                .replace(R.id.fragmentContainer, destinationFragment)
                .addToBackStack(null)
                .commit();

        return  super.onOptionsItemSelected(item);
    }
}
