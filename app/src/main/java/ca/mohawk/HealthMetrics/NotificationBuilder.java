package ca.mohawk.HealthMetrics;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.ContextWrapper;
import android.content.Intent;
import android.os.Build;

import androidx.annotation.RequiresApi;
import androidx.core.app.NotificationCompat;
import ca.mohawk.HealthMetrics.Models.Metric;
import ca.mohawk.HealthMetrics.Models.Notification;
import ca.mohawk.HealthMetrics.Models.PhotoGallery;
import ca.mohawk.HealthMetrics.Models.Prescription;

public class NotificationBuilder extends ContextWrapper {
    public static final String channelID = "channelID";
    public static final String channelName = "Channel name";

    private NotificationManager Manager;

    public NotificationBuilder(Context base) {
        super(base);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            createChannel();
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    private void createChannel() {
        NotificationChannel channel = new NotificationChannel(channelID, channelName, NotificationManager.IMPORTANCE_HIGH);

        getManager().createNotificationChannel(channel);
    }

    public NotificationManager getManager() {
        if (Manager == null) {
            Manager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        }

        return Manager;
    }

    public NotificationCompat.Builder getChannelNotification(Notification notification) {
        String message = "";
        HealthMetricsDbHelper healthMetricsDbHelper = HealthMetricsDbHelper.getInstance(getApplicationContext());
        switch (notification.notificationType) {
            case "Enter Metric Data":
                Metric metric = healthMetricsDbHelper.getMetricById(notification.targetId);
                message = "Reminder to enter your " + metric.name;
                break;
            case "Enter Gallery Data":
                PhotoGallery gallery = healthMetricsDbHelper.getPhotoGalleryById(notification.targetId);
                message = "Reminder to enter a photo for " + gallery.name;
                break;
            case "Refill Prescription":
                Prescription prescriptionRefill = healthMetricsDbHelper.getPrescriptionById(notification.targetId);
                message = "Reminder to refill your " + prescriptionRefill.name;
                break;
            case "Take Prescription":
                Prescription prescriptionTake = healthMetricsDbHelper.getPrescriptionById(notification.targetId);
                message = "Reminder to take your " + prescriptionTake.name;
                break;
        }

        healthMetricsDbHelper.deleteNotification(notification.id);

        PendingIntent contentIntent = PendingIntent.getActivity(this, 0,
                new Intent(this, MainActivity.class), PendingIntent.FLAG_UPDATE_CURRENT);

        return new NotificationCompat.Builder(getApplicationContext(), channelID)
                .setContentTitle(notification.notificationType)
                .setContentText(message)
                .setSmallIcon(R.drawable.ic_alert)
                .setContentIntent(contentIntent)
                .setAutoCancel(true);

    }
}