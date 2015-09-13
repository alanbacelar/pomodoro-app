package br.edu.fa7.pomodoro.activity;

import android.app.Service;
import android.content.ComponentName;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.IBinder;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;


import br.edu.fa7.pomodoro.R;
import br.edu.fa7.pomodoro.fragment.ChronometerFragment;
import br.edu.fa7.pomodoro.fragment.MainFragment;
import br.edu.fa7.pomodoro.service.ChronometerService;
import br.edu.fa7.pomodoro.util.ChronometerListener;

public class MainActivity extends AppCompatActivity {

    private final MainFragment mMainFragment;
    private final ChronometerFragment mChronometerFragment;

    private FragmentManager mFragmentManager;
    private ChronometerService mChronometerService;
    private ChronometerListener mChronometerListener;
    private FragmentTransaction mFragmentTransaction;

    private boolean mIsServiceBounded = false;

    public MainActivity() {
        this.mMainFragment = new MainFragment();
        this.mChronometerFragment = new ChronometerFragment();
    }

    public MainFragment getMainFragment() {
        return mMainFragment;
    }

    public void setChronometerListener(ChronometerListener listener) {
        mChronometerListener = listener;
    }

    public boolean isChronometerServiceBounded() {
        return mIsServiceBounded;
    }

    public ServiceConnection getChronometerServiceConnection() {
        return mServiceConnection;
    }

    public ChronometerService getChronometerService() {
        return mChronometerService;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mFragmentManager = getSupportFragmentManager();

        mFragmentTransaction = mFragmentManager.beginTransaction();

        Fragment fragment = (isChronometerServiceBounded() && mChronometerService.isPlaying()) ? mChronometerFragment : mMainFragment;
        mFragmentTransaction.replace(R.id.main_content, fragment);

        mFragmentTransaction.commit();
    }


    @Override
    public void onStart() {
        super.onStart();
        Intent it = new Intent(this, ChronometerService.class);
        bindService(it, mServiceConnection, Service.BIND_AUTO_CREATE);

        if (!isChronometerServiceBounded()) {
            startService(it);
        }

    }


    @Override
    public void onStop() {
        super.onStop();
        unbindService(mServiceConnection);
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        FragmentTransaction fragmentTransaction = mFragmentManager.beginTransaction();

        switch (id){
            case android.R.id.home:
                fragmentTransaction.replace(R.id.main_content, mMainFragment);
                break;
        }

        fragmentTransaction.commit();
        return true;
    }


    private ServiceConnection mServiceConnection = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            ChronometerService.LocalBinder binder = (ChronometerService.LocalBinder) service;
            mChronometerService = binder.getService();
            mChronometerService.setChronometerListener(mChronometerListener);

            mIsServiceBounded = true;
        }

        @Override
        public void onServiceDisconnected(ComponentName name) {
            mIsServiceBounded = false;
        }
    };

}
