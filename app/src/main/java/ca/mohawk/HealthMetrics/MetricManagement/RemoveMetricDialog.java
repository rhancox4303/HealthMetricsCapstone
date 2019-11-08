package ca.mohawk.HealthMetrics.MetricManagement;


import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;

import java.util.Objects;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.Fragment;


/**
 * A simple {@link Fragment} subclass.
 */
public class RemoveMetricDialog extends DialogFragment {
    private static int MetricId;
    private RemoveMetricDialogListener listener;

    public static RemoveMetricDialog newInstance(int metricId) {
        MetricId = metricId;
        return new RemoveMetricDialog();
    }

    public int getMetricId() {
        return MetricId;
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        // Verify that the host activity implements the callback interface
        try {
            // Instantiate the NoticeDialogListener so we can send events to the host
            listener = (RemoveMetricDialogListener) context;
        } catch (ClassCastException e) {
            // The activity doesn't implement the interface, throw exception
            throw new ClassCastException(getActivity().toString()
                    + " must implement RemoveMetricDialogListener");
        }
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {

        AlertDialog.Builder builder = new AlertDialog.Builder(Objects.requireNonNull(getActivity()));

        builder.setTitle("Remove Metric");
        builder.setMessage("The metric will be removed from your profile. All data entries for this metric will be deleted.");

        builder.setPositiveButton("Remove", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                // Send the positive button event back to the host activity
                listener.onRemoveMetricDialogPositiveClick(RemoveMetricDialog.this);
            }
        })
                .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        // Send the negative button event back to the host activity
                        listener.onRemoveMetricDialogNegativeClick(RemoveMetricDialog.this);
                    }
                });

        return builder.create();
    }

    public interface RemoveMetricDialogListener {
        void onRemoveMetricDialogPositiveClick(RemoveMetricDialog dialog);

        void onRemoveMetricDialogNegativeClick(RemoveMetricDialog dialog);
    }
}
