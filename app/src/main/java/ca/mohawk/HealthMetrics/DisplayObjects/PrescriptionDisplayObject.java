package ca.mohawk.HealthMetrics.DisplayObjects;

import androidx.annotation.NonNull;
import ca.mohawk.HealthMetrics.Models.Notification;

public class PrescriptionDisplayObject {
    public int Id;
    public String Name;
    public double DosageAmount;
    public String DosageMeasurement;
    public String Frequency;
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

    private String getDosageAmount() {
        return DosageAmount + " " + DosageMeasurement;
    }

    public String getFrequency() {
        return getDosageAmount() + " " + Frequency;
    }

    public String getAmount(){
        return Amount + " " + DosageMeasurement;
    }

    public String getInformation(){
        return Name + " " + getFrequency();
    }

    @NonNull
    @Override
    public String toString() {
        return Name;
    }
}
