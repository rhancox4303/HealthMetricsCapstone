package ca.mohawk.HealthMetrics.PhotoGallery;


import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.DialogFragment;

import java.util.Objects;


/**
 * The InsertPhotoDialog extends the DialogFragment.
 * Presents a dialog to the user indicating photo insertion options.
 */
public class InsertPhotoDialog extends DialogFragment {

    // Instantiate the InsertPhotoDialogListener.
    private InsertPhotoDialogListener listener;

    /**
     * Create a new instance of the InsertPhotoDialog.
     *
     * @return Returns a new InsertPhotoDialog.
     */
    public static InsertPhotoDialog newInstance() {

        return new InsertPhotoDialog();
    }

    void setListener(InsertPhotoDialogListener listener) {
        this.listener = listener;
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {

        AlertDialog.Builder builder = new AlertDialog.Builder(Objects.requireNonNull(getActivity()));

        builder.setTitle("Insert Photo Entry.");

        // Set the PositiveButton and it's OnClickListener.
        builder.setPositiveButton("Camera", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                listener.onInsertPhotoDialogPositiveClick(InsertPhotoDialog.this);
            }
        });

        // Set the NeutralButton and it's OnClickListener.
        builder.setNeutralButton("Gallery", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                listener.onInsertPhotoDialogNeutralClick(InsertPhotoDialog.this);
            }
        });

        // Set the negative button and it's OnClickListener.
        builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                listener.onInsertPhotoDialogNegativeClick(InsertPhotoDialog.this);
            }
        });

        // Return the created alert dialog.
        return builder.create();
    }

    /**
     * The InsertPhotoDialogListener models the method that will handle the button clicks.
     */
    public interface InsertPhotoDialogListener {

        /**
         * Handles a InsertPhotoDialog positive click.
         *
         * @param dialog Represents the InsertPhotoDialog.
         */
        void onInsertPhotoDialogPositiveClick(InsertPhotoDialog dialog);

        /**
         * Handles a InsertPhotoDialog neutral click.
         *
         * @param dialog Represents the InsertPhotoDialog.
         */
        void onInsertPhotoDialogNeutralClick(InsertPhotoDialog dialog);

        /**
         * Handles a InsertPhotoDialog negative click.
         *
         * @param dialog Represents the InsertPhotoDialog.
         */
        void onInsertPhotoDialogNegativeClick(InsertPhotoDialog dialog);
    }
}
