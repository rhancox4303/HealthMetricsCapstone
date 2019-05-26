package ca.mohawk.HealthMetrics.Models;

import android.graphics.Bitmap;

import java.util.Date;

public class PhotoEntry {
    public int Id;
    public PhotoGallery PhotoGallery;
    public Bitmap PhotoEntry;
    public Date DateOfEntry;

    public PhotoEntry(PhotoGallery photoGallery, Bitmap photoEntry, Date dateOfEntry) {
        PhotoGallery = photoGallery;
        PhotoEntry = photoEntry;
        DateOfEntry = dateOfEntry;
    }
}
