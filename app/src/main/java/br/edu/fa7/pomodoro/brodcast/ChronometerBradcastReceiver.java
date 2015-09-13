package br.edu.fa7.pomodoro.brodcast;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.TaskStackBuilder;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import br.edu.fa7.pomodoro.R;
import br.edu.fa7.pomodoro.activity.MainActivity;

/**
 * Created by bruno on 12/08/15.
 */
public class ChronometerBradcastReceiver extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {

        if(intent.getAction().equals("br.edu.fa7.POMODORO_CHRONOMETER")){

            Notification.Builder builder = new Notification.Builder(context)
                    .setSmallIcon(R.mipmap.ic_launcher)
                    .setContentTitle("Pomodoro Notification")
                    .setContentText("Clique aqui")
                    .setAutoCancel(true);

            Intent it = new Intent(context, MainActivity.class);

            TaskStackBuilder taskStackBuilder = TaskStackBuilder.create(context);
            taskStackBuilder.addParentStack(MainActivity.class);
            taskStackBuilder.addNextIntent(it);

            PendingIntent pendingIntent = taskStackBuilder
                    .getPendingIntent(1, PendingIntent.FLAG_UPDATE_CURRENT);
            builder.setContentIntent(pendingIntent);

            NotificationManager notificationManager = (NotificationManager) context
                    .getSystemService(Context.NOTIFICATION_SERVICE);
            notificationManager.notify(1, builder.build());

        }

    }

}
