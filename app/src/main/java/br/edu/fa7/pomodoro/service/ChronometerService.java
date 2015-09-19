package br.edu.fa7.pomodoro.service;

import android.app.Service;
import android.content.Intent;
import android.os.Binder;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.util.Log;
import android.widget.TextView;

import java.util.concurrent.TimeUnit;

import br.edu.fa7.pomodoro.listener.OnChronometerFinishListener;
import br.edu.fa7.pomodoro.model.Task;
import br.edu.fa7.pomodoro.util.Chronometer;
import br.edu.fa7.pomodoro.listener.ChronometerListener;

/**
 * Created by bruno on 26/08/15.
 */
public class ChronometerService extends Service implements ChronometerListener, OnChronometerFinishListener {

    private IBinder binder;

    private Chronometer mChronometer;
    private TextView mTextView;

//    public static final long START_TIME = 1500000; // 25 min.
    public static final long START_TIME = 10000; // 25 min.

    private boolean mIsPlaying = false;
    private OnChronometerFinishListener mFinishListener;

    private final String TAG = "ChronometerService";

    public ChronometerService() {
        this.binder = new LocalBinder();
    }

    public void onCreate() {
        super.onCreate();
    }

    @Override
    public void onTick(long millisUntilFinished) {
        long minutes = TimeUnit.MILLISECONDS.toMinutes(millisUntilFinished);
        long seconds = TimeUnit.MILLISECONDS.toSeconds(millisUntilFinished) - TimeUnit.MINUTES.toSeconds(TimeUnit.MILLISECONDS.toMinutes(millisUntilFinished));

        Log.i(TAG, "Millis Until Finished: " + millisUntilFinished);

        if (mTextView != null) {
            mTextView.setText(String.format("%02d:%02d", minutes, seconds));
        }

    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return binder;
    }

    public class LocalBinder extends Binder {
        public ChronometerService getService() {
            return ChronometerService.this;
        }
    }

    public void continuePlaying(TextView editText) {
        this.mTextView = editText;
    }
    public void setFinishListener(OnChronometerFinishListener listener) { this.mFinishListener = listener; }

    public void play(long startTime, Task task, TextView editText) {
        this.mTextView = editText;

        if ( !this.mIsPlaying) {
            this.mIsPlaying = true;

            this.mChronometer = new Chronometer(startTime, getApplicationContext());
            this.mChronometer.setListener(this);
            this.mChronometer.setOnChronometerFinishListener(this);
            this.mChronometer.setTask(task);
            this.mChronometer.start();
        }
    }

    public void play(Task task, TextView editText) {
        this.play(START_TIME, task, editText);
    }

    public boolean isPlaying() { return mIsPlaying; }

    public void stop() {
        if (this.mChronometer != null) {
            this.mChronometer.cancel();
        }

        this.onChronometerFinish(true);
    }

    public void stopService() {
        this.mFinishListener = null;
    }

    @Override
    public void onChronometerFinish(boolean byUser) {
        this.mIsPlaying = false;

        if (this.mFinishListener != null) {
            this.mFinishListener.onChronometerFinish(byUser);
        }
    }

    @Override
    public void onDestroy() {
        stop();
        super.onDestroy();
    }
}
