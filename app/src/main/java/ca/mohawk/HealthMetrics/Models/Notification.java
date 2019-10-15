package ca.mohawk.HealthMetrics.Models;

import java.util.Date;

public class Notification {
    public int Id;
    public int TargetId;
    public String NotificationType;
    public Date TargetDateTime;

    public Notification(int targetId, String notificationType, Date targetDateTime) {
        TargetId = targetId;
        NotificationType = notificationType;
        TargetDateTime = targetDateTime;
    }

    public Notification(int id, int targetId, String notificationType, Date targetDateTime) {
        Id = id;
        TargetId = targetId;
        NotificationType = notificationType;
        TargetDateTime = targetDateTime;
    }
}
