package ca.mohawk.HealthMetrics.DisplayObjects;

import androidx.annotation.NonNull;


/**
 * Represents a display object that represents a prescription object.
 * <p>
 * It used to display the prescriptions to the user.
 */

public class PrescriptionDisplayObject {

    // Represents the prescription id.
    public int id;

    // Represents the name of the prescription.
    public String name;

    // Represents the frequency of the prescription.
    public String frequency;

    // Represents the prescription amount.
    public double amount;

    // Represents the dosage amount.
    private double dosageAmount;

    // Represent the dosage measurement.
    private String dosageMeasurement;

    /**
     * Constructs a PrescriptionDisplayObject.
     *
     * @param id                Represents the prescription id.
     * @param name              Represents the name of the prescription.
     * @param dosageAmount      Represents the dosage amount.
     * @param dosageMeasurement Represent the dosage measurement.
     * @param frequency         Represents the frequency of the prescription.
     * @param amount            Represents the dosage amount.
     */
    public PrescriptionDisplayObject(int id, String name, double dosageAmount, String dosageMeasurement, String frequency, double amount) {
        this.id = id;
        this.name = name;
        this.dosageAmount = dosageAmount;
        this.dosageMeasurement = dosageMeasurement;
        this.frequency = frequency;
        this.amount = amount;
    }

    /**
     * Concatenates the dosageAmount and the dosageMeasurement.
     *
     * @return returns the concatenated string.
     */
    private String getDosageAmount() {
        return dosageAmount + " " + dosageMeasurement;
    }

    /**
     * Concatenates the dosageAmount and the frequency.
     *
     * @return returns the concatenated string.
     */
    public String getFrequency() {
        return getDosageAmount() + "\n" + frequency;
    }

    /**
     * Concatenates the amount and the dosageMeasurement.
     *
     * @return returns the concatenated string.
     */
    public String getAmount() {
        return amount + "\n" + dosageMeasurement;
    }

    /**
     * Concatenates the name and the frequency.
     *
     * @return returns the concatenated string.
     */
    public String getInformation() {
        return name + "\n" + getFrequency();
    }

    @NonNull
    @Override
    public String toString() {
        return name;
    }
}
