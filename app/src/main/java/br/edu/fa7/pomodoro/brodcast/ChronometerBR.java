package br.edu.fa7.pomodoro.brodcast;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import br.edu.fa7.pomodoro.listener.ChronometerListener;

/**
 * Created by bruno on 12/08/15.
 */
public class ChronometerBR extends BroadcastReceiver {

    private final String TAG = "ChronometerBR";
    public static final String CHRONOMETER_BROADCAST_RECEIVER = "br.edu.fa7.POMODORO_CHRONOMETER";

    private ChronometerListener mListener;

    public void setListener(ChronometerListener listener) {
        this.mListener = listener;
    }

    @Override
    public void onReceive(Context context, Intent intent) {
        if (intent.getExtras() != null) {
            long millisUntilFinished = intent.getLongExtra("countdown", 0);

            if (this.mListener != null) {
                this.mListener.onTick(millisUntilFinished);
            }

        }
    }

}
