package ca.mohawk.HealthMetrics.Models;

import android.graphics.Bitmap;

import java.util.Date;

public class PhotoEntry {
    public int Id;
    public int PhotoGalleryId;
    public String PhotoEntryPath;
    public String DateOfEntry;

    public PhotoEntry(int photoGalleryId, String photoEntryPath, String dateOfEntry) {
        PhotoGalleryId = photoGalleryId;
        PhotoEntryPath = photoEntryPath;
        DateOfEntry = dateOfEntry;
    }
}
