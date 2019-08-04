package ca.mohawk.HealthMetrics.Prescription;


import android.os.Bundle;
import androidx.fragment.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import ca.mohawk.HealthMetrics.HealthMetricsDbHelper;
import ca.mohawk.HealthMetrics.R;


/**
 * A simple {@link Fragment} subclass.
 */
public class ViewPrescriptionFragment extends Fragment {

    private HealthMetricsDbHelper healthMetricsDbHelper;
    private int PrescriptionId;

    public ViewPrescriptionFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        healthMetricsDbHelper = HealthMetricsDbHelper.getInstance(getActivity());

        Bundle bundle = this.getArguments();
        if (bundle != null) {
            PrescriptionId = bundle.getInt("prescription_selected_key", -1);
        }
        // Inflate the layout for this fragment
        View rootView = inflater.inflate(R.layout.fragment_view_prescription, container, false);

        TextView nameTextView = rootView.findViewById(R.id.textViewNameViewPrescription);
        TextView formTextView = rootView.findViewById(R.id.textViewFormViewPrescription);
        TextView strengthTextView = rootView.findViewById(R.id.textViewStrengthViewPrescription);
        TextView dosageTextView = rootView.findViewById(R.id.textViewDoseageViewPrescription);
        TextView frequencyTextView = rootView.findViewById(R.id.textViewFrequencyViewPrescription);
        TextView amountTextView = rootView.findViewById(R.id.textViewAmountViewPrescription);
        TextView reasomTextView = rootView.findViewById(R.id.textViewReasonViewPrescription);

        return rootView;
    }

}
