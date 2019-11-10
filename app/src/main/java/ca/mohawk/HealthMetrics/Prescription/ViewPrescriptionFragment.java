package ca.mohawk.HealthMetrics.Prescription;


import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.Fragment;

import java.util.Objects;

import ca.mohawk.HealthMetrics.HealthMetricsDbHelper;
import ca.mohawk.HealthMetrics.Models.DosageMeasurement;
import ca.mohawk.HealthMetrics.Models.Prescription;
import ca.mohawk.HealthMetrics.R;


/**
 * The ViewPrescriptionFragment class extends the fragment class.
 * Allows the user to view a prescription.
 */
public class ViewPrescriptionFragment extends Fragment implements View.OnClickListener {

    // Initialize the prescription id.
    private int prescriptionId;

    public ViewPrescriptionFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootView = inflater.inflate(R.layout.fragment_view_prescription, container, false);

        // Get views.
        TextView nameTextView = rootView.findViewById(R.id.textViewNameViewPrescription);
        TextView formTextView = rootView.findViewById(R.id.textViewFormViewPrescription);
        TextView strengthTextView = rootView.findViewById(R.id.textViewStrengthViewPrescription);
        TextView dosageTextView = rootView.findViewById(R.id.textViewDoseageViewPrescription);
        TextView frequencyTextView = rootView.findViewById(R.id.textViewFrequencyViewPrescription);
        TextView amountTextView = rootView.findViewById(R.id.textViewAmountViewPrescription);
        TextView reasonTextView = rootView.findViewById(R.id.textViewReasonViewPrescription);

        Button editPrescriptionButton = rootView.findViewById(R.id.buttonEditPrescriptionViewPrescription);
        Button deletePrescriptionButton = rootView.findViewById(R.id.buttonDeletePrescriptionViewPrescription);

        editPrescriptionButton.setOnClickListener(this);
        deletePrescriptionButton.setOnClickListener(this);

        // Get healthMetricsDbHelper.
        HealthMetricsDbHelper healthMetricsDbHelper = HealthMetricsDbHelper.getInstance(getActivity());

        Bundle bundle = this.getArguments();
        if (bundle != null) {
            prescriptionId = bundle.getInt("prescription_selected_key", -1);
        }

        // Get the prescription from the database.
        Prescription prescription = healthMetricsDbHelper.getPrescriptionById(prescriptionId);

        // Validate prescription is not null.
        if (prescription != null) {
            DosageMeasurement dosageMeasurement = healthMetricsDbHelper.getDosageMeasurementById(prescription.dosageMeasurementId);
            if (dosageMeasurement != null) {
                nameTextView.setText(prescription.name);
                formTextView.setText(prescription.form);
                strengthTextView.setText(prescription.strength);

                dosageTextView.setText(new StringBuilder().append(prescription.dosageAmount)
                        .append(" ").append(dosageMeasurement.unitAbbreviation).toString());
                frequencyTextView.setText(prescription.frequency);

                amountTextView.setText(String.valueOf(prescription.amount));
                reasonTextView.setText(prescription.reason);
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

        // Return rootView.
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
     * Runs when the delete and edit prescription buttons are pressed.
     *
     * @param v Represents the view.
     */
    @Override
    public void onClick(View v) {

        if (v.getId() == R.id.buttonEditPrescriptionViewPrescription) {
            Fragment destinationFragment = new EditPrescriptionFragment();
            Bundle bundle = new Bundle();
            bundle.putInt("prescription_id", prescriptionId);
            destinationFragment.setArguments(bundle);

            Objects.requireNonNull(getActivity()).getSupportFragmentManager().beginTransaction()
                    .replace(R.id.fragmentContainer, destinationFragment)
                    .addToBackStack(null)
                    .commit();
        } else if (v.getId() == R.id.buttonDeletePrescriptionViewPrescription) {
            DialogFragment deletePrescriptionDialog = DeletePrescriptionDialog.newInstance(prescriptionId);
            deletePrescriptionDialog.show(Objects.requireNonNull(getFragmentManager()), "deletePrescriptionDialog");
        }
    }
}

