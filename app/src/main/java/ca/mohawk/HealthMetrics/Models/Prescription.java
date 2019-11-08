package ca.mohawk.HealthMetrics.Models;

/**
 * Models a Prescription from the database.
 */
public class Prescription {

    // Represents the prescription id.
    public int id;

    // Represents the prescription id.
    public int dosageMeasurementId;

    // Represents the prescription name.
    public String name;

    // Represents the prescription form.
    public String form;

    // Represents the prescription strength.
    public String strength;

    // Represents the prescription dosage amount.
    public double dosageAmount;

    // Represents the prescription frequency.
    public String frequency;

    // Represents the prescription amount.
    public double amount;

    // Represents the prescription reason.
    public String reason;

    /**
     * Constructs the prescription.
     *
     * @param id                  Represents the prescription id.
     * @param dosageMeasurementId Represents the prescription id.
     * @param name                Represents the prescription name.
     * @param form                Represents the prescription form.
     * @param strength            Represents the prescription strength.
     * @param dosageAmount        Represents the prescription dosage amount.
     * @param frequency           Represents the prescription frequency.
     * @param amount              Represents the prescription amount.
     * @param reason              Represents the prescription reason.
     */
    public Prescription(int id, int dosageMeasurementId, String name, String form, String strength, double dosageAmount, String frequency, double amount, String reason) {
        this.id = id;
        this.dosageMeasurementId = dosageMeasurementId;
        this.name = name;
        this.form = form;
        this.strength = strength;
        this.dosageAmount = dosageAmount;
        this.frequency = frequency;
        this.amount = amount;
        this.reason = reason;
    }

    /**
     * Constructs the prescription.
     *
     * @param dosageMeasurementId Represents the prescription id.
     * @param name                Represents the prescription name.
     * @param form                Represents the prescription form.
     * @param strength            Represents the prescription strength.
     * @param dosageAmount        Represents the prescription dosage amount.
     * @param frequency           Represents the prescription frequency.
     * @param amount              Represents the prescription amount.
     * @param reason              Represents the prescription reason.
     */
    public Prescription(int dosageMeasurementId, String name, String form, String strength, double dosageAmount, String frequency, double amount, String reason) {
        this.dosageMeasurementId = dosageMeasurementId;
        this.name = name;
        this.form = form;
        this.strength = strength;
        this.dosageAmount = dosageAmount;
        this.frequency = frequency;
        this.amount = amount;
        this.reason = reason;
    }
}
