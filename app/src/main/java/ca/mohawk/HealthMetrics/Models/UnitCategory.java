package ca.mohawk.HealthMetrics.Models;

public class UnitCategory {

    public int Id;
    public String UnitCategory;

    public UnitCategory(int id, String unitCategory) {
        Id = id;
        UnitCategory = unitCategory;

    }

    public UnitCategory(String unitCategory) {
        UnitCategory = unitCategory;
        Id = 0;
    }

    public int getId() {
        return Id;
    }

    @Override
    public String toString() {
        return UnitCategory;
    }
}
