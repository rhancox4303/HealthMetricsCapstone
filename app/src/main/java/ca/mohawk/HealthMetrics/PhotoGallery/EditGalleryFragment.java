package ca.mohawk.HealthMetrics.PhotoGallery;


import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import ca.mohawk.HealthMetrics.HealthMetricContract;
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
        nameEditText = rootView.findViewById(R.id.editTextGalleryNameEditGallery);
        nameEditText.setText(Gallery.Name);

        Button editGalleryButton = rootView.findViewById(R.id.buttonEditGallery);
        editGalleryButton.setOnClickListener(this);

        return rootView;
    }

    private boolean validateUserInput() {
        if (nameEditText.getText().toString().trim().equals("")) {
            Toast.makeText(getActivity(), "Please fill all fields.", Toast.LENGTH_SHORT).show();
            return false;
        }
        return true;
    }

    @Override
    public void onClick(View v) {
        if(validateUserInput()){
            String galleryName = nameEditText.getText().toString();
            PhotoGallery gallery = new PhotoGallery(GalleryId,galleryName,Gallery.IsAddedToProfile);
            healthMetricsDbHelper.updateGallery(gallery);

            Fragment destinationFragment = new ManageGalleryFragment();
            Bundle galleryBundle = new Bundle();
            galleryBundle.putInt("selected_gallery_key", GalleryId);
            destinationFragment.setArguments(galleryBundle);

            getActivity().getSupportFragmentManager().beginTransaction()
                    .replace(R.id.fragmentContainer, destinationFragment)
                    .addToBackStack(null)
                    .commit();
        }
    }
}
