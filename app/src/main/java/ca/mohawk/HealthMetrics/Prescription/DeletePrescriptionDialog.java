package ca.mohawk.HealthMetrics.Prescription;


import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.DialogFragment;

import java.util.Objects;

/**
 * The DeletePrescriptionDialog extends the DialogFragment.
 * Presents a dialog to the user indicating prescription deletion options.
 */
public class DeletePrescriptionDialog extends DialogFragment {

    // Instantiate the metricId.
    private static int prescriptionId;

    // Instantiate the DeleteMetricDialogListener.
    private DeletePrescriptionDialogListener listener;

    /**
     * Create a new instance of the DeletePrescriptionDialog.
     *
     * @param prescriptionId Represents the prescription id.
     * @return Returns a new DeletePrescriptionDialog.
     */
    public static DeletePrescriptionDialog newInstance(int prescriptionId) {
        DeletePrescriptionDialog.prescriptionId = prescriptionId;
        return new DeletePrescriptionDialog();
    }

    public int getPrescriptionId() {
        return prescriptionId;
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        try {
            listener = (DeletePrescriptionDialogListener) context;
        } catch (ClassCastException e) {
            throw new ClassCastException(getActivity().toString()
                    + " must implement DeletePrescriptionDialogListener");
        }
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {

        AlertDialog.Builder builder = new AlertDialog.Builder(Objects.requireNonNull(getActivity()));

        // Set the title and message.
        builder.setTitle("Delete Prescription");
        builder.setMessage("The prescription will be deleted.");

        // Set the PositiveButton and it's OnClickListener.
        builder.setPositiveButton("Delete", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                listener.onDeletePrescriptionDialogPositiveClick(DeletePrescriptionDialog.this);
            }
        });

        // Set the NegativeButton and it's OnClickListener.
        builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                listener.onDeletePrescriptionDialogNegativeClick(DeletePrescriptionDialog.this);
            }
        });

        // Return the created alert dialog.
        return builder.create();
    }

    /**
     * The DeletePrescriptionDialogListener models the method that will handle the button clicks.
     */
    public interface DeletePrescriptionDialogListener {
        /**
         * Handles a DeletePrescriptionDialog positive click.
         *
         * @param dialog Represents the DeletePrescriptionDialog.
         */
        void onDeletePrescriptionDialogPositiveClick(DeletePrescriptionDialog dialog);

        /**
         * Handles a DeletePrescriptionDialog negative click.
         *
         * @param dialog Represents the DeletePrescriptionDialog.
         */

        void onDeletePrescriptionDialogNegativeClick(DeletePrescriptionDialog dialog);
    }
}
