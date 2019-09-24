package ca.mohawk.HealthMetrics.Models;

import java.util.Date;

public class DataEntry {
    public int Id;
    public int MetricId;
    public String DataEntry;
    public String DateOfEntry;

    public DataEntry(int metricId, String dataEntry, String dateOfEntry) {
        MetricId = metricId;
        DataEntry = dataEntry;
        DateOfEntry = dateOfEntry;
    }

    public DataEntry(int id, int metricId, String dataEntry, String dateOfEntry) {
        Id = id;
        MetricId = metricId;
        DataEntry = dataEntry;
        DateOfEntry = dateOfEntry;
    }
}