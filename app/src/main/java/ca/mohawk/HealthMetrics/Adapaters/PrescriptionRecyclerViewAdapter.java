package ca.mohawk.HealthMetrics.Adapaters;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;
import ca.mohawk.HealthMetrics.DisplayObjects.PrescriptionDisplayObject;
import ca.mohawk.HealthMetrics.HealthMetricsDbHelper;
import ca.mohawk.HealthMetrics.MainActivity;
import ca.mohawk.HealthMetrics.Models.Prescription;
import ca.mohawk.HealthMetrics.Prescription.ViewPrescriptionFragment;
import ca.mohawk.HealthMetrics.R;
import ca.mohawk.HealthMetrics.TimePickerFragment;


public class PrescriptionRecyclerViewAdapter
        extends RecyclerView.Adapter<PrescriptionRecyclerViewAdapter.ViewHolder> {

    private Context context;
    private HealthMetricsDbHelper healthMetricsDbHelper = HealthMetricsDbHelper.getInstance(context);

    //The list of prescription to be displayed in the recycler view.
    private List<PrescriptionDisplayObject> prescriptionList = new ArrayList<>();

    public PrescriptionRecyclerViewAdapter(List<PrescriptionDisplayObject> prescriptionList, Context context) {
        this.prescriptionList = prescriptionList;
        this.context = context;
    }

    class ViewHolder extends RecyclerView.ViewHolder {
        TextView prescriptionInformationTextView;
        TextView prescriptionAmountTextView;

        Button incrementAmountButton;
        Button decrementAmountButton;

        ViewHolder(View itemView) {
            super(itemView);

            prescriptionInformationTextView = (TextView) itemView.findViewById(R.id.textViewPrescriptionInformation);
            prescriptionAmountTextView = (TextView) itemView.findViewById(R.id.textViewPrescriptionAmount);
            incrementAmountButton = (Button) itemView.findViewById(R.id.buttonIncrementPrescriptionAmount);
            decrementAmountButton = (Button) itemView.findViewById(R.id.buttonDecrementPrescriptionAmount);
        }
    }

    /**
     * The onCreateViewHolder method is used to inflate
     * the custom layout and create the view holder.
     */
    @NonNull
    @Override
    public PrescriptionRecyclerViewAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        Context context = parent.getContext();
        LayoutInflater inflater = LayoutInflater.from(context);

        View contactView = inflater.inflate(R.layout.prescription_recyclerview_layout, parent, false);

        return new ViewHolder(contactView);
    }

    /**
     * The onBindViewHolder method is used to set the item views in the recycler
     * view using the metricRecyclerViewObjectList and the view holder.
     */
    @Override
    public void onBindViewHolder(final PrescriptionRecyclerViewAdapter.ViewHolder viewHolder, final int position) {

        //Get data object
        final PrescriptionDisplayObject prescriptionDisplayObject = prescriptionList.get(position);

        final String prescriptionInformation = prescriptionDisplayObject.getName();

        String frequency = "\n" + prescriptionDisplayObject.getDosageAmount() + " " + prescriptionDisplayObject.getDosageMeasurement() + " " + prescriptionDisplayObject.getFrequency();
        String amount = prescriptionDisplayObject.Amount + "\n" + prescriptionDisplayObject.DosageMeasurement;

        String information = prescriptionInformation + frequency;
        TextView informationTextView = viewHolder.prescriptionInformationTextView;
        TextView amountTextView = viewHolder.prescriptionAmountTextView;

        final Button incrementAmountButton = viewHolder.incrementAmountButton;
        Button decrementAmountButton = viewHolder.decrementAmountButton;

        amountTextView.setText(amount);
        informationTextView.setText(information);

        incrementAmountButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Prescription prescription = healthMetricsDbHelper.getPrescriptionById(prescriptionDisplayObject.Id);
                prescription.Amount += prescription.DosageAmount;
                healthMetricsDbHelper.updatePrescription(prescription);
                updatePrescriptionList();
            }
        });

        decrementAmountButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Prescription prescription = healthMetricsDbHelper.getPrescriptionById(prescriptionDisplayObject.Id);
                prescription.Amount -= prescription.DosageAmount;
                healthMetricsDbHelper.updatePrescription(prescription);
                updatePrescriptionList();
            }
        });

        viewHolder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                changeFragment(prescriptionDisplayObject);
            }
        });
    }

    private void updatePrescriptionList(){
        prescriptionList.clear();
        prescriptionList = healthMetricsDbHelper.getAllPrescriptions();
        notifyDataSetChanged();
    }

    private void changeFragment(PrescriptionDisplayObject itemSelected) {
        Fragment fragment = new ViewPrescriptionFragment();

        Bundle prescriptionBundle = new Bundle();
        prescriptionBundle.putInt("prescription_selected_key", itemSelected.Id);

        fragment.setArguments(prescriptionBundle);
        switchContent(fragment);
    }

    private void switchContent(Fragment fragment) {
        if (context == null)
            return;
        if (context instanceof MainActivity) {
            MainActivity mainActivity = (MainActivity) context;
            mainActivity.switchContent(fragment);
        }
    }

    // Returns the total count of items in the list
    @Override
    public int getItemCount() {
        return prescriptionList.size();
    }
}

