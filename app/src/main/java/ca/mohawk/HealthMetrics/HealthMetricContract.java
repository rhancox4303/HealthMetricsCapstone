package ca.mohawk.HealthMetrics;

import android.provider.BaseColumns;

/**
 * The HealthMetricContract class is used to represent the database.
 */
public final class HealthMetricContract {

    private HealthMetricContract() {

    }

    /**
     * The DosageMeasurements class implements BaseColumns.
     * It represents the dosage measurement table.
     */
    static abstract class DosageMeasurements implements BaseColumns {

        static final String TABLE_NAME = "DosageMeasurements";
        static final String COLUMN_NAME_DOSAGEMEASUREMENT = "dosageMeasurement";
        static final String COLUMN_NAME_UNITABBREVIATION = "unitAbbreviation";

        static final String CREATE_TABLE = "CREATE TABLE "
                + TABLE_NAME +
                "(" + _ID + " INTEGER PRIMARY KEY," +
                COLUMN_NAME_DOSAGEMEASUREMENT + " REAL," +
                COLUMN_NAME_UNITABBREVIATION + " TEXT );";
    }

    /**
     * The Galleries class implements BaseColumns.
     * It represents the galleries table.
     */
    static abstract class Galleries implements BaseColumns {

        static final String TABLE_NAME = "Galleries";
        static final String COLUMN_NAME_GALLERYNAME = "GalleryName";
        static final String COLUMN_NAME_ISADDEDTOPROFILE = "isAddedToProfile";

        static final String CREATE_TABLE = "CREATE TABLE "
                + TABLE_NAME +
                "(" + _ID + " INTEGER PRIMARY KEY," +
                COLUMN_NAME_ISADDEDTOPROFILE + " INTEGER," +
                COLUMN_NAME_GALLERYNAME + " TEXT )";
    }

    /**
     * The MetricDataEntries class implements BaseColumns.
     * It represents the Metric Data Entries table.
     */
    static abstract class MetricDataEntries implements BaseColumns {

        static final String TABLE_NAME = "MetricsDataEntries";
        static final String COLUMN_NAME_METRICID = "MetricID";
        static final String COLUMN_NAME_DATAENTRY = "data";
        static final String COLUMN_NAME_DATEOFENTRY = "dateOfEntry";

        static final String CREATE_TABLE = "CREATE TABLE "
                + TABLE_NAME +
                "(" + _ID + " INTEGER PRIMARY KEY," +
                COLUMN_NAME_METRICID + " INTEGER," +
                COLUMN_NAME_DATAENTRY + " REAL," +
                COLUMN_NAME_DATEOFENTRY + " TEXT," +
                "FOREIGN KEY (" + COLUMN_NAME_METRICID + ") REFERENCES " + Metrics.TABLE_NAME + "(_ID));";
    }

    /**
     * The Metrics class implements BaseColumns.
     * It represents the metrics table.
     */
    static abstract class Metrics implements BaseColumns {

        static final String TABLE_NAME = "Metrics";
        static final String COLUMN_NAME_UNITID = "UnitID";
        static final String COLUMN_NAME_METRICNAME = "MetricName";
        static final String COLUMN_NAME_ISADDEDTOPROFILE = "isAddedToProfile";
        static final String COLUMN_NAME_UNITCATEGORYID = "unitCategoryId";


        static final String CREATE_TABLE = "CREATE TABLE "
                + TABLE_NAME +
                "(" + _ID + " INTEGER PRIMARY KEY," +
                COLUMN_NAME_UNITID + " INTEGER," +
                COLUMN_NAME_METRICNAME + " TEXT," +
                COLUMN_NAME_ISADDEDTOPROFILE + " INTEGER," +
                COLUMN_NAME_UNITCATEGORYID + " INTEGER," +
                "FOREIGN KEY (" + COLUMN_NAME_UNITID + ") REFERENCES " + Units.TABLE_NAME + "(_ID)" +
                "FOREIGN KEY (" + COLUMN_NAME_UNITCATEGORYID + ") REFERENCES " + UnitCategories.TABLE_NAME + "(_ID));";
    }

    /**
     * The Notes class implements BaseColumns.
     * It represents the notes table.
     */
    static abstract class Notes implements BaseColumns {

        static final String TABLE_NAME = "Notes";
        static final String COLUMN_NAME_NOTECONTENT = "noteContent";
        static final String COLUMN_NAME_DATEOFENTRY = "dateOfEntry";

        static final String CREATE_TABLE = "CREATE TABLE "
                + TABLE_NAME +
                "(" + _ID + " INTEGER PRIMARY KEY," +
                COLUMN_NAME_NOTECONTENT + " TEXT," +
                COLUMN_NAME_DATEOFENTRY + " TEXT );";
    }

    /**
     * The Notification class implements BaseColumns.
     * It represents the notifications table.
     */
    static abstract class Notifications implements BaseColumns {

        static final String TABLE_NAME = "Notification";
        static final String COLUMN_NAME_TARGETID = "targetId";
        static final String COLUMN_NAME_TYPE = "Type";
        static final String COLUMN_NAME_TARGETDATETIME = "TargetDateAndTime";


        static final String CREATE_TABLE = "CREATE TABLE "
                + TABLE_NAME +
                "(" + _ID + " INTEGER PRIMARY KEY," +
                COLUMN_NAME_TARGETID + " INTEGER," +
                COLUMN_NAME_TYPE + " TEXT," +
                COLUMN_NAME_TARGETDATETIME + " TEXT);";
    }

