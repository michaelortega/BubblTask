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
    //    Toast.makeText(context, "ALARM",Toast.LENGTH_LONG).show();
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


    }
//
//    public void createNotification(Context context, String msg, String msgText, String msgAlert){
//
//        // Define an Intent and an action to perform with it by another application
//        PendingIntent notificIntent = PendingIntent.getActivity(context, 0,
//                new Intent(context, MainActivity.class), 0);
//
//        // Builds a notification
//        NotificationCompat.Builder mBuilder =
//                new NotificationCompat.Builder(context)
//                        .setContentTitle(msg)
//                        .setTicker(msgAlert)
//                        .setContentText(msgText);
//
//        // Defines the Intent to fire when the notification is clicked
//        mBuilder.setContentIntent(notificIntent);
//
//        // Set the default notification option
//        // DEFAULT_SOUND : Make sound
//        // DEFAULT_VIBRATE : Vibrate
//        // DEFAULT_LIGHTS : Use the default light notification
//        mBuilder.setDefaults(Notification.DEFAULT_SOUND);
//
//        // Auto cancels the notification when clicked on in the task bar
//        mBuilder.setAutoCancel(true);
//
//        // Gets a NotificationManager which is used to notify the user of the background event
//        NotificationManager mNotificationManager =
//                (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
//
//        // Post the notification
//        mNotificationManager.notify(1, mBuilder.build());
//
//    }


}
