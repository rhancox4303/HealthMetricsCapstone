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
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.TextView;

import java.util.List;

import ca.mohawk.HealthMetrics.HealthMetricsDbHelper;
import ca.mohawk.HealthMetrics.Models.Metric;
import ca.mohawk.HealthMetrics.Models.PhotoGallery;
import ca.mohawk.HealthMetrics.Models.UnitCategory;
import ca.mohawk.HealthMetrics.R;


/**
 * A simple {@link Fragment} subclass.
 */
public class CreateMetricFragment extends Fragment implements RadioGroup.OnCheckedChangeListener {

    private RadioGroup metricTypeRadioGroup;

    public CreateMetricFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootView = inflater.inflate(R.layout.fragment_create_metric, container, false);

        CreateMetricInputFragment addMetricFragment = new CreateMetricInputFragment();
        getActivity().getSupportFragmentManager().beginTransaction()
                .replace(R.id.CreateMetricFragmentContainer, addMetricFragment)
                .addToBackStack(null)
                .commit();

        metricTypeRadioGroup = rootView.findViewById(R.id.radioGroupMetricTypeCreateMetric);
        metricTypeRadioGroup.setOnCheckedChangeListener(this);
        return rootView;
    }

    @Override
    public void onCheckedChanged(RadioGroup group, int checkedId) {

        switch (checkedId) {
            case R.id.radioButtonQuantitativeCreateMetric:
                CreateMetricInputFragment addMetricFragment = new CreateMetricInputFragment();
                getActivity().getSupportFragmentManager().beginTransaction()
                        .replace(R.id.CreateMetricFragmentContainer, addMetricFragment)
                        .addToBackStack(null)
                        .commit();
                break;
            case R.id.radioButtonGalleryCreateMetric:
                CreatePhotoGalleryInputFragment addGalleryFragment = new CreatePhotoGalleryInputFragment();
                getActivity().getSupportFragmentManager().beginTransaction()
                        .replace(R.id.CreateMetricFragmentContainer, addGalleryFragment)
                        .addToBackStack(null)
                        .commit();
                break;
            case R.id.radioButtonNoteCreateMetric:
                CreateNoteInputFragment noteInputFragment = new CreateNoteInputFragment();
                getActivity().getSupportFragmentManager().beginTransaction()
                        .replace(R.id.CreateMetricFragmentContainer, noteInputFragment)
                        .addToBackStack(null)
                        .commit();
        }
    }
}
