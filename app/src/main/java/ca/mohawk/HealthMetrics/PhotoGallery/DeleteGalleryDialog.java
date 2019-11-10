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
 * The DeleteGalleryDialog extends the DialogFragment.
 * Presents a dialog to the user indicating gallery deletion options.
 */
public class DeleteGalleryDialog extends DialogFragment {

    // Instantiate the galleryId.
    private static int galleryId;

    // Instantiate the DeleteGalleryDialogListener.
    private DeleteGalleryDialogListener listener;

    /**
     * Create a new instance of the DeleteGalleryDialog.
     *
     * @param galleryId Represents the gallery id.
     * @return Returns a new DeleteGalleryDialog.
     */
    public static DeleteGalleryDialog newInstance(int galleryId) {
        DeleteGalleryDialog deleteGalleryDialog = new DeleteGalleryDialog();
        DeleteGalleryDialog.galleryId = galleryId;
        return deleteGalleryDialog;
    }

    public int getGalleryId() {
        return galleryId;
    }

    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        try {
            listener = (DeleteGalleryDialogListener) context;
        } catch (ClassCastException e) {
            throw new ClassCastException(getActivity().toString()
                    + " must implement DeleteGalleryDialogListener");
        }
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {

        AlertDialog.Builder builder = new AlertDialog.Builder(Objects.requireNonNull(getActivity()));

        // Set the title and message.
        builder.setTitle("Delete Gallery");
        builder.setMessage("The gallery and all of it's photo entries will be deleted.");

        // Set the PositiveButton and it's OnClickListener.
        builder.setPositiveButton("Delete", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                listener.onDeleteGalleryDialogPositiveClick(DeleteGalleryDialog.this);
            }
        });

        // Set the NegativeButton and it's OnClickListener.
        builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                listener.onDeleteGalleryDialogNegativeClick(DeleteGalleryDialog.this);
            }
        });

        // Return the created alert dialog.
        return builder.create();
    }

    /**
     * The DeleteGalleryDialogListener models the method that will handle the button clicks.
     */
    public interface DeleteGalleryDialogListener {

        /**
         * Handles a DeleteGalleryDialog positive click.
         *
         * @param dialog Represents the DeleteGalleryDialog.
         */
        void onDeleteGalleryDialogPositiveClick(DeleteGalleryDialog dialog);

        /**
         * Handles a DeleteGalleryDialog negative click.
         *
         * @param dialog Represents the DeleteGalleryDialog.
         */
        void onDeleteGalleryDialogNegativeClick(DeleteGalleryDialog dialog);
    }
}