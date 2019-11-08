package ca.mohawk.HealthMetrics;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
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
 * It contains helper methods for interacting with the HealthMetrics Database.
 */
public class HealthMetricsDbHelper extends SQLiteOpenHelper {

    public static final int DATABASE_VERSION = 1;
    public static final String DATABASE_NAME = "HealthMetrics.db";
    private static HealthMetricsDbHelper sInstance;

    private HealthMetricsDbHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    public static synchronized HealthMetricsDbHelper getInstance(Context context) {
        if (sInstance == null) {
            sInstance = new HealthMetricsDbHelper(context.getApplicationContext());
        }
        return sInstance;
    }

    /**
     * The onCreate method runs when the database is created.
     * It executes the Create statments from HealthMetricContract.
     *
     * @param db represents the database.
     */
    @Override
    public void onCreate(SQLiteDatabase db) {
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

    public void addDosageMeasurement(DosageMeasurement dosageMeasurement) {

        SQLiteDatabase writableDatabase = getWritableDatabase();
        writableDatabase.beginTransaction();

        try {
            ContentValues values = new ContentValues();
            values.put(HealthMetricContract.DosageMeasurements.COLUMN_NAME_DOSAGEMEASUREMENT, dosageMeasurement.DosageMeasurement);
            values.put(HealthMetricContract.DosageMeasurements.COLUMN_NAME_UNITABBREVIATION, dosageMeasurement.UnitAbbreviation);

            writableDatabase.insertOrThrow(HealthMetricContract.DosageMeasurements.TABLE_NAME, null, values);
            writableDatabase.setTransactionSuccessful();
            Log.d("TEST add", dosageMeasurement.DosageMeasurement);

        } catch (Exception e) {
            Log.d("TAG", "Error while trying to add dosage measurement to database");
        } finally {
            writableDatabase.endTransaction();
            writableDatabase.close();
        }
    }

    public boolean addNote(Note note) {

        SQLiteDatabase writableDatabase = getWritableDatabase();
        writableDatabase.beginTransaction();
        long id = 0;

        try {
            ContentValues values = new ContentValues();
            values.put(HealthMetricContract.Notes.COLUMN_NAME_NOTECONTENT, note.NoteContent);
            values.put(HealthMetricContract.Notes.COLUMN_NAME_DATEOFENTRY, note.DateOfEntry);

            id = writableDatabase.insertOrThrow(HealthMetricContract.Notes.TABLE_NAME, null, values);
            writableDatabase.setTransactionSuccessful();
        } catch (Exception e) {
            Log.d("TAG", "Error while trying to add notes to database");
        } finally {
            writableDatabase.endTransaction();
            writableDatabase.close();
        }
        return id > 0;
    }

    /**
     * The addUser method adds the user to the database.
     *
     * @param user represents the user to be added to the database.
     */
    public boolean addUser(User user) {
        long id = 0;
        SQLiteDatabase writableDatabase = getWritableDatabase();
        writableDatabase.beginTransaction();

        try {
            ContentValues values = new ContentValues();
            values.put(HealthMetricContract.Users.COLUMN_NAME_FIRSTNAME, user.FirstName);
            values.put(HealthMetricContract.Users.COLUMN_NAME_LASTNAME, user.LastName);
            values.put(HealthMetricContract.Users.COLUMN_NAME_DATEOFBIRTH, user.DateOfBirth);
            values.put(HealthMetricContract.Users.COLUMN_NAME_GENDER, user.Gender);

            id = writableDatabase.insertOrThrow(HealthMetricContract.Users.TABLE_NAME, null, values);
            writableDatabase.setTransactionSuccessful();

        } catch (Exception e) {
            Log.e("SQL Error", "Error while trying to add the user to the database." + e);
        } finally {
            writableDatabase.endTransaction();
            writableDatabase.close();
        }
        return id > 0;
    }

    public boolean addPrescription(Prescription prescription) {

        SQLiteDatabase writableDatabase = getWritableDatabase();
        writableDatabase.beginTransaction();
        long id = -1;

        try {
            ContentValues values = new ContentValues();
            values.put(HealthMetricContract.Prescriptions.COLUMN_NAME_NAME, prescription.Name);
            values.put(HealthMetricContract.Prescriptions.COLUMN_NAME_AMOUNT, prescription.Amount);
            values.put(HealthMetricContract.Prescriptions.COLUMN_NAME_DOSAGEAMOUNT, prescription.DosageAmount);
            values.put(HealthMetricContract.Prescriptions.COLUMN_NAME_DOSAGEMEASUREMENT, prescription.DosageMeasurementId);
            values.put(HealthMetricContract.Prescriptions.COLUMN_NAME_FORM, prescription.Form);
            values.put(HealthMetricContract.Prescriptions.COLUMN_NAME_FREQUENCY, prescription.Frequency);
            values.put(HealthMetricContract.Prescriptions.COLUMN_NAME_STRENGTH, prescription.Strength);
            values.put(HealthMetricContract.Prescriptions.COLUMN_NAME_REASON, prescription.Reason);

           id = writableDatabase.insertOrThrow(HealthMetricContract.Prescriptions.TABLE_NAME, null, values);
            writableDatabase.setTransactionSuccessful();

        } catch (Exception e) {
            Log.d("TAG", "Error while trying to add prescription to database");
        } finally {
            writableDatabase.endTransaction();
            writableDatabase.close();
        }
        return id > 0;
    }

    /**
     * The addPhotoGallery method adds the gallery to the database.
     *
     * @param photoGallery represents the gallery to be added to the database.
     */
    public boolean addPhotoGallery(PhotoGallery photoGallery) {

        SQLiteDatabase writableDatabase = getWritableDatabase();
        writableDatabase.beginTransaction();
        long id = 0;
        try {
            ContentValues values = new ContentValues();
            values.put(HealthMetricContract.Galleries.COLUMN_NAME_GALLERYNAME, photoGallery.Name);
            values.put(HealthMetricContract.Galleries.COLUMN_NAME_ISADDEDTOPROFILE, photoGallery.IsAddedToProfile);

            id = writableDatabase.insertOrThrow(HealthMetricContract.Galleries.TABLE_NAME, null, values);
            writableDatabase.setTransactionSuccessful();
        } catch (Exception e) {
            Log.d("TAG", "Error while trying to add gallery to database");
        } finally {
            writableDatabase.endTransaction();
            writableDatabase.close();
        }

        return id > 0;
    }

    /**
     * The addMetric method adds the metric to the database.
     *
     * @param metric represents the metric to be added to the database.
     */
    public boolean addMetric(Metric metric) {

        SQLiteDatabase writableDatabase = getWritableDatabase();
        writableDatabase.beginTransaction();
        long id = 0;

        try {
            ContentValues values = new ContentValues();
            values.put(HealthMetricContract.Metrics.COLUMN_NAME_METRICNAME, metric.Name);
            values.put(HealthMetricContract.Metrics.COLUMN_NAME_UNITID, metric.UnitId);
            values.put(HealthMetricContract.Metrics.COLUMN_NAME_UNITCATEGORYID, metric.UnitCategoryId);
            values.put(HealthMetricContract.Metrics.COLUMN_NAME_ISADDEDTOPROFILE, metric.IsAddedToProfile);

            id = writableDatabase.insertOrThrow(HealthMetricContract.Metrics.TABLE_NAME, null, values);
            writableDatabase.setTransactionSuccessful();
        } catch (Exception e) {
            Log.d("TAG", "Error while trying to add metric to database");
        } finally {
            writableDatabase.endTransaction();
            writableDatabase.close();
        }
        return id > 0;
    }

    public boolean addPhotoEntry(PhotoEntry photoEntry) {

        SQLiteDatabase writableDatabase = getWritableDatabase();
        writableDatabase.beginTransaction();
        long id = 0;
        try {
            ContentValues values = new ContentValues();
            values.put(HealthMetricContract.PhotoEntries.COLUMN_NAME_DATEOFENTRY, photoEntry.DateOfEntry);
            values.put(HealthMetricContract.PhotoEntries.COLUMN_NAME_GALLERYID, photoEntry.PhotoGalleryId);
            values.put(HealthMetricContract.PhotoEntries.COLUMN_NAME_PHOTOENTRYPATH, photoEntry.PhotoEntryPath);

            id = writableDatabase.insertOrThrow(HealthMetricContract.PhotoEntries.TABLE_NAME, null, values);
            writableDatabase.setTransactionSuccessful();
        } catch (Exception e) {
            Log.d("TAG", "Error while trying to add metric to database");
        } finally {
            writableDatabase.endTransaction();
            writableDatabase.close();
        }
        return id > 0;
    }

    /**
     * The addDataEntry adds the metric to the database.
     *
     * @param dataEntry represents the metric data entry to be added to the database.
     */
    public boolean addDataEntry(DataEntry dataEntry) {
        SQLiteDatabase writableDatabase = getWritableDatabase();
        writableDatabase.beginTransaction();
        long id = -1;

        try {
            ContentValues values = new ContentValues();
            values.put(HealthMetricContract.MetricDataEntries.COLUMN_NAME_DATAENTRY, dataEntry.DataEntry);
            values.put(HealthMetricContract.MetricDataEntries.COLUMN_NAME_DATEOFENTRY, dataEntry.DateOfEntry);
            values.put(HealthMetricContract.MetricDataEntries.COLUMN_NAME_METRICID, dataEntry.MetricId);

            id = writableDatabase.insertOrThrow(HealthMetricContract.MetricDataEntries.TABLE_NAME, null, values);
            writableDatabase.setTransactionSuccessful();
        } catch (Exception e) {
            Log.d("TAG", "Error while trying to add data entry to database");
        } finally {
            writableDatabase.endTransaction();
            writableDatabase.close();
        }
        return id > 0;
    }

    public int addNotification(Notification notification) {
        SQLiteDatabase writableDatabase = getWritableDatabase();
        writableDatabase.beginTransaction();
        Long id = new Long(-1);
        try {
            ContentValues values = new ContentValues();
            values.put(HealthMetricContract.Notifications.COLUMN_NAME_TARGETDATETIME, notification.TargetDateTime);
            values.put(HealthMetricContract.Notifications.COLUMN_NAME_TARGETID, notification.TargetId);
            values.put(HealthMetricContract.Notifications.COLUMN_NAME_TYPE, notification.NotificationType);

            id = writableDatabase.insertOrThrow(HealthMetricContract.Notifications.TABLE_NAME, null, values);
            writableDatabase.setTransactionSuccessful();
        } catch (Exception e) {
            Log.d("TAG", "Error while trying to add notification to database");
        } finally {
            writableDatabase.endTransaction();
            writableDatabase.close();
        }

        return id.intValue();
    }


    /**
     * The addUnit method adds the unit to the database.
     *
     * @param unit represents the unit to be added to the database.
     */
    public void addUnit(Unit unit) {

        SQLiteDatabase writableDatabase = getWritableDatabase();
        writableDatabase.beginTransaction();

        try {
            ContentValues values = new ContentValues();
            values.put(HealthMetricContract.Units.COLUMN_NAME_UNITNAME, unit.UnitName);
            values.put(HealthMetricContract.Units.COLUMN_NAME_ABBREVIATION, unit.UnitAbbreviation);
            values.put(HealthMetricContract.Units.COLUMN_NAME_UNITCATEGORYID, unit.UnitCategoryId);

            writableDatabase.insertOrThrow(HealthMetricContract.Units.TABLE_NAME, null, values);
            writableDatabase.setTransactionSuccessful();
        } catch (Exception e) {
            Log.d("TAG", "Error while trying to add unit to database");
        } finally {
            writableDatabase.endTransaction();
            writableDatabase.close();
        }
    }

    /**
     * The addUnitCategory method adds the unitcategory to the database.
     *
     * @param unitCategory represents the unit category to be added to the database.
     */
    public void addUnitCategory(UnitCategory unitCategory) {

        SQLiteDatabase writableDatabase = getWritableDatabase();
        writableDatabase.beginTransaction();

        try {
            ContentValues values = new ContentValues();

            if (unitCategory.Id != 0) {
                values.put(HealthMetricContract.UnitCategories._ID, unitCategory.Id);
            }

            values.put(HealthMetricContract.UnitCategories.COLUMN_NAME_UNITCATEGORY, unitCategory.UnitCategory);

            writableDatabase.insertOrThrow(HealthMetricContract.UnitCategories.TABLE_NAME, null, values);
            writableDatabase.setTransactionSuccessful();

        } catch (Exception e) {
            Log.d("TAG", "Error while trying to add unit category to database");
        } finally {
            writableDatabase.endTransaction();
            writableDatabase.close();
        }
    }

    public void seedDatabase() {
        seedUnitCategories();
        seedUnits();
        seedMetrics();
        seedDosageMeasurements();
    }

    private void seedDosageMeasurements() {
        List<DosageMeasurement> dosageMeasurementList = new ArrayList<>();

        DosageMeasurement milliliters = new DosageMeasurement("Milliliters", "ml");
        dosageMeasurementList.add(milliliters);

        DosageMeasurement teaspoon = new DosageMeasurement("Teaspoons", "tsp");
        dosageMeasurementList.add(teaspoon);

        DosageMeasurement gram = new DosageMeasurement("Grams", "tsp");
        dosageMeasurementList.add(gram);

        DosageMeasurement milligram = new DosageMeasurement("Milligrams", "mg");
        dosageMeasurementList.add(milligram);

        DosageMeasurement tablet = new DosageMeasurement("Tablets", "tab");
        dosageMeasurementList.add(tablet);

        DosageMeasurement capsule = new DosageMeasurement("Capsule", "cap");
        dosageMeasurementList.add(capsule);

        for (DosageMeasurement dosageMeasurement : dosageMeasurementList) {
            addDosageMeasurement(dosageMeasurement);
        }
    }

    /**
     * The seedUnitCategories method seeds unit categories for the database.
     */
    private void seedUnitCategories() {
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

        for (UnitCategory unitCategory : unitCategories) {
            addUnitCategory(unitCategory);
        }
    }

    /**
     * The seedUnits method seeds units for the database.
     */
    private void seedUnits() {

        ArrayList<Unit> unitArrayList = new ArrayList<Unit>();

        Unit centimeters = new Unit("Centimeters", "cm", 1);
        Unit meters = new Unit("Meters", "m", 1);
        Unit inches = new Unit("Inches", "in", 1);
        Unit feet = new Unit("Feet", "ft", 1);

        unitArrayList.add(centimeters);
        unitArrayList.add(meters);
        unitArrayList.add(inches);
        unitArrayList.add(feet);

        Unit kilograms = new Unit("Kilograms", "kg", 2);
        Unit grams = new Unit("Grams", "g", 2);
        Unit milligrams = new Unit("Milligrams", "mg", 2);
        Unit pounds = new Unit("Pounds", "lb", 2);

        unitArrayList.add(kilograms);
        unitArrayList.add(grams);
        unitArrayList.add(milligrams);
        unitArrayList.add(pounds);

        Unit hours = new Unit("Hours", "hrs", 3);
        Unit minutes = new Unit("Minutes", "min", 3);
        Unit seconds = new Unit("Seconds", "s", 3);

        unitArrayList.add(hours);
        unitArrayList.add(minutes);
        unitArrayList.add(seconds);

        Unit litres = new Unit("Litres", "l", 4);
        Unit milliliter = new Unit("Milliliters", "ml", 4);
        Unit ounce = new Unit("Ounces", "fl oz", 4);
        Unit cupMilliliter = new Unit("Cup(240ml)", "Cups", 4);
        Unit cupOunce = new Unit("Cup(8.4 fl oz)", "Cups", 4);

        unitArrayList.add(litres);
        unitArrayList.add(milliliter);
        unitArrayList.add(ounce);
        unitArrayList.add(cupMilliliter);
        unitArrayList.add(cupOunce);

        Unit systolicBloodPressure = new Unit("Systolic Blood Pressure", "mmHg", 5);
        Unit diastolicBloodPressure = new Unit("Diastolic Blood Pressure", "mmHg", 5);

        unitArrayList.add(systolicBloodPressure);
        unitArrayList.add(diastolicBloodPressure);

        for (Unit unit : unitArrayList) {
            addUnit(unit);
        }
    }

    /**
     * The seedMetrics method seeds netrics for the database.
     */
    private void seedMetrics() {
        ArrayList<Metric> metricArrayList = new ArrayList<Metric>();

        Metric leftBicepSize = new Metric(0, "Left Bicep Size", 1, 0);
        Metric rightBicepSize = new Metric(0, "Right Bicep Size", 1, 0);

        metricArrayList.add(leftBicepSize);
        metricArrayList.add(rightBicepSize);

        Metric bloodPressure = new Metric(0, "Blood Pressure", 5, 0);
        metricArrayList.add(bloodPressure);

        Metric bodyHeight = new Metric(0, "Body Height", 1, 0);
        metricArrayList.add(bodyHeight);

        Metric leftCalfSize = new Metric(0, "Left Calf Size", 1, 0);
        Metric rightCalfSize = new Metric(0, "Right Calf Size", 1, 0);

        metricArrayList.add(leftCalfSize);
        metricArrayList.add(rightCalfSize);

        Metric chestSize = new Metric(0, "Chest Size", 1, 0);
        metricArrayList.add(chestSize);

        Metric sleepDuration = new Metric(0, "Sleep Duration", 3, 0);
        metricArrayList.add(sleepDuration);

        Metric waistSize = new Metric(0, "Waist Size", 1, 0);
        metricArrayList.add(waistSize);

        Metric waterIntake = new Metric(0, "Water Intake", 4, 0);
        metricArrayList.add(waterIntake);

        for (Metric metric : metricArrayList) {
            addMetric(metric);
        }
    }

    /**
     * The getAllMetrics method get all Metrics from the database.
     *
     * @return A list of MetricSpinnerObjects is returned.
     */
    public List<MetricSpinnerObject> getAllMetrics() {
        SQLiteDatabase readableDatabase = getReadableDatabase();

        String[] projection = {
                HealthMetricContract.Metrics._ID,
                HealthMetricContract.Metrics.COLUMN_NAME_METRICNAME,
                HealthMetricContract.Metrics.COLUMN_NAME_UNITCATEGORYID
        };

        String sortOrder = HealthMetricContract.Metrics.COLUMN_NAME_METRICNAME + " ASC";

        Cursor cursor = readableDatabase.query(
                HealthMetricContract.Metrics.TABLE_NAME,
                projection,
                null,
                null,
                null,
                null,
                sortOrder);

        List metrics = new ArrayList<MetricSpinnerObject>();

        while (cursor.moveToNext()) {
            String metricName = cursor.getString(
                    cursor.getColumnIndexOrThrow(HealthMetricContract.Metrics.COLUMN_NAME_METRICNAME));

            int unitCategoryId = cursor.getInt(
                    cursor.getColumnIndexOrThrow(HealthMetricContract.Metrics.COLUMN_NAME_UNITCATEGORYID));

            int id = cursor.getInt(
                    cursor.getColumnIndexOrThrow(HealthMetricContract.Metrics._ID));
            metrics.add(new MetricSpinnerObject(unitCategoryId, metricName, id));
        }

        cursor.close();
        readableDatabase.close();
        return metrics;
    }

    public List<DosageMeasurement> getAllDosageMeasurements() {

        SQLiteDatabase readableDatabase = getReadableDatabase();

        String[] projection = {
                HealthMetricContract.DosageMeasurements._ID,
                HealthMetricContract.DosageMeasurements.COLUMN_NAME_DOSAGEMEASUREMENT,
                HealthMetricContract.DosageMeasurements.COLUMN_NAME_UNITABBREVIATION
        };

        String sortOrder = HealthMetricContract.DosageMeasurements.COLUMN_NAME_DOSAGEMEASUREMENT + " ASC";

        Cursor cursor = readableDatabase.query(
                HealthMetricContract.DosageMeasurements.TABLE_NAME,
                projection,
                null,
                null,
                null,
                null,
                sortOrder);

        List dosageMeasurements = new ArrayList<HealthMetricContract.DosageMeasurements>();

        while (cursor.moveToNext()) {
            String dosageMeasurement = cursor.getString(
                    cursor.getColumnIndexOrThrow(HealthMetricContract.DosageMeasurements.COLUMN_NAME_DOSAGEMEASUREMENT));

            String unitAbbreviation = cursor.getString(
                    cursor.getColumnIndexOrThrow(HealthMetricContract.DosageMeasurements.COLUMN_NAME_UNITABBREVIATION));

            int id = cursor.getInt(
                    cursor.getColumnIndexOrThrow(HealthMetricContract.DosageMeasurements._ID));
            Log.d("TEST", id + " ID");

            dosageMeasurements.add(new DosageMeasurement(id, unitAbbreviation, dosageMeasurement));
        }

        cursor.close();
        readableDatabase.close();
        return dosageMeasurements;
    }

    /**
     * The getAllSpinnerUnits method gets units based on the unit category id.
     *
     * @param unitCategoryId is the unit category id of units that will be returned.
     * @return A list of UnitSpinner objects is returned.
     */
    public List<UnitSpinnerObject> getAllSpinnerUnits(int unitCategoryId) {
        SQLiteDatabase readableDatabase = getReadableDatabase();

        String[] projection = {
                HealthMetricContract.Units._ID,
                HealthMetricContract.Units.COLUMN_NAME_UNITNAME
        };

        String unitCategoryIdString = String.valueOf(unitCategoryId);
        String sortOrder = HealthMetricContract.Units.COLUMN_NAME_UNITNAME + " ASC";

        String selection = HealthMetricContract.Units.COLUMN_NAME_UNITCATEGORYID + "=?";

        Cursor cursor = readableDatabase.query(
                HealthMetricContract.Units.TABLE_NAME,
                projection,
                selection,
                new String[]{unitCategoryIdString},
                null,
                null,
                sortOrder);

        List unitSpinnerObjects = new ArrayList<UnitSpinnerObject>();

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
     * The getAddedMetricsAndGalleries method retrieves the added metrics and galleries.
     *
     * @return A list of MetricRecyclerViewObjects is returned.
     */
    public List<MetricDisplayObject> getAddedMetricsAndGalleries() {

        List<MetricDisplayObject> recyclerViewObjects = new ArrayList<MetricDisplayObject>();
        recyclerViewObjects = getAddedMetrics(recyclerViewObjects);
        recyclerViewObjects = getAddedPhotoGalleries(recyclerViewObjects);
        recyclerViewObjects = getAllNotes(recyclerViewObjects);
        return recyclerViewObjects;
    }

    /**
     * The getAddedMetrics method queries the database for added metrics.
     *
     * @param recyclerViewObjects represents the list that will be returned.
     * @return A list of MetricRecyclerViewObjects is returned.
     */
    public List<MetricDisplayObject> getAddedMetrics(List<MetricDisplayObject> recyclerViewObjects) {

        SQLiteDatabase readableDatabase = getReadableDatabase();

        String[] projection = {
                HealthMetricContract.Metrics._ID,
                HealthMetricContract.Metrics.COLUMN_NAME_METRICNAME,
                HealthMetricContract.Metrics.COLUMN_NAME_UNITID
        };

        String selection = HealthMetricContract.Metrics.COLUMN_NAME_ISADDEDTOPROFILE + "=?";

        Cursor cursor = readableDatabase.query(
                HealthMetricContract.Metrics.TABLE_NAME,
                projection,
                selection,
                new String[]{String.valueOf(1)},
                null,
                null,
                null);

        while (cursor.moveToNext()) {
            String metricName = cursor.getString(
                    cursor.getColumnIndexOrThrow(HealthMetricContract.Metrics.COLUMN_NAME_METRICNAME));
            Log.d("TEST", metricName);
            int metricId = cursor.getInt(
                    cursor.getColumnIndexOrThrow(HealthMetricContract.Metrics._ID));

            int unitId = cursor.getInt(
                    cursor.getColumnIndexOrThrow(HealthMetricContract.Metrics.COLUMN_NAME_UNITID));

            String dataEntry = getLatestDataEntryValue(metricId);


            if (!dataEntry.equals("No Data Available")) {
                Unit unit = getUnitById(unitId);
                dataEntry = dataEntry + " " + unit.UnitAbbreviation;
            }
            recyclerViewObjects.add(new MetricDisplayObject(metricId, metricName, dataEntry, "Quantitative"));
        }

        cursor.close();
        readableDatabase.close();
        return recyclerViewObjects;
    }

    /**
     * The getAddedPhotoGalleries method queries the database for added photo galleries.
     *
     * @param recyclerViewObjects represents the list that will be returned.
     * @return A list of MetricRecyclerViewObjects is returned.
     */
    public List<MetricDisplayObject> getAddedPhotoGalleries(List<MetricDisplayObject> recyclerViewObjects) {

        SQLiteDatabase readableDatabase = getReadableDatabase();

        String[] projection = {
                HealthMetricContract.Galleries._ID,
                HealthMetricContract.Galleries.COLUMN_NAME_GALLERYNAME
        };

        String selection = HealthMetricContract.Galleries.COLUMN_NAME_ISADDEDTOPROFILE + "=?";

        Cursor cursor = readableDatabase.query(
                HealthMetricContract.Galleries.TABLE_NAME,
                projection,
                selection,
                new String[]{String.valueOf(1)},
                null,
                null,
                null);

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

    public List<MetricDisplayObject> getAllNotes(List<MetricDisplayObject> recyclerViewObjects) {
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

    public ArrayList<Notification> getAllNotifications() {
        SQLiteDatabase readableDatabase = getReadableDatabase();
        ArrayList<Notification> notificationArrayList = new ArrayList<>();
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

        while (cursor.moveToNext()) {
            int id = cursor.getInt(cursor.getColumnIndexOrThrow(HealthMetricContract.Notifications._ID));
            int targetId = cursor.getInt(cursor.getColumnIndexOrThrow(HealthMetricContract.Notifications.COLUMN_NAME_TARGETID));
            String type = cursor.getString(cursor.getColumnIndexOrThrow(HealthMetricContract.Notifications.COLUMN_NAME_TYPE));
            String targetDateTime = cursor.getString(cursor.getColumnIndexOrThrow(HealthMetricContract.Notifications.COLUMN_NAME_TARGETDATETIME));

            notificationArrayList.add(new Notification(id, targetId, type, targetDateTime));
        }

        cursor.close();
        readableDatabase.close();
        return notificationArrayList;
    }

    /**
     * The getAllPhotoGalleries method gets all photo galleries.
     *
     * @return The list of PhotoGallerySpinnerObjects.
     */
    public List<PhotoGallerySpinnerObject> getAllPhotoGalleries() {
        SQLiteDatabase readableDatabase = getReadableDatabase();

        String[] projection = {
                HealthMetricContract.Galleries.COLUMN_NAME_GALLERYNAME,
                HealthMetricContract.Galleries._ID
        };

        String sortOrder = HealthMetricContract.Galleries.COLUMN_NAME_GALLERYNAME + " ASC";

        Cursor cursor = readableDatabase.query(
                HealthMetricContract.Galleries.TABLE_NAME,
                projection,
                null,
                null,
                null,
                null,
                sortOrder);

        List photoGallerySpinnerObjects = new ArrayList<PhotoGallerySpinnerObject>();

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
     * The getLatestDataEntryValue method retrieves the latest data entry for a specified metric.
     *
     * @param metricId The id of the metric of the data entry retrieved.
     * @return The value of the latest data entry retrieved.
     */
    public String getLatestDataEntryValue(int metricId) {
        SQLiteDatabase readableDatabase = getReadableDatabase();

        String[] projection = {
                HealthMetricContract.MetricDataEntries.COLUMN_NAME_DATAENTRY
        };

        String selection = HealthMetricContract.MetricDataEntries.COLUMN_NAME_METRICID + "=?";
        String sortOrder =
                HealthMetricContract.MetricDataEntries.COLUMN_NAME_DATEOFENTRY + " ASC";
        Cursor cursor = readableDatabase.query(
                HealthMetricContract.MetricDataEntries.TABLE_NAME,   // The table to query
                projection,             // The array of columns to return (pass null to get all)
                selection,              // The columns for the WHERE clause
                new String[]{String.valueOf(metricId)},          // The values for the WHERE clause
                null,                   // don't group the rows
                null,                   // don't filter by row groups
                sortOrder);

        if (cursor != null && cursor.moveToFirst()) {
            String dataEntry = cursor.getString(cursor.getColumnIndex(HealthMetricContract.MetricDataEntries.COLUMN_NAME_DATAENTRY));
            cursor.close();
            readableDatabase.close();
            return dataEntry;
        } else {
            cursor.close();
            readableDatabase.close();
            return "No Data Available";
        }
    }

    public List<PhotoEntry> getPhotoEntriesByGalleryId(int photoGalleryId) {

        List<PhotoEntry> photoEntriesList = new ArrayList<>();
        SQLiteDatabase readableDatabase = getReadableDatabase();

        String[] projection = {
                HealthMetricContract.PhotoEntries._ID,
                HealthMetricContract.PhotoEntries.COLUMN_NAME_PHOTOENTRYPATH,
                HealthMetricContract.PhotoEntries.COLUMN_NAME_GALLERYID,
                HealthMetricContract.PhotoEntries.COLUMN_NAME_DATEOFENTRY,
        };

        String selection = HealthMetricContract.PhotoEntries.COLUMN_NAME_GALLERYID + "=?";

        String sortOrder =
                HealthMetricContract.PhotoEntries.COLUMN_NAME_DATEOFENTRY + " ASC";

        Cursor cursor = readableDatabase.query(
                HealthMetricContract.PhotoEntries.TABLE_NAME,   // The table to query
                projection,             // The array of columns to return (pass null to get all)
                selection,              // The columns for the WHERE clause
                new String[]{String.valueOf(photoGalleryId)},          // The values for the WHERE clause
                null,                   // don't group the rows
                null,                   // don't filter by row groups
                sortOrder);

        while (cursor.moveToNext()) {
            int id = cursor.getInt(cursor.getColumnIndex(HealthMetricContract.PhotoEntries._ID));
            String photoEntryPath = cursor.getString(cursor.getColumnIndex(HealthMetricContract.PhotoEntries.COLUMN_NAME_PHOTOENTRYPATH));
            int galleryId = cursor.getInt(cursor.getColumnIndex(HealthMetricContract.PhotoEntries.COLUMN_NAME_GALLERYID));
            String dateOfEntry = cursor.getString(cursor.getColumnIndex(HealthMetricContract.PhotoEntries.COLUMN_NAME_DATEOFENTRY));

            photoEntriesList.add(new PhotoEntry(id, galleryId, photoEntryPath, dateOfEntry));
        }
        cursor.close();
        readableDatabase.close();
        return photoEntriesList;
    }

    /**
     * The getDataEntriesByMetricId method retrieves the data entries for a specified metric.
     *
     * @param metricId The id of the metric of the data entries retrieved.
     * @return The list of the data entries retrieved.
     */
    public List<DataEntryDisplayObject> getDataEntriesByMetricId(int metricId) {

        Metric metric = getMetricById(metricId);
        Unit unit = getUnitById(metric.UnitId);

        List<DataEntryDisplayObject> dataEntryDisplayObjectList = new ArrayList<DataEntryDisplayObject>();
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
                HealthMetricContract.MetricDataEntries.TABLE_NAME,   // The table to query
                projection,             // The array of columns to return (pass null to get all)
                selection,              // The columns for the WHERE clause
                new String[]{String.valueOf(metricId)},          // The values for the WHERE clause
                null,                   // don't group the rows
                null,                   // don't filter by row groups
                sortOrder);

        while (cursor.moveToNext()) {
            int id = cursor.getInt(cursor.getColumnIndex(HealthMetricContract.MetricDataEntries._ID));
            String dataEntry = cursor.getString(cursor.getColumnIndex(HealthMetricContract.MetricDataEntries.COLUMN_NAME_DATAENTRY));
            String dateOfEntry = cursor.getString(cursor.getColumnIndex(HealthMetricContract.MetricDataEntries.COLUMN_NAME_DATEOFENTRY));
            dataEntryDisplayObjectList.add(new DataEntryDisplayObject(id, dateOfEntry, dataEntry, unit.UnitAbbreviation));
        }
        cursor.close();
        readableDatabase.close();
        return dataEntryDisplayObjectList;
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

        if (cursor != null && cursor.moveToFirst()) {
            String dateOfEntry = cursor.getString(cursor.getColumnIndex(HealthMetricContract.MetricDataEntries.COLUMN_NAME_DATEOFENTRY));
            int metricId = cursor.getInt(cursor.getColumnIndex(HealthMetricContract.MetricDataEntries.COLUMN_NAME_METRICID));
            String dataEntry = cursor.getString(cursor.getColumnIndex(HealthMetricContract.MetricDataEntries.COLUMN_NAME_DATAENTRY));

            DataEntry dataEntryObject = new DataEntry(metricId, dataEntry, dateOfEntry);
            cursor.close();
            readableDatabase.close();
            return dataEntryObject;
        } else {
            Log.d("ERROR", "No data entry found.");
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

        if (cursor != null && cursor.moveToFirst()) {
            String unitName = cursor.getString(cursor.getColumnIndex(HealthMetricContract.Units.COLUMN_NAME_UNITNAME));
            int unitCategoryId = cursor.getInt(cursor.getColumnIndex(HealthMetricContract.Units.COLUMN_NAME_UNITCATEGORYID));
            String unitAbbreviation = cursor.getString(cursor.getColumnIndex(HealthMetricContract.Units.COLUMN_NAME_ABBREVIATION));

            Unit unit = new Unit(unitName, unitAbbreviation, unitCategoryId);
            cursor.close();
            readableDatabase.close();
            return unit;

        } else {
            Log.d("ERROR", "No unit found.");
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
            Log.d("ERROR", "No metric found." + metricID);
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
    public boolean updateDataEntry(DataEntry dataEntry) {
        SQLiteDatabase database = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(HealthMetricContract.MetricDataEntries.COLUMN_NAME_METRICID, dataEntry.MetricId);
        values.put(HealthMetricContract.MetricDataEntries.COLUMN_NAME_DATEOFENTRY, dataEntry.DateOfEntry);
        values.put(HealthMetricContract.MetricDataEntries.COLUMN_NAME_DATAENTRY, dataEntry.DataEntry);

        return database.update(HealthMetricContract.MetricDataEntries.TABLE_NAME, values, HealthMetricContract.MetricDataEntries._ID + " = " + dataEntry.Id,
                null) > 0;
    }


    /**
     * The updateMetric method updates the metric in the database.
     *
     * @param metric The metric that will be updated.
     * @return An integer value indicating if the update is successful.
     */
    public boolean updateMetric(Metric metric) {
        SQLiteDatabase database = this.getWritableDatabase();

        ContentValues values = new ContentValues();

        values.put(HealthMetricContract.Metrics.COLUMN_NAME_METRICNAME, metric.Name);

        return database.update(HealthMetricContract.Metrics.TABLE_NAME, values, HealthMetricContract.Metrics._ID + " = " + metric.Id,
                null) > 0;
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
    public boolean addMetricToProfile(int unitId, int metricId) {
        Log.d("TEST", String.valueOf(unitId));
        SQLiteDatabase database = getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(HealthMetricContract.Metrics.COLUMN_NAME_UNITID, unitId);
        values.put(HealthMetricContract.Metrics.COLUMN_NAME_ISADDEDTOPROFILE, 1);

        return database.update(HealthMetricContract.Metrics.TABLE_NAME, values, HealthMetricContract.Units._ID + " = " + metricId,
                null) > 0;
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
    public boolean addGalleryToProfile(int galleryId) {

        SQLiteDatabase database = getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(HealthMetricContract.Galleries.COLUMN_NAME_ISADDEDTOPROFILE, 1);

        return database.update(HealthMetricContract.Galleries.TABLE_NAME, values, HealthMetricContract.Galleries._ID + " = " + galleryId,
                null) > 0;
    }

    public boolean updatePrescription(Prescription prescription) {

        SQLiteDatabase database = this.getWritableDatabase();

        ContentValues values = new ContentValues();

        values.put(HealthMetricContract.Prescriptions.COLUMN_NAME_NAME, prescription.Name);
        values.put(HealthMetricContract.Prescriptions.COLUMN_NAME_REASON, prescription.Reason);
        values.put(HealthMetricContract.Prescriptions.COLUMN_NAME_FREQUENCY, prescription.Frequency);
        values.put(HealthMetricContract.Prescriptions.COLUMN_NAME_STRENGTH, prescription.Strength);
        values.put(HealthMetricContract.Prescriptions.COLUMN_NAME_DOSAGEMEASUREMENT, prescription.DosageMeasurementId);
        values.put(HealthMetricContract.Prescriptions.COLUMN_NAME_FORM, prescription.Form);
        values.put(HealthMetricContract.Prescriptions.COLUMN_NAME_AMOUNT, prescription.Amount);

        return database.update(HealthMetricContract.Prescriptions.TABLE_NAME, values, HealthMetricContract.Prescriptions._ID + " = " + prescription.Id,
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

    public ArrayList<String> getAllMetricNames() {

        ArrayList<String> metricNameList = new ArrayList<>();
        SQLiteDatabase readableDatabase = getReadableDatabase();

        String[] projection = {
                HealthMetricContract.Metrics.COLUMN_NAME_METRICNAME
        };


        Cursor cursor = readableDatabase.query(
                HealthMetricContract.Metrics.TABLE_NAME,   // The table to query
                projection,             // The array of columns to return (pass null to get all)
                null,              // The columns for the WHERE clause
                null,          // The values for the WHERE clause
                null,                   // don't group the rows
                null,                   // don't filter by row groups
                null);

        while (cursor.moveToNext()) {
            String metricName = cursor.getString(cursor.getColumnIndex(HealthMetricContract.Metrics.COLUMN_NAME_METRICNAME));
            metricNameList.add(metricName.toLowerCase());
        }
        cursor.close();
        readableDatabase.close();

        return metricNameList;
    }

    public ArrayList<String> getAllGalleryNames() {

        ArrayList<String> galleryNameList = new ArrayList<>();
        SQLiteDatabase readableDatabase = getReadableDatabase();

        String[] projection = {
                HealthMetricContract.Galleries.COLUMN_NAME_GALLERYNAME
        };


        Cursor cursor = readableDatabase.query(
                HealthMetricContract.Galleries.TABLE_NAME,   // The table to query
                projection,             // The array of columns to return (pass null to get all)
                null,              // The columns for the WHERE clause
                null,          // The values for the WHERE clause
                null,                   // don't group the rows
                null,                   // don't filter by row groups
                null);

        while (cursor.moveToNext()) {
            String galleryName = cursor.getString(cursor.getColumnIndex(HealthMetricContract.Galleries.COLUMN_NAME_GALLERYNAME));
            galleryNameList.add(galleryName.toLowerCase());
        }
        cursor.close();
        readableDatabase.close();

        return galleryNameList;
    }
}

