package br.edu.fa7.pomodoro.service;

import android.app.Service;
import android.content.Intent;
import android.os.Binder;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.util.Log;

import br.edu.fa7.pomodoro.util.Chronometer;
import br.edu.fa7.pomodoro.util.ChronometerListener;

/**
 * Created by bruno on 26/08/15.
 */
public class ChronometerService extends Service implements Runnable {

    private IBinder binder;
    private boolean isPlaying = false;
    private Chronometer mChronometer;
    private long mStartTime = 1500000; // 25 min.

    private final String TAG = "ChronometerService";

    public ChronometerService() {
        this.binder = new LocalBinder();
        this.mChronometer = new Chronometer(mStartTime);
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        Log.i(TAG, "onStartCommand");
        new Thread(this).start();
        return Service.START_STICKY;
    }

    public void start() {
        isPlaying = true;
//        mChronometer.start();
    }

    @Override
    public void run() {
        mChronometer.start();
    }

    public void setChronometerListener(ChronometerListener listener) {
        mChronometer.setListener(listener);
    }

    public void setStartTime(long startTime) {
        this.mStartTime = startTime;
    }

    public boolean isPlaying() {
        return isPlaying;
   }

    public void stop() {
        this.isPlaying = false;
        mChronometer.cancel();
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
}
