package ca.mohawk.HealthMetrics.MetricManagement;


import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RadioGroup;

import java.util.Objects;

import androidx.fragment.app.Fragment;
import ca.mohawk.HealthMetrics.R;


/**
 * The CreateMetricFragment class is an extension of the Fragment class.
 * <p>
 * Allows the user to create custom metrics, galleries and notes.
 */
public class CreateMetricFragment extends Fragment implements RadioGroup.OnCheckedChangeListener {

    public CreateMetricFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        // Inflate the layout for this fragment.
        View rootView = inflater.inflate(R.layout.fragment_create_metric, container, false);

        // Initialize a CreateMetricInputFragment object and display it to the user.
        CreateMetricInputFragment addMetricFragment = new CreateMetricInputFragment();

        Objects.requireNonNull(getActivity()).getSupportFragmentManager().beginTransaction()
                .replace(R.id.createMetricFragmentContainer, addMetricFragment)
                .addToBackStack(null)
                .commit();

        // Initialize the metricTypeRadioGroup.
        RadioGroup metricTypeRadioGroup = rootView.findViewById(R.id.radioGroupMetricTypeCreateMetric);

        // Set the metricTypeRadioGroup onCheckedChangeListener.
        metricTypeRadioGroup.setOnCheckedChangeListener(this);

        // Return the rootView.
        return rootView;
    }

    /**
     * Runs when the selected radio button is changed.
     *
     * @param group     The radio group.
     * @param checkedId The id of the checked button.
     */
    @Override
    public void onCheckedChanged(RadioGroup group, int checkedId) {

        // Switch statement uses the checkedId.
        switch (checkedId) {
            case R.id.radioButtonQuantitativeCreateMetric:

                // Initialize a CreateMetricInputFragment object and display it to the user.
                CreateMetricInputFragment addMetricFragment = new CreateMetricInputFragment();

                Objects.requireNonNull(getActivity()).getSupportFragmentManager().beginTransaction()
                        .replace(R.id.createMetricFragmentContainer, addMetricFragment)
                        .addToBackStack(null)
                        .commit();
                break;

            case R.id.radioButtonGalleryCreateMetric:

                // Initialize a CreatePhotoGalleryInputFragment object and display it to the user.
                CreatePhotoGalleryInputFragment addGalleryFragment = new CreatePhotoGalleryInputFragment();

                Objects.requireNonNull(getActivity()).getSupportFragmentManager().beginTransaction()
                        .replace(R.id.createMetricFragmentContainer, addGalleryFragment)
                        .addToBackStack(null)
                        .commit();
                break;

            case R.id.radioButtonNoteCreateMetric:

                // Initialize a CreateNoteInputFragment object and displays it to the user.
                CreateNoteInputFragment noteInputFragment = new CreateNoteInputFragment();

                Objects.requireNonNull(getActivity()).getSupportFragmentManager().beginTransaction()
                        .replace(R.id.createMetricFragmentContainer, noteInputFragment)
                        .addToBackStack(null)
                        .commit();
        }
    }
}
