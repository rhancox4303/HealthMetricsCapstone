package ca.mohawk.HealthMetrics.PhotoGallery;


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

import ca.mohawk.HealthMetrics.Models.PhotoEntry;
import ca.mohawk.HealthMetrics.R;


/**
 * A simple {@link Fragment} subclass.
 */
public class DeletePhotoEntryDialog extends DialogFragment {

    private DeletePhotoEntryDialogListener listener;
    private static PhotoEntry PhotoEntry;

    public static DeletePhotoEntryDialog newInstance(PhotoEntry photoEntry) {
        DeletePhotoEntryDialog dialog = new DeletePhotoEntryDialog();
        PhotoEntry = photoEntry;
        return dialog;
    }

    public int getPhotoEntryId() {
        return PhotoEntry.Id;
    }

    public int getGalleryId() {
        return PhotoEntry.PhotoGalleryId;
    }

    public int getIsFromGallery() {
        return PhotoEntry.IsFromGallery;
    }

    public String getPhotoEntryPath() {
        return PhotoEntry.PhotoEntryPath;
    }
    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        // Verify that the host activity implements the callback interface
        try {
            // Instantiate the NoticeDialogListener so we can send events to the host
            listener = (DeletePhotoEntryDialogListener) context;
        } catch (ClassCastException e) {
            // The activity doesn't implement the interface, throw exception
            throw new ClassCastException(getActivity().toString()
                    + " must implement NoticeDialogListener");
        }
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {

        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());

        builder.setTitle("Delete Photo Entry");
        builder.setMessage("The photo entry will be deleted.");

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
        public void onDeletePhotoEntryDialogPositiveClick(DeletePhotoEntryDialog dialog);

        public void onDeletePhotoEntryDialogNegativeClick(DeletePhotoEntryDialog dialog);
    }
}
