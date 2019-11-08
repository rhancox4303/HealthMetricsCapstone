package ca.mohawk.HealthMetrics.PhotoGallery;


import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;

import java.util.Objects;

import androidx.annotation.NonNull;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.Fragment;
import ca.mohawk.HealthMetrics.HealthMetricsDbHelper;
import ca.mohawk.HealthMetrics.Models.PhotoEntry;
import ca.mohawk.HealthMetrics.R;


/**
 * A simple {@link Fragment} subclass.
 */
public class ViewPhotoEntryFragment extends Fragment implements View.OnClickListener {

    private int PhotoId;
    private PhotoEntry PhotoEntry;

    public ViewPhotoEntryFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootView = inflater.inflate(R.layout.fragment_view_photo_entry, container, false);
        HealthMetricsDbHelper healthMetricsDbHelper = HealthMetricsDbHelper.getInstance(getActivity());

        Bundle bundle = this.getArguments();
        if (bundle != null) {
            PhotoId = bundle.getInt("selected_photo_key", -1);
        }

        PhotoEntry = healthMetricsDbHelper.getPhotoEntryById(PhotoId);

        if (PhotoEntry != null) {
            ImageView imageView = rootView.findViewById(R.id.imageViewPhotoEntry);
            TextView dateOfEntryTextView = rootView.findViewById(R.id.textViewDateOfEntryViewPhotoEntry);

            Button deleteEntryButton = rootView.findViewById(R.id.buttonDeleteEntryViewPhotoEntry);
            Button editEntryButton = rootView.findViewById(R.id.buttonEditEntryViewPhotoEntry);

            deleteEntryButton.setOnClickListener(this);
            editEntryButton.setOnClickListener(this);

            dateOfEntryTextView.setText(PhotoEntry.dateOfEntry);

            Glide.with(Objects.requireNonNull(getActivity()))
                    .load(PhotoEntry.photoEntryPath)
                    .fitCenter()
                    .into(imageView);
        } else {

            //Go to fragment.
        }

        return rootView;
    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.buttonEditEntryViewPhotoEntry) {
            Fragment destinationFragment = new EditPhotoEntryFragment();
            Bundle galleryBundle = new Bundle();
            galleryBundle.putInt("selected_photo_key", PhotoId);
            destinationFragment.setArguments(galleryBundle);

            Objects.requireNonNull(getActivity()).getSupportFragmentManager().beginTransaction()
                    .replace(R.id.fragmentContainer, destinationFragment)
                    .addToBackStack(null)
                    .commit();

        } else if (v.getId() == R.id.buttonDeleteEntryViewPhotoEntry) {
            DialogFragment deletePhotoEntryDialog = DeletePhotoEntryDialog.newInstance(PhotoEntry);
            deletePhotoEntryDialog.show(Objects.requireNonNull(getFragmentManager()), "deletePhotoEntryDialog");
        }
    }
}