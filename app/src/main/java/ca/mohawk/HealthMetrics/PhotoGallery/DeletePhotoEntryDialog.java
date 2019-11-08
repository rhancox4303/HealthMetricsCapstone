package ca.mohawk.HealthMetrics.PhotoGallery;


import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;

import java.util.Objects;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.Fragment;
import ca.mohawk.HealthMetrics.Models.PhotoEntry;


/**
 * A simple {@link Fragment} subclass.
 */
public class DeletePhotoEntryDialog extends DialogFragment {

    private static PhotoEntry PhotoEntry;
    private DeletePhotoEntryDialogListener listener;

    public static DeletePhotoEntryDialog newInstance(PhotoEntry photoEntry) {
        DeletePhotoEntryDialog dialog = new DeletePhotoEntryDialog();
        PhotoEntry = photoEntry;
        return dialog;
    }

    public int getPhotoEntryId() {
        return PhotoEntry.id;
    }

    public int getGalleryId() {
        return PhotoEntry.photoGalleryId;
    }

    public int getIsFromGallery() {
        return PhotoEntry.isFromGallery;
    }

    public String getPhotoEntryPath() {
        return PhotoEntry.photoEntryPath;
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        // Verify that the host activity implements the callback interface
        try {
            // Instantiate the NoticeDialogListener so we can send events to the host
            listener = (DeletePhotoEntryDialogListener) context;
        } catch (ClassCastException e) {
            // The activity doesn't implement the interface, throw exception
            throw new ClassCastException(getActivity().toString()
                    + " must implement DeletePhotoEntryDialogListener");
        }
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {

        AlertDialog.Builder builder = new AlertDialog.Builder(Objects.requireNonNull(getActivity()));

        builder.setTitle("Delete Photo latestDataEntry");
        builder.setMessage("The photo latestDataEntry will be deleted.");

        builder.setPositiveButton("Delete", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                // Send the positive button event back to the host activity
                listener.onDeletePhotoEntryDialogPositiveClick(DeletePhotoEntryDialog.this);
            }
        })
                .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        // Send the negative button event back to the host activity
                        listener.onDeletePhotoEntryDialogNegativeClick(DeletePhotoEntryDialog.this);
                    }
                });

        return builder.create();
    }

    public interface DeletePhotoEntryDialogListener {
        void onDeletePhotoEntryDialogPositiveClick(DeletePhotoEntryDialog dialog);

        void onDeletePhotoEntryDialogNegativeClick(DeletePhotoEntryDialog dialog);
    }
}
