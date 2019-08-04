package ca.mohawk.HealthMetrics.Adapaters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import java.util.List;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import ca.mohawk.HealthMetrics.DisplayObjects.PrescriptionRecyclerViewObject;
import ca.mohawk.HealthMetrics.Models.Prescription;
import ca.mohawk.HealthMetrics.R;


public class PrescriptionRecyclerViewAdapter
        extends RecyclerView.Adapter<PrescriptionRecyclerViewAdapter.ViewHolder> {
    private Context context;

    //The list of prescription to be displayed in the recycler view.
    private List<PrescriptionRecyclerViewObject> prescriptionList;

    public PrescriptionRecyclerViewAdapter(List<PrescriptionRecyclerViewObject> metricRecyclerViewObjectList, Context context) {
        this.prescriptionList = prescriptionList;
        this.context = context;
    }

    public class ViewHolder extends RecyclerView.ViewHolder{
        public TextView prescriptionInformationTextView;
        public TextView prescriptionAmountTextView;
        public Button incrementAmountButton;
        public Button decrementAmountButton;

        public ViewHolder(View itemView) {
            super(itemView);

            prescriptionInformationTextView = (TextView) itemView.findViewById(R.id.textViewPrescriptionInformation);
            prescriptionAmountTextView = (TextView) itemView.findViewById(R.id.textViewPrescriptionAmount);
            incrementAmountButton = (Button) itemView.findViewById(R.id.buttonIncrementAmount);
            decrementAmountButton = (Button) itemView.findViewById(R.id.buttonDecrementAmount);
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
        String prescriptionInformation = prescription.getName() + "/n" + prescription.getDosageAmount() + " " + prescription.DosageMeasurement;
        String prescriptionAmount = prescription.getDosageAmount() + " " + prescription.getDosageMeasurement();
        // Set item views

    }
    // Returns the total count of items in the list
    @Override
    public int getItemCount() {
        return prescriptionList.size();
    }
}

