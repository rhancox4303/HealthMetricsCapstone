package ca.mohawk.HealthMetrics;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import java.util.ArrayList;
import java.util.List;

import ca.mohawk.HealthMetrics.DisplayObjects.MetricRecyclerViewObject;
import ca.mohawk.HealthMetrics.Models.Metric;
import ca.mohawk.HealthMetrics.Models.MetricDataEntry;
import ca.mohawk.HealthMetrics.Models.PhotoGallery;
import ca.mohawk.HealthMetrics.Models.Unit;
import ca.mohawk.HealthMetrics.Models.User;
import ca.mohawk.HealthMetrics.DisplayObjects.MetricSpinnerObject;
import ca.mohawk.HealthMetrics.DisplayObjects.UnitSpinnerObject;

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

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(HealthMetricContract.DosageMeasurements.CREATE_TABLE);
        db.execSQL(HealthMetricContract.Prescriptions.CREATE_TABLE);
        db.execSQL(HealthMetricContract.Users.CREATE_TABLE);
        db.execSQL(HealthMetricContract.Notes.CREATE_TABLE);
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

    public void addUser(User user) {
        SQLiteDatabase db = getWritableDatabase();
        db.beginTransaction();
        try {

            ContentValues values = new ContentValues();
            values.put(HealthMetricContract.Users.COLUMN_NAME_FIRSTNAME, user.FirstName);
            values.put(HealthMetricContract.Users.COLUMN_NAME_LASTNAME, user.LastName);
            values.put(HealthMetricContract.Users.COLUMN_NAME_DATEOFBIRTH, user.DateOfBirth);
            values.put(HealthMetricContract.Users.COLUMN_NAME_GENDER, user.Gender);

            // Notice how we haven't specified the primary key. SQLite auto increments the primary key column.
            db.insertOrThrow(HealthMetricContract.Users.TABLE_NAME, null, values);
            db.setTransactionSuccessful();
        } catch (Exception e) {
            Log.d("TAG", "Error while trying to add user to database");
        } finally {
            db.endTransaction();
        }
    }

    public void addMetric(Metric metric) {
        SQLiteDatabase db = getWritableDatabase();
        db.beginTransaction();
        try {
            ContentValues values = new ContentValues();
            values.put(HealthMetricContract.Metrics.COLUMN_NAME_METRICNAME, metric.Name);
            values.put(HealthMetricContract.Metrics.COLUMN_NAME_UNITID, metric.UnitId);
            values.put(HealthMetricContract.Metrics.COLUMN_NAME_UNITCATEGORY, metric.UnitCategory);
            values.put(HealthMetricContract.Metrics.COLUMN_NAME_ISADDEDTOPROFILE, metric.IsAddedToProfile);

            db.insertOrThrow(HealthMetricContract.Metrics.TABLE_NAME, null, values);
            db.setTransactionSuccessful();
        } catch (Exception e) {
            Log.d("TAG", "Error while trying to add metric to database");
        } finally {
            db.endTransaction();
        }
    }

    public void addUnit(Unit unit) {
        SQLiteDatabase db = getWritableDatabase();
        db.beginTransaction();
        try {
            ContentValues values = new ContentValues();
            values.put(HealthMetricContract.Units.COLUMN_NAME_UNITNAME, unit.UnitName);
            values.put(HealthMetricContract.Units.COLUMN_NAME_ABBREVIATION, unit.UnitAbbreviation);
            values.put(HealthMetricContract.Units.COLUMN_NAME_UNITCATEGORY, unit.UnitCategory);
            db.insertOrThrow(HealthMetricContract.Units.TABLE_NAME, null, values);
            db.setTransactionSuccessful();
        } catch (Exception e) {
            Log.d("TAG", "Error while trying to add unit to database");
        } finally {
            db.endTransaction();
        }
    }

    public void seedUnits() {

        ArrayList<Unit> unitArrayList = new ArrayList<Unit>();

        Unit centimeters = new Unit("Centimeters", "cm", "Length");
        Unit meters = new Unit("Meters", "m", "Length");
        Unit inches = new Unit("Inches", "in", "Length");
        Unit feet = new Unit("Feet", "ft", "Length");

        unitArrayList.add(centimeters);
        unitArrayList.add(meters);
        unitArrayList.add(inches);
        unitArrayList.add(feet);

        Unit kilograms = new Unit("Kilograms", "kg", "Weight");
        Unit grams = new Unit("Grams", "g", "Weight");
        Unit milligrams = new Unit("Milligrams", "mg", "Weight");
        Unit pounds = new Unit("Pounds", "lb", "Weight");

        unitArrayList.add(kilograms);
        unitArrayList.add(grams);
        unitArrayList.add(milligrams);
        unitArrayList.add(pounds);

        Unit hours = new Unit("Hours", "hrs", "Time");
        Unit minutes = new Unit("Minutes", "min", "Time");
        Unit seconds = new Unit("Seconds", "s", "Time");

        unitArrayList.add(hours);
        unitArrayList.add(minutes);
        unitArrayList.add(seconds);

        Unit litres = new Unit("Litres", "l", "Volume");
        Unit milliliter = new Unit("Milliliters", "ml", "Volume");
        Unit ounce = new Unit("Ounces", "fl oz", "Volume");
        Unit cupMilliliter = new Unit("Cup(240ml)", "Cups", "Volume");
        Unit cupOunce = new Unit("Cup(8.4 fl oz)", "Cups", "Volume");

        unitArrayList.add(litres);
        unitArrayList.add(milliliter);
        unitArrayList.add(ounce);
        unitArrayList.add(cupMilliliter);
        unitArrayList.add(cupOunce);

        Unit systolicBloodPressure = new Unit("Systolic Blood Pressure", "mmHg", "Blood Pressure");
        Unit diastolicBloodPressure = new Unit("Diastolic Blood Pressure", "mmHg", "Blood Pressure");

        unitArrayList.add(systolicBloodPressure);
        unitArrayList.add(diastolicBloodPressure);

        for (Unit unit : unitArrayList) {
            addUnit(unit);
        }
    }

    public void seedMetrics() {
        ArrayList<Metric> metricArrayList = new ArrayList<Metric>();

        Metric leftBicepSize = new Metric(0, "Left Bicep Size", "Length", 0);
        Metric rightBicepSize = new Metric(0, "Right Bicep Size", "Length", 0);

        metricArrayList.add(leftBicepSize);
        metricArrayList.add(rightBicepSize);

        Metric bloodPressure = new Metric(0, "Blood Pressure", "Blood Pressure", 0);
        metricArrayList.add(bloodPressure);

        Metric bodyHeight = new Metric(0, "Body Height", "Length", 0);
        metricArrayList.add(bodyHeight);

        Metric leftCalfSize = new Metric(0, "Left Calf Size", "Length", 0);
        Metric rightCalfSize = new Metric(0, "Right Calf Size", "Length", 0);

        metricArrayList.add(leftCalfSize);
        metricArrayList.add(rightCalfSize);

        Metric chestSize = new Metric(0, "Chest Size", "Length", 0);
        metricArrayList.add(chestSize);

        Metric sleepDuration = new Metric(0, "Sleep Duration", "Time", 0);
        metricArrayList.add(sleepDuration);

        Metric waistSize = new Metric(0, "Waist Size", "Length", 0);
        metricArrayList.add(waistSize);

        Metric waterIntake = new Metric(0, "Water Intake", "Volume", 0);
        metricArrayList.add(waterIntake);

        for (Metric metric : metricArrayList) {
            addMetric(metric);
        }

    }

    public List<MetricSpinnerObject> getAllMetrics() {
        SQLiteDatabase db = getReadableDatabase();

        // Define a projection that specifies which columns from the database
        // you will actually use after this query.
        String[] projection = {
                HealthMetricContract.Metrics._ID,
                HealthMetricContract.Metrics.COLUMN_NAME_METRICNAME,
                HealthMetricContract.Metrics.COLUMN_NAME_UNITCATEGORY
        };

        // How you want the results sorted in the resulting Cursor
        String sortOrder =
                HealthMetricContract.Metrics.COLUMN_NAME_METRICNAME + " ASC";

        Cursor cursor = db.query(
                HealthMetricContract.Metrics.TABLE_NAME,   // The table to query
                projection,             // The array of columns to return (pass null to get all)
                null,              // The columns for the WHERE clause
                null,          // The values for the WHERE clause
                null,                   // don't group the rows
                null,                   // don't filter by row groups
                sortOrder);              // The sort order

        List metrics = new ArrayList<MetricSpinnerObject>();

        while (cursor.moveToNext()) {
            String metricName = cursor.getString(
                    cursor.getColumnIndexOrThrow(HealthMetricContract.Metrics.COLUMN_NAME_METRICNAME));

            String unitCategory = cursor.getString(
                    cursor.getColumnIndexOrThrow(HealthMetricContract.Metrics.COLUMN_NAME_UNITCATEGORY));
            int id = cursor.getInt(
                    cursor.getColumnIndexOrThrow(HealthMetricContract.Metrics._ID));
            metrics.add(new MetricSpinnerObject(unitCategory, metricName, id));
        }

        cursor.close();
        return metrics;
    }

    public List<UnitSpinnerObject> getAllSpinnerUnits(String unitCategory) {
        SQLiteDatabase db = getReadableDatabase();

        // Define a projection that specifies which columns from the database
        // you will actually use after this query.
        String[] projection = {
                HealthMetricContract.Units._ID,
                HealthMetricContract.Units.COLUMN_NAME_UNITNAME
        };

        // How you want the results sorted in the resulting Cursor
        String sortOrder =
                HealthMetricContract.Units.COLUMN_NAME_UNITNAME + " ASC";

        String selection = HealthMetricContract.Units.COLUMN_NAME_UNITCATEGORY + "=?";

        Cursor cursor = db.query(
                HealthMetricContract.Units.TABLE_NAME,   // The table to query
                projection,             // The array of columns to return (pass null to get all)
                selection,              // The columns for the WHERE clause
                new String[]{unitCategory},          // The values for the WHERE clause
                null,                   // don't group the rows
                null,                   // don't filter by row groups
                sortOrder);              // The sort order

        List unitSpinnerObjects = new ArrayList<UnitSpinnerObject>();

        while (cursor.moveToNext()) {
            String unitName = cursor.getString(
                    cursor.getColumnIndexOrThrow(HealthMetricContract.Units.COLUMN_NAME_UNITNAME));

            int id = cursor.getInt(
                    cursor.getColumnIndexOrThrow(HealthMetricContract.Units._ID));
            unitSpinnerObjects.add(new UnitSpinnerObject(unitName, id));
        }

        cursor.close();
        return unitSpinnerObjects;
    }

    public List<MetricRecyclerViewObject> getAddedMetricsAndGalleries() {

        List<MetricRecyclerViewObject> recyclerViewObjects = new ArrayList<MetricRecyclerViewObject>();
        recyclerViewObjects = getAddedMetrics(recyclerViewObjects);

        return recyclerViewObjects;
    }

    public List<MetricRecyclerViewObject> getAddedMetrics(List<MetricRecyclerViewObject> recyclerViewObjects) {
        SQLiteDatabase db = getReadableDatabase();
        // Define a projection that specifies which columns from the database
        // you will actually use after this query.
        String[] projection = {
                HealthMetricContract.Metrics._ID,
                HealthMetricContract.Metrics.COLUMN_NAME_METRICNAME,
                HealthMetricContract.Metrics.COLUMN_NAME_UNITID
        };

        String selection = HealthMetricContract.Metrics.COLUMN_NAME_ISADDEDTOPROFILE + "=?";

        Cursor cursor = db.query(
                HealthMetricContract.Metrics.TABLE_NAME,   // The table to query
                projection,             // The array of columns to return (pass null to get all)
                selection,              // The columns for the WHERE clause
                new String[]{String.valueOf(1)},          // The values for the WHERE clause
                null,                   // don't group the rows
                null,                   // don't filter by row groups
                null);              // The sort order

        while (cursor.moveToNext()) {
            String metricName = cursor.getString(
                    cursor.getColumnIndexOrThrow(HealthMetricContract.Metrics.COLUMN_NAME_METRICNAME));
            Log.d("TEST", metricName);
            int metricId = cursor.getInt(
                    cursor.getColumnIndexOrThrow(HealthMetricContract.Metrics._ID));

            int unitId = cursor.getInt(
                    cursor.getColumnIndexOrThrow(HealthMetricContract.Metrics.COLUMN_NAME_UNITID));

            String dataEntry = getLatestDataEntryValue(metricId);
            Log.d("TEST", String.valueOf(unitId));
            Unit unit = getUnitById(unitId);
            recyclerViewObjects.add(new MetricRecyclerViewObject(metricName, dataEntry, unit.UnitAbbreviation, "Quantitative"));
        }

        cursor.close();
        return recyclerViewObjects;
    }

    public List<MetricRecyclerViewObject> getAddedPhotoGalleries(List<MetricRecyclerViewObject> recyclerViewObjects) {
        return recyclerViewObjects;
    }

    public String getLatestDataEntryValue(int metricId) {
        SQLiteDatabase db = getReadableDatabase();

        String[] projection = {
                HealthMetricContract.MetricDataEntries.COLUMN_NAME_DATAENTRY
        };

        String selection = HealthMetricContract.MetricDataEntries.COLUMN_NAME_METRICID + "=?";
        String sortOrder =
                HealthMetricContract.MetricDataEntries.COLUMN_NAME_DATEOFENTRY + " ASC";
        Cursor cursor = db.query(
                HealthMetricContract.MetricDataEntries.TABLE_NAME,   // The table to query
                projection,             // The array of columns to return (pass null to get all)
                selection,              // The columns for the WHERE clause
                new String[]{String.valueOf(metricId)},          // The values for the WHERE clause
                null,                   // don't group the rows
                null,                   // don't filter by row groups
                sortOrder);

        if (cursor != null) {
            if (cursor.moveToFirst()) {
                String dataEntry = cursor.getString(cursor.getColumnIndex(HealthMetricContract.MetricDataEntries.COLUMN_NAME_DATAENTRY));
                cursor.close();
                db.close();
                return dataEntry;
            } else {
                cursor.close();
                db.close();
                return "No Data Available";
            }
        } else {
            cursor.close();
            db.close();
            return "No Data Available";
        }
    }

    public Unit getUnitById(int unitId) {
        SQLiteDatabase db = getReadableDatabase();

        String[] projection = {
                HealthMetricContract.Units.COLUMN_NAME_UNITNAME,
                HealthMetricContract.Units.COLUMN_NAME_UNITCATEGORY,
                HealthMetricContract.Units.COLUMN_NAME_ABBREVIATION
        };

        String selection = HealthMetricContract.Units._ID + "=?";
        String unitIdString = String.valueOf(unitId);

        Cursor cursor = db.query(
                HealthMetricContract.Units.TABLE_NAME,   // The table to query
                projection,             // The array of columns to return (pass null to get all)
                selection,              // The columns for the WHERE clause
                new String[]{unitIdString},          // The values for the WHERE clause
                null,                   // don't group the rows
                null,
                null);                      // don't filter by row groups

        if (cursor != null) {
            if (cursor.moveToFirst()) {
                String unitName = cursor.getString(cursor.getColumnIndex(HealthMetricContract.Units.COLUMN_NAME_UNITNAME));
                String unitCategory = cursor.getString(cursor.getColumnIndex(HealthMetricContract.Units.COLUMN_NAME_UNITCATEGORY));
                String unitAbbreviation = cursor.getString(cursor.getColumnIndex(HealthMetricContract.Units.COLUMN_NAME_ABBREVIATION));

                Unit unit = new Unit(unitName, unitAbbreviation, unitCategory);
                cursor.close();
                db.close();
                return unit;
            } else {
                Log.d("ERROR", "No unit found.");
                cursor.close();
                db.close();
                return null;
            }
        } else {
            Log.d("ERROR", "No unit found.");
            cursor.close();
            db.close();
            return null;
        }
    }

    public List<String> getAllUnitCategories() {
        List<String> unitCategories = new ArrayList<String>();

        SQLiteDatabase db = getReadableDatabase();

        String[] projection = {
                HealthMetricContract.Units.COLUMN_NAME_UNITCATEGORY,
        };
        Cursor cursor = db.query(true,
                HealthMetricContract.Units.TABLE_NAME, projection,
                null,
                null,
                HealthMetricContract.Units.COLUMN_NAME_UNITCATEGORY,
                null,
                null,
                null);

        while (cursor.moveToNext()) {
            String unitCategory = cursor.getString(
                    cursor.getColumnIndexOrThrow(HealthMetricContract.Units.COLUMN_NAME_UNITCATEGORY));
            Log.d("TEST",unitCategory);
            unitCategories.add(unitCategory);
        }
        db.close();
        return unitCategories;
    }

    public User getUser() {

        SQLiteDatabase database = getReadableDatabase();
        String selectQuery = "SELECT * FROM " + HealthMetricContract.Users.TABLE_NAME + " WHERE _ID = 1";
        Cursor cursor = database.rawQuery(selectQuery, null);
        cursor.moveToFirst();

        if (cursor != null) {
            String firstName = cursor.getString(cursor.getColumnIndex(HealthMetricContract.Users.COLUMN_NAME_FIRSTNAME));
            String lastName = cursor.getString(cursor.getColumnIndex(HealthMetricContract.Users.COLUMN_NAME_LASTNAME));
            String gender = cursor.getString(cursor.getColumnIndex(HealthMetricContract.Users.COLUMN_NAME_GENDER));
            String dateOfBirth = cursor.getString(cursor.getColumnIndex(HealthMetricContract.Users.COLUMN_NAME_DATEOFBIRTH));

            User user = new User(firstName, lastName, gender, dateOfBirth);
            cursor.close();
            database.close();
            return user;
        } else {
            Log.d("ERROR", "No user found.");
            cursor.close();
            database.close();
            return null;
        }
    }

    public int updateUser(User user) {
        SQLiteDatabase database = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(HealthMetricContract.Users.COLUMN_NAME_FIRSTNAME, user.FirstName);
        values.put(HealthMetricContract.Users.COLUMN_NAME_LASTNAME, user.LastName);
        values.put(HealthMetricContract.Users.COLUMN_NAME_DATEOFBIRTH, user.DateOfBirth);
        values.put(HealthMetricContract.Users.COLUMN_NAME_GENDER, user.Gender);

        return database.update(HealthMetricContract.Users.TABLE_NAME, values, HealthMetricContract.Users._ID + " = 1",
                null);
    }

    public int addMetricToProfile(int unitId, int metricId) {
        Log.d("TEST", String.valueOf(unitId));
        SQLiteDatabase database = getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(HealthMetricContract.Metrics.COLUMN_NAME_UNITID, unitId);
        values.put(HealthMetricContract.Metrics.COLUMN_NAME_ISADDEDTOPROFILE, 1);

        return database.update(HealthMetricContract.Metrics.TABLE_NAME, values, HealthMetricContract.Users._ID + " = " + metricId,
                null);
    }
}

