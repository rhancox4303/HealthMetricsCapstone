package ca.mohawk.HealthMetrics.MetricManagement;


import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import ca.mohawk.HealthMetrics.HealthMetricsDbHelper;
import ca.mohawk.HealthMetrics.Models.PhotoGallery;
import ca.mohawk.HealthMetrics.R;

/**
 * A simple {@link Fragment} subclass.
 */
public class CreatePhotoGalleryInputFragment extends Fragment implements View.OnClickListener {

    private HealthMetricsDbHelper healthMetricsDbHelper;
    EditText galleryNameEditText;

    public CreatePhotoGalleryInputFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        healthMetricsDbHelper = HealthMetricsDbHelper.getInstance(getActivity());

        // Inflate the layout for this fragment
        View rootView = inflater.inflate(R.layout.fragment_create_photo_gallery_input, container, false);

        Button createMetricButton = rootView.findViewById(R.id.buttonCreateGallery);
        createMetricButton.setOnClickListener(this);

        galleryNameEditText = rootView.findViewById(R.id.editTextGalleryNameCreatePhotoGalleryInput);

        return rootView;
    }

    public void createGallery(){
        String name = galleryNameEditText.getText().toString();
        healthMetricsDbHelper.addPhotoGallery(new PhotoGallery(name,0));
    }

    @Override
    public void onClick(View v) {
        if(!galleryNameEditText.getText().toString().trim().equals("")){
            createGallery();

            AddMetricFragment addMetricFragment= new AddMetricFragment();
            getActivity().getSupportFragmentManager().beginTransaction()
                    .replace(R.id.fragmentContainer, addMetricFragment)
                    .addToBackStack(null)
                    .commit();
        }else{
            Toast.makeText(getActivity(), "Please enter a gallery name", Toast.LENGTH_SHORT).show();
        }
    }
}
