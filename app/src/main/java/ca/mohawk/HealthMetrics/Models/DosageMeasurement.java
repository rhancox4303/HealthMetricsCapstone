package ca.mohawk.HealthMetrics.Models;

import android.text.GetChars;

public class DosageMeasurement {

    public int Id;
    public String DosageMeasurement;
    public String UnitAbbreviation;

    public DosageMeasurement(String dosageMeasurement, String unitAbbreviation) {
        DosageMeasurement = dosageMeasurement;
        UnitAbbreviation = unitAbbreviation;
    }

    public DosageMeasurement(int id, String dosageMeasurement, String unitAbbreviation) {
        Id = id;
        DosageMeasurement = dosageMeasurement;
        UnitAbbreviation = unitAbbreviation;
    }

    @Override
    public String toString() {
        return DosageMeasurement + " (" + UnitAbbreviation+ ")";
    }
}
