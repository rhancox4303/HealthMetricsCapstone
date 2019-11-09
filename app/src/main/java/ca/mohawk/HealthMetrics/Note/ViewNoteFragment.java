package ca.mohawk.HealthMetrics.Note;


import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import java.util.Objects;

import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.Fragment;
import ca.mohawk.HealthMetrics.HealthMetricsDbHelper;
import ca.mohawk.HealthMetrics.MetricManagement.MetricsListFragment;
import ca.mohawk.HealthMetrics.Models.Note;
import ca.mohawk.HealthMetrics.R;

/**
 * The ViewNoteFragment class is an extension of the Fragment class.
 * Allows the user to view a note.
 */
public class ViewNoteFragment extends Fragment implements View.OnClickListener {

    // Instantiate the note id.
    private int noteId;

    public ViewNoteFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        // Inflate the layout for this fragment
        View rootView = inflater.inflate(R.layout.fragment_view_note, container, false);

        // Get the healthMetricsDbHelper.
        HealthMetricsDbHelper healthMetricsDbHelper = HealthMetricsDbHelper.getInstance(getActivity());

        // Get the views.
        Button editNoteButton = rootView.findViewById(R.id.buttonEditNoteViewNote);
        Button deleteNoteButton = rootView.findViewById(R.id.buttonDeleteNoteViewNote);

        TextView noteContentTextView = rootView.findViewById(R.id.textViewNoteContentViewNote);
        TextView dateOfEntryTextView = rootView.findViewById(R.id.textViewDateOfEntryDisplayViewNote);

        // Set the OnClickListeners
        editNoteButton.setOnClickListener(this);
        deleteNoteButton.setOnClickListener(this);

        // Get the note id from the passed bundle.
        Bundle bundle = this.getArguments();

        if (bundle != null) {
            noteId = bundle.getInt("selected_item_key", -1);
        }

        // Get the note from the database.
        Note note = healthMetricsDbHelper.getNoteById(noteId);

        // Validate the note is not null.
        if (note != null) {
            noteContentTextView.setText(note.noteContent);
            dateOfEntryTextView.setText(note.dateOfEntry);
        } else {
            Toast.makeText(getActivity(), "Cannot get note from database.",
                    Toast.LENGTH_SHORT).show();
            navigateToMetricsListFragment();
        }

        // Return rootView.
        return rootView;
    }

    /**
     * Replaces the current fragment with a MetricsListFragment.
     */
    private void navigateToMetricsListFragment() {

        MetricsListFragment destinationFragment = new MetricsListFragment();

        Objects.requireNonNull(getActivity()).getSupportFragmentManager().beginTransaction()
                .replace(R.id.fragmentContainer, destinationFragment)
                .addToBackStack(null)
                .commit();
    }

    /**
     * Runs when a view's onClickListener is activated.
     *
     * @param v Represents the view.
     */
    @Override
    public void onClick(View v) {

        if (v.getId() == R.id.buttonDeleteNoteViewNote) {
            DialogFragment deleteNoteDialog = DeleteNoteDialog.newInstance(noteId);
            deleteNoteDialog.show(Objects.requireNonNull(getFragmentManager()), "deleteNoteDialog");

        } else if (v.getId() == R.id.buttonEditNoteViewNote) {

            Bundle bundle = new Bundle();
            bundle.putInt("note_id_key", noteId);
            Fragment fragment = new EditNoteFragment();
            fragment.setArguments(bundle);

            Objects.requireNonNull(getActivity()).getSupportFragmentManager().beginTransaction()
                    .replace(R.id.fragmentContainer, fragment)
                    .addToBackStack(null)
                    .commit();
        }
    }
}
