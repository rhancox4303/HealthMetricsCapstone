package ca.mohawk.HealthMetrics.PhotoGallery;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.DialogFragment;

import java.util.Objects;

import ca.mohawk.HealthMetrics.Models.PhotoEntry;


/**
 * The DeletePhotoEntryDialog extends the DialogFragment.
 * Presents a dialog to the user indicating photo deletion options.
 */
public class DeletePhotoEntryDialog extends DialogFragment {

    // Instantiate the photoEntry.
    private static PhotoEntry photoEntry;

    // Instantiate the DeletePhotoEntryDialogListener.
    private DeletePhotoEntryDialogListener listener;

    /**
     * Create a new instance of the DeletePhotoEntryDialog.
     *
     * @param photoEntry Represents the photo entry.
     * @return Returns a new DeletePhotoEntryDialog.
     */
    public static DeletePhotoEntryDialog newInstance(PhotoEntry photoEntry) {
        DeletePhotoEntryDialog dialog = new DeletePhotoEntryDialog();
        DeletePhotoEntryDialog.photoEntry = photoEntry;
        return dialog;
    }

    public int getPhotoEntryId() {
        return photoEntry.id;
    }

    public int getGalleryId() {
        return photoEntry.photoGalleryId;
    }

    public int getIsFromGallery() {
        return photoEntry.isFromGallery;
    }

    public String getPhotoEntryPath() {
        return photoEntry.photoEntryPath;
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        try {
            listener = (DeletePhotoEntryDialogListener) context;
        } catch (ClassCastException e) {
            throw new ClassCastException(getActivity().toString()
                    + " must implement DeletePhotoEntryDialogListener");
        }
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {

        AlertDialog.Builder builder = new AlertDialog.Builder(Objects.requireNonNull(getActivity()));

        // Set the title and message.
        builder.setTitle("Delete Photo Entry");
        builder.setMessage("The photo entry will be deleted.");

        // Set the PositiveButton and it's OnClickListener.
        builder.setPositiveButton("Delete", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                listener.onDeletePhotoEntryDialogPositiveClick(DeletePhotoEntryDialog.this);
            }
        });

        // Set the NegativeButton and it's OnClickListener.
        builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                listener.onDeletePhotoEntryDialogNegativeClick(DeletePhotoEntryDialog.this);
            }
        });

        // Return the created alert dialog.
        return builder.create();
    }

    /**
     * The DeletePhotoEntryDialogListener models the method that will handle the button clicks.
     */
    public interface DeletePhotoEntryDialogListener {

        /**
         * Handles a DeletePhotoEntryDialog positive click.
         *
         * @param dialog Represents the DeletePhotoEntryDialog.
         */
        void onDeletePhotoEntryDialogPositiveClick(DeletePhotoEntryDialog dialog);

        /**
         * Handles a DeletePhotoEntryDialog negative click.
         *
         * @param dialog Represents the DeletePhotoEntryDialog.
         */
        void onDeletePhotoEntryDialogNegativeClick(DeletePhotoEntryDialog dialog);
    }
}
