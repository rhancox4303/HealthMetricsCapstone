package ca.mohawk.HealthMetrics.Models;

import androidx.annotation.NonNull;

/**
 * Models a unit category from the database.
 */
public class UnitCategory {

    // Represents the unit category id.
    public int id;

    // Represents the unit category.
    public String unitCategory;

    /**
     * Constructs the unit category.
     *
     * @param id           Represents the unit category id.
     * @param unitCategory Represents the unit category.
     */
    public UnitCategory(int id, String unitCategory) {
        this.id = id;
        this.unitCategory = unitCategory;

    }

    @NonNull
    @Override
    public String toString() {
        return unitCategory;
    }
}
