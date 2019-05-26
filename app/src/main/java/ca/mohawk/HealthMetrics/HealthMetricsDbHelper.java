package ca.mohawk.HealthMetrics;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.provider.BaseColumns;

public class HealthMetricsDbHelper extends SQLiteOpenHelper {

    public static final int DATABASE_VERSION = 1;
    public static final String DATABASE_NAME = "HealthMetrics.db";

    public HealthMetricsDbHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(HealthMetricContract.DosageMeasurements.CREATE_TABLE);
        db.execSQL(HealthMetricContract.Prescriptions.CREATE_TABLE);
        db.execSQL(HealthMetricContract.User.CREATE_TABLE);
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
}
