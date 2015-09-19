package br.edu.fa7.pomodoro.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;


import br.edu.fa7.pomodoro.R;
import br.edu.fa7.pomodoro.activity.MainActivity;
import br.edu.fa7.pomodoro.dao.TaskDao;
import br.edu.fa7.pomodoro.listener.OnChronometerFinishListener;
import br.edu.fa7.pomodoro.model.Task;
import br.edu.fa7.pomodoro.service.ChronometerService;

/**
 * Created by bruno on 19/08/15.
 */
public class ChronometerFragment extends Fragment implements View.OnClickListener, OnChronometerFinishListener {

    private final String TAG = "ChronometerFragment";

    private TextView mChronometerTextView;
    private Button mStopBtn;
    private MainActivity mMainActivity;
    private Toolbar mMainToolbar;
    private Task mTask;
    private TaskDao mTaskDao;
    private ChronometerService mChronometerService;
    private boolean isChronometerServiceBounded;

    private long mTaskID;

    private TextView mTaskTitle;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View v = inflater.inflate(R.layout.fragment_chronometer, container, false);

        mMainActivity = (MainActivity) getActivity();
        mChronometerTextView = (TextView) v.findViewById(R.id.fragment_chronometer_text);

        mStopBtn = (Button) v.findViewById(R.id.fragment_chronometer_btn_stop);
        mStopBtn.setOnClickListener(this);

        mMainToolbar = (Toolbar) mMainActivity.findViewById(R.id.main_toolbar);
        mMainToolbar.setTitle("Chronometer");
        mMainActivity.setSupportActionBar(mMainToolbar);

        mTaskTitle = (TextView) v.findViewById(R.id.fragment_task_title);

        mChronometerService = mMainActivity.getChronometerService();
        isChronometerServiceBounded = mMainActivity.isChronometerServiceBounded();

        if (getArguments() != null && isChronometerServiceBounded) {
            this.mTaskID = getArguments().getLong("taskID");

            mTaskDao = new TaskDao(getActivity());
            this.mTask = mTaskDao.find(this.mTaskID);

            setupChronometer();
        }

        return v;
    }

    private void setupChronometer() {
        mChronometerService.setFinishListener(this);

        if (!mChronometerService.isPlaying()) {
            mChronometerService.play(this.mTask, mChronometerTextView);
        } else {
            mChronometerService.continuePlaying(mChronometerTextView);
        }

        mTaskTitle.setText(mTask.getTitle());
    }

    @Override
    public void onResume() {
        super.onResume();
        mChronometerService.continuePlaying(mChronometerTextView);
    }

    @Override
    public void onChronometerFinish(boolean byUser) {
        if (!byUser) {
            mTask.doneTomatoes(1);
            mTaskDao.update(mTask);
            Log.i(TAG, "Task was update. Done: " + mTask.getDoneTomatoes());
        }

        FragmentTransaction fragmentTransaction = mMainActivity.getSupportFragmentManager().beginTransaction();
        fragmentTransaction.replace(R.id.main_content, new MainFragment());
        fragmentTransaction.commitAllowingStateLoss();
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
