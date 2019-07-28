package ca.mohawk.HealthMetrics.Models;

public class Metric {
    public int Id;
    public int UnitId;
    public String Name;
    public int UnitCategoryId;
    public int IsAddedToProfile;

    public Metric(int unitId, String name, int unitCategoryId,  int isAddedToProfile) {
        UnitId = unitId;
        Name = name;
        IsAddedToProfile = isAddedToProfile;
        UnitCategoryId = unitCategoryId;
    }

    public Metric(int id, int unitId, String name, int unitCategoryId, int isAddedToProfile) {
        Id = id;
        UnitId = unitId;
        Name = name;
        UnitCategoryId = unitCategoryId;
        IsAddedToProfile = isAddedToProfile;
    }
}
