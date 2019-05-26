package ca.mohawk.HealthMetrics.Models;

public class Prescription {
    public int Id;
    public DosageMeasurement DosageMeasurement;
    public String Name;
    public String Form;
    public String Strength;
    public String DosageAmount;
    public String Frequency;
    public double Amount;
    public String Reason;

    public Prescription(DosageMeasurement dosageMeasurement, String name, String form, String strength, String dosageAmount, String frequency, double amount, String reason) {
        DosageMeasurement = dosageMeasurement;
        Name = name;
        Form = form;
        Strength = strength;
        DosageAmount = dosageAmount;
        Frequency = frequency;
        Amount = amount;
        Reason = reason;
    }
}
