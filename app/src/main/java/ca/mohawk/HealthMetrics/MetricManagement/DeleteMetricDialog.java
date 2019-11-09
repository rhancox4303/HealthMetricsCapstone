package ca.mohawk.HealthMetrics.MetricManagement;


import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;

import java.util.Objects;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.DialogFragment;

/**
 * The DeleteMetricDialog extends the DialogFragment.
 * Presents a dialog to the user indicating metric deletion options.
 */
public class DeleteMetricDialog extends DialogFragment {

    // Instantiate the metricId.
    private static int metricId;

    // Instantiate the DeleteMetricDialogListener.
    private DeleteMetricDialogListener listener;

    /**
     * Create a new instance of the DeleteMetricDialog.
     *
     * @param metricId Represents the metric id.
     * @return Returns a new DeleteMetricDialog.
     */
    public static DeleteMetricDialog newInstance(int metricId) {
        DeleteMetricDialog.metricId = metricId;
        return new DeleteMetricDialog();
    }

    public int getMetricId() {
        return metricId;
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        try {
            listener = (DeleteMetricDialogListener) context;
        } catch (ClassCastException e) {
            throw new ClassCastException(getActivity().toString()
                    + " must implement DeleteMetricDialogListener");
        }
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {

        AlertDialog.Builder builder = new AlertDialog.Builder(Objects.requireNonNull(getActivity()));

        // Set the title and message.
        builder.setTitle("Delete Metric");
        builder.setMessage("The metric will be deleted. All data entries for this metric will be deleted.");

        // Set the PositiveButton and it's OnClickListener.
        builder.setPositiveButton("Delete", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                listener.onDeleteMetricDialogPositiveClick(DeleteMetricDialog.this);
            }
        });


        // Set the NegativeButton and it's OnClickListener.
        builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                listener.onDeleteMetricDialogNegativeClick(DeleteMetricDialog.this);
            }
        });

        // Return the created alert dialog.
        return builder.create();
    }

    /**
     * The DeleteMetricDialogListener models the method that will handle the button clicks.
     */
    public interface DeleteMetricDialogListener {

        /**
         * Handles a DeleteMetricDialog positive click.
         *
         * @param dialog Represents the DeleteMetricDialog.
         */
        void onDeleteMetricDialogPositiveClick(DeleteMetricDialog dialog);

        /**
         * Handles a DeleteMetricDialog negative click.
         *
         * @param dialog Represents the DeleteMetricDialog.
         */
        void onDeleteMetricDialogNegativeClick(DeleteMetricDialog dialog);
    }
}