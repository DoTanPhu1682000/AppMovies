package com.phudt7.movie;

import static com.phudt7.movie.Const.Const.CHANNEL_ID;

import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;

import com.phudt7.movie.activity.MainActivity;
import com.phudt7.movie.db.MoviesDatabase;
import com.phudt7.movie.model.Reminder;

import java.util.List;

public class AlarmBroadcastReminder extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        if (Intent.ACTION_BOOT_COMPLETED.equals(intent.getAction())) {
            startRescheduleAlarmsService(context);
        } else {
            startAlarmService(context, intent);
        }
    }

    private void startAlarmService(Context context, Intent intent) {
        Intent notificationIntent = new Intent(context, MainActivity.class);
        PendingIntent pendingIntent = PendingIntent.getActivity(context, 0, notificationIntent, 0);
        long[] pattern = {0, 100, 1000};
        NotificationCompat.Builder notification = new NotificationCompat.Builder(context, CHANNEL_ID)
                .setContentTitle(intent.getStringExtra("TITLE1"))
                .setContentText(intent.getStringExtra("TITLE2"))
                .setSmallIcon(R.mipmap.ic_launcher)
                .setVibrate(pattern)
                .setContentIntent(pendingIntent);

        NotificationManagerCompat notificationManager = NotificationManagerCompat.from(context);
        notificationManager.notify(111, notification.build());
    }

    private void startRescheduleAlarmsService(Context context) {
        List<Reminder> reminders = MoviesDatabase.getInstance(context).moviesDAO().getReminder();
        for (Reminder a : reminders) {
            a.setSchedule(context);
        }
    }
}
