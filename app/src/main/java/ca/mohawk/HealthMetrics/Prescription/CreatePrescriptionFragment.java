package ca.mohawk.HealthMetrics.Prescription;


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
import ca.mohawk.HealthMetrics.Models.DosageMeasurement;
import ca.mohawk.HealthMetrics.Models.Prescription;
import ca.mohawk.HealthMetrics.Models.UnitCategory;
import ca.mohawk.HealthMetrics.R;


/**
 * A simple {@link Fragment} subclass.
 */
public class CreatePrescriptionFragment extends Fragment implements View.OnClickListener, AdapterView.OnItemSelectedListener  {

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

    private Spinner dosageMeasurementSpinner;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        healthMetricsDbHelper = healthMetricsDbHelper.getInstance(getActivity());
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

        dosageMeasurementSpinner = rootView.findViewById(R.id.spinnerDosageMeasurementCreatePrescription);
        ArrayAdapter<DosageMeasurement> dosageMeasurementArrayAdapter = new ArrayAdapter<>(getActivity(),android.R.layout.simple_spinner_item, dosageMeasurementList);
        dosageMeasurementSpinner.setAdapter(dosageMeasurementArrayAdapter);
        dosageMeasurementSpinner.setOnItemSelectedListener(this);

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
        if (doseEditText.getText().toString().trim().length() == 0) {
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

    public void createPrescription() {

        String name = nameEditText.getText().toString();
        String form = formEditText.getText().toString();
        String strength= strengthEditText.getText().toString();
        String dose = doseEditText.getText().toString();
        String frequency = frequencyEditText.getText().toString();
        double amount = Double.parseDouble(amountEditText.getText().toString());
        String reason = reasonEditText.getText().toString();

        Prescription newPrescription = new Prescription(dosageMeasurementId,name,form,strength,dose,frequency,amount,reason);
        healthMetricsDbHelper.addPrescription(newPrescription);
    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.buttonCreatePrescription && validateUserInput()) {

            createPrescription();
            PrescriptionListFragment prescriptionListFragment = new PrescriptionListFragment();

            getActivity().getSupportFragmentManager().beginTransaction()
                    .replace(R.id.fragmentContainer, prescriptionListFragment)
                    .addToBackStack(null)
                    .commit();
        }
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        dosageMeasurementId  = ((DosageMeasurement)parent.getSelectedItem()).getId();
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }
}
