package ca.mohawk.HealthMetrics.DisplayObjects;

public class MetricRecyclerViewObject {
    public String MetricName;
    public String LatestMetricDataEntry;
    public String UnitAbbreviation;
    public String MetricCategory;

    public MetricRecyclerViewObject(String metricName, String latestMetricDataEntry, String unitAbbreviation, String metricCategory) {
        MetricName = metricName;
        LatestMetricDataEntry = latestMetricDataEntry;
        UnitAbbreviation = unitAbbreviation;
        MetricCategory = metricCategory;
    }

    public String getMetricName() {
        return MetricName;
    }
    public String getLatestMetricDataEntry(){
        if(LatestMetricDataEntry.equals("No Data Available")){
            return LatestMetricDataEntry;
        }else{
            return LatestMetricDataEntry + " " + UnitAbbreviation;
        }
    }
}
