package ca.mohawk.HealthMetrics.Models;

/**
 * Models a photo gallery from the database.
 */
public class PhotoGallery {

    // Represents the photo gallery id.
    public int id;

    // Represents the photo gallery name.
    public String name;

    // Represents whether the photo gallery is added to the user profile.
    public int isAddedToProfile;

    /**
     * Constructs a photo gallery.
     *
     * @param name             Represents the photo gallery name.
     * @param isAddedToProfile Represents whether the photo gallery is added to the user profile.
     */
    public PhotoGallery(String name, int isAddedToProfile) {
        this.name = name;
        this.isAddedToProfile = isAddedToProfile;
    }

    /**
     * Constructs a photo gallery.
     *
     * @param id               Represents the photo gallery id.
     * @param name             Represents the photo gallery name.
     * @param isAddedToProfile Represents whether the photo gallery is added to the user profile.
     */
    public PhotoGallery(int id, String name, int isAddedToProfile) {
        this.id = id;
        this.name = name;
        this.isAddedToProfile = isAddedToProfile;
    }
}
