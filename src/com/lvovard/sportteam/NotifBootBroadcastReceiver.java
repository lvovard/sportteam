package com.lvovard.sportteam;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

public class NotifBootBroadcastReceiver extends BroadcastReceiver {
  @Override
  public void onReceive(Context context, Intent intent) {
      // Launch the specified service when this message is received
    ///
    //Toast.makeText(context, "boot detected",Toast.LENGTH_LONG).show();
    
    //PendingIntent contentIntent = PendingIntent.getActivity(context, 0,new Intent(context, ConvocationActivity.class), 0);

//NotificationCompat.Builder mBuilder =
//        new NotificationCompat.Builder(context)
//        .setSmallIcon(R.drawable.ic_launcher)
//        .setContentTitle("My notification")
//        .setContentText("boot ok!");
//mBuilder.setContentIntent(contentIntent);
//mBuilder.setDefaults(Notification.DEFAULT_SOUND);
//mBuilder.setAutoCancel(true);
//NotificationManager mNotificationManager =
//    (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
//mNotificationManager.notify(1, mBuilder.build());


    ///
      Intent startServiceIntent = new Intent(context, NotifMyTestService.class);
      context.startService(startServiceIntent);
  }

}
