package ca.mohawk.HealthMetrics.Models;

public class Prescription {
    public int Id;
    public int DosageMeasurementId;
    public String Name;
    public String Form;
    public String Strength;
    public double DosageAmount;
    public String Frequency;
    public double Amount;
    public String Reason;

    public Prescription(int id, int dosageMeasurementId, String name, String form, String strength, double dosageAmount, String frequency, double amount, String reason) {
        Id = id;
        DosageMeasurementId = dosageMeasurementId;
        Name = name;
        Form = form;
        Strength = strength;
        DosageAmount = dosageAmount;
        Frequency = frequency;
        Amount = amount;
        Reason = reason;
    }

    public Prescription(int dosageMeasurementId, String name, String form, String strength, double dosageAmount, String frequency, double amount, String reason) {
        DosageMeasurementId = dosageMeasurementId;
        Name = name;
        Form = form;
        Strength = strength;
        DosageAmount = dosageAmount;
        Frequency = frequency;
        Amount = amount;
        Reason = reason;
    }
}
