package ca.mohawk.HealthMetrics.DisplayObjects;

public class MetricRecyclerViewObject {
    public int Id;
    public String Name;
    public String Entry;
    public String Category;

    public MetricRecyclerViewObject(int id, String name, String entry, String category) {
        Id = id;
        Name = name;
        Entry = entry;
        Category = category;
    }

    public String getName() {
        return Name;
    }

    public String getEntry(){
        return Entry;
    }
}
