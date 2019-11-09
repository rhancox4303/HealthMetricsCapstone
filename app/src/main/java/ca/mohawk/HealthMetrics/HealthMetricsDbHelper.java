package ca.mohawk.HealthMetrics;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import ca.mohawk.HealthMetrics.DisplayObjects.DataEntryDisplayObject;
import ca.mohawk.HealthMetrics.DisplayObjects.MetricDisplayObject;
import ca.mohawk.HealthMetrics.DisplayObjects.MetricSpinnerObject;
import ca.mohawk.HealthMetrics.DisplayObjects.PhotoGallerySpinnerObject;
import ca.mohawk.HealthMetrics.DisplayObjects.PrescriptionDisplayObject;
import ca.mohawk.HealthMetrics.DisplayObjects.UnitSpinnerObject;
import ca.mohawk.HealthMetrics.Models.DataEntry;
import ca.mohawk.HealthMetrics.Models.DosageMeasurement;
import ca.mohawk.HealthMetrics.Models.Metric;
import ca.mohawk.HealthMetrics.Models.Note;
import ca.mohawk.HealthMetrics.Models.Notification;
import ca.mohawk.HealthMetrics.Models.PhotoEntry;
import ca.mohawk.HealthMetrics.Models.PhotoGallery;
import ca.mohawk.HealthMetrics.Models.Prescription;
import ca.mohawk.HealthMetrics.Models.Unit;
import ca.mohawk.HealthMetrics.Models.UnitCategory;
import ca.mohawk.HealthMetrics.Models.User;

/**
 * The HealthMetricsDbHelper class extends the SQLiteOpenHelper Class.
 * Contains helper methods for interacting with the HealthMetrics Database.
 * <p>
 * Implements a singleton pattern.
 */
public class HealthMetricsDbHelper extends SQLiteOpenHelper {

    // Initialize the DATABASE_VERSION variable.
    private static final int DATABASE_VERSION = 1;

    // Initialize the DATABASE_NAME variable.
    private static final String DATABASE_NAME = "HealthMetrics.db";

    // Initialize the a static instance of HealthMetricsDbHelper
    private static HealthMetricsDbHelper helpInstance;

    /**
     * Constructs the HealthMetricDatabase.
     *
     * @param context Represents the application context.
     */
    private HealthMetricsDbHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    /**
     * Creates a new HealthMetricsDbHelper instance if helpInstance is null.
     *
     * @param context Represents the application context.
     * @return Return the helpInstance.
     */
    public static synchronized HealthMetricsDbHelper getInstance(Context context) {
        if (helpInstance == null) {
            helpInstance = new HealthMetricsDbHelper(context.getApplicationContext());
        }
        return helpInstance;
    }

