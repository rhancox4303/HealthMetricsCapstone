package ca.mohawk.HealthMetrics.PhotoGallery;


import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import java.util.List;

import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import ca.mohawk.HealthMetrics.Adapaters.PhotoGalleryRecyclerViewAdapter;
import ca.mohawk.HealthMetrics.HealthMetricContract;
import ca.mohawk.HealthMetrics.HealthMetricsDbHelper;
import ca.mohawk.HealthMetrics.Models.PhotoEntry;
import ca.mohawk.HealthMetrics.R;


/**
 * A simple {@link Fragment} subclass.
 */
public class ViewPhotoGalleryFragment extends Fragment implements View.OnClickListener {

    private int GalleryId;
    private HealthMetricsDbHelper healthMetricsDbHelper;
    private List<PhotoEntry> photoEntryList;

    public ViewPhotoGalleryFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        healthMetricsDbHelper = HealthMetricsDbHelper.getInstance(getActivity());

        Bundle bundle = this.getArguments();
        if (bundle != null) {
            GalleryId = bundle.getInt("selected_item_key", -1);
        }

        // Inflate the layout for this fragment
        View rootView = inflater.inflate(R.layout.fragment_view_photo_gallery, container, false);
        Button addEntryButton = rootView.findViewById(R.id.buttonAddEntryViewPhotoGallery);
        addEntryButton.setOnClickListener(this);

        Button manageGalleryButton = rootView.findViewById(R.id.buttonManageGalleryViewPhotoGallery);
        manageGalleryButton.setOnClickListener(this);

        RecyclerView photoGalleryRecyclerView = (RecyclerView) rootView.findViewById(R.id.recyclerviewPhotoGallery);

        photoEntryList = healthMetricsDbHelper.getPhotoEntriesByGalleryId(GalleryId);
        PhotoGalleryRecyclerViewAdapter adapter = new PhotoGalleryRecyclerViewAdapter(photoEntryList, getActivity());
        photoGalleryRecyclerView.setAdapter(adapter);
        photoGalleryRecyclerView.setLayoutManager(new GridLayoutManager(getActivity(), 2));

        return rootView;
    }

    @Override
    public void onClick(View v) {
        Fragment destinationFragment = new Fragment();
        if (v.getId() == R.id.buttonAddEntryViewPhotoGallery) {
            destinationFragment = new AddPhotoEntryFragment();
        } else if (v.getId() == R.id.buttonManageGalleryViewPhotoGallery) {
            destinationFragment = new AddPhotoEntryFragment();
        }
        Bundle galleryBundle = new Bundle();
        galleryBundle.putInt("selected_gallery_key", GalleryId);
        destinationFragment.setArguments(galleryBundle);

        getActivity().getSupportFragmentManager().beginTransaction()
                .replace(R.id.fragmentContainer, destinationFragment)
                .addToBackStack(null)
                .commit();
    }
}

