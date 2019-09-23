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
import android.widget.TextView;


/**
 * A simple {@link Fragment} subclass.
 */
public class InsertPhotoDialog extends DialogFragment {

    private insertPhotoDialogListener listener;

    public static InsertPhotoDialog newInstance() {

        InsertPhotoDialog dialog = new InsertPhotoDialog();
        return dialog;
    }

    public void setListener(insertPhotoDialogListener listener) {
        this.listener = listener;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {

        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());

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
        public void onInsertPhotoDialogPositiveClick(InsertPhotoDialog dialog);

        public void onInsertPhotoDialogNeutralClick(InsertPhotoDialog dialog);

        public void onInsertPhotoDialogNegativeClick(InsertPhotoDialog dialog);
    }
}
