package ca.mohawk.HealthMetrics.Models;

/**
 * Models a Data entry from the database.
 */
public class DataEntry {

    // Represents the data entry id.
    public int id;

    // Represents the metric id.
    public int metricId;

    // Represents the data entry.
    public String dataEntry;

    // Represents the date of entry.
    public String dateOfEntry;

    /**
     * Constructs a DataEntry object.
     *
     * @param metricId    Represents the metric id.
     * @param dataEntry   Represents the data entry.
     * @param dateOfEntry Represents the date of entry.
     */
    public DataEntry(int metricId, String dataEntry, String dateOfEntry) {
        this.metricId = metricId;
        this.dataEntry = dataEntry;
        this.dateOfEntry = dateOfEntry;
    }

    /**
     * Constructs a DataEntry object.
     *
     * @param id          Represents the data entry id.
     * @param metricId    Represents the metric id.
     * @param dataEntry   Represents the data entry.
     * @param dateOfEntry Represents the date of entry.
     */
    public DataEntry(int id, int metricId, String dataEntry, String dateOfEntry) {
        this.id = id;
        this.metricId = metricId;
        this.dataEntry = dataEntry;
        this.dateOfEntry = dateOfEntry;
    }
}