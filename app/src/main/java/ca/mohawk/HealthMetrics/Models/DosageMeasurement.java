package ca.mohawk.HealthMetrics.Models;

import android.text.GetChars;

public class DosageMeasurement {

    public int Id;
    public double DosageMeasurement;
    public String UnitAbbreviation;

    public DosageMeasurement(double dosageMeasurement, String unitAbbreviation) {
        DosageMeasurement = dosageMeasurement;
        UnitAbbreviation = unitAbbreviation;
    }
}
