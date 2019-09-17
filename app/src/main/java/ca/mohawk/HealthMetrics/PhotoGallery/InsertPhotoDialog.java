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

import ca.mohawk.HealthMetrics.R;

/**
 * A simple {@link Fragment} subclass.
 */
public class InsertPhotoDialog extends DialogFragment {

    public static InsertPhotoDialog newInstance() {
        InsertPhotoDialog dialog = new InsertPhotoDialog();
        return dialog;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {

        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());

        builder.setTitle("Insert Photo Entry");
        builder.setMessage("Insert a photo entry");

        builder.setPositiveButton("Use Camera", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {

            }
        });

        builder.setNegativeButton("From Gallery", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {

            }
        });

        builder.setNeutralButton("", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });

        return builder.create();
    }
}
