package com.example.michael.bubbltask;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.provider.Settings;
import android.support.v4.app.NotificationCompat;
import android.util.Log;
import android.widget.Toast;

/**
 * Created by Michael on 7/17/2017.
 */

public class AlarmReceiver extends BroadcastReceiver {
    // Called when a broadcast is made targeting this class
    @Override
    public void onReceive(Context context, Intent intent) {

        String taskName = intent.getStringExtra("task");
        int id = intent.getIntExtra("id",0);
        Log.i("test","test");
        NotificationCompat.Builder builder = new NotificationCompat.Builder(context);
        builder.setAutoCancel(true)
                .setDefaults(Notification.DEFAULT_ALL)
                .setWhen(System.currentTimeMillis())
                .setSmallIcon(R.drawable.ic_delete_white_18dp)
                .setContentTitle("Task is Due!")
                .setContentText(taskName)
                .setDefaults(Notification.DEFAULT_LIGHTS);

        NotificationManager notificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
        notificationManager.notify(id,builder.build());
        FloatingViewService.getInstance().increaseSize();


    }


}
