package ca.mohawk.HealthMetrics.Notification;


import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;

import java.util.Objects;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.DialogFragment;


/**
 * The DeleteNotificationDialog extends the DialogFragment.
 * Presents a dialog to the user indicating notifications deletion options.
 */
public class DeleteNotificationDialog extends DialogFragment {

    // Instantiate the notificationId.
    private static int notificationId;

    // Instantiate the DeleteNotificationDialogListener.
    private DeleteNotificationDialog.DeleteNotificationDialogListener listener;

    /**
     * Create a new instance of the DeleteNotificationDialog.
     *
     * @param notificationId Represents the notification id.
     * @return Returns a new DeleteNotificationDialog.
     */
    public static DeleteNotificationDialog newInstance(int notificationId) {
        DeleteNotificationDialog.notificationId = notificationId;

        return new DeleteNotificationDialog();
    }

    public int getNotificationId() {
        return notificationId;
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        try {
            listener = (DeleteNotificationDialogListener) context;
        } catch (ClassCastException e) {
            throw new ClassCastException(getActivity().toString()
                    + " must implement DeleteNotificationDialogListener");
        }
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {

        AlertDialog.Builder builder = new AlertDialog.Builder(Objects.requireNonNull(getActivity()));

        // Set the title and message.
        builder.setTitle("Delete Notification");
        builder.setMessage("The notification will be deleted.");

        // Set the PositiveButton and it's OnClickListener.
        builder.setPositiveButton("Delete", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                listener.onDeleteNotificationDialogPositiveClick(DeleteNotificationDialog.this);
            }
        });

        // Set the NegativeButton and it's OnClickListener.
        builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                listener.onDeleteNotificationDialogNegativeClick(DeleteNotificationDialog.this);
            }
        });

        // Return the created alert dialog.
        return builder.create();
    }

    /**
     * The DeleteNotificationDialogListener models the method that will handle the button clicks.
     */
    public interface DeleteNotificationDialogListener {

        /**
         * Handles a DeleteNotificationDialog positive click.
         *
         * @param dialog Represents the DeleteNotificationDialog.
         */
        void onDeleteNotificationDialogPositiveClick(DeleteNotificationDialog dialog);

        /**
         * Handles a DeleteNotificationDialog negative click.
         *
         * @param dialog Represents the DeleteNotificationDialog.
         */
        void onDeleteNotificationDialogNegativeClick(DeleteNotificationDialog dialog);
    }
}
