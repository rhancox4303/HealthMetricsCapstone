package ca.mohawk.HealthMetrics;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import java.util.ArrayList;

import ca.mohawk.HealthMetrics.Models.Metric;
import ca.mohawk.HealthMetrics.Models.Unit;
import ca.mohawk.HealthMetrics.Models.User;

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
    public void addMetric(Metric metric){
        SQLiteDatabase db = getWritableDatabase();
        db.beginTransaction();
        try{
            ContentValues values = new ContentValues();
            values.put(HealthMetricContract.Metrics.COLUMN_NAME_METRICNAME, metric.Name);
            values.put(HealthMetricContract.Metrics.COLUMN_NAME_UNITID, metric.UnitId);
            values.put(HealthMetricContract.Metrics.COLUMN_NAME_UNITCATEGORY, metric.UnitCategory);
            values.put(HealthMetricContract.Metrics.COLUMN_NAME_ISADDEDTOPROFILE, metric.IsAddedToProfile);

            db.insertOrThrow(HealthMetricContract.Metrics.TABLE_NAME,null,values);
            db.setTransactionSuccessful();
        }catch (Exception e){
            Log.d("TAG", "Error while trying to add metric to database");
        }finally {
            db.endTransaction();
        }
    }

    public void addUnit(Unit unit){
        SQLiteDatabase db =getWritableDatabase();
        db.beginTransaction();
        try{
            ContentValues values = new ContentValues();
            values.put(HealthMetricContract.Units.COLUMN_NAME_UNITNAME, unit.UnitName);
            values.put(HealthMetricContract.Units.COLUMN_NAME_ABBREVIATION, unit.UnitAbbreviation);
            values.put(HealthMetricContract.Units.COLUMN_NAME_UNITCATEGORY,unit.UnitCategory);
            db.insertOrThrow(HealthMetricContract.Units.TABLE_NAME,null,values);
            db.setTransactionSuccessful();
        }catch (Exception e){
            Log.d("TAG", "Error while trying to add unit to database");
        }finally {
            db.endTransaction();
        }
    }
    public void seedUnits(){

        ArrayList<Unit> unitArrayList =  new ArrayList<Unit>();

        Unit centimeters = new Unit("Centimeters","cm","Length");
        Unit meters = new Unit("Meters","m","Length");
        Unit inches = new Unit("Inches","in","Length");
        Unit feet = new Unit("Feet","ft","Length");

        unitArrayList.add(centimeters);
        unitArrayList.add(meters);
        unitArrayList.add(inches);
        unitArrayList.add(feet);

        Unit kilograms = new Unit("Kilograms","kg","Weight");
        Unit grams = new Unit("Grams","g","Weight");
        Unit milligrams = new Unit("Milligrams","mg","Weight");
        Unit pounds = new Unit("Pounds","lb","Weight");

        unitArrayList.add(kilograms);
        unitArrayList.add(grams);
        unitArrayList.add(milligrams);
        unitArrayList.add(pounds);

        Unit hours = new Unit("Hours","hrs","Time");
        Unit minutes = new Unit("Minutes","min","Time");
        Unit seconds = new Unit("Seconds","s","Time");

        unitArrayList.add(hours);
        unitArrayList.add(minutes);
        unitArrayList.add(seconds);

        Unit litres = new Unit("Litres","l","Volume");
        Unit milliliter = new Unit("Milliliters","ml","Volume");
        Unit ounce = new Unit("Ounces","fl oz","Volume");
        Unit cupMilliliter = new Unit("Cup(240ml)","Cups","Volume");
        Unit cupOunce = new Unit("Cup(8.4 fl oz)","Cups","Volume");

        unitArrayList.add(litres);
        unitArrayList.add(milliliter);
        unitArrayList.add(ounce);
        unitArrayList.add(cupMilliliter);
        unitArrayList.add(cupOunce);

        Unit systolicBloodPressure = new Unit("Systolic Blood Pressure","mmHg","Blood Pressure");
        Unit diastolicBloodPressure = new Unit("Diastolic Blood Pressure","mmHg","Blood Pressure");

        unitArrayList.add(systolicBloodPressure);
        unitArrayList.add(diastolicBloodPressure);

        for(Unit unit : unitArrayList){
            addUnit(unit);
        }
    }

    public void seedMetrics(){

    }
    public User getUser(){

        SQLiteDatabase database = getReadableDatabase();
        String  selectQuery = "SELECT * FROM " + HealthMetricContract.Users.TABLE_NAME + " WHERE _ID = 1";
        Cursor cursor = database.rawQuery(selectQuery, null);
        cursor.moveToFirst();

        if(cursor != null){
            String firstName = cursor.getString(cursor.getColumnIndex(HealthMetricContract.Users.COLUMN_NAME_FIRSTNAME));
            String lastName = cursor.getString(cursor.getColumnIndex(HealthMetricContract.Users.COLUMN_NAME_LASTNAME));
            String gender = cursor.getString(cursor.getColumnIndex(HealthMetricContract.Users.COLUMN_NAME_GENDER));
            String dateOfBirth = cursor.getString(cursor.getColumnIndex(HealthMetricContract.Users.COLUMN_NAME_DATEOFBIRTH));

            User user = new User(firstName,lastName,gender,dateOfBirth);
            cursor.close();
            database.close();
            return user;
        }else{
            Log.d("ERROR","No user found.");
            cursor.close();
            database.close();
            return null;
        }
    }

    public int updateUser(User user){
        SQLiteDatabase database = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(HealthMetricContract.Users.COLUMN_NAME_FIRSTNAME, user.FirstName);
        values.put(HealthMetricContract.Users.COLUMN_NAME_LASTNAME, user.LastName);
        values.put(HealthMetricContract.Users.COLUMN_NAME_DATEOFBIRTH, user.DateOfBirth);
        values.put(HealthMetricContract.Users.COLUMN_NAME_GENDER, user.Gender);

        return database.update(HealthMetricContract.Users.TABLE_NAME, values,HealthMetricContract.Users._ID + " = 1",
                null);
    }

}

