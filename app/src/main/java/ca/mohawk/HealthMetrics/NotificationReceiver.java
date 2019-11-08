package ca.mohawk.HealthMetrics;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import androidx.core.app.NotificationCompat;
import ca.mohawk.HealthMetrics.Models.Notification;

public class NotificationReceiver extends BroadcastReceiver {
    @Override

    public void onReceive(Context context, Intent intent) {
        HealthMetricsDbHelper healthMetricsDbHelper = HealthMetricsDbHelper.getInstance(context);
        int id = intent.getIntExtra("id", -1);
        Notification notification = healthMetricsDbHelper.getNotificationById(id);
        NotificationBuilder notificationBuilder = new NotificationBuilder(context);
        NotificationCompat.Builder nb = notificationBuilder.getChannelNotification(notification);
        notificationBuilder.getManager().notify(id, nb.build());
    }
}
