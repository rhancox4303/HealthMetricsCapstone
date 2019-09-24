package ca.mohawk.HealthMetrics.DisplayObjects;

public class PhotoGallerySpinnerObject {
    String GalleryName;
    int Id;

    public PhotoGallerySpinnerObject(String galleryName, int id) {
        GalleryName = galleryName;
        Id = id;
    }

    public int getId() {
        return Id;
    }

    @Override
    public String toString() {
        return GalleryName;
    }
}
