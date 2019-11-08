package ca.mohawk.HealthMetrics.Models;

/**
 * Models a unit from the database.
 */
public class Unit {

    // Represents the unit name.
    public String unitName;

    // Represents the unit abbreviation.
    public String unitAbbreviation;

    // Represents the unit category id.
    public int unitCategoryId;

    /**
     * Constructs the unit.
     *
     * @param unitName         Represents the unit name.
     * @param unitAbbreviation Represents the unit abbreviation.
     * @param unitCategoryId   Represents the unit category id.
     */
    public Unit(String unitName, String unitAbbreviation, int unitCategoryId) {
        this.unitName = unitName;
        this.unitAbbreviation = unitAbbreviation;
        this.unitCategoryId = unitCategoryId;
    }
}
