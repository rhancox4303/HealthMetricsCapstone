package ca.mohawk.HealthMetrics.Models;

public class Metric {
    public int Id;
    public int UnitId;
    public String Name;
    public int IsAddedToProfile;

    public Metric(int unitId, String name, int isAddedToProfile) {
        UnitId = unitId;
        Name = name;
        IsAddedToProfile = isAddedToProfile;
    }
}
