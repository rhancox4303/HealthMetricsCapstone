package ca.mohawk.HealthMetrics.Models;

import java.util.Date;

public class Note {
    public int Id;
    public String DateOfEntry;
    public String NoteContent;

    public Note(String dateOfEntry, String noteContent) {
        DateOfEntry = dateOfEntry;
        NoteContent = noteContent;
    }

    public Note(int id, String dateOfEntry, String noteContent) {
        Id = id;
        DateOfEntry = dateOfEntry;
        NoteContent = noteContent;
    }
}
