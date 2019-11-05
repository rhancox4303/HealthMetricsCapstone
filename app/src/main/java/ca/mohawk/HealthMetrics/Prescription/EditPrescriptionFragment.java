package ca.mohawk.HealthMetrics.Prescription;


import android.os.Bundle;
import androidx.fragment.app.Fragment;

import android.util.Log;
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
    private Spinner dosageMeasurementSpinner;

    public EditPrescriptionFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        healthMetricsDbHelper = HealthMetricsDbHelper.getInstance(getActivity());

        Bundle bundle = this.getArguments();
        if (bundle != null) {
            PrescriptionId = bundle.getInt("prescription_id", -1);
        }

        Prescription prescription = healthMetricsDbHelper.getPrescriptionById(PrescriptionId);
        DosageMeasurement dosageMeasurement = healthMetricsDbHelper.getDosageMeasurementById(prescription.getDosageMeasurementId());

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
        dosageMeasurementSpinner = rootView.findViewById(R.id.spinnerDosageMeasurementEditPrescription);

        nameEditText.setText(prescription.getName());
        formEditText.setText(prescription.getForm());
        strengthEditText.setText(prescription.getStrength());
        dosageEditText.setText(String.valueOf(prescription.DosageAmount));
        frequencyEditText.setText(prescription.getFrequency());
        amountEditText.setText(String.valueOf(prescription.getAmount()));
        reasonEditText.setText(prescription.getReason());

        ArrayAdapter<DosageMeasurement> dosageMeasurementArrayAdapter = new ArrayAdapter<>(getActivity(),android.R.layout.simple_spinner_item, dosageMeasurementList);
        dosageMeasurementSpinner.setAdapter(dosageMeasurementArrayAdapter);
        dosageMeasurementSpinner.setOnItemSelectedListener(this);

        int spinnerPosition = dosageMeasurementArrayAdapter.getPosition(dosageMeasurement);
        dosageMeasurementSpinner.setSelection(spinnerPosition);
        return rootView;
    }

    public boolean validateUserInput() {

        boolean isValidInput = true;

        if (nameEditText.getText().toString().trim().length() == 0) {
            isValidInput = false;
        }
        if (formEditText.getText().toString().trim().length() == 0) {
            isValidInput = false;
        }
        if (strengthEditText.getText().toString().trim().length() == 0) {
            isValidInput = false;
        }
        if (dosageEditText.getText().toString().trim().length() == 0) {
            isValidInput = false;
        }
        if (frequencyEditText.getText().toString().trim().length() == 0) {
            isValidInput = false;
        }
        if (amountEditText.getText().toString().trim().length() == 0) {
            isValidInput = false;
        }
        if (reasonEditText.getText().toString().trim().length() == 0) {
            isValidInput = false;
        }

        if (!isValidInput) {
            Toast.makeText(getActivity(), "Please fill in all fields", Toast.LENGTH_SHORT).show();
        }

        return isValidInput;
    }

    public void editPrescription() {

        String name = nameEditText.getText().toString();
        String form = formEditText.getText().toString();
        String strength= strengthEditText.getText().toString();
        double dose = Double.parseDouble(dosageEditText.getText().toString());
        String frequency = frequencyEditText.getText().toString();
        double amount = Double.parseDouble(amountEditText.getText().toString());
        String reason = reasonEditText.getText().toString();

        Prescription newPrescription = new Prescription(PrescriptionId,DosageMeasurementId,name,form,strength,dose,frequency,amount,reason);
        healthMetricsDbHelper.updatePrescription(newPrescription);
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        DosageMeasurementId = ((DosageMeasurement)parent.getSelectedItem()).getId();
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.buttonEditPrescription && validateUserInput()) {

            editPrescription();
            PrescriptionListFragment prescriptionListFragment = new PrescriptionListFragment();

            getActivity().getSupportFragmentManager().beginTransaction()
                    .replace(R.id.fragmentContainer, prescriptionListFragment)
                    .addToBackStack(null)
                    .commit();
        }
    }
}

