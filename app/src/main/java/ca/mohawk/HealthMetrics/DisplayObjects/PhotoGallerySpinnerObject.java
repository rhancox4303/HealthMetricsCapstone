package ca.mohawk.HealthMetrics.DisplayObjects;

import androidx.annotation.NonNull;

/**
 * Represents a display object that represents a photo gallery.
 * <p>
 * It used to display photo galleries in the metric spinner in the addMetricFragment.
 */
public class PhotoGallerySpinnerObject {

    // Represents the id of the gallery.
    public int id;

    // Represents the name of the gallery.
    private String galleryName;

    /**
     * Constructs the PhotoGallerySpinnerObject.
     *
     * @param galleryName Represents the gallery name.
     * @param id          Represents the id of the gallery.
     */
    public PhotoGallerySpinnerObject(String galleryName, int id) {
        this.galleryName = galleryName;
        this.id = id;
    }

    @NonNull
    @Override
    public String toString() {
        return galleryName;
    }
}
