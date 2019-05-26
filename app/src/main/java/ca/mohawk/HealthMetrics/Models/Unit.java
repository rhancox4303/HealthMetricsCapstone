package ca.mohawk.HealthMetrics.Models;

public class Unit {
    public int Id;
    public String UnitName;
    public String UnitAbbreviation;

    public Unit(String unitName, String unitAbbreviation) {
        UnitName = unitName;
        UnitAbbreviation = unitAbbreviation;
    }
}
