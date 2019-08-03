package ca.mohawk.HealthMetrics.DisplayObjects;

public class PrescriptionRecyclerViewObject {
    public int Id;
    public String Name;
    public String DosageAmount;
    public String DosageMeasurement;
    public String Frequency;
    public double Amount;

    public PrescriptionRecyclerViewObject(int id, String name, String dosageAmount, String dosageMeasurement, String frequency, double amount) {
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

    public String getDosageAmount() {
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
}
