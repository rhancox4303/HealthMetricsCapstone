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
 * A simple {@link Fragment} subclass.
 */
public class CreatePhotoGalleryInputFragment extends Fragment implements View.OnClickListener {

    // The HealthMetricsDbHelper healthMetricsDbHelper is used to access the SQLite database.
    private HealthMetricsDbHelper healthMetricsDbHelper;

    // Initialize the galleryNameEditText.
    private EditText galleryNameEditText;

    public CreatePhotoGalleryInputFragment() {
        // Required empty public constructor
    }

    /**
     * The onCreateView method initializes the view variables
     * and the HealthMetricsDbHelper object when the Fragment view is created.
     */
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        // Inflate the layout for this fragment
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

    private void createGallery() {

        if(validateUserInput()){

            String name = galleryNameEditText.getText().toString();
            if(healthMetricsDbHelper.addPhotoGallery(new PhotoGallery(name, 0))){
                Toast.makeText(getActivity(), "Gallery created.", Toast.LENGTH_SHORT).show();

                AddMetricFragment addMetricFragment = new AddMetricFragment();

                Objects.requireNonNull(getActivity()).getSupportFragmentManager().beginTransaction()
                        .replace(R.id.fragmentContainer, addMetricFragment)
                        .addToBackStack(null)
                        .commit();
            }else{
                Toast.makeText(getActivity(), "Failed to create gallery.", Toast.LENGTH_SHORT).show();
            }
        }
    }

    public boolean validateUserInput(){

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

    @Override
    public void onClick(View v) {
            createGallery();
    }
}
