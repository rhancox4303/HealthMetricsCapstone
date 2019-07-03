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
import ca.mohawk.HealthMetrics.Models.UnitCategory;
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
            values.put(HealthMetricContract.Metrics.COLUMN_NAME_UNITCATEGORYID, metric.UnitCategoryId);
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
            values.put(HealthMetricContract.Units.COLUMN_NAME_UNITCATEGORYID, unit.UnitCategoryId);
            db.insertOrThrow(HealthMetricContract.Units.TABLE_NAME, null, values);
            db.setTransactionSuccessful();
        } catch (Exception e) {
            Log.d("TAG", "Error while trying to add unit to database");
        } finally {
            db.endTransaction();
        }
    }

    public void addUnitCategory(UnitCategory unitCategory) {

        SQLiteDatabase db = getWritableDatabase();
        db.beginTransaction();

        try {
            ContentValues values = new ContentValues();
            if(unitCategory.Id != 0) {
                values.put(HealthMetricContract.UnitCategories._ID,unitCategory.Id);
            }
            values.put(HealthMetricContract.UnitCategories.COLUMN_NAME_UNITCATEGORY, unitCategory.UnitCategory);

            db.insertOrThrow(HealthMetricContract.UnitCategories.TABLE_NAME, null, values);
            db.setTransactionSuccessful();

        } catch (Exception e) {
            Log.d("TAG", "Error while trying to add unit category to database");
        } finally {
            db.endTransaction();
        }
    }

    public void seedUnitCategories(){
        List<UnitCategory> unitCategories = new ArrayList<>();

        UnitCategory length = new UnitCategory(1,"Length");
        unitCategories.add(length);

        UnitCategory weight = new UnitCategory(2,"Weight");
        unitCategories.add(weight);

        UnitCategory time = new UnitCategory(3,"Time");
        unitCategories.add(time);

        UnitCategory volume = new UnitCategory(4,"Volume");
        unitCategories.add(volume);

        UnitCategory bloodPressure = new UnitCategory(5,"Blood Pressure");
        unitCategories.add(bloodPressure);

        for (UnitCategory unitCategory : unitCategories) {
            addUnitCategory(unitCategory);
        }


    }
    public void seedUnits() {

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

    public void seedMetrics() {
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

    public List<MetricSpinnerObject> getAllMetrics() {
        SQLiteDatabase db = getReadableDatabase();

        // Define a projection that specifies which columns from the database
        // you will actually use after this query.
        String[] projection = {
                HealthMetricContract.Metrics._ID,
                HealthMetricContract.Metrics.COLUMN_NAME_METRICNAME,
                HealthMetricContract.Metrics.COLUMN_NAME_UNITCATEGORYID
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

            int unitCategoryId = cursor.getInt(
                    cursor.getColumnIndexOrThrow(HealthMetricContract.Metrics.COLUMN_NAME_UNITCATEGORYID));

            int id = cursor.getInt(
                    cursor.getColumnIndexOrThrow(HealthMetricContract.Metrics._ID));
            metrics.add(new MetricSpinnerObject(unitCategoryId, metricName, id));
        }

        cursor.close();
        return metrics;
    }

    public List<UnitSpinnerObject> getAllSpinnerUnits(int unitCategoryId) {
        SQLiteDatabase db = getReadableDatabase();

        // Define a projection that specifies which columns from the database
        // you will actually use after this query.
        String[] projection = {
                HealthMetricContract.Units._ID,
                HealthMetricContract.Units.COLUMN_NAME_UNITNAME
        };
        String unitCategoryIdString = String.valueOf(unitCategoryId);
        // How you want the results sorted in the resulting Cursor
        String sortOrder =
                HealthMetricContract.Units.COLUMN_NAME_UNITNAME + " ASC";

        String selection = HealthMetricContract.Units.COLUMN_NAME_UNITCATEGORYID + "=?";

        Cursor cursor = db.query(
                HealthMetricContract.Units.TABLE_NAME,   // The table to query
                projection,             // The array of columns to return (pass null to get all)
                selection,              // The columns for the WHERE clause
                new String[]{unitCategoryIdString},          // The values for the WHERE clause
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
                HealthMetricContract.Units.COLUMN_NAME_UNITCATEGORYID,
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
                int unitCategoryId = cursor.getInt(cursor.getColumnIndex(HealthMetricContract.Units.COLUMN_NAME_UNITCATEGORYID));
                String unitAbbreviation = cursor.getString(cursor.getColumnIndex(HealthMetricContract.Units.COLUMN_NAME_ABBREVIATION));

                Unit unit = new Unit(unitName, unitAbbreviation, unitCategoryId);
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

    public UnitCategory getUnitCategoryById(int unitCategoryId) {
        SQLiteDatabase db = getReadableDatabase();

        String[] projection = {
                HealthMetricContract.UnitCategories._ID,
                HealthMetricContract.UnitCategories.COLUMN_NAME_UNITCATEGORY
        };

        String selection = HealthMetricContract.UnitCategories._ID + "=?";
        String unitCategoryIdString = String.valueOf(unitCategoryId);

        Cursor cursor = db.query(
                HealthMetricContract.UnitCategories.TABLE_NAME,   // The table to query
                projection,             // The array of columns to return (pass null to get all)
                selection,              // The columns for the WHERE clause
                new String[]{unitCategoryIdString},          // The values for the WHERE clause
                null,                   // don't group the rows
                null,
                null);                      // don't filter by row groups

        if (cursor != null) {
            if (cursor.moveToFirst()) {
                String unitCategoryString = cursor.getString(cursor.getColumnIndex(HealthMetricContract.UnitCategories.COLUMN_NAME_UNITCATEGORY));
                int id = cursor.getInt(cursor.getColumnIndexOrThrow(HealthMetricContract.UnitCategories._ID));

                UnitCategory unitCategory = new UnitCategory(id,unitCategoryIdString);
                cursor.close();
                db.close();
                return unitCategory;
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
            int unitCategoryId =cursor.getInt(
                    cursor.getColumnIndexOrThrow(HealthMetricContract.UnitCategories._ID));
            String unitCategoryString = cursor.getString(
                    cursor.getColumnIndexOrThrow(HealthMetricContract.UnitCategories.COLUMN_NAME_UNITCATEGORY));

            UnitCategory unitCategory = new UnitCategory(unitCategoryId,unitCategoryString);
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

