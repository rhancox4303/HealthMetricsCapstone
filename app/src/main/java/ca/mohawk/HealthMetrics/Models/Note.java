package ca.mohawk.HealthMetrics.Models;

import java.util.Date;

public class Note {
    public int Id;
    public Date DateOfEntry;
    public String NoteContent;

    public Note(Date dateOfEntry, String noteContent) {
        DateOfEntry = dateOfEntry;
        NoteContent = noteContent;
    }
}
