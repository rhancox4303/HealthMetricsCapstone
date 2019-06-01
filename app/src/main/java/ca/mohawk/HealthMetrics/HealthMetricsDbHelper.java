package ca.mohawk.HealthMetrics;

import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.provider.BaseColumns;
import android.util.Log;

import ca.mohawk.HealthMetrics.Models.User;

import static android.support.constraint.Constraints.TAG;

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
        db.execSQL(HealthMetricContract.QuantitativeMetrics.CREATE_TABLE);
        db.execSQL(HealthMetricContract.QuantitativeMetricsEntries.CREATE_TABLE);
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
            values.put(HealthMetricContract.Users.COLUMN_NAME_DATEOFBIRTH, user.getStringDateOfBirth());
            values.put(HealthMetricContract.Users.COLUMN_NAME_GENDER, user.Gender);

            // Notice how we haven't specified the primary key. SQLite auto increments the primary key column.
            db.insertOrThrow(HealthMetricContract.Users.TABLE_NAME, null, values);
            db.setTransactionSuccessful();
        } catch (Exception e) {
            Log.d(TAG, "Error while trying to add user to database");
        } finally {
            db.endTransaction();
        }
    }
}
