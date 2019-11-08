package ca.mohawk.HealthMetrics.Models;

import androidx.annotation.NonNull;

/**
 * Models a Dosage Measurement from the database.
 */
public class DosageMeasurement {

    // Represents the dosage measurement id.
    public int id;

    // Represents the dosage measurement.
    public String dosageMeasurement;

    // Represents the unit abbreviation.
    public String unitAbbreviation;

    /**
     * Constructs a dosage measurement object.
     *
     * @param dosageMeasurement Represents the dosage measurement.
     * @param unitAbbreviation  Represents the unit abbreviation.
     */
    public DosageMeasurement(String dosageMeasurement, String unitAbbreviation) {
        this.dosageMeasurement = dosageMeasurement;
        this.unitAbbreviation = unitAbbreviation;
    }

    /**
     * Constructs a dosage measurement object.
     *
     * @param id                Represents the dosage measurement id.
     * @param dosageMeasurement Represents the dosage measurement.
     * @param unitAbbreviation  Represents the unit abbreviation.
     */
    public DosageMeasurement(int id, String dosageMeasurement, String unitAbbreviation) {
        this.id = id;
        this.dosageMeasurement = dosageMeasurement;
        this.unitAbbreviation = unitAbbreviation;
    }

    @NonNull
    @Override
    public String toString() {
        return dosageMeasurement + " (" + unitAbbreviation + ")";
    }
}
