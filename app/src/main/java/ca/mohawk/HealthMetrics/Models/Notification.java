package ca.mohawk.HealthMetrics.Models;

/**
 * Models a notification from the database.
 */
public class Notification {

    // Represents the notification id.
    public int id;

    // Represents the notification target id.
    public int targetId;

    // Represents the notification type.
    public String notificationType;

    // Represents the notification target date time.
    public String targetDateTime;

    /**
     * Constructs a notification.
     *
     * @param targetId         Represents the notification target id.
     * @param notificationType Represents the notification type.
     * @param targetDateTime   Represents the notification target date time.
     */
    public Notification(int targetId, String notificationType, String targetDateTime) {
        this.targetId = targetId;
        this.notificationType = notificationType;
        this.targetDateTime = targetDateTime;
    }

    /**
     * Constructs a notification.
     *
     * @param id               Represents the notification id.
     * @param targetId         Represents the notification target id.
     * @param notificationType Represents the notification type.
     * @param targetDateTime   Represents the notification target date time.
     */
    public Notification(int id, int targetId, String notificationType, String targetDateTime) {
        this.id = id;
        this.targetId = targetId;
        this.notificationType = notificationType;
        this.targetDateTime = targetDateTime;
    }
}
