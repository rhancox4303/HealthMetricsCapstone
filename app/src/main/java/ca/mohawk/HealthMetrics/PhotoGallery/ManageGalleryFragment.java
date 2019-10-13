package ca.mohawk.HealthMetrics.PhotoGallery;


import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import ca.mohawk.HealthMetrics.HealthMetricsDbHelper;
import ca.mohawk.HealthMetrics.Models.PhotoGallery;
import ca.mohawk.HealthMetrics.R;


/**
 * A simple {@link Fragment} subclass.
 */
public class ManageGalleryFragment extends Fragment implements View.OnClickListener {


    private int GalleryId;
    private HealthMetricsDbHelper healthMetricsDbHelper;

    public ManageGalleryFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootView = inflater.inflate(R.layout.fragment_manage_gallery, container, false);
        healthMetricsDbHelper = HealthMetricsDbHelper.getInstance(getActivity());

        Bundle bundle = this.getArguments();
        if (bundle != null) {
            GalleryId = bundle.getInt("selected_gallery_key", -1);
        }

        TextView galleryNameTextView = rootView.findViewById(R.id.textViewGalleryNameManageGallery);

        Button deleteGalleryButton = rootView.findViewById(R.id.buttonDeleteGalleryManageGallery);
        Button editGalleryButton = rootView.findViewById(R.id.buttonEditGalleryManageGallery);

        deleteGalleryButton.setOnClickListener(this);
        editGalleryButton.setOnClickListener(this);

        PhotoGallery gallery = healthMetricsDbHelper.getPhotoGalleryById(GalleryId);
        galleryNameTextView.setText(gallery.Name);

        return rootView;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.buttonDeleteGalleryManageGallery:
                break;
            case R.id.buttonEditGalleryManageGallery:

                Fragment destinationFragment = new EditGalleryFragment();
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
