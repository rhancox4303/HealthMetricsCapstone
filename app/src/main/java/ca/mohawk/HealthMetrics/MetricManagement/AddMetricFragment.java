package ca.mohawk.HealthMetrics.MetricManagement;


import android.os.Bundle;
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
import java.util.Objects;

import androidx.fragment.app.Fragment;
import ca.mohawk.HealthMetrics.DisplayObjects.MetricSpinnerObject;
import ca.mohawk.HealthMetrics.DisplayObjects.PhotoGallerySpinnerObject;
import ca.mohawk.HealthMetrics.DisplayObjects.UnitSpinnerObject;
import ca.mohawk.HealthMetrics.HealthMetricsDbHelper;
import ca.mohawk.HealthMetrics.R;


/**
 * The AddMetric Fragment allows the user to add a metric to their health profile. The user
 * can also navigate to the create metric fragment.
 */
public class AddMetricFragment extends Fragment implements View.OnClickListener, AdapterView.OnItemSelectedListener, RadioGroup.OnCheckedChangeListener {

    // The HealthMetricsDbHelper healthMetricsDbHelper is used to access the SQLite database.
    private HealthMetricsDbHelper healthMetricsDbHelper;

    // Instantiate the unit and metric spinners.
    private Spinner unitSpinner;
    private Spinner metricSpinner;

    // Instantiate the selected Unit and Metric Ids to -1.
    private int selectedUnitId = -1;
    private int selectedMetricId = -1;

    // Instantiate the Unit and Metric text views.
    private TextView unitDisplayTextView;
    private TextView metricTypeDisplayTextView;

    // Instantiate the addMetricRadioGroup.
    private RadioGroup addMetricRadioGroup;

    // Instantiate the metric and photo gallery adapters.
    private ArrayAdapter<MetricSpinnerObject> metricSpinnerObjectArrayAdapter;
    private ArrayAdapter<PhotoGallerySpinnerObject> photoGallerySpinnerObjectArrayAdapter;

    public AddMetricFragment() {
        // Required empty public constructor
    }

    /**
     * The onCreateView method initializes the view variables
     * and the HealthMetricsDbHelper object when the Fragment view is created.
     */
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        // Inflate the layout for this fragment
        View rootView = inflater.inflate(R.layout.fragment_add_metric, container, false);

        // Initialize the healthMetricsDbHelper object.
        healthMetricsDbHelper = HealthMetricsDbHelper.getInstance(getActivity());

        // Initialize the rootView elements.
        addMetricRadioGroup = rootView.findViewById(R.id.radioGroupAddMetric);
        metricSpinner = rootView.findViewById(R.id.spinnerMetricAddMetric);
        metricTypeDisplayTextView = rootView.findViewById(R.id.textViewDisplayAddMetric);
        unitDisplayTextView = rootView.findViewById(R.id.textViewUnitAddMetric);
        unitSpinner = rootView.findViewById(R.id.spinnerUnitAddMetric);

        // Set the onItemSelectedListeners for the Metric and Unit spinners.
        metricSpinner.setOnItemSelectedListener(this);
        unitSpinner.setOnItemSelectedListener(this);

        // Initialize the add Metric and create metric buttons.
        Button addMetricButton = rootView.findViewById(R.id.buttonAddMetric);
        Button createMetricButton = rootView.findViewById(R.id.buttonCreateMetricAddMetric);

        // Set the OnClickListeners for the create metric and add metric buttons.
        createMetricButton.setOnClickListener(this);
        addMetricButton.setOnClickListener(this);

        // Set the OnCheckedChangeListener for the addMetricRadioGroup.
        addMetricRadioGroup.setOnCheckedChangeListener(this);

        // Get the lists of all metrics and galleries from the database.
        List<MetricSpinnerObject> metrics = healthMetricsDbHelper.getAllMetrics();
        List<PhotoGallerySpinnerObject> galleries = healthMetricsDbHelper.getAllPhotoGalleries();

