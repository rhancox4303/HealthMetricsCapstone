package ca.mohawk.HealthMetrics.MetricManagement;


import android.os.Bundle;
import androidx.fragment.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import ca.mohawk.HealthMetrics.HealthMetricsDbHelper;
import ca.mohawk.HealthMetrics.Models.Metric;
import ca.mohawk.HealthMetrics.R;


/**
 * A simple {@link Fragment} subclass.
 */
public class EditMetricFragment extends Fragment implements View.OnClickListener {

    private int MetricId;
    private Metric Metric;
    private HealthMetricsDbHelper healthMetricsDbHelper;
    private EditText MetricNameEditText;

    public EditMetricFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        healthMetricsDbHelper = HealthMetricsDbHelper.getInstance(getActivity());

        // Inflate the layout for this fragment
        View rootView = inflater.inflate(R.layout.fragment_edit_metric, container, false);

        MetricNameEditText = rootView.findViewById(R.id.editTextMetricNameEditMetric);
        Button editMetric = rootView.findViewById(R.id.buttonEditMetric);

        editMetric.setOnClickListener(this);

        Bundle bundle = this.getArguments();
        if (bundle != null) {
            MetricId = bundle.getInt("metric_id_key", -1);
        }

        Metric = healthMetricsDbHelper.getMetricById(MetricId);
        MetricNameEditText.setText(Metric.Name);

        return rootView;
    }

    public Boolean validateUserInput(){
        if(MetricNameEditText.getText().toString().equals("")){
            Toast.makeText(getActivity(), "Please enter all fields.", Toast.LENGTH_SHORT).show();
            return false;
        }else{
            return true;
        }
    }

    public void editMetric(){
        String metricName = MetricNameEditText.getText().toString();
        Metric.Name = metricName;
        healthMetricsDbHelper.updateMetric(Metric);

    }

    @Override
    public void onClick(View v) {

        if(validateUserInput() && v.getId() == R.id.buttonEditMetric){
            editMetric();

            ManageMetricFragment manageMetricFragment = new ManageMetricFragment();

            Bundle metricBundle = new Bundle();
            metricBundle.putInt("metric_id_key", MetricId);
            manageMetricFragment.setArguments(metricBundle);

            getActivity().getSupportFragmentManager().beginTransaction()
                    .replace(R.id.fragmentContainer, manageMetricFragment)
                    .addToBackStack(null)
                    .commit();
        }
    }
}
