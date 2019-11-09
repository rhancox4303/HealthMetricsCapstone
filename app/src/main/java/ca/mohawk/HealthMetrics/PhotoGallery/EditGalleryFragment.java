package ca.mohawk.HealthMetrics.PhotoGallery;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.fragment.app.Fragment;

import java.util.ArrayList;
import java.util.Objects;

import ca.mohawk.HealthMetrics.HealthMetricsDbHelper;
import ca.mohawk.HealthMetrics.MetricManagement.MetricsListFragment;
import ca.mohawk.HealthMetrics.Models.PhotoGallery;
import ca.mohawk.HealthMetrics.R;

/**
 * EditGalleryFragment extends the Fragment class.
 * Allows the user to the edit a photo gallery.
 */
public class EditGalleryFragment extends Fragment implements View.OnClickListener {

    // Initialise the HealthMetricsDbHelper.
    private HealthMetricsDbHelper healthMetricsDbHelper;

    // Initialise the gallery id.
    private int galleryId;

    // Initialise the nameEditText.
    private EditText nameEditText;

    // Initialise the gallery.
    private PhotoGallery gallery;

    public EditGalleryFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        // Inflate the layout for this fragment
        View rootView = inflater.inflate(R.layout.fragment_edit_gallery, container, false);

        // Get the HealthMetricsDbHelper.
        healthMetricsDbHelper = HealthMetricsDbHelper.getInstance(getActivity());

        // Get the views.
        Button editGalleryButton = rootView.findViewById(R.id.buttonEditGallery);
        nameEditText = rootView.findViewById(R.id.editTextGalleryNameEditGallery);

        editGalleryButton.setOnClickListener(this);

        Bundle bundle = this.getArguments();

        // Get the gallery id from the bundle.
        if (bundle != null) {
            galleryId = bundle.getInt("selected_gallery_key", -1);
        }

        // Get gallery from the database.
        gallery = healthMetricsDbHelper.getPhotoGalleryById(galleryId);

        // Validate the gallery is not null.
        if (gallery != null) {
            nameEditText.setText(gallery.name);
        } else {
            Toast.makeText(getActivity(), "Failed to get gallery from database."
                    , Toast.LENGTH_SHORT).show();
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
     * Validates the user inputs.
     *
     * @return Return a boolean based on whether the user input is valid.
     */
    public boolean validateUserInput() {

        // Get the user inputted gallery name.
        String galleryName = nameEditText.getText().toString().trim();

        // Get all the gallery names from the database.
        ArrayList<String> galleryNameList = healthMetricsDbHelper.getAllGalleryNames();

        // If galleryName is empty then inform the user and return false.
        if (galleryName.equals("")) {
            Toast.makeText(getActivity(), "The gallery name cannot be empty.", Toast.LENGTH_SHORT).show();
            return false;
        }
        // If galleryName is greater than 25 then inform the user and return false.
        if (galleryName.length() > 25) {
            Toast.makeText(getActivity(), "Enter a gallery 25 characters or less.", Toast.LENGTH_SHORT).show();
            return false;
        }
        // If galleryName does not only contain numbers, digits and spaces. then inform the user and return false.
        if (!galleryName.matches("[a-zA-Z0-9 ]*")) {
            Toast.makeText(getActivity(), "The gallery name may only contain letters numbers and spaces.", Toast.LENGTH_SHORT).show();
            return false;
        }

        // If the gallery name already exists then inform the user and return false.
        if (galleryNameList.contains(galleryName.toLowerCase())) {
            Toast.makeText(getActivity(), galleryName + " already exists.", Toast.LENGTH_SHORT).show();
            return false;
        }
        return true;
    }

    /**
     * Updates the gallery in the database.
     */
    public void updateGallery() {

        // Validate the user input.
        if (validateUserInput()) {

            String galleryName = nameEditText.getText().toString();
            PhotoGallery gallery = new PhotoGallery(galleryId, galleryName, this.gallery.isAddedToProfile);

            // Validate the update was successful.
            if (healthMetricsDbHelper.updateGallery(gallery)) {
                Fragment destinationFragment = new ManageGalleryFragment();
                Bundle galleryBundle = new Bundle();
                galleryBundle.putInt("selected_gallery_key", galleryId);
                destinationFragment.setArguments(galleryBundle);

                Objects.requireNonNull(getActivity()).getSupportFragmentManager().beginTransaction()
                        .replace(R.id.fragmentContainer, destinationFragment)
                        .addToBackStack(null)
                        .commit();
            } else {
                Toast.makeText(getActivity(), "Failed to update the photo gallery.", Toast.LENGTH_SHORT).show();
            }
        }
    }

    /**
     * Runs when a view's onClickListener is activated.
     *
     * @param v Represents the view.
     */
    @Override
    public void onClick(View v) {
        updateGallery();
    }
}
