package ca.mohawk.HealthMetrics.DisplayObjects;

public class MetricSpinnerObject {

    private String UnitCategory;
    private String MetricName;
    private int MetridId;
    public MetricSpinnerObject(String unitCategory, String metricName, int metricId) {
        MetridId = metricId;
        UnitCategory = unitCategory;
        MetricName = metricName;
    }

    public String getUnitCategory () {
        return UnitCategory;
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

