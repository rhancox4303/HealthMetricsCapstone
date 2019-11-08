package ca.mohawk.HealthMetrics.DisplayObjects;

import android.annotation.SuppressLint;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

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

    public String getDataEntry() {
        return DataEntry + " " + UnitAbbreviation;
    }

    public float getNumericDataEntry(){
        return Float.valueOf(DataEntry);
    }

    public Date getDateOfEntry(){

        @SuppressLint("SimpleDateFormat") SimpleDateFormat dateFormat = new SimpleDateFormat("HH:mm MM-dd-yyyy");
        Date convertedDate = new Date();

        try {
            convertedDate = dateFormat.parse(DateOfEntry);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return convertedDate;
    }
}