    /**
     * Runs when the database is created.
     * It executes the Create statements from HealthMetricContract.
     *
     * @param db Represents the database.
     */
    @Override
    public void onCreate(SQLiteDatabase db) {

        //Create the tables.
        db.execSQL(HealthMetricContract.DosageMeasurements.CREATE_TABLE);
        db.execSQL(HealthMetricContract.Prescriptions.CREATE_TABLE);
        db.execSQL(HealthMetricContract.Users.CREATE_TABLE);
        db.execSQL(HealthMetricContract.Notes.CREATE_TABLE);
        db.execSQL(HealthMetricContract.UnitCategories.CREATE_TABLE);
        db.execSQL(HealthMetricContract.Units.CREATE_TABLE);
        db.execSQL(HealthMetricContract.Metrics.CREATE_TABLE);
        db.execSQL(HealthMetricContract.MetricDataEntries.CREATE_TABLE);
        db.execSQL(HealthMetricContract.Galleries.CREATE_TABLE);
        db.execSQL(HealthMetricContract.PhotoEntries.CREATE_TABLE);
        db.execSQL(HealthMetricContract.Notifications.CREATE_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }

    /**
     * Adds the data entry to the database.
     *
     * @param dataEntry Represents the data entry to be added to the database.
     * @return Returns a boolean value indicating if the insert was successful.
     */
    public boolean addDataEntry(DataEntry dataEntry) {

        // Get database.
        SQLiteDatabase writableDatabase = getWritableDatabase();
        writableDatabase.beginTransaction();

        // Initialize id variable.
        long id = -1;

        try {

            // Create content values.
            ContentValues values = new ContentValues();
            values.put(HealthMetricContract.MetricDataEntries.COLUMN_NAME_DATA_ENTRY, dataEntry.dataEntry);
            values.put(HealthMetricContract.MetricDataEntries.COLUMN_NAME_DATE_OF_ENTRY, dataEntry.dateOfEntry);
            values.put(HealthMetricContract.MetricDataEntries.COLUMN_NAME_METRIC_ID, dataEntry.metricId);

            // Insert data entry.
            id = writableDatabase.insertOrThrow(HealthMetricContract.MetricDataEntries.TABLE_NAME, null, values);
            writableDatabase.setTransactionSuccessful();
        } catch (SQLException e) {
            Log.e("SQLException:", e.getMessage());
        } finally {
            writableDatabase.endTransaction();
            writableDatabase.close();
        }

        // Validate the insert was successful.
        return id > 0;
    }

    /**
     * Adds a dosage measurement to the database.
     *
     * @param dosageMeasurement Represents the dosage measurement to be added.
     */
    private void addDosageMeasurement(DosageMeasurement dosageMeasurement) {

        // Get database.
        SQLiteDatabase writableDatabase = getWritableDatabase();
        writableDatabase.beginTransaction();

        try {
            // Create content values.
            ContentValues values = new ContentValues();
            values.put(HealthMetricContract.DosageMeasurements.COLUMN_NAME_DOSAGE_MEASUREMENT, dosageMeasurement.dosageMeasurement);
            values.put(HealthMetricContract.DosageMeasurements.COLUMN_NAME_UNIT_ABBREVIATION, dosageMeasurement.unitAbbreviation);

            // Insert the values into the database.
            writableDatabase.insertOrThrow(HealthMetricContract.DosageMeasurements.TABLE_NAME, null, values);
            writableDatabase.setTransactionSuccessful();

        } catch (SQLException e) {
            Log.e("SQLException:", e.getMessage());
        } finally {
            writableDatabase.endTransaction();
            writableDatabase.close();
        }
    }

    /**
     * Updates a gallery to add it to the user profile.
     *
     * @param galleryId Represents the id of the gallery that is being updated.
     * @return Returns a boolean value indicating if the update is successful.
     */
    public boolean addGalleryToProfile(int galleryId) {

        // Get database.
        SQLiteDatabase writableDatabase = getWritableDatabase();

        // Create content values.
        ContentValues values = new ContentValues();

        values.put(HealthMetricContract.Galleries.COLUMN_NAME_IS_ADDED_TO_PROFILE, 1);

        // Update row.
        return writableDatabase.update(HealthMetricContract.Galleries.TABLE_NAME, values,
                HealthMetricContract.Galleries._ID + " = " + galleryId,
                null) > 0;
    }

    /**
     * Adds the metric to the database.
     *
     * @param metric Represents the metric to be added to the database.
     * @return Return a boolean value indicating if the gallery was added successfully.
     */
    public boolean addMetric(Metric metric) {

        // Get the database.
        SQLiteDatabase writableDatabase = getWritableDatabase();
        writableDatabase.beginTransaction();

        // Initialize the id.
        long id = -1;

        try {
            // Create content values.
            ContentValues values = new ContentValues();

            values.put(HealthMetricContract.Metrics.COLUMN_NAME_METRIC_NAME, metric.name);
            values.put(HealthMetricContract.Metrics.COLUMN_NAME_UNIT_ID, metric.unitId);
            values.put(HealthMetricContract.Metrics.COLUMN_NAME_UNIT_CATEGORY_ID, metric.unitCategoryId);
            values.put(HealthMetricContract.Metrics.COLUMN_NAME_IS_ADDED_TO_PROFILE, metric.isAddedToProfile);

            // Insert values.
            id = writableDatabase.insertOrThrow(HealthMetricContract.Metrics.TABLE_NAME, null, values);
            writableDatabase.setTransactionSuccessful();
        } catch (SQLException e) {
            Log.e("SQLException:", e.getMessage());
        } finally {
            writableDatabase.endTransaction();
            writableDatabase.close();
        }
        return id > 0;
    }

    /**
     * Updates a metric to add a unit and that it has been added to the user profile.
     *
     * @param unitId   Represents the unitId that will be added to the metric.
     * @param metricId Represents the metricId that will be updated.
     * @return Returns a boolean value indicating if the update is successful.
     */
    public boolean addMetricToProfile(int unitId, int metricId) {

        // Get database.
        SQLiteDatabase writableDatabase = getWritableDatabase();

        // Create content values.
        ContentValues values = new ContentValues();

        values.put(HealthMetricContract.Metrics.COLUMN_NAME_UNIT_ID, unitId);
        values.put(HealthMetricContract.Metrics.COLUMN_NAME_IS_ADDED_TO_PROFILE, 1);

        // Update row.
        return writableDatabase.update(HealthMetricContract.Metrics.TABLE_NAME, values,
                HealthMetricContract.Metrics._ID + " = " + metricId,
                null) > 0;
    }

    /**
     * Adds a note to database.
     *
     * @param note Represents the note to be added to the database.
     * @return Return a boolean value indicating if the note was added successfully.
     */

    public boolean addNote(Note note) {

        // Get database.
        SQLiteDatabase writableDatabase = getWritableDatabase();
        writableDatabase.beginTransaction();

        //Initialize id.
        long id = -1;

        try {
            // Create content values.
            ContentValues values = new ContentValues();
            values.put(HealthMetricContract.Notes.COLUMN_NAME_NOTE_CONTENT, note.noteContent);
            values.put(HealthMetricContract.Notes.COLUMN_NAME_DATE_OF_ENTRY, note.dateOfEntry);

            // Insert the values into database.
            id = writableDatabase.insertOrThrow(HealthMetricContract.Notes.TABLE_NAME, null, values);
            writableDatabase.setTransactionSuccessful();
        } catch (SQLException e) {
            Log.e("SQLException:", e.getMessage());
        } finally {
            writableDatabase.endTransaction();
            writableDatabase.close();
        }

        // Validate the insert was successful.
        return id > 0;
    }

    /**
     * Adds a notification to the database.
     *
     * @param notification Represents the notification to add to the database.
     * @return Returns the id of inserted row.
     */
    public int addNotification(Notification notification) {

        // Get database.
        SQLiteDatabase writableDatabase = getWritableDatabase();
        writableDatabase.beginTransaction();

        // Initialize id variable.
        long id = (long) -1;
        try {
            // Create content values.
            ContentValues values = new ContentValues();
            values.put(HealthMetricContract.Notifications.COLUMN_NAME_TARGET_DATE_TIME, notification.targetDateTime);
            values.put(HealthMetricContract.Notifications.COLUMN_NAME_TARGET_ID, notification.targetId);
            values.put(HealthMetricContract.Notifications.COLUMN_NAME_TYPE, notification.notificationType);

            // Insert notification.
            id = writableDatabase.insertOrThrow(HealthMetricContract.Notifications.TABLE_NAME, null, values);
            writableDatabase.setTransactionSuccessful();
        } catch (SQLException e) {
            Log.e("SQLException:", e.getMessage());
        } finally {
            writableDatabase.endTransaction();
            writableDatabase.close();
        }

        // Return the id.
        return (int) id;
    }

    /**
     * Adds a photo entry to the database.
     *
     * @param photoEntry Represents the photo entry to be added to the database.
     * @return Returns a boolean value indicating if the insert was successful.
     */
    public boolean addPhotoEntry(PhotoEntry photoEntry) {

        // Get database.
        SQLiteDatabase writableDatabase = getWritableDatabase();
        writableDatabase.beginTransaction();

        // Initialize the id.
        long id = -1;
        try {

            // Create content values.
            ContentValues values = new ContentValues();

            values.put(HealthMetricContract.PhotoEntries.COLUMN_NAME_DATE_OF_ENTRY, photoEntry.dateOfEntry);
            values.put(HealthMetricContract.PhotoEntries.COLUMN_NAME_GALLERY_ID, photoEntry.photoGalleryId);
            values.put(HealthMetricContract.PhotoEntries.COLUMN_NAME_PHOTO_ENTRY_PATH, photoEntry.photoEntryPath);

            // Insert photo entry into database.
            id = writableDatabase.insertOrThrow(HealthMetricContract.PhotoEntries.TABLE_NAME, null, values);
            writableDatabase.setTransactionSuccessful();

        } catch (SQLException e) {
            Log.e("SQLException:", e.getMessage());
        } finally {
            writableDatabase.endTransaction();
            writableDatabase.close();
        }

        // Validate insert was successful.
        return id > 0;
    }

    /**
     * Adds the gallery to the database.
     *
     * @param photoGallery Represents the gallery to be added to the database.
     * @return Return a boolean value indicating if the gallery was added successfully.
     */
    public boolean addPhotoGallery(PhotoGallery photoGallery) {

        // Get the database.
        SQLiteDatabase writableDatabase = getWritableDatabase();
        writableDatabase.beginTransaction();

        // Initialize id.
        long id = -1;

        try {
            // Create content values.
            ContentValues values = new ContentValues();
            values.put(HealthMetricContract.Galleries.COLUMN_NAME_GALLERY_NAME, photoGallery.name);
            values.put(HealthMetricContract.Galleries.COLUMN_NAME_IS_ADDED_TO_PROFILE, photoGallery.isAddedToProfile);

            // Insert the gallery.
            id = writableDatabase.insertOrThrow(HealthMetricContract.Galleries.TABLE_NAME, null, values);
            writableDatabase.setTransactionSuccessful();
        } catch (SQLException e) {
            Log.e("SQLException:", e.getMessage());
        } finally {
            writableDatabase.endTransaction();
            writableDatabase.close();
        }

        // Validate the insert was successful.
        return id > 0;
    }

    /**
     * Adds a prescription to the database.
     *
     * @param prescription Represents the prescription to be added to the database.
     * @return Return a boolean value indicating if the prescription was added successfully.
     */
    public boolean addPrescription(Prescription prescription) {

        // Get the database.
        SQLiteDatabase writableDatabase = getWritableDatabase();
        writableDatabase.beginTransaction();
        long id = -1;

        try {
            // Create content values.
            ContentValues values = new ContentValues();

            values.put(HealthMetricContract.Prescriptions.COLUMN_NAME_NAME, prescription.name);
            values.put(HealthMetricContract.Prescriptions.COLUMN_NAME_AMOUNT, prescription.amount);
            values.put(HealthMetricContract.Prescriptions.COLUMN_NAME_DOSAGE_AMOUNT, prescription.dosageAmount);
            values.put(HealthMetricContract.Prescriptions.COLUMN_NAME_DOSAGE_MEASUREMENT, prescription.dosageMeasurementId);
            values.put(HealthMetricContract.Prescriptions.COLUMN_NAME_FORM, prescription.form);
            values.put(HealthMetricContract.Prescriptions.COLUMN_NAME_FREQUENCY, prescription.frequency);
            values.put(HealthMetricContract.Prescriptions.COLUMN_NAME_STRENGTH, prescription.strength);
            values.put(HealthMetricContract.Prescriptions.COLUMN_NAME_REASON, prescription.reason);

            // Add values to database.
            id = writableDatabase.insertOrThrow(HealthMetricContract.Prescriptions.TABLE_NAME, null, values);
            writableDatabase.setTransactionSuccessful();

        } catch (SQLException e) {
            Log.e("SQLException:", e.getMessage());
        } finally {
            writableDatabase.endTransaction();
            writableDatabase.close();
        }
        //  Validate the insert was successful.
        return id > 0;
    }

    /**
     * Adds the unit to the database.
     *
     * @param unit Represents the unit to be added to the database.
     */
    private void addUnit(Unit unit) {

        // Get the database.
        SQLiteDatabase writableDatabase = getWritableDatabase();
        writableDatabase.beginTransaction();

        try {
            // Create content values.
            ContentValues values = new ContentValues();
            values.put(HealthMetricContract.Units.COLUMN_NAME_UNIT_NAME, unit.unitName);
            values.put(HealthMetricContract.Units.COLUMN_NAME_ABBREVIATION, unit.unitAbbreviation);
            values.put(HealthMetricContract.Units.COLUMN_NAME_UNIT_CATEGORY_ID, unit.unitCategoryId);

            // Insert the unit in to the database.
            writableDatabase.insertOrThrow(HealthMetricContract.Units.TABLE_NAME, null, values);
            writableDatabase.setTransactionSuccessful();
        } catch (SQLException e) {
            Log.e("SQLException:", e.getMessage());
        } finally {
            writableDatabase.endTransaction();
            writableDatabase.close();
        }
    }

    /**
     * Adds the unit category to the database.
     *
     * @param unitCategory Represents the unit category to be added to the database.
     */
    private void addUnitCategory(UnitCategory unitCategory) {

        // Get the database.
        SQLiteDatabase writableDatabase = getWritableDatabase();
        writableDatabase.beginTransaction();

        try {
            // Create content values.
            ContentValues values = new ContentValues();

            if (unitCategory.id != 0) {
                values.put(HealthMetricContract.UnitCategories._ID, unitCategory.id);
            }

            values.put(HealthMetricContract.UnitCategories.COLUMN_NAME_UNIT_CATEGORY, unitCategory.unitCategory);

            // Insert unit category to database.
            writableDatabase.insertOrThrow(HealthMetricContract.UnitCategories.TABLE_NAME, null, values);
            writableDatabase.setTransactionSuccessful();

        } catch (SQLException e) {
            Log.e("SQLException:", e.getMessage());
        } finally {
            writableDatabase.endTransaction();
            writableDatabase.close();
        }
    }

    /**
     * Adds the user to the database.
     *
     * @param user Represents the user to be added to the database.
     * @return Return a boolean value indicating if the user was added successfully.
     */
    public boolean addUser(User user) {

        // Get the database.
        SQLiteDatabase writableDatabase = getWritableDatabase();
        writableDatabase.beginTransaction();

        // Initialize id.
        long id = -1;
        try {
            // Create content values.
            ContentValues values = new ContentValues();
            values.put(HealthMetricContract.Users.COLUMN_NAME_FIRST_NAME, user.firstName);
            values.put(HealthMetricContract.Users.COLUMN_NAME_LAST_NAME, user.lastName);
            values.put(HealthMetricContract.Users.COLUMN_NAME_DATE_OF_BIRTH, user.dateOfBirth);
            values.put(HealthMetricContract.Users.COLUMN_NAME_GENDER, user.gender);

            // Add values to database.
            id = writableDatabase.insertOrThrow(HealthMetricContract.Users.TABLE_NAME, null, values);
            writableDatabase.setTransactionSuccessful();

        } catch (SQLException e) {
            Log.e("SQLException:", e.getMessage());
        } finally {
            writableDatabase.endTransaction();
            writableDatabase.close();
        }

        // Validate the insert was successful.
        return id > 0;
    }

    /**
     * Deletes a data entry with the specified id from the database.
     *
     * @param dataEntryId Represents the id of the data entry.
     * @return Return a boolean value indicating if the deletion was successful.
     */
    public boolean deleteDataEntry(int dataEntryId) {
        SQLiteDatabase writableDatabase = this.getWritableDatabase();
        return writableDatabase.delete(HealthMetricContract.MetricDataEntries.TABLE_NAME,
                HealthMetricContract.MetricDataEntries._ID + "=" + dataEntryId,
                null) > 0;
    }

    /**
     * Deletes all data entries with the specified metric id from the database.
     *
     * @param metricId Represents the metric id of the deleted data entries.
     * @return Return a boolean value indicating if the deletion was successful.
     */
    public boolean deleteDataEntriesByMetricId(int metricId) {

        SQLiteDatabase writableDatabase = this.getWritableDatabase();
        return writableDatabase.delete(HealthMetricContract.MetricDataEntries.TABLE_NAME,
                HealthMetricContract.MetricDataEntries.COLUMN_NAME_METRIC_ID + "=?",
                new String[]{Integer.toString(metricId)}) > 0;
    }

    /**
     * Deletes a gallery from the database based on it's id.
     *
     * @param galleryId Represents the id of the deleted gallery.
     * @return Return a boolean value indicating if the deletion was successful.
     */
    public boolean deleteGallery(int galleryId) {

        // Get all the photo entries from this gallery.
        List<PhotoEntry> photos = getPhotoEntriesByGalleryId(galleryId);

        // Delete all photo entries from storage that are not stored in the gallery.
        for (PhotoEntry photoEntry : photos) {
            if (photoEntry.isFromGallery == 0) {
                File file = new File(photoEntry.photoEntryPath);
                file.delete();
            }
        }

        // Delete all of the photos from this gallery from the database.
        deletePhotoEntriesByGalleryId(galleryId);

        SQLiteDatabase writableDatabase = this.getWritableDatabase();
        return writableDatabase.delete(HealthMetricContract.Galleries.TABLE_NAME,
                HealthMetricContract.Galleries._ID + "=?",
                new String[]{Integer.toString(galleryId)}) > 0;
    }

    /**
     * Deletes a metric from the database with specified metric id.
     *
     * @param metricId Represents the metric id.
     * @return Return a boolean value indicating if the deletion was successful.
     */
    public boolean deleteMetric(int metricId) {

        // Delete all the data entries of this metric.
        deleteDataEntriesByMetricId(metricId);

        SQLiteDatabase writableDatabase = this.getWritableDatabase();
        return writableDatabase.delete(HealthMetricContract.Metrics.TABLE_NAME,
                HealthMetricContract.Metrics._ID + "=?",
                new String[]{Integer.toString(metricId)}) > 0;
    }

    /**
     * Deletes a note from the database with specified id.
     *
     * @param noteId Represents the note id.
     * @return Return a boolean value indicating if the deletion was successful.
     */
    public boolean deleteNoteById(int noteId) {
        SQLiteDatabase writableDatabase = this.getWritableDatabase();
        return writableDatabase.delete(HealthMetricContract.Notes.TABLE_NAME,
                HealthMetricContract.Notes._ID + "=?",
                new String[]{Integer.toString(noteId)}) > 0;
    }

    /**
     * Deletes a notification from the database with specified id.
     *
     * @param notificationId Represents the notification id.
     * @return Return a boolean value indicating if the deletion was successful.
     */
    public boolean deleteNotification(int notificationId) {
        SQLiteDatabase writableDatabase = this.getWritableDatabase();
        return writableDatabase.delete(HealthMetricContract.Notifications.TABLE_NAME,
                HealthMetricContract.Notifications._ID + "=?",
                new String[]{Integer.toString(notificationId)}) > 0;

    }

    /**
     * Deletes all photo entries with the specified gallery id from the database.
     *
     * @param galleryId Represents the gallery id of the deleted photo entries.
     * @return Return a boolean value indicating if the deletion was successful.
     */
    private boolean deletePhotoEntriesByGalleryId(int galleryId) {
        SQLiteDatabase writableDatabase = this.getWritableDatabase();
        return writableDatabase.delete(HealthMetricContract.PhotoEntries.TABLE_NAME,
                HealthMetricContract.PhotoEntries.COLUMN_NAME_GALLERY_ID + "=" + galleryId,
                null) > 0;
    }


    /**
     * Deletes a photo entry from the database with specified id.
     *
     * @param photoEntryId Represents the photo entry id.
     * @return Return a boolean value indicating if the deletion was successful.
     */
    public boolean deletePhotoEntryById(int photoEntryId) {
        SQLiteDatabase writableDatabase = this.getWritableDatabase();
        return writableDatabase.delete(HealthMetricContract.PhotoEntries.TABLE_NAME,
                HealthMetricContract.PhotoEntries._ID + "=" + photoEntryId,
                null) > 0;
    }


    /**
     * Deletes a prescription from the database with specified id.
     *
     * @param prescriptionId Represents the prescription id.
     * @return Return a boolean value indicating if the deletion was successful.
     */
    public boolean deletePrescription(int prescriptionId) {
        SQLiteDatabase writableDatabase = this.getWritableDatabase();
        return writableDatabase.delete(HealthMetricContract.Prescriptions.TABLE_NAME,
                HealthMetricContract.Prescriptions._ID + "=" + prescriptionId,
                null) > 0;
    }

    /**
     * Gets metrics added to the user profile.
     *
     * @return Returns a list of MetricRecyclerViewObjects.
     */
    public List<MetricDisplayObject> getAddedMetrics() {

        // Create the list of recyclerViewObjects.
        List<MetricDisplayObject> recyclerViewObjects = new ArrayList<>();

        // Get the database.
        SQLiteDatabase readableDatabase = getReadableDatabase();

        String[] projection = {
                HealthMetricContract.Metrics._ID,
                HealthMetricContract.Metrics.COLUMN_NAME_METRIC_NAME,
                HealthMetricContract.Metrics.COLUMN_NAME_UNIT_ID
        };

        // Set selection string.
        String selection = HealthMetricContract.Metrics.COLUMN_NAME_IS_ADDED_TO_PROFILE + "=?";

        Cursor cursor = readableDatabase.query(
                HealthMetricContract.Metrics.TABLE_NAME,
                projection,
                selection,
                new String[]{String.valueOf(1)},
                null,
                null,
                null);

        // Loop through results and add the created MetricDisplayObject to the list.
        while (cursor.moveToNext()) {
            String metricName = cursor.getString(
                    cursor.getColumnIndexOrThrow(HealthMetricContract.Metrics.COLUMN_NAME_METRIC_NAME));

            int metricId = cursor.getInt(
                    cursor.getColumnIndexOrThrow(HealthMetricContract.Metrics._ID));

            int unitId = cursor.getInt(
                    cursor.getColumnIndexOrThrow(HealthMetricContract.Metrics.COLUMN_NAME_UNIT_ID));

            String dataEntry = getLatestDataEntryValue(metricId);

            if (!dataEntry.equals("No Data Available")) {
                Unit unit = getUnitById(unitId);

                if (unit != null) {
                    dataEntry += " " + unit.unitAbbreviation;
                }

            }
            recyclerViewObjects.add(new MetricDisplayObject(metricId, metricName, dataEntry, "Quantitative"));
        }

        cursor.close();
        readableDatabase.close();
        return recyclerViewObjects;
    }

    /**
     * Gets added photo galleries from the database and converts them to MetricDisplayObject.
     *
     * @return Returns a list of MetricRecyclerViewObjects.
     */
    public List<MetricDisplayObject> getAddedPhotoGalleries() {

        // Create a the MetricDisplayObject list.
        List<MetricDisplayObject> recyclerViewObjects = new ArrayList<>();

        // Get database.
        SQLiteDatabase readableDatabase = getReadableDatabase();

        String[] projection = {
                HealthMetricContract.Galleries._ID,
                HealthMetricContract.Galleries.COLUMN_NAME_GALLERY_NAME
        };


        // Set the selection string
        String selection = HealthMetricContract.Galleries.COLUMN_NAME_IS_ADDED_TO_PROFILE + "=?";

        Cursor cursor = readableDatabase.query(
                HealthMetricContract.Galleries.TABLE_NAME,
                projection,
                selection,
                new String[]{String.valueOf(1)},
                null,
                null,
                null);

        // Loop through results and add the MetricDisplayObject to the recyclerViewObjects list.
        while (cursor.moveToNext()) {

            int galleryId = cursor.getInt(
                    cursor.getColumnIndexOrThrow(HealthMetricContract.Galleries._ID));

            String galleryName = cursor.getString(
                    cursor.getColumnIndexOrThrow(HealthMetricContract.Galleries.COLUMN_NAME_GALLERY_NAME));

            recyclerViewObjects.add(new MetricDisplayObject(galleryId, galleryName, "Photo Gallery", "Gallery"));
        }

        cursor.close();
        readableDatabase.close();
        return recyclerViewObjects;
    }

    /**
     * Gets metrics, galleries and notes that user has added to their profile.
     *
     * @return Returns a list of MetricRecyclerViewObjects.
     */
    public List<MetricDisplayObject> getAddedValues() {

        // Build the recyclerViewObjects.
        List<MetricDisplayObject> recyclerViewObjects = new ArrayList<>(getAddedMetrics());
        recyclerViewObjects.addAll(getAddedPhotoGalleries());
        recyclerViewObjects.addAll(getAllNotes());

        // Return the built list.
        return recyclerViewObjects;
    }

    /**
     * Gets all dosage measurements from the database.
     *
     * @return Return a list of dosage measurements.
     */
    public List<DosageMeasurement> getAllDosageMeasurements() {

        // Get database.
        SQLiteDatabase readableDatabase = getReadableDatabase();

        // Instantiate the list of dosage measurements.
        List<DosageMeasurement> dosageMeasurements = new ArrayList<>();

        String[] projection = {
                HealthMetricContract.DosageMeasurements._ID,
                HealthMetricContract.DosageMeasurements.COLUMN_NAME_DOSAGE_MEASUREMENT,
                HealthMetricContract.DosageMeasurements.COLUMN_NAME_UNIT_ABBREVIATION
        };

        // Sort by the measurement.
        String sortOrder = HealthMetricContract.DosageMeasurements.COLUMN_NAME_DOSAGE_MEASUREMENT + " ASC";

        Cursor cursor = readableDatabase.query(
                HealthMetricContract.DosageMeasurements.TABLE_NAME,
                projection,
                null,
                null,
                null,
                null,
                sortOrder);

        // Loop through cursor and add each dosage measurement to dosageMeasurements.
        while (cursor.moveToNext()) {
            String dosageMeasurement = cursor.getString(
                    cursor.getColumnIndexOrThrow(HealthMetricContract.DosageMeasurements.COLUMN_NAME_DOSAGE_MEASUREMENT));

            String unitAbbreviation = cursor.getString(
                    cursor.getColumnIndexOrThrow(HealthMetricContract.DosageMeasurements.COLUMN_NAME_UNIT_ABBREVIATION));

            int id = cursor.getInt(
                    cursor.getColumnIndexOrThrow(HealthMetricContract.DosageMeasurements._ID));

            dosageMeasurements.add(new DosageMeasurement(id, unitAbbreviation, dosageMeasurement));
        }

        cursor.close();
        readableDatabase.close();

        // Return the list of dosageMeasurements.
        return dosageMeasurements;
    }

    /**
     * Gets all the names of galleries from the database.
     *
     * @return Returns a list of strings.
     */
    public ArrayList<String> getAllGalleryNames() {

        // Instantiate the list of gallery names.
        ArrayList<String> galleryNames = new ArrayList<>();

        // Get the database list.
        SQLiteDatabase readableDatabase = getReadableDatabase();

        String[] projection = {
                HealthMetricContract.Galleries.COLUMN_NAME_GALLERY_NAME
        };

        Cursor cursor = readableDatabase.query(
                HealthMetricContract.Galleries.TABLE_NAME,
                projection,
                null,
                null,
                null,
                null,
                null);

        // Loop through the results and each name to the galleryNames list.
        while (cursor.moveToNext()) {
            String galleryName = cursor.getString(cursor.getColumnIndex(HealthMetricContract.Galleries.COLUMN_NAME_GALLERY_NAME));
            galleryNames.add(galleryName.toLowerCase());
        }

        cursor.close();
        readableDatabase.close();
        return galleryNames;
    }

    /**
     * Gets all the names of metrics from the database.
     *
     * @return Returns a list of strings.
     */
    public ArrayList<String> getAllMetricNames() {

        // Instantiate the list of gallery names
        ArrayList<String> metricNames = new ArrayList<>();

        // Get the database list.
        SQLiteDatabase readableDatabase = getReadableDatabase();

        String[] projection = {
                HealthMetricContract.Metrics.COLUMN_NAME_METRIC_NAME
        };

        Cursor cursor = readableDatabase.query(
                HealthMetricContract.Metrics.TABLE_NAME,
                projection,
                null,
                null,
                null,
                null,
                null);

        // Loop through the results and each name to the metricNames list.
        while (cursor.moveToNext()) {
            String metricName = cursor.getString(cursor.getColumnIndex(HealthMetricContract.Metrics.COLUMN_NAME_METRIC_NAME));
            metricNames.add(metricName.toLowerCase());
        }

        cursor.close();
        readableDatabase.close();

        return metricNames;
    }

    /**
     * Gets all metrics from the database and converts them into aspinner object.
     *
     * @return A list of MetricSpinnerObjects is returned.
     */
    public List<MetricSpinnerObject> getAllMetrics() {

        // Get the database
        SQLiteDatabase readableDatabase = getReadableDatabase();

        // Instantiate the array of metric spinner objects.
        List<MetricSpinnerObject> metricSpinnerObjects = new ArrayList<>();

        String[] projection = {
                HealthMetricContract.Metrics._ID,
                HealthMetricContract.Metrics.COLUMN_NAME_METRIC_NAME,
                HealthMetricContract.Metrics.COLUMN_NAME_UNIT_CATEGORY_ID
        };

        // Sort the metrics by the metric name.
        String sortOrder = HealthMetricContract.Metrics.COLUMN_NAME_METRIC_NAME + " ASC";

        Cursor cursor = readableDatabase.query(
                HealthMetricContract.Metrics.TABLE_NAME,
                projection,
                null,
                null,
                null,
                null,
                sortOrder);

        // Loop through the query results and add the metrics to the metricSpinnerObjects list.
        while (cursor.moveToNext()) {

            String metricName = cursor.getString(
                    cursor.getColumnIndexOrThrow(HealthMetricContract.Metrics.COLUMN_NAME_METRIC_NAME));

            int unitCategoryId = cursor.getInt(
                    cursor.getColumnIndexOrThrow(HealthMetricContract.Metrics.COLUMN_NAME_UNIT_CATEGORY_ID));

            int id = cursor.getInt(
                    cursor.getColumnIndexOrThrow(HealthMetricContract.Metrics._ID));

            metricSpinnerObjects.add(new MetricSpinnerObject(unitCategoryId, metricName, id));
        }

        cursor.close();
        readableDatabase.close();

        // Return the list of metrics
        return metricSpinnerObjects;
    }

    /**
     * Get all notes from the database and convert them to MetricDisplayObjects.
     *
     * @return Returns the list of MetricDisplayObjects.
     */
    private List<MetricDisplayObject> getAllNotes() {

        // Create the list of MetricDisplayObjects.
        List<MetricDisplayObject> recyclerViewObjects = new ArrayList<>();

        // Get the database.
        SQLiteDatabase readableDatabase = getReadableDatabase();

        String[] projection = {
                HealthMetricContract.Notes._ID,
                HealthMetricContract.Notes.COLUMN_NAME_NOTE_CONTENT
        };

        Cursor cursor = readableDatabase.query(
                HealthMetricContract.Notes.TABLE_NAME,
                projection,
                null,
                null,
                null,
                null,
                null);

        // Loop through results and add the MetricDisplayObject to the recyclerViewObjects list.
        while (cursor.moveToNext()) {

            int noteId = cursor.getInt(
                    cursor.getColumnIndexOrThrow(HealthMetricContract.Notes._ID));

            String content = cursor.getString(
                    cursor.getColumnIndexOrThrow(HealthMetricContract.Notes.COLUMN_NAME_NOTE_CONTENT));

            recyclerViewObjects.add(new MetricDisplayObject(noteId, "Note", content, "Note"));
        }

        cursor.close();
        readableDatabase.close();
        return recyclerViewObjects;
    }

    /**
     * Gets all the notifications from the database.
     *
     * @return Return the list of notifications.
     */
    public ArrayList<Notification> getAllNotifications() {

        // Create the list of notifications.
        ArrayList<Notification> notifications = new ArrayList<>();

        // Get the database.
        SQLiteDatabase readableDatabase = getReadableDatabase();

        String[] projection = {
                HealthMetricContract.Notifications._ID,
                HealthMetricContract.Notifications.COLUMN_NAME_TYPE,
                HealthMetricContract.Notifications.COLUMN_NAME_TARGET_DATE_TIME,
                HealthMetricContract.Notifications.COLUMN_NAME_TARGET_ID
        };

        Cursor cursor = readableDatabase.query(
                HealthMetricContract.Notifications.TABLE_NAME,
                projection,
                null,
                null,
                null,
                null,
                null);

        // Loop through the results and add each notification to the notifications list.
        while (cursor.moveToNext()) {
            int id = cursor.getInt(cursor.getColumnIndexOrThrow(HealthMetricContract.Notifications._ID));
            int targetId = cursor.getInt(cursor.getColumnIndexOrThrow(HealthMetricContract.Notifications.COLUMN_NAME_TARGET_ID));
            String type = cursor.getString(cursor.getColumnIndexOrThrow(HealthMetricContract.Notifications.COLUMN_NAME_TYPE));
            String targetDateTime = cursor.getString(cursor.getColumnIndexOrThrow(HealthMetricContract.Notifications.COLUMN_NAME_TARGET_DATE_TIME));

            notifications.add(new Notification(id, targetId, type, targetDateTime));
        }

        cursor.close();
        readableDatabase.close();
        return notifications;
    }

    /**
     * Get all photo galleries and convert them to photo gallery spinner objects.
     *
     * @return Return the list of PhotoGallerySpinnerObjects.
     */
    public List<PhotoGallerySpinnerObject> getAllPhotoGalleries() {

        // Get the database.
        SQLiteDatabase readableDatabase = getReadableDatabase();

        // Create the list of photoGallerySpinnerObjects.
        List<PhotoGallerySpinnerObject> photoGallerySpinnerObjects = new ArrayList<>();

        String[] projection = {
                HealthMetricContract.Galleries.COLUMN_NAME_GALLERY_NAME,
                HealthMetricContract.Galleries._ID
        };

        // Set the sort order string.
        String sortOrder = HealthMetricContract.Galleries.COLUMN_NAME_GALLERY_NAME + " ASC";

        Cursor cursor = readableDatabase.query(
                HealthMetricContract.Galleries.TABLE_NAME,
                projection,
                null,
                null,
                null,
                null,
                sortOrder);

        // Loop through the results and add the created PhotoGallerySpinnerObject to the list.
        while (cursor.moveToNext()) {
            int id = cursor.getInt(
                    cursor.getColumnIndexOrThrow(HealthMetricContract.Galleries._ID));
            String galleryName = cursor.getString(
                    cursor.getColumnIndexOrThrow(HealthMetricContract.Galleries.COLUMN_NAME_GALLERY_NAME));

            photoGallerySpinnerObjects.add(new PhotoGallerySpinnerObject(galleryName, id));
        }

        cursor.close();
        readableDatabase.close();
        return photoGallerySpinnerObjects;
    }

    /**
     * Get all prescriptions from the database.
     *
     * @return Returns a list of PrescriptionDisplayObjects.
     */
    public List<PrescriptionDisplayObject> getAllPrescriptions() {

        // Get the database.
        SQLiteDatabase readableDatabase = getReadableDatabase();

        // Instantiate the list of prescriptionDisplayObjects.
        List<PrescriptionDisplayObject> prescriptionDisplayObjects = new ArrayList<>();

        String[] projection = {
                HealthMetricContract.Prescriptions._ID,
                HealthMetricContract.Prescriptions.COLUMN_NAME_NAME,
                HealthMetricContract.Prescriptions.COLUMN_NAME_DOSAGE_AMOUNT,
                HealthMetricContract.Prescriptions.COLUMN_NAME_FREQUENCY,
                HealthMetricContract.Prescriptions.COLUMN_NAME_AMOUNT,
                HealthMetricContract.Prescriptions.COLUMN_NAME_DOSAGE_MEASUREMENT,
        };

        Cursor cursor = readableDatabase.query(
                HealthMetricContract.Prescriptions.TABLE_NAME,
                projection,
                null,
                null,
                null,
                null,
                null);


        // Loop through the query results and add prescription to prescriptionDisplayObjects.
        while (cursor.moveToNext()) {

            int id = cursor.getInt(cursor.getColumnIndex(HealthMetricContract.Prescriptions._ID));
            int dosageMeasurementId = cursor.getInt(cursor.getColumnIndex(HealthMetricContract.Prescriptions.COLUMN_NAME_DOSAGE_MEASUREMENT));

            String name = cursor.getString(cursor.getColumnIndex(HealthMetricContract.Prescriptions.COLUMN_NAME_NAME));
            String frequency = cursor.getString(cursor.getColumnIndex(HealthMetricContract.Prescriptions.COLUMN_NAME_FREQUENCY));

            double dosageAmount = cursor.getDouble(cursor.getColumnIndex(HealthMetricContract.Prescriptions.COLUMN_NAME_DOSAGE_AMOUNT));
            double amount = cursor.getDouble(cursor.getColumnIndex(HealthMetricContract.Prescriptions.COLUMN_NAME_AMOUNT));

            DosageMeasurement dosageMeasurement = getDosageMeasurementById(dosageMeasurementId);

            prescriptionDisplayObjects.add(new PrescriptionDisplayObject(id, name, dosageAmount, dosageMeasurement.dosageMeasurement, frequency, amount));
        }

        cursor.close();
        readableDatabase.close();
        return prescriptionDisplayObjects;
    }

    /**
     * Gets units based on the unit category id and convert them to unit spinner objects
     *
     * @param unitCategoryId Represents the unit category id of units that will be returned.
     * @return Return a list of unit spinner objects.
     */
    public List<UnitSpinnerObject> getAllSpinnerUnits(int unitCategoryId) {

        // Get the readable database.
        SQLiteDatabase readableDatabase = getReadableDatabase();

        // Create the list of unit spinner objects.
        List<UnitSpinnerObject> unitSpinnerObjects = new ArrayList<>();

        String[] projection = {
                HealthMetricContract.Units._ID,
                HealthMetricContract.Units.COLUMN_NAME_UNIT_NAME
        };

        // Get the string value of the unit category id.
        String unitCategoryIdString = String.valueOf(unitCategoryId);

        // Sort by the unit name.
        String sortOrder = HealthMetricContract.Units.COLUMN_NAME_UNIT_NAME + " ASC";

        // Create the selection string.
        String selection = HealthMetricContract.Units.COLUMN_NAME_UNIT_CATEGORY_ID + "=?";

        Cursor cursor = readableDatabase.query(
                HealthMetricContract.Units.TABLE_NAME,
                projection,
                selection,
                new String[]{unitCategoryIdString},
                null,
                null,
                sortOrder);

        // Loop through cursor results and add the unit to unitSpinnerObjects.
        while (cursor.moveToNext()) {

            String unitName = cursor.getString(
                    cursor.getColumnIndexOrThrow(HealthMetricContract.Units.COLUMN_NAME_UNIT_NAME));

            int id = cursor.getInt(
                    cursor.getColumnIndexOrThrow(HealthMetricContract.Units._ID));

            unitSpinnerObjects.add(new UnitSpinnerObject(unitName, id));
        }

        cursor.close();
        readableDatabase.close();
        return unitSpinnerObjects;
    }

    /**
     * Gets all unit categories from the database.
     *
     * @return Returns a list of all unit categories.
     */
    public List<UnitCategory> getAllUnitCategories() {

        // Get the database.
        SQLiteDatabase readableDatabase = getReadableDatabase();

        // Instantiate the list of unit categories.
        List<UnitCategory> unitCategories = new ArrayList<>();

        String[] projection = {
                HealthMetricContract.UnitCategories._ID,
                HealthMetricContract.UnitCategories.COLUMN_NAME_UNIT_CATEGORY,
        };

        Cursor cursor = readableDatabase.query(
                HealthMetricContract.UnitCategories.TABLE_NAME,
                projection,
                null,
                null,
                null,
                null,
                null,
                null);

        // Loop through the query results and add each unit category to unitCategories.
        while (cursor.moveToNext()) {

            int unitCategoryId = cursor.getInt(cursor.getColumnIndexOrThrow
                    (HealthMetricContract.UnitCategories._ID));

            String unitCategoryString = cursor.getString(cursor.getColumnIndexOrThrow
                    (HealthMetricContract.UnitCategories.COLUMN_NAME_UNIT_CATEGORY));

            UnitCategory unitCategory = new UnitCategory(unitCategoryId, unitCategoryString);
            unitCategories.add(unitCategory);
        }
        cursor.close();
        readableDatabase.close();
        return unitCategories;
    }

    /**
     * Gets the data entries that have a specified metric id.
     *
     * @param metricId Represents the id of the metric of the data entries retrieved.
     * @return Returns a list of the data entries.
     */
    public List<DataEntryDisplayObject> getDataEntriesByMetricId(int metricId) {

        // Get the metric.
        Metric metric = getMetricById(metricId);

        String unitAbbreviation = "";

        // Get the unit abbreviation if the metrics and units are found.
        if (metric != null) {
            Unit unit = getUnitById(metric.unitId);
            if (unit != null) {
                unitAbbreviation = unit.unitAbbreviation;
            }
        }

        // Get the database.
        SQLiteDatabase readableDatabase = getReadableDatabase();

        // Initialize the dataEntryDisplayObjects list.
        List<DataEntryDisplayObject> dataEntryDisplayObjects = new ArrayList<>();

        String[] projection = {
                HealthMetricContract.MetricDataEntries._ID,
                HealthMetricContract.MetricDataEntries.COLUMN_NAME_DATA_ENTRY,
                HealthMetricContract.MetricDataEntries.COLUMN_NAME_DATE_OF_ENTRY,
        };

        // Create the selection string.
        String selection = HealthMetricContract.MetricDataEntries.COLUMN_NAME_METRIC_ID + "=?";

        // Sort by the date of entry.
        String sortOrder =
                HealthMetricContract.MetricDataEntries.COLUMN_NAME_DATE_OF_ENTRY + " ASC";

        Cursor cursor = readableDatabase.query(
                HealthMetricContract.MetricDataEntries.TABLE_NAME,
                projection,
                selection,
                new String[]{String.valueOf(metricId)},
                null,
                null,
                sortOrder);

        // Loop through the cursor results and add each data entry to the dataEntryDisplayObjects list.
        while (cursor.moveToNext()) {
            int id = cursor.getInt(cursor.getColumnIndex(HealthMetricContract.MetricDataEntries._ID));
            String dataEntry = cursor.getString(cursor.getColumnIndex(HealthMetricContract.MetricDataEntries.COLUMN_NAME_DATA_ENTRY));
            String dateOfEntry = cursor.getString(cursor.getColumnIndex(HealthMetricContract.MetricDataEntries.COLUMN_NAME_DATE_OF_ENTRY));

            dataEntryDisplayObjects.add(new DataEntryDisplayObject(id, dateOfEntry, dataEntry, unitAbbreviation));
        }

        cursor.close();
        readableDatabase.close();
        return dataEntryDisplayObjects;
    }

    /**
     * Gets a data entry from the database based on it's id.
     *
     * @param dataEntryId Represents the id of the data entry that will be returned.
     * @return Returns the a data entry.
     */
    public DataEntry getDataEntryById(int dataEntryId) {

        // Get the database.
        SQLiteDatabase readableDatabase = getReadableDatabase();

        String[] projection = {
                HealthMetricContract.MetricDataEntries.COLUMN_NAME_DATA_ENTRY,
                HealthMetricContract.MetricDataEntries.COLUMN_NAME_METRIC_ID,
                HealthMetricContract.MetricDataEntries.COLUMN_NAME_DATE_OF_ENTRY,
        };

        // Set the selection string.
        String selection = HealthMetricContract.MetricDataEntries._ID + "=?";


        Cursor cursor = readableDatabase.query(
                HealthMetricContract.MetricDataEntries.TABLE_NAME,
                projection,
                selection,
                new String[]{String.valueOf(dataEntryId)},
                null,
                null,
                null);

        // Get the query results and return the data entry if one is found.
        if (cursor.moveToFirst()) {
            String dateOfEntry = cursor.getString(cursor.getColumnIndex(HealthMetricContract.MetricDataEntries.COLUMN_NAME_DATE_OF_ENTRY));
            int metricId = cursor.getInt(cursor.getColumnIndex(HealthMetricContract.MetricDataEntries.COLUMN_NAME_METRIC_ID));
            String dataEntry = cursor.getString(cursor.getColumnIndex(HealthMetricContract.MetricDataEntries.COLUMN_NAME_DATA_ENTRY));

            DataEntry dataEntryObject = new DataEntry(metricId, dataEntry, dateOfEntry);
            cursor.close();
            readableDatabase.close();
            return dataEntryObject;
        } else {
            cursor.close();
            readableDatabase.close();
            return null;
        }
    }

    /**
     * Gets a dosage measurement from the database based on it's id.
     *
     * @param dosageMeasurementId Represents the dosage measurement id.
     * @return Returns a dosage measurement with the specified id.
     */
    public DosageMeasurement getDosageMeasurementById(int dosageMeasurementId) {

        // Get the database.
        SQLiteDatabase readableDatabase = getReadableDatabase();

        String[] projection = {
                HealthMetricContract.DosageMeasurements.COLUMN_NAME_UNIT_ABBREVIATION,
                HealthMetricContract.DosageMeasurements.COLUMN_NAME_DOSAGE_MEASUREMENT
        };

        // Set the selection id.
        String selection = HealthMetricContract.DosageMeasurements._ID + "=?";

        Cursor cursor = readableDatabase.query(
                HealthMetricContract.DosageMeasurements.TABLE_NAME,
                projection,
                selection,
                new String[]{String.valueOf(dosageMeasurementId)},
                null,
                null,
                null);

        // Get the query results and return the dosageMeasurement if one is found.
        if (cursor.moveToFirst()) {
            String unitAbbreviation = cursor.getString(cursor.getColumnIndex(HealthMetricContract.DosageMeasurements.COLUMN_NAME_UNIT_ABBREVIATION));
            String dosageMeasurementString = cursor.getString(cursor.getColumnIndex(HealthMetricContract.DosageMeasurements.COLUMN_NAME_DOSAGE_MEASUREMENT));

            DosageMeasurement dosageMeasurement = new DosageMeasurement(dosageMeasurementString, unitAbbreviation);
            cursor.close();
            readableDatabase.close();
            return dosageMeasurement;
        } else {
            cursor.close();
            readableDatabase.close();
            return null;
        }
    }

    /**
     * Gets the latest data entry for a specified metric.
     *
     * @param metricId Represents the id of the metric.
     * @return Return the value of the latest data entry.
     */
    private String getLatestDataEntryValue(int metricId) {

        // Get the database.
        SQLiteDatabase readableDatabase = getReadableDatabase();

        String[] projection = {
                HealthMetricContract.MetricDataEntries.COLUMN_NAME_DATA_ENTRY
        };

        // Set the selection string.
        String selection = HealthMetricContract.MetricDataEntries.COLUMN_NAME_METRIC_ID + "=?";

        // Set the sort order.
        String sortOrder =
                HealthMetricContract.MetricDataEntries.COLUMN_NAME_DATE_OF_ENTRY + " ASC";

        Cursor cursor = readableDatabase.query(
                HealthMetricContract.MetricDataEntries.TABLE_NAME,
                projection,
                selection,
                new String[]{String.valueOf(metricId)},
                null,
                null,
                sortOrder);

        // Get the first data entry from the results.
        if (cursor.moveToFirst()) {

            String dataEntry = cursor.getString(cursor.getColumnIndex(HealthMetricContract.MetricDataEntries.COLUMN_NAME_DATA_ENTRY));
            cursor.close();
            readableDatabase.close();

            return dataEntry;
        } else {

            // Return No Data Available if nothing was found.
            cursor.close();
            readableDatabase.close();
            return "No Data Available";
        }
    }

    /**
     * Gets a metric from the database based on it's id.
     *
     * @param metricId Represents the id of the metric that will be returned.
     * @return Returns the metric with the id specified.
     */
    public Metric getMetricById(int metricId) {

        // Get the database.
        SQLiteDatabase readableDatabase = getReadableDatabase();

        String[] projection = {
                HealthMetricContract.Metrics.COLUMN_NAME_METRIC_NAME,
                HealthMetricContract.Metrics.COLUMN_NAME_UNIT_CATEGORY_ID,
                HealthMetricContract.Metrics.COLUMN_NAME_UNIT_ID,
                HealthMetricContract.Metrics.COLUMN_NAME_IS_ADDED_TO_PROFILE
        };

        // Set the selection string.
        String selection = HealthMetricContract.Metrics._ID + "=?";

        Cursor cursor = readableDatabase.query(
                HealthMetricContract.Metrics.TABLE_NAME,
                projection,
                selection,
                new String[]{String.valueOf(metricId)},
                null,
                null,
                null);

        // Get the query results and return the metric if one is found.
        if (cursor.moveToFirst()) {
            String metricName = cursor.getString(cursor.getColumnIndex(HealthMetricContract.Metrics.COLUMN_NAME_METRIC_NAME));
            int unitCategoryId = cursor.getInt(cursor.getColumnIndex(HealthMetricContract.Metrics.COLUMN_NAME_UNIT_CATEGORY_ID));
            int unitId = cursor.getInt(cursor.getColumnIndex(HealthMetricContract.Metrics.COLUMN_NAME_UNIT_ID));
            int isAddedToProfile = cursor.getInt(cursor.getColumnIndex(HealthMetricContract.Metrics.COLUMN_NAME_IS_ADDED_TO_PROFILE));

            Metric metric = new Metric(metricId, unitId, metricName, unitCategoryId, isAddedToProfile);
            cursor.close();
            readableDatabase.close();
            return metric;
        } else {
            cursor.close();
            readableDatabase.close();
            return null;
        }
    }

    /**
     * Gets a note from the database based on it's id.
     *
     * @param noteId Represents the note id.
     * @return Returns a note with the specified id.
     */
    public Note getNoteById(int noteId) {

        // Get the database.
        SQLiteDatabase readableDatabase = getReadableDatabase();

        String[] projection = {
                HealthMetricContract.Notes.COLUMN_NAME_DATE_OF_ENTRY,
                HealthMetricContract.Notes.COLUMN_NAME_NOTE_CONTENT,
        };

        // Set the selection string.
        String selection = HealthMetricContract.Notes._ID + "=?";

        Cursor cursor = readableDatabase.query(
                HealthMetricContract.Notes.TABLE_NAME,
                projection,
                selection,
                new String[]{String.valueOf(noteId)},
                null,
                null,
                null);

        // Get the query results and return the note if one is found.
        if (cursor.moveToFirst()) {
            String noteContent = cursor.getString(cursor.getColumnIndex(HealthMetricContract.Notes.COLUMN_NAME_NOTE_CONTENT));
            String dateOfEntry = cursor.getString(cursor.getColumnIndex(HealthMetricContract.Notes.COLUMN_NAME_DATE_OF_ENTRY));

            Note note = new Note(dateOfEntry, noteContent);
            cursor.close();
            readableDatabase.close();
            return note;

        } else {
            cursor.close();
            readableDatabase.close();
            return null;
        }
    }

    /**
     * Gets a notification from the database based on it's id.
     *
     * @param notificationId Represents the notification id.
     * @return Returns the notification with the specified id.
     */
    public Notification getNotificationById(int notificationId) {

        // Get the database.
        SQLiteDatabase readableDatabase = getReadableDatabase();

        String[] projection = {
                HealthMetricContract.Notifications.COLUMN_NAME_TARGET_ID,
                HealthMetricContract.Notifications.COLUMN_NAME_TARGET_DATE_TIME,
                HealthMetricContract.Notifications.COLUMN_NAME_TYPE
        };

        // Set the selection string.
        String selection = HealthMetricContract.Notifications._ID + "=?";

        Cursor cursor = readableDatabase.query(
                HealthMetricContract.Notifications.TABLE_NAME,
                projection,
                selection,
                new String[]{String.valueOf(notificationId)},
                null,
                null,
                null);

        // Get the query results and return the notification if one is found.
        if (cursor.moveToFirst()) {

            String targetDateTime = cursor.getString(cursor.getColumnIndex(HealthMetricContract.Notifications.COLUMN_NAME_TARGET_DATE_TIME));
            int targetId = cursor.getInt(cursor.getColumnIndex(HealthMetricContract.Notifications.COLUMN_NAME_TARGET_ID));
            String type = cursor.getString(cursor.getColumnIndex(HealthMetricContract.Notifications.COLUMN_NAME_TYPE));

            Notification notification = new Notification(notificationId, targetId, type, targetDateTime);

            cursor.close();
            readableDatabase.close();
            return notification;

        } else {
            cursor.close();
            readableDatabase.close();
            return null;
        }
    }

    /**
     * Get the photo entries from the database that has the specified photo gallery id.
     *
     * @param photoGalleryId Represents the photo gallery id that will be returned.
     * @return Returns a list of photo entry objects.
     */
    public List<PhotoEntry> getPhotoEntriesByGalleryId(int photoGalleryId) {

        // Get the database.
        SQLiteDatabase readableDatabase = getReadableDatabase();

        // Create the list of PhotoEntry objects.
        List<PhotoEntry> photoEntries = new ArrayList<>();

        String[] projection = {
                HealthMetricContract.PhotoEntries._ID,
                HealthMetricContract.PhotoEntries.COLUMN_NAME_PHOTO_ENTRY_PATH,
                HealthMetricContract.PhotoEntries.COLUMN_NAME_GALLERY_ID,
                HealthMetricContract.PhotoEntries.COLUMN_NAME_DATE_OF_ENTRY,
        };

        // Set the selection string.
        String selection = HealthMetricContract.PhotoEntries.COLUMN_NAME_GALLERY_ID + "=?";

        // Sort by the date of entry.
        String sortOrder =
                HealthMetricContract.PhotoEntries.COLUMN_NAME_DATE_OF_ENTRY + " ASC";

        Cursor cursor = readableDatabase.query(
                HealthMetricContract.PhotoEntries.TABLE_NAME,
                projection,
                selection,
                new String[]{String.valueOf(photoGalleryId)},
                null,
                null,
                sortOrder);

        // Loop through the results and add each PhotoEntry object to the photoEntries list.
        while (cursor.moveToNext()) {

            int id = cursor.getInt(cursor.getColumnIndex(HealthMetricContract.PhotoEntries._ID));
            String photoEntryPath = cursor.getString(cursor.getColumnIndex(HealthMetricContract.PhotoEntries.COLUMN_NAME_PHOTO_ENTRY_PATH));
            int galleryId = cursor.getInt(cursor.getColumnIndex(HealthMetricContract.PhotoEntries.COLUMN_NAME_GALLERY_ID));
            String dateOfEntry = cursor.getString(cursor.getColumnIndex(HealthMetricContract.PhotoEntries.COLUMN_NAME_DATE_OF_ENTRY));

            photoEntries.add(new PhotoEntry(id, galleryId, photoEntryPath, dateOfEntry));
        }

        cursor.close();
        readableDatabase.close();

        return photoEntries;
    }

    /**
     * Gets a photo entry from the database based on it's id.
     *
     * @param photoId Represents the id of the photo that will be queried for.
     * @return Returns a photo entry.
     */
    public PhotoEntry getPhotoEntryById(int photoId) {

        // Get the database.
        SQLiteDatabase readableDatabase = getReadableDatabase();

        String[] projection = {
                HealthMetricContract.PhotoEntries._ID,
                HealthMetricContract.PhotoEntries.COLUMN_NAME_DATE_OF_ENTRY,
                HealthMetricContract.PhotoEntries.COLUMN_NAME_GALLERY_ID,
                HealthMetricContract.PhotoEntries.COLUMN_NAME_PHOTO_ENTRY_PATH,
                HealthMetricContract.PhotoEntries.COLUMN_NAME_IS_FROM_GALLERY
        };

        // Set the selection string.
        String selection = HealthMetricContract.PhotoEntries._ID + "=?";

        Cursor cursor = readableDatabase.query(
                HealthMetricContract.PhotoEntries.TABLE_NAME,   //
                projection,
                selection,
                new String[]{String.valueOf(photoId)},
                null,
                null,
                null);

        // Get the query results and return the photo entry if one is found.
        if (cursor.moveToFirst()) {

            String photoEntryPath = cursor.getString(cursor.getColumnIndex(HealthMetricContract.PhotoEntries.COLUMN_NAME_PHOTO_ENTRY_PATH));
            String dateOfEntry = cursor.getString(cursor.getColumnIndex(HealthMetricContract.PhotoEntries.COLUMN_NAME_DATE_OF_ENTRY));

            int id = cursor.getInt(cursor.getColumnIndex(HealthMetricContract.PhotoEntries._ID));
            int galleryId = cursor.getInt(cursor.getColumnIndex(HealthMetricContract.PhotoEntries.COLUMN_NAME_GALLERY_ID));
            int isFromGallery = cursor.getInt(cursor.getColumnIndex(HealthMetricContract.PhotoEntries.COLUMN_NAME_IS_FROM_GALLERY));

            PhotoEntry photoEntry = new PhotoEntry(id, galleryId, photoEntryPath, dateOfEntry, isFromGallery);

            cursor.close();
            readableDatabase.close();
            return photoEntry;
        } else {
            cursor.close();
            readableDatabase.close();
            return null;
        }
    }

    /**
     * Get a photo gallery from the database based on it's id.
     *
     * @param galleryId Represents the id of the gallery.
     * @return Returns a gallery with the passed in id.
     */
    public PhotoGallery getPhotoGalleryById(int galleryId) {

        // Get the database.
        SQLiteDatabase readableDatabase = getReadableDatabase();

        String[] projection = {
                HealthMetricContract.Galleries.COLUMN_NAME_GALLERY_NAME,
                HealthMetricContract.Galleries.COLUMN_NAME_IS_ADDED_TO_PROFILE
        };

        // Set the selection string.
        String selection = HealthMetricContract.Galleries._ID + "=?";

        Cursor cursor = readableDatabase.query(
                HealthMetricContract.Galleries.TABLE_NAME,
                projection,
                selection,
                new String[]{String.valueOf(galleryId)},
                null,
                null,
                null);

        // Get the query results and return the gallery if one is found.
        if (cursor.moveToFirst()) {

            String galleryName = cursor.getString(cursor.getColumnIndex(HealthMetricContract.Galleries.COLUMN_NAME_GALLERY_NAME));
            int isAddedToProfile = cursor.getInt(cursor.getColumnIndex(HealthMetricContract.Galleries.COLUMN_NAME_IS_ADDED_TO_PROFILE));

            PhotoGallery photoGallery = new PhotoGallery(galleryName, isAddedToProfile);

            cursor.close();
            readableDatabase.close();
            return photoGallery;
        } else {
            cursor.close();
            readableDatabase.close();
            return null;
        }
    }

    /**
     * Gets a prescription from the database based on it's id.
     *
     * @param prescriptionId Represents the id of the prescription.
     * @return Returns a prescription with the passed in prescription id.
     */
    public Prescription getPrescriptionById(int prescriptionId) {

        // Get the database.
        SQLiteDatabase readableDatabase = getReadableDatabase();

        String[] projection = {
                HealthMetricContract.Prescriptions.COLUMN_NAME_NAME,
                HealthMetricContract.Prescriptions.COLUMN_NAME_DOSAGE_MEASUREMENT,
                HealthMetricContract.Prescriptions.COLUMN_NAME_AMOUNT,
                HealthMetricContract.Prescriptions.COLUMN_NAME_FREQUENCY,
                HealthMetricContract.Prescriptions.COLUMN_NAME_DOSAGE_AMOUNT,
                HealthMetricContract.Prescriptions.COLUMN_NAME_FORM,
                HealthMetricContract.Prescriptions.COLUMN_NAME_REASON,
                HealthMetricContract.Prescriptions.COLUMN_NAME_STRENGTH
        };

        // Set the selection string.
        String selection = HealthMetricContract.Prescriptions._ID + "=?";

        Cursor cursor = readableDatabase.query(
                HealthMetricContract.Prescriptions.TABLE_NAME,
                projection,
                selection,
                new String[]{String.valueOf(prescriptionId)},
                null,
                null,
                null);

        // Get the query results and return the prescription if one is found.
        if (cursor.moveToFirst()) {

            String name = cursor.getString(cursor.getColumnIndex(HealthMetricContract.Prescriptions.COLUMN_NAME_NAME));
            int dosageMeasurement = cursor.getInt(cursor.getColumnIndex(HealthMetricContract.Prescriptions.COLUMN_NAME_DOSAGE_MEASUREMENT));
            double amount = cursor.getDouble(cursor.getColumnIndex(HealthMetricContract.Prescriptions.COLUMN_NAME_AMOUNT));
            String frequency = cursor.getString(cursor.getColumnIndex(HealthMetricContract.Prescriptions.COLUMN_NAME_FREQUENCY));
            double dosageAmount = cursor.getDouble(cursor.getColumnIndex(HealthMetricContract.Prescriptions.COLUMN_NAME_DOSAGE_AMOUNT));
            String form = cursor.getString(cursor.getColumnIndex(HealthMetricContract.Prescriptions.COLUMN_NAME_FORM));
            String reason = cursor.getString(cursor.getColumnIndex(HealthMetricContract.Prescriptions.COLUMN_NAME_REASON));
            String strength = cursor.getString(cursor.getColumnIndex(HealthMetricContract.Prescriptions.COLUMN_NAME_STRENGTH));

            Prescription prescription = new Prescription(prescriptionId, dosageMeasurement, name, form, strength, dosageAmount, frequency, amount, reason);
            cursor.close();
            readableDatabase.close();
            return prescription;

        } else {
            cursor.close();
            readableDatabase.close();
            return null;
        }
    }

    /**
     * Get a unit from the database based on it's id.
     *
     * @param unitId Represents the id of the unit that will be returned.
     * @return Returns the unit with the id specified.
     */
    public Unit getUnitById(int unitId) {

        // Get the database.
        SQLiteDatabase readableDatabase = getReadableDatabase();

        String[] projection = {
                HealthMetricContract.Units.COLUMN_NAME_UNIT_NAME,
                HealthMetricContract.Units.COLUMN_NAME_UNIT_CATEGORY_ID,
                HealthMetricContract.Units.COLUMN_NAME_ABBREVIATION
        };

        // Set the selection string.
        String selection = HealthMetricContract.Units._ID + "=?";

        Cursor cursor = readableDatabase.query(
                HealthMetricContract.Units.TABLE_NAME,
                projection,
                selection,
                new String[]{String.valueOf(unitId)},
                null,
                null,
                null);

        // Get the query results and return the unit if one is found.
        if (cursor.moveToFirst()) {
            String unitName = cursor.getString(cursor.getColumnIndex(HealthMetricContract.Units.COLUMN_NAME_UNIT_NAME));
            int unitCategoryId = cursor.getInt(cursor.getColumnIndex(HealthMetricContract.Units.COLUMN_NAME_UNIT_CATEGORY_ID));
            String unitAbbreviation = cursor.getString(cursor.getColumnIndex(HealthMetricContract.Units.COLUMN_NAME_ABBREVIATION));

            Unit unit = new Unit(unitName, unitAbbreviation, unitCategoryId);
            cursor.close();
            readableDatabase.close();
            return unit;

        } else {
            cursor.close();
            readableDatabase.close();
            return null;
        }
    }

    /**
     * Gets the user from the database.
     *
     * @return Returns the user.
     */
    public User getUser() {

        // Get the database.
        SQLiteDatabase readableDatabase = getReadableDatabase();

        // Set the query.
        String selectQuery = "SELECT * FROM " + HealthMetricContract.Users.TABLE_NAME + " WHERE _ID = 1";

        Cursor cursor = readableDatabase.rawQuery(selectQuery, null);

        // Get the query results and return the user if it is found.
        if (cursor.moveToFirst()) {
            String firstName = cursor.getString(cursor.getColumnIndex(HealthMetricContract.Users.COLUMN_NAME_FIRST_NAME));
            String lastName = cursor.getString(cursor.getColumnIndex(HealthMetricContract.Users.COLUMN_NAME_LAST_NAME));
            String gender = cursor.getString(cursor.getColumnIndex(HealthMetricContract.Users.COLUMN_NAME_GENDER));
            String dateOfBirth = cursor.getString(cursor.getColumnIndex(HealthMetricContract.Users.COLUMN_NAME_DATE_OF_BIRTH));

            readableDatabase.close();
            cursor.close();

            return new User(firstName, lastName, gender, dateOfBirth);
        } else {
            readableDatabase.close();
            cursor.close();
            return null;
        }
    }

    /**
     * Updates a metric to add a unit and remove it from the user profile.
     *
     * @param metricId Represents the metricId that will be updated.
     * @return Returns a boolean value indicating if the update is successful.
     */
    public boolean removeMetricFromProfile(int metricId) {

        // Get database.
        SQLiteDatabase writableDatabase = getWritableDatabase();

        // Create content values.
        ContentValues values = new ContentValues();

        values.put(HealthMetricContract.Metrics.COLUMN_NAME_UNIT_ID, 0);
        values.put(HealthMetricContract.Metrics.COLUMN_NAME_IS_ADDED_TO_PROFILE, 0);

        // Update row.
        return writableDatabase.update(HealthMetricContract.Metrics.TABLE_NAME, values,
                HealthMetricContract.Units._ID + " = " + metricId,
                null) > 0;
    }

    /**
     * Seed the database with the starting data.
     */
    public void seedDatabase() {

        // Call the seed methods.
        seedUnitCategories();
        seedUnits();
        seedMetrics();
        seedDosageMeasurements();
        seedPhotoGalleries();
    }

    /**
     * Seed the database dosage measurement values.
     */
    private void seedDosageMeasurements() {

        // Create list of dosage measurements.
        List<DosageMeasurement> dosageMeasurements = new ArrayList<>();

        DosageMeasurement milliliters = new DosageMeasurement("Milliliters", "ml");
        dosageMeasurements.add(milliliters);

        DosageMeasurement teaspoon = new DosageMeasurement("Teaspoons", "tsp");
        dosageMeasurements.add(teaspoon);

        DosageMeasurement gram = new DosageMeasurement("Grams", "tsp");
        dosageMeasurements.add(gram);

        DosageMeasurement milligram = new DosageMeasurement("Milligrams", "mg");
        dosageMeasurements.add(milligram);

        DosageMeasurement tablet = new DosageMeasurement("Tablets", "tab");
        dosageMeasurements.add(tablet);

        DosageMeasurement capsule = new DosageMeasurement("Capsule", "cap");
        dosageMeasurements.add(capsule);

        // Add all the dosage measurements to the database.
        for (DosageMeasurement dosageMeasurement : dosageMeasurements) {
            addDosageMeasurement(dosageMeasurement);
        }
    }

    /**
     * Seed metrics for the database.
     */
    private void seedMetrics() {

        // Create the list of metrics.
        ArrayList<Metric> metrics = new ArrayList<>();

        Metric leftBicepSize = new Metric(0, "Left Bicep Size", 1, 0);
        Metric rightBicepSize = new Metric(0, "Right Bicep Size", 1, 0);

        metrics.add(leftBicepSize);
        metrics.add(rightBicepSize);

        Metric bloodPressure = new Metric(0, "Blood Pressure", 5, 0);
        metrics.add(bloodPressure);

        Metric bodyHeight = new Metric(0, "Body Height", 1, 0);
        metrics.add(bodyHeight);

        Metric leftCalfSize = new Metric(0, "Left Calf Size", 1, 0);
        Metric rightCalfSize = new Metric(0, "Right Calf Size", 1, 0);

        metrics.add(leftCalfSize);
        metrics.add(rightCalfSize);

        Metric chestSize = new Metric(0, "Chest Size", 1, 0);
        metrics.add(chestSize);

        Metric sleepDuration = new Metric(0, "Sleep Duration", 3, 0);
        metrics.add(sleepDuration);

        Metric waistSize = new Metric(0, "Waist Size", 1, 0);
        metrics.add(waistSize);

        Metric waterIntake = new Metric(0, "Water Intake", 4, 0);
        metrics.add(waterIntake);

        // Adds the metrics to the database.
        for (Metric metric : metrics) {
            addMetric(metric);
        }
    }

    /**
     * Seed the photo galleries for the database.
     */
    private void seedPhotoGalleries() {

        // Create the list of photo galleries.
        List<PhotoGallery> photoGalleries = new ArrayList<>();

        PhotoGallery sunBurnGallery = new PhotoGallery("Sunburn", 0);
        photoGalleries.add(sunBurnGallery);

        PhotoGallery rashGallery = new PhotoGallery("Rash", 0);
        photoGalleries.add(rashGallery);

        PhotoGallery coldSoreGallery = new PhotoGallery("Cold Sore", 0);
        photoGalleries.add(coldSoreGallery);

        PhotoGallery bruiseGallery = new PhotoGallery("Bruise", 0);
        photoGalleries.add(bruiseGallery);

        // Adds the galleries to the database.
        for (PhotoGallery gallery : photoGalleries) {
            addPhotoGallery(gallery);
        }
    }

    /**
     * Seed database unit categories .
     */
    private void seedUnitCategories() {

        // Create the list of unit categories.
        List<UnitCategory> unitCategories = new ArrayList<>();

        UnitCategory length = new UnitCategory(1, "Length");
        unitCategories.add(length);

        UnitCategory weight = new UnitCategory(2, "Weight");
        unitCategories.add(weight);

        UnitCategory time = new UnitCategory(3, "Time");
        unitCategories.add(time);

        UnitCategory volume = new UnitCategory(4, "Volume");
        unitCategories.add(volume);

        UnitCategory bloodPressure = new UnitCategory(5, "Blood Pressure");
        unitCategories.add(bloodPressure);

        // Add all the unit categories to the database.
        for (UnitCategory unitCategory : unitCategories) {
            addUnitCategory(unitCategory);
        }
    }

    /**
     * Seed units for the database.
     */
    private void seedUnits() {

        // Create the list of units.
        ArrayList<Unit> units = new ArrayList<>();

        Unit centimeters = new Unit("Centimeters", "cm", 1);
        Unit meters = new Unit("Meters", "m", 1);
        Unit inches = new Unit("Inches", "in", 1);
        Unit feet = new Unit("Feet", "ft", 1);

        units.add(centimeters);
        units.add(meters);
        units.add(inches);
        units.add(feet);

        Unit kilograms = new Unit("Kilograms", "kg", 2);
        Unit grams = new Unit("Grams", "g", 2);
        Unit milligrams = new Unit("Milligrams", "mg", 2);
        Unit pounds = new Unit("Pounds", "lb", 2);

        units.add(kilograms);
        units.add(grams);
        units.add(milligrams);
        units.add(pounds);

        Unit hours = new Unit("Hours", "hrs", 3);
        Unit minutes = new Unit("Minutes", "min", 3);
        Unit seconds = new Unit("Seconds", "s", 3);

        units.add(hours);
        units.add(minutes);
        units.add(seconds);

        Unit litres = new Unit("Litres", "l", 4);
        Unit milliliter = new Unit("Milliliters", "ml", 4);
        Unit ounce = new Unit("Ounces", "fl oz", 4);
        Unit cupMilliliter = new Unit("Cup(240ml)", "Cups", 4);
        Unit cupOunce = new Unit("Cup(8.4 fl oz)", "Cups", 4);

        units.add(litres);
        units.add(milliliter);
        units.add(ounce);
        units.add(cupMilliliter);
        units.add(cupOunce);

        Unit systolicBloodPressure = new Unit("Systolic Blood Pressure", "mmHg", 5);
        Unit diastolicBloodPressure = new Unit("Diastolic Blood Pressure", "mmHg", 5);

        units.add(systolicBloodPressure);
        units.add(diastolicBloodPressure);

        // Adds the units to the database.
        for (Unit unit : units) {
            addUnit(unit);
        }
    }

    /**
     * Updates a data entry in the database.
     *
     * @param dataEntry Represents the data entry that will be updated.
     * @return Returns a boolean value indicating if the update is successful.
     */
    public boolean updateDataEntry(DataEntry dataEntry) {

        // Get database.
        SQLiteDatabase writableDatabase = this.getWritableDatabase();

        // Create content values.
        ContentValues values = new ContentValues();

        values.put(HealthMetricContract.MetricDataEntries.COLUMN_NAME_METRIC_ID, dataEntry.metricId);
        values.put(HealthMetricContract.MetricDataEntries.COLUMN_NAME_DATE_OF_ENTRY, dataEntry.dateOfEntry);
        values.put(HealthMetricContract.MetricDataEntries.COLUMN_NAME_DATA_ENTRY, dataEntry.dataEntry);

        // Update row.
        return writableDatabase.update(HealthMetricContract.MetricDataEntries.TABLE_NAME, values,
                HealthMetricContract.MetricDataEntries._ID + " = " + dataEntry.id,
                null) > 0;
    }

    /**
     * Updates a photo gallery in the database.
     *
     * @param gallery Represents the photo gallery that will be updated.
     * @return Returns a boolean value indicating if the update is successful.
     */
    public boolean updateGallery(PhotoGallery gallery) {

        // Get database.
        SQLiteDatabase writableDatabase = this.getWritableDatabase();
        // Create content values.
        ContentValues values = new ContentValues();

        values.put(HealthMetricContract.Galleries.COLUMN_NAME_GALLERY_NAME, gallery.name);
        // Update row
        return writableDatabase.update(HealthMetricContract.Galleries.TABLE_NAME, values,
                HealthMetricContract.Galleries._ID + " = " + gallery.id,
                null) > 0;
    }

    /**
     * Updates a metric in the database.
     *
     * @param metric Represents the metric that will be updated.
     * @return Returns a boolean value indicating if the update is successful.
     */
    public boolean updateMetric(Metric metric) {

        // Get database.
        SQLiteDatabase writableDatabase = this.getWritableDatabase();


        // Create content values.
        ContentValues values = new ContentValues();

        values.put(HealthMetricContract.Metrics.COLUMN_NAME_METRIC_NAME, metric.name);

        // Update row.
        return writableDatabase.update(HealthMetricContract.Metrics.TABLE_NAME, values,
                HealthMetricContract.Metrics._ID + " = " + metric.id,
                null) > 0;
    }

    /**
     * Updates a note in the database.
     *
     * @param note Represents the note that will be updated.
     * @return Returns a boolean value indicating if the update is successful.
     */
    public boolean updateNote(Note note) {

        // Get database.
        SQLiteDatabase writableDatabase = this.getWritableDatabase();

        // Create content values.
        ContentValues values = new ContentValues();

        values.put(HealthMetricContract.Notes.COLUMN_NAME_DATE_OF_ENTRY, note.dateOfEntry);
        values.put(HealthMetricContract.Notes.COLUMN_NAME_NOTE_CONTENT, note.noteContent);

        // Update row.
        return writableDatabase.update(HealthMetricContract.Notes.TABLE_NAME, values,
                HealthMetricContract.Notes._ID + " = " + note.id,
                null) > 0;
    }

    /**
     * Updates a notification in the database.
     *
     * @param notification Represents the notification that will be updated.
     * @return Returns a boolean value indicating if the update is successful.
     */
    public boolean updateNotification(Notification notification) {

        // Get database.
        SQLiteDatabase writableDatabase = this.getWritableDatabase();

        // Create content values
        ContentValues values = new ContentValues();

        values.put(HealthMetricContract.Notifications.COLUMN_NAME_TYPE, notification.notificationType);
        values.put(HealthMetricContract.Notifications.COLUMN_NAME_TARGET_DATE_TIME, notification.targetDateTime);
        values.put(HealthMetricContract.Notifications.COLUMN_NAME_TARGET_ID, notification.targetId);

        // Update row.
        return writableDatabase.update(HealthMetricContract.Notifications.TABLE_NAME, values,
                HealthMetricContract.Notifications._ID + "=?",
                new String[]{Integer.toString(notification.id)}) > 0;
    }

    /**
     * Updates a photoEntry in the database.
     *
     * @param photoEntry Represents the photo entry that will be updated.
     * @return Returns a boolean value indicating if the update is successful.
     */
    public boolean updatePhotoEntry(PhotoEntry photoEntry) {

        // Get database.
        SQLiteDatabase writableDatabase = this.getWritableDatabase();

        // Create content values
        ContentValues values = new ContentValues();

        values.put(HealthMetricContract.PhotoEntries.COLUMN_NAME_DATE_OF_ENTRY, photoEntry.dateOfEntry);
        values.put(HealthMetricContract.PhotoEntries.COLUMN_NAME_IS_FROM_GALLERY, photoEntry.isFromGallery);
        values.put(HealthMetricContract.PhotoEntries.COLUMN_NAME_PHOTO_ENTRY_PATH, photoEntry.photoEntryPath);

        // Update row.
        return writableDatabase.update(HealthMetricContract.PhotoEntries.TABLE_NAME, values,
                HealthMetricContract.PhotoEntries._ID + "=?",
                new String[]{Integer.toString(photoEntry.id)}) > 0;
    }

    /**
     * Updates a prescription in the database.
     *
     * @param prescription Represents the prescription that will be updated.
     * @return Returns a boolean value indicating if the update is successful.
     */
    public boolean updatePrescription(Prescription prescription) {

        // Get database.
        SQLiteDatabase writableDatabase = this.getWritableDatabase();

        // Create content values
        ContentValues values = new ContentValues();

        values.put(HealthMetricContract.Prescriptions.COLUMN_NAME_NAME, prescription.name);
        values.put(HealthMetricContract.Prescriptions.COLUMN_NAME_REASON, prescription.reason);
        values.put(HealthMetricContract.Prescriptions.COLUMN_NAME_FREQUENCY, prescription.frequency);
        values.put(HealthMetricContract.Prescriptions.COLUMN_NAME_STRENGTH, prescription.strength);
        values.put(HealthMetricContract.Prescriptions.COLUMN_NAME_DOSAGE_MEASUREMENT, prescription.dosageMeasurementId);
        values.put(HealthMetricContract.Prescriptions.COLUMN_NAME_FORM, prescription.form);
        values.put(HealthMetricContract.Prescriptions.COLUMN_NAME_AMOUNT, prescription.amount);

        // Update row.
        return writableDatabase.update(HealthMetricContract.Prescriptions.TABLE_NAME, values,
                HealthMetricContract.Prescriptions._ID + " = " + prescription.id,
                null) > 0;
    }

    /**
     * Updates the user profile in the database.
     *
     * @param user Represents the user that will be upgraded.
     * @return Returns a boolean value indicating if the update is successful.
     */
    public boolean updateUser(User user) {

        // Get database.
        SQLiteDatabase writableDatabase = this.getWritableDatabase();

        // Create content values.
        ContentValues values = new ContentValues();
        values.put(HealthMetricContract.Users.COLUMN_NAME_FIRST_NAME, user.firstName);
        values.put(HealthMetricContract.Users.COLUMN_NAME_LAST_NAME, user.lastName);
        values.put(HealthMetricContract.Users.COLUMN_NAME_DATE_OF_BIRTH, user.dateOfBirth);
        values.put(HealthMetricContract.Users.COLUMN_NAME_GENDER, user.gender);

        // Update row.
        return writableDatabase.update(HealthMetricContract.Users.TABLE_NAME, values,
                HealthMetricContract.Users._ID + " = 1", null) > 0;
    }
}