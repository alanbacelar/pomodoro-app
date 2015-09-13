package br.edu.fa7.pomodoro.util;

import android.os.CountDownTimer;
import android.util.Log;

/**
 * Created by alan on 9/12/15.
 */
public class Chronometer extends CountDownTimer {
    private boolean isChronometerStarted = false;
    private ChronometerListener mListener;
    private final String TAG = "Chronometer";

    public Chronometer(long millisInFuture) {
        super(millisInFuture, 1000);
    }

    public void setListener(ChronometerListener listener) {
        this.mListener = listener;
    }

    @Override
    public void onTick(long millisUntilFinished) {
//        long minutes = TimeUnit.MILLISECONDS.toMinutes( millisUntilFinished);
//        long seconds = TimeUnit.MILLISECONDS.toSeconds(millisUntilFinished) - TimeUnit.MINUTES.toSeconds(TimeUnit.MILLISECONDS.toMinutes(millisUntilFinished));
//        mChronometerView.setText(String.format("%02d:%02d", minutes, seconds));

        if (this.mListener != null) {
            this.mListener.onTick(millisUntilFinished);
        }

        Log.d(TAG, Long.toString(millisUntilFinished));

    }

    @Override
    public void onFinish() {
        clear();
    }

    public void reset() {
        this.cancel();
        clear();
    }

    public void play() {
        clear();

        this.start();
        isChronometerStarted = true;
    }


    public void clear() {
        isChronometerStarted = false;
    }

    public boolean isPlayning() {
        return isChronometerStarted;
    }

}
