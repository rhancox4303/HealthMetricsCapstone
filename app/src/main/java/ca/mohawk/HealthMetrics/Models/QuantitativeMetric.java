package ca.mohawk.HealthMetrics.Models;

public class QuantitativeMetric {
    public int Id;
    public Unit Unit;
    public String Name;
    public Boolean IsAddedToProfile;

    public QuantitativeMetric(Unit unit, String name, Boolean isAddedToProfile) {
        Unit = unit;
        Name = name;
        IsAddedToProfile = isAddedToProfile;
    }
}
