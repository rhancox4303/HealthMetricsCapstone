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
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import java.util.List;

import ca.mohawk.HealthMetrics.DisplayObjects.PhotoGallerySpinnerObject;
import ca.mohawk.HealthMetrics.HealthMetricsDbHelper;
import ca.mohawk.HealthMetrics.R;
import ca.mohawk.HealthMetrics.DisplayObjects.MetricSpinnerObject;
import ca.mohawk.HealthMetrics.DisplayObjects.UnitSpinnerObject;


/**
 * A simple {@link Fragment} subclass.
 */
public class AddMetricFragment extends Fragment implements View.OnClickListener, AdapterView.OnItemSelectedListener, RadioGroup.OnCheckedChangeListener {

    HealthMetricsDbHelper healthMetricsDbHelper;

    public AddMetricFragment() {
        // Required empty public constructor
    }

    private Spinner unitSpinner;
    private int UnitId;
    private int MetricId;
    private TextView unitDisplaytextView;
    private TextView textViewDisplay;
    private Spinner metricSpinner;
    RadioGroup addMetricRadioGroup;
    private ArrayAdapter<MetricSpinnerObject> metricSpinnerObjectArrayAdapter;
    private ArrayAdapter<PhotoGallerySpinnerObject> photoGallerySpinnerObjectArrayAdapter;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_add_metric, container, false);

        healthMetricsDbHelper = HealthMetricsDbHelper.getInstance(getActivity());

        addMetricRadioGroup = view.findViewById(R.id.radioGroupAddMetric);
        metricSpinner = view.findViewById(R.id.spinnerMetricAddMetric);
        textViewDisplay = view.findViewById(R.id.textViewDisplayAddMetric);
        unitDisplaytextView = view.findViewById(R.id.textViewUnitAddMetric);
        unitSpinner = view.findViewById(R.id.spinnerUnitAddMetric);

        metricSpinner.setOnItemSelectedListener(this);
        unitSpinner.setOnItemSelectedListener(this);


        Button addMetricButton = view.findViewById(R.id.buttonAddMetric);

        Button createMetricButton = view.findViewById(R.id.buttonCreateMetricAddMetric);

        createMetricButton.setOnClickListener(this);
        addMetricRadioGroup.setOnCheckedChangeListener(this);
        addMetricButton.setOnClickListener(this);


        List<MetricSpinnerObject> metrics = healthMetricsDbHelper.getAllMetrics();
        List<PhotoGallerySpinnerObject> galleries = healthMetricsDbHelper.getAllPhotoGalleries();

        metricSpinnerObjectArrayAdapter = new ArrayAdapter<MetricSpinnerObject>(view.getContext(), android.R.layout.simple_spinner_item, metrics);
        metricSpinnerObjectArrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        photoGallerySpinnerObjectArrayAdapter = new ArrayAdapter<PhotoGallerySpinnerObject>(view.getContext(), android.R.layout.simple_spinner_item, galleries);
        photoGallerySpinnerObjectArrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        metricSpinner.setAdapter(photoGallerySpinnerObjectArrayAdapter);
        return view;
    }

    @Override
    public void onClick(View v) {
        boolean isAddedToProfile = false;
        if (v.getId() == R.id.buttonCreateMetricAddMetric) {
            CreateMetricFragment createMetricFragment = new CreateMetricFragment();
            getActivity().getSupportFragmentManager().beginTransaction()
                    .replace(R.id.fragmentContainer, createMetricFragment)
                    .addToBackStack(null)
                    .commit();
        } else if (v.getId() == R.id.buttonAddMetric) {
            isAddedToProfile = addMetricOrGallery();
        }

        if(isAddedToProfile) {
            MetricsListFragment metricsListFragment = new MetricsListFragment();
            getActivity().getSupportFragmentManager().beginTransaction()
                    .replace(R.id.fragmentContainer, metricsListFragment)
                    .addToBackStack(null)
                    .commit();
        }
    }


    public boolean addMetricOrGallery() {
        boolean isAddedToProfile = false;
        int updateStatus;
        if (addMetricRadioGroup.getCheckedRadioButtonId() == R.id.radioButtonMetricAddMetric) {
            updateStatus = healthMetricsDbHelper.addMetricToProfile(UnitId, MetricId);
        } else {
            updateStatus = healthMetricsDbHelper.addGalleryToProfile(MetricId);
        }
        if (updateStatus != 1) {
            Toast.makeText(getActivity(), "Error when adding metric", Toast.LENGTH_SHORT).show();
        } else {
            isAddedToProfile = true;
            Toast.makeText(getActivity(), "Metric added successfully", Toast.LENGTH_SHORT).show();
        }
        return isAddedToProfile;
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        if (parent.getId() == R.id.spinnerMetricAddMetric && addMetricRadioGroup.getCheckedRadioButtonId() == R.id.radioButtonMetricAddMetric) {

            int unitCategoryId = ((MetricSpinnerObject) parent.getSelectedItem()).getUnitCategoryId();
            MetricId = ((MetricSpinnerObject) parent.getSelectedItem()).getMetridId();
            LoadUnitSpinner(unitCategoryId);

        } else if (parent.getId() == R.id.spinnerMetricAddMetric && addMetricRadioGroup.getCheckedRadioButtonId() == R.id.radioButtonGalleryAddMetric) {
            MetricId = ((PhotoGallerySpinnerObject) parent.getSelectedItem()).getId();
        } else if (parent.getId() == R.id.spinnerUnitAddMetric) {
            UnitId = ((UnitSpinnerObject) parent.getSelectedItem()).getUnitId();
            Log.d("UNITID", "TEST" + UnitId);
        }
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }

    public void LoadUnitSpinner(int unitCategoryId) {

        unitSpinner.setAdapter(null);

        List<UnitSpinnerObject> units = healthMetricsDbHelper.getAllSpinnerUnits(unitCategoryId);
        ArrayAdapter<UnitSpinnerObject> unitSpinnerObjectArrayAdapter = new ArrayAdapter<UnitSpinnerObject>(getActivity().getBaseContext(), android.R.layout.simple_spinner_item, units);
        unitSpinnerObjectArrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        unitSpinner.setAdapter(unitSpinnerObjectArrayAdapter);
    }

    @Override
    public void onCheckedChanged(RadioGroup group, int checkedId) {
        if (checkedId == R.id.radioButtonGalleryAddMetric) {
            textViewDisplay.setText("Gallery");
            metricSpinner.setAdapter(null);
            metricSpinner.setAdapter(photoGallerySpinnerObjectArrayAdapter);
            unitSpinner.setVisibility(View.INVISIBLE);
            unitDisplaytextView.setVisibility(View.INVISIBLE);
        } else {
            textViewDisplay.setText("Metric");
            metricSpinner.setAdapter(null);
            metricSpinner.setAdapter(metricSpinnerObjectArrayAdapter);
            unitSpinner.setVisibility(View.VISIBLE);
            unitDisplaytextView.setVisibility(View.VISIBLE);
        }
    }
}