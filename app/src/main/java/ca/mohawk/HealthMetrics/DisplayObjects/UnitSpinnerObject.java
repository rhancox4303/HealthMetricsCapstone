package ca.mohawk.HealthMetrics.DisplayObjects;

import androidx.annotation.NonNull;

/**
 * Represents a display object that represents unit.
 * <p>
 * It used to display units in the unit spinner in the addMetricFragment.
 */
public class UnitSpinnerObject {

    // Represents the unit id.
    public int unitId;

    // Represent the name of the unit.
    private String unitName;

    /**
     * Constructs the UnitSpinnerObject.
     *
     * @param unitName Represents the name of the unit.
     * @param unitId   Represents the unit id.
     */
    public UnitSpinnerObject(String unitName, int unitId) {
        this.unitName = unitName;
        this.unitId = unitId;
    }

    @NonNull
    @Override
    public String toString() {
        return unitName;
    }
}

