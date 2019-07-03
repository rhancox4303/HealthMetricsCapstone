package ca.mohawk.HealthMetrics.DisplayObjects;

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

    public String getValue () {
        return MetricName;
    }

    public int getMetridId(){
        return  MetridId;
    }

    @Override
    public String toString () {
        return MetricName;
    }
}

