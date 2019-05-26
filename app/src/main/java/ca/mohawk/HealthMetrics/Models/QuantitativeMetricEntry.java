package ca.mohawk.HealthMetrics.Models;

import java.util.Date;

public class QuantitativeMetricEntry {
    public int Id;
    public QuantitativeMetric QuantitativeMetric;
    public double DataEntry;
    public Date DateOfEntry;

    public QuantitativeMetricEntry(QuantitativeMetric quantitativeMetric, double dataEntry, Date dateOfEntry) {
        QuantitativeMetric = quantitativeMetric;
        DataEntry = dataEntry;
        DateOfEntry = dateOfEntry;
    }
}
