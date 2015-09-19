package br.edu.fa7.pomodoro.util;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.TaskStackBuilder;
import android.content.Context;
import android.content.Intent;
import android.os.CountDownTimer;

import br.edu.fa7.pomodoro.R;
import br.edu.fa7.pomodoro.activity.MainActivity;
import br.edu.fa7.pomodoro.listener.ChronometerListener;
import br.edu.fa7.pomodoro.listener.OnChronometerFinishListener;
import br.edu.fa7.pomodoro.model.Task;

/**
 * Created by alan on 9/12/15.
 */
public class Chronometer extends CountDownTimer {

    private ChronometerListener mListener;
    private OnChronometerFinishListener mOnChronometerFinishListener;
    private Context mContext;
    NotificationManager mNotificationManager;
    private Task mTask;
    private final String TAG = "Chronometer";

    public Chronometer(long millisInFuture, Context context) {
        super(millisInFuture, 1000);
        this.mContext = context;
        this.mNotificationManager = (NotificationManager) mContext.getSystemService(Context.NOTIFICATION_SERVICE);
    }

    public void setListener(ChronometerListener listener) {
        if (listener != null) {
            this.mListener = listener;
        }
    }

    public void setOnChronometerFinishListener(OnChronometerFinishListener listener) {
        if (listener != null) {
            this.mOnChronometerFinishListener = listener;
        }
    }

    public void setTask(Task task) {
        if (task != null) {
            this.mTask = task;
        }
    }

    @Override
    public void onTick(long millisUntilFinished) {
        if (this.mListener != null) {
            this.mListener.onTick(millisUntilFinished);
        }
    }

    @Override
    public void onFinish() {
        if (this.mOnChronometerFinishListener != null) {
            this.mOnChronometerFinishListener.onChronometerFinish(false);
        }

        showFinishNotification();
    }

    private void showFinishNotification() {
        String title = (this.mTask != null) ? this.mTask.getTitle() : "Fa7 Pomodoro";
        String ID = (this.mTask != null) ? this.mTask.getId().toString() : "0";

        Notification.Builder builder = new Notification.Builder(mContext)
                .setSmallIcon(R.mipmap.ic_launcher)
                .setContentTitle(title)
                .setContentText("Pomodoro finalizado para task #" + ID + ".")
                .setAutoCancel(true);


        Intent it = new Intent(mContext, MainActivity.class);

        TaskStackBuilder taskStackBuilder = TaskStackBuilder.create(mContext);
        taskStackBuilder.addParentStack(MainActivity.class);
        taskStackBuilder.addNextIntent(it);

        PendingIntent pendingIntent = taskStackBuilder
                .getPendingIntent(1, PendingIntent.FLAG_UPDATE_CURRENT);
        builder.setContentIntent(pendingIntent);

        this.mNotificationManager.notify(1, builder.build());
    }

}
