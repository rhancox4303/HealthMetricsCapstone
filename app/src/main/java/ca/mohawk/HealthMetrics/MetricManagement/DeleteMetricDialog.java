package ca.mohawk.HealthMetrics.MetricManagement;


import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;

import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.appcompat.app.AlertDialog;
import ca.mohawk.HealthMetrics.R;


/**
 * A simple {@link Fragment} subclass.
 */
public class DeleteMetricDialog extends DialogFragment {

    private DeleteMetricDialogListener listener;
    private static int MetricId;

    public static DeleteMetricDialog newInstance(int metricId) {
        MetricId = metricId;
        DeleteMetricDialog frag = new DeleteMetricDialog();
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
            listener = (DeleteMetricDialogListener) context;
        } catch (ClassCastException e) {
            // The activity doesn't implement the interface, throw exception
            throw new ClassCastException(getActivity().toString()
                    + " must implement NoticeDialogListener");
        }
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {

        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());

        builder.setTitle("Delete Metric");
        builder.setMessage("The metric will be deleted. All data entries for this metric will be deleted.");

        builder.setPositiveButton("Delete", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                // Send the positive button event back to the host activity
                listener.onDeleteMetricDialogPositiveClick(DeleteMetricDialog.this);
            }
        })
                .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        // Send the negative button event back to the host activity
                        listener.onDeleteMetricDialogNegativeClick(DeleteMetricDialog.this);
                    }
                });

        return builder.create();
    }

    public interface DeleteMetricDialogListener {
        public void onDeleteMetricDialogPositiveClick(DeleteMetricDialog dialog);

        public void onDeleteMetricDialogNegativeClick(DeleteMetricDialog dialog);
    }
}
