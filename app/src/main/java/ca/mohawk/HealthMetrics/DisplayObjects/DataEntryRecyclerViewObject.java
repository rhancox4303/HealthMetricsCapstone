package ca.mohawk.HealthMetrics.DisplayObjects;

public class DataEntryRecyclerViewObject {

    public int Id;
    public String DateOfEntry;
    public String DataEntry;
    public String UnitAbbreviation;

    public DataEntryRecyclerViewObject(int id, String dateOfEntry, String dataEntry, String unitAbbreviation) {
        Id = id;
        DateOfEntry = dateOfEntry;
        DataEntry = dataEntry;
        UnitAbbreviation = unitAbbreviation;
    }

    public int getId() {
        return Id;
    }

    public String getDateOfEntry() {
        return DateOfEntry;
    }

    public String getDataEntry() {
        return DataEntry + " " + UnitAbbreviation;
    }
}
