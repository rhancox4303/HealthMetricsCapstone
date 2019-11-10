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
        static final String COLUMN_NAME_DOSAGE_MEASUREMENT = "dosageMeasurement";
        static final String COLUMN_NAME_UNIT_ABBREVIATION = "unitAbbreviation";

        static final String CREATE_TABLE = "CREATE TABLE "
                + TABLE_NAME +
                "(" + _ID + " INTEGER PRIMARY KEY," +
                COLUMN_NAME_DOSAGE_MEASUREMENT + " REAL," +
                COLUMN_NAME_UNIT_ABBREVIATION + " TEXT );";
    }

    /**
     * The Galleries class implements BaseColumns.
     * It represents the galleries table.
     */
    static abstract class Galleries implements BaseColumns {

        static final String TABLE_NAME = "Galleries";
        static final String COLUMN_NAME_GALLERY_NAME = "GalleryName";
        static final String COLUMN_NAME_IS_ADDED_TO_PROFILE = "isAddedToProfile";

        static final String CREATE_TABLE = "CREATE TABLE "
                + TABLE_NAME +
                "(" + _ID + " INTEGER PRIMARY KEY," +
                COLUMN_NAME_IS_ADDED_TO_PROFILE + " INTEGER," +
                COLUMN_NAME_GALLERY_NAME + " TEXT )";
    }

    /**
     * The MetricDataEntries class implements BaseColumns.
     * It represents the Metric Data Entries table.
     */
    static abstract class MetricDataEntries implements BaseColumns {

        static final String TABLE_NAME = "MetricsDataEntries";
        static final String COLUMN_NAME_METRIC_ID = "MetricId";
        static final String COLUMN_NAME_DATA_ENTRY = "data";
        static final String COLUMN_NAME_DATE_OF_ENTRY = "dateOfEntry";

        static final String CREATE_TABLE = "CREATE TABLE "
                + TABLE_NAME +
                "(" + _ID + " INTEGER PRIMARY KEY," +
                COLUMN_NAME_METRIC_ID + " INTEGER," +
                COLUMN_NAME_DATA_ENTRY + " REAL," +
                COLUMN_NAME_DATE_OF_ENTRY + " TEXT," +
                "FOREIGN KEY (" + COLUMN_NAME_METRIC_ID + ") REFERENCES " + Metrics.TABLE_NAME + "(_ID));";
    }

    /**
     * The Metrics class implements BaseColumns.
     * It represents the metrics table.
     */
    static abstract class Metrics implements BaseColumns {

        static final String TABLE_NAME = "Metrics";
        static final String COLUMN_NAME_UNIT_ID = "UnitID";
        static final String COLUMN_NAME_METRIC_NAME = "MetricName";
        static final String COLUMN_NAME_IS_ADDED_TO_PROFILE = "isAddedToProfile";
        static final String COLUMN_NAME_UNIT_CATEGORY_ID = "unitCategoryId";


        static final String CREATE_TABLE = "CREATE TABLE "
                + TABLE_NAME +
                "(" + _ID + " INTEGER PRIMARY KEY," +
                COLUMN_NAME_UNIT_ID + " INTEGER," +
                COLUMN_NAME_METRIC_NAME + " TEXT," +
                COLUMN_NAME_IS_ADDED_TO_PROFILE + " INTEGER," +
                COLUMN_NAME_UNIT_CATEGORY_ID + " INTEGER," +
                "FOREIGN KEY (" + COLUMN_NAME_UNIT_ID + ") REFERENCES " + Units.TABLE_NAME + "(_ID)" +
                "FOREIGN KEY (" + COLUMN_NAME_UNIT_CATEGORY_ID + ") REFERENCES " + UnitCategories.TABLE_NAME + "(_ID));";
    }

    /**
     * The Notes class implements BaseColumns.
     * It represents the notes table.
     */
    static abstract class Notes implements BaseColumns {

        static final String TABLE_NAME = "Notes";
        static final String COLUMN_NAME_NOTE_CONTENT = "noteContent";
        static final String COLUMN_NAME_DATE_OF_ENTRY = "dateOfEntry";

        static final String CREATE_TABLE = "CREATE TABLE "
                + TABLE_NAME +
                "(" + _ID + " INTEGER PRIMARY KEY," +
                COLUMN_NAME_NOTE_CONTENT + " TEXT," +
                COLUMN_NAME_DATE_OF_ENTRY + " TEXT );";
    }

    /**
     * The Notification class implements BaseColumns.
     * It represents the notifications table.
     */
    static abstract class Notifications implements BaseColumns {

        static final String TABLE_NAME = "Notification";
        static final String COLUMN_NAME_TARGET_ID = "targetId";
        static final String COLUMN_NAME_TYPE = "Type";
        static final String COLUMN_NAME_TARGET_DATE_TIME = "TargetDateAndTime";


        static final String CREATE_TABLE = "CREATE TABLE "
                + TABLE_NAME +
                "(" + _ID + " INTEGER PRIMARY KEY," +
                COLUMN_NAME_TARGET_ID + " INTEGER," +
                COLUMN_NAME_TYPE + " TEXT," +
                COLUMN_NAME_TARGET_DATE_TIME + " TEXT);";
    }

    /**
     * The PhotoEntries class implements BaseColumns.
     * It represents the Photo Entries table.
     */
    static abstract class PhotoEntries implements BaseColumns {
        static final String TABLE_NAME = "PhotoEntries";
        static final String COLUMN_NAME_GALLERY_ID = "GalleryID";
        static final String COLUMN_NAME_PHOTO_ENTRY_PATH = "photoEntryPath";
        static final String COLUMN_NAME_DATE_OF_ENTRY = "DataOfEntry";
        static final String COLUMN_NAME_IS_FROM_GALLERY = "isFromGallery";

        static final String CREATE_TABLE = "CREATE TABLE "
                + TABLE_NAME +
                "(" + _ID + " INTEGER PRIMARY KEY," +
                COLUMN_NAME_GALLERY_ID + " INTEGER," +
                COLUMN_NAME_PHOTO_ENTRY_PATH + " TEXT," +
                COLUMN_NAME_IS_FROM_GALLERY + " INTEGER," +
                COLUMN_NAME_DATE_OF_ENTRY + " TEXT," +
                "FOREIGN KEY (" + COLUMN_NAME_GALLERY_ID + ") REFERENCES " + Galleries.TABLE_NAME + "(_ID));";

    }

    /**
     * The Prescriptions class implements BaseColumns.
     * It represents the prescriptions table.
     */
    static abstract class Prescriptions implements BaseColumns {

        static final String TABLE_NAME = "Prescriptions";
        static final String COLUMN_NAME_DOSAGE_MEASUREMENT = "dosageMeasurement";
        static final String COLUMN_NAME_NAME = "name";
        static final String COLUMN_NAME_FORM = "form";
        static final String COLUMN_NAME_STRENGTH = "strength";
        static final String COLUMN_NAME_DOSAGE_AMOUNT = "dosageAmount";
        static final String COLUMN_NAME_FREQUENCY = "frequency";
        static final String COLUMN_NAME_AMOUNT = "amount";
        static final String COLUMN_NAME_REASON = "reason";

        static final String CREATE_TABLE = "CREATE TABLE "
                + TABLE_NAME +
                "(" + _ID + " INTEGER PRIMARY KEY," +
                COLUMN_NAME_DOSAGE_MEASUREMENT + " INTEGER," +
                COLUMN_NAME_NAME + " TEXT," +
                COLUMN_NAME_FORM + " TEXT," +
                COLUMN_NAME_STRENGTH + " TEXT," +
                COLUMN_NAME_DOSAGE_AMOUNT + " REAL," +
                COLUMN_NAME_FREQUENCY + " TEXT," +
                COLUMN_NAME_AMOUNT + " REAL," +
                COLUMN_NAME_REASON + " TEXT," +
                " FOREIGN KEY (" + COLUMN_NAME_DOSAGE_MEASUREMENT + ") REFERENCES " + DosageMeasurements.TABLE_NAME + "(_ID));";
    }

    /**
     * The Users class implements BaseColumns.
     * It represents the users table.
     */
    static abstract class Users implements BaseColumns {
        static final String TABLE_NAME = "Users";
        static final String COLUMN_NAME_FIRST_NAME = "firstName";
        static final String COLUMN_NAME_LAST_NAME = "lastName";
        static final String COLUMN_NAME_GENDER = "gender";
        static final String COLUMN_NAME_DATE_OF_BIRTH = "dateOfBirth";

        static final String CREATE_TABLE = "CREATE TABLE "
                + TABLE_NAME +
                "(" + _ID + " INTEGER PRIMARY KEY," +
                COLUMN_NAME_FIRST_NAME + " TEXT," +
                COLUMN_NAME_LAST_NAME + " TEXT," +
                COLUMN_NAME_GENDER + " TEXT," +
                COLUMN_NAME_DATE_OF_BIRTH + " TEXT )";
    }


    /**
     * The Units class implements BaseColumns.
     * It represents the units table.
     */
    static abstract class Units implements BaseColumns {

        static final String TABLE_NAME = "Units";
        static final String COLUMN_NAME_UNIT_NAME = "unitName";
        static final String COLUMN_NAME_ABBREVIATION = "unitAbbreviation";
        static final String COLUMN_NAME_UNIT_CATEGORY_ID = "unitCategoryId";

        static final String CREATE_TABLE = "CREATE TABLE "
                + TABLE_NAME +
                "(" + _ID + " INTEGER PRIMARY KEY," +
                COLUMN_NAME_UNIT_NAME + " TEXT," +
                COLUMN_NAME_ABBREVIATION + " TEXT," +
                COLUMN_NAME_UNIT_CATEGORY_ID + " INTEGER," +
                "FOREIGN KEY (" + COLUMN_NAME_UNIT_CATEGORY_ID + ") REFERENCES " + UnitCategories.TABLE_NAME + "(_ID));";
    }

    /**
     * The UnitsCategories class implements BaseColumns.
     * It represents the unit categories table.
     */
    static abstract class UnitCategories implements BaseColumns {

        static final String TABLE_NAME = "UnitsCategories";
        static final String COLUMN_NAME_UNIT_CATEGORY = "unitName";

        static final String CREATE_TABLE = "CREATE TABLE "
                + TABLE_NAME +
                "(" + _ID + " INTEGER PRIMARY KEY," +
                COLUMN_NAME_UNIT_CATEGORY + " TEXT );";
    }
}
