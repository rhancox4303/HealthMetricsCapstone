package ca.mohawk.HealthMetrics.Models;

/**
 * Models a photo entry from the database.
 */
public class PhotoEntry {

    // Represents the photo entry id.
    public int id;

    // Represents the photo entry's gallery id.
    public int photoGalleryId;

    // Represents the photo entry path.
    public String photoEntryPath;

    // Represents the date of entry.
    public String dateOfEntry;

    // Represents whether the photo entry came from the user's android photo gallery.
    public int isFromGallery;


    /**
     * Constructs a photo entry.
     *
     * @param photoGalleryId Represents the photo entry's gallery id.
     * @param photoEntryPath Represents the photo entry path.
     * @param dateOfEntry    Represents the date of entry.
     * @param isFromGallery  Represents whether the photo entry came from the user's android photo gallery.
     */
    public PhotoEntry(int photoGalleryId, String photoEntryPath, String dateOfEntry, int isFromGallery) {
        this.photoGalleryId = photoGalleryId;
        this.photoEntryPath = photoEntryPath;
        this.dateOfEntry = dateOfEntry;
        this.isFromGallery = isFromGallery;
    }

    /**
     * Constructs a photo entry.
     *
     * @param id             Represents the photo entry id.
     * @param photoGalleryId Represents the photo entry's gallery id.
     * @param photoEntryPath Represents the photo entry path.
     * @param dateOfEntry    Represents the date of entry.
     * @param isFromGallery  Represents whether the photo entry came from the user's android photo gallery.
     */
    public PhotoEntry(int id, int photoGalleryId, String photoEntryPath, String dateOfEntry, int isFromGallery) {
        this.id = id;
        this.photoGalleryId = photoGalleryId;
        this.photoEntryPath = photoEntryPath;
        this.dateOfEntry = dateOfEntry;
        this.isFromGallery = isFromGallery;
    }

    /**
     * Constructs a photo entry.
     *
     * @param id             Represents the photo entry id.
     * @param photoGalleryId Represents the photo entry's gallery id.
     * @param photoEntryPath Represents the photo entry path.
     * @param dateOfEntry    Represents the date of entry.
     */
    public PhotoEntry(int id, int photoGalleryId, String photoEntryPath, String dateOfEntry) {
        this.id = id;
        this.photoGalleryId = photoGalleryId;
        this.photoEntryPath = photoEntryPath;
        this.dateOfEntry = dateOfEntry;
    }
}
