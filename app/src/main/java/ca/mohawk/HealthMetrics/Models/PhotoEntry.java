package ca.mohawk.HealthMetrics.Models;

import android.graphics.Bitmap;

import java.util.Date;

public class PhotoEntry {
    public int Id;
    public int PhotoGalleryId;
    public String PhotoEntry;
    public String DateOfEntry;

    public PhotoEntry(int photoGalleryId, String photoEntry, String dateOfEntry) {
        PhotoGalleryId = photoGalleryId;
        PhotoEntry = photoEntry;
        DateOfEntry = dateOfEntry;
    }
}
