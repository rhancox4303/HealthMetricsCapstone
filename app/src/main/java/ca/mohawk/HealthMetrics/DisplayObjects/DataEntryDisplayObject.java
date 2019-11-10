package ca.mohawk.HealthMetrics.DisplayObjects;

import android.annotation.SuppressLint;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Represents a display object that represents a data entry object.
 * <p>
 * It used to display the data entries to the user.
 */
public class DataEntryDisplayObject {

    // Represents the data entry id.
    public int id;

    // Represents the date and time of the data entry.
    public String dateOfEntry;

    // Represents the data of the data entry.
    public String data;

    // Represent the abbreviation of the unit that the data entry uses to measure it's data.
    public String unitAbbreviation;

    /**
     * Constructs the DataEntryDisplayObject.
     *
     * @param id               Represents the data entry id.
     * @param dateOfEntry      Represents the date and time of the data entry.
     * @param data             Represents the data of the data entry.
     * @param unitAbbreviation Represents the unit abbreviation.
     */
    public DataEntryDisplayObject(int id, String dateOfEntry, String data, String unitAbbreviation) {
        this.id = id;
        this.dateOfEntry = dateOfEntry;
        this.data = data;
        this.unitAbbreviation = unitAbbreviation;
    }

    /**
     * Concatenates the data and the unitAbbreviation.
     *
     * @return Returns the data.
     */
    public String getData() {
        return data + " " + unitAbbreviation;
    }

    /**
     * Convert the data entry to a float.
     *
     * @return Return a float representation of the data.
     */
    public float getNumericData() {
        return Float.valueOf(data);
    }

    /**
     * Converts the date of entry to a date object.
     *
     * @return Returns converted dateOfEntry date object.
     */
    public Date getDateOfEntry() {

        @SuppressLint("SimpleDateFormat") SimpleDateFormat dateFormat = new SimpleDateFormat("HH:mm MM-dd-yyyy");
        Date date = new Date();

        try {
            date = dateFormat.parse(dateOfEntry);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return date;
    }
}

