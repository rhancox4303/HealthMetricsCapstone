package ca.mohawk.HealthMetrics.Models;

/**
 * Models a Metric from the database.
 */
public class Metric {

    // Represents the metric id.
    public int id;

    // Represents the unit id.
    public int unitId;

    // Represents the metric name.
    public String name;

    // Represents the unit category id.
    public int unitCategoryId;

    // Represents whether the metric is added to the user profile.
    public int isAddedToProfile;

    /**
     * Constructs a metric.
     *
     * @param unitId           Represents the unit id.
     * @param name             Represents the metric name.
     * @param unitCategoryId   Represents the unit category id.
     * @param isAddedToProfile Represents whether the metric is added to the user profile.
     */
    public Metric(int unitId, String name, int unitCategoryId, int isAddedToProfile) {
        this.unitId = unitId;
        this.name = name;
        this.isAddedToProfile = isAddedToProfile;
        this.unitCategoryId = unitCategoryId;
    }

    /**
     * Constructs a metric.
     *
     * @param id               Represents the metric id.
     * @param unitId           Represents the unit id.
     * @param name             Represents the metric name.
     * @param unitCategoryId   Represents the unit category id.
     * @param isAddedToProfile Represents whether the metric is added to the user profile.
     */
    public Metric(int id, int unitId, String name, int unitCategoryId, int isAddedToProfile) {
        this.id = id;
        this.unitId = unitId;
        this.name = name;
        this.unitCategoryId = unitCategoryId;
        this.isAddedToProfile = isAddedToProfile;
    }
}
