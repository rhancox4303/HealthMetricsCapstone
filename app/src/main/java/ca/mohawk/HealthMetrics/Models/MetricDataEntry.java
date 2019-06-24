package ca.mohawk.HealthMetrics.Models;

import java.util.Date;

public class MetricDataEntry {
    public int Id;
    public Metric Metric;
    public double DataEntry;
    public Date DateOfEntry;

    public MetricDataEntry(Metric metric, double dataEntry, Date dateOfEntry) {
        Metric = metric;
        DataEntry = dataEntry;
        DateOfEntry = dateOfEntry;
    }
}