        // Set the metric spinner array adapter.
        metricSpinnerObjectArrayAdapter = new ArrayAdapter<>(rootView.getContext(), android.R.layout.simple_spinner_item, metrics);
        metricSpinnerObjectArrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        // Set the photo gallery array adapter.
        photoGallerySpinnerObjectArrayAdapter = new ArrayAdapter<>(rootView.getContext(), android.R.layout.simple_spinner_item, galleries);
        photoGallerySpinnerObjectArrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        //Set the metric spinner adapter to the photoGallerySpinnerObjectArrayAdapter.
        metricSpinner.setAdapter(photoGallerySpinnerObjectArrayAdapter);

        // Return the rootView
        return rootView;
    }

    /**
     * The onClick method runs when the view's onClickListener is activated.
     */
    @Override
    public void onClick(View v) {

        // If the createMetric button is pressed then...
        if (v.getId() == R.id.buttonCreateMetricAddMetric) {

            // Initialize the CreateMetricFragment.
            CreateMetricFragment createMetricFragment = new CreateMetricFragment();

            // Navigate to the the CreateMetricFragment.
            Objects.requireNonNull(getActivity()).getSupportFragmentManager().beginTransaction()
                    .replace(R.id.fragmentContainer, createMetricFragment)
                    .addToBackStack(null)
                    .commit();

            // Else if the buttonAddMetric is pressed then call addMetricOrGallery
        } else if (v.getId() == R.id.buttonAddMetric) {
            addMetricOrGallery();
        }
    }

    /**
     * The addMetricOrGallery methods adds the user selected metric or gallery to their profile.
     */
    private void addMetricOrGallery() {

        // If validateUserInput returns true then continue.

        if (validateUserInput()) {
            // Initialize the updateStatus to false.
            boolean updateStatus = false;

            // If the radioButtonMetricAddMetric button is checked then add the metric to the user.
            if (addMetricRadioGroup.getCheckedRadioButtonId() == R.id.radioButtonMetricAddMetric) {

                updateStatus = healthMetricsDbHelper.addMetricToProfile(selectedUnitId, selectedMetricId);

                // Else then add the gallery to the user.
            } else {
                updateStatus = healthMetricsDbHelper.addGalleryToProfile(selectedMetricId);
            }

            // If the database update was not successful then inform the user.
            if (!updateStatus) {
                Toast.makeText(getActivity(), "Error when adding metric. ", Toast.LENGTH_SHORT).show();

                // Else...
            } else {

                // Inform the user.
                Toast.makeText(getActivity(), "Metric added successfully", Toast.LENGTH_SHORT).show();

                // Initialize the metricsListFragment and navigate to it.
                MetricsListFragment metricsListFragment = new MetricsListFragment();

                Objects.requireNonNull(getActivity()).getSupportFragmentManager().beginTransaction()
                        .replace(R.id.fragmentContainer, metricsListFragment)
                        .addToBackStack(null)
                        .commit();
            }
        }
    }

    /**
     * The onItemSelected method is called when a item in the spinner is selected.
     *
     * @param parent   The parent adapter view.
     * @param view     The selected view.
     * @param position The position of the selected item in the spinner.
     * @param id       The id.
     */
    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

        // If the parent is the spinnerMetricAddMetric and radioButtonMetricAddMetric is pressed.
        if (parent.getId() == R.id.spinnerMetricAddMetric &&
                addMetricRadioGroup.getCheckedRadioButtonId() == R.id.radioButtonMetricAddMetric) {

            // Get the unitCategoryId.
            int unitCategoryId = ((MetricSpinnerObject) parent.getSelectedItem()).getUnitCategoryId();

            // Get the selectedMetricId.
            selectedMetricId = ((MetricSpinnerObject) parent.getSelectedItem()).getMetricId();

            // Call the loadUnitSpinner method with the unitCategoryId passed in.
            loadUnitSpinner(unitCategoryId);

            // Else if the the parent is the spinnerMetricAddMetric
            // and the radioButtonGalleryAddMetric is checked.
        } else if (parent.getId() == R.id.spinnerMetricAddMetric &&
                addMetricRadioGroup.getCheckedRadioButtonId() == R.id.radioButtonGalleryAddMetric) {

            // Get the selectedMetricId.
            selectedMetricId = ((PhotoGallerySpinnerObject) parent.getSelectedItem()).getId();

            // Else if the parent is the spinnerUnitAddMetric.
        } else if (parent.getId() == R.id.spinnerUnitAddMetric) {

            // Get the selectedUnitId.
            selectedUnitId = ((UnitSpinnerObject) parent.getSelectedItem()).getUnitId();
        }
    }


    /**
     * The loadUnitSpinner method loads the Unit spinner with units with the specified unit category.
     *
     * @param unitCategoryId The unit category.
     */
    private void loadUnitSpinner(int unitCategoryId) {

        //Clear the unitSpinner.
        unitSpinner.setAdapter(null);

        //Get the list of Units from the database that have the unitCategoryId.
        List<UnitSpinnerObject> units = healthMetricsDbHelper.getAllSpinnerUnits(unitCategoryId);

        // Create the unitSpinnerObjectArrayAdapter.
        ArrayAdapter<UnitSpinnerObject> unitSpinnerObjectArrayAdapter = new ArrayAdapter<>
                (Objects.requireNonNull(getActivity()).getBaseContext(),
                        android.R.layout.simple_spinner_item, units);

        unitSpinnerObjectArrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        //Set the unit spinner adapter.
        unitSpinner.setAdapter(unitSpinnerObjectArrayAdapter);
    }

    /**
     * The onCheckedChanged method is called when the radio buttons are selected.
     *
     * @param group     The radio group.
     * @param checkedId The id of the checked button.
     */
    @Override
    public void onCheckedChanged(RadioGroup group, int checkedId) {

        // If the radioButtonGalleryAddMetric is checked then...
        if (checkedId == R.id.radioButtonGalleryAddMetric) {

            // Set the display text view text to gallery.
            metricTypeDisplayTextView.setText(R.string.gallery);

            // Clear the metricSpinner adapter.
            metricSpinner.setAdapter(null);

            // Set the photoGallerySpinnerObjectArrayAdapter.
            metricSpinner.setAdapter(photoGallerySpinnerObjectArrayAdapter);

            // Hide the unit views.
            unitSpinner.setVisibility(View.INVISIBLE);
            unitDisplayTextView.setVisibility(View.INVISIBLE);

            // else...
        } else {

            // Set the metricTypeDisplayTextView to metric.
            metricTypeDisplayTextView.setText(R.string.metric);

            // Clear the set adapter.
            metricSpinner.setAdapter(null);

            // Set the metricSpinnerObjectArrayAdapter adapter.
            metricSpinner.setAdapter(metricSpinnerObjectArrayAdapter);

            // Show the unit views.
            unitSpinner.setVisibility(View.VISIBLE);
            unitDisplayTextView.setVisibility(View.VISIBLE);
        }

        // Reset the unit and metric ids.
        selectedUnitId = -1;
        selectedMetricId = -1;
    }

    /**
     * The validateUserInput method validates the user's input.
     *
     * @return A boolean value is returned based on whether the user input is valid.
     */
    private boolean validateUserInput() {

        // If a metric or gallery has not been selected then inform the user and return false.
        if (selectedMetricId == -1) {
            Toast.makeText(getContext(), "Please select a gallery or metric. ", Toast.LENGTH_SHORT).show();
            return false;
        }

        // If the a unit is not selected and the Metric radio button is checked then
        // inform the user and return false.
        if ((selectedUnitId == -1) && addMetricRadioGroup.getCheckedRadioButtonId() == R.id.radioButtonMetricAddMetric) {
            Toast.makeText(getContext(), "Please select a unit and metric. ", Toast.LENGTH_SHORT).show();
            return false;
        }

        // Return true.
        return true;
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }
}