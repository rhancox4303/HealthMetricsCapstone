package ca.mohawk.HealthMetrics.Models;

import java.util.Date;

public class MetricDataEntry {
    public int Id;
    public int MetricId;
    public String DataEntry;
    public String DateOfEntry;

    public MetricDataEntry(int metricId, String dataEntry, String dateOfEntry) {
        MetricId = metricId;
        DataEntry = dataEntry;
        DateOfEntry = dateOfEntry;
    }
}