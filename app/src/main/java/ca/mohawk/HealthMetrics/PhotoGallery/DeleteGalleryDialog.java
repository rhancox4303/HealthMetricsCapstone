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


/**
 * A simple {@link Fragment} subclass.
 */
public class DeleteGalleryDialog extends DialogFragment {

    private static int GalleryId;
    private DeleteGalleryDialogListener listener;

    public static DeleteGalleryDialog newInstance(int galleryId) {
        DeleteGalleryDialog dialog = new DeleteGalleryDialog();
        GalleryId = galleryId;
        return dialog;
    }

    public int getGalleryId() {
        return GalleryId;
    }

    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        // Verify that the host activity implements the callback interface
        try {
            // Instantiate the NoticeDialogListener so we can send events to the host
            listener = (DeleteGalleryDialogListener) context;
        } catch (ClassCastException e) {
            // The activity doesn't implement the interface, throw exception
            throw new ClassCastException(getActivity().toString()
                    + " must implement DeleteGalleryDialogListener");
        }
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {

        AlertDialog.Builder builder = new AlertDialog.Builder(Objects.requireNonNull(getActivity()));

        builder.setTitle("Delete Gallery");
        builder.setMessage("The gallery will be deleted.");

        builder.setPositiveButton("Delete", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                // Send the positive button event back to the host activity
                listener.onDeleteGalleryDialogPositiveClick(DeleteGalleryDialog.this);
            }
        })
                .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        // Send the negative button event back to the host activity
                        listener.onDeleteGalleryDialogNegativeClick(DeleteGalleryDialog.this);
                    }
                });

        return builder.create();
    }

    public interface DeleteGalleryDialogListener {
        void onDeleteGalleryDialogPositiveClick(DeleteGalleryDialog dialog);

        void onDeleteGalleryDialogNegativeClick(DeleteGalleryDialog dialog);
    }
}