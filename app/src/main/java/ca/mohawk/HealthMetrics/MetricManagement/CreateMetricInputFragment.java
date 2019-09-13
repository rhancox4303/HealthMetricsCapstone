package ca.mohawk.HealthMetrics.MetricManagement;


import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import java.util.List;

import ca.mohawk.HealthMetrics.HealthMetricsDbHelper;
import ca.mohawk.HealthMetrics.Models.Metric;
import ca.mohawk.HealthMetrics.Models.UnitCategory;
import ca.mohawk.HealthMetrics.R;

/**
 * A simple {@link Fragment} subclass.
 */
public class CreateMetricInputFragment extends Fragment implements View.OnClickListener, AdapterView.OnItemSelectedListener {
    private int unitCategoryId;
    private HealthMetricsDbHelper healthMetricsDbHelper;
    private Spinner unitCategorySpinner;
    private EditText metricNameEditText;
    public CreateMetricInputFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View rootView =  inflater.inflate(R.layout.fragment_create_metric_input, container, false);

        healthMetricsDbHelper = HealthMetricsDbHelper.getInstance(getActivity());
        List<UnitCategory> unitCategoriesList = healthMetricsDbHelper.getAllUnitCategories();

        Button createMetricButton = rootView.findViewById(R.id.buttonCreateMetricInput);
        createMetricButton.setOnClickListener(this);
        metricNameEditText = rootView.findViewById(R.id.editTextMetricNameCreateMetricInput);

        Spinner unitCategorySpinner = rootView.findViewById(R.id.spinnerUnitCategoryCreateMetricInput);

        ArrayAdapter<UnitCategory> unitCategoryAdapater = new ArrayAdapter<>(getActivity(),android.R.layout.simple_spinner_item, unitCategoriesList);
        unitCategorySpinner.setAdapter(unitCategoryAdapater);
        unitCategorySpinner.setOnItemSelectedListener(this);

        return  rootView;
    }

    @Override
    public void onClick(View v) {
        if(!metricNameEditText.getText().toString().trim().equals(""))    {
            createMetric();

            AddMetricFragment addMetricFragment= new AddMetricFragment();
            getActivity().getSupportFragmentManager().beginTransaction()
                    .replace(R.id.fragmentContainer, addMetricFragment)
                    .addToBackStack(null)
                    .commit();
        }else{
            Toast.makeText(getActivity(), "Please enter all fields", Toast.LENGTH_SHORT).show();
        }

    }
    public void createMetric(){
        String metricName = metricNameEditText.getText().toString();
        healthMetricsDbHelper.addMetric(new Metric(0, metricName, unitCategoryId,0));
        Toast.makeText(getActivity(), "Metric created.", Toast.LENGTH_SHORT).show();
    }
    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        unitCategoryId = ((UnitCategory)parent.getSelectedItem()).getId();

    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }
}
