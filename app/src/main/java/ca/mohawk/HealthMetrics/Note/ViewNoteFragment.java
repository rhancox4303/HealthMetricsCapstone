package ca.mohawk.HealthMetrics.Note;


import android.os.Bundle;

import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import ca.mohawk.HealthMetrics.DataEntry.DeleteDataEntryDialog;
import ca.mohawk.HealthMetrics.HealthMetricsDbHelper;
import ca.mohawk.HealthMetrics.Models.Note;
import ca.mohawk.HealthMetrics.R;


/**
 * A simple {@link Fragment} subclass.
 */
public class ViewNoteFragment extends Fragment implements View.OnClickListener {
    private HealthMetricsDbHelper healthMetricsDbHelper;
    private int NoteId;
    public ViewNoteFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        healthMetricsDbHelper = healthMetricsDbHelper.getInstance(getActivity());
        View rootView = inflater.inflate(R.layout.fragment_view_note, container, false);

        Button editNoteButton = rootView.findViewById(R.id.buttonEditNoteViewNote);
        Button deleteNoteButton = rootView.findViewById(R.id.buttonDeleteNoteViewNote);

        editNoteButton.setOnClickListener(this);
        deleteNoteButton.setOnClickListener(this);

        TextView noteContentTextView= rootView.findViewById(R.id.textViewNoteContentViewNote);
        TextView dateOfEntryTextView = rootView.findViewById(R.id.textViewDateOfEntryDisplayViewNote);

        Bundle bundle = this.getArguments();
        if (bundle != null) {
            NoteId = bundle.getInt("selected_item_key", -1);
        }

        Note note = healthMetricsDbHelper.getNoteById(NoteId);
        noteContentTextView.setText(note.NoteContent);
        dateOfEntryTextView.setText(note.DateOfEntry);
        return rootView;
    }

    @Override
    public void onClick(View v) {
        if(v.getId() == R.id.buttonDeleteNoteViewNote){
            DialogFragment deleteNoteDialog = DeleteNoteDialog.newInstance(NoteId);
            deleteNoteDialog.show(getFragmentManager(), "dialog");
        }else if(v.getId() == R.id.buttonEditNoteViewNote){

            Bundle bundle = new Bundle();
            bundle.putInt("note_id_key",NoteId);
            Fragment fragment = new EditNoteFragment();
            fragment.setArguments(bundle);

            getActivity().getSupportFragmentManager().beginTransaction()
                    .replace(R.id.fragmentContainer, fragment)
                    .addToBackStack(null)
                    .commit();
        }
    }
}
