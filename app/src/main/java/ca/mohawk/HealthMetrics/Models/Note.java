package ca.mohawk.HealthMetrics.Models;

/**
 * Models a note from the database.
 */
public class Note {

    // Represents a note id.
    public int id;

    // Represents date of entry.
    public String dateOfEntry;

    // Represents the note content.
    public String noteContent;

    /**
     * Constructs note.
     *
     * @param dateOfEntry Represents date of entry.
     * @param noteContent Represents the note content.
     */
    public Note(String dateOfEntry, String noteContent) {
        this.dateOfEntry = dateOfEntry;
        this.noteContent = noteContent;
    }

    /**
     * Constructs note.
     *
     * @param id          Represents a note id.
     * @param dateOfEntry Represents date of entry.
     * @param noteContent Represents the note content.
     */
    public Note(int id, String dateOfEntry, String noteContent) {
        this.id = id;
        this.dateOfEntry = dateOfEntry;
        this.noteContent = noteContent;
    }
}
