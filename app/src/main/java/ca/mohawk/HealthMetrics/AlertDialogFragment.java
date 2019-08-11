package ca.mohawk.HealthMetrics;


import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;

import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.DialogFragment;

public class AlertDialogFragment extends DialogFragment {

    AlertDialogListener listener;
    private static String deleteType;
    private static int dataID;
    private static int dataParentID;

    public static int getDataID() {
        return dataID;
    }

    public static String getDeleteType() {
        return deleteType;
    }

    public static int getDataParentID() {
        return dataParentID;
    }

    public static AlertDialogFragment newInstance(String type, int id, int dataParentId) {
        deleteType = type;
        dataID = id;
        dataParentID = dataParentId;
        AlertDialogFragment frag = new AlertDialogFragment();
        return frag;
    }

    public static AlertDialogFragment newInstance(String type, int id) {
        deleteType = type;
        dataID = id;
        AlertDialogFragment frag = new AlertDialogFragment();
        return frag;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        // Verify that the host activity implements the callback interface
        try {
            // Instantiate the NoticeDialogListener so we can send events to the host
            listener = (AlertDialogListener) context;
        } catch (ClassCastException e) {
            // The activity doesn't implement the interface, throw exception
            throw new ClassCastException(getActivity().toString()
                    + " must implement NoticeDialogListener");
        }
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {

        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        switch (deleteType){
            case "DataEntry":
                builder.setTitle("Delete Data Entry");
                builder.setMessage("The data entry will be deleted");
                break;
            case "Prescription":
                builder.setTitle("Delete Prescription");
                builder.setMessage("The prescription will be deleted");
                break;
        }
        builder.setPositiveButton("Delete", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        // Send the positive button event back to the host activity
                        listener.onDialogPositiveClick(AlertDialogFragment.this);
                    }
                })
                .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        // Send the negative button event back to the host activity
                        listener.onDialogNegativeClick(AlertDialogFragment.this);
                    }
                });
                return builder.create();
    }

    public interface AlertDialogListener {
        public void onDialogPositiveClick(DialogFragment dialog);

        public void onDialogNegativeClick(DialogFragment dialog);
    }
}