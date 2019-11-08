package ca.mohawk.HealthMetrics;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

import androidx.core.app.NotificationCompat;
import ca.mohawk.HealthMetrics.Models.Notification;

/**
 * NotificationReceiver extends theBroadcastReceiver class.
 * <p>
 * Receives the notification intents and displays a notification.
 */
public class NotificationReceiver extends BroadcastReceiver {

    /**
     * Receives the intent.
     *
     * @param context Represents the application context.
     * @param intent  Represents the received intent.
     */
    @Override
    public void onReceive(Context context, Intent intent) {

        HealthMetricsDbHelper healthMetricsDbHelper = HealthMetricsDbHelper.getInstance(context);

        int id = intent.getIntExtra("id", -1);

        // Get the notification from the database.
        Notification notification = healthMetricsDbHelper.getNotificationById(id);

        // Verify the notification was found.
        if (notification != null) {

            //Build and display the notification.
            NotificationBuilder notificationBuilder = new NotificationBuilder(context);

            NotificationCompat.Builder builder = notificationBuilder.getChannelNotification(notification);

            notificationBuilder.getManager().notify(id, builder.build());
        } else {
            Log.e("Notification Error", "Notification was not found.");
        }
    }
}
