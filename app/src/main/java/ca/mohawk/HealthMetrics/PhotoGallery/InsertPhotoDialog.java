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
public class InsertPhotoDialog extends DialogFragment {

    private insertPhotoDialogListener listener;

    public static InsertPhotoDialog newInstance() {

        return new InsertPhotoDialog();
    }

    void setListener(insertPhotoDialogListener listener) {
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

        builder.setTitle("Insert Photo Entry");

        builder.setPositiveButton("Camera", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                // Send the positive button event back to the host activity
                listener.onInsertPhotoDialogPositiveClick(InsertPhotoDialog.this);
            }
        })
                .setNeutralButton("Gallery", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        // Send the positive button event back to the host activity
                        listener.onInsertPhotoDialogNeutralClick(InsertPhotoDialog.this);
                    }
                })
                .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        // Send the negative button event back to the host activity
                        listener.onInsertPhotoDialogNegativeClick(InsertPhotoDialog.this);
                    }
                });

        return builder.create();
    }

    public interface insertPhotoDialogListener {
        void onInsertPhotoDialogPositiveClick(InsertPhotoDialog dialog);

        void onInsertPhotoDialogNeutralClick(InsertPhotoDialog dialog);

        void onInsertPhotoDialogNegativeClick(InsertPhotoDialog dialog);
    }
}
