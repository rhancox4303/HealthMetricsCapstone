package ca.mohawk.HealthMetrics.Models;

public class PhotoEntry {
    public int Id;
    public int PhotoGalleryId;
    public String PhotoEntryPath;
    public String DateOfEntry;
    public int IsFromGallery;

    public PhotoEntry(int photoGalleryId, String photoEntryPath, String dateOfEntry, int isFromGallery) {
        PhotoGalleryId = photoGalleryId;
        PhotoEntryPath = photoEntryPath;
        DateOfEntry = dateOfEntry;
        IsFromGallery = isFromGallery;
    }

    public PhotoEntry(int id, int photoGalleryId, String photoEntryPath, String dateOfEntry, int isFromGallery) {
        Id = id;
        PhotoGalleryId = photoGalleryId;
        PhotoEntryPath = photoEntryPath;
        DateOfEntry = dateOfEntry;
        IsFromGallery = isFromGallery;
    }

    public PhotoEntry(int id, int photoGalleryId, String photoEntryPath, String dateOfEntry) {
        Id = id;
        PhotoGalleryId = photoGalleryId;
        PhotoEntryPath = photoEntryPath;
        DateOfEntry = dateOfEntry;
    }
}
