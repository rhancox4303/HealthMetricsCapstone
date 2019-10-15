package ca.mohawk.HealthMetrics.Models;

import java.util.Date;

public class Notification {
    public int Id;
    public int TargetId;
    public String NotificationType;
    public String TargetDateTime;

    public Notification(int targetId, String notificationType, String targetDateTime) {
        TargetId = targetId;
        NotificationType = notificationType;
        TargetDateTime = targetDateTime;
    }

    public Notification(int id, int targetId, String notificationType, String targetDateTime) {
        Id = id;
        TargetId = targetId;
        NotificationType = notificationType;
        TargetDateTime = targetDateTime;
    }
}
