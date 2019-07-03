package ca.mohawk.HealthMetrics;

import android.provider.BaseColumns;

public final class HealthMetricContract {

    private HealthMetricContract() {

    }

    //DosageMeasurements Table
    public static abstract class DosageMeasurements implements BaseColumns {
        public static final String TABLE_NAME = "DosageMeasurements";
        public static final String COLUMN_NAME_DOSAGEMEASUREMENT = "DosageMeasurement";
        public static final String COLUMN_NAME_UNITABBREVIATION = "UnitAbbreviation";

        public static final String CREATE_TABLE = "CREATE TABLE "
                + TABLE_NAME +
                "(" + _ID + " INTEGER PRIMARY KEY," +
                COLUMN_NAME_DOSAGEMEASUREMENT + " REAL," +
                COLUMN_NAME_UNITABBREVIATION + " TEXT );";

        public static final String DELETE_TABLE = "DROP TABLE IF EXISTS " + TABLE_NAME;
    }

    //Prescriptions Table
    public static abstract class Prescriptions implements BaseColumns {
        public static final String TABLE_NAME = "Prescriptions";
        public static final String COLUMN_NAME_DOSAGEMEASUREMENT = "DosageMeasurement";
        public static final String COLUMN_NAME_NAME = "Name";
        public static final String COLUMN_NAME_FORM = "Form";
        public static final String COLUMN_NAME_STRENGTH = "Strength";
        public static final String COLUMN_NAME_DOSAGEAMOUNT = "DosageAmount";
        public static final String COLUMN_NAME_FREQUENCY = "Frequency";
        public static final String COLUMN_NAME_AMOUNT = "Amount";
        public static final String COLUMN_NAME_REASON = "Reason";

        public static final String CREATE_TABLE = "CREATE TABLE "
                + TABLE_NAME +
                "(" + _ID + " INTEGER PRIMARY KEY," +
                COLUMN_NAME_DOSAGEMEASUREMENT + " INTEGER," +
                COLUMN_NAME_NAME + " TEXT," +
                COLUMN_NAME_FORM + " TEXT," +
                COLUMN_NAME_STRENGTH + " TEXT," +
                COLUMN_NAME_DOSAGEAMOUNT + " TEXT," +
                COLUMN_NAME_FREQUENCY + "T EXT," +
                COLUMN_NAME_AMOUNT + " REAL," +
                COLUMN_NAME_REASON + " TEXT," +
                " FOREIGN KEY (" + COLUMN_NAME_DOSAGEMEASUREMENT + ") REFERENCES " + DosageMeasurements.TABLE_NAME + "(_ID));";

        public static final String DELETE_TABLE = "DROP TABLE IF EXISTS " + TABLE_NAME;
    }

    //Notification Table
    public static abstract class Notifications implements BaseColumns {
        public static final String TABLE_NAME = "Notifications";
        public static final String COLUMN_NAME_PRESCRIPTIONID = "PrescriptionID";
        public static final String COLUMN_NAME_GALLERYID = "GalleryID";
        public static final String COLUMN_NAME_METRICID = "MetricID";
        public static final String COLUMN_NAME_TYPE = "Type";
        public static final String COLUMN_NAME_TARGETDATETIME = "TargetDateAndTime";


        public static final String CREATE_TABLE = "CREATE TABLE "
                + TABLE_NAME +
                "(" + _ID + " INTEGER PRIMARY KEY," +
                COLUMN_NAME_PRESCRIPTIONID + " INTEGER," +
                COLUMN_NAME_GALLERYID + " INTEGER," +
                COLUMN_NAME_METRICID + " INTEGER," +
                COLUMN_NAME_TYPE + " TEXT," +
                COLUMN_NAME_TARGETDATETIME + " TEXT," +
                "FOREIGN KEY (" + COLUMN_NAME_PRESCRIPTIONID + ") REFERENCES " + Prescriptions.TABLE_NAME + "(_ID)" +
                "FOREIGN KEY (" +  COLUMN_NAME_GALLERYID + ") REFERENCES " + Galleries.TABLE_NAME + "(_ID)" +
                "FOREIGN KEY (" + COLUMN_NAME_METRICID + ") REFERENCES " + Metrics.TABLE_NAME + "(_ID));";

