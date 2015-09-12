package br.edu.fa7.pomodoro.util;

import android.os.CountDownTimer;
import android.widget.TextView;

import java.util.concurrent.TimeUnit;

/**
 * Created by alan on 9/12/15.
 */
public class Chronometer extends CountDownTimer {
    private TextView mChronometerView;
    private boolean isChronometerStarted = false;

    public Chronometer(long millisInFuture, TextView chronometerView) {
        super(millisInFuture, 1000);
        mChronometerView = chronometerView;
    }

    @Override
    public void onTick(long millisUntilFinished) {
        long minutes = TimeUnit.MILLISECONDS.toMinutes( millisUntilFinished);
        long seconds = TimeUnit.MILLISECONDS.toSeconds(millisUntilFinished) - TimeUnit.MINUTES.toSeconds(TimeUnit.MILLISECONDS.toMinutes(millisUntilFinished));

        mChronometerView.setText(String.format("%02d:%02d", minutes, seconds));
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
        mChronometerView.setText("00:00");
    }

    public boolean isPlayning() {
        return isChronometerStarted;
    }

}
