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

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import ca.mohawk.HealthMetrics.HealthMetricsDbHelper;
import ca.mohawk.HealthMetrics.Models.DosageMeasurement;
import ca.mohawk.HealthMetrics.Models.Prescription;
import ca.mohawk.HealthMetrics.R;


/**
 * A simple {@link Fragment} subclass.
 */
public class EditPrescriptionFragment extends Fragment implements AdapterView.OnItemSelectedListener, View.OnClickListener {


    private HealthMetricsDbHelper healthMetricsDbHelper;
    private int PrescriptionId;
    private int DosageMeasurementId;
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

        healthMetricsDbHelper = HealthMetricsDbHelper.getInstance(getActivity());

        Bundle bundle = this.getArguments();
        if (bundle != null) {
            PrescriptionId = bundle.getInt("prescription_id", -1);
        }

        Prescription prescription = healthMetricsDbHelper.getPrescriptionById(PrescriptionId);
        DosageMeasurement dosageMeasurement = healthMetricsDbHelper.getDosageMeasurementById(prescription.DosageMeasurementId);

        List<DosageMeasurement> dosageMeasurementList = healthMetricsDbHelper.getAllDosageMeasurements();
        // Inflate the layout for this fragment
        View rootView = inflater.inflate(R.layout.fragment_edit_prescription, container, false);

        Button editPrescriptionButton = rootView.findViewById(R.id.buttonEditPrescription);
        editPrescriptionButton.setOnClickListener(this);

        nameEditText = rootView.findViewById(R.id.editTextNameEditPrescription);
        formEditText = rootView.findViewById(R.id.editTextFormEditPrescription);
        strengthEditText = rootView.findViewById(R.id.editTextStrengthEditPrescription);
        dosageEditText = rootView.findViewById(R.id.editTextDoseEditPrescription);
        frequencyEditText = rootView.findViewById(R.id.editTextFrequencyEditPrescription);
        amountEditText = rootView.findViewById(R.id.editTextAmountEditPrescription);
        reasonEditText = rootView.findViewById(R.id.editTextReasonEditPrescription);
        Spinner dosageMeasurementSpinner = rootView.findViewById(R.id.spinnerDosageMeasurementEditPrescription);

        nameEditText.setText(prescription.Name);
        formEditText.setText(prescription.Form);
        strengthEditText.setText(prescription.Strength);
        dosageEditText.setText(String.valueOf(prescription.DosageAmount));
        frequencyEditText.setText(prescription.Frequency);
        amountEditText.setText(String.valueOf(prescription.Amount));
        reasonEditText.setText(prescription.Reason);

        ArrayAdapter<DosageMeasurement> dosageMeasurementArrayAdapter = new ArrayAdapter<>(Objects.requireNonNull(getActivity()), android.R.layout.simple_spinner_item, dosageMeasurementList);
        dosageMeasurementSpinner.setAdapter(dosageMeasurementArrayAdapter);
        dosageMeasurementSpinner.setOnItemSelectedListener(this);

        int spinnerPosition = dosageMeasurementArrayAdapter.getPosition(dosageMeasurement);
        dosageMeasurementSpinner.setSelection(spinnerPosition);
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

    private void editPrescription() {

        String name = nameEditText.getText().toString();
        String form = formEditText.getText().toString();
        String strength = strengthEditText.getText().toString();
        double dose = Double.parseDouble(dosageEditText.getText().toString());
        String frequency = frequencyEditText.getText().toString();
        double amount = Double.parseDouble(amountEditText.getText().toString());
        String reason = reasonEditText.getText().toString();

        Prescription newPrescription = new Prescription(PrescriptionId, DosageMeasurementId, name, form, strength, dose, frequency, amount, reason);

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

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        DosageMeasurementId = ((DosageMeasurement) parent.getSelectedItem()).getId();
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.buttonEditPrescription && validateUserInput()) {
            editPrescription();
        }
    }
}

