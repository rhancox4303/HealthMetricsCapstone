package ca.mohawk.HealthMetrics.Note;


import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;

import java.util.Objects;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.DialogFragment;


/**
 * The DeleteNoteDialog extends the DialogFragment.
 * Presents a dialog to the user indicating note deletion options.
 */
public class DeleteNoteDialog extends DialogFragment {

    // Instantiate the noteId.
    private static int noteId;

    // Instantiate the DeleteNoteDialogListener.
    private DeleteNoteDialogListener listener;

    /**
     * Create a new instance of the DeleteNoteDialog.
     *
     * @param noteId Represents the note id.
     * @return Returns a new DeleteNoteDialog.
     */
    public static DeleteNoteDialog newInstance(int noteId) {
        DeleteNoteDialog.noteId = noteId;
        return new DeleteNoteDialog();
    }

    public int getNoteId() {
        return noteId;
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        try {
            listener = (DeleteNoteDialogListener) context;
        } catch (ClassCastException e) {
            throw new ClassCastException(getActivity().toString()
                    + " must implement DeleteNoteDialogListener");
        }
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {

        AlertDialog.Builder builder = new AlertDialog.Builder(Objects.requireNonNull(getActivity()));

        // Set the title and message.
        builder.setTitle("Delete Note");
        builder.setMessage("The note will be deleted.");

        // Set the PositiveButton and it's OnClickListener.
        builder.setPositiveButton("Delete", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                listener.onDeleteNoteDialogPositiveClick(DeleteNoteDialog.this);
            }
        });

        // Set the NegativeButton and it's OnClickListener.
        builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                listener.onDeleteNoteDialogNegativeClick(DeleteNoteDialog.this);
            }
        });

        // Return the created alert dialog.
        return builder.create();
    }

    /**
     * The DeleteNoteDialogListener models the method that will handle the button clicks.
     */
    public interface DeleteNoteDialogListener {
        /**
         * Handles a DeleteNoteDialog positive click.
         *
         * @param dialog Represents the DeleteNoteDialog.
         */
        void onDeleteNoteDialogPositiveClick(DeleteNoteDialog dialog);

        /**
         * Handles a DeleteNoteDialog negative click.
         *
         * @param dialog Represents the DeleteNoteDialog.
         */
        void onDeleteNoteDialogNegativeClick(DeleteNoteDialog dialog);
    }
}