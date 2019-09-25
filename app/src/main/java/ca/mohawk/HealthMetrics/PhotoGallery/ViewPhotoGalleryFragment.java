package ca.mohawk.HealthMetrics.PhotoGallery;


import android.os.Bundle;
import androidx.fragment.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import ca.mohawk.HealthMetrics.HealthMetricsDbHelper;
import ca.mohawk.HealthMetrics.R;


/**
 * A simple {@link Fragment} subclass.
 */
public class ViewPhotoGalleryFragment extends Fragment implements View.OnClickListener {

    private int GalleryId;
    private HealthMetricsDbHelper healthMetricsDbHelper;

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
        return rootView;
    }

    @Override
    public void onClick(View v) {
        if(v.getId() == R.id.buttonAddEntryViewPhotoGallery) {
            AddPhotoEntryFragment addPhotoEntryFragment = new AddPhotoEntryFragment();
            getActivity().getSupportFragmentManager().beginTransaction()
                    .replace(R.id.fragmentContainer, addPhotoEntryFragment)
                    .addToBackStack(null)
                    .commit();
        }
    }
}
