package ca.mohawk.HealthMetrics.Models;

public class PhotoGallery {
    public int Id;
    public String Name;
    public int IsAddedToProfile;

    public PhotoGallery(String name, int isAddedToProfile) {
        Name = name;
        IsAddedToProfile = isAddedToProfile;
    }

    public PhotoGallery(int id, String name, int isAddedToProfile) {
        Id = id;
        Name = name;
        IsAddedToProfile = isAddedToProfile;
    }
}
