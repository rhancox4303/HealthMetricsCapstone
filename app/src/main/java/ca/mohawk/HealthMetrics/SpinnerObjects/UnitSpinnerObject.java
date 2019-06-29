package ca.mohawk.HealthMetrics.SpinnerObjects;

public class UnitSpinnerObject {

    private String UnitName;
    private int UnitId;

    public UnitSpinnerObject(String unitName, int unitId) {
        UnitName = unitName;
        UnitId = unitId;
    }

    public String getUnitName() {
        return UnitName;
    }

    public int getUnitId() {
        return UnitId;
    }

    @Override
    public String toString() {
        return UnitName ;
    }
}

