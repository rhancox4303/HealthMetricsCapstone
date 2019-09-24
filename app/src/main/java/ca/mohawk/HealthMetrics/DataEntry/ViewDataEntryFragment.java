package ca.mohawk.HealthMetrics.DataEntry;


import android.os.Bundle;

import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import ca.mohawk.HealthMetrics.HealthMetricsDbHelper;
import ca.mohawk.HealthMetrics.Models.Metric;
import ca.mohawk.HealthMetrics.Models.DataEntry;
import ca.mohawk.HealthMetrics.Models.Unit;
import ca.mohawk.HealthMetrics.R;


/**
 * A simple {@link Fragment} subclass.
 */
public class ViewDataEntryFragment extends Fragment implements View.OnClickListener {


    public ViewDataEntryFragment() {
        // Required empty public constructor
    }

    HealthMetricsDbHelper healthMetricsDbHelper;
    private int DataEntryId;
    private int MetricId;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.fragment_view_data_entry, container, false);
        TextView metricNameTextView = rootView.findViewById(R.id.textViewMetricDisplayViewDataEntry);
        TextView dataEntryTextView = rootView.findViewById(R.id.textViewDataDisplayViewDataEntry);
        TextView dateOfEntryTextView = rootView.findViewById(R.id.textViewDateOfEntryViewDataEntry);

        Button editDataEntryButton = rootView.findViewById(R.id.buttonEditEntryViewDataEntry);
        editDataEntryButton.setOnClickListener(this);

        Button deleteDataEntryButton = rootView.findViewById(R.id.buttonDeleteEntryViewDataEntry);
        deleteDataEntryButton.setOnClickListener(this);

        healthMetricsDbHelper = HealthMetricsDbHelper.getInstance(getActivity());

        Bundle bundle = this.getArguments();
        if (bundle != null) {
            DataEntryId = bundle.getInt("data_entry_selected_key", -1);
        }

        DataEntry dataEntry = healthMetricsDbHelper.getDataEntryById(DataEntryId);
        MetricId = dataEntry.MetricId;
        Metric metric = healthMetricsDbHelper.getMetricById(MetricId);
        Unit unit = healthMetricsDbHelper.getUnitById(metric.UnitId);

        metricNameTextView.setText(metric.Name);
        dataEntryTextView.setText(dataEntry.DataEntry + " " + unit.UnitAbbreviation);
        dateOfEntryTextView.setText(dataEntry.DateOfEntry);
        return rootView;
    }

    @Override
    public void onClick(View v) {
        if(v.getId() == R.id.buttonEditEntryViewDataEntry){
            Bundle bundle = new Bundle();
            bundle.putInt("data_entry_selected_key",DataEntryId);
            Fragment fragment = new EditDataEntryFragment();
            fragment.setArguments(bundle);

            getActivity().getSupportFragmentManager().beginTransaction()
                    .replace(R.id.fragmentContainer, fragment)
                    .addToBackStack(null)
                    .commit();

        }else if(v.getId() == R.id.buttonDeleteEntryViewDataEntry){

            DialogFragment newFragment = DeleteDataEntryDialog.newInstance(DataEntryId,MetricId);
            newFragment.show(getFragmentManager(), "dialog");
        }
    }
}
