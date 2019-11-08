package ca.mohawk.HealthMetrics.DataEntry;


import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;

import java.util.Objects;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.DialogFragment;

/**
 * The DeleteDataEntryDialog extends the DialogFragment.
 * Presents a dialog to the user indicating data deletion options.
 */
public class DeleteDataEntryDialog extends DialogFragment {

    // Initialize the dataEntryId.
    private static int dataEntryId;

    // Initialize the metricId.
    private static int metricId;

    // Initialize the listener.
    private DeleteDataEntryDialogListener listener;

    /**
     * Creates a new instance of the DeleteDataEntryDialog.
     *
     * @param dataEntryId Represents the dataEntryId.
     * @param metricId    Represents the metricId
     * @return A DeleteDataEntryDialog is returned.
     */
    public static DeleteDataEntryDialog newInstance(int dataEntryId, int metricId) {
        DeleteDataEntryDialog.metricId = metricId;
        DeleteDataEntryDialog.dataEntryId = dataEntryId;
        return new DeleteDataEntryDialog();
    }

    public int getDataEntryId() {
        return dataEntryId;
    }

    public int getMetricId() {
        return metricId;
    }

    @Override
    public void onAttach(@NonNull Context context) {

        super.onAttach(context);
        try {
            listener = (DeleteDataEntryDialogListener) context;
        } catch (ClassCastException e) {
            throw new ClassCastException(getActivity().toString()
                    + " must implement DeleteDataEntryDialogListener");
        }
    }

    @NonNull
    @Override
    public AlertDialog onCreateDialog(Bundle savedInstanceState) {

        // Create a new AlertDialog Builder.
        AlertDialog.Builder builder = new AlertDialog.Builder(Objects.requireNonNull(getActivity()));

        // Set the title and message.
        builder.setTitle("Delete Data latestDataEntry");
        builder.setMessage("The data latestDataEntry will be deleted.");

        // Set the PositiveButton.
        builder.setPositiveButton("Delete", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                // Send the positive button event back to the main activity.
                listener.onDeleteDataEntryDialogPositiveClick(DeleteDataEntryDialog.this);
            }
        });

        // Set the NegativeButton.
        builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                // Send the negative button event back to the main activity.
                listener.onDeleteDataEntryDialogNegativeClick(DeleteDataEntryDialog.this);
            }
        });

        // Return the created Alert Dialog.
        return builder.create();
    }

    /**
     * The DeleteDataEntryDialogListener will handle the button clicks.
     */
    public interface DeleteDataEntryDialogListener {

        void onDeleteDataEntryDialogPositiveClick(DeleteDataEntryDialog dialog);

        void onDeleteDataEntryDialogNegativeClick(DeleteDataEntryDialog dialog);
    }
}
