package ca.mohawk.HealthMetrics.MetricManagement;


import android.os.Bundle;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.Toast;

import java.util.List;

import ca.mohawk.HealthMetrics.HealthMetricsDbHelper;
import ca.mohawk.HealthMetrics.R;
import ca.mohawk.HealthMetrics.DisplayObjects.MetricSpinnerObject;
import ca.mohawk.HealthMetrics.DisplayObjects.UnitSpinnerObject;


/**
 * A simple {@link Fragment} subclass.
 */
public class AddMetricFragment extends Fragment implements View.OnClickListener, AdapterView.OnItemSelectedListener{

    HealthMetricsDbHelper healthMetricsDbHelper;
    public AddMetricFragment() {
        // Required empty public constructor
    }

    Spinner unitSpinner;
    private int UnitId;
    private int MetricId;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_add_metric, container, false);

        healthMetricsDbHelper = HealthMetricsDbHelper.getInstance(getActivity());
        Button createMetricButton =  view.findViewById(R.id.buttonCreateMetricAddMetric);
        createMetricButton.setOnClickListener(this);

        Button addMetricButton =  view.findViewById(R.id.buttonAddMetric);
        addMetricButton.setOnClickListener(this);

        unitSpinner = (Spinner) view.findViewById(R.id.spinnerUnitAddMetric);
        unitSpinner.setOnItemSelectedListener(this);

        Spinner metricSpinner = (Spinner) view.findViewById(R.id.spinnerMetricAddMetric);
        metricSpinner.setOnItemSelectedListener(this);

        List<MetricSpinnerObject> metrics = healthMetricsDbHelper.getAllMetrics();

        ArrayAdapter<MetricSpinnerObject> metricSpinnerObjectArrayAdapter = new ArrayAdapter<MetricSpinnerObject>(view.getContext(), android.R.layout.simple_spinner_item, metrics);
        metricSpinnerObjectArrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        metricSpinner.setAdapter(metricSpinnerObjectArrayAdapter);


        return view;
    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.buttonCreateMetricAddMetric){
            CreateMetricFragment createMetricFragment= new CreateMetricFragment();
            getActivity().getSupportFragmentManager().beginTransaction()
                    .replace(R.id.fragmentContainer, createMetricFragment)
                    .addToBackStack(null)
                    .commit();
        }else if (v.getId() == R.id.buttonAddMetric){
            int updateStatus = healthMetricsDbHelper.addMetricToProfile(UnitId,MetricId);
           if(updateStatus != 1){
               Toast.makeText(getActivity(), "Error when adding metric", Toast.LENGTH_SHORT).show();
           }else{
               Toast.makeText(getActivity(), "Metric added successfully", Toast.LENGTH_SHORT).show();

               MetricsViewFragment metricsViewFragment= new MetricsViewFragment();
               getActivity().getSupportFragmentManager().beginTransaction()
                       .replace(R.id.fragmentContainer, metricsViewFragment)
                       .addToBackStack(null)
                       .commit();
           }
        }
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        if (parent.getId() == R.id.spinnerMetricAddMetric) {
            String unitCategory = ((MetricSpinnerObject)parent.getSelectedItem()).getUnitCategory();
            MetricId = ((MetricSpinnerObject)parent.getSelectedItem()).getMetridId();
            LoadUnitSpinner(unitCategory,view);
        } else if (parent.getId() == R.id.spinnerUnitAddMetric){
            UnitId = ((UnitSpinnerObject)parent.getSelectedItem()).getUnitId();
            Log.d("UNITID","TEST" + UnitId);
        }
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }
    public void LoadUnitSpinner(String unitCategory, View view){

        unitSpinner.setAdapter(null);

        List<UnitSpinnerObject> units = healthMetricsDbHelper.getAllSpinnerUnits(unitCategory);
        ArrayAdapter<UnitSpinnerObject> unitSpinnerObjectArrayAdapter = new ArrayAdapter<UnitSpinnerObject>(getActivity().getBaseContext(), android.R.layout.simple_spinner_item, units);
        unitSpinnerObjectArrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        unitSpinner.setAdapter(unitSpinnerObjectArrayAdapter);
    }
}