        public static final String DELETE_TABLE = "DROP TABLE IF EXISTS " + TABLE_NAME;
    }
    //User Table

    public static abstract class Users implements BaseColumns {
        public static final String TABLE_NAME = "Users";
        public static final String COLUMN_NAME_FIRSTNAME = "FirstName";
        public static final String COLUMN_NAME_LASTNAME = "LastName";
        public static final String COLUMN_NAME_GENDER = "Gender";
        public static final String COLUMN_NAME_DATEOFBIRTH = "DateOfBirth";

        public static final String CREATE_TABLE = "CREATE TABLE "
                + TABLE_NAME +
                "(" + _ID + " INTEGER PRIMARY KEY," +
                COLUMN_NAME_FIRSTNAME + " TEXT," +
                COLUMN_NAME_LASTNAME + " TEXT," +
                COLUMN_NAME_GENDER + " TEXT," +
                COLUMN_NAME_DATEOFBIRTH + " TEXT )";

        public static final String DELETE_TABLE = "DROP TABLE IF EXISTS " + TABLE_NAME;
    }

    //Notes Table
    public static abstract class Notes implements BaseColumns {
        public static final String TABLE_NAME = "Notes";
        public static final String COLUMN_NAME_NOTECONTENT = "NoteContent";
        public static final String COLUMN_NAME_DATEOFENTRY = "DateOfEntry";

        public static final String CREATE_TABLE = "CREATE TABLE "
                + TABLE_NAME +
                "(" + _ID + " INTEGER PRIMARY KEY," +
                COLUMN_NAME_NOTECONTENT + " TEXT," +
                COLUMN_NAME_DATEOFENTRY + " TEXT );";

        public static final String DELETE_TABLE = "DROP TABLE IF EXISTS " + TABLE_NAME;
    }

    //Units Table
    public static abstract class Units implements BaseColumns {
        public static final String TABLE_NAME = "Units";
        public static final String COLUMN_NAME_UNITNAME = "UnitName";
        public static final String COLUMN_NAME_ABBREVIATION = "UnitAbbreviation";
        public static final String COLUMN_NAME_UNITCATEGORYID = "UnitCategoryId";

        public static final String CREATE_TABLE = "CREATE TABLE "
                + TABLE_NAME +
                "(" + _ID + " INTEGER PRIMARY KEY," +
                COLUMN_NAME_UNITNAME + " TEXT," +
                COLUMN_NAME_ABBREVIATION + " TEXT," +
                COLUMN_NAME_UNITCATEGORYID + " INTEGER," +
                "FOREIGN KEY (" + COLUMN_NAME_UNITCATEGORYID + ") REFERENCES " + UnitCategories.TABLE_NAME + "(_ID));";

        public static final String DELETE_TABLE = "DROP TABLE IF EXISTS " + TABLE_NAME;
    }

    //Unit Category Table
    public static abstract class UnitCategories implements BaseColumns {
        public static final String TABLE_NAME = "UnitsCategories";
        public static final String COLUMN_NAME_UNITCATEGORY = "UnitName";


        public static final String CREATE_TABLE = "CREATE TABLE "
                + TABLE_NAME +
                "(" + _ID + " INTEGER PRIMARY KEY," +
                COLUMN_NAME_UNITCATEGORY + " TEXT );";

        public static final String DELETE_TABLE = "DROP TABLE IF EXISTS " + TABLE_NAME;
    }

    //Metric Table
    public static abstract class Metrics implements BaseColumns {
        public static final String TABLE_NAME = "Metrics";
        public static final String COLUMN_NAME_UNITID = "UnitID";
        public static final String COLUMN_NAME_METRICNAME = "MetricName";
        public static final String COLUMN_NAME_ISADDEDTOPROFILE = "IsAddedToProfile";
        public static final String COLUMN_NAME_UNITCATEGORYID = "UnitCategoryId";


