package ca.mohawk.HealthMetrics.Note;


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
public class DeleteNoteDialog extends DialogFragment {

    private DeleteNoteDialog.DeleteNoteDialogListener listener;
    private static int NoteId;

    public static DeleteNoteDialog newInstance(int noteId) {
        NoteId = noteId;

        return new DeleteNoteDialog();
    }

    public int getNoteId() {
        return NoteId;
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        // Verify that the host activity implements the callback interface
        try {
            // Instantiate the NoticeDialogListener so we can send events to the host
            listener = (DeleteNoteDialogListener) context;
        } catch (ClassCastException e) {
            // The activity doesn't implement the interface, throw exception
            throw new ClassCastException(getActivity().toString()
                    + " must implement DeleteNoteDialogListener");
        }
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {

        AlertDialog.Builder builder = new AlertDialog.Builder(Objects.requireNonNull(getActivity()));

        builder.setTitle("Delete Note");
        builder.setMessage("The note will be deleted.");

        builder.setPositiveButton("Delete", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                // Send the positive button event back to the host activity
                listener.onDeleteNoteDialogPositiveClick(DeleteNoteDialog.this);
            }
        })
                .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        // Send the negative button event back to the host activity
                        listener.onDeleteNoteDialogNegativeClick(DeleteNoteDialog.this);
                    }
                });

        return builder.create();
    }

    public interface DeleteNoteDialogListener {
        void onDeleteNoteDialogPositiveClick(DeleteNoteDialog dialog);

        void onDeleteNoteDialogNegativeClick(DeleteNoteDialog dialog);
    }
}

