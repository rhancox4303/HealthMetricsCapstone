package ca.mohawk.HealthMetrics.DisplayObjects;

import androidx.annotation.NonNull;

/**
 * Represents a display object that represents a metric object.
 * <p>
 * It used to display metrics in the metric spinner in the addMetricFragment.
 */
public class AddMetricSpinnerObject {

    // Represents the metric id.
    public int metricId;

    // Represents the id of the metric's unit category.
    public int unitCategoryId;

    // Represents the metric name.
    private String metricName;

    /**
     * Creates the AddMetricSpinnerObject.
     *
     * @param unitCategoryId Represents the unit category id.
     * @param metricName     Represents the metric name.
     * @param metricId       Represents the metric id.
     */
    public AddMetricSpinnerObject(int unitCategoryId, String metricName, int metricId) {
        this.metricId = metricId;
        this.unitCategoryId = unitCategoryId;
        this.metricName = metricName;
    }

    @NonNull
    @Override
    public String toString() {
        return metricName;
    }
}

