package ca.mohawk.HealthMetrics.Notification;


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
public class DeleteNotificationDialog extends DialogFragment {

    private DeleteNotificationDialog.DeleteNotificationDialogListener listener;
    private static int NotificationId;

    public static DeleteNotificationDialog newInstance(int notificationId) {
        NotificationId = notificationId;

        DeleteNotificationDialog dialog = new DeleteNotificationDialog();
        return dialog;
    }
    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        // Verify that the host activity implements the callback interface
        try {
            // Instantiate the NoticeDialogListener so we can send events to the host
            listener = (DeleteNotificationDialogListener) context;
        } catch (ClassCastException e) {
            // The activity doesn't implement the interface, throw exception
            throw new ClassCastException(getActivity().toString()
                    + " must implement NoticeDialogListener");
        }
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {

        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());

        builder.setTitle("Delete Notification");
        builder.setMessage("The notification will be deleted.");

        builder.setPositiveButton("Delete", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                // Send the positive button event back to the host activity
                listener.onDeleteNotificationDialogPositiveClick(DeleteNotificationDialog.this);
            }
        })
                .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        // Send the negative button event back to the host activity
                        listener.onDeleteNotificationDialogNegativeClick(DeleteNotificationDialog.this);
                    }
                });

        return builder.create();
    }

    public interface DeleteNotificationDialogListener {
        public void onDeleteNotificationDialogPositiveClick(DeleteNotificationDialog dialog);

        public void onDeleteNotificationDialogNegativeClick(DeleteNotificationDialog dialog);
    }
}
