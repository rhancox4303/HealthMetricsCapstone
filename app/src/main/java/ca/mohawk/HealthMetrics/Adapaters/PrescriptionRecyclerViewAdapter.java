package ca.mohawk.HealthMetrics.Adapaters;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

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

/**
 * Acts as a custom adapter to display
 * the prescriptions in the prescription list recycler view.
 */
public class PrescriptionRecyclerViewAdapter
        extends RecyclerView.Adapter<PrescriptionRecyclerViewAdapter.ViewHolder> {

    // Instantiate the context variable.
    private Context context;

    // Instantiate the HealthMetricsDbHelper variable.
    private HealthMetricsDbHelper healthMetricsDbHelper;

    // Instantiate the list of prescription display objects to use in the adapter.
    private List<PrescriptionDisplayObject> prescriptionDisplayObjects;

    /**
     * Creates the adapter.
     *
     * @param prescriptionDisplayObjects Represents the list of prescriptions.
     * @param context                    Represents the application context.
     */
    public PrescriptionRecyclerViewAdapter(List<PrescriptionDisplayObject> prescriptionDisplayObjects,
                                           Context context) {

        this.prescriptionDisplayObjects = prescriptionDisplayObjects;
        this.context = context;
        healthMetricsDbHelper = HealthMetricsDbHelper.getInstance(context);
    }

    /**
     * Creates the View Holder.
     *
     * @param parent   Represents the parent view group.
     * @param viewType Represents the view type.
     * @return A created view holder is returned.
     */
    @NonNull
    @Override
    public PrescriptionRecyclerViewAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        // Get the context.
        Context context = parent.getContext();

        // Inflate the view.
        LayoutInflater inflater = LayoutInflater.from(context);
        View contactView = inflater.inflate(R.layout.prescription_recyclerview_layout, parent, false);

        // Return the View Holder.
        return new ViewHolder(contactView);
    }

    /**
     * Sets the item views in the view holder.
     *
     * @param viewHolder Represents the view holder.
     * @param position   Represents the position of the prescription that is being displayed.
     */
    @Override
    public void onBindViewHolder(final PrescriptionRecyclerViewAdapter.ViewHolder viewHolder, final int position) {

        //Get PrescriptionDisplayObject.
        final PrescriptionDisplayObject prescriptionDisplayObject = prescriptionDisplayObjects.get(position);

        // Display the prescription information in the recycler view.
        TextView informationTextView = viewHolder.prescriptionInformationTextView;
        informationTextView.setText(prescriptionDisplayObject.getInformation());

        // Display the prescription amount in the recycler view.
        TextView amountTextView = viewHolder.prescriptionAmountTextView;
        amountTextView.setText(prescriptionDisplayObject.getAmount());

        // Get the increment and decrement amount buttons.
        Button incrementAmountButton = viewHolder.incrementAmountButton;
        Button decrementAmountButton = viewHolder.decrementAmountButton;

        // Set the incrementAmountButton OnClickListener.
        incrementAmountButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                // Get the prescription from the database.
                Prescription prescription = healthMetricsDbHelper.getPrescriptionById(prescriptionDisplayObject.id);

                // Increment the amount by the dosage amount.
                prescription.amount += prescription.dosageAmount;

                // If unable to update the database then notify the user.
                if (!healthMetricsDbHelper.updatePrescription(prescription)) {
                    Toast.makeText(context, "Unable to update amount.", Toast.LENGTH_SHORT).show();
                }

                // Call updateRecyclerView.
                updateRecyclerView();
            }
        });

        // Set the decrementAmountButton OnClickListener.
        decrementAmountButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Get the prescription from the database.
                Prescription prescription = healthMetricsDbHelper.getPrescriptionById(prescriptionDisplayObject.id);

                // Decrement the amount by the dosage amount.
                prescription.amount -= prescription.dosageAmount;

                // Validate the amount is not less than 0. Notify the user if it is.
                if (prescription.amount < 0) {
                    Toast.makeText(context, "amount cannot be less than 0.", Toast.LENGTH_SHORT).show();
                }

                // If unable to update the database then notify the user.
                if (!healthMetricsDbHelper.updatePrescription(prescription)) {
                    Toast.makeText(context, "Unable to update amount.", Toast.LENGTH_SHORT).show();
                }

                // Call updateRecyclerView.
                updateRecyclerView();
            }
        });

        // Set the itemView onCLickListener.
        viewHolder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //
                switchFragment(prescriptionDisplayObject);
            }
        });
    }

    /**
     * Updates the recycler view prescriptions.
     */
    private void updateRecyclerView() {

        // Clear the prescriptionDisplayObjects.
        prescriptionDisplayObjects.clear();

        //Get the prescriptions from the database.
        prescriptionDisplayObjects = healthMetricsDbHelper.getAllPrescriptions();

        // Call notifyDataSetChanged.
        notifyDataSetChanged();
    }

    /**
     * Gets the new fragment and calls switch fragment on the main activity.
     *
     * @param selectedPrescription Represents the selected prescription.
     */
    private void switchFragment(PrescriptionDisplayObject selectedPrescription) {

        // Create ViewPrescription Fragment.
        Fragment destinationFragment = new ViewPrescriptionFragment();

        // Create bundle and add the prescription id.
        Bundle prescriptionBundle = new Bundle();
        prescriptionBundle.putInt("prescription_selected_key", selectedPrescription.id);

        // Set the bundle to the destination fragment.
        destinationFragment.setArguments(prescriptionBundle);

        // If the context is an instance of MainActivity then call switchFragment.
        if (context instanceof MainActivity) {
            MainActivity mainActivity = (MainActivity) context;
            mainActivity.switchFragment(destinationFragment);
        }
    }

    // Returns the size of prescriptionDisplayObjects.
    @Override
    public int getItemCount() {
        return prescriptionDisplayObjects.size();
    }

    /**
     * Defines the view of a row inside the recycler view.
     */
    class ViewHolder extends RecyclerView.ViewHolder {

        // Initialize the information and amount text views.
        TextView prescriptionInformationTextView;
        TextView prescriptionAmountTextView;

        // Initialize the increment and decrement buttons.
        Button incrementAmountButton;
        Button decrementAmountButton;

        ViewHolder(View itemView) {
            super(itemView);

            // Get the views from the prescription list recycler view layout.
            prescriptionInformationTextView = itemView.findViewById(R.id.textViewPrescriptionInformation);
            prescriptionAmountTextView = itemView.findViewById(R.id.textViewPrescriptionAmount);
            incrementAmountButton = itemView.findViewById(R.id.buttonIncrementPrescriptionAmount);
            decrementAmountButton = itemView.findViewById(R.id.buttonDecrementPrescriptionAmount);
        }
    }
}

