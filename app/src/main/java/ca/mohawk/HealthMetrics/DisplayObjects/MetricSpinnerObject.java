package ca.mohawk.HealthMetrics.DisplayObjects;

import androidx.annotation.NonNull;

public class MetricSpinnerObject {

    private int UnitCategoryId;
    private String MetricName;
    private int MetridId;

    public MetricSpinnerObject(int unitCategory, String metricName, int metricId) {
        MetridId = metricId;
        UnitCategoryId = unitCategory;
        MetricName = metricName;
    }

    public int getUnitCategoryId () {
        return UnitCategoryId;
    }

    public int getMetricId(){
        return  MetridId;
    }

    @NonNull
    @Override
    public String toString () {
        return MetricName;
    }
}

