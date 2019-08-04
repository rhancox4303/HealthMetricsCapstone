package ca.mohawk.HealthMetrics.Adapaters;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;
import ca.mohawk.HealthMetrics.DisplayObjects.PrescriptionRecyclerViewObject;
import ca.mohawk.HealthMetrics.MainActivity;
import ca.mohawk.HealthMetrics.Models.Prescription;
import ca.mohawk.HealthMetrics.Prescription.ViewPrescriptionFragment;
import ca.mohawk.HealthMetrics.R;


public class PrescriptionRecyclerViewAdapter
        extends RecyclerView.Adapter<PrescriptionRecyclerViewAdapter.ViewHolder> {
    private Context context;

    //The list of prescription to be displayed in the recycler view.
    private List<PrescriptionRecyclerViewObject> prescriptionList = new ArrayList<>();

    public PrescriptionRecyclerViewAdapter(List<PrescriptionRecyclerViewObject> prescriptionList, Context context) {
        this.prescriptionList = prescriptionList;
        this.context = context;
    }

    public class ViewHolder extends RecyclerView.ViewHolder{
        public TextView prescriptionInformationTextView;
        public TextView prescriptionFrequencyTextView;
       // public Button incrementAmountButton;
       // public Button decrementAmountButton;

        public ViewHolder(View itemView) {
            super(itemView);

            prescriptionInformationTextView = (TextView) itemView.findViewById(R.id.textViewPrescriptionInformation);
            prescriptionFrequencyTextView = (TextView) itemView.findViewById(R.id.textViewPrescriptionFrequency);
           // incrementAmountButton = (Button) itemView.findViewById(R.id.buttonIncrementAmount);
           // decrementAmountButton = (Button) itemView.findViewById(R.id.buttonDecrementAmount);
        }
    }

    /**
     * The onCreateViewHolder method is used to inflate
     * the custom layout and create the viewholder.
     */
    @Override
    public PrescriptionRecyclerViewAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        Context context = parent.getContext();
        LayoutInflater inflater = LayoutInflater.from(context);

        View contactView = inflater.inflate(R.layout.prescription_recyclerview_layout, parent, false);

        ViewHolder viewHolder = new ViewHolder(contactView);
        return viewHolder;
    }

    /**
     * The onBindViewHolder method is used to set the item views in the recycler
     * view using the metricRecyclerViewObjectList and the view holder.
     */
    @Override
    public void onBindViewHolder(PrescriptionRecyclerViewAdapter.ViewHolder viewHolder, int position) {
        //Get data object
        final PrescriptionRecyclerViewObject prescription = prescriptionList.get(position);
        String prescriptionInformation = prescription.getName();
        String frequency = prescription.getDosageAmount() + " " + prescription.getDosageMeasurement() + " " + prescription.getFrequency();

        TextView informationTextView = viewHolder.prescriptionInformationTextView;
        TextView frequencyTextView = viewHolder.prescriptionFrequencyTextView;

        informationTextView.setText(prescriptionInformation);
        frequencyTextView.setText(frequency);

        viewHolder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                changeFragment(prescription);
            }
        });

//        fre.setText(prescriptionAmount);
        // Set item views

    }

    private void changeFragment(PrescriptionRecyclerViewObject itemSelected) {
        Fragment fragment = new ViewPrescriptionFragment();

        Bundle prescriptionBundle = new Bundle();
        prescriptionBundle.putInt("prescription_selected_key", itemSelected.Id);

        fragment.setArguments(prescriptionBundle);
        switchContent(fragment);
    }

    public void switchContent(Fragment fragment) {
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

