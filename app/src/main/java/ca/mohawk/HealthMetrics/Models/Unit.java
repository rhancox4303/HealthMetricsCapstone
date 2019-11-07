package ca.mohawk.HealthMetrics.Models;

public class Unit {
    public int Id;
    public String UnitName;
    public String UnitAbbreviation;
    public int UnitCategoryId;

    public Unit(String unitName, String unitAbbreviation, int unitCategoryId) {
        UnitName = unitName;
        UnitAbbreviation = unitAbbreviation;
        UnitCategoryId = unitCategoryId;
    }
}
