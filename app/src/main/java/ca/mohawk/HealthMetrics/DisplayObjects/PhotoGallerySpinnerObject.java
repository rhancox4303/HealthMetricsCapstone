package ca.mohawk.HealthMetrics.DisplayObjects;

import androidx.annotation.NonNull;

public class PhotoGallerySpinnerObject {
    private String GalleryName;
    private int Id;

    public PhotoGallerySpinnerObject(String galleryName, int id) {
        GalleryName = galleryName;
        Id = id;
    }

    public int getId() {
        return Id;
    }

    @NonNull
    @Override
    public String toString() {
        return GalleryName;
    }
}
