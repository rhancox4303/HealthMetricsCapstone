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

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import ca.mohawk.HealthMetrics.MainActivity;
import ca.mohawk.HealthMetrics.Models.PhotoEntry;
import ca.mohawk.HealthMetrics.PhotoGallery.ViewPhotoEntryFragment;
import ca.mohawk.HealthMetrics.R;

/**
 * Acts as a custom adapter to display
 * the photo entries in the photo entry list recycler view.
 */
public class PhotoGalleryRecyclerViewAdapter extends
        RecyclerView.Adapter<PhotoGalleryRecyclerViewAdapter.ViewHolder> {

    // Instantiate the context variable.
    private Context context;

    // Instantiate the list of photo entries to use in the adapter.
    private List<PhotoEntry> photoEntries;

    /**
     * Creates the adapter.
     *
     * @param photoEntries Represents the list of photo entries.
     * @param context      Represents the application context.
     */
    public PhotoGalleryRecyclerViewAdapter(List<PhotoEntry> photoEntries, Context context) {
        this.photoEntries = photoEntries;
        this.context = context;
    }

    /**
     * Creates the View Holder.
     *
     * @param parent   Represents the parent view group.
     * @param viewType Represents the view type.
     * @return A created view holder is returned.
     */
    @NonNull
    @Override
    public PhotoGalleryRecyclerViewAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        // Get the context.
        Context context = parent.getContext();

        // Inflate the view.
        LayoutInflater inflater = LayoutInflater.from(context);
        View contactView = inflater.inflate(R.layout.photo_gallery_recycler_view_layout, parent, false);

        // Return the View Holder.
        return new ViewHolder(contactView);
    }

    /**
     * Sets the item views in the view holder.
     *
     * @param viewHolder Represents the view holder.
     * @param position   Represents the position of the photo entry that is being displayed.
     */
    @Override
    public void onBindViewHolder(PhotoGalleryRecyclerViewAdapter.ViewHolder viewHolder, int position) {

        // Get photo entry.
        final PhotoEntry photoEntry = photoEntries.get(position);

        // Display the date of entry in the recycler view.
        TextView dateOfEntryTextView = viewHolder.textViewDateOfEntry;
        dateOfEntryTextView.setText(photoEntry.DateOfEntry);

        // Use Glide to load the photo into the view holder's image view.
        Glide.with(context)
                .load(photoEntry.PhotoEntryPath)
                .centerCrop()
                .into(viewHolder.imageViewPhotoEntry);

        // Set the itemView onCLickListener.
        viewHolder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Call the switchFragment method with the photo entry passed in.
                switchFragment(photoEntry);
            }
        });
    }

    /**
     * Gets the new fragment and calls switch fragment on the main activity.
     *
     * @param photoEntrySelected Represents the selected notification.
     */
    private void switchFragment(PhotoEntry photoEntrySelected) {

        // Create ViewPhotoEntry Fragment.
        ViewPhotoEntryFragment destinationFragment = new ViewPhotoEntryFragment();

        // Create bundle and add the photo entry id.
        Bundle galleryBundle = new Bundle();
        galleryBundle.putInt("selected_photo_key", photoEntrySelected.Id);

        // Set the bundle to the destination fragment.
        destinationFragment.setArguments(galleryBundle);

        // If the context is an instance of MainActivity then call switchFragment.
        if (context instanceof MainActivity) {
            MainActivity mainActivity = (MainActivity) context;
            mainActivity.switchFragment(destinationFragment);
        }
    }

    // Returns the size of photoEntries.
    @Override
    public int getItemCount() {
        return photoEntries.size();
    }

    /**
     * Defines the view of a row inside the recycler view.
     */
    class ViewHolder extends RecyclerView.ViewHolder {

        // Initialize the image view and the date of entry text view.
        ImageView imageViewPhotoEntry;
        TextView textViewDateOfEntry;

        ViewHolder(View itemView) {
            super(itemView);

            // Get the views from the photo entries list recycler view layout.
            imageViewPhotoEntry = itemView.findViewById(R.id.image_view_gallery);
            textViewDateOfEntry = itemView.findViewById(R.id.date_gallery);
        }
    }
}
