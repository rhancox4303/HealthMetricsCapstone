package ca.mohawk.HealthMetrics.Prescription;


import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import java.util.List;
import java.util.Objects;

import ca.mohawk.HealthMetrics.HealthMetricsDbHelper;
import ca.mohawk.HealthMetrics.MetricManagement.MetricsListFragment;
import ca.mohawk.HealthMetrics.Models.DosageMeasurement;
import ca.mohawk.HealthMetrics.Models.Prescription;
import ca.mohawk.HealthMetrics.R;


/**
 * The EditPrescription Fragment extends the fragment class.
 * Allows the user to edit a prescription.
 */
public class EditPrescriptionFragment extends Fragment implements AdapterView.OnItemSelectedListener, View.OnClickListener {

    // Initialize the healthMetricsDbHelper.
    private HealthMetricsDbHelper healthMetricsDbHelper;

    // Initialize the prescription id.
    private int prescriptionId;

    // Initialize the dosageMeasurementId.
    private int dosageMeasurementId;

    // Initialize the edit texts.
    private EditText nameEditText;
    private EditText formEditText;
    private EditText strengthEditText;
    private EditText dosageEditText;
    private EditText frequencyEditText;
    private EditText amountEditText;
    private EditText reasonEditText;

    public EditPrescriptionFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        // Inflate the layout for this fragment
        View rootView = inflater.inflate(R.layout.fragment_edit_prescription, container, false);

        // Get views
        nameEditText = rootView.findViewById(R.id.editTextNameEditPrescription);
        formEditText = rootView.findViewById(R.id.editTextFormEditPrescription);
        strengthEditText = rootView.findViewById(R.id.editTextStrengthEditPrescription);
        dosageEditText = rootView.findViewById(R.id.editTextDoseEditPrescription);
        frequencyEditText = rootView.findViewById(R.id.editTextFrequencyEditPrescription);
        amountEditText = rootView.findViewById(R.id.editTextAmountEditPrescription);
        reasonEditText = rootView.findViewById(R.id.editTextReasonEditPrescription);

        Spinner dosageMeasurementSpinner = rootView.findViewById(R.id.spinnerDosageMeasurementEditPrescription);
        dosageMeasurementSpinner.setOnItemSelectedListener(this);

        Button editPrescriptionButton = rootView.findViewById(R.id.buttonEditPrescription);
        editPrescriptionButton.setOnClickListener(this);

        // Get the healthMetricsDbHelper.
        healthMetricsDbHelper = HealthMetricsDbHelper.getInstance(getActivity());

        // Get the prescription id from the bundle.
        Bundle bundle = this.getArguments();
        if (bundle != null) {
            prescriptionId = bundle.getInt("prescription_id", -1);
        }

        // Get the prescription from the database.
        Prescription prescription = healthMetricsDbHelper.getPrescriptionById(prescriptionId);

        // Validate the prescription is not null.
        if (prescription != null) {
            DosageMeasurement dosageMeasurement = healthMetricsDbHelper.getDosageMeasurementById(prescription.dosageMeasurementId);

            // Validate the dosageMeasurement is not null.
            if (dosageMeasurement != null) {

                // Get all dosage measurements from the database.
                List<DosageMeasurement> dosageMeasurements = healthMetricsDbHelper.getAllDosageMeasurements();
                formEditText.setText(prescription.form);

                strengthEditText.setText(prescription.strength);
                dosageEditText.setText(String.valueOf(prescription.dosageAmount));
                frequencyEditText.setText(prescription.frequency);
                amountEditText.setText(String.valueOf(prescription.amount));
                reasonEditText.setText(prescription.reason);
                nameEditText.setText(prescription.name);

                // Create and set the dosageMeasurementArrayAdapter.
                ArrayAdapter<DosageMeasurement> dosageMeasurementArrayAdapter =
                        new ArrayAdapter<>(Objects.requireNonNull(getActivity()),
                                android.R.layout.simple_spinner_item, dosageMeasurements);

                dosageMeasurementSpinner.setAdapter(dosageMeasurementArrayAdapter);
                int spinnerPosition = dosageMeasurementArrayAdapter.getPosition(dosageMeasurement);
                dosageMeasurementSpinner.setSelection(spinnerPosition);

            } else {
                Toast.makeText(getActivity(), "Cannot get dosage measurement from database.",
                        Toast.LENGTH_SHORT).show();
                navigateToPrescriptionListFragment();
            }
        } else {
            Toast.makeText(getActivity(), "Cannot get prescription from database.",
                    Toast.LENGTH_SHORT).show();
            navigateToPrescriptionListFragment();
        }

