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
 * The RemoveMetricDialog extends the DialogFragment.
 * Presents a dialog to the user indicating metric removal options.
 */
public class RemoveMetricDialog extends DialogFragment {

    // Instantiate the metricId.
    private static int metricId;

    //Instantiate the RemoveMetricDialogListener.
    private RemoveMetricDialogListener listener;

    /**
     * Create a new instance of the RemoveMetricDialog.
     *
     * @param metricId Represents the metric id.
     * @return Returns a new RemoveMetricDialog.
     */
    public static RemoveMetricDialog newInstance(int metricId) {
        RemoveMetricDialog.metricId = metricId;
        return new RemoveMetricDialog();
    }

    public int getMetricId() {
        return metricId;
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        try {
            listener = (RemoveMetricDialogListener) context;
        } catch (ClassCastException e) {
            throw new ClassCastException(getActivity().toString()
                    + " must implement RemoveMetricDialogListener");
        }
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {

        AlertDialog.Builder builder = new AlertDialog.Builder(Objects.requireNonNull(getActivity()));

        // Set the title and message.
        builder.setTitle("Remove Metric");
        builder.setMessage("The metric will be removed from your profile. All data entries for this metric will be deleted.");

        // Set the PositiveButton and it's OnClickListener.
        builder.setPositiveButton("Remove", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                listener.onRemoveMetricDialogPositiveClick(RemoveMetricDialog.this);
            }
        });

        // Set the NegativeButton and it's OnClickListener.
        builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                listener.onRemoveMetricDialogNegativeClick(RemoveMetricDialog.this);
            }
        });

        // Return the created alert dialog.
        return builder.create();
    }

    /**
     * The RemoveMetricDialogListener models the method that will handle the button clicks.
     */
    public interface RemoveMetricDialogListener {
        void onRemoveMetricDialogPositiveClick(RemoveMetricDialog dialog);

        void onRemoveMetricDialogNegativeClick(RemoveMetricDialog dialog);
    }
}