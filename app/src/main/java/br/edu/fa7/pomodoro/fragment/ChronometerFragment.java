package br.edu.fa7.pomodoro.fragment;

import android.app.Service;
import android.content.ComponentName;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.melnykov.fab.FloatingActionButton;

import java.util.concurrent.TimeUnit;

import br.edu.fa7.pomodoro.R;
import br.edu.fa7.pomodoro.activity.MainActivity;
import br.edu.fa7.pomodoro.adapter.TaskAdapter;
import br.edu.fa7.pomodoro.dao.TaskDao;
import br.edu.fa7.pomodoro.service.ChronometerService;
import br.edu.fa7.pomodoro.util.ChronometerListener;
import br.edu.fa7.pomodoro.util.EditModeType;
import br.edu.fa7.pomodoro.util.NotifyAdapter;
import br.edu.fa7.pomodoro.util.RecyclerViewOnClickListener;

/**
 * Created by bruno on 19/08/15.
 */
public class ChronometerFragment extends Fragment implements ChronometerListener, View.OnClickListener {

    private final String TAG = "ChronometerFragment";

    private TextView mChronometerTextView;
    private Button mStopBtn;
    private MainActivity mMainActivity;
    private ChronometerService mChronometerService;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View v = inflater.inflate(R.layout.fragment_chronometer, container, false);

        mMainActivity = (MainActivity) getActivity();
        mChronometerTextView = (TextView) v.findViewById(R.id.fragment_chronometer_text);

        mStopBtn = (Button) v.findViewById(R.id.fragment_chronometer_btn_stop);
        mStopBtn.setOnClickListener(this);

        mChronometerService = mMainActivity.getChronometerService();

        mChronometerService.setChronometerListener(this);

        if (!mChronometerService.isPlaying()) {
            mChronometerService.start();
        }

        return v;
    }


    @Override
    public void onTick(long millisUntilFinished) {
        long minutes = TimeUnit.MILLISECONDS.toMinutes(millisUntilFinished);
        long seconds = TimeUnit.MILLISECONDS.toSeconds(millisUntilFinished) - TimeUnit.MINUTES.toSeconds(TimeUnit.MILLISECONDS.toMinutes(millisUntilFinished));

        mChronometerTextView.setText(String.format("%02d:%02d", minutes, seconds));
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.fragment_chronometer_btn_stop:
                mChronometerService.stop();
                break;
        }
    }
}
