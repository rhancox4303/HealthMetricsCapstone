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

    public int getId() {
        return Id;
    }

    public void setId(int id) {
        Id = id;
    }

    public int getDosageMeasurementId() {
        return DosageMeasurementId;
    }

    public void setDosageMeasurementId(int dosageMeasurementId) {
        DosageMeasurementId = dosageMeasurementId;
    }

    public String getName() {
        return Name;
    }

    public void setName(String name) {
        Name = name;
    }

    public String getForm() {
        return Form;
    }

    public void setForm(String form) {
        Form = form;
    }

    public String getStrength() {
        return Strength;
    }

    public void setStrength(String strength) {
        Strength = strength;
    }

    public double getDosageAmount() {
        return DosageAmount;
    }

    public String getFrequency() {
        return Frequency;
    }

    public void setFrequency(String frequency) {
        Frequency = frequency;
    }

    public double getAmount() {
        return Amount;
    }

    public void setAmount(double amount) {
        Amount = amount;
    }

    public String getReason() {
        return Reason;
    }

    public void setReason(String reason) {
        Reason = reason;
    }
}
