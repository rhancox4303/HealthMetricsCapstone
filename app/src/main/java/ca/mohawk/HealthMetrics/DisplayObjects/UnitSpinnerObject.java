package ca.mohawk.HealthMetrics.DisplayObjects;

public class UnitSpinnerObject {

    private String UnitName;
    private int UnitId;

    public UnitSpinnerObject(String unitName, int unitId) {
        UnitName = unitName;
        UnitId = unitId;
    }

    public int getUnitId() {
        return UnitId;
    }

    @Override
    public String toString() {
        return UnitName ;
    }
}