    /**
     * The PhotoEntries class implements BaseColumns.
     * It represents the Photo Entries table.
     */
    static abstract class PhotoEntries implements BaseColumns {
        static final String TABLE_NAME = "PhotoEntries";
        static final String COLUMN_NAME_GALLERYID = "GalleryID";
        static final String COLUMN_NAME_PHOTOENTRYPATH = "photoEntryPath";
        static final String COLUMN_NAME_DATEOFENTRY = "DataOfEntry";
        static final String COLUMN_NAME_ISFROMGALLERY = "isFromGallery";

        static final String CREATE_TABLE = "CREATE TABLE "
                + TABLE_NAME +
                "(" + _ID + " INTEGER PRIMARY KEY," +
                COLUMN_NAME_GALLERYID + " INTEGER," +
                COLUMN_NAME_PHOTOENTRYPATH + " TEXT," +
                COLUMN_NAME_ISFROMGALLERY + " INTEGER," +
                COLUMN_NAME_DATEOFENTRY + " TEXT," +
                "FOREIGN KEY (" + COLUMN_NAME_GALLERYID + ") REFERENCES " + Galleries.TABLE_NAME + "(_ID));";

    }

    /**
     * The Prescriptions class implements BaseColumns.
     * It represents the prescriptions table.
     */
    static abstract class Prescriptions implements BaseColumns {

        static final String TABLE_NAME = "Prescriptions";
        static final String COLUMN_NAME_DOSAGEMEASUREMENT = "dosageMeasurement";
        static final String COLUMN_NAME_NAME = "name";
        static final String COLUMN_NAME_FORM = "form";
        static final String COLUMN_NAME_STRENGTH = "strength";
        static final String COLUMN_NAME_DOSAGEAMOUNT = "dosageAmount";
        static final String COLUMN_NAME_FREQUENCY = "frequency";
        static final String COLUMN_NAME_AMOUNT = "amount";
        static final String COLUMN_NAME_REASON = "reason";

        static final String CREATE_TABLE = "CREATE TABLE "
                + TABLE_NAME +
                "(" + _ID + " INTEGER PRIMARY KEY," +
                COLUMN_NAME_DOSAGEMEASUREMENT + " INTEGER," +
                COLUMN_NAME_NAME + " TEXT," +
                COLUMN_NAME_FORM + " TEXT," +
                COLUMN_NAME_STRENGTH + " TEXT," +
                COLUMN_NAME_DOSAGEAMOUNT + " REAL," +
                COLUMN_NAME_FREQUENCY + " TEXT," +
                COLUMN_NAME_AMOUNT + " REAL," +
                COLUMN_NAME_REASON + " TEXT," +
                " FOREIGN KEY (" + COLUMN_NAME_DOSAGEMEASUREMENT + ") REFERENCES " + DosageMeasurements.TABLE_NAME + "(_ID));";
    }

    /**
     * The Users class implements BaseColumns.
     * It represents the users table.
     */
    static abstract class Users implements BaseColumns {
        static final String TABLE_NAME = "Users";
        static final String COLUMN_NAME_FIRSTNAME = "firstName";
        static final String COLUMN_NAME_LASTNAME = "lastName";
        static final String COLUMN_NAME_GENDER = "gender";
        static final String COLUMN_NAME_DATEOFBIRTH = "dateOfBirth";

        static final String CREATE_TABLE = "CREATE TABLE "
                + TABLE_NAME +
                "(" + _ID + " INTEGER PRIMARY KEY," +
                COLUMN_NAME_FIRSTNAME + " TEXT," +
                COLUMN_NAME_LASTNAME + " TEXT," +
                COLUMN_NAME_GENDER + " TEXT," +
                COLUMN_NAME_DATEOFBIRTH + " TEXT )";
    }


    /**
     * The Units class implements BaseColumns.
     * It represents the units table.
     */
    static abstract class Units implements BaseColumns {

        static final String TABLE_NAME = "Units";
        static final String COLUMN_NAME_UNITNAME = "unitName";
        static final String COLUMN_NAME_ABBREVIATION = "unitAbbreviation";
        static final String COLUMN_NAME_UNITCATEGORYID = "unitCategoryId";

        static final String CREATE_TABLE = "CREATE TABLE "
                + TABLE_NAME +
                "(" + _ID + " INTEGER PRIMARY KEY," +
                COLUMN_NAME_UNITNAME + " TEXT," +
                COLUMN_NAME_ABBREVIATION + " TEXT," +
                COLUMN_NAME_UNITCATEGORYID + " INTEGER," +
                "FOREIGN KEY (" + COLUMN_NAME_UNITCATEGORYID + ") REFERENCES " + UnitCategories.TABLE_NAME + "(_ID));";
    }

    /**
     * The UnitsCategories class implements BaseColumns.
     * It represents the unit categories table.
     */
    static abstract class UnitCategories implements BaseColumns {

        static final String TABLE_NAME = "UnitsCategories";
        static final String COLUMN_NAME_UNITCATEGORY = "unitName";

        static final String CREATE_TABLE = "CREATE TABLE "
                + TABLE_NAME +
                "(" + _ID + " INTEGER PRIMARY KEY," +
                COLUMN_NAME_UNITCATEGORY + " TEXT );";
    }
}
