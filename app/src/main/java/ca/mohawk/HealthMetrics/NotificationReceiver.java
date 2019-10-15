package ca.mohawk.HealthMetrics;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import androidx.core.app.NotificationCompat;

public class NotificationReceiver extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        NotificationBuilder notificationBuilder= new NotificationBuilder(context);
        NotificationCompat.Builder nb = notificationBuilder.getChannelNotification();
        notificationBuilder.getManager().notify(1, nb.build());
    }
}
