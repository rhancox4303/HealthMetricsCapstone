package ca.mohawk.HealthMetrics.Adapaters;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;

import java.util.List;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;
import ca.mohawk.HealthMetrics.MainActivity;
import ca.mohawk.HealthMetrics.Models.PhotoEntry;
import ca.mohawk.HealthMetrics.PhotoGallery.ViewPhotoEntryFragment;
import ca.mohawk.HealthMetrics.PhotoGallery.ViewPhotoGalleryFragment;
import ca.mohawk.HealthMetrics.R;

public class PhotoGalleryRecyclerViewAdapter extends
        RecyclerView.Adapter<PhotoGalleryRecyclerViewAdapter.ViewHolder> {
    private Context context;

    //The list of metrics to be displayed in the recycler view.
    private List<PhotoEntry> photoEntryList;

    public PhotoGalleryRecyclerViewAdapter(List<PhotoEntry> photoEntryList, Context context) {
        this.photoEntryList = photoEntryList;
        this.context = context;
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        public ImageView imageViewPhotoEntry;
        public TextView textViewDateOfEntry;

        public ViewHolder(View itemView) {
            super(itemView);

            imageViewPhotoEntry = (ImageView) itemView.findViewById(R.id.image_view_gallery);
            textViewDateOfEntry = (TextView) itemView.findViewById(R.id.date_gallery);
        }
    }

    @Override
    public PhotoGalleryRecyclerViewAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        Context context = parent.getContext();
        LayoutInflater inflater = LayoutInflater.from(context);

        View contactView = inflater.inflate(R.layout.photo_gallery_recycler_view_layout, parent, false);

        ViewHolder viewHolder = new ViewHolder(contactView);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(PhotoGalleryRecyclerViewAdapter.ViewHolder viewHolder, int position) {

        //Get data object
        final PhotoEntry photoEntry = photoEntryList.get(position);

        // Set item views
        TextView dateOfEntryTextView = viewHolder.textViewDateOfEntry;
        dateOfEntryTextView.setText(photoEntry.DateOfEntry);

        Glide.with(context)
                .load(photoEntry.PhotoEntryPath)
                .centerCrop()
                .into(viewHolder.imageViewPhotoEntry);

        viewHolder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                changeFragment(photoEntry);
            }
        });
    }

    @Override
    public int getItemCount() {
        return photoEntryList.size();
    }

    private void changeFragment(PhotoEntry itemSelected) {
        ViewPhotoEntryFragment fragment = new ViewPhotoEntryFragment();
        Bundle galleryBundle = new Bundle();
        galleryBundle.putInt("selected_photo_key", itemSelected.Id);
        fragment.setArguments(galleryBundle);
        switchContent(fragment);
    }

    public void switchContent(Fragment fragment) {
        if (context == null)
            return;
        if (context instanceof MainActivity) {
            MainActivity mainActivity = (MainActivity) context;
            mainActivity.switchContent(fragment);
        }
    }
}
