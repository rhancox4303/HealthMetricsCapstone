package ca.mohawk.HealthMetrics.Models;

import java.util.Date;

public class Notifications {
    public int Id;
    public Prescription Prescription;
    public PhotoGallery PhotoGallery;
    public QuantitativeMetric QuantitativeMetric;
    public String NotificationType;
    public Date TargetDateTime;

    public Notifications(Prescription prescription, String notificationType, Date targetDateTime) {
        Prescription = prescription;
        NotificationType = notificationType;
        TargetDateTime = targetDateTime;
    }
    public Notifications(QuantitativeMetric quantitativeMetric, String notificationType, Date targetDateTime) {
        QuantitativeMetric = quantitativeMetric;
        NotificationType = notificationType;
        TargetDateTime = targetDateTime;
    }
    public Notifications(PhotoGallery photoGallery, String notificationType, Date targetDateTime) {
        PhotoGallery = photoGallery;
        NotificationType = notificationType;
        TargetDateTime = targetDateTime;
    }
}
