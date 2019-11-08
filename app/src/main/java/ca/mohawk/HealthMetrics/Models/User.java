package ca.mohawk.HealthMetrics.Models;

/**
 * Models a user from the database.
 */
public class User {

    // Represents the first name.
    public String firstName;

    // Represents the last name.
    public String lastName;

    // Represents the gender.
    public String gender;

    // Represents the date of the birth.
    public String dateOfBirth;

    /**
     * Constructs a user.
     *
     * @param firstName   Represents the first name.
     * @param lastName    Represents the last name.
     * @param gender      Represents the gender.
     * @param dateOfBirth Represents the date of the birth.
     */
    public User(String firstName, String lastName, String gender, String dateOfBirth) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.gender = gender;
        this.dateOfBirth = dateOfBirth;
    }
}