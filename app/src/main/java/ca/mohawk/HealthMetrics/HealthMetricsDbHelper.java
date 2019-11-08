package ca.mohawk.HealthMetrics;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import ca.mohawk.HealthMetrics.DisplayObjects.DataEntryRecyclerViewObject;
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
     * @param db represents the database.
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
            values.put(HealthMetricContract.DosageMeasurements.COLUMN_NAME_DOSAGEMEASUREMENT, dosageMeasurement.DosageMeasurement);
            values.put(HealthMetricContract.DosageMeasurements.COLUMN_NAME_UNITABBREVIATION, dosageMeasurement.UnitAbbreviation);

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
            values.put(HealthMetricContract.Notes.COLUMN_NAME_NOTECONTENT, note.NoteContent);
            values.put(HealthMetricContract.Notes.COLUMN_NAME_DATEOFENTRY, note.DateOfEntry);

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
            values.put(HealthMetricContract.Users.COLUMN_NAME_FIRSTNAME, user.FirstName);
            values.put(HealthMetricContract.Users.COLUMN_NAME_LASTNAME, user.LastName);
            values.put(HealthMetricContract.Users.COLUMN_NAME_DATEOFBIRTH, user.DateOfBirth);
            values.put(HealthMetricContract.Users.COLUMN_NAME_GENDER, user.Gender);

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
     * Adds a prescription to the database.
     *
     * @param prescription Represents the prescription to be added to the database.
     * @return Return a boolean value indicating if the prescription was added successfully.
     */
    public boolean addPrescription(Prescription prescription) {

        // Get the database.
        SQLiteDatabase writableDatabase = getWritableDatabase();
        writableDatabase.beginTransaction();

        // Initialize id.
        long id = -1;

        try {

            // Create content values.
            ContentValues values = new ContentValues();

            values.put(HealthMetricContract.Prescriptions.COLUMN_NAME_NAME, prescription.Name);
            values.put(HealthMetricContract.Prescriptions.COLUMN_NAME_AMOUNT, prescription.Amount);
            values.put(HealthMetricContract.Prescriptions.COLUMN_NAME_DOSAGEAMOUNT, prescription.DosageAmount);
            values.put(HealthMetricContract.Prescriptions.COLUMN_NAME_DOSAGEMEASUREMENT, prescription.DosageMeasurementId);
            values.put(HealthMetricContract.Prescriptions.COLUMN_NAME_FORM, prescription.Form);
            values.put(HealthMetricContract.Prescriptions.COLUMN_NAME_FREQUENCY, prescription.Frequency);
            values.put(HealthMetricContract.Prescriptions.COLUMN_NAME_STRENGTH, prescription.Strength);
            values.put(HealthMetricContract.Prescriptions.COLUMN_NAME_REASON, prescription.Reason);

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
            values.put(HealthMetricContract.Galleries.COLUMN_NAME_GALLERYNAME, photoGallery.Name);
            values.put(HealthMetricContract.Galleries.COLUMN_NAME_ISADDEDTOPROFILE, photoGallery.IsAddedToProfile);

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

            values.put(HealthMetricContract.Metrics.COLUMN_NAME_METRICNAME, metric.Name);
            values.put(HealthMetricContract.Metrics.COLUMN_NAME_UNITID, metric.UnitId);
            values.put(HealthMetricContract.Metrics.COLUMN_NAME_UNITCATEGORYID, metric.UnitCategoryId);
            values.put(HealthMetricContract.Metrics.COLUMN_NAME_ISADDEDTOPROFILE, metric.IsAddedToProfile);

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

            values.put(HealthMetricContract.PhotoEntries.COLUMN_NAME_DATEOFENTRY, photoEntry.DateOfEntry);
            values.put(HealthMetricContract.PhotoEntries.COLUMN_NAME_GALLERYID, photoEntry.PhotoGalleryId);
            values.put(HealthMetricContract.PhotoEntries.COLUMN_NAME_PHOTOENTRYPATH, photoEntry.PhotoEntryPath);

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
     * Adds the data entry to the database.
     *
     * @param dataEntry represents the data entry to be added to the database.
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
            values.put(HealthMetricContract.MetricDataEntries.COLUMN_NAME_DATAENTRY, dataEntry.DataEntry);
            values.put(HealthMetricContract.MetricDataEntries.COLUMN_NAME_DATEOFENTRY, dataEntry.DateOfEntry);
            values.put(HealthMetricContract.MetricDataEntries.COLUMN_NAME_METRICID, dataEntry.MetricId);

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
            values.put(HealthMetricContract.Notifications.COLUMN_NAME_TARGETDATETIME, notification.TargetDateTime);
            values.put(HealthMetricContract.Notifications.COLUMN_NAME_TARGETID, notification.TargetId);
            values.put(HealthMetricContract.Notifications.COLUMN_NAME_TYPE, notification.NotificationType);

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
            values.put(HealthMetricContract.Units.COLUMN_NAME_UNITNAME, unit.UnitName);
            values.put(HealthMetricContract.Units.COLUMN_NAME_ABBREVIATION, unit.UnitAbbreviation);
            values.put(HealthMetricContract.Units.COLUMN_NAME_UNITCATEGORYID, unit.UnitCategoryId);

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

            if (unitCategory.Id != 0) {
                values.put(HealthMetricContract.UnitCategories._ID, unitCategory.Id);
            }

            values.put(HealthMetricContract.UnitCategories.COLUMN_NAME_UNITCATEGORY, unitCategory.UnitCategory);

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
     * Seeds the database with the starting data.
     */
    void seedDatabase() {

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
     * Seeds units for the database.
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
     * Seeds metrics for the database.
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
     * Seeds the photo galleries for the database.
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
     * Gets all metrics from the database and converts them into spinner object.
     *
     * @return A list of MetricSpinnerObjects is returned.
     */
    public List<MetricSpinnerObject> getAllMetrics() {

        // Get the database.
        SQLiteDatabase readableDatabase = getReadableDatabase();

        // Instantiate the array of metric spinner objects.
        List<MetricSpinnerObject> metricSpinnerObjects = new ArrayList<>();

        String[] projection = {
                HealthMetricContract.Metrics._ID,
                HealthMetricContract.Metrics.COLUMN_NAME_METRICNAME,
                HealthMetricContract.Metrics.COLUMN_NAME_UNITCATEGORYID
        };

        // Sort the metrics by the metric name.
        String sortOrder = HealthMetricContract.Metrics.COLUMN_NAME_METRICNAME + " ASC";

        Cursor cursor = readableDatabase.query(
                HealthMetricContract.Metrics.TABLE_NAME,
                projection,
                null,
                null,
                null,
                null,
                sortOrder);

        // Get the query results.
        while (cursor.moveToNext()) {

            String metricName = cursor.getString(
                    cursor.getColumnIndexOrThrow(HealthMetricContract.Metrics.COLUMN_NAME_METRICNAME));

            int unitCategoryId = cursor.getInt(
                    cursor.getColumnIndexOrThrow(HealthMetricContract.Metrics.COLUMN_NAME_UNITCATEGORYID));

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
                HealthMetricContract.DosageMeasurements.COLUMN_NAME_DOSAGEMEASUREMENT,
                HealthMetricContract.DosageMeasurements.COLUMN_NAME_UNITABBREVIATION
        };

        // Sort by the measurement.
        String sortOrder = HealthMetricContract.DosageMeasurements.COLUMN_NAME_DOSAGEMEASUREMENT + " ASC";

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
                    cursor.getColumnIndexOrThrow(HealthMetricContract.DosageMeasurements.COLUMN_NAME_DOSAGEMEASUREMENT));

            String unitAbbreviation = cursor.getString(
                    cursor.getColumnIndexOrThrow(HealthMetricContract.DosageMeasurements.COLUMN_NAME_UNITABBREVIATION));

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
                HealthMetricContract.Units.COLUMN_NAME_UNITNAME
        };

        // Get the string value of the unit category id.
        String unitCategoryIdString = String.valueOf(unitCategoryId);

        // Sort by the unit name.
        String sortOrder = HealthMetricContract.Units.COLUMN_NAME_UNITNAME + " ASC";

        // Create the selection string.
        String selection = HealthMetricContract.Units.COLUMN_NAME_UNITCATEGORYID + "=?";

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
                    cursor.getColumnIndexOrThrow(HealthMetricContract.Units.COLUMN_NAME_UNITNAME));

            int id = cursor.getInt(
                    cursor.getColumnIndexOrThrow(HealthMetricContract.Units._ID));

            unitSpinnerObjects.add(new UnitSpinnerObject(unitName, id));
        }

        cursor.close();
        readableDatabase.close();
        return unitSpinnerObjects;
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
                HealthMetricContract.Metrics.COLUMN_NAME_METRICNAME,
                HealthMetricContract.Metrics.COLUMN_NAME_UNITID
        };

        // Set selection string.
        String selection = HealthMetricContract.Metrics.COLUMN_NAME_ISADDEDTOPROFILE + "=?";

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
                    cursor.getColumnIndexOrThrow(HealthMetricContract.Metrics.COLUMN_NAME_METRICNAME));

            int metricId = cursor.getInt(
                    cursor.getColumnIndexOrThrow(HealthMetricContract.Metrics._ID));

            int unitId = cursor.getInt(
                    cursor.getColumnIndexOrThrow(HealthMetricContract.Metrics.COLUMN_NAME_UNITID));

            String dataEntry = getLatestDataEntryValue(metricId);

            if (!dataEntry.equals("No Data Available")) {
                Unit unit = getUnitById(unitId);
                if (unit != null) {
                    dataEntry += " " + unit.UnitAbbreviation;
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
                HealthMetricContract.Galleries.COLUMN_NAME_GALLERYNAME
        };


        // Set the selection string
        String selection = HealthMetricContract.Galleries.COLUMN_NAME_ISADDEDTOPROFILE + "=?";

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
                    cursor.getColumnIndexOrThrow(HealthMetricContract.Galleries.COLUMN_NAME_GALLERYNAME));

            recyclerViewObjects.add(new MetricDisplayObject(galleryId, galleryName, "Photo Gallery", "Gallery"));
        }

        cursor.close();
        readableDatabase.close();
        return recyclerViewObjects;
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
                HealthMetricContract.Notes.COLUMN_NAME_NOTECONTENT
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
                    cursor.getColumnIndexOrThrow(HealthMetricContract.Notes.COLUMN_NAME_NOTECONTENT));

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
                HealthMetricContract.Notifications.COLUMN_NAME_TARGETDATETIME,
                HealthMetricContract.Notifications.COLUMN_NAME_TARGETID
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
            int targetId = cursor.getInt(cursor.getColumnIndexOrThrow(HealthMetricContract.Notifications.COLUMN_NAME_TARGETID));
            String type = cursor.getString(cursor.getColumnIndexOrThrow(HealthMetricContract.Notifications.COLUMN_NAME_TYPE));
            String targetDateTime = cursor.getString(cursor.getColumnIndexOrThrow(HealthMetricContract.Notifications.COLUMN_NAME_TARGETDATETIME));

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
                HealthMetricContract.Galleries.COLUMN_NAME_GALLERYNAME,
                HealthMetricContract.Galleries._ID
        };

        // Set the sort order string.
        String sortOrder = HealthMetricContract.Galleries.COLUMN_NAME_GALLERYNAME + " ASC";

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
                    cursor.getColumnIndexOrThrow(HealthMetricContract.Galleries.COLUMN_NAME_GALLERYNAME));

            photoGallerySpinnerObjects.add(new PhotoGallerySpinnerObject(galleryName, id));
        }

        cursor.close();
        readableDatabase.close();
        return photoGallerySpinnerObjects;
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
                HealthMetricContract.MetricDataEntries.COLUMN_NAME_DATAENTRY
        };

        // Set the selection string.
        String selection = HealthMetricContract.MetricDataEntries.COLUMN_NAME_METRICID + "=?";

        // Set the sort order.
        String sortOrder =
                HealthMetricContract.MetricDataEntries.COLUMN_NAME_DATEOFENTRY + " ASC";

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

            String dataEntry = cursor.getString(cursor.getColumnIndex(HealthMetricContract.MetricDataEntries.COLUMN_NAME_DATAENTRY));
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
                HealthMetricContract.PhotoEntries.COLUMN_NAME_PHOTOENTRYPATH,
                HealthMetricContract.PhotoEntries.COLUMN_NAME_GALLERYID,
                HealthMetricContract.PhotoEntries.COLUMN_NAME_DATEOFENTRY,
        };

        // Set the selection string.
        String selection = HealthMetricContract.PhotoEntries.COLUMN_NAME_GALLERYID + "=?";

        // Sort by the date of entry.
        String sortOrder =
                HealthMetricContract.PhotoEntries.COLUMN_NAME_DATEOFENTRY + " ASC";

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
            String photoEntryPath = cursor.getString(cursor.getColumnIndex(HealthMetricContract.PhotoEntries.COLUMN_NAME_PHOTOENTRYPATH));
            int galleryId = cursor.getInt(cursor.getColumnIndex(HealthMetricContract.PhotoEntries.COLUMN_NAME_GALLERYID));
            String dateOfEntry = cursor.getString(cursor.getColumnIndex(HealthMetricContract.PhotoEntries.COLUMN_NAME_DATEOFENTRY));

            photoEntries.add(new PhotoEntry(id, galleryId, photoEntryPath, dateOfEntry));
        }

        cursor.close();
        readableDatabase.close();

        return photoEntries;
    }

    /**
     * Gets the data entries that have a specified metric id.
     *
     * @param metricId Represents the id of the metric of the data entries retrieved.
     * @return Returns a list of the data entries.
     */
    public List<DataEntryRecyclerViewObject> getDataEntriesByMetricId(int metricId) {

        Metric metric = getMetricById(metricId);
        String unitAbbreviation = "";

        if (metric != null) {
            Unit unit = getUnitById(metric.UnitId);
            if (unit != null) {
                unitAbbreviation = unit.UnitAbbreviation;
            }
        }

        List<DataEntryRecyclerViewObject> dataEntryRecyclerViewObjectList = new ArrayList<>();
        SQLiteDatabase readableDatabase = getReadableDatabase();

        String[] projection = {
                HealthMetricContract.MetricDataEntries._ID,
                HealthMetricContract.MetricDataEntries.COLUMN_NAME_DATAENTRY,
                HealthMetricContract.MetricDataEntries.COLUMN_NAME_DATEOFENTRY,
        };

        String selection = HealthMetricContract.MetricDataEntries.COLUMN_NAME_METRICID + "=?";

        String sortOrder =
                HealthMetricContract.MetricDataEntries.COLUMN_NAME_DATEOFENTRY + " ASC";

        Cursor cursor = readableDatabase.query(
                HealthMetricContract.MetricDataEntries.TABLE_NAME,
                projection,
                selection,
                new String[]{String.valueOf(metricId)},
                null,
                null,
                sortOrder);

        while (cursor.moveToNext()) {
            int id = cursor.getInt(cursor.getColumnIndex(HealthMetricContract.MetricDataEntries._ID));
            String dataEntry = cursor.getString(cursor.getColumnIndex(HealthMetricContract.MetricDataEntries.COLUMN_NAME_DATAENTRY));
            String dateOfEntry = cursor.getString(cursor.getColumnIndex(HealthMetricContract.MetricDataEntries.COLUMN_NAME_DATEOFENTRY));

            dataEntryRecyclerViewObjectList.add(new DataEntryRecyclerViewObject(id, dateOfEntry, dataEntry, unitAbbreviation));
        }

        cursor.close();
        readableDatabase.close();
        return dataEntryRecyclerViewObjectList;
    }

    /**
     * The getDataEntryById retrieves a data entry based on it's id.
     *
     * @param dataEntryId The id of the entry that will be returned.
     * @return The entry with the id specified.
     */
    public DataEntry getDataEntryById(int dataEntryId) {
        SQLiteDatabase readableDatabase = getReadableDatabase();

        String[] projection = {
                HealthMetricContract.MetricDataEntries.COLUMN_NAME_DATAENTRY,
                HealthMetricContract.MetricDataEntries.COLUMN_NAME_METRICID,
                HealthMetricContract.MetricDataEntries.COLUMN_NAME_DATEOFENTRY,

        };

        String selection = HealthMetricContract.MetricDataEntries._ID + "=?";
        String dataEntryIdString = String.valueOf(dataEntryId);

        Cursor cursor = readableDatabase.query(
                HealthMetricContract.MetricDataEntries.TABLE_NAME,   // The table to query
                projection,             // The array of columns to return (pass null to get all)
                selection,              // The columns for the WHERE clause
                new String[]{dataEntryIdString},          // The values for the WHERE clause
                null,                   // don't group the rows
                null,
                null);                      // don't filter by row groups

        if (cursor.moveToFirst()) {
            String dateOfEntry = cursor.getString(cursor.getColumnIndex(HealthMetricContract.MetricDataEntries.COLUMN_NAME_DATEOFENTRY));
            int metricId = cursor.getInt(cursor.getColumnIndex(HealthMetricContract.MetricDataEntries.COLUMN_NAME_METRICID));
            String dataEntry = cursor.getString(cursor.getColumnIndex(HealthMetricContract.MetricDataEntries.COLUMN_NAME_DATAENTRY));

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
     * The getUnitById retrieves a unit based on it's id.
     *
     * @param unitId The id of the unit that will be returned.
     * @return The unit with the id specified.
     */
    public Unit getUnitById(int unitId) {
        SQLiteDatabase readableDatabase = getReadableDatabase();

        String[] projection = {
                HealthMetricContract.Units.COLUMN_NAME_UNITNAME,
                HealthMetricContract.Units.COLUMN_NAME_UNITCATEGORYID,
                HealthMetricContract.Units.COLUMN_NAME_ABBREVIATION
        };

        String selection = HealthMetricContract.Units._ID + "=?";
        String unitIdString = String.valueOf(unitId);

        Cursor cursor = readableDatabase.query(
                HealthMetricContract.Units.TABLE_NAME,   // The table to query
                projection,             // The array of columns to return (pass null to get all)
                selection,              // The columns for the WHERE clause
                new String[]{unitIdString},          // The values for the WHERE clause
                null,                   // don't group the rows
                null,
                null);                      // don't filter by row groups

        if (cursor.moveToFirst()) {
            String unitName = cursor.getString(cursor.getColumnIndex(HealthMetricContract.Units.COLUMN_NAME_UNITNAME));
            int unitCategoryId = cursor.getInt(cursor.getColumnIndex(HealthMetricContract.Units.COLUMN_NAME_UNITCATEGORYID));
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
     * The getMetricById retrieves a metric based on it's id.
     *
     * @param metricID The id of the metric that will be returned.
     * @return The metric with the id specified.
     */
    public Metric getMetricById(int metricID) {
        SQLiteDatabase readableDatabase = getReadableDatabase();

        String[] projection = {
                HealthMetricContract.Metrics.COLUMN_NAME_METRICNAME,
                HealthMetricContract.Metrics.COLUMN_NAME_UNITCATEGORYID,
                HealthMetricContract.Metrics.COLUMN_NAME_UNITID,
                HealthMetricContract.Metrics.COLUMN_NAME_ISADDEDTOPROFILE
        };

        String selection = HealthMetricContract.Metrics._ID + "=?";
        String metricIDString = String.valueOf(metricID);

        Cursor cursor = readableDatabase.query(
                HealthMetricContract.Metrics.TABLE_NAME,   // The table to query
                projection,             // The array of columns to return (pass null to get all)
                selection,              // The columns for the WHERE clause
                new String[]{metricIDString},          // The values for the WHERE clause
                null,                   // don't group the rows
                null,
                null);                      // don't filter by row groups

        if (cursor != null && cursor.moveToFirst()) {
            String metricName = cursor.getString(cursor.getColumnIndex(HealthMetricContract.Metrics.COLUMN_NAME_METRICNAME));
            int unitCategoryId = cursor.getInt(cursor.getColumnIndex(HealthMetricContract.Metrics.COLUMN_NAME_UNITCATEGORYID));
            int unitId = cursor.getInt(cursor.getColumnIndex(HealthMetricContract.Metrics.COLUMN_NAME_UNITID));
            int isAddedToProfile = cursor.getInt(cursor.getColumnIndex(HealthMetricContract.Metrics.COLUMN_NAME_ISADDEDTOPROFILE));

            Metric metric = new Metric(metricID, unitId, metricName, unitCategoryId, isAddedToProfile);
            cursor.close();
            readableDatabase.close();
            return metric;
        } else {
            cursor.close();
            readableDatabase.close();
            return null;
        }
    }

    public PhotoEntry getPhotoEntryById(int photoId) {
        SQLiteDatabase readableDatabase = getReadableDatabase();

        String[] projection = {
                HealthMetricContract.PhotoEntries._ID,
                HealthMetricContract.PhotoEntries.COLUMN_NAME_DATEOFENTRY,
                HealthMetricContract.PhotoEntries.COLUMN_NAME_GALLERYID,
                HealthMetricContract.PhotoEntries.COLUMN_NAME_PHOTOENTRYPATH,
                HealthMetricContract.PhotoEntries.COLUMN_NAME_ISFROMGALLERY
        };

        String selection = HealthMetricContract.PhotoEntries._ID + "=?";
        String photoIdString = String.valueOf(photoId);

        Cursor cursor = readableDatabase.query(
                HealthMetricContract.PhotoEntries.TABLE_NAME,   // The table to query
                projection,             // The array of columns to return (pass null to get all)
                selection,              // The columns for the WHERE clause
                new String[]{photoIdString},          // The values for the WHERE clause
                null,                   // don't group the rows
                null,
                null);                      // don't filter by row groups

        if (cursor != null && cursor.moveToFirst()) {

            String photoEntryPath = cursor.getString(cursor.getColumnIndex(HealthMetricContract.PhotoEntries.COLUMN_NAME_PHOTOENTRYPATH));
            String dateOfEntry = cursor.getString(cursor.getColumnIndex(HealthMetricContract.PhotoEntries.COLUMN_NAME_DATEOFENTRY));

            int id = cursor.getInt(cursor.getColumnIndex(HealthMetricContract.PhotoEntries._ID));
            int galleryId = cursor.getInt(cursor.getColumnIndex(HealthMetricContract.PhotoEntries.COLUMN_NAME_GALLERYID));
            int isFromGallery = cursor.getInt(cursor.getColumnIndex(HealthMetricContract.PhotoEntries.COLUMN_NAME_ISFROMGALLERY));

            PhotoEntry photoEntry = new PhotoEntry(id, galleryId, photoEntryPath, dateOfEntry, isFromGallery);

            cursor.close();
            readableDatabase.close();
            return photoEntry;
        } else {
            Log.d("ERROR", "No photo entry found." + photoId);
            cursor.close();
            readableDatabase.close();
            return null;
        }
    }

    public PhotoGallery getPhotoGalleryById(int galleryId) {
        SQLiteDatabase readableDatabase = getReadableDatabase();

        String[] projection = {
                HealthMetricContract.Galleries.COLUMN_NAME_GALLERYNAME,
                HealthMetricContract.Galleries.COLUMN_NAME_ISADDEDTOPROFILE
        };

        String selection = HealthMetricContract.Galleries._ID + "=?";
        String galleryIdString = String.valueOf(galleryId);

        Cursor cursor = readableDatabase.query(
                HealthMetricContract.Galleries.TABLE_NAME,
                projection,
                selection,
                new String[]{galleryIdString},
                null,
                null,
                null);

        if (cursor != null && cursor.moveToFirst()) {

            String galleryName = cursor.getString(cursor.getColumnIndex(HealthMetricContract.Galleries.COLUMN_NAME_GALLERYNAME));
            int isAddedToProfile = cursor.getInt(cursor.getColumnIndex(HealthMetricContract.Galleries.COLUMN_NAME_ISADDEDTOPROFILE));

            PhotoGallery photoGallery = new PhotoGallery(galleryName, isAddedToProfile);

            cursor.close();
            readableDatabase.close();
            return photoGallery;
        } else {
            Log.d("ERROR", "No photo entry found." + galleryId);
            cursor.close();
            readableDatabase.close();
            return null;
        }
    }

    public Prescription getPrescriptionById(int prescriptionId) {

        SQLiteDatabase readableDatabase = getReadableDatabase();

        String[] projection = {
                HealthMetricContract.Prescriptions.COLUMN_NAME_NAME,
                HealthMetricContract.Prescriptions.COLUMN_NAME_DOSAGEMEASUREMENT,
                HealthMetricContract.Prescriptions.COLUMN_NAME_AMOUNT,
                HealthMetricContract.Prescriptions.COLUMN_NAME_FREQUENCY,
                HealthMetricContract.Prescriptions.COLUMN_NAME_DOSAGEAMOUNT,
                HealthMetricContract.Prescriptions.COLUMN_NAME_FORM,
                HealthMetricContract.Prescriptions.COLUMN_NAME_REASON,
                HealthMetricContract.Prescriptions.COLUMN_NAME_STRENGTH
        };

        String selection = HealthMetricContract.Prescriptions._ID + "=?";
        String prescriptionIdString = String.valueOf(prescriptionId);

        Cursor cursor = readableDatabase.query(
                HealthMetricContract.Prescriptions.TABLE_NAME,
                projection,
                selection,
                new String[]{prescriptionIdString},
                null,
                null,
                null);

        if (cursor != null && cursor.moveToFirst()) {

            String name = cursor.getString(cursor.getColumnIndex(HealthMetricContract.Prescriptions.COLUMN_NAME_NAME));
            int dosageMeasurement = cursor.getInt(cursor.getColumnIndex(HealthMetricContract.Prescriptions.COLUMN_NAME_DOSAGEMEASUREMENT));
            double amount = cursor.getDouble(cursor.getColumnIndex(HealthMetricContract.Prescriptions.COLUMN_NAME_AMOUNT));
            String frequency = cursor.getString(cursor.getColumnIndex(HealthMetricContract.Prescriptions.COLUMN_NAME_FREQUENCY));
            double dosageAmount = cursor.getDouble(cursor.getColumnIndex(HealthMetricContract.Prescriptions.COLUMN_NAME_DOSAGEAMOUNT));
            String form = cursor.getString(cursor.getColumnIndex(HealthMetricContract.Prescriptions.COLUMN_NAME_FORM));
            String reason = cursor.getString(cursor.getColumnIndex(HealthMetricContract.Prescriptions.COLUMN_NAME_REASON));
            String strength = cursor.getString(cursor.getColumnIndex(HealthMetricContract.Prescriptions.COLUMN_NAME_STRENGTH));

            Prescription prescription = new Prescription(prescriptionId, dosageMeasurement, name, form, strength, dosageAmount, frequency, amount, reason);
            cursor.close();
            readableDatabase.close();
            return prescription;

        } else {
            Log.d("ERROR", "No prescription found.");
            cursor.close();
            readableDatabase.close();
            return null;
        }
    }

    public List<PrescriptionDisplayObject> getAllPrescriptions() {
        SQLiteDatabase readableDatabase = getReadableDatabase();

        List<PrescriptionDisplayObject> prescriptionDisplayObjects = new ArrayList<>();


        String[] projection = {
                HealthMetricContract.Prescriptions._ID,
                HealthMetricContract.Prescriptions.COLUMN_NAME_NAME,
                HealthMetricContract.Prescriptions.COLUMN_NAME_DOSAGEAMOUNT,
                HealthMetricContract.Prescriptions.COLUMN_NAME_FREQUENCY,
                HealthMetricContract.Prescriptions.COLUMN_NAME_AMOUNT,
                HealthMetricContract.Prescriptions.COLUMN_NAME_DOSAGEMEASUREMENT,
        };

        Cursor cursor = readableDatabase.query(
                HealthMetricContract.Prescriptions.TABLE_NAME,
                projection,
                null,
                null,
                null,
                null,
                null);

        while (cursor.moveToNext()) {

            int id = cursor.getInt(cursor.getColumnIndex(HealthMetricContract.Prescriptions._ID));
            int dosageMeasurementId = cursor.getInt(cursor.getColumnIndex(HealthMetricContract.Prescriptions.COLUMN_NAME_DOSAGEMEASUREMENT));
            Log.d("TEST", " G" + dosageMeasurementId);
            String name = cursor.getString(cursor.getColumnIndex(HealthMetricContract.Prescriptions.COLUMN_NAME_NAME));
            double dosageAmount = cursor.getDouble(cursor.getColumnIndex(HealthMetricContract.Prescriptions.COLUMN_NAME_DOSAGEAMOUNT));
            String frequency = cursor.getString(cursor.getColumnIndex(HealthMetricContract.Prescriptions.COLUMN_NAME_FREQUENCY));
            double amount = cursor.getDouble(cursor.getColumnIndex(HealthMetricContract.Prescriptions.COLUMN_NAME_AMOUNT));

            DosageMeasurement dosageMeasurement = getDosageMeasurementById(dosageMeasurementId);

            prescriptionDisplayObjects.add(new PrescriptionDisplayObject(id, name, dosageAmount, dosageMeasurement.DosageMeasurement, frequency, amount));

        }

        cursor.close();
        readableDatabase.close();
        return prescriptionDisplayObjects;
    }


    public DosageMeasurement getDosageMeasurementById(int dosageMeasurementId) {

        SQLiteDatabase db = getReadableDatabase();

        String[] projection = {
                HealthMetricContract.DosageMeasurements.COLUMN_NAME_UNITABBREVIATION,
                HealthMetricContract.DosageMeasurements.COLUMN_NAME_DOSAGEMEASUREMENT
        };

        String selection = HealthMetricContract.DosageMeasurements._ID + "=?";
        String dosageMeasurementsId = String.valueOf(dosageMeasurementId);

        Cursor cursor = db.query(
                HealthMetricContract.DosageMeasurements.TABLE_NAME,   // The table to query
                projection,             // The array of columns to return (pass null to get all)
                selection,              // The columns for the WHERE clause
                new String[]{dosageMeasurementsId},          // The values for the WHERE clause
                null,                   // don't group the rows
                null,
                null);                      // don't filter by row groups

        if (cursor != null && cursor.moveToFirst()) {
            String unitAbbreviation = cursor.getString(cursor.getColumnIndex(HealthMetricContract.DosageMeasurements.COLUMN_NAME_UNITABBREVIATION));
            String dosageMeasurementString = cursor.getString(cursor.getColumnIndex(HealthMetricContract.DosageMeasurements.COLUMN_NAME_DOSAGEMEASUREMENT));


            DosageMeasurement dosageMeasurement = new DosageMeasurement(dosageMeasurementString, unitAbbreviation);
            cursor.close();
            db.close();
            return dosageMeasurement;

        } else {
            Log.d("ERROR", "No unit found.");
            cursor.close();
            db.close();
            return null;
        }
    }

    public Notification getNotificationById(int notificationId) {
        SQLiteDatabase readableDatabase = getReadableDatabase();

        String[] projection = {
                HealthMetricContract.Notifications.COLUMN_NAME_TARGETID,
                HealthMetricContract.Notifications.COLUMN_NAME_TARGETDATETIME,
                HealthMetricContract.Notifications.COLUMN_NAME_TYPE
        };

        String selection = HealthMetricContract.Notifications._ID + "=?";
        String notificationIdString = String.valueOf(notificationId);

        Cursor cursor = readableDatabase.query(
                HealthMetricContract.Notifications.TABLE_NAME,
                projection,
                selection,
                new String[]{notificationIdString},
                null,
                null,
                null);

        if (cursor != null && cursor.moveToFirst()) {

            String targetDateTime = cursor.getString(cursor.getColumnIndex(HealthMetricContract.Notifications.COLUMN_NAME_TARGETDATETIME));
            int targetId = cursor.getInt(cursor.getColumnIndex(HealthMetricContract.Notifications.COLUMN_NAME_TARGETID));
            String type = cursor.getString(cursor.getColumnIndex(HealthMetricContract.Notifications.COLUMN_NAME_TYPE));

            Notification notification = new Notification(notificationId, targetId, type, targetDateTime);

            cursor.close();
            readableDatabase.close();
            return notification;
        } else {
            Log.d("ERROR", "No notifications found." + notificationId);
            cursor.close();
            readableDatabase.close();
            return null;
        }
    }

    /**
     * The getAllUnitCategories method returns all unit categories from the database
     *
     * @return A list of all unit categories.
     */
    public List<UnitCategory> getAllUnitCategories() {
        List<UnitCategory> unitCategories = new ArrayList<>();

        SQLiteDatabase db = getReadableDatabase();

        String[] projection = {
                HealthMetricContract.UnitCategories._ID,
                HealthMetricContract.UnitCategories.COLUMN_NAME_UNITCATEGORY,
        };
        Cursor cursor = db.query(
                HealthMetricContract.UnitCategories.TABLE_NAME,
                projection,
                null,
                null,
                null,
                null,
                null,
                null);

        while (cursor.moveToNext()) {
            int unitCategoryId = cursor.getInt(
                    cursor.getColumnIndexOrThrow(HealthMetricContract.UnitCategories._ID));
            String unitCategoryString = cursor.getString(
                    cursor.getColumnIndexOrThrow(HealthMetricContract.UnitCategories.COLUMN_NAME_UNITCATEGORY));

            UnitCategory unitCategory = new UnitCategory(unitCategoryId, unitCategoryString);
            unitCategories.add(unitCategory);
        }
        cursor.close();
        db.close();
        return unitCategories;
    }

    public Note getNoteById(int id) {
        SQLiteDatabase database = getReadableDatabase();

        String[] projection = {
                HealthMetricContract.Notes.COLUMN_NAME_DATEOFENTRY,
                HealthMetricContract.Notes.COLUMN_NAME_NOTECONTENT,
        };

        String selection = HealthMetricContract.Notes._ID + "=?";
        String noteId = String.valueOf(id);

        Cursor cursor = database.query(
                HealthMetricContract.Notes.TABLE_NAME,   // The table to query
                projection,             // The array of columns to return (pass null to get all)
                selection,              // The columns for the WHERE clause
                new String[]{noteId},          // The values for the WHERE clause
                null,                   // don't group the rows
                null,
                null);                      // don't filter by row groups

        if (cursor != null && cursor.moveToFirst()) {
            String noteContent = cursor.getString(cursor.getColumnIndex(HealthMetricContract.Notes.COLUMN_NAME_NOTECONTENT));
            String dateOfEntry = cursor.getString(cursor.getColumnIndex(HealthMetricContract.Notes.COLUMN_NAME_DATEOFENTRY));

            Note note = new Note(dateOfEntry, noteContent);
            cursor.close();
            database.close();
            return note;

        } else {
            Log.d("ERROR", "No Note found.");
            cursor.close();
            database.close();
            return null;
        }
    }

    /**
     * The getUser profile retrieves the user from the database.
     *
     * @return The user is returned.
     */
    public User getUser() {
        User user = null;
        Cursor cursor = null;
        SQLiteDatabase database = getReadableDatabase();
        String selectQuery = "SELECT * FROM " + HealthMetricContract.Users.TABLE_NAME + " WHERE _ID = 1";

        try {
            cursor = database.rawQuery(selectQuery, null);
            if (cursor != null) {
                try {
                    if (cursor.moveToFirst()) {
                        String firstName = cursor.getString(cursor.getColumnIndex(HealthMetricContract.Users.COLUMN_NAME_FIRSTNAME));
                        String lastName = cursor.getString(cursor.getColumnIndex(HealthMetricContract.Users.COLUMN_NAME_LASTNAME));
                        String gender = cursor.getString(cursor.getColumnIndex(HealthMetricContract.Users.COLUMN_NAME_GENDER));
                        String dateOfBirth = cursor.getString(cursor.getColumnIndex(HealthMetricContract.Users.COLUMN_NAME_DATEOFBIRTH));

                        user = new User(firstName, lastName, gender, dateOfBirth);

                    } else {
                        Log.e("SQLite ERROR", "User not found.");
                        cursor.close();
                        return null;
                    }
                } finally {
                    cursor.close();
                    database.close();
                }
            }

        } catch (SQLiteException e) {
            Log.e("SQLite ERROR", e.getMessage());
        }

        return user;
    }

    /**
     * The updateUser method updates the user profile in the database.
     *
     * @param user The user that will be updated.
     * @return An integer value indicating if the update is successful.
     */
    public boolean updateUser(User user) {
        SQLiteDatabase database = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(HealthMetricContract.Users.COLUMN_NAME_FIRSTNAME, user.FirstName);
        values.put(HealthMetricContract.Users.COLUMN_NAME_LASTNAME, user.LastName);
        values.put(HealthMetricContract.Users.COLUMN_NAME_DATEOFBIRTH, user.DateOfBirth);
        values.put(HealthMetricContract.Users.COLUMN_NAME_GENDER, user.Gender);

        return database.update(HealthMetricContract.Users.TABLE_NAME, values, HealthMetricContract.Users._ID + " = 1",
                null) > 0;
    }

    /**
     * The updateDataEntry method updates the data entry profile in the database.
     *
     * @param dataEntry The user that will be updated.
     * @return An integer value indicating if the update is successful.
     */
    public int updateDataEntry(DataEntry dataEntry) {
        SQLiteDatabase database = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(HealthMetricContract.MetricDataEntries.COLUMN_NAME_METRICID, dataEntry.MetricId);
        values.put(HealthMetricContract.MetricDataEntries.COLUMN_NAME_DATEOFENTRY, dataEntry.DateOfEntry);
        values.put(HealthMetricContract.MetricDataEntries.COLUMN_NAME_DATAENTRY, dataEntry.DataEntry);

        return database.update(HealthMetricContract.MetricDataEntries.TABLE_NAME, values, HealthMetricContract.MetricDataEntries._ID + " = " + dataEntry.Id,
                null);
    }

    /**
     * The updateMetric method updates the metric in the database.
     *
     * @param metric The metric that will be updated.
     * @return An integer value indicating if the update is successful.
     */
    public int updateMetric(Metric metric) {
        SQLiteDatabase database = this.getWritableDatabase();

        ContentValues values = new ContentValues();

        values.put(HealthMetricContract.Metrics.COLUMN_NAME_METRICNAME, metric.Name);

        return database.update(HealthMetricContract.Metrics.TABLE_NAME, values, HealthMetricContract.Metrics._ID + " = " + metric.Id,
                null);
    }

    public boolean updateNote(Note note) {
        SQLiteDatabase database = this.getWritableDatabase();

        ContentValues values = new ContentValues();

        values.put(HealthMetricContract.Notes.COLUMN_NAME_DATEOFENTRY, note.DateOfEntry);
        values.put(HealthMetricContract.Notes.COLUMN_NAME_NOTECONTENT, note.NoteContent);

        return database.update(HealthMetricContract.Notes.TABLE_NAME, values, HealthMetricContract.Notes._ID + " = " + note.Id,
                null) > 0;
    }

    /**
     * The addMetricToProfile method adds the metric to the user profile.
     *
     * @param unitId   The unit id of the unit that the metric will use.
     * @param metricId The id of the metric that is being updated.
     * @return An integer value indicating if the update is successful.
     */
    public int addMetricToProfile(int unitId, int metricId) {
        Log.d("TEST", String.valueOf(unitId));
        SQLiteDatabase database = getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(HealthMetricContract.Metrics.COLUMN_NAME_UNITID, unitId);
        values.put(HealthMetricContract.Metrics.COLUMN_NAME_ISADDEDTOPROFILE, 1);

        return database.update(HealthMetricContract.Metrics.TABLE_NAME, values, HealthMetricContract.Units._ID + " = " + metricId,
                null);
    }

    public boolean removeMetricFromProfile(int metricId) {

        SQLiteDatabase database = getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(HealthMetricContract.Metrics.COLUMN_NAME_UNITID, 0);
        values.put(HealthMetricContract.Metrics.COLUMN_NAME_ISADDEDTOPROFILE, 0);

        return database.update(HealthMetricContract.Metrics.TABLE_NAME, values, HealthMetricContract.Units._ID + " = " + metricId,
                null) > 0;
    }

    /**
     * The addGalleryToProfile method adds the gallery to the user profile.
     *
     * @param galleryId The id of the gallery that is being updated.
     * @return An integer value indicating if the update is successful.
     */
    public int addGalleryToProfile(int galleryId) {

        SQLiteDatabase database = getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(HealthMetricContract.Galleries.COLUMN_NAME_ISADDEDTOPROFILE, 1);

        return database.update(HealthMetricContract.Galleries.TABLE_NAME, values, HealthMetricContract.Galleries._ID + " = " + galleryId,
                null);
    }

    public boolean updatePrescription(Prescription prescription) {

        SQLiteDatabase database = this.getWritableDatabase();

        ContentValues values = new ContentValues();

        values.put(HealthMetricContract.Prescriptions.COLUMN_NAME_NAME, prescription.getName());
        values.put(HealthMetricContract.Prescriptions.COLUMN_NAME_REASON, prescription.getReason());
        values.put(HealthMetricContract.Prescriptions.COLUMN_NAME_FREQUENCY, prescription.getFrequency());
        values.put(HealthMetricContract.Prescriptions.COLUMN_NAME_STRENGTH, prescription.getStrength());
        values.put(HealthMetricContract.Prescriptions.COLUMN_NAME_DOSAGEMEASUREMENT, prescription.getDosageMeasurementId());
        values.put(HealthMetricContract.Prescriptions.COLUMN_NAME_FORM, prescription.getForm());
        values.put(HealthMetricContract.Prescriptions.COLUMN_NAME_AMOUNT, prescription.getAmount());

        return database.update(HealthMetricContract.Prescriptions.TABLE_NAME, values, HealthMetricContract.Prescriptions._ID + " = " + prescription.getId(),
                null) > 0;
    }

    public boolean deleteDataEntry(int id) {
        SQLiteDatabase database = this.getWritableDatabase();
        return database.delete(HealthMetricContract.MetricDataEntries.TABLE_NAME, HealthMetricContract.MetricDataEntries._ID + "=" + id, null) > 0;
    }

    public boolean deletePhotoEntryById(int id) {
        SQLiteDatabase database = this.getWritableDatabase();
        return database.delete(HealthMetricContract.PhotoEntries.TABLE_NAME, HealthMetricContract.PhotoEntries._ID + "=" + id, null) > 0;
    }

    public boolean deletePhotoEntriesByGalleryId(int galleryId) {
        SQLiteDatabase database = this.getWritableDatabase();
        return database.delete(HealthMetricContract.PhotoEntries.TABLE_NAME, HealthMetricContract.PhotoEntries.COLUMN_NAME_GALLERYID + "=" + galleryId, null) > 0;
    }

    public boolean deletePrescription(int id) {
        SQLiteDatabase database = this.getWritableDatabase();
        return database.delete(HealthMetricContract.Prescriptions.TABLE_NAME, HealthMetricContract.Prescriptions._ID + "=" + id, null) > 0;
    }

    public boolean deleteNotification(int id) {
        SQLiteDatabase database = this.getWritableDatabase();
        return database.delete(HealthMetricContract.Notifications.TABLE_NAME, HealthMetricContract.Notifications._ID + "=?", new String[]{Integer.toString(id)}) > 0;

    }

    public boolean deleteMetric(int id) {
        deleteDataEntryByMetricId(id);

        SQLiteDatabase database = this.getWritableDatabase();
        return database.delete(HealthMetricContract.Metrics.TABLE_NAME, HealthMetricContract.Metrics._ID + "=?", new String[]{Integer.toString(id)}) > 0;
    }

    public boolean deleteDataEntryByMetricId(int metricId) {

        SQLiteDatabase database = this.getWritableDatabase();
        return database.delete(HealthMetricContract.MetricDataEntries.TABLE_NAME, HealthMetricContract.MetricDataEntries.COLUMN_NAME_METRICID + "=?", new String[]{Integer.toString(metricId)}) > 0;
    }

    public boolean deleteGallery(int galleryId) {
        List<PhotoEntry> photos = getPhotoEntriesByGalleryId(galleryId);

        for (PhotoEntry photoEntry : photos) {
            if (photoEntry.IsFromGallery == 0) {
                Log.d("PATHTEST", photoEntry.PhotoEntryPath);
                File file = new File(photoEntry.PhotoEntryPath);
                file.delete();
            }
        }

        deletePhotoEntriesByGalleryId(galleryId);

        SQLiteDatabase database = this.getWritableDatabase();
        return database.delete(HealthMetricContract.Galleries.TABLE_NAME, HealthMetricContract.Galleries._ID + "=?", new String[]{Integer.toString(galleryId)}) > 0;
    }


    public boolean deleteNoteById(int noteId) {
        SQLiteDatabase database = this.getWritableDatabase();
        return database.delete(HealthMetricContract.Notes.TABLE_NAME, HealthMetricContract.Notes._ID + "=?", new String[]{Integer.toString(noteId)}) > 0;
    }

    public boolean updatePhotoEntry(PhotoEntry photoEntry) {

        SQLiteDatabase database = this.getWritableDatabase();

        ContentValues values = new ContentValues();

        values.put(HealthMetricContract.PhotoEntries.COLUMN_NAME_DATEOFENTRY, photoEntry.DateOfEntry);
        values.put(HealthMetricContract.PhotoEntries.COLUMN_NAME_ISFROMGALLERY, photoEntry.IsFromGallery);
        values.put(HealthMetricContract.PhotoEntries.COLUMN_NAME_PHOTOENTRYPATH, photoEntry.PhotoEntryPath);
        Log.d("PHOTOID", photoEntry.Id + "");

        return database.update(HealthMetricContract.PhotoEntries.TABLE_NAME, values, HealthMetricContract.PhotoEntries._ID + "=?", new String[]{Integer.toString(photoEntry.Id)}) > 0;
    }

    public boolean updateNotification(Notification notification) {

        SQLiteDatabase database = this.getWritableDatabase();

        ContentValues values = new ContentValues();

        values.put(HealthMetricContract.Notifications.COLUMN_NAME_TYPE, notification.NotificationType);
        values.put(HealthMetricContract.Notifications.COLUMN_NAME_TARGETDATETIME, notification.TargetDateTime);
        values.put(HealthMetricContract.Notifications.COLUMN_NAME_TARGETID, notification.TargetId);

        return database.update(HealthMetricContract.Notifications.TABLE_NAME, values, HealthMetricContract.Notifications._ID + "=?", new String[]{Integer.toString(notification.Id)}) > 0;
    }

    public boolean updateGallery(PhotoGallery gallery) {

        SQLiteDatabase database = this.getWritableDatabase();

        ContentValues values = new ContentValues();

        values.put(HealthMetricContract.Galleries.COLUMN_NAME_GALLERYNAME, gallery.Name);

        return database.update(HealthMetricContract.Galleries.TABLE_NAME, values, HealthMetricContract.Galleries._ID + " = " + gallery.Id,
                null) > 0;

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }
}