package ca.mohawk.HealthMetrics.DisplayObjects;

import androidx.annotation.NonNull;

public class MetricDisplayObject {
    public int Id;
    public String Name;
    public String Entry;
    public String Category;

    public MetricDisplayObject(int id, String name, String entry, String category) {
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

    @NonNull
    @Override
    public String toString() {
        return Name;
    }
}
