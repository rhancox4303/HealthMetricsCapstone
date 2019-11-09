package ca.mohawk.HealthMetrics.PhotoGallery;


import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.Fragment;

import com.bumptech.glide.Glide;

import java.util.Objects;

import ca.mohawk.HealthMetrics.HealthMetricsDbHelper;
import ca.mohawk.HealthMetrics.MetricManagement.MetricsListFragment;
import ca.mohawk.HealthMetrics.Models.PhotoEntry;
import ca.mohawk.HealthMetrics.R;


/**
 * The ManageGalleryFragment class is an extension of the Fragment class.
 * <p>
 * Allows the user to view a photo entry.
 */
public class ViewPhotoEntryFragment extends Fragment implements View.OnClickListener {

    private int photoId;
    private PhotoEntry photoEntry;

    public ViewPhotoEntryFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        // Inflate the layout for this fragment
        View rootView = inflater.inflate(R.layout.fragment_view_photo_entry, container, false);

        // Get the views
        ImageView imageView = rootView.findViewById(R.id.imageViewPhotoEntry);
        TextView dateOfEntryTextView = rootView.findViewById(R.id.textViewDateOfEntryViewPhotoEntry);

        Button deleteEntryButton = rootView.findViewById(R.id.buttonDeleteEntryViewPhotoEntry);
        Button editEntryButton = rootView.findViewById(R.id.buttonEditEntryViewPhotoEntry);

        deleteEntryButton.setOnClickListener(this);
        editEntryButton.setOnClickListener(this);

        // Get the healthMetricsDbHelper.
        HealthMetricsDbHelper healthMetricsDbHelper = HealthMetricsDbHelper.getInstance(getActivity());

        // Get the photo id from the bundle.
        Bundle bundle = this.getArguments();
        if (bundle != null) {
            photoId = bundle.getInt("selected_photo_key", -1);
        }

        // Get the photo entry from the database.
        photoEntry = healthMetricsDbHelper.getPhotoEntryById(photoId);

        // Validate the photoEntry is not null.
        if (photoEntry != null) {

            dateOfEntryTextView.setText(photoEntry.dateOfEntry);

            // Load the photo entry image with using the Glide library.
            Glide.with(Objects.requireNonNull(getActivity()))
                    .load(photoEntry.photoEntryPath)
                    .fitCenter()
                    .into(imageView);
        } else {

            Toast.makeText(getActivity(), "Error getting the Photo entry from the database.",
                    Toast.LENGTH_SHORT).show();
            navigateToMetricsListFragment();
        }

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
        if (v.getId() == R.id.buttonEditEntryViewPhotoEntry) {
            Fragment destinationFragment = new EditPhotoEntryFragment();
            Bundle galleryBundle = new Bundle();
            galleryBundle.putInt("selected_photo_key", photoId);
            destinationFragment.setArguments(galleryBundle);

            Objects.requireNonNull(getActivity()).getSupportFragmentManager().beginTransaction()
                    .replace(R.id.fragmentContainer, destinationFragment)
                    .addToBackStack(null)
                    .commit();

        } else if (v.getId() == R.id.buttonDeleteEntryViewPhotoEntry) {
            DialogFragment deletePhotoEntryDialog = DeletePhotoEntryDialog.newInstance(photoEntry);
            deletePhotoEntryDialog.show(Objects.requireNonNull(getFragmentManager()), "deletePhotoEntryDialog");
        }
    }
}