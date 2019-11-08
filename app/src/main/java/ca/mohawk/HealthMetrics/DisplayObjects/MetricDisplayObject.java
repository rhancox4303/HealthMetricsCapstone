package ca.mohawk.HealthMetrics.DisplayObjects;

import androidx.annotation.NonNull;

/**
 * Represents a display object that represents a metric object.
 * <p>
 * It used to display metrics to the user.
 */
public class MetricDisplayObject {

    // Represents the metric id.
    public int id;

    // Represents the metric name.
    public String name;

    // Represents the last data entry.
    public String latestDataEntry;

    // Represents the category of the metric.
    public String category;

    /**
     * Constructs the MetricDisplayObject.
     *
     * @param id              Represents the metric id.
     * @param name            Represents the metric name.
     * @param latestDataEntry Represents the last data entry.
     * @param category        Represents the category of the metric.
     */
    public MetricDisplayObject(int id, String name, String latestDataEntry, String category) {
        this.id = id;
        this.name = name;
        this.latestDataEntry = latestDataEntry;
        this.category = category;
    }

    @NonNull
    @Override
    public String toString() {
        return name;
    }
}
