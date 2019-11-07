package ca.mohawk.HealthMetrics.Prescription;


import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.Objects;

import ca.mohawk.HealthMetrics.R;


/**
 * A simple {@link Fragment} subclass.
 */

    public class DeletePrescriptionDialog extends DialogFragment {

        private DeletePrescriptionDialogListener listener;
        private static int PrescriptionId;

        public static DeletePrescriptionDialog newInstance(int prescriptionId) {
            PrescriptionId = prescriptionId;
            return new DeletePrescriptionDialog();
        }

    public int getPrescriptionId() {
        return PrescriptionId;
    }

    @Override
        public void onAttach(@NonNull Context context) {
            super.onAttach(context);
            // Verify that the host activity implements the callback interface
            try {
                // Instantiate the NoticeDialogListener so we can send events to the host
                listener = (DeletePrescriptionDialogListener) context;
            } catch (ClassCastException e) {
                // The activity doesn't implement the interface, throw exception
                throw new ClassCastException(getActivity().toString()
                        + " must implement DeletePrescriptionDialogListener");
            }
        }

        @NonNull
        @Override
        public Dialog onCreateDialog(Bundle savedInstanceState) {

            AlertDialog.Builder builder = new AlertDialog.Builder(Objects.requireNonNull(getActivity()));

            builder.setTitle("Delete Prescription");
            builder.setMessage("The prescription will be deleted.");

            builder.setPositiveButton("Delete", new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int id) {
                    // Send the positive button event back to the host activity
                    listener.onDeletePrescriptionDialogPositiveClick(DeletePrescriptionDialog.this);
                }
            })
                    .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {
                            // Send the negative button event back to the host activity
                            listener.onDeletePrescriptionDialogNegativeClick(DeletePrescriptionDialog.this);
                        }
                    });

            return builder.create();
        }

        public interface DeletePrescriptionDialogListener {
            void onDeletePrescriptionDialogPositiveClick(DeletePrescriptionDialog dialog);

            void onDeletePrescriptionDialogNegativeClick(DeletePrescriptionDialog dialog);
        }
    }
