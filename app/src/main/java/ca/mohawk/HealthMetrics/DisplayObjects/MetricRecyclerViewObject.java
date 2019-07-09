package ca.mohawk.HealthMetrics.DisplayObjects;

import ca.mohawk.HealthMetrics.Models.Metric;

public class MetricRecyclerViewObject {
    public int Id;
    public String MetricName;
    public String LatestMetricDataEntry;
    public String UnitAbbreviation;
    public String MetricCategory;

    public MetricRecyclerViewObject(int id, String metricName, String latestMetricDataEntry, String unitAbbreviation, String metricCategory) {
        Id = id;
        MetricName = metricName;
        LatestMetricDataEntry = latestMetricDataEntry;
        UnitAbbreviation = unitAbbreviation;
        MetricCategory = metricCategory;
    }

    public String getMetricName() {
        return MetricName;
    }
    public String getLatestMetricDataEntry(){
        if(MetricCategory.equals("Gallery")){
            return "Photo Gallery";
        } else if(LatestMetricDataEntry.equals("No Data Available")){
            return LatestMetricDataEntry;
        }else{
            return LatestMetricDataEntry + " " + UnitAbbreviation;
        }
    }
}
