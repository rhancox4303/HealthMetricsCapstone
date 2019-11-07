package ca.mohawk.HealthMetrics.PhotoGallery;


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
public class EditGalleryFragment extends Fragment implements View.OnClickListener {

    private HealthMetricsDbHelper healthMetricsDbHelper;
    private int GalleryId;
    private EditText nameEditText;
    private PhotoGallery Gallery;

    public EditGalleryFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        // Inflate the layout for this fragment
        View rootView = inflater.inflate(R.layout.fragment_edit_gallery, container, false);
        healthMetricsDbHelper = HealthMetricsDbHelper.getInstance(getActivity());

        Bundle bundle = this.getArguments();
        if (bundle != null) {
            GalleryId = bundle.getInt("selected_gallery_key", -1);
        }


        Gallery = healthMetricsDbHelper.getPhotoGalleryById(GalleryId);

        if (Gallery != null) {
            nameEditText = rootView.findViewById(R.id.editTextGalleryNameEditGallery);
            nameEditText.setText(Gallery.Name);

            Button editGalleryButton = rootView.findViewById(R.id.buttonEditGallery);
            editGalleryButton.setOnClickListener(this);
        } else {
            //Leave fragment.
        }

        return rootView;
    }

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

    public void editGallery() {
        if (validateUserInput()) {

            String galleryName = nameEditText.getText().toString();
            PhotoGallery gallery = new PhotoGallery(GalleryId, galleryName, Gallery.IsAddedToProfile);

            if (healthMetricsDbHelper.updateGallery(gallery)) {
                Fragment destinationFragment = new ManageGalleryFragment();
                Bundle galleryBundle = new Bundle();
                galleryBundle.putInt("selected_gallery_key", GalleryId);
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

    @Override
    public void onClick(View v) {
        editGallery();
    }
}
