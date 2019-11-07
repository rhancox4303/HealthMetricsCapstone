package ca.mohawk.HealthMetrics.DisplayObjects;

import androidx.annotation.NonNull;

public class PrescriptionDisplayObject {
    public int Id;
    public String Name;
    private double DosageAmount;
    public String DosageMeasurement;
    private String Frequency;
    public double Amount;

    public PrescriptionDisplayObject(int id, String name, double dosageAmount, String dosageMeasurement, String frequency, double amount) {
        Id = id;
        Name = name;
        DosageAmount = dosageAmount;
        DosageMeasurement = dosageMeasurement;
        Frequency = frequency;
        Amount = amount;
    }

    public String getName() {
        return Name;
    }

    public double getDosageAmount() {
        return DosageAmount;
    }

    public String getDosageMeasurement() {
        return DosageMeasurement;
    }

    public String getFrequency() {
        return Frequency;
    }

    public double getAmount() {
        return Amount;
    }

    @NonNull
    @Override
    public String toString() {
        return Name;
    }
}
