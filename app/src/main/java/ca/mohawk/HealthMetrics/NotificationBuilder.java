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

/**
 * The NotificationBuilder class extends the ContextWrapper class.
 * <p>
 * Builds the notifications.
 */
public class NotificationBuilder extends ContextWrapper {

    // Instantiate the CHANNEL_ID.
    public static final String CHANNEL_ID = "channelId";

    // Instantiate the HEALTH_METRICS_CHANNEL.
    public static final String HEALTH_METRICS_CHANNEL = "healthMetricsChannel";

    // Instantiate the NotificationManager.
    private NotificationManager Manager;

    /**
     * Calls the ContextWrapper and createChannel if required.
     *
     * @param base Represents the base context.
     */
    public NotificationBuilder(Context base) {
        super(base);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            createChannel();
        }
    }

    /**
     * Creates the NotificationChannel if the correct API.
     */
    @RequiresApi(api = Build.VERSION_CODES.O)
    private void createChannel() {
        NotificationChannel channel = new NotificationChannel(CHANNEL_ID, HEALTH_METRICS_CHANNEL,
                NotificationManager.IMPORTANCE_HIGH);
        getManager().createNotificationChannel(channel);
    }

    /**
     * Gets the NotificationManager.
     *
     * @return Returns the notification manager.
     */
    public NotificationManager getManager() {
        if (Manager == null) {
            Manager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        }

        return Manager;
    }

    /**
     * Creates a notification builder based on the notification type.
     *
     * @param notification Represents the notification that will be displayed.
     * @return Returns a notification builder.
     */

    public NotificationCompat.Builder getChannelNotification(Notification notification) {

        String message = "";

        HealthMetricsDbHelper healthMetricsDbHelper = HealthMetricsDbHelper.getInstance(getApplicationContext());

        // Create message based on notificationType.
        switch (notification.notificationType) {
            case "Enter Metric Data":
                Metric metric = healthMetricsDbHelper.getMetricById(notification.targetId);
                message = metric != null ? "Input " + metric.name : "";
                break;
            case "Enter Gallery Data":
                PhotoGallery gallery = healthMetricsDbHelper.getPhotoGalleryById(notification.targetId);
                message = gallery != null ? "Input " + gallery.name : "";
                break;
            case "Refill Prescription":
                Prescription prescriptionRefill = healthMetricsDbHelper.getPrescriptionById(notification.targetId);
                message = prescriptionRefill != null ? "Refill " + prescriptionRefill.name : "";
                break;
            case "Take Prescription":
                Prescription prescriptionTake = healthMetricsDbHelper.getPrescriptionById(notification.targetId);
                message = prescriptionTake != null ? "Take " + prescriptionTake.name : "";
                break;
        }

        // Delete the notification from the database.
        healthMetricsDbHelper.deleteNotification(notification.id);

        PendingIntent contentIntent = PendingIntent.getActivity(this, 0,
                new Intent(this, MainActivity.class), PendingIntent.FLAG_UPDATE_CURRENT);

        // Return the notification builder.
        return new NotificationCompat.Builder(getApplicationContext(), CHANNEL_ID)
                .setContentTitle(notification.notificationType)
                .setContentText(message)
                .setSmallIcon(R.drawable.ic_alert)
                .setContentIntent(contentIntent)
                .setAutoCancel(true);

    }
}