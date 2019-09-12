package ca.mohawk.HealthMetrics.MetricManagement;


import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;

import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import ca.mohawk.HealthMetrics.R;


/**
 * A simple {@link Fragment} subclass.
 */
public class RemoveMetricDialog extends DialogFragment {
    private RemoveMetricDialogListener listener;
    private static int MetricId;

    public static RemoveMetricDialog newInstance(int metricId) {
        MetricId = metricId;
        RemoveMetricDialog frag = new RemoveMetricDialog();
        return frag;
    }

    public int getMetricId() {
        return MetricId;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        // Verify that the host activity implements the callback interface
        try {
            // Instantiate the NoticeDialogListener so we can send events to the host
            listener = (RemoveMetricDialogListener) context;
        } catch (ClassCastException e) {
            // The activity doesn't implement the interface, throw exception
            throw new ClassCastException(getActivity().toString()
                    + " must implement Listener");
        }
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {

        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());

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
        public void onRemoveMetricDialogPositiveClick(RemoveMetricDialog dialog);

        public void onRemoveMetricDialogNegativeClick(RemoveMetricDialog dialog);
    }
}
