package com.lvovard.sportteam;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.support.v4.app.NotificationCompat;
import android.widget.Toast;

public class BootBroadcastReceiver extends BroadcastReceiver {
  @Override
  public void onReceive(Context context, Intent intent) {
      // Launch the specified service when this message is received
    ///
    Toast.makeText(context, "boot detected",
        Toast.LENGTH_LONG).show();
    
    PendingIntent contentIntent = PendingIntent.getActivity(context, 0,
        new Intent(context, ConvocationActivity.class), 0);

NotificationCompat.Builder mBuilder =
        new NotificationCompat.Builder(context)
        .setSmallIcon(R.drawable.ic_launcher)
        .setContentTitle("My notification")
        .setContentText("boot ok!");
mBuilder.setContentIntent(contentIntent);
mBuilder.setDefaults(Notification.DEFAULT_SOUND);
mBuilder.setAutoCancel(true);
NotificationManager mNotificationManager =
    (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
mNotificationManager.notify(1, mBuilder.build());


    ///
      Intent startServiceIntent = new Intent(context, MyTestService.class);
      context.startService(startServiceIntent);
  }

}
