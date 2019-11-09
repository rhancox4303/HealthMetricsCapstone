package ca.mohawk.HealthMetrics.PhotoGallery;


import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;
import java.util.Objects;

import ca.mohawk.HealthMetrics.Adapaters.PhotoGalleryRecyclerViewAdapter;
import ca.mohawk.HealthMetrics.HealthMetricsDbHelper;
import ca.mohawk.HealthMetrics.Models.PhotoEntry;
import ca.mohawk.HealthMetrics.R;


/**
 * The ViewPhotoGalleryFragment class is an extension of the Fragment class.
 * TheViewPhotoGalleryFragment displays all the photo entries a photo gallery.
 */
public class ViewPhotoGalleryFragment extends Fragment implements View.OnClickListener {

    // Initialize the gallery id.
    private int galleryId;

    public ViewPhotoGalleryFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {


        // Inflate the layout for this fragment
        View rootView = inflater.inflate(R.layout.fragment_view_photo_gallery, container, false);

        // Get the views.
        Button addEntryButton = rootView.findViewById(R.id.buttonAddEntryViewPhotoGallery);
        Button manageGalleryButton = rootView.findViewById(R.id.buttonManageGalleryViewPhotoGallery);

        addEntryButton.setOnClickListener(this);
        manageGalleryButton.setOnClickListener(this);

        RecyclerView photoGalleryRecyclerView = rootView.findViewById(R.id.recyclerviewPhotoGallery);

        // Get the healthMetricsDbHelper.
        HealthMetricsDbHelper healthMetricsDbHelper = HealthMetricsDbHelper.getInstance(getActivity());

        // Get the galleryId from the bundle.
        Bundle bundle = this.getArguments();
        if (bundle != null) {
            galleryId = bundle.getInt("selected_item_key", -1);
        }

        // Get the list of photo entries from the database.
        List<PhotoEntry> photoEntries = healthMetricsDbHelper.getPhotoEntriesByGalleryId(galleryId);

        // Create and set the PhotoGalleryRecyclerViewAdapter.
        PhotoGalleryRecyclerViewAdapter adapter = new PhotoGalleryRecyclerViewAdapter(photoEntries, getActivity());
        photoGalleryRecyclerView.setAdapter(adapter);
        photoGalleryRecyclerView.setLayoutManager(new GridLayoutManager(getActivity(), 2));

        // Return rootView.
        return rootView;
    }

    /**
     * Runs when a view's onClickListener is activated.
     *
     * @param v Represents the view.
     */
    @Override
    public void onClick(View v) {
        Fragment destinationFragment = new Fragment();
        if (v.getId() == R.id.buttonAddEntryViewPhotoGallery) {
            destinationFragment = new AddPhotoEntryFragment();
        } else if (v.getId() == R.id.buttonManageGalleryViewPhotoGallery) {
            destinationFragment = new ManageGalleryFragment();
        }
        Bundle galleryBundle = new Bundle();
        galleryBundle.putInt("selected_gallery_key", galleryId);
        destinationFragment.setArguments(galleryBundle);

        Objects.requireNonNull(getActivity()).getSupportFragmentManager().beginTransaction()
                .replace(R.id.fragmentContainer, destinationFragment)
                .addToBackStack(null)
                .commit();
    }
}

