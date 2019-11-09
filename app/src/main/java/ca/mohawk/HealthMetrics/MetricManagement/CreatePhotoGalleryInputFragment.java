package ca.mohawk.HealthMetrics.MetricManagement;


import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Objects;

import androidx.fragment.app.Fragment;
import ca.mohawk.HealthMetrics.HealthMetricsDbHelper;
import ca.mohawk.HealthMetrics.Models.PhotoGallery;
import ca.mohawk.HealthMetrics.R;

/**
 * CreatePhotoGalleryInputFragment extends the Fragment class.
 * Allows the user to create photo galleries.
 */
public class CreatePhotoGalleryInputFragment extends Fragment implements View.OnClickListener {

    // Initialize the healthMetricsDbHelper.
    private HealthMetricsDbHelper healthMetricsDbHelper;

    // Initialize the galleryNameEditText.
    private EditText galleryNameEditText;

    public CreatePhotoGalleryInputFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        // Inflate the layout for this fragment.
        View rootView = inflater.inflate(R.layout.fragment_create_photo_gallery_input, container, false);

        // Instantiate the healthMetricsDbHelper.
        healthMetricsDbHelper = HealthMetricsDbHelper.getInstance(getActivity());

        // Instantiate the createGalleryButton.
        Button createGalleryButton = rootView.findViewById(R.id.buttonCreateGallery);

        // Set the OnClickListener.
        createGalleryButton.setOnClickListener(this);

        // Initialize the galleryNameEditText.
        galleryNameEditText = rootView.findViewById(R.id.editTextGalleryNameCreatePhotoGalleryInput);

        // Return the rootView.
        return rootView;
    }

    /**
     * Creates the gallery in the database.
     */
    private void createGallery() {

        // Validate the user input.
        if (validateUserInput()) {

            String name = galleryNameEditText.getText().toString();

            // Add the gallery to the database and verify it was successful.
            if (healthMetricsDbHelper.addPhotoGallery(new PhotoGallery(name, 0))) {

                AddMetricFragment addMetricFragment = new AddMetricFragment();

                Objects.requireNonNull(getActivity()).getSupportFragmentManager().beginTransaction()
                        .replace(R.id.fragmentContainer, addMetricFragment)
                        .addToBackStack(null)
                        .commit();
            } else {
                Toast.makeText(getActivity(), "Failed to create gallery.", Toast.LENGTH_SHORT).show();
            }
        }
    }

    /**
     * Validates the user inputs.
     *
     * @return Return a boolean based on whether the user input is valid.
     */
    public boolean validateUserInput() {

        // Get the user inputted gallery name.
        String galleryName = galleryNameEditText.getText().toString().trim();

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
            Toast.makeText(getActivity(), "The gallery name may only contain letters and numbers", Toast.LENGTH_SHORT).show();
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
     * Runs when a view's onClickListener is activated.
     *
     * @param v Represents the view.
     */
    @Override
    public void onClick(View v) {
        createGallery();
    }
}
