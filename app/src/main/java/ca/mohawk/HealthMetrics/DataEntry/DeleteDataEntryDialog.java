package ca.mohawk.HealthMetrics.DataEntry;


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
public class DeleteDataEntryDialog extends DialogFragment {

    private static int DataEntryId;
    private static int MetricId;
    private DeleteDataEntryDialogListener listener;

    public static DeleteDataEntryDialog newInstance(int dataEntryId, int metricId) {
        MetricId = metricId;
        DataEntryId = dataEntryId;
        return new DeleteDataEntryDialog();
    }

    public int getDataEntryId() {
        return DataEntryId;
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
            listener = (DeleteDataEntryDialogListener) context;
        } catch (ClassCastException e) {
            // The activity doesn't implement the interface, throw exception
            throw new ClassCastException(getActivity().toString()
                    + " must implement DeleteDataEntryDialogListener");
        }
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {

        AlertDialog.Builder builder = new AlertDialog.Builder(Objects.requireNonNull(getActivity()));

        builder.setTitle("Delete Data Entry");
        builder.setMessage("The data entry will be deleted.");

        builder.setPositiveButton("Delete", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                // Send the positive button event back to the host activity
                listener.onDeleteDataEntryDialogPositiveClick(DeleteDataEntryDialog.this);
            }
        })
                .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        // Send the negative button event back to the host activity
                        listener.onDeleteDataEntryDialogNegativeClick(DeleteDataEntryDialog.this);
                    }
                });

        return builder.create();
    }

    public interface DeleteDataEntryDialogListener {
        void onDeleteDataEntryDialogPositiveClick(DeleteDataEntryDialog dialog);

        void onDeleteDataEntryDialogNegativeClick(DeleteDataEntryDialog dialog);
    }
}
