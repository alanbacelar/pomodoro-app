package br.edu.fa7.pomodoro.fragment;

import android.os.Bundle;
import android.os.CountDownTimer;
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
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.melnykov.fab.FloatingActionButton;

import java.util.ArrayList;
import java.util.List;

import br.edu.fa7.pomodoro.R;
import br.edu.fa7.pomodoro.adapter.TaskAdapter;
import br.edu.fa7.pomodoro.dao.TaskDao;
import br.edu.fa7.pomodoro.model.Task;
import br.edu.fa7.pomodoro.util.Chronometer;
import br.edu.fa7.pomodoro.util.EditModeType;
import br.edu.fa7.pomodoro.util.NotifyAdapter;
import br.edu.fa7.pomodoro.util.RecyclerViewOnClickListener;

/**
 * Created by bruno on 19/08/15.
 */
public class MainFragment extends Fragment implements RecyclerViewOnClickListener, View.OnClickListener, NotifyAdapter {

    private AppCompatActivity mMainActivity;
    private Toolbar mMainToolbar;
    private RecyclerView mRecyclerView;
    private FloatingActionButton mFloatingActionButton;
    private TaskAdapter mTaskAdapter;
    private TaskDao mTaskDao;
    private FragmentTransaction mFragmentTransaction;
    private Chronometer mChronometer;
    private TextView mChronometerView;

    private final int startPomodoroTime = 1500000; // 25min

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View v = inflater.inflate(R.layout.fragment_main, container, false);

        mMainActivity = (AppCompatActivity) getActivity();

        // Adicionando a Toolbar
        mMainToolbar = (Toolbar) mMainActivity.findViewById(R.id.main_toolbar);
        mMainToolbar.setTitle(R.string.app_name);
        mMainActivity.setSupportActionBar(mMainToolbar);
        mMainActivity.getSupportActionBar().setDisplayHomeAsUpEnabled(false);

        mFragmentTransaction = mMainActivity.getSupportFragmentManager().beginTransaction();

        mTaskDao = new TaskDao(mMainActivity);

        this.setupTaskAdapter();
        this.setupRecycleView(v);

        mChronometerView = (TextView) v.findViewById(R.id.text_chronometer);
        mFloatingActionButton = (FloatingActionButton) v.findViewById(R.id.fragment_main_floating_add_button);
        mFloatingActionButton.setOnClickListener(this);
        mFloatingActionButton.attachToRecyclerView(mRecyclerView);

        mChronometer = new Chronometer(startPomodoroTime, mChronometerView);

        return v;
    }

    private void setupRecycleView(View v) {
        LinearLayoutManager llm = new LinearLayoutManager(mMainActivity);

        mRecyclerView = (RecyclerView) v.findViewById(R.id.fragment_main_recyclerview);
        mRecyclerView.setLayoutManager(llm);
        mRecyclerView.setAdapter(mTaskAdapter);
    }

    private void setupTaskAdapter() {
        mTaskAdapter = new TaskAdapter(mMainActivity, mTaskDao.findAll());
        mTaskAdapter.setListener(this);
    }

    @Override
    public void onClick(View v, int position) {
        switch (v.getId()) {
            case R.id.cardview_play_btn :
                toggleChronometer();
                break;

            default:
                Bundle bundle = new Bundle();
                bundle.putSerializable("mode", EditModeType.EDIT_UPDATE_MODE);
                bundle.putLong("id", mTaskAdapter.getItemId(position));

                startFormFragment(bundle);

                break;
        }
    }

    @Override
    public void onClick(View v) {
        Bundle bundle = new Bundle();

        switch (v.getId()) {
            case R.id.fragment_main_floating_add_button :
                bundle.putSerializable("mode", EditModeType.EDIT_NEW_MODE);
                startFormFragment(bundle);
                break;
        }
    }

    private void toggleChronometer() {
        if (mChronometer.isPlayning()) {
            mChronometer.reset();
        } else {
            mChronometer.play();
        }
    }


    private void startFormFragment(Bundle bundle) {
        FormFragment formFragment = new FormFragment();
        formFragment.setArguments(bundle);

        startFragment(formFragment);
    }

    private void startFragment(Fragment fragment) {
        mFragmentTransaction.replace(R.id.main_content, fragment);
        mFragmentTransaction.commit();
    }

    @Override
    public void onResume() {
        super.onResume();
        dataChanged();
    }

    @Override
    public void dataChanged() {
        mTaskAdapter.getList().clear();
        mTaskAdapter.getList().addAll(mTaskDao.findAll());
        mTaskAdapter.notifyDataSetChanged();
    }
}
