package ca.mohawk.HealthMetrics.UserProfile;


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
import android.widget.TextView;

import ca.mohawk.HealthMetrics.R;

/**
 * A simple {@link Fragment} subclass.
 */
public class DeleteProfileDialog extends DialogFragment {

    private DeleteUserDialogListener listener;

    public static DeleteProfileDialog newInstance() {
        DeleteProfileDialog frag = new DeleteProfileDialog();
        return frag;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        // Verify that the host activity implements the callback interface
        try {
            // Instantiate the NoticeDialogListener so we can send events to the host
            listener = (DeleteUserDialogListener) context;
        } catch (ClassCastException e) {
            // The activity doesn't implement the interface, throw exception
            throw new ClassCastException(getActivity().toString()
                    + " must implement NoticeDialogListener");
        }
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {

        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());

        builder.setTitle("Delete User Profile");
        builder.setMessage("The profile will be deleted. All data will be deleted");

        builder.setPositiveButton("Delete", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                // Send the positive button event back to the host activity
                listener.onDeletePrescriptionDialogPositiveClick(DeleteProfileDialog.this);
            }
        })
                .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        // Send the negative button event back to the host activity
                        listener.onDeletePrescriptionDialogNegativeClick(DeleteProfileDialog.this);
                    }
                });

        return builder.create();
    }

    public interface DeleteUserDialogListener {
        public void onDeletePrescriptionDialogPositiveClick(DeleteProfileDialog dialog);

        public void onDeletePrescriptionDialogNegativeClick(DeleteProfileDialog dialog);
    }
}
