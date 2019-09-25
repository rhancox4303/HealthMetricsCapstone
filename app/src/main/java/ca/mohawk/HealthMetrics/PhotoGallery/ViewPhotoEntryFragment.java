package ca.mohawk.HealthMetrics.PhotoGallery;


import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;

import org.w3c.dom.Text;

import ca.mohawk.HealthMetrics.HealthMetricsDbHelper;
import ca.mohawk.HealthMetrics.Models.PhotoEntry;
import ca.mohawk.HealthMetrics.R;


/**
 * A simple {@link Fragment} subclass.
 */
public class ViewPhotoEntryFragment extends Fragment implements View.OnClickListener {
    private HealthMetricsDbHelper healthMetricsDbHelper;
    private int PhotoId;


    public ViewPhotoEntryFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment

        healthMetricsDbHelper = HealthMetricsDbHelper.getInstance(getActivity());

        Bundle bundle = this.getArguments();
        if (bundle != null) {
            PhotoId = bundle.getInt("selected_photo_key", -1);
        }

        PhotoEntry photoEntry = healthMetricsDbHelper.getPhotoEntryById(PhotoId);

        View rootView = inflater.inflate(R.layout.fragment_view_photo_entry, container, false);
        ImageView imageView = rootView.findViewById(R.id.imageViewPhotoEntry);
        TextView dateOfEntryTextView = rootView.findViewById(R.id.textViewDateOfEntryViewPhotoEntry);
        Button deleteEntryButton = rootView.findViewById(R.id.buttonDeleteEntryViewPhotoEntry);
        Button editEntryButton = rootView.findViewById(R.id.buttonEditEntryViewPhotoEntry);

        deleteEntryButton.setOnClickListener(this);
        editEntryButton.setOnClickListener(this);


        dateOfEntryTextView.setText(photoEntry.DateOfEntry);

        Glide.with(getActivity())
                .load(photoEntry.PhotoEntryPath)
                .fitCenter()
                .into(imageView);

        return rootView;
    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.buttonEditEntryViewPhotoEntry) {
            Fragment destinationFragment = new EditPhotoEntryFragment();
            Bundle galleryBundle = new Bundle();
            galleryBundle.putInt("selected_photo_key", PhotoId);
            destinationFragment.setArguments(galleryBundle);

            getActivity().getSupportFragmentManager().beginTransaction()
                    .replace(R.id.fragmentContainer, destinationFragment)
                    .addToBackStack(null)
                    .commit();
        }
    }
}