        return rootView;
    }


    /**
     * Replaces the current fragment with a PrescriptionListFragment.
     */
    private void navigateToPrescriptionListFragment() {

        PrescriptionListFragment destinationFragment = new PrescriptionListFragment();

        Objects.requireNonNull(getActivity()).getSupportFragmentManager().beginTransaction()
                .replace(R.id.fragmentContainer, destinationFragment)
                .addToBackStack(null)
                .commit();
    }

    /**
     * Validates the user inputs.
     *
     * @return Return a boolean based on whether the user input is valid.
     */
    public boolean validateUserInput() {
        if (!validateNameInput()) {
            return false;
        }
        if (!validateFormInput()) {
            return false;
        }
        if (!validateStrengthInput()) {
            return false;
        }
        if (!validateDosageInput()) {
            return false;
        }
        if (!validateFrequencyInput()) {
            return false;
        }
        if (!validateAmountInput()) {
            return false;
        }

        return validateReasonInput();
    }

    /**
     * Validates the user inputted form value.
     *
     * @return Return a boolean based on whether the user input is valid.
     */
    private boolean validateFormInput() {

        if (formEditText.getText().toString().trim().equals("")) {
            Toast.makeText(getActivity(), "Form cannot be empty.", Toast.LENGTH_SHORT).show();
            return false;
        }

        if (formEditText.getText().toString().trim().length() > 25) {
            Toast.makeText(getActivity(), "Please enter a form 25 character or less.", Toast.LENGTH_SHORT).show();
            return false;
        }
        return true;
    }

    /**
     * Validates the user inputted name value.
     *
     * @return Return a boolean based on whether the user input is valid.
     */
    private boolean validateNameInput() {
        if (nameEditText.getText().toString().trim().equals("")) {
            Toast.makeText(getActivity(), "Name cannot be empty.", Toast.LENGTH_SHORT).show();
            return false;
        }

        if (nameEditText.getText().toString().trim().length() > 25) {
            Toast.makeText(getActivity(), "Please enter a name 25 character or less.", Toast.LENGTH_SHORT).show();
            return false;
        }
        return true;
    }

    /**
     * Validates the user inputted strength value.
     *
     * @return Return a boolean based on whether the user input is valid.
     */
    private boolean validateStrengthInput() {
        if (strengthEditText.getText().toString().trim().equals("")) {
            Toast.makeText(getActivity(), "Strength cannot be empty.", Toast.LENGTH_SHORT).show();
            return false;
        }

        if (strengthEditText.getText().toString().trim().length() > 25) {
            Toast.makeText(getActivity(), "Please enter a strength 25 character or less.", Toast.LENGTH_SHORT).show();
            return false;
        }
        return true;
    }

    /**
     * Validates the user inputted dosage value.
     *
     * @return Return a boolean based on whether the user input is valid.
     */
    private boolean validateDosageInput() {
        if (dosageEditText.getText().toString().trim().equals("")) {
            Toast.makeText(getActivity(), "Dosage cannot be empty.", Toast.LENGTH_SHORT).show();
            return false;
        }

        if (Double.parseDouble(dosageEditText.getText().toString().trim()) <= 0) {
            Toast.makeText(getActivity(), "Please enter a dosage greater than 0.", Toast.LENGTH_SHORT).show();
            return false;
        }
        return true;
    }

    /**
     * Validates the user inputted frequency value.
     *
     * @return Return a boolean based on whether the user input is valid.
     */

    private boolean validateFrequencyInput() {
        if (frequencyEditText.getText().toString().trim().equals("")) {
            Toast.makeText(getActivity(), "Frequency cannot be empty.", Toast.LENGTH_SHORT).show();
            return false;
        }

        if (frequencyEditText.getText().toString().trim().length() > 25) {
            Toast.makeText(getActivity(), "Please enter a frequency 25 character or less.", Toast.LENGTH_SHORT).show();
            return false;
        }
        return true;
    }

    /**
     * Validates the user inputted amount value.
     *
     * @return Return a boolean based on whether the user input is valid.
     */
    private boolean validateAmountInput() {
        if (amountEditText.getText().toString().trim().equals("")) {
            Toast.makeText(getActivity(), "Amount cannot be empty.", Toast.LENGTH_SHORT).show();
            return false;
        }

        if (Double.parseDouble(amountEditText.getText().toString().trim()) <= 0) {
            Toast.makeText(getActivity(), "Please enter an amount greater than 0.", Toast.LENGTH_SHORT).show();
            return false;
        }
        return true;
    }

    /**
     * Validates the user inputted form value.
     *
     * @return Return a boolean based on whether the user input is valid.
     */
    private boolean validateReasonInput() {
        if (reasonEditText.getText().toString().trim().equals("")) {
            Toast.makeText(getActivity(), "Reason cannot be empty.", Toast.LENGTH_SHORT).show();
            return false;
        }

        if (reasonEditText.getText().toString().trim().length() > 25) {
            Toast.makeText(getActivity(), "Please enter a reason 25 character or less.", Toast.LENGTH_SHORT).show();
            return false;
        }
        return true;
    }

    /**
     * Updates a prescription in the database based on inputted values.
     */
    private void updatePrescription() {

        // Validate the user input.
        if (validateUserInput()) {

            String name = nameEditText.getText().toString();
            String form = formEditText.getText().toString();
            String strength = strengthEditText.getText().toString();
            double dose = Double.parseDouble(dosageEditText.getText().toString());
            String frequency = frequencyEditText.getText().toString();
            double amount = Double.parseDouble(amountEditText.getText().toString());
            String reason = reasonEditText.getText().toString();

            Prescription newPrescription = new Prescription(prescriptionId, dosageMeasurementId, name, form, strength, dose, frequency, amount, reason);

            // Validate the update was successful.
            if (healthMetricsDbHelper.updatePrescription(newPrescription)) {
                PrescriptionListFragment prescriptionListFragment = new PrescriptionListFragment();

                Objects.requireNonNull(getActivity()).getSupportFragmentManager().beginTransaction()
                        .replace(R.id.fragmentContainer, prescriptionListFragment)
                        .addToBackStack(null)
                        .commit();
            } else {
                Toast.makeText(getActivity(), "Failed to update prescription in database.", Toast.LENGTH_SHORT).show();
            }
        }
    }

    /**
     * Runs the edit prescription button is pressed.
     *
     * @param v Represents the view.
     */
    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.buttonEditPrescription) {
            updatePrescription();
        }
    }

    /**
     * Runs when a item in a spinner is selected.
     *
     * @param parent   The parent adapter view.
     * @param view     The selected view.
     * @param position The position of the selected item in the spinner.
     * @param id       The id.
     */
    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        dosageMeasurementId = ((DosageMeasurement) parent.getSelectedItem()).id;
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }
}

