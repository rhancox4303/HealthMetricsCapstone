package ca.mohawk.HealthMetrics.PhotoGallery;


import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.fragment.app.Fragment;

import java.util.Objects;

import ca.mohawk.HealthMetrics.HealthMetricsDbHelper;
import ca.mohawk.HealthMetrics.MetricManagement.MetricsListFragment;
import ca.mohawk.HealthMetrics.Models.PhotoGallery;
import ca.mohawk.HealthMetrics.R;

/**
 * The ManageGalleryFragment class is an extension of the Fragment class.
 * <p>
 * Allows the user to manage a gallery.
 */
public class ManageGalleryFragment extends Fragment implements View.OnClickListener {


    //Initialize the gallery id.
    private int galleryId;

    public ManageGalleryFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        // Inflate the layout for this fragment
        View rootView = inflater.inflate(R.layout.fragment_manage_gallery, container, false);

        // Get the healthMetricsDbHelper.
        HealthMetricsDbHelper healthMetricsDbHelper = HealthMetricsDbHelper.getInstance(getActivity());

        // Get the gallery id from the bundles.
        Bundle bundle = this.getArguments();
        if (bundle != null) {
            galleryId = bundle.getInt("selected_gallery_key", -1);
        }

        // Get the views
        TextView galleryNameTextView = rootView.findViewById(R.id.textViewGalleryNameManageGallery);
        Button deleteGalleryButton = rootView.findViewById(R.id.buttonDeleteGalleryManageGallery);
        Button editGalleryButton = rootView.findViewById(R.id.buttonEditGalleryManageGallery);

        deleteGalleryButton.setOnClickListener(this);
        editGalleryButton.setOnClickListener(this);

        // Get the gallery from the database.
        PhotoGallery gallery = healthMetricsDbHelper.getPhotoGalleryById(galleryId);

        // Validate the gallery is not null.
        if (gallery != null) {
            galleryNameTextView.setText(gallery.name);
        } else {
            Toast.makeText(getActivity(), "Error getting the gallery from the database.",
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
        switch (v.getId()) {
            case R.id.buttonDeleteGalleryManageGallery:
                DeleteGalleryDialog deleteGalleryDialog = DeleteGalleryDialog.newInstance(galleryId);
                deleteGalleryDialog.show(Objects.requireNonNull(getFragmentManager()), "deleteGalleryDialog");
                break;

            case R.id.buttonEditGalleryManageGallery:
                Fragment destinationFragment = new EditGalleryFragment();
                Bundle galleryBundle = new Bundle();
                galleryBundle.putInt("selected_gallery_key", galleryId);
                destinationFragment.setArguments(galleryBundle);

                Objects.requireNonNull(getActivity()).getSupportFragmentManager().beginTransaction()
                        .replace(R.id.fragmentContainer, destinationFragment)
                        .addToBackStack(null)
                        .commit();
        }
    }
}