        public static final String CREATE_TABLE = "CREATE TABLE "
                + TABLE_NAME +
                "(" + _ID + " INTEGER PRIMARY KEY," +
                COLUMN_NAME_UNITID + " INTEGER," +
                COLUMN_NAME_METRICNAME + " TEXT," +
                COLUMN_NAME_ISADDEDTOPROFILE + " INTEGER," +
                COLUMN_NAME_UNITCATEGORYID + " INTEGER," +
                "FOREIGN KEY (" + COLUMN_NAME_UNITID + ") REFERENCES " + Units.TABLE_NAME + "(_ID)" +
                "FOREIGN KEY (" + COLUMN_NAME_UNITCATEGORYID + ") REFERENCES " + UnitCategories.TABLE_NAME + "(_ID));";

        public static final String DELETE_TABLE = "DROP TABLE IF EXISTS " + TABLE_NAME;
    }

    //Galleries Table
    public static abstract class Galleries implements BaseColumns {
        public static final String TABLE_NAME = "Galleries";
        public static final String COLUMN_NAME_GALLERYNAME = "GalleryName";
        public static final String COLUMN_NAME_ISADDEDTOPROFILE = "IsAddedToProfile";

        public static final String CREATE_TABLE = "CREATE TABLE "
                + TABLE_NAME +
                "(" + _ID + " INTEGER PRIMARY KEY," +
                COLUMN_NAME_ISADDEDTOPROFILE + " INTEGER," +
                COLUMN_NAME_GALLERYNAME + " TEXT )";

        public static final String DELETE_TABLE = "DROP TABLE IF EXISTS " + TABLE_NAME;
    }

    //QuantitativeMetricsEntries Table
    public static abstract class MetricDataEntries implements BaseColumns {
        public static final String TABLE_NAME = "MetricsDataEntries";
        public static final String COLUMN_NAME_METRICID = "MetricID";
        public static final String COLUMN_NAME_DATAENTRY = "DataEntry";
        public static final String COLUMN_NAME_DATEOFENTRY = "DateOfEntry";

        public static final String CREATE_TABLE = "CREATE TABLE "
                + TABLE_NAME +
                "(" + _ID + " INTEGER PRIMARY KEY," +
                COLUMN_NAME_METRICID + " INTEGER," +
                COLUMN_NAME_DATAENTRY + " REAL," +
                COLUMN_NAME_DATEOFENTRY + " TEXT," +
                "FOREIGN KEY (" + COLUMN_NAME_METRICID + ") REFERENCES " + Metrics.TABLE_NAME + "(_ID));";
        public static final String DELETE_TABLE = "DROP TABLE IF EXISTS " + TABLE_NAME;
    }

    //PhotoEntries Table
    public static abstract class PhotoEntries implements BaseColumns {
        public static final String TABLE_NAME = "PhotoEntries";
        public static final String COLUMN_NAME_GALLERYID = "GalleryID";
        public static final String COLUMN_NAME_PHOTOENTRY = "PhotoEntry";
        public static final String COLUMN_NAME_DATEOFENTRY = "DataOfEntry";

        public static final String CREATE_TABLE = "CREATE TABLE "
                + TABLE_NAME +
                "(" + _ID + " INTEGER PRIMARY KEY," +
                COLUMN_NAME_GALLERYID + " INTEGER," +
                COLUMN_NAME_PHOTOENTRY + " BLOB," +
                COLUMN_NAME_DATEOFENTRY + " TEXT," +
                "FOREIGN KEY (" + COLUMN_NAME_GALLERYID + ") REFERENCES " + Galleries.TABLE_NAME + "(_ID));";
        public static final String DELETE_TABLE = "DROP TABLE IF EXISTS " + TABLE_NAME;
    }
}
