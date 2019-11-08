package ca.mohawk.HealthMetrics.Prescription;


import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import java.util.Objects;

import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.Fragment;
import ca.mohawk.HealthMetrics.HealthMetricsDbHelper;
import ca.mohawk.HealthMetrics.Models.DosageMeasurement;
import ca.mohawk.HealthMetrics.Models.Prescription;
import ca.mohawk.HealthMetrics.R;


/**
 * A simple {@link Fragment} subclass.
 */
public class ViewPrescriptionFragment extends Fragment implements View.OnClickListener {

    private int PrescriptionId;

    public ViewPrescriptionFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootView = inflater.inflate(R.layout.fragment_view_prescription, container, false);

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

        HealthMetricsDbHelper healthMetricsDbHelper = HealthMetricsDbHelper.getInstance(getActivity());

        Bundle bundle = this.getArguments();
        if (bundle != null) {
            PrescriptionId = bundle.getInt("prescription_selected_key", -1);
        }

        Prescription prescription = healthMetricsDbHelper.getPrescriptionById(PrescriptionId);
        DosageMeasurement dosageMeasurement = healthMetricsDbHelper.getDosageMeasurementById(prescription.dosageMeasurementId);

        if (null != prescription || null != dosageMeasurement) {
            nameTextView.setText(prescription.name);
            formTextView.setText(prescription.form);
            strengthTextView.setText(prescription.strength);

            dosageTextView.setText(new StringBuilder().append(prescription.dosageAmount)
                    .append(" ").append(dosageMeasurement.unitAbbreviation).toString());
            frequencyTextView.setText(prescription.frequency);

            amountTextView.setText(String.valueOf(prescription.amount));
            reasonTextView.setText(prescription.reason);
        } else {
            // Leave
        }
        return rootView;
    }

    @Override
    public void onClick(View v) {

        Fragment destinationFragment = new Fragment();

        if (v.getId() == R.id.buttonEditPrescriptionViewPrescription) {

            destinationFragment = new EditPrescriptionFragment();
            Bundle bundle = new Bundle();
            bundle.putInt("prescription_id", PrescriptionId);
            destinationFragment.setArguments(bundle);

            Objects.requireNonNull(getActivity()).getSupportFragmentManager().beginTransaction()
                    .replace(R.id.fragmentContainer, destinationFragment)
                    .addToBackStack(null)
                    .commit();
        } else if (v.getId() == R.id.buttonDeletePrescriptionViewPrescription) {
            showDeleteDialog();
        }
    }

    public void showDeleteDialog() {
        DialogFragment deletePrescriptionDialog = DeletePrescriptionDialog.newInstance(PrescriptionId);
        deletePrescriptionDialog.show(Objects.requireNonNull(getFragmentManager()), "deletePrescriptionDialog");
    }
}
