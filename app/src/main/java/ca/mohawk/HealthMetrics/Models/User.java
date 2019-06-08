package ca.mohawk.HealthMetrics.Models;

import java.util.Date;

public class User {
    public int Id;
    public String FirstName;
    public String LastName;
    public String Gender;
    public String DateOfBirth;

    public User(String firstName, String lastName, String gender, String dateOfBirth) {
        FirstName = firstName;
        LastName = lastName;
        Gender = gender;
        DateOfBirth = dateOfBirth;
    }
}
