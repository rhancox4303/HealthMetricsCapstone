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

import java.util.List;
import java.util.Objects;

import androidx.fragment.app.Fragment;
import ca.mohawk.HealthMetrics.HealthMetricsDbHelper;
import ca.mohawk.HealthMetrics.Models.DosageMeasurement;
import ca.mohawk.HealthMetrics.Models.Prescription;
import ca.mohawk.HealthMetrics.R;


/**
 * A simple {@link Fragment} subclass.
 */
public class CreatePrescriptionFragment extends Fragment implements View.OnClickListener, AdapterView.OnItemSelectedListener {

    private HealthMetricsDbHelper healthMetricsDbHelper;
    private EditText nameEditText;
    private EditText formEditText;
    private EditText strengthEditText;
    private EditText doseEditText;
    private EditText frequencyEditText;
    private EditText amountEditText;
    private EditText reasonEditText;
    private int dosageMeasurementId;

    public CreatePrescriptionFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        healthMetricsDbHelper = HealthMetricsDbHelper.getInstance(getActivity());
        List<DosageMeasurement> dosageMeasurementList = healthMetricsDbHelper.getAllDosageMeasurements();

        // Inflate the layout for this fragment
        View rootView = inflater.inflate(R.layout.fragment_create_prescription, container, false);

        nameEditText = rootView.findViewById(R.id.editTextNameCreatePrescription);
        formEditText = rootView.findViewById(R.id.editTextFormCreatePrescription);
        strengthEditText = rootView.findViewById(R.id.editTextStrengthCreatePrescription);
        doseEditText = rootView.findViewById(R.id.editTextDoseCreatePrescription);
        frequencyEditText = rootView.findViewById(R.id.editTextFrequencyCreatePrescription);
        amountEditText = rootView.findViewById(R.id.editTextAmountCreatePrescription);
        reasonEditText = rootView.findViewById(R.id.editTextReasonCreatePrescription);

        Button createPrescriptionButton = rootView.findViewById(R.id.buttonCreatePrescription);
        createPrescriptionButton.setOnClickListener(this);

        Spinner dosageMeasurementSpinner = rootView.findViewById(R.id.spinnerDosageMeasurementCreatePrescription);
        ArrayAdapter<DosageMeasurement> dosageMeasurementArrayAdapter = new ArrayAdapter<>(Objects.requireNonNull(getActivity()), android.R.layout.simple_spinner_item, dosageMeasurementList);
        dosageMeasurementSpinner.setAdapter(dosageMeasurementArrayAdapter);
        dosageMeasurementSpinner.setOnItemSelectedListener(this);

        return rootView;
    }

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

    private boolean validateNameInput() {
        if (nameEditText.getText().toString().trim().equals("")) {
            Toast.makeText(getActivity(), "name cannot be empty.", Toast.LENGTH_SHORT).show();
            return false;
        }

        if (nameEditText.getText().toString().trim().length() > 25) {
            Toast.makeText(getActivity(), "Please enter a name 25 character or less.", Toast.LENGTH_SHORT).show();
            return false;
        }
        return true;
    }

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

    private boolean validateDosageInput() {
        if (doseEditText.getText().toString().trim().equals("")) {
            Toast.makeText(getActivity(), "Dosage cannot be empty.", Toast.LENGTH_SHORT).show();
            return false;
        }

        if (Double.parseDouble(doseEditText.getText().toString().trim()) <= 0) {
            Toast.makeText(getActivity(), "Please enter a dosage greater than 0.", Toast.LENGTH_SHORT).show();
            return false;
        }
        return true;
    }

    private boolean validateFrequencyInput() {
        if (frequencyEditText.getText().toString().trim().equals("")) {
            Toast.makeText(getActivity(), "frequency cannot be empty.", Toast.LENGTH_SHORT).show();
            return false;
        }

        if (frequencyEditText.getText().toString().trim().length() > 25) {
            Toast.makeText(getActivity(), "Please enter a frequency 25 character or less.", Toast.LENGTH_SHORT).show();
            return false;
        }
        return true;
    }

    private boolean validateAmountInput() {
        if (amountEditText.getText().toString().trim().equals("")) {
            Toast.makeText(getActivity(), "amount cannot be empty.", Toast.LENGTH_SHORT).show();
            return false;
        }

        if (Double.parseDouble(amountEditText.getText().toString().trim()) <= 0) {
            Toast.makeText(getActivity(), "Please enter an amount greater than 0.", Toast.LENGTH_SHORT).show();
            return false;
        }
        return true;
    }

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

    private void createPrescription() {

        String name = nameEditText.getText().toString();
        String form = formEditText.getText().toString();
        String strength = strengthEditText.getText().toString();
        double dose = Double.parseDouble(doseEditText.getText().toString());
        String frequency = frequencyEditText.getText().toString();
        double amount = Double.parseDouble(amountEditText.getText().toString());
        String reason = reasonEditText.getText().toString();

        Prescription newPrescription = new Prescription(dosageMeasurementId, name, form, strength, dose, frequency, amount, reason);

        if (healthMetricsDbHelper.addPrescription(newPrescription)) {
            PrescriptionListFragment prescriptionListFragment = new PrescriptionListFragment();

            Objects.requireNonNull(getActivity()).getSupportFragmentManager().beginTransaction()
                    .replace(R.id.fragmentContainer, prescriptionListFragment)
                    .addToBackStack(null)
                    .commit();
        }else{
            Toast.makeText(getActivity(), "Failed to add prescription to database.", Toast.LENGTH_SHORT).show();
        }
    }
    
    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.buttonCreatePrescription && validateUserInput()) {
            createPrescription();
        }
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        dosageMeasurementId = ((DosageMeasurement) parent.getSelectedItem()).getId();
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }
}
