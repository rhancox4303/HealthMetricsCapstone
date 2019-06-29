package ca.mohawk.HealthMetrics.Models;

public class Unit {
    public int Id;
    public String UnitName;
    public String UnitAbbreviation;
    public String UnitCategory;

    public Unit(String unitName, String unitAbbreviation, String unitCategory) {
        UnitName = unitName;
        UnitAbbreviation = unitAbbreviation;
        UnitCategory = unitCategory;
    }

    @Override
    public String toString() {
        return UnitName;
    }
}
